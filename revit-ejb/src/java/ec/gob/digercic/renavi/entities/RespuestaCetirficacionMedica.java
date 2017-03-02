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
@Table(name = "RESPUESTA_CETIRFICACION_MEDICA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RespuestaCetirficacionMedica.findAll", query = "SELECT r FROM RespuestaCetirficacionMedica r"),
    @NamedQuery(name = "RespuestaCetirficacionMedica.findByIdResCerMed", query = "SELECT r FROM RespuestaCetirficacionMedica r WHERE r.idResCerMed = :idResCerMed"),
    @NamedQuery(name = "RespuestaCetirficacionMedica.findByValorResCerMed", query = "SELECT r FROM RespuestaCetirficacionMedica r WHERE r.valorResCerMed = :valorResCerMed")})
public class RespuestaCetirficacionMedica implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RES_CER_MED")
    private Integer idResCerMed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "VALOR_RES_CER_MED")
    private String valorResCerMed;
    @OneToMany(mappedBy = "fkIdResCerMed", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public RespuestaCetirficacionMedica() {
    }

    public RespuestaCetirficacionMedica(Integer idResCerMed) {
        this.idResCerMed = idResCerMed;
    }

    public RespuestaCetirficacionMedica(Integer idResCerMed, String valorResCerMed) {
        this.idResCerMed = idResCerMed;
        this.valorResCerMed = valorResCerMed;
    }

    public Integer getIdResCerMed() {
        return idResCerMed;
    }

    public void setIdResCerMed(Integer idResCerMed) {
        this.idResCerMed = idResCerMed;
    }

    public String getValorResCerMed() {
        return valorResCerMed;
    }

    public void setValorResCerMed(String valorResCerMed) {
        this.valorResCerMed = valorResCerMed;
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
        hash += (idResCerMed != null ? idResCerMed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RespuestaCetirficacionMedica)) {
            return false;
        }
        RespuestaCetirficacionMedica other = (RespuestaCetirficacionMedica) object;
        if ((this.idResCerMed == null && other.idResCerMed != null) || (this.idResCerMed != null && !this.idResCerMed.equals(other.idResCerMed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return valorResCerMed;
    }
    
}
