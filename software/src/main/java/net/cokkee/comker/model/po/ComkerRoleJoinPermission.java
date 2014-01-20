package net.cokkee.comker.model.po;

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
public class ComkerRoleJoinPermission extends ComkerAbstractItem {

    private ComkerRoleJoinPermissionPk pk = new ComkerRoleJoinPermissionPk();

    @EmbeddedId
    public ComkerRoleJoinPermissionPk getPk() {
        return pk;
    }

    public void setPk(ComkerRoleJoinPermissionPk pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerRole getRole() {
        return getPk().getRole();
    }

    public void setRole(ComkerRole item) {
        getPk().setRole(item);
    }

    @Transient
    public ComkerPermission getPermission() {
        return getPk().getPermission();
    }

    public void setPermission(ComkerPermission product) {
        getPk().setPermission(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerRoleJoinPermission that = (ComkerRoleJoinPermission) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
