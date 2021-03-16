package es.sacyl.gsa.inform.bean;

import es.sacyl.gsa.inform.dao.JimenaDao;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.File;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class Informe. *
 *
 * @author Juan Nieto
 * @version 23.5.2018
 */
public class JimenaInformeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numeroOrden;

    private Long id;

    private String descripcion;

    private PacienteBean paciente;

    private Long episodio;

    private CentroBean centro;

    private GfhBean servicio;

    private String referecia;

    private LocalDate fecha;

    private Long hora;

    private int estado;

    private Blob docuxml;

    private int tipoxml;

    private Blob docubin;

    private int tipobin;

    private Long peticion;

    private UsuarioBean userid;

    private Long canal;

    private int tipoinforme;

    private UsuarioBean useridauth;

    private GfhBean srvauth;

    private UsuarioBean useridredactor;

    private Long plantalla_editor;

    private int flag;

    private Long pertenece;

    private int version;

    private int nive_visibilidad;

    private Long subservicio;

    private UsuarioBean useridpeticionario;

    private int visto;

    private Long ultimoguardado;

    private int bloqueado;

    private Long almacenamiento;

    private Long tipo_documento;

    private Long ambito;

    private GfhBean servicio_realizador;

    private LocalDateTime fecha_proceso;

    private String referencia_almacenamiento;

    private int num_accesos;

    private UsuarioBean user_visto;

    private Long fecha_visto;

    private String comentario_visto;

    private File ficheroInformeFile;

    private String urlFilePdf;
    private String nombrePdf;

    private ArrayList<JimenaCampos_iBean> listaCampos = new ArrayList<JimenaCampos_iBean>();

    private Long interconsultaid;
    private GfhBean interconsultaServicioDestino;
    private CentroBean interconsultaCentroDestino;

    public final static int INFORME_ESTADO_EDICION = 1;

    public final static int INFORME_ESTADO_CONSOLIDADO = 2;

    public final static int INFORME_ESTADO_SUSTITUIDO = 5;

    public final static int CANAL_DEFECTO = 6;

    public final static int ORDENFECHA = 1;

    public final static int ORDENFECHADESC = 2;

    protected DateTimeFormatter fechadma = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    protected DateTimeFormatter fechadmahhmm = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm");

    private static final Logger logger = LogManager.getLogger(JimenaInformeBean.class);

    /**
     * Instantiates a new informe.
     */
    public JimenaInformeBean() {
        this.id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDescripcion20() {
        if (descripcion != null && descripcion.length() > 20) {
            return descripcion.substring(0, 20);
        } else {
            return descripcion;
        }
    }

    public String getDescripcion12Char() {
        return descripcion.substring(12);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPacienteBeanNhc() {
        return paciente.getNumerohc();
    }

    public void setPacienteBean(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public Long getEpisodio() {
        return episodio;
    }

    public void setEpisodio(Long episodio) {
        this.episodio = episodio;
    }

    public CentroBean getCentro() {
        return centro;
    }

    public void setCentro(CentroBean centro) {
        this.centro = centro;
    }

    public GfhBean getServicioBean() {
        return servicio;
    }

    public String getServicioBeanCodigo() {
        if (servicio != null) {
            return servicio.getCodigo();
        } else {
            return "";
        }
    }

    public void setServicioBean(GfhBean servicio) {
        this.servicio = servicio;
    }

    public String getReferecia() {
        return referecia;
    }

    public void setReferecia(String referecia) {
        this.referecia = referecia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Blob getDocuxml() {
        return docuxml;
    }

    public void setDocuxml(Blob docuxml) {
        this.docuxml = docuxml;
    }

    public int getTipoxml() {
        return tipoxml;
    }

    public void setTipoxml(int tipoxml) {
        this.tipoxml = tipoxml;
    }

    public Blob getDocubin() {
        return docubin;
    }

    public void setDocubin(Blob docubin) {
        this.docubin = docubin;
    }

    public int getTipobin() {
        return tipobin;
    }

    public void setTipobin(int tipobin) {
        this.tipobin = tipobin;
    }

    public Long getPeticion() {
        return peticion;
    }

    public void setPeticion(Long peticion) {
        this.peticion = peticion;
    }

    public UsuarioBean getUserid() {
        return userid;
    }

    public void setUserid(UsuarioBean userid) {
        this.userid = userid;
    }

    public int getTipoinforme() {
        return tipoinforme;
    }

    public void setTipoinforme(int tipoinforme) {
        this.tipoinforme = tipoinforme;
    }

    public UsuarioBean getUseridauth() {
        return useridauth;
    }

    public void setUseridauth(UsuarioBean useridauth) {
        this.useridauth = useridauth;
    }

    public GfhBean getSrvauth() {
        return srvauth;
    }

    public void setSrvauth(GfhBean srvauth) {
        this.srvauth = srvauth;
    }

    public UsuarioBean getUseridredactor() {
        return useridredactor;
    }

    public void setUseridredactor(UsuarioBean useridredactor) {
        this.useridredactor = useridredactor;
    }

    public Long getPlantalla_editor() {
        return plantalla_editor;
    }

    public void setPlantalla_editor(Long plantalla_editor) {
        this.plantalla_editor = plantalla_editor;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Long getPertenece() {
        return pertenece;
    }

    public void setPertenece(Long pertenece) {
        this.pertenece = pertenece;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getNive_visibilidad() {
        return nive_visibilidad;
    }

    public void setNive_visibilidad(int nive_visibilidad) {
        this.nive_visibilidad = nive_visibilidad;
    }

    public Long getSubservicio() {
        return subservicio;
    }

    public void setSubservicio(Long subservicio) {
        this.subservicio = subservicio;
    }

    public UsuarioBean getUseridpeticionario() {
        return useridpeticionario;
    }

    public void setUseridpeticionario(UsuarioBean useridpeticionario) {
        this.useridpeticionario = useridpeticionario;
    }

    public int getVisto() {
        return visto;
    }

    public void setVisto(int visto) {
        this.visto = visto;
    }

    public Long getUltimoguardado() {
        return ultimoguardado;
    }

    public void setUltimoguardado(Long ultimoguardado) {
        this.ultimoguardado = ultimoguardado;
    }

    public int getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(int bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Long getAlmacenamiento() {
        return almacenamiento;
    }

    public void setAlmacenamiento(Long almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public Long getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(Long tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public Long getAmbito() {
        return ambito;
    }

    public void setAmbito(Long ambito) {
        this.ambito = ambito;
    }

    public GfhBean getServicioBean_realizador() {
        return servicio_realizador;
    }

    public void setServicioBean_realizador(GfhBean servicio_realizador) {
        this.servicio_realizador = servicio_realizador;
    }

    public LocalDateTime getFecha_proceso() {
        return fecha_proceso;
    }

    public void setFecha_proceso(LocalDateTime fecha_proceso) {
        this.fecha_proceso = fecha_proceso;
    }

    public String getReferencia_almacenamiento() {
        return referencia_almacenamiento;
    }

    public void setReferencia_almacenamiento(String referencia_almacenamiento) {
        this.referencia_almacenamiento = referencia_almacenamiento;
    }

    public int getNum_accesos() {
        return num_accesos;
    }

    public void setNum_accesos(int num_accesos) {
        this.num_accesos = num_accesos;
    }

    public UsuarioBean getUser_visto() {
        return user_visto;
    }

    public void setUser_visto(UsuarioBean user_visto) {
        this.user_visto = user_visto;
    }

    public Long getFecha_visto() {
        return fecha_visto;
    }

    public void setFecha_visto(Long fecha_visto) {
        this.fecha_visto = fecha_visto;
    }

    public String getComentario_Visto() {
        return comentario_visto;
    }

    public void setComentario_Visto(String comentario_visto) {
        this.comentario_visto = comentario_visto;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public File getFicheroInformeFile() {
        return ficheroInformeFile;
    }

    public void setFicheroInformeFile(File ficheroInformeFile) {
        this.ficheroInformeFile = ficheroInformeFile;
    }

    public Long getCanal() {
        return canal;
    }

    public void setCanal(Long canal) {
        this.canal = canal;
    }

    public ArrayList<JimenaCampos_iBean> getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(ArrayList<JimenaCampos_iBean> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public String getFechaHoraInforme() {
        String feString = "";
        if (fecha != null) {
            feString = fechadma.format(fecha);
            if (hora != null) {
                if (!hora.equals(new Long(0))) {
                    feString = feString + " " + Utilidades.getHoraHH_MM(hora);
                }
            }

        }
        return feString;
    }

    public String getFechaHoraProceso() {

        String feString = "";

        feString = fechadmahhmm.format(fecha_proceso);

        return feString;
    }

    public String getFechaHoraServcio() {
        String string = "";
        string = this.getFechaHoraInforme() + "  " + this.getServicioBean().getCodigo();
        return string;
    }

    public String getFechaHoraServcioDescrip() {
        String string = "";
        string = this.getFechaHoraInforme() + "  " + this.getServicioBean().getCodigo() + " " + this.getDescripcion20();

        return string;
    }

    public String getCodigoServicioBean() {
        return this.getServicioBean().getCodigo();
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public GfhBean getServicio() {
        return servicio;
    }

    public void setServicio(GfhBean servicio) {
        this.servicio = servicio;
    }

    public GfhBean getServicio_realizador() {
        return servicio_realizador;
    }

    public void setServicio_realizador(GfhBean servicio_realizador) {
        this.servicio_realizador = servicio_realizador;
    }

    public String getComentario_visto() {
        return comentario_visto;
    }

    public void setComentario_visto(String comentario_visto) {
        this.comentario_visto = comentario_visto;
    }

    public String getNombrePdf() {
        return nombrePdf;
    }

    public void setNombrePdf(String nombrePdf) {
        this.nombrePdf = nombrePdf;
    }

    public Long getInterconsultaid() {
        return interconsultaid;
    }

    public void setInterconsultaid(Long interconsultaid) {
        this.interconsultaid = interconsultaid;
    }

    public GfhBean getInterconsultaServicioDestino() {
        return interconsultaServicioDestino;
    }

    public String getInterconsultaServicioDestinoString() {
        if (interconsultaServicioDestino != null && interconsultaServicioDestino.getCodigo() != null) {
            return interconsultaServicioDestino.getCodigo();
        } else {
            return "";
        }
    }

    public void setInterconsultaServicioDestino(GfhBean interconsultaServicioDestino) {
        this.interconsultaServicioDestino = interconsultaServicioDestino;
    }

    public CentroBean getInterconsultaCentroDestino() {
        return interconsultaCentroDestino;
    }

    public void setInterconsultaCentroDestino(CentroBean interconsultaCentroDestino) {
        this.interconsultaCentroDestino = interconsultaCentroDestino;
    }

    public DateTimeFormatter getFechadmahhmm() {
        return fechadmahhmm;
    }

    public void setFechadmahhmm(DateTimeFormatter fechadmahhmm) {
        this.fechadmahhmm = fechadmahhmm;
    }

    public String getPathAbsolutePdf() {
        return Constantes.PDFPATHABSOLUTO + "inf_" + this.id + ".pdf";
    }

    public String getUrlFile() {
        return "http://localhost:8080" + Constantes.PDFPATHRELATIVO + "inf_" + this.id + ".pdf";
    }

    public void setUrlFilePdf(String urlFilePdf) {
        this.urlFilePdf = urlFilePdf;
    }

    public String getUsuarioBeanApellidosNombre() {
        if (this.getUserid() != null) {
            return this.getUserid().getApellidosNombre();
        } else {
            return "";
        }
    }

    public String getHtmlCabecera() {
        String html = "<b>";

        html = html.concat(" " + this.getFechaHoraInforme() + " ");

        if (this.servicio != null) {
            html = html.concat(this.getServicioBean().getCodigo() + " ");
        }
        html = html.concat("<hr>");
        if (this.getUserid() != null) {
            html = html.concat("Dr/a:" + this.getUserid().getApellidosNombre());
        }
        html = html.concat("<hr>");
        if (this.getPaciente() != null && this.getPaciente().getNumerohc() != null) {
            html = html.concat("Nhc:" + this.getPaciente().getNumerohc() + "&nbsp;PacienteBean:"
                    + this.getPaciente().getApellidosnombre() + "<hr>");
        }
        return html;
    }

    public String getTxtCabecera() {
        String html = "";

        html = html.concat(" " + this.getFechaHoraInforme() + " ");

        if (this.servicio != null) {
            html = html.concat(this.getServicioBean().getCodigo() + " ");
        }

        if (this.getUserid() != null) {
            html = html.concat("Dr/a:" + this.getUserid().getApellidosNombre());
        }
        html = html.concat("\n");
        if (this.getPaciente() != null) {
            html = html.concat("Nhc:" + this.getPaciente().getNumerohc() + "&nbsp;PacienteBean:"
                    + this.getPaciente().getApellidosnombre() + "\n");
        }
        return html;
    }

    public String getHtmlCampos_i() {
        String html = "";
        for (JimenaCampos_iBean campo : getListaCampos()) {
            if (campo.getDato() != null) {
                try {
                    if (campo.getDescripcion().length() > 5) {
                        if (!campo.getDescripcion().substring(0, 5).equals("DICOM")) {
                            int caracteres = (int) campo.getDato().length();
                            html = html.concat("<b>" + campo.getDescripcion() + ":</b>&nbsp;"
                                    + campo.getDato().getSubString(1, caracteres));
                            if (campo.getUnidades() != null && !campo.getUnidades().isEmpty()) {
                                html = html.concat("&nbsp;&nbsp;" + campo.getUnidades());
                            }
                            html = html.concat("<br>");
                        }
                    } else {
                        int caracteres = (int) campo.getDato().length();
                        html = html.concat("<b>" + campo.getDescripcion() + ":</b>&nbsp;"
                                + campo.getDato().getSubString(1, caracteres));
                        if (campo.getUnidades() != null && !campo.getUnidades().isEmpty()) {
                            html = html.concat("&nbsp;&nbsp;" + campo.getUnidades());
                        }
                        html = html.concat("<br>");

                    }
                } catch (SQLException e) {
                    logger.error("Error conversión campo CLOB ", e);
                }
            }
        }
        return html;
    }

    public String gettxtCampos_i() {
        String html = "<b>";
        for (JimenaCampos_iBean campo : getListaCampos()) {
            if (campo.getDato() != null) {
                try {
                    int caracteres = (int) campo.getDato().length();
                    if (campo.getDescripcion().length() > 5) {
                        if (!campo.getDescripcion().substring(0, 5).equals("DICOM")) {
                            html = html.concat(campo.getDescripcion() + ": "
                                    + campo.getDato().getSubString(1, caracteres));
                            if (campo.getUnidades() != null && !campo.getUnidades().isEmpty()) {
                                html = html.concat("  " + campo.getUnidades());
                            }
                            html = html.concat("<hr>");
                        }
                    } else {
                        html = html.concat(campo.getDescripcion() + ": "
                                + campo.getDato().getSubString(1, caracteres));
                        if (campo.getUnidades() != null && !campo.getUnidades().isEmpty()) {
                            html = html.concat("  " + campo.getUnidades());
                        }
                        html = html.concat("<hr>");
                    }
                } catch (SQLException e) {
                    logger.error("Error conversión campo CLOB ", e);
                }
            }
        }
        html = html.concat("</b>");
        return html;
    }

    /*
    public File getFilePdfInforme() {
        File file = null;
        try {
            FileOutputStream outpu = null;
            String pathname = this.getPathFilePdf();
            file = new File(pathname);
            outpu = new FileOutputStream(file);
            Blob archivo = new JimenaDAO().getBlobPdfInforme(id,null);
            InputStream inStream = archivo.getBinaryStream();
            int size = (int) archivo.length();
            byte[] buffer = new byte[size];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outpu.write(buffer, 0, length);
            }
            outpu.close();
            inStream.close();
        } catch (Exception ioe) {
            logger.error(ioe);
        }
        return file;
    }
     */
    public static JimenaInformeBean getInformeResulsetJimena(ResultSet rs, boolean conCampos_I, PacienteBean paciente, CentroBean centro,
            GfhBean servicio, UsuarioBean usuario, Integer estado) {
        JimenaInformeBean informe = new JimenaInformeBean();

        try {
            informe.setNumeroOrden(rs.getInt("numeroorden"));
            informe.setId(rs.getLong("id"));
            informe.setDescripcion(rs.getString("descripcion"));
            if (paciente == null) {
                // informe.setPacienteBean(new PacienteBeanDAO().getPacienteBeanPorId(rs.getLong("paciente"), false));
            } else {
                informe.setPacienteBean(paciente);
            }
            //  informe.setProblema(new Proceso(rs.getLong("problema")));
            informe.setEpisodio(rs.getLong("episodio"));
            if (centro == null) {
                //  informe.setCentro(new CentroDAO().getRegistroId(rs.getLong("centro")));
            } else {
                informe.setCentro(centro);
            }
            if (servicio == null) {
                //   informe.setServicioBean(new ServicioBeansDAO().getRegistroId(rs.getLong("servicio")));
            } else {
                informe.setServicioBean(servicio);
            }
            if (usuario != null) {
                informe.setUserid(usuario);
            }
            informe.setReferecia(rs.getString("referencia"));
            informe.setFecha(Utilidades.getFechaLocalDate(rs.getLong("fecha")));
            informe.setHora(rs.getLong("hora"));
            informe.setEstado(rs.getInt("estado"));
            // informe.setDocuxml(docuxml);
            informe.setTipoxml(rs.getInt("tipoxml"));
            // informe.setDocubin(docubin);
            informe.setTipobin(rs.getInt("tipobin"));
            informe.setPeticion(rs.getLong("peticion"));
            //    informe.setUserid(new UsuarioBeanDAO().getUsuarioBeanUserid(rs.getString("userid"), false));
            informe.setCanal(rs.getLong("canal"));
            informe.setTipoinforme(rs.getInt("tipoinforme"));
            informe.setUseridauth(new UsuarioBean(rs.getString("useridauth")));
            //     informe.setSrvauth(new GfhBean(rs.getLong("srvauth")));
            informe.setUseridredactor(new UsuarioBean(rs.getString("useridredactor")));

            informe.setPlantalla_editor(rs.getLong("plantilla_editor"));

            informe.setFlag(rs.getInt("flag"));
            informe.setPertenece(rs.getLong("pertenece"));
            informe.setVersion(rs.getInt("version"));
            informe.setNive_visibilidad(rs.getInt("nivel_visibilidad"));
            informe.setSubservicio(rs.getLong("subservicio"));
            informe.setUseridpeticionario(new UsuarioBean(rs.getString("useridpeticionario")));
            informe.setVisto(rs.getInt("visto"));
            informe.setUltimoguardado(rs.getLong("ultimoguardado"));
            informe.setBloqueado(rs.getInt("bloqueado"));
            informe.setAlmacenamiento(rs.getLong("almacenamiento"));
            informe.setTipo_documento(rs.getLong("tipo_documento"));
            informe.setAmbito(rs.getLong("ambito"));
            //     informe.setServicioBean_realizador(new GfhBean(rs.getLong("servicio_realizador")));
            informe.setFecha_proceso(Utilidades.getFechaLocalDateTime(rs.getLong("fecha_proceso")));
            informe.setReferencia_almacenamiento(rs.getString("referencia_almacenamiento"));
            informe.setNum_accesos(rs.getInt("num_accesos"));
            //     informe.setProblema(new Proceso(rs.getLong("problema")));
            informe.setUser_visto(new UsuarioBean(rs.getString("user_visto")));
            informe.setFecha_visto(rs.getLong("fecha_visto"));
            informe.setComentario_Visto(rs.getString("comentario_visto"));
            if (conCampos_I == true) {
                informe.setListaCampos(new JimenaDao().getListaCamposInforme(informe.getId(), estado));
            }
        } catch (SQLException e) {
            logger.error("Error resulet ", e);
        }
        return informe;
    }
}
