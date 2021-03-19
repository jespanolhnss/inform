/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author 06551256M
 */
public class EquipoAplicacionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private EquipoBean equipo;
    private AplicacionBean aplicacion;
    private LocalDate fecha;
    private String comentario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EquipoBean getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoBean equipo) {
        this.equipo = equipo;
    }

    public AplicacionBean getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(AplicacionBean aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getApliacacionString() {
        if (aplicacion != null && aplicacion.getDescripcion() != null) {
            return aplicacion.getDescripcion();
        } else {
            return "";
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
