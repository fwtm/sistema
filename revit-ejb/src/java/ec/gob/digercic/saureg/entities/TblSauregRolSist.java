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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hernan.inga
 */
@Entity
@Table(name = "TBL_SAUREG_ROL_SIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregRolSist.findAll", query = "SELECT t FROM TblSauregRolSist t"),
    @NamedQuery(name = "TblSauregRolSist.findByIdRol", query = "SELECT t FROM TblSauregRolSist t WHERE t.tblSauregRolSistPK.idRol = :idRol"),
    @NamedQuery(name = "TblSauregRolSist.findByIdSistema", query = "SELECT t FROM TblSauregRolSist t WHERE t.tblSauregRolSistPK.idSistema = :idSistema"),
    @NamedQuery(name = "TblSauregRolSist.findByRolesActivosAndSistema", query = "SELECT t FROM TblSauregRolSist t WHERE t.tblSauregSistema.idSistema = :idSistema AND t.status = :status")
})
public class TblSauregRolSist implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblSauregRolSistPK tblSauregRolSistPK;
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "ID_SISTEMA", referencedColumnName = "ID_SISTEMA", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblSauregSistema tblSauregSistema;
    @JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblSauregRol tblSauregRol;

    public TblSauregRolSist() {
    }

    public TblSauregRolSist(TblSauregRolSistPK tblSauregRolSistPK) {
        this.tblSauregRolSistPK = tblSauregRolSistPK;
    }

    public TblSauregRolSist(long idRol, long idSistema) {
        this.tblSauregRolSistPK = new TblSauregRolSistPK(idRol, idSistema);
    }

    public TblSauregRolSistPK getTblSauregRolSistPK() {
        return tblSauregRolSistPK;
    }

    public void setTblSauregRolSistPK(TblSauregRolSistPK tblSauregRolSistPK) {
        this.tblSauregRolSistPK = tblSauregRolSistPK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TblSauregSistema getTblSauregSistema() {
        return tblSauregSistema;
    }

    public void setTblSauregSistema(TblSauregSistema tblSauregSistema) {
        this.tblSauregSistema = tblSauregSistema;
    }

    public TblSauregRol getTblSauregRol() {
        return tblSauregRol;
    }

    public void setTblSauregRol(TblSauregRol tblSauregRol) {
        this.tblSauregRol = tblSauregRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblSauregRolSistPK != null ? tblSauregRolSistPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregRolSist)) {
            return false;
        }
        TblSauregRolSist other = (TblSauregRolSist) object;
        if ((this.tblSauregRolSistPK == null && other.tblSauregRolSistPK != null) || (this.tblSauregRolSistPK != null && !this.tblSauregRolSistPK.equals(other.tblSauregRolSistPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tblSauregRol.getDescripcion();
    }
    
}
