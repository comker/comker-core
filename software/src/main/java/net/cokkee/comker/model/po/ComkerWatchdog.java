package net.cokkee.comker.model.po;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */

@XmlRootElement
@Entity
@Table(name = "comker_watchdog")
public class ComkerWatchdog extends ComkerAbstractItem {

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

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "f_id", unique = true, nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "f_username", unique = false, nullable = true, length = 64)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "f_method_name", unique = false, nullable = true, length = 255)
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodId) {
        this.methodName = methodId;
    }

    @Column(name="f_method_args", columnDefinition="TEXT")
    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String args) {
        this.methodArgs = args;
    }

    @Column(name="f_hit_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getHitTime() {
        return hitTime;
    }

    public void setHitTime(Date hitTime) {
        this.hitTime = hitTime;
    }

    @Column(name="f_hit_duration")
    public Long getHitDuration() {
        return hitDuration;
    }

    public void setHitDuration(Long duration) {
        this.hitDuration = duration;
    }

    @Column(name="f_hit_state")
    public Integer getHitState() {
        return hitState;
    }

    public void setHitState(Integer flag) {
        this.hitState = flag;
    }

    @Column(name = "f_comment", unique = false, nullable = true, length = 1024)
    public String getComment() {
        return comment;
    }

    public void setComment(String text) {
        this.comment = text;
    }
}