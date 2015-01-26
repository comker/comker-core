package net.cokkee.comker.core.config;

import java.util.LinkedList;

import net.cokkee.comker.base.ComkerBaseConstant;

import net.cokkee.comker.core.config.parts.ComkerCoreSecurityConfig;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerModuleDao;
import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRegistrationDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSettingDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.dao.ComkerWatchdogDao;
import net.cokkee.comker.dao.impl.ComkerCrewDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerModuleDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerNavbarDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerPermissionDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerRegistrationDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerRoleDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerSettingDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerSpotDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerUserDaoHibernate;
import net.cokkee.comker.dao.impl.ComkerWatchdogDaoHibernate;
import net.cokkee.comker.service.ComkerExceptionTransformer;
import net.cokkee.comker.service.ComkerInitializationService;
import net.cokkee.comker.service.ComkerLocalizationService;
import net.cokkee.comker.service.ComkerSecurityContextHolder;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.service.impl.ComkerExceptionTransformerImpl;
import net.cokkee.comker.service.impl.ComkerInitializationSampleData;
import net.cokkee.comker.service.impl.ComkerInitializationServiceImpl;
import net.cokkee.comker.service.impl.ComkerLocalizationServiceImpl;
import net.cokkee.comker.service.impl.ComkerSecurityContextHolderImpl;
import net.cokkee.comker.service.impl.ComkerSecurityServiceImpl;
import net.cokkee.comker.service.impl.ComkerSessionServiceImpl;
import net.cokkee.comker.service.impl.ComkerToolboxServiceImpl;
import net.cokkee.comker.storage.ComkerCrewStorage;
import net.cokkee.comker.storage.ComkerNavbarStorage;
import net.cokkee.comker.storage.ComkerPermissionStorage;
import net.cokkee.comker.storage.ComkerRegistrationStorage;
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.storage.ComkerWatchdogStorage;
import net.cokkee.comker.storage.impl.ComkerCrewStorageImpl;
import net.cokkee.comker.storage.impl.ComkerNavbarStorageImpl;
import net.cokkee.comker.storage.impl.ComkerPermissionStorageImpl;
import net.cokkee.comker.storage.impl.ComkerRegistrationStorageImpl;
import net.cokkee.comker.storage.impl.ComkerRoleStorageImpl;
import net.cokkee.comker.storage.impl.ComkerSpotStorageImpl;
import net.cokkee.comker.storage.impl.ComkerUserStorageImpl;
import net.cokkee.comker.storage.impl.ComkerWatchdogStorageImpl;
import net.cokkee.comker.util.ComkerBootstrapInitializer;
import net.cokkee.comker.util.ComkerBootstrapPublisher;
import net.cokkee.comker.validation.ComkerCrewValidator;
import net.cokkee.comker.validation.ComkerPermissionValidator;
import net.cokkee.comker.validation.ComkerRegistrationValidator;
import net.cokkee.comker.validation.ComkerRoleValidator;
import net.cokkee.comker.validation.ComkerSpotValidator;
import net.cokkee.comker.validation.ComkerUserValidator;
import org.hibernate.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author drupalex
 */
@Configuration
@Import({
    ComkerCoreSecurityConfig.class
})
public class ComkerCoreContextConfig {

    private static final Logger logger = LoggerFactory.getLogger(ComkerCoreContextConfig.class);
    
    public ComkerCoreContextConfig() {
        if (logger.isDebugEnabled()) {
            logger.debug("==@ " + ComkerCoreContextConfig.class.getSimpleName() + " is invoked");
        }
    }
    
    //--------------------------------------------------------------------------
    
