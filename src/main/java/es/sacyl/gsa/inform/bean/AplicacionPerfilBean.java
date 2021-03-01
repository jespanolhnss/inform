package es.sacyl.gsa.inform.bean;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;

/**
 *
 * @author 06551256M
 */
public class AplicacionPerfilBean extends MasterBean {

    // id, nombre, aplicacion, categoria, codigo, comentario, estado,usucambio, fechacambio;
    private String nombre;
    private AplicacionBean aplicacion;
    private UsuarioCategoriaBean categoria;
    private String codigo;
    private String comentario;

    public AplicacionPerfilBean() {
        this.id = new Long(0);
        this.estado = ConexionDao.BBDD_ACTIVOSI;
        this.fechacambio = LocalDate.now();
        this.usucambio = ((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public AplicacionBean getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(AplicacionBean aplicacion) {
        this.aplicacion = aplicacion;
    }

    public UsuarioCategoriaBean getCategoria() {
        return categoria;
    }

    public void setCategoria(UsuarioCategoriaBean categoria) {
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
