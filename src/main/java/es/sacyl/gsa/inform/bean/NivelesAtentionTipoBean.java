package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author JuanNieto
 */
public class NivelesAtentionTipoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String descripcion;
    private Integer estado;

    public static final NivelesAtentionTipoBean ESPECIALIZADA = new NivelesAtentionTipoBean(new Long(1), "A.ESPECIALIZADA");

    public NivelesAtentionTipoBean() {
        this.id = new Long(0);
    }

    public NivelesAtentionTipoBean(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
}
