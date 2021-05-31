package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author JuanNieto
 */
public class ComboBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String grupo;
    private String descripcion;
    private Integer orden;
    private Long pertenece;
    private String rama;
    private String valor;
    private final static String titulo = "Combos ";

    public final static String TIPOEQUIPOMARCA = "TIPOEQUIPOMARCA";
    public final static String TIPOEQUIPODATOS = "TIPOEQUIPODATOS";
    public final static String TIPOEQUIPOMARCAMODELO = "TIPOEQUIPOMARCAMODELO";

    public final static String EQUIPODOCTECNICOTIPO = "EQUIPODOCTECNICOTIPO";

    public final static String APPAMBITO = "APPAMBITO";
    public final static String APPGESTIONUSUARIOS = "APPGESTIONUSUARIOS";
    public final static String CARGOSCENTROS = "CARGOSCENTROS";

    public final static String TIPOAPLICACIONDATO = "TIPOAPLICACIONDATO";
    public final static String TIPOAPLICACIONDATOAPLICACION = "Aplicación";

    public final static String DWAREASINDICADORES = "DWAREASINDICADORES";
    public final static String DWTIPOINDICADOR = "DWTIPOINDICADOR";

    public final static String PROVEEDORDATOGENERICO = "PROVEEDORDATOGENERICO";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getPertenece() {
        return pertenece;
    }

    public void setPertenece(Long pertenece) {
        this.pertenece = pertenece;
    }

    public String getRama() {
        return rama;
    }

    public void setRama(String rama) {
        this.rama = rama;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public static String getTitulo() {
        return titulo;
    }

    @Override
    public String toString() {
        return "Combo{" + "id=" + id + ", grupo=" + grupo + ", descripcion=" + descripcion + ", orden=" + orden + ", pertenece=" + pertenece + ", rama=" + rama + ", valor=" + valor + '}';
    }

}
