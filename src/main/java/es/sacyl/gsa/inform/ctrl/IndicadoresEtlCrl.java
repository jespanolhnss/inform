package es.sacyl.gsa.inform.ctrl;

import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.bean.DWIndicadorHis;
import es.sacyl.gsa.inform.bean.DWIndicadorValor;
import es.sacyl.gsa.inform.dao.DWDao;
import es.sacyl.gsa.inform.dao.HpHisClinicaDao;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public final class IndicadoresEtlCrl {

    private static final Logger logger = LogManager.getLogger(IndicadoresEtlCrl.class);

    private LocalDate desde;
    private LocalDate hasta;
    private String tipo;
    private ArrayList<DWIndicador> indicadoresLista = new ArrayList<>();
    private int norden = 0;

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
            indicadoresLista = new DWDao().getLista(getAreaDW(tipo));
            // doProcesa();
        }
    }

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
                // busca el área de hospitalizacion
                indicadorDw.setAreahosp(getAreaHospitalizacionSiae(indHis.getServicio()));
                // recodifica servicio
                indicadorDw.setServicio(getRecodificaServHis(indHis.getServicio()));
                new DWDao().doGrabaDatos(indicadorDw, "DW_HOS_INDICADORES");
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
            //   System.out.println(indi.getCodivarhis() + "-" + codivarhis);
            if (codivarhis != null && indi.getCodivarhis() != null && indi.getCodivarhis().equals(codivarhis)) {
                dWIndicador = indi;
            }
        }
        if (dWIndicador == null) {
            logger.debug("Código indicador no encontrado para codigohis=" + codivarhis);
        }
        return dWIndicador;
    }

    /**
     *
     * @return
     *
     * add("PSQAGUDOS"); add("PSQINFANTO"); add("PSQDESINTO"); add("PSQDUALES");
     * add("PSQALIMENTO"); add("PALIATIVOS"); add("INTENSIVAPED");
     * add("QUEMADOS");
     *
     * recpera el área de hospitalización en función del servicio
     *
     * ADM HPCP HPDS HPSQ PSQ
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
            case "UCI":
                cadena = "INTENSIVA";
                break;

        }
        return cadena;
    }

    public String getRecodificaServHis(String servicio) {
        String cadena = "";
        switch (servicio) {

            case "HEMA":
                cadena = "HEM";
                break;

            case "CG":
                cadena = "CGD";
                break;

            case "MAT":
                cadena = "OBS";
                break;

            case "MIV":
                cadena = "INTENSIVA";
                break;
            default:
                cadena = servicio;

        }
        return cadena;
    }

}