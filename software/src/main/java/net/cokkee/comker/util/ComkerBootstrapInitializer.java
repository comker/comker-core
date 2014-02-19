package net.cokkee.comker.util;

import net.cokkee.comker.service.ComkerInitializationService;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
/**
 *
 * @author drupalex
 */
public class ComkerBootstrapInitializer implements ApplicationListener<ComkerBootstrapEvent>, Ordered {

    private ComkerInitializationService initializationService;

    public ComkerInitializationService getInitializationService() {
        return initializationService;
    }

    public void setInitializationService(ComkerInitializationService initializationService) {
        this.initializationService = initializationService;
    }

    @Override
    public void onApplicationEvent(ComkerBootstrapEvent e) {
        getInitializationService().initComkerApplication();
        getInitializationService().initDemonstrationData();
    }

    @Override
    public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
