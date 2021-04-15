/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author juannietopajares
 */
public class CentroBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private AutonomiaBean autonomia;
    private GerenciaBean gerencia;
    private ZonaBean zona;
    private String codigo;
    private String nomcen;
    private String tipovia;
    private String callecen;
    private Integer numcalcen;
    private String otrdirecen;
    private LocalidadBean localidad;
    private String cpcentro;
    private String teleprev;
    private CentroTipoBean tipocentro;
    private NivelesAtencionBean nivatencion;

    private String mapgoogle;
    private String nomcorto;

    private ArrayList<CentroFicheroBean> centroFicheroArrayList = new ArrayList<>();

    private ArrayList<CentroUsuarioBean> centroUsuarioArrayList = new ArrayList<>();

    public CentroBean() {
        this.id = new Long(0);

    }

    public AutonomiaBean getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(AutonomiaBean autonomia) {
        this.autonomia = autonomia;
    }

    public GerenciaBean getGerencia() {
        return gerencia;
    }

    public void setGerencia(GerenciaBean gerencia) {
        this.gerencia = gerencia;
    }

    public ZonaBean getZona() {
        return zona;
    }

    public void setZona(ZonaBean zona) {
        this.zona = zona;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomcen() {
        return nomcen;
    }

    public String getNomcenParaCombo() {
        if (nomcorto != null) {
            return nomcorto;
        } else {
            return nomcen;
        }
    }

    public String getNomcenCorto() {
        if (nomcen != null && nomcen.length() > 15) {
            return nomcen.substring(0, 15);
        } else {
            return nomcen;
        }
    }

    public void setNomcen(String nomcen) {
        this.nomcen = nomcen;
    }

    public String getTipovia() {
        return tipovia;
    }

    public void setTipovia(String tipovia) {
        this.tipovia = tipovia;
    }

    public String getCallecen() {
        return callecen;
    }

    public void setCallecen(String callecen) {
        this.callecen = callecen;
    }

    public Integer getNumcalcen() {
        return numcalcen;
    }

    public void setNumcalcen(Integer numcalcen) {
        this.numcalcen = numcalcen;
    }

    public String getOtrdirecen() {
        return otrdirecen;
    }

    public void setOtrdirecen(String otrdirecen) {
        this.otrdirecen = otrdirecen;
    }

    public LocalidadBean getLocalidad() {
        return localidad;
    }

    public String getLocalidadString() {
        if (localidad != null) {
            return localidad.getNombre();
        } else {
            return "";
        }
    }

    public String getLocalidadCortoString() {
        if (localidad != null) {
            if (localidad.getNombre() != null && localidad.getNombre().length() > 15) {
                return localidad.getNombre().substring(0, 15);
            }
            return localidad.getNombre();
        } else {
            return "";
        }
    }

    public void setLocalidad(LocalidadBean localidad) {
        this.localidad = localidad;
    }

    public String getCpcentro() {
        return cpcentro;
    }

    public void setCpcentro(String cpcentro) {
        this.cpcentro = cpcentro;
    }

    public String getTeleprev() {
        return teleprev;
    }

    public void setTeleprev(String teleprev) {
        this.teleprev = teleprev;
    }

    public CentroTipoBean getTipocentro() {
        return tipocentro;
    }

    public String getTipocentroString() {
        if (tipocentro != null && tipocentro.getId() != null) {
            return Long.toString(tipocentro.getId());
        } else {
            return "";
        }
    }

    public void setTipocentro(CentroTipoBean tipocentro) {
        this.tipocentro = tipocentro;
    }

    public NivelesAtencionBean getNivatencion() {
        return nivatencion;
    }

    public void setNivatencion(NivelesAtencionBean nivatencion) {
        this.nivatencion = nivatencion;
    }

    public String getMapgoogle() {
        return mapgoogle;
    }

    public void setMapgoogle(String mapgoogle) {
        this.mapgoogle = mapgoogle;
    }

    public String getNomcorto() {
        return nomcorto;
    }

    public void setNomcorto(String nomcorto) {
        this.nomcorto = nomcorto;
    }

    public ArrayList<CentroFicheroBean> getCentroFicheroArrayList() {
        return centroFicheroArrayList;
    }

    public void setCentroFicheroArrayList(ArrayList<CentroFicheroBean> centroFicheroArrayList) {
        this.centroFicheroArrayList = centroFicheroArrayList;
    }

    public ArrayList<CentroUsuarioBean> getCentroUsuarioArrayList() {
        return centroUsuarioArrayList;
    }

    public void setCentroUsuarioArrayList(ArrayList<CentroUsuarioBean> centroUsuarioArrayList) {
        this.centroUsuarioArrayList = centroUsuarioArrayList;
    }

    public ArrayList<CentroFicheroBean> getCentroFicheroArrayListImg() {
        ArrayList<CentroFicheroBean> lista = new ArrayList<>();
        for (CentroFicheroBean fichero : this.getCentroFicheroArrayList()) {
            String extension = fichero.getNombreFichero().trim().substring(fichero.getNombreFichero().trim().length() - 3, fichero.getNombreFichero().trim().length());
            if (CentroFicheroBean.TIPOFICHEROIMG.contains(extension)) {
                lista.add(fichero);
            }
        }
        return lista;
    }

    public ArrayList<CentroFicheroBean> getCentroFicheroArrayListPdf() {
        ArrayList<CentroFicheroBean> lista = new ArrayList<>();
        for (CentroFicheroBean fichero : this.getCentroFicheroArrayList()) {
            String extension = fichero.getNombreFichero().trim().substring(fichero.getNombreFichero().trim().length() - 3, fichero.getNombreFichero().trim().length());
            if (CentroFicheroBean.TIPOFICHEROPDF.contains(extension)) {
                lista.add(fichero);
            }
        }
        return lista;
    }
}
