package net.cokkee.comker.util;

import java.util.List;
import net.cokkee.comker.service.ComkerInitializationService;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
/**
 *
 * @author drupalex
 */
public class ComkerBootstrapInitializer implements ApplicationListener<ComkerBootstrapEvent>, Ordered {

    private ComkerInitializationService defaultInitializationService;

    public void setDefaultInitializationService(ComkerInitializationService initializationService) {
        this.defaultInitializationService = initializationService;
    }

    private List<ComkerInitializationService> initializationServices;

    public void setInitializationServices(List<ComkerInitializationService> initializationServices) {
        this.initializationServices = initializationServices;
    }

    @Override
    public void onApplicationEvent(ComkerBootstrapEvent e) {
        if (defaultInitializationService != null) {
            defaultInitializationService.init();
        }
        if (initializationServices != null) {
            for(ComkerInitializationService service:initializationServices) {
                service.init();
            }
        }
    }

    @Override
    public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
