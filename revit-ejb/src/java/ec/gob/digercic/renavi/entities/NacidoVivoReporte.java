/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.math.BigDecimal;

/**
 *
 * @author daniel.porras
 */
public class NacidoVivoReporte {
    private String provincia;
    private String institucion;
    private String agencia;
    private String codMSP;
    private BigDecimal incompletos;
    private BigDecimal noFirmados;
    private BigDecimal firmados;
    private BigDecimal inscritos;
    private BigDecimal anulados;
    private BigDecimal totalPorAgencia;

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getCodMSP() {
        return codMSP;
    }

    public void setCodMSP(String codMSP) {
        this.codMSP = codMSP;
    }

    public BigDecimal getIncompletos() {
        return incompletos;
    }

    public void setIncompletos(BigDecimal incompletos) {
        this.incompletos = incompletos;
    }

    public BigDecimal getNoFirmados() {
        return noFirmados;
    }

    public void setNoFirmados(BigDecimal noFirmados) {
        this.noFirmados = noFirmados;
    }

    public BigDecimal getFirmados() {
        return firmados;
    }

    public void setFirmados(BigDecimal firmados) {
        this.firmados = firmados;
    }

    public BigDecimal getInscritos() {
        return inscritos;
    }

    public void setInscritos(BigDecimal inscritos) {
        this.inscritos = inscritos;
    }

    public BigDecimal getAnulados() {
        return anulados;
    }

    public void setAnulados(BigDecimal anulados) {
        this.anulados = anulados;
    }

    public BigDecimal getTotalPorAgencia() {
        return totalPorAgencia;
    }

    public void setTotalPorAgencia(BigDecimal totalPorAgencia) {
        this.totalPorAgencia = totalPorAgencia;
    }

}
