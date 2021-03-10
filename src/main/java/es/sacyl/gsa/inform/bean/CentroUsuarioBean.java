package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class CentroUsuarioBean extends MasterBean {

    private CentroBean centro;
    private UsuarioBean usuario;
    private String cargo;
    private String comentario;

    public CentroUsuarioBean() {
        super();
    }

    public CentroBean getCentro() {
        return centro;
    }

    public void setCentro(CentroBean centro) {
        this.centro = centro;
    }

    public UsuarioBean getUsuario() {
        return usuario;
    }

    public String getUsuarioDni() {
        if (usuario != null && !usuario.getDni().isEmpty()) {
            return usuario.getDni();
        } else {
            return "";
        }
    }

    public String getUsuarioNombre() {
        if (usuario != null) {
            return usuario.getApellidosNombre();
        } else {
            return "";
        }
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
