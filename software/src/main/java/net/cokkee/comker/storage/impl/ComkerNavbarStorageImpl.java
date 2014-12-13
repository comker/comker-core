package net.cokkee.comker.storage.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.storage.ComkerNavbarStorage;
import net.cokkee.comker.model.dto.ComkerNavNodeFormDTO;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;
import net.cokkee.comker.model.dpo.ComkerNavbarNodeDPO;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerNavbarStorageImpl implements ComkerNavbarStorage {

    private ComkerNavbarDao navbarDao = null;

    public void setNavbarDao(ComkerNavbarDao navbarDao) {
        this.navbarDao = navbarDao;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavNodeViewDTO getTree() {
        return convertDPOTree(navbarDao.getTree());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavNodeViewDTO getTree(String fromNodeId, String excludedNodeId) {
        return convertDPOTree(navbarDao.getTree(fromNodeId, excludedNodeId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerNavNodeViewDTO> getList() {
        return convertDPOList(navbarDao.getList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerNavNodeViewDTO> getList(String fromNodeId, String excludedNodeId) {
        return convertDPOList(navbarDao.getList(fromNodeId, excludedNodeId));
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavNodeViewDTO get(String id) {
        ComkerNavbarNodeDPO nodeDPO = getNotNull(id);
        ComkerNavNodeViewDTO result = new ComkerNavNodeViewDTO();
        ComkerDataUtil.copyPropertiesExcludes(nodeDPO, result, new String[] {"children"});
        loadAggregationRefs(nodeDPO, result);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerNavNodeViewDTO create(ComkerNavNodeFormDTO item) {

        ComkerNavbarNodeDPO nodeDPO = new ComkerNavbarNodeDPO();
        ComkerDataUtil.copyPropertiesExcludes(item, nodeDPO, new String[] {"children"});
        saveAggregationRefs(item, nodeDPO);
        nodeDPO = navbarDao.create(nodeDPO);

        ComkerNavNodeViewDTO result = new ComkerNavNodeViewDTO();
        ComkerDataUtil.copyPropertiesExcludes(nodeDPO, result, new String[] {"children"});
        loadAggregationRefs(nodeDPO, result);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerNavNodeFormDTO item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        navbarDao.delete(getNotNull(id));
    }
    
    //--------------------------------------------------------------------------
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void loadAggregationRefs(ComkerNavbarNodeDPO source, ComkerNavNodeViewDTO target) {
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void saveAggregationRefs(ComkerNavNodeFormDTO source, ComkerNavbarNodeDPO target) {
        ComkerNavbarNodeDPO parent = null;
        String parentId = source.getParentId();
        if (parentId != null) {
            parent = getNotNull(parentId);
            parent.getChildren().add(target);
        } else {
            int count = navbarDao.count();
            if (count > 0) {
                throw new ComkerInvalidParameterException("parent_node_already_exist");
            }
        }
        target.setParent(parent);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerNavbarNodeDPO getNotNull(String id) {
        ComkerNavbarNodeDPO dbItem = navbarDao.get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "navbarnode_with__id__not_found",
                    new ComkerExceptionExtension("error.navbarnode_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("NavbarNode object with id:{0} not found", 
                                    new Object[] {id})));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerNavNodeViewDTO convertDPOTree(ComkerNavbarNodeDPO rootDPO) {
        if (rootDPO == null) return null;

        Queue<ComkerNavbarNodeDPO> queueDPO = new LinkedList<ComkerNavbarNodeDPO>();
        Queue<ComkerNavNodeViewDTO> queueDTO = new LinkedList<ComkerNavNodeViewDTO>();

        queueDPO.add(rootDPO);

        ComkerNavNodeViewDTO rootDTO = new ComkerNavNodeViewDTO();
        queueDTO.add(rootDTO);

        while(!queueDPO.isEmpty()) {
            ComkerNavbarNodeDPO nodeDPO = queueDPO.remove();
            ComkerNavNodeViewDTO nodeDTO = queueDTO.remove();
            ComkerDataUtil.copyPropertiesExcludes(nodeDPO, nodeDTO,
                    new String[] {"children"});

            Set<ComkerNavbarNodeDPO> childrenDPO = nodeDPO.getChildren();
            Set<ComkerNavNodeViewDTO> childrenDTO = nodeDTO.getChildren();
            if (childrenDPO == null) continue;

            for(ComkerNavbarNodeDPO childDPO:childrenDPO) {
                ComkerNavNodeViewDTO childDTO = new ComkerNavNodeViewDTO();
                childrenDTO.add(childDTO);

                queueDPO.add(childDPO);
                queueDTO.add(childDTO);
            }
        }
        
        return rootDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private List<ComkerNavNodeViewDTO> convertDPOList(List<ComkerNavbarNodeDPO> listDPO) {
        List<ComkerNavNodeViewDTO> listDTO = new ArrayList<ComkerNavNodeViewDTO>();
        for(ComkerNavbarNodeDPO itemDPO:listDPO) {
            ComkerNavNodeViewDTO itemDTO = new ComkerNavNodeViewDTO();
            ComkerDataUtil.copyPropertiesExcludes(itemDPO, itemDTO,
                    new String[] {"children"});
            listDTO.add(itemDTO);
        }
        return listDTO;
    }
}
