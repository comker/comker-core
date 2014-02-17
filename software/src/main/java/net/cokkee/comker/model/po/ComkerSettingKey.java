package net.cokkee.comker.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@XmlRootElement
@Entity
@Table(name = "comker_setting_key",
        uniqueConstraints = @UniqueConstraint(columnNames = "f_code"))
public class ComkerSettingKey extends ComkerAbstractItem {

    public ComkerSettingKey() {
        super();
    }

    public ComkerSettingKey(String code, String type, String range) {
        super();
        this.code = code;
        this.type = type;
        this.range = range;
    }

    private String id;
    private String code;
    private String type;
    private String range;

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

    @Column(name = "f_code", unique = true, nullable = false, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "f_type", unique = false, nullable = false, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }
    
    @Column(name = "f_range", unique = false, nullable = true, length = 1024)
    public String getRange() {
        return range;
    }

    public void setRange(String description) {
        this.range = description;
    }
}
