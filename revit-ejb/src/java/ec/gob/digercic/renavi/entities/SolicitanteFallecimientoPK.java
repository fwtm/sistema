/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author daniel.porras
 */
@Embeddable
public class SolicitanteFallecimientoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PAR")
    private BigInteger idPar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FAL")
    private BigInteger idFal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_INV")
    private BigInteger idTipInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CIU_INV")
    private BigInteger idCiuInv;

    public SolicitanteFallecimientoPK() {
    }

    public SolicitanteFallecimientoPK(BigInteger idPar, BigInteger idFal, BigInteger idTipInv, BigInteger idCiuInv) {
        this.idPar = idPar;
        this.idFal = idFal;
        this.idTipInv = idTipInv;
        this.idCiuInv = idCiuInv;
    }

    public BigInteger getIdPar() {
        return idPar;
    }

    public void setIdPar(BigInteger idPar) {
        this.idPar = idPar;
    }

    public BigInteger getIdFal() {
        return idFal;
    }

    public void setIdFal(BigInteger idFal) {
        this.idFal = idFal;
    }

    public BigInteger getIdTipInv() {
        return idTipInv;
    }

    public void setIdTipInv(BigInteger idTipInv) {
        this.idTipInv = idTipInv;
    }

    public BigInteger getIdCiuInv() {
        return idCiuInv;
    }

    public void setIdCiuInv(BigInteger idCiuInv) {
        this.idCiuInv = idCiuInv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPar != null ? idPar.hashCode() : 0);
        hash += (idFal != null ? idFal.hashCode() : 0);
        hash += (idTipInv != null ? idTipInv.hashCode() : 0);
        hash += (idCiuInv != null ? idCiuInv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitanteFallecimientoPK)) {
            return false;
        }
        SolicitanteFallecimientoPK other = (SolicitanteFallecimientoPK) object;
        if ((this.idPar == null && other.idPar != null) || (this.idPar != null && !this.idPar.equals(other.idPar))) {
            return false;
        }
        if ((this.idFal == null && other.idFal != null) || (this.idFal != null && !this.idFal.equals(other.idFal))) {
            return false;
        }
        if ((this.idTipInv == null && other.idTipInv != null) || (this.idTipInv != null && !this.idTipInv.equals(other.idTipInv))) {
            return false;
        }
        if ((this.idCiuInv == null && other.idCiuInv != null) || (this.idCiuInv != null && !this.idCiuInv.equals(other.idCiuInv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.SolicitanteFallecimientoPK[ idPar=" + idPar + ", idFal=" + idFal + ", idTipInv=" + idTipInv + ", idCiuInv=" + idCiuInv + " ]";
    }
    
}
