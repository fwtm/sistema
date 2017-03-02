/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author daniel.porras
 */
public class NacidoVivoReporteDetallado {
    private String provincia;
    private BigDecimal id;
    private String cedula;
    private String codigoRc;
    private String historiaClinica;
    private String nombres;
    private Date fechaNacimiento;
    private String nombresMadre;
    private String cedulaMadre;
    private String nombresPadre;
    private String cedulaPadre;
    private byte[] pdf;
    private String estado;  

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCodigoRc() {
        return codigoRc;
    }

    public void setCodigoRc(String codigoRc) {
        this.codigoRc = codigoRc;
    }

    public String getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(String historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombresMadre() {
        return nombresMadre;
    }

    public void setNombresMadre(String nombresMadre) {
        this.nombresMadre = nombresMadre;
    }

    public String getCedulaMadre() {
        return cedulaMadre;
    }

    public void setCedulaMadre(String cedulaMadre) {
        this.cedulaMadre = cedulaMadre;
    }

    public String getNombresPadre() {
        return nombresPadre;
    }

    public void setNombresPadre(String nombresPadre) {
        this.nombresPadre = nombresPadre;
    }

    public String getCedulaPadre() {
        return cedulaPadre;
    }

    public void setCedulaPadre(String cedulaPadre) {
        this.cedulaPadre = cedulaPadre;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

}
