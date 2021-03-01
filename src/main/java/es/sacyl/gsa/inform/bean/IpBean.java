package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class IpBean extends MasterBean {

    private String ip;
    public EquipoBean equipo;
    private VlanBean vlan;

    public final static String IPLIBRESI = "S";
    public final static String IPLIBRENO = "N";

    public IpBean() {
        super();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public EquipoBean getEquipo() {
        return equipo;
    }

    public String getEquipoString() {
        if (equipo != null && equipo.getTipo() != null && equipo.getMarca() != null) {
            return equipo.getTipo().concat(" " + equipo.getModelo());

        } else {
            return "";
        }
    }

    public void setEquipo(EquipoBean equipo) {
        this.equipo = equipo;
    }

    public VlanBean getVlan() {
        return vlan;
    }

    public void setVlan(VlanBean vlan) {
        this.vlan = vlan;
    }

}
