/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "CIUDADANO_INVOLUCRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CiudadanoInvolucrado.findAll", query = "SELECT c FROM CiudadanoInvolucrado c"),
    @NamedQuery(name = "CiudadanoInvolucrado.findByIdCiuInv", query = "SELECT c FROM CiudadanoInvolucrado c WHERE c.idCiuInv = :idCiuInv"),
    @NamedQuery(name = "CiudadanoInvolucrado.findByNombreCiuInv", query = "SELECT c FROM CiudadanoInvolucrado c WHERE c.nombreCiuInv = :nombreCiuInv"),
    @NamedQuery(name = "CiudadanoInvolucrado.findByCedulaCiuInv", query = "SELECT c FROM CiudadanoInvolucrado c WHERE c.cedulaCiuInv = :cedulaCiuInv"),
    @NamedQuery(name = "CiudadanoInvolucrado.findByPasaporteCiuInv", query = "SELECT c FROM CiudadanoInvolucrado c WHERE c.pasaporteCiuInv = :pasaporteCiuInv"),
    @NamedQuery(name = "CiudadanoInvolucrado.findByFechaNacimientoCiuInv", query = "SELECT c FROM CiudadanoInvolucrado c WHERE c.fechaNacimientoCiuInv = :fechaNacimientoCiuInv")})
public class CiudadanoInvolucrado implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CIU_INV")
    private Integer idCiuInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "NOMBRE_CIU_INV")
    private String nombreCiuInv;
    @Size(max = 10)
    @Column(name = "CEDULA_CIU_INV")
    private String cedulaCiuInv;
    @Size(max = 20)
    @Column(name = "PASAPORTE_CIU_INV")
    private String pasaporteCiuInv;
    @Column(name = "FECHA_NACIMIENTO_CIU_INV")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoCiuInv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciudadanoInvolucrado", fetch = FetchType.LAZY)
    private List<SolicitanteFallecimiento> solicitanteFallecimientoList;

    public CiudadanoInvolucrado() {
    }

    public CiudadanoInvolucrado(Integer idCiuInv) {
        this.idCiuInv = idCiuInv;
    }

    public CiudadanoInvolucrado(Integer idCiuInv, String nombreCiuInv) {
        this.idCiuInv = idCiuInv;
        this.nombreCiuInv = nombreCiuInv;
    }

    public Integer getIdCiuInv() {
        return idCiuInv;
    }

    public void setIdCiuInv(Integer idCiuInv) {
        this.idCiuInv = idCiuInv;
    }

    public String getNombreCiuInv() {
        return nombreCiuInv;
    }

    public void setNombreCiuInv(String nombreCiuInv) {
        this.nombreCiuInv = nombreCiuInv;
    }

    public String getCedulaCiuInv() {
        return cedulaCiuInv;
    }

    public void setCedulaCiuInv(String cedulaCiuInv) {
        this.cedulaCiuInv = cedulaCiuInv;
    }

    public String getPasaporteCiuInv() {
        return pasaporteCiuInv;
    }

    public void setPasaporteCiuInv(String pasaporteCiuInv) {
        this.pasaporteCiuInv = pasaporteCiuInv;
    }

    public Date getFechaNacimientoCiuInv() {
        return fechaNacimientoCiuInv;
    }

    public void setFechaNacimientoCiuInv(Date fechaNacimientoCiuInv) {
        this.fechaNacimientoCiuInv = fechaNacimientoCiuInv;
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
        hash += (idCiuInv != null ? idCiuInv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CiudadanoInvolucrado)) {
            return false;
        }
        CiudadanoInvolucrado other = (CiudadanoInvolucrado) object;
        if ((this.idCiuInv == null && other.idCiuInv != null) || (this.idCiuInv != null && !this.idCiuInv.equals(other.idCiuInv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.CiudadanoInvolucrado[ idCiuInv=" + idCiuInv + " ]";
    }
    
}
