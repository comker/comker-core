package net.cokkee.comker.test.unit.dao;

import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import net.cokkee.comker.dao.ComkerSettingDao;
import net.cokkee.comker.dao.impl.ComkerSettingDaoHibernate;
import net.cokkee.comker.model.dpo.ComkerSettingEntryDPO;
import net.cokkee.comker.model.dpo.ComkerSettingEntryPK;
import net.cokkee.comker.model.dpo.ComkerSettingKeyDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {
            ComkerSettingDaoUnitTest.GeneralConfig.class,
            ComkerSettingDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerSettingDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory = null;

    @Autowired
    @Qualifier("comkerSettingDao")
    private ComkerSettingDao testSettingDao = null;
    
    @Before
    public void init() {
        // init seed spots
        ComkerSpotDPO spotUnknown = getUnknownSpot();
        ComkerSpotDPO spotDefault = getDefaultSpot();

        // init seed users
        ComkerUserDPO userUnknown = getUnknownUser();
        ComkerUserDPO userDefault = getDefaultUser();

        // init settings
        ComkerSettingKeyDPO sKey = null;

        sKey = getOrCreateSettingKey("APPLICATION_SETTING_", ComkerSettingKeyDPO.TYPE_STRING, "");
        defineValue(spotUnknown, userUnknown, sKey, "Default Value of Application Setting ");
    }

    @Test
    public void test_something() {

    }

    //--------------------------------------------------------------------------

    private ComkerSpotDPO getOrCreateSpot(String code, String name, String description) {
        Session session = testSessionFactory.getCurrentSession();

        Criteria c = session.createCriteria(ComkerSpotDPO.class);
        c.add(Restrictions.eq("code", code));
        List<ComkerSpotDPO> result = c.list();
        ComkerSpotDPO item = (result.isEmpty()) ? null:result.get(0);

        if (item == null) {
            item = new ComkerSpotDPO(code, name, description);
            session.saveOrUpdate(item);
        }
        return item;
    }

    private ComkerSpotDPO getUnknownSpot() {
        return getOrCreateSpot(ComkerSpotDPO.UNKNOWN, "UNKNOWN SPOT", "");
    }

    private ComkerSpotDPO getDefaultSpot() {
        return getOrCreateSpot(ComkerSpotDPO.DEFAULT, "DEFAULT SPOT", "");
    }


    private ComkerUserDPO getOrCreateUser(String email, String username, String password, String fullname){
        Session session = testSessionFactory.getCurrentSession();

        Criteria c = session.createCriteria(ComkerUserDPO.class);
        c.add(Restrictions.eq("username", username));
        List<ComkerUserDPO> result = c.list();
        ComkerUserDPO item = (result.isEmpty()) ? null:result.get(0);

        if (item == null) {
            item = new ComkerUserDPO(email, username, password, fullname);
            session.saveOrUpdate(item);
        }
        return item;
    }

    private ComkerUserDPO getUnknownUser(){
        return getOrCreateUser("unknown@cokkee.net", ComkerUserDPO.UNKNOWN, "ff808181442e048701442e04aa710008", "UNKNOWN USER");
    }

    private ComkerUserDPO getDefaultUser(){
        return getOrCreateUser("default@cokkee.net", ComkerUserDPO.DEFAULT, "ff808181442e048701442e04aa610006", "DEFAULT USER");
    }


    private ComkerSettingKeyDPO getOrCreateSettingKey(String code, String type, String range) {
        Session session = testSessionFactory.getCurrentSession();

        Criteria c = session.createCriteria(ComkerSettingKeyDPO.class);
        c.add(Restrictions.eq("code", code));
        List<ComkerSettingKeyDPO> result = c.list();
        ComkerSettingKeyDPO item = (result.isEmpty()) ? null:result.get(0);

        if (item == null) {
            item = new ComkerSettingKeyDPO(code, type, range);
            session.saveOrUpdate(item);
        }
        return item;
    }

    private void defineValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, String defaultValue) {
        Session session = testSessionFactory.getCurrentSession();
        ComkerSettingEntryPK pk = new ComkerSettingEntryPK(key, spot, user);
        ComkerSettingEntryDPO item = (ComkerSettingEntryDPO) session.get(ComkerSettingEntryDPO.class, pk);
        if (item == null) {
            item = new ComkerSettingEntryDPO(key, spot, user);
        }
        item.setValue(defaultValue);
        session.saveOrUpdate(item);
    }
    
    @Configuration
    public static class ContextConfig {
        
        private static final Logger logger = LoggerFactory.getLogger(ContextConfig.class);
    
        public ContextConfig() {
            if (logger.isDebugEnabled()) {
                logger.debug("==@ " + ContextConfig.class.getSimpleName() + " is invoked");
            }
        }
        
        @Bean
        public ComkerSettingDao comkerSettingDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerSettingDaoHibernate bean = new ComkerSettingDaoHibernate();
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
                    net.cokkee.comker.model.dpo.ComkerSettingEntryDPO.class,
                    net.cokkee.comker.model.dpo.ComkerSettingEntryPK.class,
                    net.cokkee.comker.model.dpo.ComkerSettingKeyDPO.class);
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
