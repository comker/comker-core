package net.cokkee.comker.model.dpo;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author drupalex
 */
@Entity
@Table(name = "comker_role_join_permission")
@AssociationOverrides({
    @AssociationOverride(name = "pk.role", joinColumns = @JoinColumn(name = "f_role_id")),
    @AssociationOverride(name = "pk.permission", joinColumns = @JoinColumn(name = "f_permission_id"))
})
public class ComkerRoleJoinPermissionDPO extends ComkerAbstractDPO {

    public ComkerRoleJoinPermissionDPO() {
        super();
        this.pk = new ComkerRoleJoinPermissionPK();
    }

    public ComkerRoleJoinPermissionDPO(ComkerRoleDPO role, ComkerPermissionDPO permission) {
        super();
        this.pk = new ComkerRoleJoinPermissionPK(role, permission);
    }

    private ComkerRoleJoinPermissionPK pk;

    @EmbeddedId
    public ComkerRoleJoinPermissionPK getPk() {
        return pk;
    }

    public void setPk(ComkerRoleJoinPermissionPK pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerRoleDPO getRole() {
        return getPk().getRole();
    }

    public void setRole(ComkerRoleDPO item) {
        getPk().setRole(item);
    }

    @Transient
    public ComkerPermissionDPO getPermission() {
        return getPk().getPermission();
    }

    public void setPermission(ComkerPermissionDPO product) {
        getPk().setPermission(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerRoleJoinPermissionDPO that = (ComkerRoleJoinPermissionDPO) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
