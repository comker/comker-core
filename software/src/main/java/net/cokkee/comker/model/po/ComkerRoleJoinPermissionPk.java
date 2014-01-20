/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cokkee.comker.model.po;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author drupalex
 */
@Embeddable
public class ComkerRoleJoinPermissionPk implements Serializable {

    private ComkerRole role;
    private ComkerPermission permission;

    @ManyToOne
    @JoinColumn(name="f_role_id")
    public ComkerRole getRole() {
        return role;
    }

    public void setRole(ComkerRole role) {
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name="f_permission_id")
    public ComkerPermission getPermission() {
        return permission;
    }

    public void setPermission(ComkerPermission permission) {
        this.permission = permission;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerRoleJoinPermissionPk that = (ComkerRoleJoinPermissionPk) o;

        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (permission != null ? !permission.equals(that.permission) : that.permission != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (role != null ? role.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        return result;
    }
}