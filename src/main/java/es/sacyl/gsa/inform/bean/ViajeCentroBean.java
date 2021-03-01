package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.util.Objects;

public class ViajeCentroBean implements Serializable {
 

    private Long id;
    private Long idViaje;
    
    private CentroBean centroDestino;
    private String preparacion;
    private String actuacion;

	
	 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

    public Long getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Long idViaje) {
        this.idViaje = idViaje;
    }
	 
	 
	public String getPreparacion() {
		return preparacion;
	}
	public void setPreparacion(String preparacion) {
		this.preparacion = preparacion;
	}
	public String getActuacion() {
		return actuacion;
	}
	public void setActuacion(String actuacion) {
		this.actuacion = actuacion;
	}

    public CentroBean getCentroDestino() {
        return centroDestino;
    }

    public void setCentroDestino(CentroBean centroDestino) {
        this.centroDestino = centroDestino;
    }
    
    public String getNombreCentro() {
        String nombre;
        
        if (Objects.isNull(centroDestino)) {
            nombre = "s/n";
        } else {
            nombre = centroDestino.getNomcen();
        }
        return nombre;
    }
}