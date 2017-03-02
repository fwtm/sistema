/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "CERTIFICADO_POR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CertificadoPor.findAll", query = "SELECT c FROM CertificadoPor c"),
    @NamedQuery(name = "CertificadoPor.findByIdCerPor", query = "SELECT c FROM CertificadoPor c WHERE c.idCerPor = :idCerPor"),
    @NamedQuery(name = "CertificadoPor.findByNombreCerPor", query = "SELECT c FROM CertificadoPor c WHERE c.nombreCerPor = :nombreCerPor")})
public class CertificadoPor implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CER_POR")
    private Integer idCerPor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE_CER_POR")
    private String nombreCerPor;
    @OneToMany(mappedBy = "fkIdCerPor", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public CertificadoPor() {
    }

    public CertificadoPor(Integer idCerPor) {
        this.idCerPor = idCerPor;
    }

    public CertificadoPor(Integer idCerPor, String nombreCerPor) {
        this.idCerPor = idCerPor;
        this.nombreCerPor = nombreCerPor;
    }

    public Integer getIdCerPor() {
        return idCerPor;
    }

    public void setIdCerPor(Integer idCerPor) {
        this.idCerPor = idCerPor;
    }

    public String getNombreCerPor() {
        return nombreCerPor;
    }

    public void setNombreCerPor(String nombreCerPor) {
        this.nombreCerPor = nombreCerPor;
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
        hash += (idCerPor != null ? idCerPor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CertificadoPor)) {
            return false;
        }
        CertificadoPor other = (CertificadoPor) object;
        if ((this.idCerPor == null && other.idCerPor != null) || (this.idCerPor != null && !this.idCerPor.equals(other.idCerPor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreCerPor;
    }
    
}
