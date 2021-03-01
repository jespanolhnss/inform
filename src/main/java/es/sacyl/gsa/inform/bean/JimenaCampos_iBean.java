package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JimenaCampos_iBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(JimenaCampos_iBean.class);
    private Long id;
    private Long informe;
    private Long item;
    private String userid;
    private Long fecha;
    private Long hora;
    private String descripcion;
    private String unidades;
    private String referencias;
    private String flags;
    private Clob dato;
    private String codigo;
    private String tipo_codigo;
    private int estado;
    private int pegado_en_informe;
    private Blob datobin;
    private int nivel_visibilidad;
    private Long lateralidad;
    private Long itemvalor;
    private Long canal;
    private Long prueba;

    public JimenaCampos_iBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInforme() {
        return informe;
    }

    public void setInforme(Long informe) {
        this.informe = informe;
    }

    public Long getItem() {
        return item;
    }

    public void setItem(Long item) {
        this.item = item;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public Clob getDato() {
        return dato;
    }

    public void setDato(Clob dato) {
        this.dato = dato;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo_codigo() {
        return tipo_codigo;
    }

    public void setTipo_codigo(String tipo_codigo) {
        this.tipo_codigo = tipo_codigo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getPegado_en_informe() {
        return pegado_en_informe;
    }

    public void setPegado_en_informe(int pegado_en_informe) {
        this.pegado_en_informe = pegado_en_informe;
    }

    public Blob getDatobin() {
        return datobin;
    }

    public void setDatobin(Blob datobin) {
        this.datobin = datobin;
    }

    public int getNivel_visibilidad() {
        return nivel_visibilidad;
    }

    public void setNivel_visibilida(int nivel_visibilidad) {
        this.nivel_visibilidad = nivel_visibilidad;
    }

    public Long getLateralidad() {
        return lateralidad;
    }

    public void setLateralidad(Long lateralidad) {
        this.lateralidad = lateralidad;
    }

    public Long getItemvalor() {
        return itemvalor;
    }

    public void setItemvalor(Long itemvalor) {
        this.itemvalor = itemvalor;
    }

    public Long getCanal() {
        return canal;
    }

    public void setCanal(Long canal) {
        this.canal = canal;
    }

    public Long getPrueba() {
        return prueba;
    }

    public void setPrueba(Long prueba) {
        this.prueba = prueba;
    }

    public static JimenaCampos_iBean getCamposResulSet(ResultSet rs) {
        JimenaCampos_iBean campo_i = new JimenaCampos_iBean();
        try {
            campo_i.setId(rs.getLong("id"));
            campo_i.setInforme(rs.getLong("informe"));
            campo_i.setItem(rs.getLong("item"));
            campo_i.setUserid(rs.getString("userid"));
            campo_i.setFecha(rs.getLong("fecha"));
            campo_i.setHora(rs.getLong("hora"));
            campo_i.setDescripcion(rs.getString("descripcion"));
            campo_i.setUnidades(rs.getString("unidades"));
            campo_i.setReferencias(rs.getString("referencias"));
            campo_i.setFlags(rs.getString("flags"));
            campo_i.setDato(rs.getClob("dato"));
            campo_i.setCodigo(rs.getString("codigo"));
            campo_i.setTipo_codigo(rs.getString("tipo_codigo"));
            campo_i.setEstado(rs.getInt("estado"));
            campo_i.setPegado_en_informe(rs.getInt("pegado_en_informe"));
            campo_i.setDatobin(rs.getBlob("datobin"));
            campo_i.setNivel_visibilida(rs.getInt("nivel_visibilidad"));
            campo_i.setLateralidad(rs.getLong("lateralidad"));
            campo_i.setItemvalor(rs.getLong("itemvalor"));
            campo_i.setCanal(rs.getLong("canal"));
            campo_i.setPrueba(rs.getLong("prueba"));
        } catch (SQLException e) {

        }

        return campo_i;
    }

}
