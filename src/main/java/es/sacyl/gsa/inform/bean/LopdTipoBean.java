package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class LopdTipoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String descripcion;
    private LopdSujetoBean sujeto; // 1 paciente, 2 trabajador, 3 usuario, 4 otro
    private Boolean mailReponsable;
    private boolean estado;
    private UsuarioBean usucambio;
    private LocalDate fechaCambio;
    public static final LopdTipoBean LOPDTIPOPORDEFECTO = new LopdTipoBean(new Long(2),"Informe no corresponde",LopdSujetoBean.SUJETO_PACIENTE,false);
 
    public LopdTipoBean() {
        this.id = new Long(0);
    }

    public LopdTipoBean(Long id, String descripcion, LopdSujetoBean sujeto, boolean mail) {
        this.id = id;
        this.descripcion = descripcion;
        this.sujeto = sujeto;
        this.mailReponsable = mail;

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

    public LopdSujetoBean getSujeto() {
        return sujeto;
    }

    public Integer getSujetoId() {
        if (sujeto != null) {
            return sujeto.getId();
        } else {
            return null;
        }
    }

    public String getSujetoDescripcion() {
        if (sujeto != null) {
            return sujeto.getDescripcion();
        } else {
            return "";
        }
    }

    public void setSujeto(LopdSujetoBean sujeto) {
        this.sujeto = sujeto;
    }

    public Boolean getMailReponsable() {
        return mailReponsable;
    }

    public void setMailReponsable(Boolean mailReponsable) {
        this.mailReponsable = mailReponsable;
    }

    public boolean isEstado() {
        return estado;
    }

    public Integer getEstadoInt() {
        if (estado) {
            return 1;
        } else {
            return 0;
        }

    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public UsuarioBean getUsucambio() {
        return usucambio;
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

}
