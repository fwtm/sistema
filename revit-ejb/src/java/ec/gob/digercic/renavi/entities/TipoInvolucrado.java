/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "TIPO_INVOLUCRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoInvolucrado.findAll", query = "SELECT t FROM TipoInvolucrado t"),
    @NamedQuery(name = "TipoInvolucrado.findByIdTipInv", query = "SELECT t FROM TipoInvolucrado t WHERE t.idTipInv = :idTipInv"),
    @NamedQuery(name = "TipoInvolucrado.findByNombreTipInv", query = "SELECT t FROM TipoInvolucrado t WHERE t.nombreTipInv = :nombreTipInv"),
    @NamedQuery(name = "TipoInvolucrado.findByDescripcionTipInv", query = "SELECT t FROM TipoInvolucrado t WHERE t.descripcionTipInv = :descripcionTipInv")})
public class TipoInvolucrado implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_INV")
    private Integer idTipInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_TIP_INV")
    private String nombreTipInv;
    @Size(max = 200)
    @Column(name = "DESCRIPCION_TIP_INV")
    private String descripcionTipInv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoInvolucrado", fetch = FetchType.LAZY)
    private List<SolicitanteFallecimiento> solicitanteFallecimientoList;

    public TipoInvolucrado() {
    }

    public TipoInvolucrado(Integer idTipInv) {
        this.idTipInv = idTipInv;
    }

    public TipoInvolucrado(Integer idTipInv, String nombreTipInv) {
        this.idTipInv = idTipInv;
        this.nombreTipInv = nombreTipInv;
    }

    public Integer getIdTipInv() {
        return idTipInv;
    }

    public void setIdTipInv(Integer idTipInv) {
        this.idTipInv = idTipInv;
    }

    public String getNombreTipInv() {
        return nombreTipInv;
    }

    public void setNombreTipInv(String nombreTipInv) {
        this.nombreTipInv = nombreTipInv;
    }

    public String getDescripcionTipInv() {
        return descripcionTipInv;
    }

    public void setDescripcionTipInv(String descripcionTipInv) {
        this.descripcionTipInv = descripcionTipInv;
    }

    @XmlTransient
    public List<SolicitanteFallecimiento> getSolicitanteFallecimientoList() {
        return solicitanteFallecimientoList;
    }

    public void setSolicitanteFallecimientoList(List<SolicitanteFallecimiento> solicitanteFallecimientoList) {
        this.solicitanteFallecimientoList = solicitanteFallecimientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipInv != null ? idTipInv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoInvolucrado)) {
            return false;
        }
        TipoInvolucrado other = (TipoInvolucrado) object;
        if ((this.idTipInv == null && other.idTipInv != null) || (this.idTipInv != null && !this.idTipInv.equals(other.idTipInv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.TipoInvolucrado[ idTipInv=" + idTipInv + " ]";
    }
    
}
