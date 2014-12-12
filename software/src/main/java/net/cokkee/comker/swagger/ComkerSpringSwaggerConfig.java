package net.cokkee.comker.swagger;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.paths.RelativeSwaggerPathProvider;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

import com.wordnik.swagger.model.ApiInfo;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author drupalex
 */
@EnableSwagger
@PropertySource("classpath:swagger.properties")
@ComponentScan("net.cokkee.comker.controller")
public class ComkerSpringSwaggerConfig implements ServletContextAware {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    private SpringSwaggerConfig springSwaggerConfig;

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }
    
    private ServletContext servletContext;
    
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Bean //Don't forget the @Bean annotation
    public SwaggerSpringMvcPlugin customImplementation() {
        ApiInfo apiInfo = new ApiInfo(
                "Comker Core Module",
                "Comker module provides System Monitor and Administration.",
                "My Apps API terms of service",
                "pnhung177@gmail.com",
                "My Apps API Licence Type",
                "My Apps API License URL"
        );
        
        SwaggerPathProvider pathProvider = new RelativeSwaggerPathProvider(servletContext);
        pathProvider.setApiResourcePrefix(environment.getProperty("swagger.resource_prefix"));
        
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo)
                .pathProvider(pathProvider);
    }
}
