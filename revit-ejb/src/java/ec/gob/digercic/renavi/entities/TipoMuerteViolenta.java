/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "TIPO_MUERTE_VIOLENTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoMuerteViolenta.findAll", query = "SELECT t FROM TipoMuerteViolenta t"),
    @NamedQuery(name = "TipoMuerteViolenta.findByIdTipMueVio", query = "SELECT t FROM TipoMuerteViolenta t WHERE t.idTipMueVio = :idTipMueVio"),
    @NamedQuery(name = "TipoMuerteViolenta.findByNombreTipMueVio", query = "SELECT t FROM TipoMuerteViolenta t WHERE t.nombreTipMueVio = :nombreTipMueVio")})
public class TipoMuerteViolenta implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_MUE_VIO")
    private Integer idTipMueVio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_TIP_MUE_VIO")
    private String nombreTipMueVio;
    @OneToMany(mappedBy = "fkIdTipMueVio", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public TipoMuerteViolenta() {
    }

    public TipoMuerteViolenta(Integer idTipMueVio) {
        this.idTipMueVio = idTipMueVio;
    }

    public TipoMuerteViolenta(Integer idTipMueVio, String nombreTipMueVio) {
        this.idTipMueVio = idTipMueVio;
        this.nombreTipMueVio = nombreTipMueVio;
    }

    public Integer getIdTipMueVio() {
        return idTipMueVio;
    }

    public void setIdTipMueVio(Integer idTipMueVio) {
        this.idTipMueVio = idTipMueVio;
    }

    public String getNombreTipMueVio() {
        return nombreTipMueVio;
    }

    public void setNombreTipMueVio(String nombreTipMueVio) {
        this.nombreTipMueVio = nombreTipMueVio;
    }

    @XmlTransient
    public List<Fallecido> getFallecidoList() {
        return fallecidoList;
    }

    public void setFallecidoList(List<Fallecido> fallecidoList) {
        this.fallecidoList = fallecidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipMueVio != null ? idTipMueVio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoMuerteViolenta)) {
            return false;
        }
        TipoMuerteViolenta other = (TipoMuerteViolenta) object;
        if ((this.idTipMueVio == null && other.idTipMueVio != null) || (this.idTipMueVio != null && !this.idTipMueVio.equals(other.idTipMueVio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.TipoMuerteViolenta[ idTipMueVio=" + idTipMueVio + " ]";
    }
    
}
