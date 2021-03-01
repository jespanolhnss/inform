package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author JuanNieto
 */
public class NivelesAtencionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String codigo;
    private String descripcion;
    private NivelesAtentionTipoBean tipo;
    private ProvinciaBean provincia ;
    private Integer estado;

    private static String titulo = "Niveles de atención sanitaria";

    public NivelesAtencionBean() {
this.id=new Long(0);
    }

    public NivelesAtencionBean(Long id) {
        this.id = id;
    }

    public NivelesAtencionBean(Long id, String codigo, String descripcion, NivelesAtentionTipoBean tipo) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public NivelesAtentionTipoBean getTipo() {
        return tipo;
    }

    public Long getTipoInteger() {
        if (tipo != null) {
            return tipo.getId();
        } else {
            return null;
        }
    }

    public void setTipo(NivelesAtentionTipoBean tipo) {
        this.tipo = tipo;
    }

    public ProvinciaBean getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaBean provincia) {
        this.provincia = provincia;
    }

    
    public static String getTitulo() {
        return titulo;
    }

    public static void setTitulo(String titulo) {
        NivelesAtencionBean.titulo = titulo;
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