    @Bean
    public ComkerInitializationService comkerInitializationService(
            @Qualifier("comkerUserDao") ComkerUserDao comkerUserDao,
            @Qualifier("comkerSpotDao") ComkerSpotDao comkerSpotDao,
            @Qualifier("comkerSettingDao") ComkerSettingDao comkerSettingDao,
            @Qualifier("comkerCrewDao") ComkerCrewDao comkerCrewDao,
            @Qualifier("comkerRoleDao") ComkerRoleDao comkerRoleDao,
            @Qualifier("comkerPermissionDao") ComkerPermissionDao comkerPermissionDao,
            @Qualifier("comkerNavbarDao") ComkerNavbarDao comkerNavbarDao,
            @Qualifier("comkerPasswordEncoder") PasswordEncoder comkerPasswordEncoder) {
        ComkerInitializationServiceImpl bean = new ComkerInitializationServiceImpl();
        bean.setUserDao(comkerUserDao);
        bean.setSpotDao(comkerSpotDao);
        bean.setSettingDao(comkerSettingDao);
        bean.setCrewDao(comkerCrewDao);
        bean.setRoleDao(comkerRoleDao);
        bean.setPermissionDao(comkerPermissionDao);
        bean.setNavbarDao(comkerNavbarDao);
        bean.setPasswordEncoder(comkerPasswordEncoder);
        return bean;
    }
    
    @Bean
    public ComkerInitializationService comkerInitializationSampleData(
            @Qualifier("comkerUserDao") ComkerUserDao comkerUserDao,
            @Qualifier("comkerSpotDao") ComkerSpotDao comkerSpotDao,
            @Qualifier("comkerSettingDao") ComkerSettingDao comkerSettingDao,
            @Qualifier("comkerCrewDao") ComkerCrewDao comkerCrewDao,
            @Qualifier("comkerRoleDao") ComkerRoleDao comkerRoleDao,
            @Qualifier("comkerPermissionDao") ComkerPermissionDao comkerPermissionDao,
            @Qualifier("comkerNavbarDao") ComkerNavbarDao comkerNavbarDao,
            @Qualifier("comkerPasswordEncoder") PasswordEncoder comkerPasswordEncoder) {
        ComkerInitializationSampleData bean = new ComkerInitializationSampleData();
        bean.setUserDao(comkerUserDao);
        bean.setSpotDao(comkerSpotDao);
        bean.setSettingDao(comkerSettingDao);
        bean.setCrewDao(comkerCrewDao);
        bean.setRoleDao(comkerRoleDao);
        bean.setPermissionDao(comkerPermissionDao);
        bean.setNavbarDao(comkerNavbarDao);
        bean.setPasswordEncoder(comkerPasswordEncoder);
        return bean;
    }
    
    @Bean
    public ComkerSecurityService comkerSecurityService(
            @Qualifier("comkerSecurityContextHolder") ComkerSecurityContextHolder comkerSecurityContextHolder,
            @Qualifier("comkerUserStorage") ComkerUserStorage comkerUserStorage,
            @Qualifier("comkerPasswordEncoder") PasswordEncoder comkerPasswordEncoder) {
        ComkerSecurityServiceImpl bean = new ComkerSecurityServiceImpl();
        bean.setsecurityContextHolder(comkerSecurityContextHolder);
        bean.setUserStorage(comkerUserStorage);
        bean.setPasswordEncoder(comkerPasswordEncoder);
        return bean;
    }
    
    @Bean
    public PasswordEncoder comkerPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ComkerSecurityContextHolder comkerSecurityContextHolder() {
        return new ComkerSecurityContextHolderImpl();
    }
    
    @Bean
    public ComkerToolboxService comkerToolboxService() {
        return new ComkerToolboxServiceImpl();
    }
    
    //--------------------------------------------------------------------------
    
    @Bean
    public ComkerRegistrationStorage comkerRegistrationStorage(
            @Qualifier("comkerRegistrationValidator") ComkerRegistrationValidator comkerRegistrationValidator,
            @Qualifier("comkerRegistrationDao") ComkerRegistrationDao comkerRegistrationDao,
            @Qualifier("comkerUserDao") ComkerUserDao comkerUserDao) {
        ComkerRegistrationStorageImpl bean = new ComkerRegistrationStorageImpl();
        bean.setRegistrationValidator(comkerRegistrationValidator);
        bean.setRegistrationDao(comkerRegistrationDao);
        bean.setUserDao(comkerUserDao);
        return bean;
    }
    
    @Bean
    public ComkerUserStorage comkerUserStorage(
            @Qualifier("comkerUserValidator") ComkerUserValidator comkerUserValidator,
            @Qualifier("comkerUserDao") ComkerUserDao comkerUserDao,
            @Qualifier("comkerCrewDao") ComkerCrewDao comkerCrewDao,
            @Qualifier("comkerRoleDao") ComkerRoleDao comkerRoleDao,
            @Qualifier("comkerToolboxService") ComkerToolboxService comkerToolboxService) {
        ComkerUserStorageImpl bean = new ComkerUserStorageImpl();
        bean.setUserValidator(comkerUserValidator);
        bean.setUserDao(comkerUserDao);
        bean.setCrewDao(comkerCrewDao);
        bean.setRoleDao(comkerRoleDao);
        bean.setToolboxService(comkerToolboxService);
        return bean;
    }
    
