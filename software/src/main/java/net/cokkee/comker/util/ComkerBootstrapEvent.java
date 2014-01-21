package net.cokkee.comker.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 *
 * @author drupalex
 */
public class ComkerBootstrapEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = 1L;

    public ComkerBootstrapEvent(ApplicationContext source) {
        super(source);
    }
}
