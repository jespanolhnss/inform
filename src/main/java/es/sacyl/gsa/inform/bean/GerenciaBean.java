package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author juannietopajares
 */
public class GerenciaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //  private String codauto;
    private AutonomiaBean autonomia;
    private String codigo;
    private String nombre;
    private String tipovia;

    private String callesec;
    private Integer numcalsec;
    private String otrdomger;
    private String cpger;
    private LocalidadBean localidad;
    private ProvinciaBean provincia;

    private Integer estado;

    /*

       codauto          varchar2(2 char)
codigo              varchar2(2 char)
nombre             varchar2(65 char)
tipovia              varchar2(5 char)
callesec            varchar2(4000 char)
numcalsec     number(38,0)
otrdomger    varchar2(40 char)
cpger  varchar2(5 char)
localger          varchar2(4000 char)

           codauto ,codigo, nombre , tipovia, callesec ,numcalsec, otrdomger, cpger , localger
     */
    //  public static final GerenciaBean GERENCIADEFECTO = new GerenciaDao().getPorCodigo("17", "01");
    public AutonomiaBean getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(AutonomiaBean autonomia) {
        this.autonomia = autonomia;
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

    public String getTipovia() {
        return tipovia;
    }

    public void setTipovia(String tipovia) {
        this.tipovia = tipovia;
    }

    public String getCallesec() {
        return callesec;
    }

    public void setCallesec(String callesec) {
        this.callesec = callesec;
    }

    public Integer getNumcalsec() {
        return numcalsec;
    }

    public void setNumcalsec(Integer numcalsec) {
        this.numcalsec = numcalsec;
    }

    public String getOtrdomger() {
        return otrdomger;
    }

    public void setOtrdomger(String otrdomger) {
        this.otrdomger = otrdomger;
    }

    public String getCpger() {
        return cpger;
    }

    public void setCpger(String cpger) {
        this.cpger = cpger;
    }

    public LocalidadBean getLocalidad() {
        return localidad;
    }

    public void setLocalidad(LocalidadBean localidad) {
        this.localidad = localidad;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getEstadoString() {
        if (estado == null) {
            return "";
        } else {
            if (estado == 0) {
                return "N";
            } else {
                return "S";
            }
        }
    }

    public Boolean getEstadoBoolena() {
        if (estado == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setEstado(String estado) {
        if (estado.equals("S")) {
            this.estado = 1;
        } else {
            this.estado = 0;
        }
    }

    public void setEstado(Boolean estado) {
        if (estado == true) {
            this.estado = 1;
        } else {
            this.estado = 0;
        }
    }

    public ProvinciaBean getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaBean provincia) {
        this.provincia = provincia;
    }

}
