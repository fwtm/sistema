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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan.hernandez
 */
@Entity
@Table(name = "CATALOGO_RENAVI", catalog = "", schema = "REVIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoRenavi.findAll", query = "SELECT c FROM CatalogoRenavi c"),
    @NamedQuery(name = "CatalogoRenavi.findByIdCatRenavi", query = "SELECT c FROM CatalogoRenavi c WHERE c.idCatRenavi = :idCatRenavi"),
    @NamedQuery(name = "CatalogoRenavi.findByNombreCatRenavi", query = "SELECT c FROM CatalogoRenavi c WHERE c.nombreCatRenavi = :nombreCatRenavi"),
    @NamedQuery(name = "CatalogoRenavi.findByDescripcionCatRenavi", query = "SELECT c FROM CatalogoRenavi c WHERE c.descripcionCatRenavi = :descripcionCatRenavi"),
    @NamedQuery(name = "CatalogoRenavi.findByValorCatRenavi", query = "SELECT c FROM CatalogoRenavi c WHERE c.valorCatRenavi = :valorCatRenavi"),
    @NamedQuery(name = "CatalogoRenavi.findByCondicionCatRenavi", query = "SELECT c FROM CatalogoRenavi c WHERE c.condicionCatRenavi = :condicionCatRenavi"),
    @NamedQuery(name = "CatalogoRenavi.findByStatusCatRenavi", query = "SELECT c FROM CatalogoRenavi c WHERE c.statusCatRenavi = :statusCatRenavi")})
public class CatalogoRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CAT_RENAVI", nullable = false, precision = 22)
    private BigDecimal idCatRenavi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "NOMBRE_CAT_RENAVI", nullable = false, length = 150)
    private String nombreCatRenavi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPCION_CAT_RENAVI", nullable = false, length = 100)
    private String descripcionCatRenavi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "VALOR_CAT_RENAVI", nullable = false, length = 50)
    private String valorCatRenavi;
    @Size(max = 150)
    @Column(name = "CONDICION_CAT_RENAVI", length = 150)
    private String condicionCatRenavi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "STATUS_CAT_RENAVI", nullable = false, length = 1)
    private String statusCatRenavi;
    @JoinTable(name = "MALFOR_NACIDO_VIVO_RENAVI", joinColumns = {
        @JoinColumn(name = "ID_CAT_RENAVI", referencedColumnName = "ID_CAT_RENAVI", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ID_NAC_VIV", referencedColumnName = "ID_NAC_VIV", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public CatalogoRenavi() {
    }

    public CatalogoRenavi(BigDecimal idCatRenavi) {
        this.idCatRenavi = idCatRenavi;
    }

    public CatalogoRenavi(BigDecimal idCatRenavi, String nombreCatRenavi, String descripcionCatRenavi, String valorCatRenavi, String statusCatRenavi) {
        this.idCatRenavi = idCatRenavi;
        this.nombreCatRenavi = nombreCatRenavi;
        this.descripcionCatRenavi = descripcionCatRenavi;
        this.valorCatRenavi = valorCatRenavi;
        this.statusCatRenavi = statusCatRenavi;
    }

    public BigDecimal getIdCatRenavi() {
        return idCatRenavi;
    }

    public void setIdCatRenavi(BigDecimal idCatRenavi) {
        this.idCatRenavi = idCatRenavi;
    }

    public String getNombreCatRenavi() {
        return nombreCatRenavi;
    }

    public void setNombreCatRenavi(String nombreCatRenavi) {
        this.nombreCatRenavi = nombreCatRenavi;
    }

    public String getDescripcionCatRenavi() {
        return descripcionCatRenavi;
    }

    public void setDescripcionCatRenavi(String descripcionCatRenavi) {
        this.descripcionCatRenavi = descripcionCatRenavi;
    }

    public String getValorCatRenavi() {
        return valorCatRenavi;
    }

    public void setValorCatRenavi(String valorCatRenavi) {
        this.valorCatRenavi = valorCatRenavi;
    }

    public String getCondicionCatRenavi() {
        return condicionCatRenavi;
    }

    public void setCondicionCatRenavi(String condicionCatRenavi) {
        this.condicionCatRenavi = condicionCatRenavi;
    }

    public String getStatusCatRenavi() {
        return statusCatRenavi;
    }

    public void setStatusCatRenavi(String statusCatRenavi) {
        this.statusCatRenavi = statusCatRenavi;
    }

    @XmlTransient
    public List<NacidoVivoRenavi> getNacidoVivoRenaviList() {
        return nacidoVivoRenaviList;
    }

    public void setNacidoVivoRenaviList(List<NacidoVivoRenavi> nacidoVivoRenaviList) {
        this.nacidoVivoRenaviList = nacidoVivoRenaviList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCatRenavi != null ? idCatRenavi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatalogoRenavi)) {
            return false;
        }
        CatalogoRenavi other = (CatalogoRenavi) object;
        if ((this.idCatRenavi == null && other.idCatRenavi != null) || (this.idCatRenavi != null && !this.idCatRenavi.equals(other.idCatRenavi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.CatalogoRenavi[ idCatRenavi=" + idCatRenavi + " ]";
    }
    
}
