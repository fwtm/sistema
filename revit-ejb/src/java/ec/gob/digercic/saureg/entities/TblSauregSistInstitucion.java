/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "TBL_SAUREG_SIST_INSTITUCION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregSistInstitucion.findAll", query = "SELECT t FROM TblSauregSistInstitucion t"),
    @NamedQuery(name = "TblSauregSistInstitucion.findByIdSistInst", query = "SELECT t FROM TblSauregSistInstitucion t WHERE t.idSistInst = :idSistInst")
})
public class TblSauregSistInstitucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SIST_INST")
    private Long idSistInst;
    @Size(max = 25)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "STATUS")
    private Character status;
    @JoinColumn(name = "ID_SISTEMA", referencedColumnName = "ID_SISTEMA")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblSauregSistema idSistema;
    @JoinColumn(name = "ID_INSTITUC", referencedColumnName = "ID_INSTITUC")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblSauregInstitucion idInstituc;

    public TblSauregSistInstitucion() {
    }

    public TblSauregSistInstitucion(Long idSistInst) {
        this.idSistInst = idSistInst;
    }

    public Long getIdSistInst() {
        return idSistInst;
    }

    public void setIdSistInst(Long idSistInst) {
        this.idSistInst = idSistInst;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public TblSauregSistema getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(TblSauregSistema idSistema) {
        this.idSistema = idSistema;
    }

    public TblSauregInstitucion getIdInstituc() {
        return idInstituc;
    }

    public void setIdInstituc(TblSauregInstitucion idInstituc) {
        this.idInstituc = idInstituc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSistInst != null ? idSistInst.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregSistInstitucion)) {
            return false;
        }
        TblSauregSistInstitucion other = (TblSauregSistInstitucion) object;
        if ((this.idSistInst == null && other.idSistInst != null) || (this.idSistInst != null && !this.idSistInst.equals(other.idSistInst))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregSistInstitucion[ idSistInst=" + idSistInst + " ]";
    }
    
}
