package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class VlanBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String direccion;
    private String nombre;
    private String mascara;
    private String puertaenlace;
    private String comentario;
    private String broadcast;
    private String ultimaIp;
    private Integer numeroDirecciones;

    private ArrayList<IpBean> ipsDelRango = new ArrayList<>();

    public VlanBean() {
        super();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuertaenlace() {
        return puertaenlace;
    }

    public void setPuertaenlace(String puertaenlace) {
        this.puertaenlace = puertaenlace;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public String getUltimaIp() {
        return ultimaIp;
    }

    public void setUltimaIp(String ultimaIp) {
        this.ultimaIp = ultimaIp;
    }

    public Integer getNumeroDirecciones() {
        return numeroDirecciones;
    }

    public void setNumeroDirecciones(Integer numeroDirecciones) {
        this.numeroDirecciones = numeroDirecciones;
    }

    public ArrayList<IpBean> getIpsDelRango() {
        return ipsDelRango;
    }

    public void setIpsDelRango(ArrayList<IpBean> ipsDelRango) {
        this.ipsDelRango = ipsDelRango;
    }

}
