package es.sacyl.gsa.inform.ctrl;

import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.bean.DWIndicadorHis;
import es.sacyl.gsa.inform.bean.DWIndicadorValor;
import es.sacyl.gsa.inform.dao.DWDao;
import es.sacyl.gsa.inform.dao.DWIndicadorDao;
import es.sacyl.gsa.inform.dao.HpHisClinicaDao;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 *
 * Carga los datos del periodo de estudio en la tabla DW_HOS_INDICADORES
 *
 * Para realiar el proceso debe existir el mismo cálculo en HIS
 *
 * Transformaciones de datos.
 *
 * El indicador hphis.est_servi.codivar a DW_indicador segun los valores
 * definidos en la tabla dw_indicadores
 *
 * Calcula el area SIAE a partir del servicio hphis.est_servi.servicio a partir
 * de las definiciones funcionales
 *
 * Código oficial del servicio a paritr de hphis.servicio.maes_serv
 *
 *
 *
 *
 */
public final class IndicadoresEtlCrl {

    private static final Logger logger = LogManager.getLogger(IndicadoresEtlCrl.class);

    private final LocalDate desde;
    private final LocalDate hasta;
    private final String tipo;
    private ArrayList<DWIndicador> indicadoresLista = new ArrayList<>();
    private int norden = 0;

    /**
     *
     * @param desde
     * @param hasta
     * @param tipo tipo de area de actividad HIS que se va a calcula
     * @param info
     */
    public IndicadoresEtlCrl(LocalDate desde, LocalDate hasta, String tipo) {
        this.desde = desde;
        this.hasta = hasta;
        this.tipo = tipo;

        norden = new HpHisClinicaDao().existeCalculoPeriodo(desde, hasta);
        if (norden == 0) {
            Notification.show(" No hay datos para esas fechas ");
        } else if (norden < 0) {
            Notification.show("Varios cálculos para esas ");
        } else {
            indicadoresLista = new DWIndicadorDao().getLista(getAreaDW(tipo));
        }
    }

    /**
     *
     * @param tipo
     * @return En función del área de cáculo de his recupera el área para
     * dw_indicadores
     */
    public String getAreaDW(String tipo) {
        String area = "";
        switch (tipo) {
            case "HOS":
                area = "HOSPITALIZACION";
                break;
            case "CEX":
                area = "CONSULTAS";
                break;
            case "LEQ":
                area = "LISTAESPERAQUI";
                break;
            case "QUI":
                area = "QUIROFANO";
                break;
            case "URG":
                area = "URGENCIAS";
                break;
        }
        return area;
    }

    /**
     * Para cada área de his HOS CEX QUI URG LEQ HDIA método diferente de
     * cálculo
     */
    public void doProcesa() {
        if (tipo.equals(DWIndicador.AREACALCULOCEX)) {

        } else if (tipo.equals(DWIndicador.AREACALCULOHDIA)) {

        } else if (tipo.equals(DWIndicador.AREACALCULOHOS)) {
            doProcesaHospitalizacion();
        } else if (tipo.equals(DWIndicador.AREACALCULOQUI)) {

        } else if (tipo.equals(DWIndicador.AREACALCULOURG)) {

        }
    }

    /**
     * Lee los datos de la tabla est_servi de his en función del norden que ha
     * validado que existe y es único para las fechas desde hasta en
     * clinica.est_servi
     *
     * Para cada registro calcula en indicador DW a partir del
     * clinica.est_servi.codivar
     *
     * calcual el área de hospitalización a paritr de clinica.est_servi.servicio
     *
     * según siae recodifica los servicio clinica.est_servi.servicio
     *
     * graba en la tabla dw_hos_indicadors
     *
     */
    public void doProcesaHospitalizacion() {
        // procesa los datos de la tabla est_servi

        ArrayList<DWIndicadorHis> lista = new HpHisClinicaDao().getListaEst_Servi(norden);
        lista.forEach(indHis -> {
            DWIndicadorValor indicadorDw = new DWIndicadorValor();
            indicadorDw.setMes(indHis.getMes());
            indicadorDw.setAno(indHis.getAnyo());
            indicadorDw.setValor(indHis.getValor());
            indicadorDw.setIndicador(getCodIndicadorDW(indHis.getCodivar()));
            // ha econtrado un indicador HOSP?????? asociado al codivarhis
            if (indicadorDw.getIndicador() != null) {
                // busca el área de hospitalizacion a partir del servicio
                indicadorDw.setDimension1(getAreaHospitalizacionSiae(indHis.getServicio().trim()));
                // el servicio DW lo saca de maes_serv
                indicadorDw.setServicio(indHis.getMaes_sev().trim());
                if (indicadorDw.getValor() != 0) {
                    new DWDao().doGrabaDatos(indicadorDw, "DW_HOS_INDICADORES");
                }
            }
        });

    }

