/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "SOLICITANTE_FALLECIMIENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitanteFallecimiento.findAll", query = "SELECT s FROM SolicitanteFallecimiento s"),
    @NamedQuery(name = "SolicitanteFallecimiento.findByIdPar", query = "SELECT s FROM SolicitanteFallecimiento s WHERE s.solicitanteFallecimientoPK.idPar = :idPar"),
    @NamedQuery(name = "SolicitanteFallecimiento.findByIdFal", query = "SELECT s FROM SolicitanteFallecimiento s WHERE s.solicitanteFallecimientoPK.idFal = :idFal"),
    @NamedQuery(name = "SolicitanteFallecimiento.findByIdTipInv", query = "SELECT s FROM SolicitanteFallecimiento s WHERE s.solicitanteFallecimientoPK.idTipInv = :idTipInv"),
    @NamedQuery(name = "SolicitanteFallecimiento.findByIdCiuInv", query = "SELECT s FROM SolicitanteFallecimiento s WHERE s.solicitanteFallecimientoPK.idCiuInv = :idCiuInv")
})
public class SolicitanteFallecimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SolicitanteFallecimientoPK solicitanteFallecimientoPK;
    @Column(name = "EDAD_SOL_FAL")
    private BigInteger edadSolFal;
    @Size(max = 150)
    @Column(name = "CAUSA_PROBABLE_MUERTE")
    private String causaProbableMuerte;
    @Size(max = 200)
    @Column(name = "SINTOMAS_SOL_FAL")
    private String sintomasSolFal;
    @Size(max = 100)
    @Column(name = "DIRECCION_SOL_FAL")
    private String direccionSolFal;
    @Size(max = 10)
    @Column(name = "TELEFONO_SOL_FAL")
    private String telefonoSolFal;
    @Lob
    @Column(name = "FIRMA_SOL_FAL")
    private Serializable firmaSolFal;
    @JoinColumn(name = "ID_TIP_INV", referencedColumnName = "ID_TIP_INV", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoInvolucrado tipoInvolucrado;
    @JoinColumn(name = "ID_PAR", referencedColumnName = "ID_PAR", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Parentesco parentesco;
    @JoinColumn(name = "ID_FAL", referencedColumnName = "ID_FAL", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Fallecido fallecido;
    @JoinColumn(name = "ID_CIU_INV", referencedColumnName = "ID_CIU_INV", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiudadanoInvolucrado ciudadanoInvolucrado;

    public SolicitanteFallecimiento() {
    }

    public SolicitanteFallecimiento(SolicitanteFallecimientoPK solicitanteFallecimientoPK) {
        this.solicitanteFallecimientoPK = solicitanteFallecimientoPK;
    }

    public SolicitanteFallecimiento(BigInteger idPar, BigInteger idFal, BigInteger idTipInv, BigInteger idCiuInv) {
        this.solicitanteFallecimientoPK = new SolicitanteFallecimientoPK(idPar, idFal, idTipInv, idCiuInv);
    }

    public SolicitanteFallecimientoPK getSolicitanteFallecimientoPK() {
        return solicitanteFallecimientoPK;
    }

    public void setSolicitanteFallecimientoPK(SolicitanteFallecimientoPK solicitanteFallecimientoPK) {
        this.solicitanteFallecimientoPK = solicitanteFallecimientoPK;
    }

    public BigInteger getEdadSolFal() {
        return edadSolFal;
    }

    public void setEdadSolFal(BigInteger edadSolFal) {
        this.edadSolFal = edadSolFal;
    }

    public String getCausaProbableMuerte() {
        return causaProbableMuerte;
    }

    public void setCausaProbableMuerte(String causaProbableMuerte) {
        this.causaProbableMuerte = causaProbableMuerte;
    }

    public String getSintomasSolFal() {
        return sintomasSolFal;
    }

    public void setSintomasSolFal(String sintomasSolFal) {
        this.sintomasSolFal = sintomasSolFal;
    }

    public String getDireccionSolFal() {
        return direccionSolFal;
    }

    public void setDireccionSolFal(String direccionSolFal) {
        this.direccionSolFal = direccionSolFal;
    }

    public String getTelefonoSolFal() {
        return telefonoSolFal;
    }

    public void setTelefonoSolFal(String telefonoSolFal) {
        this.telefonoSolFal = telefonoSolFal;
    }

    public Serializable getFirmaSolFal() {
        return firmaSolFal;
    }

    public void setFirmaSolFal(Serializable firmaSolFal) {
        this.firmaSolFal = firmaSolFal;
    }

    public TipoInvolucrado getTipoInvolucrado() {
        return tipoInvolucrado;
    }

    public void setTipoInvolucrado(TipoInvolucrado tipoInvolucrado) {
        this.tipoInvolucrado = tipoInvolucrado;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public Fallecido getFallecido() {
        return fallecido;
    }

    public void setFallecido(Fallecido fallecido) {
        this.fallecido = fallecido;
    }

    public CiudadanoInvolucrado getCiudadanoInvolucrado() {
        return ciudadanoInvolucrado;
    }

    public void setCiudadanoInvolucrado(CiudadanoInvolucrado ciudadanoInvolucrado) {
        this.ciudadanoInvolucrado = ciudadanoInvolucrado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solicitanteFallecimientoPK != null ? solicitanteFallecimientoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitanteFallecimiento)) {
            return false;
        }
        SolicitanteFallecimiento other = (SolicitanteFallecimiento) object;
        if ((this.solicitanteFallecimientoPK == null && other.solicitanteFallecimientoPK != null) || (this.solicitanteFallecimientoPK != null && !this.solicitanteFallecimientoPK.equals(other.solicitanteFallecimientoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.SolicitanteFallecimiento[ solicitanteFallecimientoPK=" + solicitanteFallecimientoPK + " ]";
    }
    
}
