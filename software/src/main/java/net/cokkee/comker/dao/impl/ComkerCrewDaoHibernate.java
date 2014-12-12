package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.dao.*;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerCrewJoinGlobalRole;
import net.cokkee.comker.model.po.ComkerCrewJoinGlobalRolePk;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpot;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpotPk;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerCrewDaoHibernate extends ComkerAbstractDaoHibernate
            implements ComkerCrewDao {

    private ComkerRoleDao roleDao = null;

    public ComkerRoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrew.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrew.class);
        ComkerQueryPager.apply(c, pager);
        return c.list();
    }

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
    public List findAllWhere(Map<String,Object> params, ComkerQueryPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrew.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerQueryPager.apply(c, filter);
        List list = c.list();
        return list;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(ComkerRole globalRole) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewJoinGlobalRole.class);

        session.buildLockRequest(LockOptions.NONE).lock(globalRole);

        c.add(Restrictions.eq(FIELD_ROLE, globalRole));
        List<ComkerCrewJoinGlobalRole> result = c.list();
        
        List<ComkerCrew> list = new ArrayList<ComkerCrew>();
        for(ComkerCrewJoinGlobalRole item:result) {
            list.add(item.getCrew());
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean exists(String id) {
        Query query = this.getSessionFactory().getCurrentSession().
                createQuery(MessageFormat.format(
                    "select 1 from {0} t where t.id = :id",
                    new Object[] {ComkerCrew.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrew get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerCrew item = (ComkerCrew) session.get(ComkerCrew.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrew getByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_NAME, name);
        return findWhere(params);
    }
    
    /* // Examples: ctx, log
        def cd = ctx.getBean('comkerCrewDao');
        def sd = ctx.getBean('comkerSpotDao');
        def rd = ctx.getBean('comkerRoleDao');

        def s = sd.getByCode('buocnho.com');
        def r = rd.getByCode('Manager');

        def c = cd.getBySpotWithRole(s, r);
        print c.getName();
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrew getBySpotWithRole(ComkerSpot spot, ComkerRole role) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewJoinRoleWithSpot.class);

        session.buildLockRequest(LockOptions.NONE).lock(spot);
        session.buildLockRequest(LockOptions.NONE).lock(role);

        c.add(Restrictions.eq(FIELD_SPOT, spot));
        c.add(Restrictions.eq(FIELD_ROLE, role));
        ComkerCrewJoinRoleWithSpot result = (ComkerCrewJoinRoleWithSpot) c.uniqueResult();
        if (result == null) return null;
        return result.getCrew();
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectSpotWithRole(Map<ComkerSpot,Set<ComkerRole>> bag, ComkerCrew crew) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(crew);
        List<ComkerCrewJoinRoleWithSpot> list = crew.getCrewJoinRoleWithSpotList();
        for(ComkerCrewJoinRoleWithSpot item:list) {
            ComkerSpot spot = item.getSpot();
            Set<ComkerRole> roleSet = bag.get(spot);
            if (roleSet == null) {
                roleSet = new HashSet<ComkerRole>();
                bag.put(spot, roleSet);
            }
            roleSet.add(item.getRole());
        }
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectGlobalRole(Set<ComkerRole> bag, ComkerCrew crew) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(crew);
        List<ComkerCrewJoinGlobalRole> list = crew.getCrewJoinGlobalRoleList();
        for(ComkerCrewJoinGlobalRole item:list) {
            bag.add(item.getRole());
        }
    }

    //----------------------------------------------------------------------

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerCrew create(ComkerCrew item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerCrew update(ComkerCrew item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerCrew item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }
}
