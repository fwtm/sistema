/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.util.List;

/**
 *
 * @author daniel.porras
 */
public class UsuarioReporteDetallado {
    private String institucion;
    private String agencia;
    private String codMSP;
    private String nombres;
    private String cedula;
    private List<NacidoVivoReporteDetallado> nacVivDetallado;
    private int anulados;
    private int firmados;
    private int noFirmados;
    private int inscritos;
    private int incompletos;
    private int total;
    private String provincia;

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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<NacidoVivoReporteDetallado> getNacVivDetallado() {
        return nacVivDetallado;
    }

    public void setNacVivDetallado(List<NacidoVivoReporteDetallado> nacVivDetallado) {
        this.nacVivDetallado = nacVivDetallado;
    }

    public int getAnulados() {
        return anulados;
    }

    public void setAnulados(int anulados) {
        this.anulados = anulados;
    }

    public int getFirmados() {
        return firmados;
    }

    public void setFirmados(int firmados) {
        this.firmados = firmados;
    }

    public int getNoFirmados() {
        return noFirmados;
    }

    public void setNoFirmados(int noFirmados) {
        this.noFirmados = noFirmados;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }

    public int getIncompletos() {
        return incompletos;
    }

    public void setIncompletos(int incompletos) {
        this.incompletos = incompletos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
}
