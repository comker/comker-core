package net.cokkee.comker.core.config.parts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author drupalex
 */
@Configuration
@ImportResource({
    "classpath:META-INF/bootstrap/comker/security-context.xml"
})
public class ComkerCoreSecurityConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(ComkerCoreSecurityConfig.class);
    
    public ComkerCoreSecurityConfig() {
        if (logger.isDebugEnabled()) {
            logger.debug("==@ " + ComkerCoreSecurityConfig.class.getSimpleName() + " is invoked");
        }
    }
}