    /**
     *
     * @param codivarhis
     * @return Recupera un indicador HOSP???? a partir del codivarhis la tabla
     * DW_INDICADORES tiene el valor codivarhis
     */
    public DWIndicador getCodIndicadorDW(String codivarhis) {
        DWIndicador dWIndicador = null;
        for (DWIndicador indi : indicadoresLista) {
            if (codivarhis != null && indi.getCodivarhis() != null && indi.getCodivarhis().equals(Integer.parseInt(codivarhis))) {
                dWIndicador = indi;
            }
        }
        if (dWIndicador == null) {
            logger.debug("Código indicador no encontrado en DW_INDICADORES para hphis.est_setv.codivar=" + codivarhis);
        }
        return dWIndicador;
    }

    /**
     *
     * @return
     *
     * add("PSQAGUDOS"); add("PSQINFANTO"); add("PSQDESINTO"); add("PSQDUALES");
     * add("PSQALIMENTO"); add(""); add("INTENSIVAPED"); add("QUEMADOS");
     *
     * recupera el área de hospitalización en función del servicio his
     *
     * ADM HPDS HPSQ
     *
     * "MEDICAS" CAR DIG HEMA MIR NEF NML NRL
     *
     * "QUIRÚRGICAS" CG DER GIN OFT ORL TRA URO
     *
     * "NEONATOLOGIA" NEO
     *
     * "OBSTETRICIA" MAT
     *
     * "INTENSIVA" UCI
     *
     * "PEDIATRIA" PED
     *
     * PSQAGUDOS PSQ
     *
     * PSQCRONICOS HPDS
     *
     * PALIATIVOS HPCP
     *
     * PSQDESINTO HPSQ
     *
     *
     */
    public String getAreaHospitalizacionSiae(String servicio) {
        String cadena = "";
        switch (servicio.trim()) {
            case "CAR":
                cadena = "MEDICAS";
                break;
            case "DIG":
                cadena = "MEDICAS";
                break;
            case "HEMA":
                cadena = "MEDICAS";
                break;
            case "MIR":
                cadena = "MEDICAS";
                break;
            case "NEF":
                cadena = "MEDICAS";
                break;
            case "NML":
                cadena = "MEDICAS";
                break;
            case "NRL":
                cadena = "MEDICAS";
                break;
            case "CG":
                cadena = "QUIRÚRGICAS";
                break;
            case "DER":
                cadena = "QUIRÚRGICAS";
                break;
            case "GIN":
                cadena = "QUIRÚRGICAS";
                break;
            case "OFT":
                cadena = "QUIRÚRGICAS";
                break;
            case "ORL":
                cadena = "QUIRÚRGICAS";
                break;
            case "TRA":
                cadena = "QUIRÚRGICAS";
                break;
            case "URO":
                cadena = "QUIRÚRGICAS";
                break;
            case "MAT":
                cadena = "OBSTETRICIA";
                break;
            case "NEO":
                cadena = "NEONATOLOGIA";
                break;
            case "PED":
                cadena = "PEDIATRIA";
                break;
            case "PSQ":
                cadena = "PSQAGUDOS";
                break;
            case "HPDS":
                cadena = "PSQCRONICOS";
                break;
            case "HPSQ":
                cadena = "PSQDESINTO";
                break;
            case "UCI":
                cadena = "INTENSIVA";
                break;
            case "HPCP":
                cadena = "PALIATIVOS";
                break;
            default:
                logger.debug("Sin definir area SIAE para el Servicio his:" + servicio);
                cadena = "SINDEFINIR";
                break;
        }
        return cadena;
    }

}
