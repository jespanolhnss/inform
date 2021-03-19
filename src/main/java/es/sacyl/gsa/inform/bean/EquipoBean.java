package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class EquipoBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tipo;
    private String inventario;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String macadress;
    private CentroBean centro;
    private UbicacionBean ubicacion;
    private GfhBean servicio;
    private String comentario;
    private String nombredominio;
    private UsuarioBean usuario;
    private ArrayList<IpBean> listaIps = new ArrayList<>();
    private ArrayList<EquipoAplicacionBean> aplicacinesArrayList = new ArrayList<>();

    private ArrayList<DatoGenericoBean> datosGenericoBeans = new ArrayList<>();

    public static String TIPOCPU = "Cpu";
    public static String TIPOTELEFONO = "Teléfono";

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

    public String getMacadress() {
        return macadress;
    }

    public void setMacadress(String macadress) {
        this.macadress = macadress;
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

    public String getNombredominio() {
        return nombredominio;
    }

    public void setNombredominio(String nombredominio) {
        this.nombredominio = nombredominio;
    }

    public UsuarioBean getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
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

    public ArrayList<DatoGenericoBean> getDatosGenericoBeans() {
        return datosGenericoBeans;
    }

    public void setDatosGenericoBeans(ArrayList<DatoGenericoBean> datosGenericoBeans) {
        this.datosGenericoBeans = datosGenericoBeans;
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
        cadena = cadena.concat("<hr> ");
        cadena = cadena.concat("Inventario:   ");
        if (inventario != null) {
            cadena = cadena.concat(inventario);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("<hr> ");
        cadena = cadena.concat("Marca: ");
        if (marca != null) {
            cadena = cadena.concat(marca);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("<hr> ");
        cadena = cadena.concat("Modelo: ");
        if (modelo != null) {
            cadena = cadena.concat(modelo);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat("<hr> ");
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
