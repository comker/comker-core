package net.cokkee.comker.model;

@javax.xml.bind.annotation.XmlRootElement
public class ComkerExceptionResponse {

    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int OK = 4;
    public static final int TOO_BUSY = 5;

    public static final String WEBAPP_ERRORS = "WEBAPP_ERRORS";
    public static final String SYSTEM_ERRORS = "SYSTEM_ERRORS";
    public static final String COMKER_ERRORS = "COMKER_ERRORS";

    private int code;
    private String type;
    private String name;
    private String message;

    public ComkerExceptionResponse() {
    }

    public ComkerExceptionResponse(int code, String message) {
        this.code = code;
        if (code == 500) {
            this.type = SYSTEM_ERRORS;
        } else if (code < 500) {
            this.type = WEBAPP_ERRORS;
        } else if (code > 500) {
            this.type = COMKER_ERRORS;
        }
        this.message = message;
    }

    public ComkerExceptionResponse(int code, String message, String name) {
        this(code, message);
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
