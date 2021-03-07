package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ViajeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime salida;
    private LocalDateTime llegada;

    private String matricula;

    private ArrayList<ViajeCentroBean> listaCentros = new ArrayList<>();

    private ArrayList<UsuarioBean> listaTecnicos = new ArrayList<>();

    public ViajeBean() {
        id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSalida() {
        return salida;
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
