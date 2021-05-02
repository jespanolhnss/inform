package es.sacyl.gsa.inform.bean;

import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class DWIndicador {

    /*
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
     */
 /*
    public static ArrayList<String> DWINDICADORAREAACTIVIDAD = new ArrayList<String>() {
        {
            add("AMBULATORIA");
            add("HOSPITALIZACION");
            add("CONSULTAS");
            add("URGENCIAS");
            add("PARTOS");
            add("LISTAESPERAQUI");
            add("QUIROFANO");
            add("PAPELERIA");
            add("OFT");
        }

    };
     */
 /*
    public static ArrayList<String> DWINDICADORTIPO = new ArrayList<String>() {
        {
            add("RECURSOS");
            add("ACTIVIDAD");
            add("CALIDAD");
            add("ECONÓMICOS");
        }

    };
     */
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

    public static String TIPORECURSOS = "RECURSOS";

    private String codigo;
    private String nombre;
    private String area;
    private Integer orden;
    private String visible;
    private String calculado;
    private String formula;
    private Long item;
    private Integer codivarhis;
    private String tablahis;
    private String sql;
    private String descripcion;
    private String tipo;

    public DWIndicador() {

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

    public Integer getCodivarhis() {
        return codivarhis;
    }

    public void setCodivarhis(Integer codivarhis) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
