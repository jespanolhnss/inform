package es.sacyl.gsa.inform.bean;

import es.sacyl.gsa.inform.util.Constantes;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class EquipoBean extends MasterBean {

    private String tipo;
    private String inventario;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private CentroBean centro;
    private UbicacionBean ubicacion;
    private GfhBean servicio;
    private String comentario;
    private ArrayList<IpBean> listaIps = new ArrayList<>();
    private ArrayList<EquipoAplicacionBean> aplicacinesArrayList = new ArrayList<>();

    public EquipoBean() {
        super();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInventario() {
        return inventario;
    }

    public void setInventario(String inventario) {
        this.inventario = inventario;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public CentroBean getCentro() {
        return centro;
    }

    public void setCentro(CentroBean centro) {
        this.centro = centro;
    }

    public UbicacionBean getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionBean ubicacion) {
        this.ubicacion = ubicacion;
    }

    public GfhBean getServicio() {
        return servicio;
    }

    public void setServicio(GfhBean servicio) {
        this.servicio = servicio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public ArrayList<IpBean> getListaIps() {
        return listaIps;
    }

    public void setListaIps(ArrayList<IpBean> listaIps) {
        this.listaIps = listaIps;
    }

    public ArrayList<EquipoAplicacionBean> getAplicacinesArrayList() {
        return aplicacinesArrayList;
    }

    public void setAplicacinesArrayList(ArrayList<EquipoAplicacionBean> aplicacinesArrayList) {
        this.aplicacinesArrayList = aplicacinesArrayList;
    }

    public String getIpsCadena() {
        String cadena = "";
        for (IpBean ipBean : getListaIps()) {
            if (cadena.length() > 8) {
                cadena = cadena.concat(",");
            }
            cadena = cadena.concat(ipBean.getIp());
        }
        return cadena;
    }

    @Override
    public String toString() {
        return "EquipoBean{" + "tipo=" + tipo + ", inventario=" + inventario + ", marca=" + marca + ", modelo=" + modelo + ", numeroSerie=" + numeroSerie + ", centro=" + centro + ", ubicacion=" + ubicacion + ", servicio=" + servicio + ", comentario=" + comentario + ", listaIps=" + listaIps + '}';
    }

    public String toHtml() {
        String cadena = "<b>";
        cadena = cadena.concat("Tipo:  ");

        if (tipo != null) {
            cadena = cadena.concat(tipo);
        } else {
            cadena = cadena.concat(" ");
        }

        cadena = cadena.concat(Constantes.HTMLESPACIO1 + "Inventario:   ");
        if (inventario != null) {
            cadena = cadena.concat(inventario);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("<hr> ");
        cadena = cadena.concat(Constantes.HTMLESPACIO1 + "Marca: ");
        if (marca != null) {
            cadena = cadena.concat(marca);
        } else {
            cadena = cadena.concat(" ");
        }

        cadena = cadena.concat("Modelo: ");
        if (modelo != null) {
            cadena = cadena.concat(modelo);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("NumeroSerie: ");
        if (numeroSerie != null) {
            cadena = cadena.concat(numeroSerie);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("<hr> ");
        cadena = cadena.concat("Centro: ");
        if (centro != null && centro.getNomcen() != null) {
            cadena = cadena.concat(centro.getNomcen());
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("<hr> ");
        cadena = cadena.concat("Ubicacion: ");
        if (ubicacion != null && ubicacion.getDescripcion() != null) {
            cadena = cadena.concat(ubicacion.getDescripcion());
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("<hr> ");
        cadena = cadena.concat("IP's: ");

        cadena = cadena.concat(getIpsCadena());

        cadena = cadena.concat("</b>");

        return cadena;
    }

}
