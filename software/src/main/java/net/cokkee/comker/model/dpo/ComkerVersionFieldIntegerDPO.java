package net.cokkee.comker.model.dpo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@Entity
@Table(name = "comker_version_field_integer")
public class ComkerVersionFieldIntegerDPO extends ComkerAbstractDPO {

    private String id;
    private String field;
    private Integer value;
    private Date changedOn;
    private Long revision;

    private ComkerVersionEntityDPO entity;

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
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
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
    public ComkerVersionEntityDPO getEntity() {
        return entity;
    }

    public void setEntity(ComkerVersionEntityDPO entity) {
        this.entity = entity;
    }
}
