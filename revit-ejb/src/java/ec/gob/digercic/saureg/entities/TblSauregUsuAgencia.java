/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "TBL_SAUREG_USU_AGENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregUsuAgencia.findAll", query = "SELECT t FROM TblSauregUsuAgencia t"),
    @NamedQuery(name = "TblSauregUsuAgencia.findByIdAgencia", query = "SELECT t FROM TblSauregUsuAgencia t WHERE t.tblSauregUsuAgenciaPK.idAgencia = :idAgencia"),
    @NamedQuery(name = "TblSauregUsuAgencia.findByNomUsuario", query = "SELECT t FROM TblSauregUsuAgencia t WHERE t.tblSauregUsuAgenciaPK.nomUsuario = :nomUsuario AND t.status = :status"),
    //
    @NamedQuery(name = "TblSauregUsuAgencia.findByIdUsuarioAndIdAgencia", query = "SELECT t FROM TblSauregUsuAgencia t WHERE t.tblSauregUsuAgenciaPK.nomUsuario = :nomUsuario AND t.tblSauregUsuAgenciaPK.idAgencia = :idAgencia"),
    /**/
    @NamedQuery(name = "TblSauregUsuAgencia.findByIdUsuarioAndIdAgenciaAndStatus", query = "SELECT t FROM TblSauregUsuAgencia t WHERE t.tblSauregUsuAgenciaPK.nomUsuario = :nomUsuario AND t.tblSauregUsuAgenciaPK.idAgencia = :idAgencia AND t.status = :status")
    })
public class TblSauregUsuAgencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblSauregUsuAgenciaPK tblSauregUsuAgenciaPK;
    @Column(name = "STATUS")
    private Character status;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 64)
    @Column(name = "E_MAIL")
    private String eMail;
    @Size(max = 11)
    @Column(name = "CELULAR")
    private String celular;
    @Size(max = 15)
    @Column(name = "TELEFONO")
    private String telefono;
    @Size(max = 10)
    @Column(name = "TELF_EXTENSION")
    private String telfExtension;
    @JoinColumn(name = "NOM_USUARIO", referencedColumnName = "NOM_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TblSauregUsuario tblSauregUsuario;

    public TblSauregUsuAgencia() {
    }

    public TblSauregUsuAgencia(TblSauregUsuAgenciaPK tblSauregUsuAgenciaPK) {
        this.tblSauregUsuAgenciaPK = tblSauregUsuAgenciaPK;
    }

    public TblSauregUsuAgencia(long idAgencia, String nomUsuario) {
        this.tblSauregUsuAgenciaPK = new TblSauregUsuAgenciaPK(idAgencia, nomUsuario);
    }

    public TblSauregUsuAgenciaPK getTblSauregUsuAgenciaPK() {
        return tblSauregUsuAgenciaPK;
    }

    public void setTblSauregUsuAgenciaPK(TblSauregUsuAgenciaPK tblSauregUsuAgenciaPK) {
        this.tblSauregUsuAgenciaPK = tblSauregUsuAgenciaPK;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelfExtension() {
        return telfExtension;
    }

    public void setTelfExtension(String telfExtension) {
        this.telfExtension = telfExtension;
    }

    public TblSauregUsuario getTblSauregUsuario() {
        return tblSauregUsuario;
    }

    public void setTblSauregUsuario(TblSauregUsuario tblSauregUsuario) {
        this.tblSauregUsuario = tblSauregUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblSauregUsuAgenciaPK != null ? tblSauregUsuAgenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregUsuAgencia)) {
            return false;
        }
        TblSauregUsuAgencia other = (TblSauregUsuAgencia) object;
        if ((this.tblSauregUsuAgenciaPK == null && other.tblSauregUsuAgenciaPK != null) || (this.tblSauregUsuAgenciaPK != null && !this.tblSauregUsuAgenciaPK.equals(other.tblSauregUsuAgenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregUsuAgencia[ tblSauregUsuAgenciaPK=" + tblSauregUsuAgenciaPK + " ]";
    }
    
}
