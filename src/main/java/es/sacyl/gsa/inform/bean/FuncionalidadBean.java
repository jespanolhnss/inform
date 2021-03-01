package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class FuncionalidadBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String descripcion;
    private String textomenu;
    private Boolean estado;
    private LocalDate fechacambio;
    private UsuarioBean usucambio;
    private Boolean permitida;

    public static FuncionalidadBean PEDIRUSUARIO = new FuncionalidadBean(new Long(2));

    public FuncionalidadBean() {
        this.id = new Long(0);
    }

    public FuncionalidadBean(Long id) {
        this.id = id;
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

    public String getTextomenu() {
        return textomenu;
    }

    public void setTextomenu(String textomenu) {
        this.textomenu = textomenu;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDate getFechacambio() {
        return fechacambio;
    }

    public void setFechacambio(LocalDate fechacambio) {
        this.fechacambio = fechacambio;
    }

    public UsuarioBean getUsucambio() {
        return usucambio;
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

    public Boolean getPermitida() {
        return permitida;
    }

    public void setPermitida(Boolean permitida) {
        this.permitida = permitida;
    }

}
