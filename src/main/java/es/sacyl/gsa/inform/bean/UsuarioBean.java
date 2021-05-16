package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author JuanNieto
 */
public class UsuarioBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private GfhBean gfh;

    private String comentario;
    private String solicita;
    private String movilUsuario;
    private String correoPrivadoUsuario;
    private String telegram;
    private Set<String> funcionalidadStrings = new HashSet<String>();
    private Map<String, FuncionalidadBean> fucionalidadesMap = new HashMap<>();

//    private ArrayList<FuncionalidadBean> fucionalidadesArrayList = new ArrayList<FuncionalidadBean>();
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni != null) {
            this.dni = dni.trim();
        } else {
            this.dni = dni;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreAp1_Ap2_() {

        return getNombreAp1_() + "." + apellido2.substring(0, 1);
    }

    public String getNombreAp1_() {
        String nombres[] = nombre.split(" ");

        return nombres[0] + "." + apellido1.substring(0, 1);
    }

    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre.trim();
        } else {
            this.nombre = nombre;
        }
    }

    public String getApellido1() {

        return apellido1;
    }

    public String getApellido2() {

        return apellido2;
    }

    public void setApellido1(String apellido1) {
        if (apellido1 != null) {
            this.apellido1 = apellido1.trim();
        }
        this.apellido1 = apellido1;
    }

    public void setApellido2(String apellido2) {
        if (apellido2 != null) {
            this.apellido2 = apellido2.trim();
        }
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
        if (mail != null) {
            this.mail = mail.trim();
        } else {
            this.mail = mail;
        }
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono != null) {
            this.telefono = telefono.trim();
        } else {
            this.telefono = telefono;
        }
    }

    public Long getIdCategoria() {
        Long idCategoria = new Long(0);
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
        if (movilUsuario != null) {
            this.movilUsuario = movilUsuario.trim();
        } else {
            this.movilUsuario = movilUsuario;
        }
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

    public Map<String, FuncionalidadBean> getFucionalidadesMap() {
        return fucionalidadesMap;
    }

    public void setFucionalidadesMap(Map<String, FuncionalidadBean> fucionalidadesMap) {
        this.fucionalidadesMap = fucionalidadesMap;
    }

    /**
     *
     * @param usuario
     * @param fun
     * @return Compara el nombre del menu con el SET de funcionalidades que
     * guarda en textomenu
     */
    public Boolean tieneLaFuncionalidad(String fun) {
        Boolean laTiene = false;
        for (String funcionaliad : getFuncionalidadStrings()) {
            if (funcionaliad.trim().toUpperCase().equals(fun.trim().toUpperCase())) {
                laTiene = true;
            }
        }
        return laTiene;
    }

    public String toHtml(String separador) {

        String cadena = "<b>";
        cadena = cadena.concat("Dni:  ");
        if (dni != null) {
            cadena = cadena.concat(dni);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat(separador);

        cadena = cadena.concat("Apellidos:  ");
        cadena = cadena.concat(getApellidosNombre());
        cadena = cadena.concat(separador);

        cadena = cadena.concat("Telefonos:  ");
        if (telefono != null) {
            cadena = cadena.concat(telefono);
        } else {
            cadena = cadena.concat(" ");
        }
        if (movilUsuario != null) {
            cadena = cadena.concat(movilUsuario);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat(separador);
        cadena = cadena.concat("Correo:  ");
        if (mail != null) {
            cadena = cadena.concat(mail);
        } else {
            cadena = cadena.concat(" ");
        }
        cadena = cadena.concat(separador);

        cadena = cadena.concat("Categoria:  ");
        if (categoria != null) {
            cadena = cadena.concat(categoria.getNombre());
        } else {
            cadena = cadena.concat(" ");
        }

        cadena = cadena.concat("</b>");
        return cadena;
    }
}
