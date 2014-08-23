package net.cokkee.comker.model.error;

/**
 *
 * @author drupalex
 */
public class ComkerEntityError extends ComkerMessageResolvable {

    public ComkerEntityError() {
    }

    public ComkerEntityError(String objectName) {
        this.objectName = objectName;
    }

    public ComkerEntityError(String objectName,
            String code, Object[] arguments, String defaultMessage) {
        super(code, arguments, defaultMessage);
        this.objectName = objectName;
    }

    private String objectName;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
