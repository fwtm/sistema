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
import javax.validation.constraints.Size;

/**
 *
 * @author daniel.porras
 */
@Embeddable
public class TblSauregUsuAgenciaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_AGENCIA")
    private long idAgencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "NOM_USUARIO")
    private String nomUsuario;

    public TblSauregUsuAgenciaPK() {
    }

    public TblSauregUsuAgenciaPK(long idAgencia, String nomUsuario) {
        this.idAgencia = idAgencia;
        this.nomUsuario = nomUsuario;
    }

    public long getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(long idAgencia) {
        this.idAgencia = idAgencia;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAgencia;
        hash += (nomUsuario != null ? nomUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregUsuAgenciaPK)) {
            return false;
        }
        TblSauregUsuAgenciaPK other = (TblSauregUsuAgenciaPK) object;
        if (this.idAgencia != other.idAgencia) {
            return false;
        }
        if ((this.nomUsuario == null && other.nomUsuario != null) || (this.nomUsuario != null && !this.nomUsuario.equals(other.nomUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregUsuAgenciaPK[ idAgencia=" + idAgencia + ", nomUsuario=" + nomUsuario + " ]";
    }
    
}
