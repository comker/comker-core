package net.cokkee.comker.core.config.demo;

import java.util.LinkedList;
import java.util.Properties;
import javax.sql.DataSource;
import net.cokkee.comker.base.ComkerBaseConstant;
import net.cokkee.comker.base.config.ComkerBaseContextConfig;
import net.cokkee.comker.base.service.ComkerBaseInitializationService;
import net.cokkee.comker.core.config.ComkerCoreContextConfig;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author drupalex
 */
@Configuration
@Import({
    ComkerBaseContextConfig.class,
    ComkerCoreContextConfig.class
})
@EnableTransactionManagement
@PropertySource("file:src/main/webapp/WEB-INF/jdbc.properties")
@Profile(ComkerBaseConstant.PROFILE_DEMO)
public class ComkerCoreContextDemoConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(ComkerCoreContextDemoConfig.class);
    
    public ComkerCoreContextDemoConfig() {
        if (logger.isDebugEnabled()) {
            logger.debug("==@ " + ComkerCoreContextDemoConfig.class.getSimpleName() + " is invoked");
        }
    }
 
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public LinkedList<ComkerBaseInitializationService> comkerInitializationServiceList(
            @Qualifier("comkerInitializationService") 
                    ComkerBaseInitializationService comkerInitializationService,
            @Qualifier("comkerInitializationSampleData") 
                    ComkerBaseInitializationService comkerInitializationSampleData) {
        LinkedList<ComkerBaseInitializationService> list = new LinkedList<ComkerBaseInitializationService>();
        list.add(comkerInitializationService);
        list.add(comkerInitializationSampleData);
        return list;
    }
    
    @Bean
    public HibernateTransactionManager comkerTransactionManager(
            @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
        HibernateTransactionManager bean = new HibernateTransactionManager();
        bean.setSessionFactory(sessionFactory);
        return bean;
    }

    @Bean
    public AnnotationSessionFactoryBean comkerSessionFactory(
            @Qualifier("comkerDataSource") DataSource dataSource,
            @Qualifier("comkerHibernateProperties") Properties hibernateProperties) {
        AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
        asfb.setAnnotatedClasses(
                net.cokkee.comker.model.dpo.ComkerPermissionDPO.class,
                net.cokkee.comker.model.dpo.ComkerRoleDPO.class,
                net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionDPO.class,
                net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionPK.class,
                net.cokkee.comker.model.dpo.ComkerModuleDPO.class,
                net.cokkee.comker.model.dpo.ComkerSpotDPO.class,
                net.cokkee.comker.model.dpo.ComkerSpotJoinModuleDPO.class,
                net.cokkee.comker.model.dpo.ComkerSpotJoinModulePK.class,
                net.cokkee.comker.model.dpo.ComkerCrewDPO.class,
                net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRoleDPO.class,
                net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRolePK.class,
                net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotDPO.class,
                net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotPK.class,
                net.cokkee.comker.model.dpo.ComkerUserDPO.class,
                net.cokkee.comker.model.dpo.ComkerUserJoinCrewDPO.class,
                net.cokkee.comker.model.dpo.ComkerUserJoinCrewPK.class,
                net.cokkee.comker.model.dpo.ComkerNavbarNodeDPO.class,
                net.cokkee.comker.model.dpo.ComkerSettingEntryDPO.class,
                net.cokkee.comker.model.dpo.ComkerSettingEntryPK.class,
                net.cokkee.comker.model.dpo.ComkerSettingKeyDPO.class,
                net.cokkee.comker.model.dpo.ComkerWatchdogDPO.class);
        asfb.setDataSource(dataSource);
        asfb.setHibernateProperties(hibernateProperties);
        return asfb;
    }
        
    @Bean
    public DataSource comkerDataSource(
            @Value("${jdbc.driverClassName}") String driverClassName,
            @Value("${jdbc.url}") String url,
            @Value("${jdbc.username}") String username,
            @Value("${jdbc.password}") String password) {
            BasicDataSource delegate = new BasicDataSource();
        delegate.setDriverClassName(driverClassName);
        delegate.setUrl(url);
        delegate.setUsername(username);
        delegate.setPassword(password);
        return delegate;
    }

    @Bean
    public Properties comkerHibernateProperties(
            @Value("${hibernate.dialect}") String hibernateDialect,
            @Value("${hibernate.show_sql}") String hibernateShowSql,
            @Value("${hibernate.hbm2ddl.auto}") String hibernateHbm2ddlAuto) {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        return properties;
    }
}
