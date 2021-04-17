package es.sacyl.gsa.inform.bean;

import java.time.LocalDate;

/**
 *
 * @author 06551256M
 */
public class DWIndicadorHis {

    String servicio;
    String cod_enferme;
    Integer mes;
    Integer anyo;
    String codivar;
    Double valor;
    Integer norden;
    LocalDate fecha_desde;
    LocalDate fecha_hasta;

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getCod_enferme() {
        return cod_enferme;
    }

    public void setCod_enferme(String cod_enferme) {
        this.cod_enferme = cod_enferme;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnyo() {
        return anyo;
    }

    public void setAnyo(Integer anyo) {
        this.anyo = anyo;
    }

    public String getCodivar() {
        return codivar;
    }

    public void setCodivar(String codivar) {
        this.codivar = codivar;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getNorden() {
        return norden;
    }

    public void setNorden(Integer norden) {
        this.norden = norden;
    }

    public LocalDate getFecha_desde() {
        return fecha_desde;
    }

    public void setFecha_desde(LocalDate fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    public LocalDate getFecha_hasta() {
        return fecha_hasta;
    }

    public void setFecha_hasta(LocalDate fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

}
