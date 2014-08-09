package net.cokkee.comker.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.exception.ComkerFieldDuplicatedException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpot;

import net.cokkee.comker.model.po.ComkerSpot;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerSpotDao extends ComkerAbstractDao {

    Integer count();

    List findAll(ComkerPager filter);

    ComkerSpot findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    ComkerSpot get(String id);

    ComkerSpot getByCode(String code);

    ComkerSpot create(ComkerSpot item);

    ComkerSpot update(ComkerSpot item);

    void delete(ComkerSpot item);

    void delete(String id);

    //Map<String,Set<String>> getCodeOfCrewWithRole(ComkerSpot spot);

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static class Hibernate extends ComkerAbstractDao.Hibernate
            implements ComkerSpotDao {

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Integer count() {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerSpot.class);
            c.setProjection(Projections.rowCount());
            return ((Long) c.uniqueResult()).intValue();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List findAll(ComkerPager filter) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerSpot.class);
            ComkerPager.apply(c, filter);
            return c.list();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerSpot findWhere(Map<String, Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerSpot.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            List result = c.list();
            if (result.isEmpty()) return null;
            return (ComkerSpot)result.get(0);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List findAllWhere(Map<String,Object> params, ComkerPager filter) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerSpot.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            ComkerPager.apply(c, filter);
            return c.list();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerSpot get(String id) {
            Session session = this.getSessionFactory().getCurrentSession();
            ComkerSpot item = (ComkerSpot) session.get(ComkerSpot.class, id);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerSpot getByCode(String code) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(FIELD_CODE, code);
            ComkerSpot item = findWhere(params);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerSpot create(ComkerSpot item) {
            Session session = this.getSessionFactory().getCurrentSession();

            if (getByCode(item.getCode()) != null) {
                throw new ComkerFieldDuplicatedException(400, "Spot has already existed");
            }

            session.save(item);

            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerSpot update(ComkerSpot item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void delete(ComkerSpot item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.delete(item);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void delete(String id) {
            Session session = this.getSessionFactory().getCurrentSession();
            ComkerSpot item = (ComkerSpot) session.get(ComkerSpot.class, id);
            session.delete(item);
        }

        /*
        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public Map<String,Set<String>> getCodeOfCrewWithRole(ComkerSpot spot) {
            Map<String,Set<String>> result = new HashMap<String,Set<String>>();

            List<ComkerCrewJoinRoleWithSpot> joinRoleWithSpots = spot.getCrewJoinRoleWithSpotList();
            for(ComkerCrewJoinRoleWithSpot item:joinRoleWithSpots) {
                ComkerCrew crew = item.getCrew();
                Set<String> roleSet = result.get(crew.getName());
                if (roleSet == null) {
                    roleSet = new HashSet<String>();
                    result.put(crew.getName(), roleSet);
                }
                roleSet.add(item.getRole().getCode());
            }

            return result;
        }
        */
    }
}

