package net.cokkee.comker.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.exception.ComkerFieldDuplicatedException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.model.po.ComkerUserJoinCrew;
import net.cokkee.comker.model.po.ComkerUserJoinCrewPk;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerUserDao extends ComkerAbstractDao {

    ComkerUser findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    ComkerUser get(String id);

    ComkerUser getByEmail(String email);

    ComkerUser getByUsername(String username);

    ComkerUser create(ComkerUser item);

    ComkerUser update(ComkerUser item);

    void delete(ComkerUser item);

    void delete(String id);

    void addCrew(ComkerUser user, ComkerCrew crew);

    void removeCrew(ComkerUser user, ComkerCrew crew);

    Set<String> getCodeOfGlobalPermission(ComkerUser user);

    Map<String,Set<String>> getCodeOfSpotWithPermission(ComkerUser user);

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public static class Hibernate extends ComkerAbstractDao.Hibernate
            implements ComkerUserDao {

        private ComkerCrewDao crewDao = null;

        public ComkerCrewDao getCrewDao() {
            return crewDao;
        }

        public void setCrewDao(ComkerCrewDao crewDao) {
            this.crewDao = crewDao;
        }

        //----------------------------------------------------------------------
        
        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerUser findWhere(Map<String, Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerUser.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            ComkerUser item = (ComkerUser)c.uniqueResult();
            loadAggregationRefs(item);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List findAllWhere(Map<String,Object> params, ComkerPager filter) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerUser.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            ComkerPager.apply(c, filter);
            List list = c.list();

            for(Object item:list) {
                loadAggregationRefs((ComkerUser) item);
            }
            return list;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerUser get(String id) {
            Session session = this.getSessionFactory().getCurrentSession();
            ComkerUser item = (ComkerUser) session.get(ComkerUser.class, id);
            loadAggregationRefs(item);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerUser getByEmail(String email) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(FIELD_EMAIL, email);
            return findWhere(params);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerUser getByUsername(String username) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(FIELD_USERNAME, username);
            return findWhere(params);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerUser create(ComkerUser item) {
            Session session = this.getSessionFactory().getCurrentSession();

            if (getByUsername(item.getUsername()) != null) {
                throw new ComkerFieldDuplicatedException(400, "Username has already existed");
            }

            if (getByEmail(item.getEmail()) != null) {
                throw new ComkerFieldDuplicatedException(400, "Email has already existed");
            }

            session.save(item);
            
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerUser update(ComkerUser item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void delete(ComkerUser item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.delete(item);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void delete(String id) {
            Session session = this.getSessionFactory().getCurrentSession();
            ComkerUser item = (ComkerUser) session.get(ComkerUser.class, id);
            session.delete(item);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Set<String> getCodeOfGlobalPermission(ComkerUser user) {
            Set<String> result = new HashSet<String>();

            List<ComkerUserJoinCrew> list = user.getUserJoinCrewList();
            for(ComkerUserJoinCrew item:list) {
                getCrewDao().collectCodeOfGlobalPermission(result, item.getCrew());
            }
            return result;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void addCrew(ComkerUser user, ComkerCrew crew) {
            ComkerUserJoinCrewPk id = new ComkerUserJoinCrewPk(user, crew);
            ComkerUserJoinCrew item = new ComkerUserJoinCrew();
            item.setPk(id);
            Session session = this.getSessionFactory().getCurrentSession();
            if (session.get(ComkerUserJoinCrew.class, id) == null) {
                session.saveOrUpdate(item);
            }
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void removeCrew(ComkerUser user, ComkerCrew crew) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.delete(new ComkerUserJoinCrew(user, crew));
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Map<String,Set<String>> getCodeOfSpotWithPermission(ComkerUser user) {
            Map<String,Set<String>> result = new HashMap<String,Set<String>>();

            List<ComkerUserJoinCrew> list = user.getUserJoinCrewList();
            for(ComkerUserJoinCrew item:list) {
                getCrewDao().collectCodeOfSpotWithPermission(result, item.getCrew());
            }
            return result;
        }

        //----------------------------------------------------------------------

        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        private void loadAggregationRefs(ComkerUser user) {
            if (user == null) return;
            user.getIdsOfCrewList().clear();
            List<ComkerUserJoinCrew> list = user.getUserJoinCrewList();
            for(ComkerUserJoinCrew item:list) {
                user.getIdsOfCrewList().add(item.getCrew().getId());
            }
        }
    }
}

