package es.sacyl.gsa.inform.bean;

import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class DWIndicador {

    public static ArrayList<String> HOSPIAREASSIAE = new ArrayList<String>() {
        {
            add("PSQAGUDOS");
            add("PSQINFANTO");
            add("PSQDESINTO");
            add("PSQDUALES");
            add("PSQALIMENTO");
            add("PALIATIVOS");
            add("MEDICAS");
            add("QUIRÚRGICAS");
            add("PEDIATRIA");
            add("NEONATOLOGIA");
            add("OBSTETRICIA");
            add("INTENSIVA");
            add("INTENSIVAPED");
            add("QUEMADOS");
        }
    };

    public static String AREACALCULOHOS = "HOS";
    public static String AREACALCULOCEX = "CEX";
    public static String AREACALCULOURG = "URG";
    public static String AREACALCULOQUI = "QUI";
    public static String AREACALCULOHDIA = "HDIA";

    public static ArrayList<String> AREASCALCULO = new ArrayList<String>() {
        {
            add(AREACALCULOHOS);
            add(AREACALCULOCEX);
            add(AREACALCULOURG);
            add(AREACALCULOQUI);
            add(AREACALCULOHDIA);
        }
    };

    public String codigo;
    public String nombre;
    public String area;
    public Integer orden;
    public String visible;
    public String calculado;
    public String formula;
    public Long item;
    public String codivarhis;
    public String tablahis;
    public String sql;
    public String descricion;

    public static ArrayList<String> getHOSAREASSIAE() {
        return HOSPIAREASSIAE;
    }

    public static void setHOSAREASSIAE(ArrayList<String> HOSAREASSIAE) {
        DWIndicador.HOSPIAREASSIAE = HOSAREASSIAE;
    }

    public static String getAREACALCULOHOS() {
        return AREACALCULOHOS;
    }

    public static void setAREACALCULOHOS(String AREACALCULOHOS) {
        DWIndicador.AREACALCULOHOS = AREACALCULOHOS;
    }

    public static String getAREACALCULOCEX() {
        return AREACALCULOCEX;
    }

    public static void setAREACALCULOCEX(String AREACALCULOCEX) {
        DWIndicador.AREACALCULOCEX = AREACALCULOCEX;
    }

    public static String getAREACALCULOURG() {
        return AREACALCULOURG;
    }

    public static void setAREACALCULOURG(String AREACALCULOURG) {
        DWIndicador.AREACALCULOURG = AREACALCULOURG;
    }

    public static String getAREACALCULOQUI() {
        return AREACALCULOQUI;
    }

    public static void setAREACALCULOQUI(String AREACALCULOQUI) {
        DWIndicador.AREACALCULOQUI = AREACALCULOQUI;
    }

    public static String getAREACALCULOHDIA() {
        return AREACALCULOHDIA;
    }

    public static void setAREACALCULOHDIA(String AREACALCULOHDIA) {
        DWIndicador.AREACALCULOHDIA = AREACALCULOHDIA;
    }

    public static ArrayList<String> getAREASCALCULO() {
        return AREASCALCULO;
    }

    public static void setAREASCALCULO(ArrayList<String> AREASCALCULO) {
        DWIndicador.AREASCALCULO = AREASCALCULO;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getCalculado() {
        return calculado;
    }

    public void setCalculado(String calculado) {
        this.calculado = calculado;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Long getItem() {
        return item;
    }

    public void setItem(Long item) {
        this.item = item;
    }

    public String getCodivarhis() {
        return codivarhis;
    }

    public void setCodivarhis(String codivarhis) {
        this.codivarhis = codivarhis;
    }

    public String getTablahis() {
        return tablahis;
    }

    public void setTablahis(String tablahis) {
        this.tablahis = tablahis;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDescricion() {
        return descricion;
    }

    public void setDescricion(String descricion) {
        this.descricion = descricion;
    }

}
