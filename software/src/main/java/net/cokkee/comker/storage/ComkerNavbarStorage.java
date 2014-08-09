package net.cokkee.comker.storage;

import java.util.List;
import net.cokkee.comker.model.dto.ComkerNavNodeFormDTO;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerNavbarStorage {

    ComkerNavNodeViewDTO getTree();

    ComkerNavNodeViewDTO getTree(String fromNodeId, String excludedNodeId);

    List<ComkerNavNodeViewDTO> getList();

    List<ComkerNavNodeViewDTO> getList(String fromNodeId, String excludedNodeId);
    
    ComkerNavNodeViewDTO get(String id);

    ComkerNavNodeViewDTO create(ComkerNavNodeFormDTO item);

    void update(ComkerNavNodeFormDTO item);

    void delete(String id);
}
