package net.cokkee.comker.interceptor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.cokkee.comker.base.util.ComkerBaseDataUtil;
import net.cokkee.comker.dao.ComkerWatchdogDao;
import net.cokkee.comker.model.dpo.ComkerWatchdogDPO;
import net.cokkee.comker.model.dto.ComkerAbstractDTO;
import net.cokkee.comker.service.ComkerSecurityContextReader;
import net.cokkee.comker.service.ComkerToolboxService;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerWatchdogInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ComkerWatchdogInterceptor.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerWatchdogDao watchdogDao = null;

    public void setWatchdogDao(ComkerWatchdogDao watchdogDao) {
        this.watchdogDao = watchdogDao;
    }

    
    private TaskExecutor taskExecutor = null;

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }


    private ComkerSecurityContextReader securityContextReader = null;

    public void setSecurityContextReader(ComkerSecurityContextReader securityContextReader) {
        this.securityContextReader = securityContextReader;
    }


    private ComkerToolboxService toolboxService = null;

    public void setToolboxService(ComkerToolboxService toolboxService) {
        this.toolboxService = toolboxService;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object result = null;

        ComkerWatchdogDPO item = new ComkerWatchdogDPO();
        item.setHitTime(toolboxService.getTime());
        
        try {
            long start = toolboxService.getTimeInMillis();
            result = invocation.proceed();
            long end = toolboxService.getTimeInMillis();
            item.setHitDuration(end - start);

            item.setHitState(ComkerWatchdogDPO.HIT_STATE_SUCCESS);
        } catch (RuntimeException e) {
            item.setHitState(ComkerWatchdogDPO.HIT_STATE_FAILURE);
            if (log.isErrorEnabled()) {
                log.error(MessageFormat.format(
                        "ComkerWatchInterceptor.invoke() - Exception:{0} / Message:{1}",
                        new Object[] { e.getClass().getName(), e.getMessage()}));
            }
            throw e;
        } finally {
            this.taskExecutor.execute(new WriteWatchdogTask(invocation, item));
        }

        return result;
    }

    private class WriteWatchdogTask implements Runnable {

        private final MethodInvocation method;
        private final ComkerWatchdogDPO item;

        public WriteWatchdogTask(MethodInvocation method, ComkerWatchdogDPO item) {
            this.method = method;
            this.item = item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void run() {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("WriteWatchdogTask.run() - create WatchLog");
                }
                extractUserInfo(item);
                extractMethodInfo(method, item);
                watchdogDao.create(item);
            } catch (RuntimeException e) {
                if (log.isErrorEnabled()) {
                    log.error(MessageFormat.format(
                            "WriteWatchdogTask.run() - Exception:{0} / Message:{1}",
                            new Object[] { e.getClass().getName(), e.getMessage()}));
                }
            }
        }

        @Transactional(propagation = Propagation.REQUIRED)
        private void extractUserInfo(ComkerWatchdogDPO item) {
            item.setUsername(securityContextReader.getUsername());
        }
        
        @Transactional(propagation = Propagation.REQUIRED)
        private void extractMethodInfo(MethodInvocation invocation, ComkerWatchdogDPO watchLog) {
            String className = invocation.getMethod().getDeclaringClass().getName();
            String methodName = invocation.getMethod().getName();
            Object[] params = invocation.getArguments();

            watchLog.setMethodName(MessageFormat.format("{0}.{1}", new Object[] {className, methodName}));
            
            if (true) {
                watchLog.setMethodArgs(ComkerBaseDataUtil.convertObjectToJson(params));
            } else {
                List<Object> objs = new ArrayList<Object>();
                for(int i=0; (params!=null) && (i<params.length); i++) {
                    if (params[i] instanceof ComkerAbstractDTO ||
                            params[i] instanceof String ||
                            params[i] instanceof Integer) {
                        objs.add(params[i]);
                    } else {
                        objs.add("__unknown__");
                    }
                }
                watchLog.setMethodArgs(ComkerBaseDataUtil.convertObjectToJson(objs));
            }

            if (methodName.matches(".*(create|update).*")) {
                if (log.isDebugEnabled()) {
                    log.debug(MessageFormat.format(
                            "WriteWatchdogTask.extractMethodInfo() - create/update",
                            new Object[] {}));
                }
            }
        }
    }
}
