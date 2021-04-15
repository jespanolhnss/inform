package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViajeBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime salida;
    private LocalDateTime llegada;

    private String matricula;

    private ArrayList<ViajeCentroBean> listaCentros = new ArrayList<>();

    private ArrayList<UsuarioBean> listaTecnicos = new ArrayList<>();

    public ViajeBean() {
        super();
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    public String getSalidaString() {
        DateTimeFormatter formatterdd_mm_yyyy_hh_mm = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        return formatterdd_mm_yyyy_hh_mm.format(salida);
    }

    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }

    public LocalDateTime getLlegada() {
        return llegada;
    }

    public void setLlegada(LocalDateTime llegada) {
        this.llegada = llegada;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public ArrayList<ViajeCentroBean> getListaCentros() {
        return listaCentros;
    }

    public void setListaCentros(ArrayList<ViajeCentroBean> listaCentros) {
        this.listaCentros = listaCentros;
    }

    public ArrayList<UsuarioBean> getListaTecnicos() {
        return listaTecnicos;
    }

    public void setListaTecnicos(ArrayList<UsuarioBean> listaTecnicos) {
        this.listaTecnicos = listaTecnicos;
    }

}
