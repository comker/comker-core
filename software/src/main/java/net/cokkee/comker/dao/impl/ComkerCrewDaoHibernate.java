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
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRoleDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRolePK;
import net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotPK;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewDPO.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewDPO.class);
        ComkerQueryPager.apply(c, pager);
        return c.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrewDPO findWhere(Map<String, Object> params) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        List result = c.list();
        if (result.isEmpty()) return null;
        return (ComkerCrewDPO)result.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(Map<String,Object> params, ComkerQueryPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerQueryPager.apply(c, filter);
        List list = c.list();
        return list;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(ComkerRoleDPO globalRole) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewJoinGlobalRoleDPO.class);

        session.buildLockRequest(LockOptions.NONE).lock(globalRole);

        c.add(Restrictions.eq(FIELD_ROLE, globalRole));
        List<ComkerCrewJoinGlobalRoleDPO> result = c.list();
        
        List<ComkerCrewDPO> list = new ArrayList<ComkerCrewDPO>();
        for(ComkerCrewJoinGlobalRoleDPO item:result) {
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
                    new Object[] {ComkerCrewDPO.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrewDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerCrewDPO item = (ComkerCrewDPO) session.get(ComkerCrewDPO.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrewDPO getByName(String name) {
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
    public ComkerCrewDPO getBySpotWithRole(ComkerSpotDPO spot, ComkerRoleDPO role) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerCrewJoinRoleWithSpotDPO.class);

        session.buildLockRequest(LockOptions.NONE).lock(spot);
        session.buildLockRequest(LockOptions.NONE).lock(role);

        c.add(Restrictions.eq(FIELD_SPOT, spot));
        c.add(Restrictions.eq(FIELD_ROLE, role));
        ComkerCrewJoinRoleWithSpotDPO result = (ComkerCrewJoinRoleWithSpotDPO) c.uniqueResult();
        if (result == null) return null;
        return result.getCrew();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addRoleWithSpot(ComkerCrewDPO crew, ComkerRoleDPO role, ComkerSpotDPO spot) {
        ComkerCrewJoinRoleWithSpotDPO item = new ComkerCrewJoinRoleWithSpotDPO();
        ComkerCrewJoinRoleWithSpotPK itemId = new ComkerCrewJoinRoleWithSpotPK(crew, role, spot);
        item.setPk(itemId);
        Session session = this.getSessionFactory().getCurrentSession();
        if (session.get(ComkerCrewJoinRoleWithSpotDPO.class, itemId) == null) {
            session.saveOrUpdate(item);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeRoleWithSpot(ComkerCrewDPO crew, ComkerRoleDPO role, ComkerSpotDPO spot) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(new ComkerCrewJoinRoleWithSpotDPO(crew, role, spot));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectSpotWithRole(Map<ComkerSpotDPO,Set<ComkerRoleDPO>> bag, ComkerCrewDPO crew) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(crew);
        List<ComkerCrewJoinRoleWithSpotDPO> list = crew.getCrewJoinRoleWithSpotList();
        for(ComkerCrewJoinRoleWithSpotDPO item:list) {
            ComkerSpotDPO spot = item.getSpot();
            Set<ComkerRoleDPO> roleSet = bag.get(spot);
            if (roleSet == null) {
                roleSet = new HashSet<ComkerRoleDPO>();
                bag.put(spot, roleSet);
            }
            roleSet.add(item.getRole());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addGlobalRole(ComkerCrewDPO crew, ComkerRoleDPO role) {
        ComkerCrewJoinGlobalRoleDPO item = new ComkerCrewJoinGlobalRoleDPO();
        item.setPk(new ComkerCrewJoinGlobalRolePK(crew, role));
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeGlobalRole(ComkerCrewDPO crew, ComkerRoleDPO role) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(new ComkerCrewJoinGlobalRoleDPO(crew, role));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectGlobalRole(Set<ComkerRoleDPO> bag, ComkerCrewDPO crew) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(crew);
        List<ComkerCrewJoinGlobalRoleDPO> list = crew.getCrewJoinGlobalRoleList();
        for(ComkerCrewJoinGlobalRoleDPO item:list) {
            bag.add(item.getRole());
        }
    }

    //----------------------------------------------------------------------

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String create(ComkerCrewDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        return String.valueOf(session.save(item));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerCrewDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerCrewDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }
}
