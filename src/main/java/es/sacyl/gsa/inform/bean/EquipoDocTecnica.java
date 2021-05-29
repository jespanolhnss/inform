package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class EquipoDocTecnica extends DatoGenericoBean {

    private String tipoEquipo;
    private String marca;
    private String modelo;

    public EquipoDocTecnica() {
        super();
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
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

}
