package net.cokkee.comker.core.config;

import net.cokkee.comker.controller.ComkerAdmCrewController;
import net.cokkee.comker.controller.ComkerAdmPermissionController;
import net.cokkee.comker.controller.ComkerAdmRoleController;
import net.cokkee.comker.controller.ComkerAdmSpotController;
import net.cokkee.comker.controller.ComkerAdmUserController;
import net.cokkee.comker.controller.ComkerAdmWatchdogController;
import net.cokkee.comker.controller.ComkerRegistrationController;
import net.cokkee.comker.controller.ComkerSessionController;
import net.cokkee.comker.service.ComkerExceptionTransformer;
import net.cokkee.comker.service.ComkerLocalizationService;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.service.impl.ComkerExceptionTransformerImpl;
import net.cokkee.comker.service.impl.ComkerLocalizationServiceImpl;
import net.cokkee.comker.service.impl.ComkerSessionServiceImpl;
import net.cokkee.comker.storage.ComkerCrewStorage;
import net.cokkee.comker.storage.ComkerPermissionStorage;
import net.cokkee.comker.storage.ComkerRegistrationStorage;
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.storage.ComkerWatchdogStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author drupalex
 */
@Configuration
@EnableWebMvc
public class ComkerCoreServletConfig {

    private static final Logger logger = LoggerFactory.getLogger(ComkerCoreServletConfig.class);

    public ComkerCoreServletConfig() {
        if (logger.isDebugEnabled()) {
            logger.debug("==@ " + ComkerCoreServletConfig.class.getSimpleName() + " is invoked");
        }
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public ComkerSessionService comkerSessionService() {
        return new ComkerSessionServiceImpl();
    }
    
    @Bean
    public ComkerSessionController comkerSessionController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerSecurityService") ComkerSecurityService comkerSecurityService) {
        ComkerSessionController controller = new ComkerSessionController();
        controller.setSecurityService(comkerSecurityService);
        return controller;
    }
    
    @Bean
    public ComkerRegistrationController comkerRegistrationController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerRegistrationStorage") ComkerRegistrationStorage comkerRegistrationStorage) {
        ComkerRegistrationController controller = new ComkerRegistrationController();
        controller.setRegistrationStorage(comkerRegistrationStorage);
        return controller;
    }
    
    @Bean
    public ComkerAdmUserController comkerAdmUserController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerUserStorage") ComkerUserStorage comkerUserStorage) {
        ComkerAdmUserController controller = new ComkerAdmUserController();
        controller.setUserStorage(comkerUserStorage);
        return controller;
    }
    
    @Bean
    public ComkerAdmCrewController comkerAdmCrewController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerCrewStorage") ComkerCrewStorage comkerCrewStorage) {
        ComkerAdmCrewController controller = new ComkerAdmCrewController();
        controller.setCrewStorage(comkerCrewStorage);
        return controller;
    }
    
    @Bean
    public ComkerAdmSpotController comkerAdmSpotController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerSpotStorage") ComkerSpotStorage comkerSpotStorage) {
        ComkerAdmSpotController controller = new ComkerAdmSpotController();
        controller.setSpotStorage(comkerSpotStorage);
        return controller;
    }
    
    @Bean
    public ComkerAdmRoleController comkerAdmRoleController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerRoleStorage") ComkerRoleStorage comkerRoleStorage) {
        ComkerAdmRoleController controller = new ComkerAdmRoleController();
        controller.setRoleStorage(comkerRoleStorage);
        return controller;
    }
    
    @Bean
    public ComkerAdmPermissionController comkerAdmPermissionController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerPermissionStorage") ComkerPermissionStorage comkerPermissionStorage) {
        ComkerAdmPermissionController controller = new ComkerAdmPermissionController();
        controller.setPermissionStorage(comkerPermissionStorage);
        return controller;
    }
    
    @Bean
    public ComkerAdmWatchdogController comkerAdmWatchdogController(
            @Qualifier("comkerSessionService") ComkerSessionService comkerSessionService,
            @Qualifier("comkerWatchdogStorage") ComkerWatchdogStorage comkerWatchdogStorage) {
        ComkerAdmWatchdogController controller = new ComkerAdmWatchdogController();
        controller.setWatchdogStorage(comkerWatchdogStorage);
        return controller;
    }
    
    @Bean
    public ComkerExceptionTransformer comkerExceptionTransformer(
            @Qualifier("comkerLocalizationService") 
                    ComkerLocalizationService baseLocalizationService) {
        ComkerExceptionTransformerImpl bean = new ComkerExceptionTransformerImpl();
        bean.setLocalizationService(baseLocalizationService);
        return bean;
    }

    @Bean
    public ComkerLocalizationService comkerLocalizationService(
            @Qualifier("comkerMessageSource") 
                    MessageSource comkerMessageSource) {
        ComkerLocalizationServiceImpl bean = new ComkerLocalizationServiceImpl();
        bean.setMessageSource(comkerMessageSource);
        return bean;
    }
}
