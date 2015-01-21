package net.cokkee.comker.test.unit.dao;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractDaoUnitTest {
    
    @Configuration
    @PropertySource("classpath:/jdbc.properties")
    @EnableTransactionManagement
    public static class GeneralConfig {
        
        private static final Logger logger = LoggerFactory.getLogger(GeneralConfig.class);
    
        public GeneralConfig() {
            if (logger.isDebugEnabled()) {
                logger.debug("==@ " + GeneralConfig.class.getSimpleName() + " is invoked");
            }
        }
        
        @Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
        
        @Bean
        public HibernateTransactionManager comkerTransactionManager(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            HibernateTransactionManager bean = new HibernateTransactionManager();
            bean.setSessionFactory(sessionFactory);
            return bean;
        }
        
        @Bean(destroyMethod = "close")
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
}
