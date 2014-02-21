package net.cokkee.comker.interceptor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.dao.ComkerWatchDao;
import net.cokkee.comker.model.po.ComkerAbstractItem;
import net.cokkee.comker.model.po.ComkerWatchLog;
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
public class ComkerWatchInterceptor implements MethodInterceptor {

    private static Logger log = LoggerFactory.getLogger(ComkerWatchInterceptor.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerWatchDao watchDao = null;

    public ComkerWatchDao getWatchDao() {
        return watchDao;
    }

    public void setWatchDao(ComkerWatchDao watchDao) {
        this.watchDao = watchDao;
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

        ComkerWatchLog item = new ComkerWatchLog();
        item.setHitTime(getToolboxService().getTime());
        
        try {
            long start = getToolboxService().getTimeInMillis();
            result = invocation.proceed();
            long end = getToolboxService().getTimeInMillis();
            item.setHitDuration(end - start);

            item.setHitState(ComkerWatchLog.HIT_STATE_SUCCESS);
        } catch (RuntimeException e) {
            item.setHitState(ComkerWatchLog.HIT_STATE_FAILURE);
            if (log.isErrorEnabled()) {
                log.error(MessageFormat.format(
                        "ComkerWatchInterceptor.invoke() - Exception:{0} / Message:{1}",
                        new Object[] { e.getClass().getName(), e.getMessage()}));
            }
            throw e;
        } finally {
            getTaskExecutor().execute(new WriteWatchLogTask(invocation, item));
        }

        return result;
    }

    private class WriteWatchLogTask implements Runnable {

        private MethodInvocation method;
        private ComkerWatchLog item;

        public WriteWatchLogTask(MethodInvocation method, ComkerWatchLog item) {
            this.method = method;
            this.item = item;
        }

        @Override
        public void run() {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("WriteWatchLogTask.run() - create WatchLog");
                }
                extractUserInfo(item);
                extractMethodInfo(method, item);
                getWatchDao().create(item);
            } catch (RuntimeException e) {
                if (log.isErrorEnabled()) {
                    log.error(MessageFormat.format(
                            "WriteWatchLogTask.run() - Exception:{0} / Message:{1}",
                            new Object[] { e.getClass().getName(), e.getMessage()}));
                }
            }
        }

        private void extractUserInfo(ComkerWatchLog item) {
            item.setUsername(getSecurityService().getUsername());
        }
        
        private void extractMethodInfo(MethodInvocation invocation, ComkerWatchLog watchLog) {
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
                            "WriteWatchLogTask.extractMethodInfo() - create/update",
                            new Object[] {}));
                }
            }
        }
    }
}
