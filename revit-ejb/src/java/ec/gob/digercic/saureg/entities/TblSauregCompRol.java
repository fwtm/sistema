/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "TBL_SAUREG_COMP_ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregCompRol.findAll", query = "SELECT t FROM TblSauregCompRol t"),
    @NamedQuery(name = "TblSauregCompRol.findByIdEstrucSist", query = "SELECT t FROM TblSauregCompRol t WHERE t.tblSauregCompRolPK.idEstrucSist = :idEstrucSist"),
    @NamedQuery(name = "TblSauregCompRol.findByIdRol", query = "SELECT t FROM TblSauregCompRol t WHERE t.tblSauregCompRolPK.idRol = :idRol"),
    @NamedQuery(name = "TblSauregCompRol.findByIdPermiso", query = "SELECT t FROM TblSauregCompRol t WHERE t.tblSauregCompRolPK.idPermiso = :idPermiso"),
    //
    @NamedQuery(name = "TblSauregCompRol.findByUserSistema", query = "SELECT t FROM TblSauregCompRol t WHERE t.tblSauregRol.idRol IN :idRoles AND t.tblSauregEstrucSist.idSistema = :idSistema"),
    @NamedQuery(name = "TblSauregCompRol.findByPermisoPagina", query = "SELECT t FROM TblSauregCompRol t WHERE t.tblSauregPermiso.idPermiso = :permiso AND t.tblSauregEstrucSist.idEstrucSist = :pagina")
})
public class TblSauregCompRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblSauregCompRolPK tblSauregCompRolPK;
    @JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TblSauregRol tblSauregRol;
    @JoinColumn(name = "ID_PERMISO", referencedColumnName = "ID_PERMISO", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TblSauregPermiso tblSauregPermiso;
    @JoinColumn(name = "ID_ESTRUC_SIST", referencedColumnName = "ID_ESTRUC_SIST", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TblSauregEstrucSist tblSauregEstrucSist;

    public TblSauregCompRol() {
    }

    public TblSauregCompRol(TblSauregCompRolPK tblSauregCompRolPK) {
        this.tblSauregCompRolPK = tblSauregCompRolPK;
    }

    public TblSauregCompRol(long idEstrucSist, long idRol, long idPermiso) {
        this.tblSauregCompRolPK = new TblSauregCompRolPK(idEstrucSist, idRol, idPermiso);
    }

    public TblSauregCompRolPK getTblSauregCompRolPK() {
        return tblSauregCompRolPK;
    }

    public void setTblSauregCompRolPK(TblSauregCompRolPK tblSauregCompRolPK) {
        this.tblSauregCompRolPK = tblSauregCompRolPK;
    }

    public TblSauregRol getTblSauregRol() {
        return tblSauregRol;
    }

    public void setTblSauregRol(TblSauregRol tblSauregRol) {
        this.tblSauregRol = tblSauregRol;
    }

    public TblSauregPermiso getTblSauregPermiso() {
        return tblSauregPermiso;
    }

    public void setTblSauregPermiso(TblSauregPermiso tblSauregPermiso) {
        this.tblSauregPermiso = tblSauregPermiso;
    }

    public TblSauregEstrucSist getTblSauregEstrucSist() {
        return tblSauregEstrucSist;
    }

    public void setTblSauregEstrucSist(TblSauregEstrucSist tblSauregEstrucSist) {
        this.tblSauregEstrucSist = tblSauregEstrucSist;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblSauregCompRolPK != null ? tblSauregCompRolPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregCompRol)) {
            return false;
        }
        TblSauregCompRol other = (TblSauregCompRol) object;
        if ((this.tblSauregCompRolPK == null && other.tblSauregCompRolPK != null) || (this.tblSauregCompRolPK != null && !this.tblSauregCompRolPK.equals(other.tblSauregCompRolPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsinformacion.modelo.entities.TblSauregCompRol[ tblSauregCompRolPK=" + tblSauregCompRolPK + " ]";
    }
    
}
