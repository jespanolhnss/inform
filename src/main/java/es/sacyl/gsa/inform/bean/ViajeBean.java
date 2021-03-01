package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class ViajeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate fecha;
    private int horaSalida;
    private int horaLlegada;
    private String matricula;
    private ArrayList<ViajeCentroBean> listaCentros = new ArrayList<>();
    private ArrayList<ViajeTecnicoBean> listaTecnicos = new ArrayList<>();

    public ViajeBean() {
        id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(int horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(int horaLlegada) {
        this.horaLlegada = horaLlegada;
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

    public ArrayList<ViajeTecnicoBean> getListaTecnicos() {
        return listaTecnicos;
    }

    public void setListaTecnicos(ArrayList<ViajeTecnicoBean> listaTecnicos) {
        this.listaTecnicos = listaTecnicos;
    }

}
