package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author 06532775Q
 */
public class ViajeTecnicoBean implements Serializable {

    private Long id;
    private ViajeBean viaje;
    private UsuarioBean tecnico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ViajeBean getViaje() {
        return viaje;
    }

    public void setViaje(ViajeBean viaje) {
        this.viaje = viaje;
    }

    public UsuarioBean getTecnico() {
        return tecnico;
    }

    public void setTecnico(UsuarioBean tecnico) {
        this.tecnico = tecnico;
    }

    public String getTecnicoApellidos() {
        if (tecnico != null) {
            return tecnico.getApellidosNombre();
        } else {
            return "";
        }
    }
}
