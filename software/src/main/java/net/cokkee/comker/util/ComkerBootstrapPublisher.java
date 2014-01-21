package net.cokkee.comker.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *
 * @author drupalex
 */
public class ComkerBootstrapPublisher implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        if (isRootApplicationContext(applicationContext)) {
            event.getApplicationContext().publishEvent(new ComkerBootstrapEvent(applicationContext));
        }
    }

    private boolean isRootApplicationContext(ApplicationContext applicationContext) {
        return applicationContext.getParent() == null;
    }
}
