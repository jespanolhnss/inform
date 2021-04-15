package es.sacyl.gsa.inform.bean;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LopdIncidenciaBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private UsuarioBean usuarioRegistra;
    private LopdSujetoBean sujeto;
    private LopdTipoBean tipo;
    private LocalDateTime fechaHora;
    private PacienteBean paciente;
    private String idDocumento;
    private LocalDateTime fechaHoraDocumento;
    private GfhBean servicio;
    private String descriDocu;
    private Boolean perdidaDatos;
    private String descripcionError;
    private String descripcionSolucion;

    private UsuarioBean usuCambio;
    private UsuarioBean usuarioAfectado;
    private Boolean resuelta;
    private LocalDate fechaSolucion;

    private ArrayList<File> documentosAsociados = new ArrayList<>();

    public static String MAIL_ASUNTO_NUEVA = "Nueva incidencia relacionada con la seguiridad de los datos protegidos por  LOPD. ";
    public static String MAIL_ASUNTO_RESUELTA = "Resolución de incidencia de seguiridad  ";
    public static String MAIL_CONTENIDO_CABECERA = "Registro de una nueva incidencia de LOPD: \n"
            + "----------------------------------------------- \n ";

    public static String MAIL_CONTENIDO_PIE = "----------------------------------------------- \n"
            + "Desde el servicio de informática te mantendremos informado sobre el proceso de solución: \n" + "\n";

    public LopdIncidenciaBean() {
        this.id = new Long(0);
        this.resuelta = false;
        this.paciente = new PacienteBean();
        this.sujeto = LopdSujetoBean.SUJETO_PACIENTE;
    }

    public LopdIncidenciaBean(LocalDateTime fechaHora) {
        this.id = new Long(0);
        this.fechaHora = fechaHora;
        this.resuelta = false;
        this.paciente = new PacienteBean();
        this.sujeto = LopdSujetoBean.SUJETO_PACIENTE;

    }

    public Long getId() {
        return id;
    }

    public String getIdString() {
        if (id != null) {
            return Long.toHexString(id);
        } else {
            return "";
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioBean getUsuarioRegistra() {
        return usuarioRegistra;
    }

    public void setUsuarioRegistra(UsuarioBean usuarioRegistra) {
        this.usuarioRegistra = usuarioRegistra;
    }

    public LopdSujetoBean getSujeto() {
        return sujeto;
    }

    public void setSujeto(LopdSujetoBean sujeto) {
        this.sujeto = sujeto;
    }

    public LopdTipoBean getTipo() {
        return tipo;
    }

    public String getTipoDescripcion() {
        if (tipo != null) {
            return tipo.getDescripcion();
        } else {
            return "";
        }
    }

    public void setTipo(LopdTipoBean tipo) {
        this.tipo = tipo;
    }

    public void setTipo(String valor) {
        this.tipo.setDescripcion(valor);
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getFechaHoraString() {
        DateTimeFormatter fechaformato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        if (this.fechaHora != null) {
            return fechaformato.format(this.fechaHora);
        } else {
            return "";
        }
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public String getPacienteNumerohc() {
        if (paciente != null) {
            return paciente.getNumerohc();
        } else {
            return "";
        }
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public void setPacienteNumerohc(String numerohc) {
        if (paciente != null) {
            this.paciente.setNumerohc(numerohc);
        }

    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public LocalDateTime getFechaHoraDocumento() {

        return fechaHoraDocumento;
    }

    public void setFechaHoraDocumento(LocalDateTime fechaHoraDocumento) {
        this.fechaHoraDocumento = fechaHoraDocumento;
    }

    public GfhBean getServicio() {
        return servicio;
    }

    public void setServicio(GfhBean servicio) {
        this.servicio = servicio;
    }

    public String getPacienteApellidos() {
        if (paciente != null) {
            return paciente.getApellidosnombre();
        } else {
            return "";
        }
    }

    public void setPacienteApellidos(String apellidosnombre) {
        if (paciente != null) {
            paciente.setApellidosnombre(apellidosnombre);
        }
    }

    public String getDescriDocu() {
        return descriDocu;
    }

    public void setDescriDocu(String descriDocu) {
        this.descriDocu = descriDocu;
    }

    public Boolean getPerdidaDatos() {
        return perdidaDatos;
    }

    public void setPerdidaDatos(Boolean perdidaDatos) {
        if (perdidaDatos == null) {
            this.perdidaDatos = false;
        } else {
            this.perdidaDatos = perdidaDatos;
        }
    }

    public String getPerdidaDatosString() {
        if (perdidaDatos == null) {
            return "";
        } else {
            if (perdidaDatos == false) {
                return "N";
            } else {
                return "S";
            }
        }
    }

    public void setPerdidaDatos(String valor) {
        if (valor != null && valor.equals("S")) {
            this.perdidaDatos = true;
        } else {
            this.perdidaDatos = false;
        }
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public String getDescripcionErrorCorto() {

        if (descripcionError != null && descripcionError.length() > 50) {
            return descripcionError.substring(0, 49) + "....";
        } else {
            return descripcionError;
        }
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public String getDescripcionSolucion() {
        return descripcionSolucion;
    }

    public void setDescripcionSolucion(String descripcionSolucion) {
        this.descripcionSolucion = descripcionSolucion;
    }

    public UsuarioBean getUsuCambio() {
        return usuCambio;
    }

    public String getUsuCambioDni() {
        if (usuCambio != null) {
            return usuCambio.getDni();
        } else {
            return null;
        }
    }

    public String getUsuCambioApellidos() {
        if (usuCambio != null) {
            return usuCambio.getApellidosNombre();
        } else {
            return null;
        }
    }

    public void setUsuCambio(UsuarioBean usuCambio) {
        this.usuCambio = usuCambio;
    }

    public Boolean getResuelta() {
        return resuelta;
    }

    public String getResueltaString() {
        if (resuelta == true) {
            return "S";
        } else {
            return "N";
        }
    }

    public void setResuelta(Boolean resuelta) {
        this.resuelta = resuelta;
    }

    public LocalDate getFechaSolucion() {
        return fechaSolucion;
    }

    public String getFechaSolucionString() {
        DateTimeFormatter fechadma = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        if (this.getFechaSolucion() != null) {
            return fechadma.format(this.fechaSolucion);
        } else {
            return "";
        }
    }

    public void setFechaSolucion(LocalDate fechaSolucion) {
        this.fechaSolucion = fechaSolucion;
    }

    public ArrayList<File> getDocumentosAsociados() {
        return documentosAsociados;
    }

    public void setDocumentosAsociados(ArrayList<File> documentosAsociados) {
        this.documentosAsociados = documentosAsociados;
    }

    public UsuarioBean getUsuarioAfectado() {
        return usuarioAfectado;
    }

    public void setUsuarioAfectado(UsuarioBean usuarioAfectado) {
        this.usuarioAfectado = usuarioAfectado;
    }

    public String getHtmlContenidoSolicitud() {
        DateTimeFormatter fechaFormato = DateTimeFormatter.ofPattern("dd/mm/YYYY hh:mm");
        String textHtml = "<b>Número:</b>" + this.getId() + "\n";
        textHtml = textHtml.concat("<b>Fecha hora: </b>" + fechaFormato.format(this.getFechaHora()) + " \n");
        textHtml = textHtml.concat("<b>Tipo: </b> " + this.getTipo().getDescripcion() + "<br> \n");
        if (this.getPaciente() != null && this.getPaciente().getNumerohc() != null
                && !this.getPaciente().getNumerohc().isEmpty()) {
            textHtml = textHtml.concat("<b>Númerohc:  </b>" + this.getPaciente().getNumerohc() + " \n");
            textHtml = textHtml.concat("<b>Paciente: </b> " + this.getPaciente().getApellidosnombre() + " <br> \n");
            if (this.getFechaHoraDocumento() != null) {
                textHtml = textHtml.concat("<b>Documento fecha: </b> " + fechaFormato.format(this.getFechaHoraDocumento()) + " \n");
            } else {
                textHtml = textHtml.concat("<b>Documento fecha: </b> sin fecha \n");
            }
            textHtml = textHtml.concat("<b>Documento: </b> " + this.getDescriDocu() + " <br> \n");
        } else {

        }
        textHtml = textHtml.concat("<b>Descripción error: </b> " + this.getDescripcionError() + "<hr>");
        return textHtml;
    }

    public String getHtmlContenidoSolución() {
        String textHtml = "Fecha hora: " + this.getFechaSolucion() + " \n";
        textHtml = textHtml.concat("Solución: " + this.getDescripcionSolucion() + " \n");
        textHtml = textHtml.concat("Técnico: " + this.getUsuCambio().getApellidosNombre() + " \n");

        return textHtml;
    }

    public String getAyudaHtml() {
        String ayudaHtml = " <b>Descripción de los campos de la incidencia:\n  </b> " + "<ul> "
                + "<li><b>Dni:</b> Dni del  profesional que indica la incidencia.</li>"
                + "<li><b>Usuario:</b>Apellidos y nombre.</li>"
                + "<li><b>Correo:</b>El correo electrónico del usuario.</li>"
                + "<li><b>Tipo:</b>Clasificación de la incidencia según los siguientes criterios.</li>"
                + "<ul> "
                + "<li><b>Paciente:Datos de identificación incorrectos:</b>Cuando algún dato de filiación del paciente no es correcto.</li>"
                + "<li><b>Paciente:Informe no corresponde:</b>El informe no corresponde a ese paciente.</li>"
                + "<li><b>Paciente:Contenido de informe erróneo:</b>Algún dato del contenido del informe no es correcto.</li>"
                + "<li><b>Paciente:Arquetipo no corresponde a paciente:</b>El paciente tiene un registro o arquetipo que no es suyo, no le corresponde.</li>"
                + "<li><b>Paciente:Arquetipo contenido erróneo:</b>En un registro o arquetipo algún dato no es correcto.</li>"
                + "<li><b>Paciente:Resolución Gerente:</b>Resolución de Gerencia para modificar un dato.</li>"
                + "<li><b>Usuario: Incidencia de usuario:</b>Olvio de contraseña, acceso no reconocido u otras incicencias relacionadas con un usuario.</li>"
                + "<li><b>Trabajador: Datos del trabajador:</b>Incidencias relacionadas con datos de los trabajadores.</li>"
                + "<li><b>Incidencias con datos impresos o en soportes:</b>Incidencias relativas a documentos impresos, historias en papel o datos personales en otros soportes infomáticos.</li>"
                + "<li><b>Otra incidencia de seguridad:</b>Incidencias de alimentación eléctrica, fallos en las comunicaciones,.</li>"
                + "</ul>" + "<li><b>Fecha y hora:</b>Del registro de la incidencia.</li>"
                + "<li><b>Datos para incidencias de pacientes</b></li>"
                + "<ul> "
                + "<li><b>NHC:</b> Número de historia clínica</li>"
                + "<li><b>idDocumento:</b> El número de identificación del documento o del informe afectado</li>"
                + "<li><b>Fecha docu:</b> Fecha del documento afectado</li>"
                + "<li><b>Hora docu:</b> Hora del documento afectado</li>"
                + "<li><b>Servicio:</b> Servicio al que pertenece el documento afectado.</li>"
                + "<li><b>Paciente:</b> Apellidos y nombre del paciente.</li>"
                + "<li><b>Descripción del documento:</b> La descripción del documento afectado.</li>"
                + "</ul>"
                + "<li><b>Desripción detallada del error:</b> La descripción detallada del error que permite a los técnicos hacer la corrección de forma segura.</li>"
                + "<li><b>Botones de acción</b> </li>" + "<ul> "
                + "<li><b>Grabar. </b>Almacena los datos de forma permanente.</li>"
                + "<li><b>Adjuntar documento. </b>Permite añadir un documento PDF que aclaratorio de la indicencia</li>"
                + "<li><b>Ayuda. </b>Esta pantalla.</li>" + "</ul>"
                + "</ul>";

        return ayudaHtml;
    }
}