    @Bean
    public ComkerCrewStorage comkerCrewStorage(
            @Qualifier("comkerCrewValidator") ComkerCrewValidator comkerCrewValidator,
            @Qualifier("comkerCrewDao") ComkerCrewDao comkerCrewDao,
            @Qualifier("comkerSpotDao") ComkerSpotDao comkerSpotDao,
            @Qualifier("comkerRoleDao") ComkerRoleDao comkerRoleDao,
            @Qualifier("comkerToolboxService") ComkerToolboxService comkerToolboxService) {
        ComkerCrewStorageImpl bean = new ComkerCrewStorageImpl();
        bean.setCrewValidator(comkerCrewValidator);
        bean.setCrewDao(comkerCrewDao);
        bean.setSpotDao(comkerSpotDao);
        bean.setRoleDao(comkerRoleDao);
        bean.setToolboxService(comkerToolboxService);
        return bean;
    }
    
    @Bean
    public ComkerSpotStorage comkerSpotStorage(
            @Qualifier("comkerSpotValidator") ComkerSpotValidator comkerSpotValidator,
            @Qualifier("comkerSpotDao") ComkerSpotDao comkerSpotDao,
            @Qualifier("comkerModuleDao") ComkerModuleDao comkerModuleDao,
            @Qualifier("comkerToolboxService") ComkerToolboxService comkerToolboxService) {
        ComkerSpotStorageImpl bean = new ComkerSpotStorageImpl();
        bean.setSpotValidator(comkerSpotValidator);
        bean.setSpotDao(comkerSpotDao);
        bean.setModuleDao(comkerModuleDao);
        bean.setToolboxService(comkerToolboxService);
        return bean;
    }
    
    @Bean
    public ComkerRoleStorage comkerRoleStorage(
            @Qualifier("comkerRoleValidator") ComkerRoleValidator comkerRoleValidator,
            @Qualifier("comkerRoleDao") ComkerRoleDao comkerRoleDao,
            @Qualifier("comkerPermissionDao") ComkerPermissionDao comkerPermissionDao,
            @Qualifier("comkerToolboxService") ComkerToolboxService comkerToolboxService) {
        ComkerRoleStorageImpl bean = new ComkerRoleStorageImpl();
        bean.setRoleValidator(comkerRoleValidator);
        bean.setRoleDao(comkerRoleDao);
        bean.setPermissionDao(comkerPermissionDao);
        bean.setToolboxService(comkerToolboxService);
        return bean;
    }
    
    @Bean
    public ComkerPermissionStorage comkerPermissionStorage(
            @Qualifier("comkerPermissionValidator") ComkerPermissionValidator comkerPermissionValidator,
            @Qualifier("comkerPermissionDao") ComkerPermissionDao comkerPermissionDao,
            @Qualifier("comkerToolboxService") ComkerToolboxService comkerToolboxService) {
        ComkerPermissionStorageImpl bean = new ComkerPermissionStorageImpl();
        bean.setPermissionValidator(comkerPermissionValidator);
        bean.setPermissionDao(comkerPermissionDao);
        bean.setToolboxService(comkerToolboxService);
        return bean;
    }
    
    @Bean
    public ComkerNavbarStorage comkerNavbarStorage(
            @Qualifier("comkerNavbarDao") ComkerNavbarDao comkerNavbarDao) {
        ComkerNavbarStorageImpl bean = new ComkerNavbarStorageImpl();
        bean.setNavbarDao(comkerNavbarDao);
        return bean;
    }
    
    @Bean
    public ComkerWatchdogStorage comkerWatchdogStorage(
            @Qualifier("comkerWatchdogDao") ComkerWatchdogDao comkerWatchdogDao,
            @Qualifier("comkerToolboxService") ComkerToolboxService comkerToolboxService) {
        ComkerWatchdogStorageImpl bean = new ComkerWatchdogStorageImpl();
        bean.setWatchdogDao(comkerWatchdogDao);
        bean.setToolboxService(comkerToolboxService);
        return bean;
    }
    
