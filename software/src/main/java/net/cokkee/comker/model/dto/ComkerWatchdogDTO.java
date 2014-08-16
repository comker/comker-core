package net.cokkee.comker.model.dto;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */

@XmlRootElement
public class ComkerWatchdogDTO extends ComkerAbstractDTO {

    public static final int HIT_STATE_SUCCESS = 0;
    public static final int HIT_STATE_FAILURE = 1;

    private String id;
    private String username;
    private String methodName;
    private String methodArgs;
    private Date hitTime;
    private Long hitDuration;
    private Integer hitState = HIT_STATE_SUCCESS;

    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodId) {
        this.methodName = methodId;
    }

    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String args) {
        this.methodArgs = args;
    }

    public Date getHitTime() {
        return hitTime;
    }

    public void setHitTime(Date hitTime) {
        this.hitTime = hitTime;
    }

    public Long getHitDuration() {
        return hitDuration;
    }

    public void setHitDuration(Long duration) {
        this.hitDuration = duration;
    }

    public Integer getHitState() {
        return hitState;
    }

    public void setHitState(Integer flag) {
        this.hitState = flag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String text) {
        this.comment = text;
    }

    @XmlRootElement
    public static class Filter extends ComkerAbstractDTO.Filter {
        public Filter() {
            super();
        }
        public Filter(String queryString) {
            super(queryString);
        }
    }
    
    @XmlRootElement
    public static class Pack {

        public Pack() {
        }

        public Pack(Integer total, List<ComkerWatchdogDTO> collection) {
            this();
            this.total = total;
            this.collection = collection;
        }

        private Integer total = null;

        private List<ComkerWatchdogDTO> collection = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<ComkerWatchdogDTO> getCollection() {
            return collection;
        }

        public void setCollection(List<ComkerWatchdogDTO> collection) {
            this.collection = collection;
        }
    }
}
