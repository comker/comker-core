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

    public ComkerPermissionDao getPermissionDao() {
        return permissionDao;
    }

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    private ComkerToolboxService toolboxService = null;

    public ComkerToolboxService getToolboxService() {
        return toolboxService;
    }

    public void setToolboxService(ComkerToolboxService toolboxService) {
        this.toolboxService = toolboxService;
    }

    @Override
    public Integer count() {
        return getPermissionDao().count();
    }

    @Override
    public List findAll(ComkerPager filter) {
        List<ComkerPermissionDTO> polist = new ArrayList<ComkerPermissionDTO>();
        List dblist = getPermissionDao().findAll(filter);
        for(Object dbitem:dblist) {
            ComkerPermissionDTO poitem = new ComkerPermissionDTO();
            ComkerDataUtil.copyProperties(dbitem, poitem);
            polist.add(poitem);
        }
        return polist;
    }

    @Override
    public ComkerPermissionDTO get(String id) {
        ComkerPermission dbItem = getPermissionDao().get(id);
        ComkerPermissionDTO poitem = new ComkerPermissionDTO();
        ComkerDataUtil.copyProperties(dbItem, poitem);
        return poitem;
    }

    @Override
    public ComkerPermissionDTO getByAuthority(String authority) {
        ComkerPermission dbItem = getPermissionDao().getByAuthority(authority);
        ComkerPermissionDTO poitem = new ComkerPermissionDTO();
        ComkerDataUtil.copyProperties(dbItem, poitem);
        return poitem;
    }
    
}
