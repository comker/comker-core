package net.cokkee.comker.model.po;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "comker_audit_field_boolean")
public class ComkerAuditFieldBoolean extends ComkerAbstractItem {

    private String id;
    private String field;
    private Boolean value;
    private Date changedOn;
    private Long revision;

    private ComkerAuditEntity entity;

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

    @Column(name = "f_field", unique = false, nullable = false, length = 255)
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Column(name = "f_value", unique = false, nullable = true)
    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Column(name="f_changed_on")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(Date changedOn) {
        this.changedOn = changedOn;
    }

    @Column(name="f_revision")
    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="f_entity_id")
    public ComkerAuditEntity getEntity() {
        return entity;
    }

    public void setEntity(ComkerAuditEntity entity) {
        this.entity = entity;
    }
}
