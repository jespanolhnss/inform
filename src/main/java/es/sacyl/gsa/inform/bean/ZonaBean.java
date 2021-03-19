package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author juannietopajares
 */
public class ZonaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //codauto,codgeren,codigo,nombre,codprov
    private AutonomiaBean autonomia;
    private GerenciaBean gerencia;
    private String codigo;
    private String nombre;
    private ProvinciaBean provincia;

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

    public AutonomiaBean getAutonomia() {
        return autonomia;
    }

    public String getAutonomiaString() {
        if (autonomia != null & autonomia.getCodigo() != null) {
            return autonomia.getCodigo();
        } else {
            return "";
        }
    }

    public void setAutonomia(AutonomiaBean autonomia) {
        this.autonomia = autonomia;
    }

    public GerenciaBean getGerencia() {
        return gerencia;
    }

    public String getGerenciaString() {
        if (gerencia != null && gerencia.getCodigo() != null) {
            return gerencia.getCodigo();
        }
        return "";
    }

    public void setGerencia(GerenciaBean gerencia) {
        this.gerencia = gerencia;
    }

    public ProvinciaBean getProvincia() {
        return provincia;
    }

    public String getProvinciaString() {
        if (provincia != null && provincia.getCodigo() != null) {
            return provincia.getCodigo();
        } else {
            return "";
        }
    }

    public void setProvincia(ProvinciaBean provincia) {
        this.provincia = provincia;
    }

}
