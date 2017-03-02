/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "AUDITORIA_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditoriaRenavi.findAll", query = "SELECT a FROM AuditoriaRenavi a"),
    @NamedQuery(name = "AuditoriaRenavi.findByIdAud", query = "SELECT a FROM AuditoriaRenavi a WHERE a.idAud = :idAud"),
    @NamedQuery(name = "AuditoriaRenavi.findByIdNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.idNacViv = :idNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByProviRcdscNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.proviRcdscNacViv = :proviRcdscNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCantnRcdscNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.cantnRcdscNacViv = :cantnRcdscNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByPaarqRcdscNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.paarqRcdscNacViv = :paarqRcdscNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByOficiRcNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.oficiRcNacViv = :oficiRcNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdTipIns", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdTipIns = :fkIdTipIns"),
    @NamedQuery(name = "AuditoriaRenavi.findByFechaInscrNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fechaInscrNacViv = :fechaInscrNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByActaInscrNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.actaInscrNacViv = :actaInscrNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByNombrNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.nombrNacViv = :nombrNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByApellNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.apellNacViv = :apellNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCedulNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.cedulNacViv = :cedulNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdSexoNv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdSexoNv = :fkIdSexoNv"),
    @NamedQuery(name = "AuditoriaRenavi.findByTallaNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.tallaNacViv = :tallaNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByPesoNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.pesoNacViv = :pesoNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findBySemanGstcnNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.semanGstcnNacViv = :semanGstcnNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdTipPar", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdTipPar = :fkIdTipPar"),
    @NamedQuery(name = "AuditoriaRenavi.findByFechaNacimNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fechaNacimNacViv = :fechaNacimNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdProEmb", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdProEmb = :fkIdProEmb"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdLugNac", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdLugNac = :fkIdLugNac"),
    @NamedQuery(name = "AuditoriaRenavi.findByLugNacEspecifique", query = "SELECT a FROM AuditoriaRenavi a WHERE a.lugNacEspecifique = :lugNacEspecifique"),
    @NamedQuery(name = "AuditoriaRenavi.findByNombrStblcNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.nombrStblcNacViv = :nombrStblcNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByProviEstbdsNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.proviEstbdsNacViv = :proviEstbdsNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCantnEstbdsNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.cantnEstbdsNacViv = :cantnEstbdsNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByParrqEstbdsNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.parrqEstbdsNacViv = :parrqEstbdsNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCddlocStblcNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.cddlocStblcNacViv = :cddlocStblcNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByDirecStblcNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.direcStblcNacViv = :direcStblcNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByTelefStblcNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.telefStblcNacViv = :telefStblcNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdTipAsi", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdTipAsi = :fkIdTipAsi"),
    @NamedQuery(name = "AuditoriaRenavi.findByTipAsiEspecifique", query = "SELECT a FROM AuditoriaRenavi a WHERE a.tipAsiEspecifique = :tipAsiEspecifique"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdEstFir", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdEstFir = :fkIdEstFir"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdEstReg", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdEstReg = :fkIdEstReg"),
    @NamedQuery(name = "AuditoriaRenavi.findByObsrvAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.obsrvAtprtNacViv = :obsrvAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByIdMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.idMad = :idMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByNombrMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.nombrMad = :nombrMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdPaisMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdPaisMad = :fkIdPaisMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdNacMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdNacMad = :fkIdNacMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByCedulMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.cedulMad = :cedulMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByPasapMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.pasapMad = :pasapMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFechaNacimMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fechaNacimMad = :fechaNacimMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByEdadMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.edadMad = :edadMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByCondicCedMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.condicCedMad = :condicCedMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByConyugeMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.conyugeMad = :conyugeMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFecFallecMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fecFallecMad = :fecFallecMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByNomPadreMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.nomPadreMad = :nomPadreMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByNomMadreMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.nomMadreMad = :nomMadreMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByCntrlPrntlNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.cntrlPrntlNacViv = :cntrlPrntlNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByNumerPartoNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.numerPartoNacViv = :numerPartoNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByHijosVivsaMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.hijosVivsaMad = :hijosVivsaMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByHijosNvmrtMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.hijosNvmrtMad = :hijosNvmrtMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByHijosNmrtsMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.hijosNmrtsMad = :hijosNmrtsMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdIdeEtn", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdIdeEtn = :fkIdIdeEtn"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdEstCivMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdEstCivMad = :fkIdEstCivMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdSabeLeerMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdSabeLeerMad = :fkIdSabeLeerMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdNivelInstr", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdNivelInstr = :fkIdNivelInstr"),
    @NamedQuery(name = "AuditoriaRenavi.findByResidnProvdsMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.residnProvdsMad = :residnProvdsMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByResidnCantdsMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.residnCantdsMad = :residnCantdsMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByResidnParrdsMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.residnParrdsMad = :residnParrdsMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByResidnLocalMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.residnLocalMad = :residnLocalMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByResidnDirecMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.residnDirecMad = :residnDirecMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByNombrAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.nombrAtprtNacViv = :nombrAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByNumregprfsnlAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.numregprfsnlAtprtNacViv = :numregprfsnlAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByTelefcelAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.telefcelAtprtNacViv = :telefcelAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByTelefconvenofiAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.telefconvenofiAtprtNacViv = :telefconvenofiAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByTelfconvenextAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.telfconvenextAtprtNacViv = :telfconvenextAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByEmailAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.emailAtprtNacViv = :emailAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByEstadoAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.estadoAtprtNacViv = :estadoAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByUsernameAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.usernameAtprtNacViv = :usernameAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdTipUsuAtprtNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdTipUsuAtprtNacViv = :fkIdTipUsuAtprtNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecRcPcpNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecRcPcpNacViv = :campoInecRcPcpNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecRcOfiNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecRcOfiNacViv = :campoInecRcOfiNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecFecCriNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecFecCriNacViv = :campoInecFecCriNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecCodigStblcNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecCodigStblcNacViv = :campoInecCodigStblcNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecCodPaisMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecCodPaisMad = :campoInecCodPaisMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdTipoAreaNv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdTipoAreaNv = :fkIdTipoAreaNv"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecAreaNacViv", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecAreaNacViv = :campoInecAreaNacViv"),
    @NamedQuery(name = "AuditoriaRenavi.findByFkIdTipoAreaMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fkIdTipoAreaMad = :fkIdTipoAreaMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecAreaMad", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecAreaMad = :campoInecAreaMad"),
    @NamedQuery(name = "AuditoriaRenavi.findByCampoInecCodCriCod", query = "SELECT a FROM AuditoriaRenavi a WHERE a.campoInecCodCriCod = :campoInecCodCriCod"),
    @NamedQuery(name = "AuditoriaRenavi.findByFechaAudit", query = "SELECT a FROM AuditoriaRenavi a WHERE a.fechaAudit = :fechaAudit"),
    @NamedQuery(name = "AuditoriaRenavi.findByUsuarioCambio", query = "SELECT a FROM AuditoriaRenavi a WHERE a.usuarioCambio = :usuarioCambio")})
public class AuditoriaRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_AUD")
    private BigDecimal idAud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_NAC_VIV")
    private BigInteger idNacViv;
    @Size(max = 30)
    @Column(name = "PROVI_RCDSC_NAC_VIV")
    private String proviRcdscNacViv;
    @Size(max = 30)
    @Column(name = "CANTN_RCDSC_NAC_VIV")
    private String cantnRcdscNacViv;
    @Size(max = 30)
    @Column(name = "PAARQ_RCDSC_NAC_VIV")
    private String paarqRcdscNacViv;
    @Size(max = 100)
    @Column(name = "OFICI_RC_NAC_VIV")
    private String oficiRcNacViv;
    @Size(max = 30)
    @Column(name = "FK_ID_TIP_INS")
    private String fkIdTipIns;
    @Column(name = "FECHA_INSCR_NAC_VIV")
    @Temporal(TemporalType.DATE)
    private Date fechaInscrNacViv;
    @Size(max = 5)
    @Column(name = "ACTA_INSCR_NAC_VIV")
    private String actaInscrNacViv;
    @Size(max = 50)
    @Column(name = "NOMBR_NAC_VIV")
    private String nombrNacViv;
    @Size(max = 50)
    @Column(name = "APELL_NAC_VIV")
    private String apellNacViv;
    @Size(max = 10)
    @Column(name = "CEDUL_NAC_VIV")
    private String cedulNacViv;
    @Size(max = 30)
    @Column(name = "FK_ID_SEXO_NV")
    private String fkIdSexoNv;
    @Column(name = "TALLA_NAC_VIV")
    private Short tallaNacViv;
    @Column(name = "PESO_NAC_VIV")
    private Short pesoNacViv;
    @Column(name = "SEMAN_GSTCN_NAC_VIV")
    private Short semanGstcnNacViv;
    @Size(max = 30)
    @Column(name = "FK_ID_TIP_PAR")
    private String fkIdTipPar;
    @Column(name = "FECHA_NACIM_NAC_VIV")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimNacViv;
    @Size(max = 30)
    @Column(name = "FK_ID_PRO_EMB")
    private String fkIdProEmb;
    @Size(max = 30)
    @Column(name = "FK_ID_LUG_NAC")
    private String fkIdLugNac;
    @Size(max = 70)
    @Column(name = "LUG_NAC_ESPECIFIQUE")
    private String lugNacEspecifique;
    @Size(max = 50)
    @Column(name = "NOMBR_STBLC_NAC_VIV")
    private String nombrStblcNacViv;
    @Size(max = 30)
    @Column(name = "PROVI_ESTBDS_NAC_VIV")
    private String proviEstbdsNacViv;
    @Size(max = 30)
    @Column(name = "CANTN_ESTBDS_NAC_VIV")
    private String cantnEstbdsNacViv;
    @Size(max = 30)
    @Column(name = "PARRQ_ESTBDS_NAC_VIV")
    private String parrqEstbdsNacViv;
    @Size(max = 50)
    @Column(name = "CDDLOC_STBLC_NAC_VIV")
    private String cddlocStblcNacViv;
    @Size(max = 80)
    @Column(name = "DIREC_STBLC_NAC_VIV")
    private String direcStblcNacViv;
    @Size(max = 9)
    @Column(name = "TELEF_STBLC_NAC_VIV")
    private String telefStblcNacViv;
    @Size(max = 30)
    @Column(name = "FK_ID_TIP_ASI")
    private String fkIdTipAsi;
    @Size(max = 70)
    @Column(name = "TIP_ASI_ESPECIFIQUE")
    private String tipAsiEspecifique;
    @Size(max = 50)
    @Column(name = "FK_ID_EST_FIR")
    private String fkIdEstFir;
    @Size(max = 50)
    @Column(name = "FK_ID_EST_REG")
    private String fkIdEstReg;
    @Size(max = 2000)
    @Column(name = "OBSRV_ATPRT_NAC_VIV")
    private String obsrvAtprtNacViv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MAD")
    private BigInteger idMad;
    @Size(max = 120)
    @Column(name = "NOMBR_MAD")
    private String nombrMad;
    @Size(max = 50)
    @Column(name = "FK_ID_PAIS_MAD")
    private String fkIdPaisMad;
    @Size(max = 50)
    @Column(name = "FK_ID_NAC_MAD")
    private String fkIdNacMad;
    @Size(max = 10)
    @Column(name = "CEDUL_MAD")
    private String cedulMad;
    @Size(max = 20)
    @Column(name = "PASAP_MAD")
    private String pasapMad;
    @Column(name = "FECHA_NACIM_MAD")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimMad;
    @Column(name = "EDAD_MAD")
    private Short edadMad;
    @Lob
    @Column(name = "FOTO_MAD")
    private Serializable fotoMad;
    @Size(max = 100)
    @Column(name = "CONDIC_CED_MAD")
    private String condicCedMad;
    @Size(max = 100)
    @Column(name = "CONYUGE_MAD")
    private String conyugeMad;
    @Size(max = 30)
    @Column(name = "FEC_FALLEC_MAD")
    private String fecFallecMad;
    @Size(max = 120)
    @Column(name = "NOM_PADRE_MAD")
    private String nomPadreMad;
    @Size(max = 120)
    @Column(name = "NOM_MADRE_MAD")
    private String nomMadreMad;
    @Column(name = "CNTRL_PRNTL_NAC_VIV")
    private Short cntrlPrntlNacViv;
    @Column(name = "NUMER_PARTO_NAC_VIV")
    private Short numerPartoNacViv;
    @Column(name = "HIJOS_VIVSA_MAD")
    private Short hijosVivsaMad;
    @Column(name = "HIJOS_NVMRT_MAD")
    private Short hijosNvmrtMad;
    @Column(name = "HIJOS_NMRTS_MAD")
    private Short hijosNmrtsMad;
    @Size(max = 50)
    @Column(name = "FK_ID_IDE_ETN")
    private String fkIdIdeEtn;
    @Size(max = 50)
    @Column(name = "FK_ID_EST_CIV_MAD")
    private String fkIdEstCivMad;
    @Size(max = 2)
    @Column(name = "FK_ID_SABE_LEER_MAD")
    private String fkIdSabeLeerMad;
    @Size(max = 50)
    @Column(name = "FK_ID_NIVEL_INSTR")
    private String fkIdNivelInstr;
    @Size(max = 50)
    @Column(name = "RESIDN_PROVDS_MAD")
    private String residnProvdsMad;
    @Size(max = 50)
    @Column(name = "RESIDN_CANTDS_MAD")
    private String residnCantdsMad;
    @Size(max = 50)
    @Column(name = "RESIDN_PARRDS_MAD")
    private String residnParrdsMad;
    @Size(max = 50)
    @Column(name = "RESIDN_LOCAL_MAD")
    private String residnLocalMad;
    @Size(max = 50)
    @Column(name = "RESIDN_DIREC_MAD")
    private String residnDirecMad;
    @Size(max = 50)
    @Column(name = "NOMBR_ATPRT_NAC_VIV")
    private String nombrAtprtNacViv;
    @Size(max = 20)
    @Column(name = "NUMREGPRFSNL_ATPRT_NAC_VIV")
    private String numregprfsnlAtprtNacViv;
    @Size(max = 10)
    @Column(name = "TELEFCEL_ATPRT_NAC_VIV")
    private String telefcelAtprtNacViv;
    @Size(max = 10)
    @Column(name = "TELEFCONVENOFI_ATPRT_NAC_VIV")
    private String telefconvenofiAtprtNacViv;
    @Size(max = 10)
    @Column(name = "TELFCONVENEXT_ATPRT_NAC_VIV")
    private String telfconvenextAtprtNacViv;
    @Size(max = 100)
    @Column(name = "EMAIL_ATPRT_NAC_VIV")
    private String emailAtprtNacViv;
    @Size(max = 1)
    @Column(name = "ESTADO_ATPRT_NAC_VIV")
    private String estadoAtprtNacViv;
    @Size(max = 20)
    @Column(name = "USERNAME_ATPRT_NAC_VIV")
    private String usernameAtprtNacViv;
    @Size(max = 30)
    @Column(name = "FK_ID_TIP_USU_ATPRT_NAC_VIV")
    private String fkIdTipUsuAtprtNacViv;
    @Size(max = 6)
    @Column(name = "CAMPO_INEC_RC_PCP_NAC_VIV")
    private String campoInecRcPcpNacViv;
    @Size(max = 2)
    @Column(name = "CAMPO_INEC_RC_OFI_NAC_VIV")
    private String campoInecRcOfiNacViv;
    @Column(name = "CAMPO_INEC_FEC_CRI_NAC_VIV")
    @Temporal(TemporalType.DATE)
    private Date campoInecFecCriNacViv;
    @Size(max = 7)
    @Column(name = "CAMPO_INEC_CODIG_STBLC_NAC_VIV")
    private String campoInecCodigStblcNacViv;
    @Size(max = 2)
    @Column(name = "CAMPO_INEC_COD_PAIS_MAD")
    private String campoInecCodPaisMad;
    @Size(max = 50)
    @Column(name = "FK_ID_TIPO_AREA_NV")
    private String fkIdTipoAreaNv;
    @Size(max = 10)
    @Column(name = "CAMPO_INEC_AREA_NAC_VIV")
    private String campoInecAreaNacViv;
    @Size(max = 50)
    @Column(name = "FK_ID_TIPO_AREA_MAD")
    private String fkIdTipoAreaMad;
    @Size(max = 10)
    @Column(name = "CAMPO_INEC_AREA_MAD")
    private String campoInecAreaMad;
    @Size(max = 2)
    @Column(name = "CAMPO_INEC_COD_CRI_COD")
    private String campoInecCodCriCod;
    @Column(name = "FECHA_AUDIT")
    @Temporal(TemporalType.DATE)
    private Date fechaAudit;
    @Size(max = 120)
    @Column(name = "USUARIO_CAMBIO")
    private String usuarioCambio;

    public AuditoriaRenavi() {
    }

    public AuditoriaRenavi(BigDecimal idAud) {
        this.idAud = idAud;
    }

    public AuditoriaRenavi(BigDecimal idAud, BigInteger idNacViv, BigInteger idMad) {
        this.idAud = idAud;
        this.idNacViv = idNacViv;
        this.idMad = idMad;
    }

    public BigDecimal getIdAud() {
        return idAud;
    }

    public void setIdAud(BigDecimal idAud) {
        this.idAud = idAud;
    }

    public BigInteger getIdNacViv() {
        return idNacViv;
    }

    public void setIdNacViv(BigInteger idNacViv) {
        this.idNacViv = idNacViv;
    }

    public String getProviRcdscNacViv() {
        return proviRcdscNacViv;
    }

    public void setProviRcdscNacViv(String proviRcdscNacViv) {
        this.proviRcdscNacViv = proviRcdscNacViv;
    }

    public String getCantnRcdscNacViv() {
        return cantnRcdscNacViv;
    }

    public void setCantnRcdscNacViv(String cantnRcdscNacViv) {
        this.cantnRcdscNacViv = cantnRcdscNacViv;
    }

    public String getPaarqRcdscNacViv() {
        return paarqRcdscNacViv;
    }

    public void setPaarqRcdscNacViv(String paarqRcdscNacViv) {
        this.paarqRcdscNacViv = paarqRcdscNacViv;
    }

    public String getOficiRcNacViv() {
        return oficiRcNacViv;
    }

    public void setOficiRcNacViv(String oficiRcNacViv) {
        this.oficiRcNacViv = oficiRcNacViv;
    }

    public String getFkIdTipIns() {
        return fkIdTipIns;
    }

    public void setFkIdTipIns(String fkIdTipIns) {
        this.fkIdTipIns = fkIdTipIns;
    }

    public Date getFechaInscrNacViv() {
        return fechaInscrNacViv;
    }

    public void setFechaInscrNacViv(Date fechaInscrNacViv) {
        this.fechaInscrNacViv = fechaInscrNacViv;
    }

    public String getActaInscrNacViv() {
        return actaInscrNacViv;
    }

    public void setActaInscrNacViv(String actaInscrNacViv) {
        this.actaInscrNacViv = actaInscrNacViv;
    }

    public String getNombrNacViv() {
        return nombrNacViv;
    }

    public void setNombrNacViv(String nombrNacViv) {
        this.nombrNacViv = nombrNacViv;
    }

    public String getApellNacViv() {
        return apellNacViv;
    }

    public void setApellNacViv(String apellNacViv) {
        this.apellNacViv = apellNacViv;
    }

    public String getCedulNacViv() {
        return cedulNacViv;
    }

    public void setCedulNacViv(String cedulNacViv) {
        this.cedulNacViv = cedulNacViv;
    }

    public String getFkIdSexoNv() {
        return fkIdSexoNv;
    }

    public void setFkIdSexoNv(String fkIdSexoNv) {
        this.fkIdSexoNv = fkIdSexoNv;
    }

    public Short getTallaNacViv() {
        return tallaNacViv;
    }

    public void setTallaNacViv(Short tallaNacViv) {
        this.tallaNacViv = tallaNacViv;
    }

    public Short getPesoNacViv() {
        return pesoNacViv;
    }

    public void setPesoNacViv(Short pesoNacViv) {
        this.pesoNacViv = pesoNacViv;
    }

    public Short getSemanGstcnNacViv() {
        return semanGstcnNacViv;
    }

    public void setSemanGstcnNacViv(Short semanGstcnNacViv) {
        this.semanGstcnNacViv = semanGstcnNacViv;
    }

    public String getFkIdTipPar() {
        return fkIdTipPar;
    }

    public void setFkIdTipPar(String fkIdTipPar) {
        this.fkIdTipPar = fkIdTipPar;
    }

    public Date getFechaNacimNacViv() {
        return fechaNacimNacViv;
    }

    public void setFechaNacimNacViv(Date fechaNacimNacViv) {
        this.fechaNacimNacViv = fechaNacimNacViv;
    }

    public String getFkIdProEmb() {
        return fkIdProEmb;
    }

    public void setFkIdProEmb(String fkIdProEmb) {
        this.fkIdProEmb = fkIdProEmb;
    }

    public String getFkIdLugNac() {
        return fkIdLugNac;
    }

    public void setFkIdLugNac(String fkIdLugNac) {
        this.fkIdLugNac = fkIdLugNac;
    }

    public String getLugNacEspecifique() {
        return lugNacEspecifique;
    }

    public void setLugNacEspecifique(String lugNacEspecifique) {
        this.lugNacEspecifique = lugNacEspecifique;
    }

    public String getNombrStblcNacViv() {
        return nombrStblcNacViv;
    }

    public void setNombrStblcNacViv(String nombrStblcNacViv) {
        this.nombrStblcNacViv = nombrStblcNacViv;
    }

    public String getProviEstbdsNacViv() {
        return proviEstbdsNacViv;
    }

    public void setProviEstbdsNacViv(String proviEstbdsNacViv) {
        this.proviEstbdsNacViv = proviEstbdsNacViv;
    }

    public String getCantnEstbdsNacViv() {
        return cantnEstbdsNacViv;
    }

    public void setCantnEstbdsNacViv(String cantnEstbdsNacViv) {
        this.cantnEstbdsNacViv = cantnEstbdsNacViv;
    }

    public String getParrqEstbdsNacViv() {
        return parrqEstbdsNacViv;
    }

    public void setParrqEstbdsNacViv(String parrqEstbdsNacViv) {
        this.parrqEstbdsNacViv = parrqEstbdsNacViv;
    }

    public String getCddlocStblcNacViv() {
        return cddlocStblcNacViv;
    }

    public void setCddlocStblcNacViv(String cddlocStblcNacViv) {
        this.cddlocStblcNacViv = cddlocStblcNacViv;
    }

    public String getDirecStblcNacViv() {
        return direcStblcNacViv;
    }

    public void setDirecStblcNacViv(String direcStblcNacViv) {
        this.direcStblcNacViv = direcStblcNacViv;
    }

    public String getTelefStblcNacViv() {
        return telefStblcNacViv;
    }

    public void setTelefStblcNacViv(String telefStblcNacViv) {
        this.telefStblcNacViv = telefStblcNacViv;
    }

    public String getFkIdTipAsi() {
        return fkIdTipAsi;
    }

    public void setFkIdTipAsi(String fkIdTipAsi) {
        this.fkIdTipAsi = fkIdTipAsi;
    }

    public String getTipAsiEspecifique() {
        return tipAsiEspecifique;
    }

    public void setTipAsiEspecifique(String tipAsiEspecifique) {
        this.tipAsiEspecifique = tipAsiEspecifique;
    }

    public String getFkIdEstFir() {
        return fkIdEstFir;
    }

    public void setFkIdEstFir(String fkIdEstFir) {
        this.fkIdEstFir = fkIdEstFir;
    }

    public String getFkIdEstReg() {
        return fkIdEstReg;
    }

    public void setFkIdEstReg(String fkIdEstReg) {
        this.fkIdEstReg = fkIdEstReg;
    }

    public String getObsrvAtprtNacViv() {
        return obsrvAtprtNacViv;
    }

    public void setObsrvAtprtNacViv(String obsrvAtprtNacViv) {
        this.obsrvAtprtNacViv = obsrvAtprtNacViv;
    }

    public BigInteger getIdMad() {
        return idMad;
    }

    public void setIdMad(BigInteger idMad) {
        this.idMad = idMad;
    }

    public String getNombrMad() {
        return nombrMad;
    }

    public void setNombrMad(String nombrMad) {
        this.nombrMad = nombrMad;
    }

    public String getFkIdPaisMad() {
        return fkIdPaisMad;
    }

    public void setFkIdPaisMad(String fkIdPaisMad) {
        this.fkIdPaisMad = fkIdPaisMad;
    }

    public String getFkIdNacMad() {
        return fkIdNacMad;
    }

    public void setFkIdNacMad(String fkIdNacMad) {
        this.fkIdNacMad = fkIdNacMad;
    }

    public String getCedulMad() {
        return cedulMad;
    }

    public void setCedulMad(String cedulMad) {
        this.cedulMad = cedulMad;
    }

    public String getPasapMad() {
        return pasapMad;
    }

    public void setPasapMad(String pasapMad) {
        this.pasapMad = pasapMad;
    }

    public Date getFechaNacimMad() {
        return fechaNacimMad;
    }

    public void setFechaNacimMad(Date fechaNacimMad) {
        this.fechaNacimMad = fechaNacimMad;
    }

    public Short getEdadMad() {
        return edadMad;
    }

    public void setEdadMad(Short edadMad) {
        this.edadMad = edadMad;
    }

    public Serializable getFotoMad() {
        return fotoMad;
    }

    public void setFotoMad(Serializable fotoMad) {
        this.fotoMad = fotoMad;
    }

    public String getCondicCedMad() {
        return condicCedMad;
    }

    public void setCondicCedMad(String condicCedMad) {
        this.condicCedMad = condicCedMad;
    }

    public String getConyugeMad() {
        return conyugeMad;
    }

    public void setConyugeMad(String conyugeMad) {
        this.conyugeMad = conyugeMad;
    }

    public String getFecFallecMad() {
        return fecFallecMad;
    }

    public void setFecFallecMad(String fecFallecMad) {
        this.fecFallecMad = fecFallecMad;
    }

    public String getNomPadreMad() {
        return nomPadreMad;
    }

    public void setNomPadreMad(String nomPadreMad) {
        this.nomPadreMad = nomPadreMad;
    }

    public String getNomMadreMad() {
        return nomMadreMad;
    }

    public void setNomMadreMad(String nomMadreMad) {
        this.nomMadreMad = nomMadreMad;
    }

    public Short getCntrlPrntlNacViv() {
        return cntrlPrntlNacViv;
    }

    public void setCntrlPrntlNacViv(Short cntrlPrntlNacViv) {
        this.cntrlPrntlNacViv = cntrlPrntlNacViv;
    }

    public Short getNumerPartoNacViv() {
        return numerPartoNacViv;
    }

    public void setNumerPartoNacViv(Short numerPartoNacViv) {
        this.numerPartoNacViv = numerPartoNacViv;
    }

    public Short getHijosVivsaMad() {
        return hijosVivsaMad;
    }

    public void setHijosVivsaMad(Short hijosVivsaMad) {
        this.hijosVivsaMad = hijosVivsaMad;
    }

    public Short getHijosNvmrtMad() {
        return hijosNvmrtMad;
    }

    public void setHijosNvmrtMad(Short hijosNvmrtMad) {
        this.hijosNvmrtMad = hijosNvmrtMad;
    }

    public Short getHijosNmrtsMad() {
        return hijosNmrtsMad;
    }

    public void setHijosNmrtsMad(Short hijosNmrtsMad) {
        this.hijosNmrtsMad = hijosNmrtsMad;
    }

    public String getFkIdIdeEtn() {
        return fkIdIdeEtn;
    }

    public void setFkIdIdeEtn(String fkIdIdeEtn) {
        this.fkIdIdeEtn = fkIdIdeEtn;
    }

    public String getFkIdEstCivMad() {
        return fkIdEstCivMad;
    }

    public void setFkIdEstCivMad(String fkIdEstCivMad) {
        this.fkIdEstCivMad = fkIdEstCivMad;
    }

    public String getFkIdSabeLeerMad() {
        return fkIdSabeLeerMad;
    }

    public void setFkIdSabeLeerMad(String fkIdSabeLeerMad) {
        this.fkIdSabeLeerMad = fkIdSabeLeerMad;
    }

    public String getFkIdNivelInstr() {
        return fkIdNivelInstr;
    }

    public void setFkIdNivelInstr(String fkIdNivelInstr) {
        this.fkIdNivelInstr = fkIdNivelInstr;
    }

    public String getResidnProvdsMad() {
        return residnProvdsMad;
    }

    public void setResidnProvdsMad(String residnProvdsMad) {
        this.residnProvdsMad = residnProvdsMad;
    }

    public String getResidnCantdsMad() {
        return residnCantdsMad;
    }

    public void setResidnCantdsMad(String residnCantdsMad) {
        this.residnCantdsMad = residnCantdsMad;
    }

    public String getResidnParrdsMad() {
        return residnParrdsMad;
    }

    public void setResidnParrdsMad(String residnParrdsMad) {
        this.residnParrdsMad = residnParrdsMad;
    }

    public String getResidnLocalMad() {
        return residnLocalMad;
    }

    public void setResidnLocalMad(String residnLocalMad) {
        this.residnLocalMad = residnLocalMad;
    }

    public String getResidnDirecMad() {
        return residnDirecMad;
    }

    public void setResidnDirecMad(String residnDirecMad) {
        this.residnDirecMad = residnDirecMad;
    }

    public String getNombrAtprtNacViv() {
        return nombrAtprtNacViv;
    }

    public void setNombrAtprtNacViv(String nombrAtprtNacViv) {
        this.nombrAtprtNacViv = nombrAtprtNacViv;
    }

    public String getNumregprfsnlAtprtNacViv() {
        return numregprfsnlAtprtNacViv;
    }

    public void setNumregprfsnlAtprtNacViv(String numregprfsnlAtprtNacViv) {
        this.numregprfsnlAtprtNacViv = numregprfsnlAtprtNacViv;
    }

    public String getTelefcelAtprtNacViv() {
        return telefcelAtprtNacViv;
    }

    public void setTelefcelAtprtNacViv(String telefcelAtprtNacViv) {
        this.telefcelAtprtNacViv = telefcelAtprtNacViv;
    }

    public String getTelefconvenofiAtprtNacViv() {
        return telefconvenofiAtprtNacViv;
    }

    public void setTelefconvenofiAtprtNacViv(String telefconvenofiAtprtNacViv) {
        this.telefconvenofiAtprtNacViv = telefconvenofiAtprtNacViv;
    }

    public String getTelfconvenextAtprtNacViv() {
        return telfconvenextAtprtNacViv;
    }

    public void setTelfconvenextAtprtNacViv(String telfconvenextAtprtNacViv) {
        this.telfconvenextAtprtNacViv = telfconvenextAtprtNacViv;
    }

    public String getEmailAtprtNacViv() {
        return emailAtprtNacViv;
    }

    public void setEmailAtprtNacViv(String emailAtprtNacViv) {
        this.emailAtprtNacViv = emailAtprtNacViv;
    }

    public String getEstadoAtprtNacViv() {
        return estadoAtprtNacViv;
    }

    public void setEstadoAtprtNacViv(String estadoAtprtNacViv) {
        this.estadoAtprtNacViv = estadoAtprtNacViv;
    }

    public String getUsernameAtprtNacViv() {
        return usernameAtprtNacViv;
    }

    public void setUsernameAtprtNacViv(String usernameAtprtNacViv) {
        this.usernameAtprtNacViv = usernameAtprtNacViv;
    }

    public String getFkIdTipUsuAtprtNacViv() {
        return fkIdTipUsuAtprtNacViv;
    }

    public void setFkIdTipUsuAtprtNacViv(String fkIdTipUsuAtprtNacViv) {
        this.fkIdTipUsuAtprtNacViv = fkIdTipUsuAtprtNacViv;
    }

    public String getCampoInecRcPcpNacViv() {
        return campoInecRcPcpNacViv;
    }

    public void setCampoInecRcPcpNacViv(String campoInecRcPcpNacViv) {
        this.campoInecRcPcpNacViv = campoInecRcPcpNacViv;
    }

    public String getCampoInecRcOfiNacViv() {
        return campoInecRcOfiNacViv;
    }

    public void setCampoInecRcOfiNacViv(String campoInecRcOfiNacViv) {
        this.campoInecRcOfiNacViv = campoInecRcOfiNacViv;
    }

    public Date getCampoInecFecCriNacViv() {
        return campoInecFecCriNacViv;
    }

    public void setCampoInecFecCriNacViv(Date campoInecFecCriNacViv) {
        this.campoInecFecCriNacViv = campoInecFecCriNacViv;
    }

    public String getCampoInecCodigStblcNacViv() {
        return campoInecCodigStblcNacViv;
    }

    public void setCampoInecCodigStblcNacViv(String campoInecCodigStblcNacViv) {
        this.campoInecCodigStblcNacViv = campoInecCodigStblcNacViv;
    }

    public String getCampoInecCodPaisMad() {
        return campoInecCodPaisMad;
    }

    public void setCampoInecCodPaisMad(String campoInecCodPaisMad) {
        this.campoInecCodPaisMad = campoInecCodPaisMad;
    }

    public String getFkIdTipoAreaNv() {
        return fkIdTipoAreaNv;
    }

    public void setFkIdTipoAreaNv(String fkIdTipoAreaNv) {
        this.fkIdTipoAreaNv = fkIdTipoAreaNv;
    }

    public String getCampoInecAreaNacViv() {
        return campoInecAreaNacViv;
    }

    public void setCampoInecAreaNacViv(String campoInecAreaNacViv) {
        this.campoInecAreaNacViv = campoInecAreaNacViv;
    }

    public String getFkIdTipoAreaMad() {
        return fkIdTipoAreaMad;
    }

    public void setFkIdTipoAreaMad(String fkIdTipoAreaMad) {
        this.fkIdTipoAreaMad = fkIdTipoAreaMad;
    }

    public String getCampoInecAreaMad() {
        return campoInecAreaMad;
    }

    public void setCampoInecAreaMad(String campoInecAreaMad) {
        this.campoInecAreaMad = campoInecAreaMad;
    }

    public String getCampoInecCodCriCod() {
        return campoInecCodCriCod;
    }

    public void setCampoInecCodCriCod(String campoInecCodCriCod) {
        this.campoInecCodCriCod = campoInecCodCriCod;
    }

    public Date getFechaAudit() {
        return fechaAudit;
    }

    public void setFechaAudit(Date fechaAudit) {
        this.fechaAudit = fechaAudit;
    }

    public String getUsuarioCambio() {
        return usuarioCambio;
    }

    public void setUsuarioCambio(String usuarioCambio) {
        this.usuarioCambio = usuarioCambio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAud != null ? idAud.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditoriaRenavi)) {
            return false;
        }
        AuditoriaRenavi other = (AuditoriaRenavi) object;
        if ((this.idAud == null && other.idAud != null) || (this.idAud != null && !this.idAud.equals(other.idAud))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.AuditoriaRenavi[ idAud=" + idAud + " ]";
    }
    
}
