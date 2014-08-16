package net.cokkee.comker.storage.impl;

import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.storage.ComkerPermissionStorage;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.util.ComkerDataUtil;

/**
 *
 * @author drupalex
 */
public class ComkerPermissionStorageImpl implements ComkerPermissionStorage {

    private ComkerPermissionDao permissionDao = null;

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    private ComkerToolboxService toolboxService = null;

    public void setToolboxService(ComkerToolboxService toolboxService) {
        this.toolboxService = toolboxService;
    }

    @Override
    public Integer count() {
        return count(null);
    }

    @Override
    public List findAll(ComkerPager pager) {
        return findAll(null, pager);
    }

    @Override
    public Integer count(ComkerPermissionDTO.Filter filter) {
        return permissionDao.count(filter);
    }

    @Override
    public List findAll(ComkerPermissionDTO.Filter filter, ComkerPager pager) {
        List<ComkerPermissionDTO> polist = new ArrayList<ComkerPermissionDTO>();
        List dblist = permissionDao.findAll(filter, pager);
        for(Object dbitem:dblist) {
            ComkerPermissionDTO poitem = new ComkerPermissionDTO();
            ComkerDataUtil.copyProperties(dbitem, poitem);
            polist.add(poitem);
        }
        return polist;
    }

    @Override
    public ComkerPermissionDTO get(String id) {
        ComkerPermission dbItem = permissionDao.get(id);
        ComkerPermissionDTO poitem = new ComkerPermissionDTO();
        ComkerDataUtil.copyProperties(dbItem, poitem);
        return poitem;
    }

    @Override
    public ComkerPermissionDTO getByAuthority(String authority) {
        ComkerPermission dbItem = permissionDao.getByAuthority(authority);
        ComkerPermissionDTO poitem = new ComkerPermissionDTO();
        ComkerDataUtil.copyProperties(dbItem, poitem);
        return poitem;
    }
    
}
