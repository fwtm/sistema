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
public class TblSauregRolSistPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ROL")
    private long idRol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SISTEMA")
    private long idSistema;

    public TblSauregRolSistPK() {
    }

    public TblSauregRolSistPK(long idRol, long idSistema) {
        this.idRol = idRol;
        this.idSistema = idSistema;
    }

    public long getIdRol() {
        return idRol;
    }

    public void setIdRol(long idRol) {
        this.idRol = idRol;
    }

    public long getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(long idSistema) {
        this.idSistema = idSistema;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRol;
        hash += (int) idSistema;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregRolSistPK)) {
            return false;
        }
        TblSauregRolSistPK other = (TblSauregRolSistPK) object;
        if (this.idRol != other.idRol) {
            return false;
        }
        if (this.idSistema != other.idSistema) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregRolSistPK[ idRol=" + idRol + ", idSistema=" + idSistema + " ]";
    }
    
}
