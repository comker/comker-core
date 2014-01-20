package net.cokkee.comker.service;

import java.util.List;
import net.cokkee.comker.model.po.ComkerQuestion;
import net.cokkee.comker.service.impl.ComkerStorageManagerImpl;

/**
 *
 * @author drupalex
 */
public abstract class ComkerStorageManager {

    private static ComkerStorageManager instance = null;

    public static ComkerStorageManager getInstance() {
        if (instance == null) {
            instance = new ComkerStorageManagerImpl();
        }
        return instance;
    }

    public abstract List<ComkerQuestion> getQuestionList();
}
