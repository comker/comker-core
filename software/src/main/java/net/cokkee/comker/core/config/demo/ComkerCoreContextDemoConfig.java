package net.cokkee.comker.core.config.demo;

import java.util.LinkedList;
import java.util.Properties;
import javax.sql.DataSource;
import net.cokkee.comker.base.ComkerBaseConstant;
import net.cokkee.comker.base.service.ComkerBaseInitializationService;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author drupalex
 */
@Configuration
@Import({
    net.cokkee.comker.base.config.ComkerBaseContextConfig.class,
    net.cokkee.comker.msg.config.ComkerMsgContextConfig.class,
    net.cokkee.comker.core.config.ComkerCoreContextConfig.class
})
@EnableTransactionManagement
@PropertySource({
    "file:src/main/webapp/WEB-INF/jdbc.properties",
    "file:src/main/webapp/WEB-INF/smtp.properties"
})
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
    
    //--------------------------------------------------------------------------
    // Msg Services
    //--------------------------------------------------------------------------
    
    @Bean
    public LinkedList<JavaMailSender> comkerJavaMailSenderList(
            @Qualifier("comkerJavaMailSenderGmail1") JavaMailSender mailSender1,
            @Qualifier("comkerJavaMailSenderGmail2") JavaMailSender mailSender2,
            @Qualifier("comkerJavaMailSenderGmail3") JavaMailSender mailSender3) {
        LinkedList<JavaMailSender> list = new LinkedList<JavaMailSender>();
        list.add(mailSender1);
        list.add(mailSender2);
        list.add(mailSender3);
        return list;
    }
    
    @Bean
    public JavaMailSender comkerJavaMailSenderGmail1(
            @Value("${gmail.host}") String host,
            @Value("${gmail.port}") String port,
            @Value("${gmail_1.username}") String username,
            @Value("${gmail_1.password}") String password) {
        return createJavaMailSender(host, port, username, password);
    }
    
    @Bean
    public JavaMailSender comkerJavaMailSenderGmail2(
            @Value("${gmail.host}") String host,
            @Value("${gmail.port}") String port,
            @Value("${gmail_2.username}") String username,
            @Value("${gmail_2.password}") String password) {
        return createJavaMailSender(host, port, username, password);
    }
    
    @Bean
    public JavaMailSender comkerJavaMailSenderGmail3(
            @Value("${gmail.host}") String host,
            @Value("${gmail.port}") String port,
            @Value("${gmail_3.username}") String username,
            @Value("${gmail_3.password}") String password) {
        return createJavaMailSender(host, port, username, password);
    }
    
    private JavaMailSender createJavaMailSender(String host, String port, String username, String password) {
        JavaMailSenderImpl bean = new JavaMailSenderImpl();
        bean.setHost(host);
        bean.setPort(Integer.parseInt(port));
        bean.setUsername(username);
        bean.setPassword(password);
        
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.socketFactory.port", port);
        javaMailProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.setProperty("mail.smtp.socketFactory.fallback", "false");
        javaMailProperties.setProperty("mail.smtp.debug", "true");
        
        bean.setJavaMailProperties(javaMailProperties);
        
        return bean;
    }
    
    //--------------------------------------------------------------------------
    // Common services (be used in all modules)
    //--------------------------------------------------------------------------
    
    @Bean
    public ThreadPoolTaskExecutor comkerTaskExecutor() {
        ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
        bean.setCorePoolSize(5);
        bean.setMaxPoolSize(100);
        bean.setQueueCapacity(3000);
        return bean;
    }
    
    @Bean
    public MessageSource comkerMessageSource() {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasenames(
                "classpath:comker-base/messages",
                "classpath:comker-msg/messages",
                "classpath:comker-core/messages");
        return bean;
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
                net.cokkee.comker.model.dpo.ComkerRegistrationDPO.class,
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
