package es.sacyl.gsa.inform.bean;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;

/**
 *
 * @author 06551256M En esta clase se definen atributos comunes a muchas clase
 * bean
 */
public abstract class MasterBean {

    protected Long id;
    protected Integer estado;
    protected LocalDate fechacambio;
    protected UsuarioBean usucambio;

    public MasterBean() {
        this.id = new Long(0);
        this.setValoresAut();
        this.estado = ConexionDao.BBDD_ACTIVOSI;
        this.fechacambio = LocalDate.now();
        this.usucambio = ((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public LocalDate getFechacambio() {
        return fechacambio;
    }

    public void setFechacambio(LocalDate fechacambio) {
        this.fechacambio = fechacambio;
    }

    public UsuarioBean getUsucambio() {
        return usucambio;
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

    public String getEstadoString() {
        if (estado == null) {
            return "";
        } else {
            if (estado == 0) {
                return "N";
            } else {
                return "S";
            }
        }
    }

    public Boolean getEstadoBoolena() {
        if (estado == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setEstado(String estado) {
        if (estado != null && estado.equals("S")) {
            this.estado = 1;
        } else {
            this.estado = 0;
        }
    }

    public void setEstado(Boolean estado) {
        if (estado != null && estado == true) {
            this.estado = 1;
        } else {
            this.estado = 0;
        }
    }

    public void setValoresAut() {
        this.setFechacambio(LocalDate.now());
        this.setUsucambio(((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME)));
    }
}
