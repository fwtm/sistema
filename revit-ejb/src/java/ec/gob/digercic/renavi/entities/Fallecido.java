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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
@Table(name = "FALLECIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fallecido.findAll", query = "SELECT f FROM Fallecido f"),
    @NamedQuery(name = "Fallecido.findByIdFal", query = "SELECT f FROM Fallecido f WHERE f.idFal = :idFal"),
    @NamedQuery(name = "Fallecido.findByFechaActualizacionFal", query = "SELECT f FROM Fallecido f WHERE f.fechaActualizacionFal = :fechaActualizacionFal"),
    @NamedQuery(name = "Fallecido.findByCedulaFal", query = "SELECT f FROM Fallecido f WHERE f.cedulaFal = :cedulaFal"),
    @NamedQuery(name = "Fallecido.findByIdentificacionPasaporteFal", query = "SELECT f FROM Fallecido f WHERE f.identificacionPasaporteFal = :identificacionPasaporteFal"),
    @NamedQuery(name = "Fallecido.findByNombreFal", query = "SELECT f FROM Fallecido f WHERE f.nombreFal = :nombreFal"),
    @NamedQuery(name = "Fallecido.findByNombrePadreFal", query = "SELECT f FROM Fallecido f WHERE f.nombrePadreFal = :nombrePadreFal"),
    @NamedQuery(name = "Fallecido.findByNombreMadreFal", query = "SELECT f FROM Fallecido f WHERE f.nombreMadreFal = :nombreMadreFal"),
    @NamedQuery(name = "Fallecido.findByFechaNacimientoFal", query = "SELECT f FROM Fallecido f WHERE f.fechaNacimientoFal = :fechaNacimientoFal"),
    @NamedQuery(name = "Fallecido.findByFechaFallecimientoFal", query = "SELECT f FROM Fallecido f WHERE f.fechaFallecimientoFal = :fechaFallecimientoFal"),
    @NamedQuery(name = "Fallecido.findByEdadFal", query = "SELECT f FROM Fallecido f WHERE f.edadFal = :edadFal"),
    @NamedQuery(name = "Fallecido.findByProvinciaIdFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.provinciaIdFllcmntFal = :provinciaIdFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByProvinciaDsFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.provinciaDsFllcmntFal = :provinciaDsFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByCantonIdFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.cantonIdFllcmntFal = :cantonIdFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByCantonDsFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.cantonDsFllcmntFal = :cantonDsFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByCiudadFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.ciudadFllcmntFal = :ciudadFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByParroquiaIdFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.parroquiaIdFllcmntFal = :parroquiaIdFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByParroquiaDsFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.parroquiaDsFllcmntFal = :parroquiaDsFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByLocalidadFllcmntFal", query = "SELECT f FROM Fallecido f WHERE f.localidadFllcmntFal = :localidadFllcmntFal"),
    @NamedQuery(name = "Fallecido.findByProvinciaIdRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.provinciaIdRsdncFal = :provinciaIdRsdncFal"),
    @NamedQuery(name = "Fallecido.findByProvinciaDsRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.provinciaDsRsdncFal = :provinciaDsRsdncFal"),
    @NamedQuery(name = "Fallecido.findByCantonIdRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.cantonIdRsdncFal = :cantonIdRsdncFal"),
    @NamedQuery(name = "Fallecido.findByCantonDsRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.cantonDsRsdncFal = :cantonDsRsdncFal"),
    @NamedQuery(name = "Fallecido.findByCiudadRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.ciudadRsdncFal = :ciudadRsdncFal"),
    @NamedQuery(name = "Fallecido.findByParroquiaIdRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.parroquiaIdRsdncFal = :parroquiaIdRsdncFal"),
    @NamedQuery(name = "Fallecido.findByParroquiaDsRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.parroquiaDsRsdncFal = :parroquiaDsRsdncFal"),
    @NamedQuery(name = "Fallecido.findByLocalidadRsdncFal", query = "SELECT f FROM Fallecido f WHERE f.localidadRsdncFal = :localidadRsdncFal"),
    @NamedQuery(name = "Fallecido.findByCausaMuertaAFal", query = "SELECT f FROM Fallecido f WHERE f.causaMuertaAFal = :causaMuertaAFal"),
    @NamedQuery(name = "Fallecido.findByTiempoMuerteAFal", query = "SELECT f FROM Fallecido f WHERE f.tiempoMuerteAFal = :tiempoMuerteAFal"),
    @NamedQuery(name = "Fallecido.findByCausaMuertaBFal", query = "SELECT f FROM Fallecido f WHERE f.causaMuertaBFal = :causaMuertaBFal"),
    @NamedQuery(name = "Fallecido.findByTiempoMuerteBFal", query = "SELECT f FROM Fallecido f WHERE f.tiempoMuerteBFal = :tiempoMuerteBFal"),
    @NamedQuery(name = "Fallecido.findByCausaMuerteCFal", query = "SELECT f FROM Fallecido f WHERE f.causaMuerteCFal = :causaMuerteCFal"),
    @NamedQuery(name = "Fallecido.findByTiempoMuerteCFal", query = "SELECT f FROM Fallecido f WHERE f.tiempoMuerteCFal = :tiempoMuerteCFal"),
    @NamedQuery(name = "Fallecido.findByCausaMuerteDFal", query = "SELECT f FROM Fallecido f WHERE f.causaMuerteDFal = :causaMuerteDFal"),
    @NamedQuery(name = "Fallecido.findByTiempoMuerteDFal", query = "SELECT f FROM Fallecido f WHERE f.tiempoMuerteDFal = :tiempoMuerteDFal"),
    @NamedQuery(name = "Fallecido.findByOtrosMotivosPatologicosFal", query = "SELECT f FROM Fallecido f WHERE f.otrosMotivosPatologicosFal = :otrosMotivosPatologicosFal"),
    @NamedQuery(name = "Fallecido.findByTiempoMuerteOtrsMotivFal", query = "SELECT f FROM Fallecido f WHERE f.tiempoMuerteOtrsMotivFal = :tiempoMuerteOtrsMotivFal"),
    @NamedQuery(name = "Fallecido.findByFkUsuSaureg", query = "SELECT f FROM Fallecido f WHERE f.fkUsuSaureg = :fkUsuSaureg"),
    @NamedQuery(name = "Fallecido.findByFkAgenciaSaureg", query = "SELECT f FROM Fallecido f WHERE f.fkAgenciaSaureg = :fkAgenciaSaureg"),
    @NamedQuery(name = "Fallecido.findByFkUsuFirmaSaureg", query = "SELECT f FROM Fallecido f WHERE f.fkUsuFirmaSaureg = :fkUsuFirmaSaureg"),
    @NamedQuery(name = "Fallecido.findByFkAgenciaFirmaSaureg", query = "SELECT f FROM Fallecido f WHERE f.fkAgenciaFirmaSaureg = :fkAgenciaFirmaSaureg"),
    @NamedQuery(name = "Fallecido.findByDescripcionMuerteViolenta", query = "SELECT f FROM Fallecido f WHERE f.descripcionMuerteViolenta = :descripcionMuerteViolenta"),
    @NamedQuery(name = "Fallecido.findByCondicionCeduladoFal", query = "SELECT f FROM Fallecido f WHERE f.condicionCeduladoFal = :condicionCeduladoFal"),
    @NamedQuery(name = "Fallecido.findByCodigoPaisFal", query = "SELECT f FROM Fallecido f WHERE f.codigoPaisFal = :codigoPaisFal"),
    @NamedQuery(name = "Fallecido.findByConyugeFal", query = "SELECT f FROM Fallecido f WHERE f.conyugeFal = :conyugeFal"),
    @NamedQuery(name = "Fallecido.findByStatusFal", query = "SELECT f FROM Fallecido f WHERE f.statusFal = :statusFal"),
    
    //Por Daniel
    @NamedQuery(name = "Fallecido.findAllByInstitucion", query = "SELECT f FROM Fallecido f WHERE f.statusFal = :status  AND f.fkAgenciaSaureg = :fkAgenciaSaureg AND f.fkUsuSaureg = :fkUsuSaureg AND f.fkIdEstadoFirma.idEstFir IN (SELECT ef.idEstFir FROM EstadoFirmaRenavi ef WHERE ef.idEstFir = :estFirma) AND f.fkIdEstRegFal.idEstRegFal NOT IN (SELECT er.idEstRegFal FROM EstadoRegistroFallecido er WHERE er.idEstRegFal = :estRegistro) ORDER BY f.fechaCreacionFal DESC"),
    @NamedQuery(name = "Fallecido.findInFechaCreacion", query = "SELECT f FROM Fallecido f WHERE (f.fechaCreacionFal BETWEEN :fechaInicio AND :fechaFin) AND f.fkUsuFirmaSaureg = :usuario AND  f.statusFal = :statusF AND f.fkAgenciaFirmaSaureg = :idInstitucion AND f.fkIdEstadoFirma.idEstFir = :estFirma AND f.fkIdEstRegFal.idEstRegFal IN :estRegistro AND f.pdfSinFirma IS NOT NULL ORDER BY f.fechaCreacionFal ASC"),
    @NamedQuery(name = "Fallecido.findInFechaFirma", query = "SELECT f FROM Fallecido f WHERE (f.fechaFirmaFal BETWEEN :fechaInicio AND :fechaFin) AND f.fkUsuFirmaSaureg = :usuario AND  f.statusFal = :statusF AND f.fkAgenciaFirmaSaureg = :idInstitucion AND f.fkIdEstadoFirma.idEstFir = :estFirma AND f.fkIdEstRegFal.idEstRegFal IN :estRegistro AND f.pdfConFirma IS NOT NULL ORDER BY f.fechaCreacionFal DESC"),
})
public class Fallecido implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SEQ_RENAVI_FALLECIDO_ID", sequenceName = "SEQ_RENAVI_FALLECIDO_ID", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_FALLECIDO_ID")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FAL")
    private Long idFal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION_FAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacionFal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_ACTUALIZACION_FAL")
    @Temporal(TemporalType.DATE)
    private Date fechaActualizacionFal;
    @Size(max = 100)
    @Column(name = "OFICINA_RC_NSCRPCN_FAL")
    private String oficinaRcNscrpcnFal;
    @Size(max = 2)
    @Column(name = "PROVINCIA_ID_NSCRPCN_FAL")
    private String provinciaIdNscrpcnFal;
    @Size(max = 100)
    @Column(name = "PROVINCIA_DS_NSCRPCN_FAL")
    private String provinciaDsNscrpcnFal;
    @Size(max = 4)
    @Column(name = "CANTON_ID_NSCRPCN_FALS")
    private String cantonIdNscrpcnFals;
    @Size(max = 100)
    @Column(name = "CANTON_DS_NSCRPCN_FALS")
    private String cantonDsNscrpcnFals;
    @Size(max = 6)
    @Column(name = "PARROQUIA_ID_NSCRPCN_FAL")
    private String parroquiaIdNscrpcnFal;
    @Size(max = 100)
    @Column(name = "PARROQUIA_DS_NSCRPCN_FAL")
    private String parroquiaDsNscrpcnFal;
    @Column(name = "FECHA_NSCRPCN_FAL")
    @Temporal(TemporalType.DATE)
    private Date fechaNscrpcnFal;
    @Size(max = 15)
    @Column(name = "ACTA_NSCRPCN_FAL")
    private String actaNscrpcnFal;
    @Size(max = 20)
    @Column(name = "SEXO_FAL")
    private String sexoFal;
    @Size(max = 12)
    @Column(name = "CEDULA_FAL")
    private String cedulaFal;
    @Size(max = 25)
    @Column(name = "IDENTIFICACION_PASAPORTE_FAL")
    private String identificacionPasaporteFal;
    @Size(max = 150)
    @Column(name = "NOMBRE_FAL")
    private String nombreFal;
    @Size(max = 150)
    @Column(name = "NOMBRE_PADRE_FAL")
    private String nombrePadreFal;
    @Size(max = 150)
    @Column(name = "NOMBRE_MADRE_FAL")
    private String nombreMadreFal;
    @Column(name = "FECHA_NACIMIENTO_FAL")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoFal;
    @Column(name = "FECHA_FALLECIMIENTO_FAL")
    @Temporal(TemporalType.DATE)
    private Date fechaFallecimientoFal;
    @Column(name = "EDAD_FAL")
    private Short edadFal;
    @Size(max = 2)
    @Column(name = "PROVINCIA_ID_FLLCMNT_FAL")
    private String provinciaIdFllcmntFal;
    @Size(max = 100)
    @Column(name = "PROVINCIA_DS_FLLCMNT_FAL")
    private String provinciaDsFllcmntFal;
    @Size(max = 4)
    @Column(name = "CANTON_ID_FLLCMNT_FAL")
    private String cantonIdFllcmntFal;
    @Size(max = 100)
    @Column(name = "CANTON_DS_FLLCMNT_FAL")
    private String cantonDsFllcmntFal;
    @Size(max = 100)
    @Column(name = "CIUDAD_FLLCMNT_FAL")
    private String ciudadFllcmntFal;
    @Size(max = 6)
    @Column(name = "PARROQUIA_ID_FLLCMNT_FAL")
    private String parroquiaIdFllcmntFal;
    @Size(max = 100)
    @Column(name = "PARROQUIA_DS_FLLCMNT_FAL")
    private String parroquiaDsFllcmntFal;
    @Size(max = 100)
    @Column(name = "LOCALIDAD_FLLCMNT_FAL")
    private String localidadFllcmntFal;
    @Size(max = 2)
    @Column(name = "PROVINCIA_ID_RSDNC_FAL")
    private String provinciaIdRsdncFal;
    @Size(max = 100)
    @Column(name = "PROVINCIA_DS_RSDNC_FAL")
    private String provinciaDsRsdncFal;
    @Size(max = 4)
    @Column(name = "CANTON_ID_RSDNC_FAL")
    private String cantonIdRsdncFal;
    @Size(max = 100)
    @Column(name = "CANTON_DS_RSDNC_FAL")
    private String cantonDsRsdncFal;
    @Size(max = 100)
    @Column(name = "CIUDAD_RSDNC_FAL")
    private String ciudadRsdncFal;
    @Size(max = 6)
    @Column(name = "PARROQUIA_ID_RSDNC_FAL")
    private String parroquiaIdRsdncFal;
    @Size(max = 100)
    @Column(name = "PARROQUIA_DS_RSDNC_FAL")
    private String parroquiaDsRsdncFal;
    @Size(max = 100)
    @Column(name = "LOCALIDAD_RSDNC_FAL")
    private String localidadRsdncFal;
    @Size(max = 250)
    @Column(name = "CAUSA_MUERTA_A_FAL")
    private String causaMuertaAFal;
    @Size(max = 100)
    @Column(name = "TIEMPO_MUERTE_A_FAL")
    private String tiempoMuerteAFal;
    @Size(max = 250)
    @Column(name = "CAUSA_MUERTA_B_FAL")
    private String causaMuertaBFal;
    @Size(max = 100)
    @Column(name = "TIEMPO_MUERTE_B_FAL")
    private String tiempoMuerteBFal;
    @Size(max = 250)
    @Column(name = "CAUSA_MUERTE_C_FAL")
    private String causaMuerteCFal;
    @Size(max = 100)
    @Column(name = "TIEMPO_MUERTE_C_FAL")
    private String tiempoMuerteCFal;
    @Size(max = 250)
    @Column(name = "CAUSA_MUERTE_D_FAL")
    private String causaMuerteDFal;
    @Size(max = 100)
    @Column(name = "TIEMPO_MUERTE_D_FAL")
    private String tiempoMuerteDFal;
    @Size(max = 100)
    @Column(name = "OTROS_MOTIVOS_PATOLOGICOS_FAL")
    private String otrosMotivosPatologicosFal;
    @Size(max = 100)
    @Column(name = "TIEMPO_MUERTE_OTRS_MOTIV_FAL")
    private String tiempoMuerteOtrsMotivFal;
    @Size(max = 10)
    @Column(name = "FK_USU_SAUREG")
    private String fkUsuSaureg;
    @Size(max = 15)
    @Column(name = "FK_AGENCIA_SAUREG")
    private String fkAgenciaSaureg;
    @Size(max = 10)
    @Column(name = "FK_USU_FIRMA_SAUREG")
    private String fkUsuFirmaSaureg;
    @Size(max = 15)
    @Column(name = "FK_AGENCIA_FIRMA_SAUREG")
    private String fkAgenciaFirmaSaureg;
    @Size(max = 250)
    @Column(name = "DESCRIPCION_MUERTE_VIOLENTA")
    private String descripcionMuerteViolenta;
    @Size(max = 100)
    @Column(name = "CONDICION_CEDULADO_FAL")
    private String condicionCeduladoFal;
    @Size(max = 2)
    @Column(name = "CODIGO_PAIS_FAL")
    private String codigoPaisFal;
    @Size(max = 150)
    @Column(name = "CONYUGE_FAL")
    private String conyugeFal;
    @Size(max = 1)
    @Column(name = "STATUS_FAL")
    private String statusFal;
    @Lob
    @Column(name = "FOTOGRAFIA_FAL")
    private byte[] fotografiaFal;
    @Size(max = 250)
    @Column(name = "OBSERVACION_PROFESIONAL_FAL")
    private String observacionProfesionalFal;
    @Lob
    @Column(name = "PDF_CON_FIRMA")
    private byte[] pdfConFirma;
    @Lob
    @Column(name = "PDF_SIN_FIRMA")
    private byte[] pdfSinFirma;
    @Column(name = "FECHA_FIRMA_FAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFirmaFal;
    @Lob
    @Column(name = "PDF_CERTIFICADO")
    private byte[] pdfCertificado;
    @JoinColumn(name = "FK_ID_TIP_REG_FAL", referencedColumnName = "ID_TIP_REG_FAL")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoRegistroFallecimiento fkIdTipRegFal;
    @JoinColumn(name = "FK_ID_TIP_MUE_VIO", referencedColumnName = "ID_TIP_MUE_VIO")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoMuerteViolenta fkIdTipMueVio;
    @JoinColumn(name = "FK_ID_TIP_EDA", referencedColumnName = "ID_TIP_EDA")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoEdad fkIdTipEda;
    @JoinColumn(name = "FK_ID_RES_MUE_VIO", referencedColumnName = "ID_RES_MUE_VIO")
    @ManyToOne(fetch = FetchType.LAZY)
    private RespuestaMuerteViolenta fkIdResMueVio;
    @JoinColumn(name = "FK_ID_RES_MUE_ENEST", referencedColumnName = "ID_RES_MUE_ENEST")
    @ManyToOne(fetch = FetchType.LAZY)
    private RespuestaMuerteenestablecim fkIdResMueEnest;
    @JoinColumn(name = "FK_ID_RES_CER_MED", referencedColumnName = "ID_RES_CER_MED")
    @ManyToOne(fetch = FetchType.LAZY)
    private RespuestaCetirficacionMedica fkIdResCerMed;
    @JoinColumn(name = "FK_ID_ALFABETISMO", referencedColumnName = "ID_ALFB")
    @ManyToOne(fetch = FetchType.LAZY)
    private AlfabetismoRenavi fkIdAlfabetismo;
    @JoinColumn(name = "FK_ID_CER_POR", referencedColumnName = "ID_CER_POR")
    @ManyToOne(fetch = FetchType.LAZY)
    private CertificadoPor fkIdCerPor;
    @JoinColumn(name = "FK_ID_ESTADO_CIVIL_FAL", referencedColumnName = "ID_EST_CIV")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstadoCivilRenavi fkIdEstadoCivilFal;
    @JoinColumn(name = "FK_ID_ESTADO_FIRMA", referencedColumnName = "ID_EST_FIR")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstadoFirmaRenavi fkIdEstadoFirma;
    @JoinColumn(name = "FK_ID_EST_REG_FAL", referencedColumnName = "ID_EST_REG_FAL")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EstadoRegistroFallecido fkIdEstRegFal;
    @JoinColumn(name = "FK_ID_ETNIA", referencedColumnName = "ID_IDE_ETN")
    @ManyToOne(fetch = FetchType.LAZY)
    private IdentificacionEtnicaRenavi fkIdEtnia;
    @JoinColumn(name = "FK_ID_LUG_MUE_VIO", referencedColumnName = "ID_LUG_MUE_VIO")
    @ManyToOne(fetch = FetchType.LAZY)
    private LugarMuerteViolenta fkIdLugMueVio;
    @JoinColumn(name = "FK_ID_MOR_MAT", referencedColumnName = "ID_MOR_MAT")
    @ManyToOne(fetch = FetchType.LAZY)
    private MortalidadMaterna fkIdMorMat;
    @JoinColumn(name = "FK_ID_NACIONALIDAD", referencedColumnName = "ID_NAC")
    @ManyToOne(fetch = FetchType.LAZY)
    private NacionalidadRenavi fkIdNacionalidad;
    @JoinColumn(name = "FK_ID_NIVEL_INSTRUCCION", referencedColumnName = "ID_NIV_INS")
    @ManyToOne(fetch = FetchType.LAZY)
    private NivelInstruccionRenavi fkIdNivelInstruccion;
    @JoinColumn(name = "FK_ID_PAIS", referencedColumnName = "ID_PAIS")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaisRenavi fkIdPais;
    @JoinColumn(name = "FK_ID_RES_AUT", referencedColumnName = "ID_RES_AUT")
    @ManyToOne(fetch = FetchType.LAZY)
    private RespuestaAutopsia fkIdResAut;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fallecido", fetch = FetchType.LAZY)
    private List<SolicitanteFallecimiento> solicitanteFallecimientoList;

    public Fallecido() {
    }

    public Fallecido(Long idFal) {
        this.idFal = idFal;
    }

    public Fallecido(Long idFal, Date fechaCreacionFal, Date fechaActualizacionFal) {
        this.idFal = idFal;
        this.fechaCreacionFal = fechaCreacionFal;
        this.fechaActualizacionFal = fechaActualizacionFal;
    }

    public Long getIdFal() {
        return idFal;
    }

    public void setIdFal(Long idFal) {
        this.idFal = idFal;
    }

    public Date getFechaCreacionFal() {
        return fechaCreacionFal;
    }

    public void setFechaCreacionFal(Date fechaCreacionFal) {
        this.fechaCreacionFal = fechaCreacionFal;
    }

    public Date getFechaActualizacionFal() {
        return fechaActualizacionFal;
    }

    public void setFechaActualizacionFal(Date fechaActualizacionFal) {
        this.fechaActualizacionFal = fechaActualizacionFal;
    }

    public String getOficinaRcNscrpcnFal() {
        return oficinaRcNscrpcnFal;
    }

    public void setOficinaRcNscrpcnFal(String oficinaRcNscrpcnFal) {
        this.oficinaRcNscrpcnFal = oficinaRcNscrpcnFal;
    }

    public String getProvinciaIdNscrpcnFal() {
        return provinciaIdNscrpcnFal;
    }

    public void setProvinciaIdNscrpcnFal(String provinciaIdNscrpcnFal) {
        this.provinciaIdNscrpcnFal = provinciaIdNscrpcnFal;
    }

    public String getProvinciaDsNscrpcnFal() {
        return provinciaDsNscrpcnFal;
    }

    public void setProvinciaDsNscrpcnFal(String provinciaDsNscrpcnFal) {
        this.provinciaDsNscrpcnFal = provinciaDsNscrpcnFal;
    }

    public String getCantonIdNscrpcnFals() {
        return cantonIdNscrpcnFals;
    }

    public void setCantonIdNscrpcnFals(String cantonIdNscrpcnFals) {
        this.cantonIdNscrpcnFals = cantonIdNscrpcnFals;
    }

    public String getCantonDsNscrpcnFals() {
        return cantonDsNscrpcnFals;
    }

    public void setCantonDsNscrpcnFals(String cantonDsNscrpcnFals) {
        this.cantonDsNscrpcnFals = cantonDsNscrpcnFals;
    }

    public String getParroquiaIdNscrpcnFal() {
        return parroquiaIdNscrpcnFal;
    }

    public void setParroquiaIdNscrpcnFal(String parroquiaIdNscrpcnFal) {
        this.parroquiaIdNscrpcnFal = parroquiaIdNscrpcnFal;
    }

    public String getParroquiaDsNscrpcnFal() {
        return parroquiaDsNscrpcnFal;
    }

    public void setParroquiaDsNscrpcnFal(String parroquiaDsNscrpcnFal) {
        this.parroquiaDsNscrpcnFal = parroquiaDsNscrpcnFal;
    }

    public Date getFechaNscrpcnFal() {
        return fechaNscrpcnFal;
    }

    public void setFechaNscrpcnFal(Date fechaNscrpcnFal) {
        this.fechaNscrpcnFal = fechaNscrpcnFal;
    }

    public String getActaNscrpcnFal() {
        return actaNscrpcnFal;
    }

    public void setActaNscrpcnFal(String actaNscrpcnFal) {
        this.actaNscrpcnFal = actaNscrpcnFal;
    }

    public String getSexoFal() {
        return sexoFal;
    }

    public void setSexoFal(String sexoFal) {
        this.sexoFal = sexoFal;
    }

    public String getCedulaFal() {
        return cedulaFal;
    }

    public void setCedulaFal(String cedulaFal) {
        this.cedulaFal = cedulaFal;
    }

    public String getIdentificacionPasaporteFal() {
        return identificacionPasaporteFal;
    }

    public void setIdentificacionPasaporteFal(String identificacionPasaporteFal) {
        this.identificacionPasaporteFal = identificacionPasaporteFal;
    }

    public String getNombreFal() {
        return nombreFal;
    }

    public void setNombreFal(String nombreFal) {
        this.nombreFal = nombreFal;
    }

    public String getNombrePadreFal() {
        return nombrePadreFal;
    }

    public void setNombrePadreFal(String nombrePadreFal) {
        this.nombrePadreFal = nombrePadreFal;
    }

    public String getNombreMadreFal() {
        return nombreMadreFal;
    }

    public void setNombreMadreFal(String nombreMadreFal) {
        this.nombreMadreFal = nombreMadreFal;
    }

    public Date getFechaNacimientoFal() {
        return fechaNacimientoFal;
    }

    public void setFechaNacimientoFal(Date fechaNacimientoFal) {
        this.fechaNacimientoFal = fechaNacimientoFal;
    }

    public Date getFechaFallecimientoFal() {
        return fechaFallecimientoFal;
    }

    public void setFechaFallecimientoFal(Date fechaFallecimientoFal) {
        this.fechaFallecimientoFal = fechaFallecimientoFal;
    }

    public Short getEdadFal() {
        return edadFal;
    }

    public void setEdadFal(Short edadFal) {
        this.edadFal = edadFal;
    }

    public String getProvinciaIdFllcmntFal() {
        return provinciaIdFllcmntFal;
    }

    public void setProvinciaIdFllcmntFal(String provinciaIdFllcmntFal) {
        this.provinciaIdFllcmntFal = provinciaIdFllcmntFal;
    }

    public String getProvinciaDsFllcmntFal() {
        return provinciaDsFllcmntFal;
    }

    public void setProvinciaDsFllcmntFal(String provinciaDsFllcmntFal) {
        this.provinciaDsFllcmntFal = provinciaDsFllcmntFal;
    }

    public String getCantonIdFllcmntFal() {
        return cantonIdFllcmntFal;
    }

    public void setCantonIdFllcmntFal(String cantonIdFllcmntFal) {
        this.cantonIdFllcmntFal = cantonIdFllcmntFal;
    }

    public String getCantonDsFllcmntFal() {
        return cantonDsFllcmntFal;
    }

    public void setCantonDsFllcmntFal(String cantonDsFllcmntFal) {
        this.cantonDsFllcmntFal = cantonDsFllcmntFal;
    }

    public String getCiudadFllcmntFal() {
        return ciudadFllcmntFal;
    }

    public void setCiudadFllcmntFal(String ciudadFllcmntFal) {
        this.ciudadFllcmntFal = ciudadFllcmntFal;
    }

    public String getParroquiaIdFllcmntFal() {
        return parroquiaIdFllcmntFal;
    }

    public void setParroquiaIdFllcmntFal(String parroquiaIdFllcmntFal) {
        this.parroquiaIdFllcmntFal = parroquiaIdFllcmntFal;
    }

    public String getParroquiaDsFllcmntFal() {
        return parroquiaDsFllcmntFal;
    }

    public void setParroquiaDsFllcmntFal(String parroquiaDsFllcmntFal) {
        this.parroquiaDsFllcmntFal = parroquiaDsFllcmntFal;
    }

    public String getLocalidadFllcmntFal() {
        return localidadFllcmntFal;
    }

    public void setLocalidadFllcmntFal(String localidadFllcmntFal) {
        this.localidadFllcmntFal = localidadFllcmntFal;
    }

    public String getProvinciaIdRsdncFal() {
        return provinciaIdRsdncFal;
    }

    public void setProvinciaIdRsdncFal(String provinciaIdRsdncFal) {
        this.provinciaIdRsdncFal = provinciaIdRsdncFal;
    }

    public String getProvinciaDsRsdncFal() {
        return provinciaDsRsdncFal;
    }

    public void setProvinciaDsRsdncFal(String provinciaDsRsdncFal) {
        this.provinciaDsRsdncFal = provinciaDsRsdncFal;
    }

    public String getCantonIdRsdncFal() {
        return cantonIdRsdncFal;
    }

    public void setCantonIdRsdncFal(String cantonIdRsdncFal) {
        this.cantonIdRsdncFal = cantonIdRsdncFal;
    }

    public String getCantonDsRsdncFal() {
        return cantonDsRsdncFal;
    }

    public void setCantonDsRsdncFal(String cantonDsRsdncFal) {
        this.cantonDsRsdncFal = cantonDsRsdncFal;
    }

    public String getCiudadRsdncFal() {
        return ciudadRsdncFal;
    }

    public void setCiudadRsdncFal(String ciudadRsdncFal) {
        this.ciudadRsdncFal = ciudadRsdncFal;
    }

    public String getParroquiaIdRsdncFal() {
        return parroquiaIdRsdncFal;
    }

    public void setParroquiaIdRsdncFal(String parroquiaIdRsdncFal) {
        this.parroquiaIdRsdncFal = parroquiaIdRsdncFal;
    }

    public String getParroquiaDsRsdncFal() {
        return parroquiaDsRsdncFal;
    }

    public void setParroquiaDsRsdncFal(String parroquiaDsRsdncFal) {
        this.parroquiaDsRsdncFal = parroquiaDsRsdncFal;
    }

    public String getLocalidadRsdncFal() {
        return localidadRsdncFal;
    }

    public void setLocalidadRsdncFal(String localidadRsdncFal) {
        this.localidadRsdncFal = localidadRsdncFal;
    }

    public String getCausaMuertaAFal() {
        return causaMuertaAFal;
    }

    public void setCausaMuertaAFal(String causaMuertaAFal) {
        this.causaMuertaAFal = causaMuertaAFal;
    }

    public String getTiempoMuerteAFal() {
        return tiempoMuerteAFal;
    }

    public void setTiempoMuerteAFal(String tiempoMuerteAFal) {
        this.tiempoMuerteAFal = tiempoMuerteAFal;
    }

    public String getCausaMuertaBFal() {
        return causaMuertaBFal;
    }

    public void setCausaMuertaBFal(String causaMuertaBFal) {
        this.causaMuertaBFal = causaMuertaBFal;
    }

    public String getTiempoMuerteBFal() {
        return tiempoMuerteBFal;
    }

    public void setTiempoMuerteBFal(String tiempoMuerteBFal) {
        this.tiempoMuerteBFal = tiempoMuerteBFal;
    }

    public String getCausaMuerteCFal() {
        return causaMuerteCFal;
    }

    public void setCausaMuerteCFal(String causaMuerteCFal) {
        this.causaMuerteCFal = causaMuerteCFal;
    }

    public String getTiempoMuerteCFal() {
        return tiempoMuerteCFal;
    }

    public void setTiempoMuerteCFal(String tiempoMuerteCFal) {
        this.tiempoMuerteCFal = tiempoMuerteCFal;
    }

    public String getCausaMuerteDFal() {
        return causaMuerteDFal;
    }

    public void setCausaMuerteDFal(String causaMuerteDFal) {
        this.causaMuerteDFal = causaMuerteDFal;
    }

    public String getTiempoMuerteDFal() {
        return tiempoMuerteDFal;
    }

    public void setTiempoMuerteDFal(String tiempoMuerteDFal) {
        this.tiempoMuerteDFal = tiempoMuerteDFal;
    }

    public String getOtrosMotivosPatologicosFal() {
        return otrosMotivosPatologicosFal;
    }

    public void setOtrosMotivosPatologicosFal(String otrosMotivosPatologicosFal) {
        this.otrosMotivosPatologicosFal = otrosMotivosPatologicosFal;
    }

    public String getTiempoMuerteOtrsMotivFal() {
        return tiempoMuerteOtrsMotivFal;
    }

    public void setTiempoMuerteOtrsMotivFal(String tiempoMuerteOtrsMotivFal) {
        this.tiempoMuerteOtrsMotivFal = tiempoMuerteOtrsMotivFal;
    }

    public String getFkUsuSaureg() {
        return fkUsuSaureg;
    }

    public void setFkUsuSaureg(String fkUsuSaureg) {
        this.fkUsuSaureg = fkUsuSaureg;
    }

    public String getFkAgenciaSaureg() {
        return fkAgenciaSaureg;
    }

    public void setFkAgenciaSaureg(String fkAgenciaSaureg) {
        this.fkAgenciaSaureg = fkAgenciaSaureg;
    }

    public String getFkUsuFirmaSaureg() {
        return fkUsuFirmaSaureg;
    }

    public void setFkUsuFirmaSaureg(String fkUsuFirmaSaureg) {
        this.fkUsuFirmaSaureg = fkUsuFirmaSaureg;
    }

    public String getFkAgenciaFirmaSaureg() {
        return fkAgenciaFirmaSaureg;
    }

    public void setFkAgenciaFirmaSaureg(String fkAgenciaFirmaSaureg) {
        this.fkAgenciaFirmaSaureg = fkAgenciaFirmaSaureg;
    }

    public String getDescripcionMuerteViolenta() {
        return descripcionMuerteViolenta;
    }

    public void setDescripcionMuerteViolenta(String descripcionMuerteViolenta) {
        this.descripcionMuerteViolenta = descripcionMuerteViolenta;
    }

    public String getCondicionCeduladoFal() {
        return condicionCeduladoFal;
    }

    public void setCondicionCeduladoFal(String condicionCeduladoFal) {
        this.condicionCeduladoFal = condicionCeduladoFal;
    }

    public String getCodigoPaisFal() {
        return codigoPaisFal;
    }

    public void setCodigoPaisFal(String codigoPaisFal) {
        this.codigoPaisFal = codigoPaisFal;
    }

    public String getConyugeFal() {
        return conyugeFal;
    }

    public void setConyugeFal(String conyugeFal) {
        this.conyugeFal = conyugeFal;
    }

    public String getStatusFal() {
        return statusFal;
    }

    public void setStatusFal(String statusFal) {
        this.statusFal = statusFal;
    }

    public byte[] getFotografiaFal() {
        return fotografiaFal;
    }

    public void setFotografiaFal(byte[] fotografiaFal) {
        this.fotografiaFal = fotografiaFal;
    }

    public String getObservacionProfesionalFal() {
        return observacionProfesionalFal;
    }

    public void setObservacionProfesionalFal(String observacionProfesionalFal) {
        this.observacionProfesionalFal = observacionProfesionalFal;
    }

    public byte[] getPdfConFirma() {
        return pdfConFirma;
    }

    public void setPdfConFirma(byte[] pdfConFirma) {
        this.pdfConFirma = pdfConFirma;
    }

    public byte[] getPdfSinFirma() {
        return pdfSinFirma;
    }

    public void setPdfSinFirma(byte[] pdfSinFirma) {
        this.pdfSinFirma = pdfSinFirma;
    }

    public Date getFechaFirmaFal() {
        return fechaFirmaFal;
    }

    public void setFechaFirmaFal(Date fechaFirmaFal) {
        this.fechaFirmaFal = fechaFirmaFal;
    }

    public byte[] getPdfCertificado() {
        return pdfCertificado;
    }

    public void setPdfCertificado(byte[] pdfCertificado) {
        this.pdfCertificado = pdfCertificado;
    }

    public TipoRegistroFallecimiento getFkIdTipRegFal() {
        return fkIdTipRegFal;
    }

    public void setFkIdTipRegFal(TipoRegistroFallecimiento fkIdTipRegFal) {
        this.fkIdTipRegFal = fkIdTipRegFal;
    }

    public TipoMuerteViolenta getFkIdTipMueVio() {
        return fkIdTipMueVio;
    }

    public void setFkIdTipMueVio(TipoMuerteViolenta fkIdTipMueVio) {
        this.fkIdTipMueVio = fkIdTipMueVio;
    }

    public TipoEdad getFkIdTipEda() {
        return fkIdTipEda;
    }

    public void setFkIdTipEda(TipoEdad fkIdTipEda) {
        this.fkIdTipEda = fkIdTipEda;
    }

    public RespuestaMuerteViolenta getFkIdResMueVio() {
        return fkIdResMueVio;
    }

    public void setFkIdResMueVio(RespuestaMuerteViolenta fkIdResMueVio) {
        this.fkIdResMueVio = fkIdResMueVio;
    }

    public RespuestaMuerteenestablecim getFkIdResMueEnest() {
        return fkIdResMueEnest;
    }

    public void setFkIdResMueEnest(RespuestaMuerteenestablecim fkIdResMueEnest) {
        this.fkIdResMueEnest = fkIdResMueEnest;
    }

    public RespuestaCetirficacionMedica getFkIdResCerMed() {
        return fkIdResCerMed;
    }

    public void setFkIdResCerMed(RespuestaCetirficacionMedica fkIdResCerMed) {
        this.fkIdResCerMed = fkIdResCerMed;
    }

    public AlfabetismoRenavi getFkIdAlfabetismo() {
        return fkIdAlfabetismo;
    }

    public void setFkIdAlfabetismo(AlfabetismoRenavi fkIdAlfabetismo) {
        this.fkIdAlfabetismo = fkIdAlfabetismo;
    }

    public CertificadoPor getFkIdCerPor() {
        return fkIdCerPor;
    }

    public void setFkIdCerPor(CertificadoPor fkIdCerPor) {
        this.fkIdCerPor = fkIdCerPor;
    }

    public EstadoCivilRenavi getFkIdEstadoCivilFal() {
        return fkIdEstadoCivilFal;
    }

    public void setFkIdEstadoCivilFal(EstadoCivilRenavi fkIdEstadoCivilFal) {
        this.fkIdEstadoCivilFal = fkIdEstadoCivilFal;
    }

    public EstadoFirmaRenavi getFkIdEstadoFirma() {
        return fkIdEstadoFirma;
    }

    public void setFkIdEstadoFirma(EstadoFirmaRenavi fkIdEstadoFirma) {
        this.fkIdEstadoFirma = fkIdEstadoFirma;
    }

    public EstadoRegistroFallecido getFkIdEstRegFal() {
        return fkIdEstRegFal;
    }

    public void setFkIdEstRegFal(EstadoRegistroFallecido fkIdEstRegFal) {
        this.fkIdEstRegFal = fkIdEstRegFal;
    }

    public IdentificacionEtnicaRenavi getFkIdEtnia() {
        return fkIdEtnia;
    }

    public void setFkIdEtnia(IdentificacionEtnicaRenavi fkIdEtnia) {
        this.fkIdEtnia = fkIdEtnia;
    }

    public LugarMuerteViolenta getFkIdLugMueVio() {
        return fkIdLugMueVio;
    }

    public void setFkIdLugMueVio(LugarMuerteViolenta fkIdLugMueVio) {
        this.fkIdLugMueVio = fkIdLugMueVio;
    }

    public MortalidadMaterna getFkIdMorMat() {
        return fkIdMorMat;
    }

    public void setFkIdMorMat(MortalidadMaterna fkIdMorMat) {
        this.fkIdMorMat = fkIdMorMat;
    }

    public NacionalidadRenavi getFkIdNacionalidad() {
        return fkIdNacionalidad;
    }

    public void setFkIdNacionalidad(NacionalidadRenavi fkIdNacionalidad) {
        this.fkIdNacionalidad = fkIdNacionalidad;
    }

    public NivelInstruccionRenavi getFkIdNivelInstruccion() {
        return fkIdNivelInstruccion;
    }

    public void setFkIdNivelInstruccion(NivelInstruccionRenavi fkIdNivelInstruccion) {
        this.fkIdNivelInstruccion = fkIdNivelInstruccion;
    }

    public PaisRenavi getFkIdPais() {
        return fkIdPais;
    }

    public void setFkIdPais(PaisRenavi fkIdPais) {
        this.fkIdPais = fkIdPais;
    }

    public RespuestaAutopsia getFkIdResAut() {
        return fkIdResAut;
    }

    public void setFkIdResAut(RespuestaAutopsia fkIdResAut) {
        this.fkIdResAut = fkIdResAut;
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
        hash += (idFal != null ? idFal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fallecido)) {
            return false;
        }
        Fallecido other = (Fallecido) object;
        if ((this.idFal == null && other.idFal != null) || (this.idFal != null && !this.idFal.equals(other.idFal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.Fallecido[ idFal=" + idFal + " ]";
    }
    
}
