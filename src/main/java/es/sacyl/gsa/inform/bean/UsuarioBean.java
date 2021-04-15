package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author JuanNieto
 */
public class UsuarioBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dniSolicitante;
    private String nombreSolicitante;
    private String apellido1Solicitante;
    private String apellido2Solcitante;
    private String movilSolicitante;
    private String telefonoSolicitante;
    private LocalDate fechaSolicitud;
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String codigoFarmatools;
    private String servicioFarmatols;
    private String clave;
    private String mail;
    private String telefono;
    private CategoriaBean categoria;
<<<<<<< HEAD
    private GfhBean gfh;
    private String tipo;
    private String comentario;

=======
    private String solicita;
    private String movilUsuario;
    private String correoPrivadoUsuario;
    private String telegram;
>>>>>>> 8b9a5f7906000c7e95091622141998434c99725e
    private Set<String> funcionalidadStrings = new HashSet<String>();

    private ArrayList<FuncionalidadBean> fucionalidadesArrayList = new ArrayList<FuncionalidadBean>();

    public static String PASSWORD_DEFECTO = "murallas";
    public static int USUARIO_DEBAJA = 0;
    public static int USUARIO_ACTIVO = 1;
    public static int USUARIO_ADMINISTRADOR = 2;
    public static UsuarioBean USUARIO_SISTEMA = new UsuarioBean(new Long(1828), "06384936K", "SISTEMA", "SISTEMA", "SISTEMA");

    public UsuarioBean() {
        super();
    }

    public UsuarioBean(String dni) {
        super();
        this.dni = dni;
    }

    public UsuarioBean(Long id, String dni, String nombre, String apellido1, String apellido2) {
        super();
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public UsuarioBean(String dni, String nombre) {
        super();
        this.dni = dni;
        this.nombre = nombre;
    }

    public String getDniSolicitante() {
        return dniSolicitante;
    }

    public void setDniSolicitante(String dniSolicitante) {
        this.dniSolicitante = dniSolicitante;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getApellido1Solicitante() {
        return apellido1Solicitante;
    }

    public void setApellido1Solicitante(String apellido1Solicitante) {
        this.apellido1Solicitante = apellido1Solicitante;
    }

    public String getApellido2Solcitante() {
        return apellido2Solcitante;
    }

    public void setApellido2Solcitante(String apellido2Solcitante) {
        this.apellido2Solcitante = apellido2Solcitante;
    }

    public String getMovilSolicitante() {
        return movilSolicitante;
    }

    public void setMovilSolicitante(String movilSolicitante) {
        this.movilSolicitante = movilSolicitante;
    }

    public String getTelefonoSolicitante() {
        return telefonoSolicitante;
    }

    public void setTelefonoSolicitante(String telefonoSolicitante) {
        this.telefonoSolicitante = telefonoSolicitante;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getCodigoFarmatools() {
        return codigoFarmatools;
    }

    public void setCodigoFarmatools(String codigoFarmatools) {
        this.codigoFarmatools = codigoFarmatools;
    }

    public String getServicioFarmatols() {
        return servicioFarmatols;
    }

    public void setServicioFarmatols(String servicioFarmatols) {
        this.servicioFarmatols = servicioFarmatols;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Long getIdCategoria() {
        Long idCategoria = new Long(0)
                ;
        if (categoria != null) {
            idCategoria = categoria.id;
        }
        
        return idCategoria;
    }
    
    public String getNombreCategoria() {
        String nombreCategoria = "";
        
        if (categoria != null) {
            nombreCategoria = categoria.getNombre();
        }
        return nombreCategoria;
    }
    
    public CategoriaBean getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaBean categoria) {
        this.categoria = categoria;
    }
    
    public void setIdCategoria(Long categoria) {
        this.categoria.id = categoria;
    }
    
    public GfhBean getGfh() {
        return gfh;
    }
    
    public Long getIdGfh() {
        Long idGfh = new Long(0);
        
        if (gfh != null) {
            idGfh = gfh.getId();
        }
        
        return idGfh;
    }
    
    public String getNombreGfh() {
        String nombreGfh = "";
        
        if (gfh != null) {
            nombreGfh = gfh.getDescripcion();
        }
        return nombreGfh;
    }
    
    public void setGfh(GfhBean gfh) {
        this.gfh = gfh;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getComentario() {
        return comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    public UsuarioBean getUsucambio() {
        return usucambio;
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

    public String getApellidosNombre() {
        String nombreCompleto = "";
        if (apellido1 != null) {
            nombreCompleto = nombreCompleto.concat(apellido1 + " ");
        }
        if (apellido2 != null) {
            nombreCompleto = nombreCompleto.concat(apellido2 + " ");
        }
        if (nombre != null) {
            nombreCompleto = nombreCompleto.concat(nombre);
        }
        return nombreCompleto;
    }

    public Set<String> getFuncionalidadStrings() {
        return funcionalidadStrings;
    }

    public void setFuncionalidadStrings(Set<String> funcionalidadStrings) {
        this.funcionalidadStrings = funcionalidadStrings;
    }

    public ArrayList<FuncionalidadBean> getFucionalidadesArrayList() {
        return fucionalidadesArrayList;
    }

    public void setFucionalidadesArrayList(ArrayList<FuncionalidadBean> fucionalidadesArrayList) {
        this.fucionalidadesArrayList = fucionalidadesArrayList;
    }

    public static String getPASSWORD_DEFECTO() {
        return PASSWORD_DEFECTO;
    }

    public static void setPASSWORD_DEFECTO(String PASSWORD_DEFECTO) {
        UsuarioBean.PASSWORD_DEFECTO = PASSWORD_DEFECTO;
    }

    public static int getUSUARIO_DEBAJA() {
        return USUARIO_DEBAJA;
    }

    public static void setUSUARIO_DEBAJA(int USUARIO_DEBAJA) {
        UsuarioBean.USUARIO_DEBAJA = USUARIO_DEBAJA;
    }

    public static int getUSUARIO_ACTIVO() {
        return USUARIO_ACTIVO;
    }

    public static void setUSUARIO_ACTIVO(int USUARIO_ACTIVO) {
        UsuarioBean.USUARIO_ACTIVO = USUARIO_ACTIVO;
    }

    public static int getUSUARIO_ADMINISTRADOR() {
        return USUARIO_ADMINISTRADOR;
    }

    public static void setUSUARIO_ADMINISTRADOR(int USUARIO_ADMINISTRADOR) {
        UsuarioBean.USUARIO_ADMINISTRADOR = USUARIO_ADMINISTRADOR;
    }

    public CategoriaBean getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaBean categoria) {
        this.categoria = categoria;
    }

    public String getSolicita() {
        return solicita;
    }

    public void setSolicita(String solicita) {
        this.solicita = solicita;
    }

    public String getMovilUsuario() {
        return movilUsuario;
    }

    public void setMovilUsuario(String movilUsuario) {
        this.movilUsuario = movilUsuario;
    }

    public String getCorreoPrivadoUsuario() {
        return correoPrivadoUsuario;
    }

    public void setCorreoPrivadoUsuario(String correoPrivadoUsuario) {
        this.correoPrivadoUsuario = correoPrivadoUsuario;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

}
