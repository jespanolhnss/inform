package es.sacyl.gsa.inform.ui.lopd;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.JimenaInformeBean;
import es.sacyl.gsa.inform.bean.LopdDocumentoBean;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdNotaBean;
import es.sacyl.gsa.inform.bean.PacienteBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.JimenaDao;
import es.sacyl.gsa.inform.dao.LopdDocumentoDao;
import es.sacyl.gsa.inform.dao.LopdNotaDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.EmbeddedPdfDocument;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmJimenaBorraInf extends Dialog {

    private final HorizontalLayout contenedorPrincipal = new HorizontalLayout();

    private final VerticalLayout contenedorIzquierda = new VerticalLayout();
    private final HorizontalLayout contenedorBotones = new HorizontalLayout();
    private final VerticalLayout contenedorDerecha = new VerticalLayout();

    private final PacienteBean paciente;

    private LopdIncidenciaBean lopdIncidenciaBean = null;

    private final DatePicker fechaSeleccion = new ObjetosComunes().getDatePicker("Fecha desde", null, null);
    private final ComboBox<GfhBean> servicioCombo;
    protected final DateTimeFormatter fechadma = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    private ArrayList<JimenaInformeBean> listaInformes = new ArrayList<>();

    private final PaginatedGrid<JimenaInformeBean> jimenaInformeGrid = new PaginatedGrid<>();

    private final Button borrarInfomeBoton = new ObjetosComunes().getBoton("Borrar informe", null, VaadinIcon.CHECK.create());
    private final Button cancelarBoton = new ObjetosComunes().getBoton("Cancela", null, VaadinIcon.CLOSE_CIRCLE.create());
    private JimenaInformeBean informe = null;

    private Boolean borradoConfirmado = Boolean.FALSE;
    private UsuarioBean usuarioBean;

    public FrmJimenaBorraInf(LopdIncidenciaBean incidenciaParam) {
        this.lopdIncidenciaBean = incidenciaParam;
        this.paciente = lopdIncidenciaBean.getPaciente();
        usuarioBean = ((UsuarioBean) com.vaadin.flow.server.VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));

        /**
         * Son servciosBean de informática no de jimena. Tienen el atrinuto
         * idJimena
         */
        this.servicioCombo = new CombosUi().getServicioComboJimenaPaciente(paciente.getNumerohc());
        doComponentesOrganizacion();
        doCompentesEventos();
        doGrid();
        doActualziaGrid();

    }

    public void doCompentesEventos() {
        servicioCombo.addValueChangeListener(e -> doActualziaGrid());

        fechaSeleccion.addValueChangeListener(e -> doActualziaGrid());

        jimenaInformeGrid.addSelectionListener(e -> doSeleccionaInforme());

        borrarInfomeBoton.addClickListener(e -> doBorrar());
        borrarInfomeBoton.setEnabled(Boolean.FALSE);
        cancelarBoton.addClickListener(e -> doCancela());
    }

    public void doComponentesOrganizacion() {

        this.add(contenedorPrincipal);

        this.contenedorIzquierda.setMargin(false);

        this.contenedorDerecha.setMargin(false);

        this.contenedorBotones.setMargin(false);

        //  borrarInfomeBoton.setEnabled(false);
        contenedorBotones.add(fechaSeleccion, servicioCombo, borrarInfomeBoton, cancelarBoton);

        contenedorPrincipal.add(contenedorIzquierda, contenedorDerecha);

        contenedorIzquierda.add(contenedorBotones, jimenaInformeGrid);

    }

    public void doGrid() {
        jimenaInformeGrid.setWidth("600px");
        jimenaInformeGrid.setPage(10);
        jimenaInformeGrid.setPageSize(10);
        jimenaInformeGrid.addColumn(JimenaInformeBean::getFechaHoraInforme).setHeader("Fecha Hora Informe").setAutoWidth(true).setWidth("100px");
        jimenaInformeGrid.addColumn(JimenaInformeBean::getServicioBeanCodigo).setHeader("Serv").setWidth("60px").setAutoWidth(true);
        jimenaInformeGrid.addColumn(JimenaInformeBean::getDescripcion20).setHeader("Informe").setAutoWidth(true);
        jimenaInformeGrid.addColumn(JimenaInformeBean::getUsuarioBeanApellidosNombre).setHeader("Médico").setAutoWidth(true);
    }

    public void doCancela() {
        this.close();
    }

    public void doBorrar() {
        if (informe != null) {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    " Confirmas que quieres borrar el informe " + informe.getFechaHoraServcioDescrip(),
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        doProcesoDeBorrado(informe);
                    });
            dialog.open();

        }
    }

    public void doProcesoDeBorrado(JimenaInformeBean informe) {
        // inserta el pdf antes de borrar en la tabla lopd_documentos
        doInsertaPdf(informe);
        // borrado lógico de informe y campos
        String respuestaInforme = new JimenaDao().doUpdateInformeBorrado(informe.getId());
        String respuestaCampos = new JimenaDao().doUpdateCampos_iBorrado(informe.getId());
        // inserta una nota en la incidencia con la sentencia sql que se ha  ejecutado.
        doInsertaNota(respuestaInforme + "\n" + respuestaCampos);

        Notification.show(FrmMensajes.AVISODATOBORRADO);
        setBorradoConfirmado(Boolean.TRUE);
        doCancela();
    }

    /**
     *
     * @param informe Cuando hacer el borrado lógico en jimena si el informe
     * tiene pdf asociado hace una copia en lopd_dcoumentos asociado a la
     * incidencia
     */
    public void doInsertaPdf(JimenaInformeBean informe) {
        // sólo inserta si tiene pdf asociado en jimena
        if (informe.getTipobin() == 1) {
            LopdDocumentoBean documento = new LopdDocumentoBean();
            documento.setEstado(ConexionDao.BBDD_ACTIVOSI);
            documento.setFecha(LocalDate.now());
            documento.setFechaCambio(LocalDate.now());
            documento.setHora(Utilidades.getHoraActualInt());
            documento.setIdIncidenica(lopdIncidenciaBean);
            documento.setUsuCambio(usuarioBean);
            documento.setIfinformejimena(informe.getId());
            documento.setFicheroAdjunto(informe.getFicheroInformeFile());
            LopdDocumentoDao lopdDocumentoDAO = new LopdDocumentoDao();
            lopdDocumentoDAO.grabaDatos(documento);
        }
    }

    public void doInsertaNota(String texto) {
        LopdNotaBean nota = new LopdNotaBean();
        nota.setIdIncidenciaLong(lopdIncidenciaBean.getId());
        nota.setFecha(LocalDate.now());
        nota.setHora(Utilidades.getHoraActualInt());
        nota.setDescripcion(texto);
        nota.setUsucambio(usuarioBean);
        nota.setEstado(ConexionDao.BBDD_ACTIVOSI);
        new LopdNotaDao().grabaDatos(lopdIncidenciaBean, nota);

    }

    public Boolean getBorradoConfirmado() {
        return borradoConfirmado;
    }

    public void setBorradoConfirmado(Boolean borradoConfirmado) {
        this.borradoConfirmado = borradoConfirmado;
    }

    public void cerrarClick() {

        close();
    }

    public void doSeleccionaInforme() {
        jimenaInformeGrid.setEnabled(false);
        if (jimenaInformeGrid.getSelectedItems().size() > 0) {
            informe = jimenaInformeGrid.getSelectedItems().iterator().next();
            contenedorDerecha.removeAll();
            borrarInfomeBoton.setEnabled(Boolean.TRUE);
            if (informe.getTipobin() == 1) {
                new JimenaDao().getFicheroPdfInformeBean(informe);
                String url = informe.getUrlFile();
                if (url != null) {
                    contenedorDerecha.add(new EmbeddedPdfDocument(informe.getUrlFile(), "600px"));
                    borrarInfomeBoton.setEnabled(true);
                } else {
                }
            } else {
                informe.setListaCampos(new JimenaDao().getListaCamposInforme(informe.getId(), JimenaInformeBean.INFORME_ESTADO_EDICION));
                if (informe.getListaCampos().size() > 0) {
                    Details component = new Details(new Span(informe.getTxtCabecera()),
                            new Span(informe.gettxtCampos_i()));
                    component.setEnabled(false);
                    component.setOpened(true);
                    contenedorDerecha.setMaxHeight("200px");
                    contenedorDerecha.add(component);
                    borrarInfomeBoton.setEnabled(true);
                } else {
                }
            }

        } else {
            Notification.show("Sin seleccionar informe");
            borrarInfomeBoton.setEnabled(Boolean.FALSE);
        }
        jimenaInformeGrid.setEnabled(true);
    }

    public void doActualziaGrid() {
        listaInformes = new JimenaDao().getListaInformesPaciente(paciente, servicioCombo.getValue(), fechaSeleccion.getValue(), null, JimenaInformeBean.ORDENFECHADESC, JimenaInformeBean.INFORME_ESTADO_CONSOLIDADO);
        jimenaInformeGrid.setItems(listaInformes);
        contenedorDerecha.removeAll();
        borrarInfomeBoton.setEnabled(Boolean.FALSE);
    }
}
