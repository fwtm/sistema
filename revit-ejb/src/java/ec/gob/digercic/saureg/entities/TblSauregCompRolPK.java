/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author daniel.porras
 */
@Embeddable
public class TblSauregCompRolPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ESTRUC_SIST")
    private long idEstrucSist;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ROL")
    private long idRol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PERMISO")
    private long idPermiso;

    public TblSauregCompRolPK() {
    }

    public TblSauregCompRolPK(long idEstrucSist, long idRol, long idPermiso) {
        this.idEstrucSist = idEstrucSist;
        this.idRol = idRol;
        this.idPermiso = idPermiso;
    }

    public long getIdEstrucSist() {
        return idEstrucSist;
    }

    public void setIdEstrucSist(long idEstrucSist) {
        this.idEstrucSist = idEstrucSist;
    }

    public long getIdRol() {
        return idRol;
    }

    public void setIdRol(long idRol) {
        this.idRol = idRol;
    }

    public long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(long idPermiso) {
        this.idPermiso = idPermiso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEstrucSist;
        hash += (int) idRol;
        hash += (int) idPermiso;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregCompRolPK)) {
            return false;
        }
        TblSauregCompRolPK other = (TblSauregCompRolPK) object;
        if (this.idEstrucSist != other.idEstrucSist) {
            return false;
        }
        if (this.idRol != other.idRol) {
            return false;
        }
        if (this.idPermiso != other.idPermiso) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregCompRolPK[ idEstrucSist=" + idEstrucSist + ", idRol=" + idRol + ", idPermiso=" + idPermiso + " ]";
    }
    
}