    //--------------------------------------------------------------------------
    
    @Bean
    public ComkerRegistrationValidator comkerRegistrationValidator(
            @Qualifier("comkerUserDao") ComkerUserDao comkerUserDao) {
        ComkerRegistrationValidator bean = new ComkerRegistrationValidator();
        bean.setUserDao(comkerUserDao);
        return bean;
    }
    
    @Bean
    public ComkerUserValidator comkerUserValidator(
            @Qualifier("comkerUserDao") ComkerUserDao comkerUserDao,
            @Qualifier("comkerCrewDao") ComkerCrewDao comkerCrewDao) {
        ComkerUserValidator bean = new ComkerUserValidator();
        bean.setUserDao(comkerUserDao);
        bean.setCrewDao(comkerCrewDao);
        return bean;
    }
    
    @Bean
    public ComkerCrewValidator comkerCrewValidator(
            @Qualifier("comkerCrewDao") ComkerCrewDao comkerCrewDao,
            @Qualifier("comkerSpotDao") ComkerSpotDao comkerSpotDao,
            @Qualifier("comkerRoleDao") ComkerRoleDao comkerRoleDao) {
        ComkerCrewValidator bean = new ComkerCrewValidator();
        bean.setCrewDao(comkerCrewDao);
        bean.setSpotDao(comkerSpotDao);
        bean.setRoleDao(comkerRoleDao);
        return bean;
    }
    
    @Bean
    public ComkerSpotValidator comkerSpotValidator(
            @Qualifier("comkerSpotDao") ComkerSpotDao comkerSpotDao,
            @Qualifier("comkerModuleDao") ComkerModuleDao comkerModuleDao) {
        ComkerSpotValidator bean = new ComkerSpotValidator();
        bean.setSpotDao(comkerSpotDao);
        bean.setModuleDao(comkerModuleDao);
        return bean;
    }
    
    @Bean
    public ComkerRoleValidator comkerRoleValidator(
            @Qualifier("comkerRoleDao") ComkerRoleDao comkerRoleDao,
            @Qualifier("comkerPermissionDao") ComkerPermissionDao comkerPermissionDao) {
        ComkerRoleValidator bean = new ComkerRoleValidator();
        bean.setRoleDao(comkerRoleDao);
        bean.setPermissionDao(comkerPermissionDao);
        return bean;
    }
    
    @Bean
    public ComkerPermissionValidator comkerPermissionValidator(
            @Qualifier("comkerPermissionDao") ComkerPermissionDao comkerPermissionDao) {
        ComkerPermissionValidator bean = new ComkerPermissionValidator();
        bean.setPermissionDao(comkerPermissionDao);
        return bean;
    }

    //--------------------------------------------------------------------------

    @Bean
    public ComkerRegistrationDao comkerRegistrationDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerRegistrationDaoHibernate bean = new ComkerRegistrationDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    @Bean
    public ComkerSettingDao comkerSettingDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerSettingDaoHibernate bean = new ComkerSettingDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    @Bean
    public ComkerUserDao comkerUserDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerUserDaoHibernate bean = new ComkerUserDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    @Bean
    public ComkerCrewDao comkerCrewDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerCrewDaoHibernate bean = new ComkerCrewDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }
    
    @Bean
    public ComkerRoleDao comkerRoleDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerRoleDaoHibernate bean = new ComkerRoleDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }
    
    @Bean
    public ComkerPermissionDao comkerPermissionDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerPermissionDaoHibernate bean = new ComkerPermissionDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }
    
    @Bean
    public ComkerSpotDao comkerSpotDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerSpotDaoHibernate bean = new ComkerSpotDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }
    
    @Bean
    public ComkerModuleDao comkerModuleDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerModuleDaoHibernate bean = new ComkerModuleDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }
    
    @Bean
    public ComkerNavbarDao comkerNavbarDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerNavbarDaoHibernate bean = new ComkerNavbarDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    @Bean
    public ComkerWatchdogDao comkerWatchdogDao(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        ComkerWatchdogDaoHibernate bean = new ComkerWatchdogDaoHibernate();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }
    
    //--------------------------------------------------------------------------
}
