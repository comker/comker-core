package net.cokkee.comker.model.dpo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@Entity
@Table(name = "comker_registration")
public class ComkerRegistrationDPO extends ComkerAbstractDPO {
    
    private String id;
    private String email;
    private String encodedPassword;
    private String confirmationCode;
    private Boolean confirmed = Boolean.FALSE;

    public ComkerRegistrationDPO() {
        super();
    }

    public ComkerRegistrationDPO(String email, String encodedPassword, String confirmationCode) {
        super();
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.confirmationCode = confirmationCode;
    }
    
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
    
    @Column(name = "f_email", unique = false, nullable = false, length = 64)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "f_encoded_password", unique = false, nullable = false, length = 64)
    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    @Column(name = "f_confirmation_code", unique = true, nullable = false, length = 64)
    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    @Column(name = "f_confirmed", nullable = true)
    public Boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
