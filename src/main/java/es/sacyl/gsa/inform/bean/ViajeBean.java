package es.sacyl.gsa.inform.bean;

import es.sacyl.gsa.inform.dao.ViajesDao;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViajeBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime salida;
    private LocalDateTime llegada;
    private String matricula;

    /**
     * Esto es una pequeña chapu para montar un falso tree en el grid de la
     * pantalla el centro sólo se asigna valor en el método de seleccionar los
     * hijos el viaje padre es el viaje que tiene varios centros
     */
    private CentroBean centro;
    private ViajeBean viajePadre;

    private ArrayList<ViajeCentroBean> listaCentros = new ArrayList<>();
    private ArrayList<UsuarioBean> listaTecnicos = new ArrayList<>();

    public ViajeBean() {
        super();
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    public String getSalidaString() {
        DateTimeFormatter formatterdd_mm_yyyy_hh_mm = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
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

    /**
     *
     *
     * Estos métodos son lo que se usan para montar el falso tree
     */
    public CentroBean getCentro() {
        return centro;
    }

    public void setCentro(CentroBean centro) {
        this.centro = centro;
    }

    public String getCentroNombre() {
        String nombre = "";
        if (this.centro != null) {
            if (this.centro.getNomcen() != null) {
                nombre = this.centro.getNomcen();
            }
            /*
            if (this.centro.getNomcenCorto() != null) {
                nombre = this.centro.getNomcorto();
            }
             */
        }
        return nombre;
    }

    /**
     *
     * @return Para cada viaje crea una lista de viajes con centros con el viaje
     * padre
     */
    public ArrayList<ViajeBean> getListraCentrosTree() {
        return new ViajesDao().getListaViajesHijosTree(this);
    }

    public ViajeBean getViajePadre() {
        return viajePadre;
    }

    public void setViajePadre(ViajeBean viajePadre) {
        this.viajePadre = viajePadre;
    }

    public String getSalidaPadre() {
        if (viajePadre != null && viajePadre.getSalidaString() != null) {
            return viajePadre.getSalidaString();
        } else {
            return "";
        }
    }
}
