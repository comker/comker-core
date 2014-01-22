package net.cokkee.comker.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerCrewJoinGlobalRole;
import net.cokkee.comker.model.po.ComkerCrewJoinGlobalRolePk;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpot;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpotPk;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;
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
public interface ComkerCrewDao extends ComkerAbstractDao {

    ComkerCrew findWhere(Map<String,Object> params);

    ComkerCrew getByName(String name);
    
    ComkerCrew save(ComkerCrew item);

    void addGlobalRole(ComkerCrew crew, ComkerRole role);

    void removeGlobalRole(ComkerCrew crew, ComkerRole role);

    void addRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot);

    void removeRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot);

    Set<String> getCodeOfGlobalRole(ComkerCrew crew);

    void collectCodeOfGlobalRole(Set<String> bag, ComkerCrew crew);

    Map<String,Set<String>> getCodeOfSpotWithRole(ComkerCrew crew);

    void collectCodeOfSpotWithRole(Map<String,Set<String>> bag, ComkerCrew crew);

    public static class Hibernate extends ComkerAbstractDao.Hibernate
            implements ComkerCrewDao {

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerCrew findWhere(Map<String, Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerCrew.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            List result = c.list();
            if (result.isEmpty()) return null;
            return (ComkerCrew)result.get(0);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerCrew getByName(String name) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(FIELD_NAME, name);
            return findWhere(params);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerCrew save(ComkerCrew item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void addRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot) {
            ComkerCrewJoinRoleWithSpot item = new ComkerCrewJoinRoleWithSpot();
            ComkerCrewJoinRoleWithSpotPk itemId = new ComkerCrewJoinRoleWithSpotPk(crew, role, spot);
            item.setPk(itemId);
            Session session = this.getSessionFactory().getCurrentSession();
            if (session.get(ComkerCrewJoinRoleWithSpot.class, itemId) == null) {
                session.saveOrUpdate(item);
            }
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void removeRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.delete(new ComkerCrewJoinRoleWithSpot(crew, role, spot));
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void addGlobalRole(ComkerCrew crew, ComkerRole role) {
            ComkerCrewJoinGlobalRole item = new ComkerCrewJoinGlobalRole();
            item.setPk(new ComkerCrewJoinGlobalRolePk(crew, role));
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void removeGlobalRole(ComkerCrew crew, ComkerRole role) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.delete(new ComkerCrewJoinGlobalRole(crew, role));
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public Set<String> getCodeOfGlobalRole(ComkerCrew crew) {
            Set<String> result = new HashSet<String>();
            collectCodeOfGlobalRole(result, crew);
            return result;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void collectCodeOfGlobalRole(Set<String> bag, ComkerCrew crew) {
            if (bag == null) return;
            List<ComkerCrewJoinGlobalRole> list = crew.getCrewJoinGlobalRoleList();
            for(ComkerCrewJoinGlobalRole item:list) {
                bag.add(item.getRole().getCode());
            }
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public Map<String,Set<String>> getCodeOfSpotWithRole(ComkerCrew crew) {
            Map<String,Set<String>> result = new HashMap<String,Set<String>>();
            collectCodeOfSpotWithRole(result, crew);
            return result;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void collectCodeOfSpotWithRole(Map<String,Set<String>> bag, ComkerCrew crew) {
            if (bag == null) return;
            List<ComkerCrewJoinRoleWithSpot> list = crew.getCrewJoinRoleWithSpotList();
            for(ComkerCrewJoinRoleWithSpot item:list) {
                ComkerSpot spot = item.getSpot();
                Set<String> roleSet = bag.get(spot.getCode());
                if (roleSet == null) {
                    roleSet = new HashSet<String>();
                    bag.put(spot.getCode(), roleSet);
                }
                roleSet.add(item.getRole().getCode());
            }
        }
    }
}

