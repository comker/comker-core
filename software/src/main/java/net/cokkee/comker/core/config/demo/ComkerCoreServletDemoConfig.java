package net.cokkee.comker.core.config.demo;

import net.cokkee.comker.base.ComkerBaseConstant;
import net.cokkee.comker.base.config.ComkerBaseServletConfig;
import net.cokkee.comker.core.config.ComkerCoreServletConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author drupalex
 */
@Configuration
@EnableWebMvc
@Import({
    ComkerBaseServletConfig.class,
    ComkerCoreServletConfig.class
})
@Profile(ComkerBaseConstant.PROFILE_DEMO)
public class ComkerCoreServletDemoConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ComkerCoreServletDemoConfig.class);
    
    public ComkerCoreServletDemoConfig() {
        if (logger.isDebugEnabled()) {
            logger.debug("==@ " + ComkerCoreServletDemoConfig.class.getSimpleName() + " is invoked");
        }
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
}
