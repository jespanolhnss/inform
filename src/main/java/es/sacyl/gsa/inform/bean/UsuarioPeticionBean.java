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
 * @author 06532775Q
 */
public class UsuarioPeticionBean extends MasterBean implements Serializable {
    
    Long idusuario;
    Long idpeticionario;
    LocalDate fechaSolicitud;
    String centros;
    String comentario;

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public Long getIdpeticionario() {
        return idpeticionario;
    }

    public void setIdpeticionario(Long idpeticionario) {
        this.idpeticionario = idpeticionario;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public String getCentros() {
        return centros;
    }

    public void setCentros(String centros) {
        this.centros = centros;
    }
    
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
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
    
}