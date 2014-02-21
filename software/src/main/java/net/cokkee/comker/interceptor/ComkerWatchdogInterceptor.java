package net.cokkee.comker.interceptor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.dao.ComkerWatchdogDao;
import net.cokkee.comker.model.po.ComkerAbstractItem;
import net.cokkee.comker.model.po.ComkerWatchdog;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerToolboxService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

/**
 *
 * @author drupalex
 */
public class ComkerWatchdogInterceptor implements MethodInterceptor {

    private static Logger log = LoggerFactory.getLogger(ComkerWatchdogInterceptor.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerWatchdogDao watchdogDao = null;

    public ComkerWatchdogDao getWatchdogDao() {
        return watchdogDao;
    }

    public void setWatchdogDao(ComkerWatchdogDao watchdogDao) {
        this.watchdogDao = watchdogDao;
    }

    private TaskExecutor taskExecutor = null;

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }


    private ComkerSecurityService securityService = null;

    public ComkerSecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(ComkerSecurityService securityService) {
        this.securityService = securityService;
    }


    private ComkerToolboxService toolboxService = null;

    public ComkerToolboxService getToolboxService() {
        return toolboxService;
    }

    public void setToolboxService(ComkerToolboxService toolboxService) {
        this.toolboxService = toolboxService;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object result = null;

        ComkerWatchdog item = new ComkerWatchdog();
        item.setHitTime(getToolboxService().getTime());
        
        try {
            long start = getToolboxService().getTimeInMillis();
            result = invocation.proceed();
            long end = getToolboxService().getTimeInMillis();
            item.setHitDuration(end - start);

            item.setHitState(ComkerWatchdog.HIT_STATE_SUCCESS);
        } catch (RuntimeException e) {
            item.setHitState(ComkerWatchdog.HIT_STATE_FAILURE);
            if (log.isErrorEnabled()) {
                log.error(MessageFormat.format(
                        "ComkerWatchInterceptor.invoke() - Exception:{0} / Message:{1}",
                        new Object[] { e.getClass().getName(), e.getMessage()}));
            }
            throw e;
        } finally {
            getTaskExecutor().execute(new WriteWatchdogTask(invocation, item));
        }

        return result;
    }

    private class WriteWatchdogTask implements Runnable {

        private MethodInvocation method;
        private ComkerWatchdog item;

        public WriteWatchdogTask(MethodInvocation method, ComkerWatchdog item) {
            this.method = method;
            this.item = item;
        }

        @Override
        public void run() {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("WriteWatchdogTask.run() - create WatchLog");
                }
                extractUserInfo(item);
                extractMethodInfo(method, item);
                getWatchdogDao().create(item);
            } catch (RuntimeException e) {
                if (log.isErrorEnabled()) {
                    log.error(MessageFormat.format(
                            "WriteWatchdogTask.run() - Exception:{0} / Message:{1}",
                            new Object[] { e.getClass().getName(), e.getMessage()}));
                }
            }
        }

        private void extractUserInfo(ComkerWatchdog item) {
            item.setUsername(getSecurityService().getUsername());
        }
        
        private void extractMethodInfo(MethodInvocation invocation, ComkerWatchdog watchLog) {
            String className = invocation.getMethod().getDeclaringClass().getName();
            String methodName = invocation.getMethod().getName();
            Object[] params = invocation.getArguments();

            watchLog.setMethodName(MessageFormat.format("{0}.{1}", new Object[] {className, methodName}));

            List objs = new ArrayList();
            for(int i=0; (params!=null) && (i<params.length); i++) {
                if (params[i] instanceof ComkerAbstractItem ||
                        params[i] instanceof String ||
                        params[i] instanceof Integer) {
                    objs.add(params[i]);
                } else {
                    objs.add("__unknown__");
                }
            }
            watchLog.setMethodArgs(getToolboxService().convertEntityToJson(objs));

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
