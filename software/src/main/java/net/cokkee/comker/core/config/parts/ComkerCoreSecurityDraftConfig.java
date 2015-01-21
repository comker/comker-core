package net.cokkee.comker.core.config.parts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author drupalex
 */
@Configuration
@EnableWebSecurity
public class ComkerCoreSecurityDraftConfig extends WebSecurityConfigurerAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(ComkerCoreSecurityDraftConfig.class);
    
    public ComkerCoreSecurityDraftConfig() {
        if (logger.isDebugEnabled()) {
            logger.debug("==@ " + ComkerCoreSecurityDraftConfig.class.getSimpleName() + " is invoked");
        }
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/mvc/blog/**").hasRole("ADMIN")
                .antMatchers("/mvc/blog").permitAll()
                .antMatchers("/mvc/rest/*").permitAll()
                .antMatchers("/mvc/status", "/mvc/status.txt").permitAll()
            .and()
                .formLogin()
                    .loginPage("/mvc/auth/login")
                    .defaultSuccessUrl("/mvc/blog/posts")
                    .failureUrl("/mvc/auth/login")
                    .usernameParameter("user")
                    .passwordParameter("pwd")
                    .permitAll()
            .and()
                .logout()
                    .logoutUrl("/mvc/auth/logout")
                    .logoutSuccessUrl("/mvc/blog")
                    .permitAll();
    }
}
