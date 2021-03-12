package es.sacyl.gsa.inform.ui.lopd;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.LopdDocumentoBean;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdNotaBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.PacienteBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.LopdDocumentoDao;
import es.sacyl.gsa.inform.dao.LopdIncidenciaDao;
import es.sacyl.gsa.inform.dao.LopdNotaDao;
import es.sacyl.gsa.inform.reports.lopd.IncidenciaPdf;
import es.sacyl.gsa.inform.reports.lopd.IncidenciaPdfCompleta;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.ui.VentanaPdf;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.MandaMail;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author JuanNieto
 */
public final class FrmLopdIncidenciaGestionar extends FrmMasterPantalla implements Serializable {

    private static final long serialVersionUID = 1L;

    // componentes de la fila buscador del grid
    private final DatePicker desde = new ObjetosComunes().getDatePicker("Desde", null, LocalDate.now());
    private final DatePicker hasta = new ObjetosComunes().getDatePicker("Hasta", null, LocalDate.now());
    private final ComboBox comboPendiente = new CombosUi().getStringCombo("Pendientes?", "S", ObjetosComunes.SINOB, "100px");
// segunda fila de botones del formulario
    private final HorizontalLayout contendor2Botones = new HorizontalLayout();
    private final Button botonNota = new ObjetosComunes().getBoton("Nota.", null, VaadinIcon.PENCIL.create());
    private final Button botonInforme = new ObjetosComunes().getBoton("Infor.", null, VaadinIcon.FILE.create());
    private final Button botonJimenaInforme = new ObjetosComunes().getBoton("JInfor", null, VaadinIcon.RECORDS.create());
    private final Button botonJimenaRegistro = new ObjetosComunes().getBoton("JRegis", null, VaadinIcon.RECORDS.create());
    private final Button botonJimenaIntereconsulta = new ObjetosComunes().getBoton("JInterco", null, VaadinIcon.RECORDS.create());
    private final Button botonHis = new ObjetosComunes().getBoton("Registro", null, VaadinIcon.BED.create());

    private final TextField idusuario = new ObjetosComunes().getTextField("IdUsuario", "", 9, "30px", "30px");
    private final TextField dni = new ObjetosComunes().getDni();
    private final TextField apellidosNombre = new ObjetosComunes().getTextField("Solicitante", "", 50, "200px", "80px");
    private final TextField mail = new ObjetosComunes().getTextField("Correo", "", 50, "50px", "50px");
    private final TextField telefono = new ObjetosComunes().getTextField("Teléfono", "", 50, "50px", "50px");

    private final ComboBox<LopdTipoBean> comboTiposIncidencia = new CombosUi().getLopdTipoCombo(null, null, null);

    private final TextField id = new TextField("Id");
    private final DateTimePicker fechaHora = new ObjetosComunes().getDateTimePicker("Fecha Hora", null, null);
    private final Checkbox perdidaDatos = new Checkbox("Pérdida datos");

    private final TextField numerohc = new ObjetosComunes().getNumeroHc();
    private final TextField pacienteApellidos = new ObjetosComunes().getTextField("Paciente", "", 50, "100px", "150px");
    private final TextField idDocumento = new ObjetosComunes().getTextField("IdDocu", "", 10, "20px", "20px");
    private final DateTimePicker fechaHoraDocumento = new DateTimePicker("Fecha Hora");
    private final ComboBox<GfhBean> comboServicio = new ObjetosComunes().getServicioCombo(null, false);
    private final TextField descriDocu = new TextField("Descripción documento");

    private final TextArea descripcionError = new TextArea("Desripción detallada del error");
    private final TextArea descripcionSolucion = new TextArea("Desripción detallada de la solución");
    private final Checkbox resuelta = new Checkbox("Resuelta");
    private final DatePicker fechaSolucion = new ObjetosComunes().getDatePicker("Solución", null, null);

    private final TextField tecnicodni = new ObjetosComunes().getDni();
    private final TextField tecnicoapellidosNombre = new ObjetosComunes().getTextField("Técnico", "", 50, "200px", "80px");

    private UsuarioBean usuRegistraBean = new UsuarioBean();
    private PacienteBean pacienteBean = new PacienteBean();

    private final Binder<UsuarioBean> usuarioRegistraBinder = new Binder<>();
    private final Binder<PacienteBean> pacienteBinder = new Binder<>();
    private final Binder<LopdIncidenciaBean> lopdIncidenciaBinder = new Binder<>();

    private LopdIncidenciaBean lopdIncidenciaBean = new LopdIncidenciaBean();
    private ArrayList<LopdIncidenciaBean> lopdIncidenciaLista = new ArrayList<>();
    //   private final Grid<LopdIncidenciaBean> lopdIncidenciaGrid = new Grid<>();
    private final PaginatedGrid<LopdIncidenciaBean> lopdIncidenciaGrid = new PaginatedGrid<>();
    private final PaginatedGrid<LopdNotaBean> lopdNotaGrid = new PaginatedGrid<>();
    private final PaginatedGrid<LopdDocumentoBean> lopdDocumentoGrid = new PaginatedGrid<>();

    //   private final Grid<LopdNotaBean> lopdNotaGrid = new Grid<>();
    public FrmLopdIncidenciaGestionar() {
        super();
        doComponentesOrganizacion();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doGrid();
        doGridNotas();
        doGridDocumentos();
        doControlBotones();
        // actualiza los datos de GRID
        desde.clear();
    }

    @Override
    public void doGrabar() {
        if (lopdIncidenciaBinder.writeBeanIfValid(lopdIncidenciaBean)) {
            lopdIncidenciaBean.setUsuCambio((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
            if (new LopdIncidenciaDao().actualizaRespuesta(lopdIncidenciaBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                if (lopdIncidenciaBean.getResuelta() == Boolean.TRUE) {
                    new MandaMail().sendEmail(lopdIncidenciaBean.getUsuarioRegistra().getMail(),
                            LopdIncidenciaBean.MAIL_ASUNTO_RESUELTA,
                            LopdIncidenciaBean.MAIL_CONTENIDO_CABECERA + lopdIncidenciaBean.getHtmlContenidoSolicitud() + "\n\n"
                            + lopdIncidenciaBean.getHtmlContenidoSolución());
                }
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<LopdIncidenciaBean> validate = lopdIncidenciaBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }

    }

    @Override
    public void doCancelar() {
        this.removeAll();
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new LopdIncidenciaDao().doBorraDatos(lopdIncidenciaBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();

    }

    @Override
    public void doAyuda() {

    }

    @Override
    public void doLimpiar() {
        pacienteBean = new PacienteBean();
        lopdIncidenciaBean = new LopdIncidenciaBean();
        usuRegistraBean = new UsuarioBean();
        usuarioRegistraBinder.readBean(usuRegistraBean);
        pacienteBinder.readBean(pacienteBean);
        lopdIncidenciaBinder.readBean(lopdIncidenciaBean);
        doControlBotones();
        doActualizaGridNotas();
        doActualizaGriDocumentos();
    }

    @Override
    /**
     * Define las columnas del grid de incidencias
     */
    public void doGrid() {
        lopdIncidenciaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        lopdIncidenciaGrid.setHeightByRows(true);
        //   lopdIncidenciaGrid.setPageSize(14);
        lopdIncidenciaGrid.addColumn(LopdIncidenciaBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        lopdIncidenciaGrid.addColumn(LopdIncidenciaBean::getFechaHora).setAutoWidth(true).setHeader(new Html("<b>Fecha</b>"));

        lopdIncidenciaGrid.addColumn(new ComponentRenderer<>(lopdIncidenciaBean -> {
            if (lopdIncidenciaBean.getResuelta() == Boolean.TRUE) {
                return new Icon(VaadinIcon.CHECK);
            } else {
                return new Icon(VaadinIcon.QUESTION);
            }
        })).setHeader("Resuelta");
        lopdIncidenciaGrid.addColumn(LopdIncidenciaBean::getDescripcionErrorCorto).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));

        lopdIncidenciaGrid.setPageSize(5);
        lopdIncidenciaGrid.setPaginatorSize(5);
    }

    public void doGridNotas() {
        lopdNotaGrid.setHeightByRows(true);
        lopdNotaGrid.addColumn(LopdNotaBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id.</b>"));
        lopdNotaGrid.addColumn(LopdNotaBean::getFechaHoraFormato).setAutoWidth(true).setHeader(new Html("<b>Fecha</b>"));
        lopdNotaGrid.addColumn(LopdNotaBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        lopdNotaGrid.setPageSize(5);
        lopdNotaGrid.setPaginatorSize(5);
    }

    public void doGridDocumentos() {
        lopdDocumentoGrid.setHeightByRows(true);
        lopdDocumentoGrid.addColumn(LopdDocumentoBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id.</b>"));
        lopdDocumentoGrid.addColumn(LopdDocumentoBean::getFechaHoraFormato).setAutoWidth(true).setHeader(new Html("<b>Fecha</b>"));
        lopdDocumentoGrid.addColumn(new ComponentRenderer<>(LopdDocumentoBean -> {

            return new Icon(VaadinIcon.FILE_TEXT);

        })).setHeader("Doc");
        lopdDocumentoGrid.setPageSize(5);
        lopdDocumentoGrid.setPaginatorSize(5);
    }

    public void doActualizaGridNotas() {
        ArrayList<LopdNotaBean> listaNostasIncidencia = new LopdNotaDao().getNostasIncidencia(lopdIncidenciaBean);
        lopdNotaGrid.setItems(listaNostasIncidencia);
    }

    public void doActualizaGriDocumentos() {
        ArrayList<LopdDocumentoBean> listaDocumentos = new LopdDocumentoDao().getListaDocumentos(lopdIncidenciaBean);
        lopdDocumentoGrid.setItems(listaDocumentos);
    }

    @Override
    public void doActualizaGrid() {
        String pendiente = null;
        String textoAbusar = null;
        if (comboPendiente != null && comboPendiente.getValue() != null) {
            pendiente = (String) comboPendiente.getValue();
        }
        if (buscador != null && buscador.getValue().isEmpty()) {
            textoAbusar = buscador.getValue();
        }
        this.lopdIncidenciaLista = new LopdIncidenciaDao().getListaInicidencias(desde.getValue(), hasta.getValue(), null, null, pendiente, textoAbusar, null);
        if (this.lopdIncidenciaLista.size() > 14) {
            lopdIncidenciaGrid.setPageSize(14);
        } else if (this.lopdIncidenciaLista.size() > 0) {
            lopdIncidenciaGrid.setPageSize(lopdIncidenciaLista.size());
        }
        lopdIncidenciaGrid.setHeightByRows(true);
        lopdIncidenciaGrid.setItems(lopdIncidenciaLista);
    }

    @Override
    public void doBinderPropiedades() {
        // UsuarioBean que ha registrado la incidencia. Estos datos sólo se recuperan no se cambian
        usuarioRegistraBinder.forField(idusuario)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(UsuarioBean::getId, null);
        usuarioRegistraBinder.forField(dni)
                .withNullRepresentation("")
                .bind(UsuarioBean::getDni, null);
        usuarioRegistraBinder.forField(apellidosNombre)
                .withNullRepresentation("")
                .bind(UsuarioBean::getApellidosNombre, null);

        usuarioRegistraBinder.forField(mail)
                .withNullRepresentation("")
                .bind(UsuarioBean::getMail, null);
        usuarioRegistraBinder.forField(telefono)
                .withNullRepresentation("")
                .bind(UsuarioBean::getTelefono, null);

        lopdIncidenciaBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(LopdIncidenciaBean::getId, null);

        lopdIncidenciaBinder.forField(comboTiposIncidencia)
                .bind(LopdIncidenciaBean::getTipo, null);

// datos del paciente afectado
        pacienteBinder.forField(numerohc)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 7))
                .bind(PacienteBean::getNumerohc, null);

        pacienteBinder.forField(pacienteApellidos)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 7))
                .bind(PacienteBean::getApellidosnombre, null);

        // Datos de la incidencia que no se cambian
        lopdIncidenciaBinder.forField(fechaHora)
                // .withNullRepresentation(new LocalDateTime())
                .bind(LopdIncidenciaBean::getFechaHora, null);

        lopdIncidenciaBinder.forField(perdidaDatos)
                .withNullRepresentation(false)
                .bind(LopdIncidenciaBean::getPerdidaDatos, null);

        lopdIncidenciaBinder.forField(fechaHoraDocumento)
                .bind(LopdIncidenciaBean::getFechaHoraDocumento, null);

        lopdIncidenciaBinder.forField(comboServicio)
                .bind(LopdIncidenciaBean::getServicio, null);

        lopdIncidenciaBinder.forField(this.perdidaDatos)
                .bind(LopdIncidenciaBean::getPerdidaDatos, null);

        lopdIncidenciaBinder.forField(this.descripcionError)
                .bind(LopdIncidenciaBean::getDescripcionError, null);

        // Datos de la incidencia que no se cambian
        lopdIncidenciaBinder.forField(this.descripcionSolucion)
                .bind(LopdIncidenciaBean::getDescripcionSolucion, LopdIncidenciaBean::setDescripcionSolucion);

        lopdIncidenciaBinder.forField(this.resuelta)
                .bind(LopdIncidenciaBean::getResuelta, LopdIncidenciaBean::setResuelta);

        lopdIncidenciaBinder.forField(this.fechaSolucion)
                .bind(LopdIncidenciaBean::getFechaSolucion, LopdIncidenciaBean::setFechaSolucion);

        lopdIncidenciaBinder.forField(tecnicodni)
                .bind(LopdIncidenciaBean::getUsuCambioDni, null);
        lopdIncidenciaBinder.forField(tecnicoapellidosNombre)
                .bind(LopdIncidenciaBean::getUsuCambioApellidos, null);
    }

    @Override
    public void doComponenesAtributos() {
        this.buscador.setLabel("Texto a buscar");

        comboServicio.setWidth("30px");
        comboServicio.setEnabled(true);

        tecnicodni.setReadOnly(true);
        tecnicodni.setReadOnly(true);

    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4),
                new FormLayout.ResponsiveStep("50px", 5),
                new FormLayout.ResponsiveStep("50px", 6));

        this.contenedorIzquierda.removeAll();
        this.contenedorIzquierda.add(contenedorBotones, contendor2Botones, contenedorFormulario);
        this.contenedorBotones.add(botonImprimir);
        this.contendor2Botones.add(botonNota, botonInforme, botonJimenaInforme, botonJimenaIntereconsulta, botonJimenaRegistro, botonHis);
        // Elementos del buscador y grid
        this.contenedorDerecha.removeAll();
        this.contenedorBuscadores.add(buscador, desde, hasta, comboPendiente);
        this.contenedorDerecha.add(this.contenedorBuscadores, new Label("Listado de incidencias "), this.lopdIncidenciaGrid,
                new Label("Notas de la incidencia "), lopdNotaGrid, new Label("Documentos de la incidencia"), lopdDocumentoGrid);

        /**
         * Campos del formulario Fila 1
         */
        contenedorFormulario.add(dni);
        contenedorFormulario.add(apellidosNombre, 2);
        contenedorFormulario.add(mail, 2);
        contenedorFormulario.add(telefono);
        /**
         * Fila 2
         */
        contenedorFormulario.add(id, 2);
        contenedorFormulario.add(comboTiposIncidencia, 4);
        /**
         * Fila 3
         */
        contenedorFormulario.add(fechaHoraDocumento, 2);
        contenedorFormulario.add(comboServicio, 4);
        /**
         * Fila 4
         */
        contenedorFormulario.add(numerohc);
        contenedorFormulario.add(pacienteApellidos, 5);
        /**
         * Fila 5
         */
        contenedorFormulario.add(descriDocu, 4);
        contenedorFormulario.add(perdidaDatos, 2);
        /**
         * Fila 6
         */
        contenedorFormulario.add(descripcionError, 6);
        /**
         * Fila 6
         */
        contenedorFormulario.add(descripcionSolucion, 4);
        contenedorFormulario.add(resuelta, 1);
        contenedorFormulario.add(fechaSolucion, 1);
        /**
         * Fila 7
         */
        contenedorFormulario.add(tecnicodni, 2);
        contenedorFormulario.add(tecnicoapellidosNombre, 4);

    }

    public void doControlBotones() {

        if (lopdIncidenciaBean != null && lopdIncidenciaBean.getId() != null && lopdIncidenciaBean.getId() != 0) {
            super.doControlBotones(lopdIncidenciaBean);
            botonInforme.setEnabled(true);
            botonNota.setEnabled(true);
            lopdNotaGrid.setVisible(Boolean.TRUE);
            botonJimenaIntereconsulta.setEnabled(true);
            lopdDocumentoGrid.setVisible(Boolean.TRUE);
            if (lopdIncidenciaBean.getPaciente() != null && !lopdIncidenciaBean.getPaciente().getNumerohc().isEmpty()) {
                botonJimenaInforme.setEnabled(true);
                botonJimenaRegistro.setEnabled(true);

                botonHis.setEnabled(true);

            } else {
                botonJimenaInforme.setEnabled(false);
                botonJimenaRegistro.setEnabled(false);
                botonHis.setEnabled(false);

            }
        } else {
            super.doControlBotones(null);
            botonInforme.setEnabled(false);
            botonNota.setEnabled(false);
            botonJimenaInforme.setEnabled(false);
            botonJimenaRegistro.setEnabled(false);
            botonJimenaIntereconsulta.setEnabled(false);
            botonHis.setEnabled(false);
            lopdNotaGrid.setVisible(Boolean.FALSE);
            lopdDocumentoGrid.setVisible(Boolean.FALSE);
        }
    }

    @Override
    public void doCompentesEventos() {
        /**
         * Evento de selección de incidencia en el lopdIncidenciaGridde
         * incidencias recupera beans incidencia, usuario registra y paceinte
         * activa botones
         */
        lopdIncidenciaGrid.addItemClickListener(event -> {
            lopdIncidenciaBean = event.getItem();
            usuRegistraBean = lopdIncidenciaBean.getUsuarioRegistra();
            pacienteBean = lopdIncidenciaBean.getPaciente();
            lopdIncidenciaBinder.readBean(event.getItem());
            usuarioRegistraBinder.readBean(usuRegistraBean);
            pacienteBinder.readBean(pacienteBean);
            this.doControlBotones();
            doActualizaGridNotas();
            doActualizaGriDocumentos();
        }
        );

        /**
         * Si hace clic en el lopdIncidenciaGridde notas llama al método
         * doVentanaNota con el valor de la nota seleccionada
         */
        lopdNotaGrid.addItemClickListener(event -> {
            doVentanaNota(event.getItem());
        }
        );

        /**
         * Si hace clic en el lopdDocumentoGrid muestra el contenido del pdf
         */
        lopdDocumentoGrid.addItemClickListener(event -> {
            LopdDocumentoBean lopdDocumentoBean = event.getItem();
            LopdDocumentoDao lopdDocumentoDAO = new LopdDocumentoDao();
            lopdDocumentoDAO.getGeneraFilePdf(lopdDocumentoBean);

            Page page = new Page(getUI().get());
            String url = lopdDocumentoBean.getUrlFile();
            page.open(url, "_blank");

        }
        );

        /**
         * Caundo ambia e valor en el combo de filtro de incidencias del grid.
         * actualiza la lista de incidencias del grid
         */
        comboPendiente.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        /**
         * Cada vez que escribe algo en el campo texto lanza una búsqueda de
         * lista de incidencias para ctualizar el grid
         */
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        desde.addValueChangeListener(e -> doActualizaGrid());
        hasta.addValueChangeListener(e -> doActualizaGrid());
        /**
         * Si marca el check resuelta, actualiza la fecha de solución a la fecha
         * actual
         */

        resuelta.addClickListener(event -> {
            fechaSolucion.setValue(LocalDate.now());
        });
        /**
         * Añadir nota. Si hace clic en el botón añadir nota crea una nueva
         * ventana con una nota en blano y la incidencia
         */
        botonNota.addClickListener(event -> {
            LopdNotaBean lopdNotaBean = new LopdNotaBean();
            lopdNotaBean.setFecha(LocalDate.now());
            doVentanaNota(new LopdNotaBean());
        });

        /**
         * botonInforme. Opción de imprimir informe detallado de la incidencia
         * con la notas
         */
        botonInforme.addClickListener(event -> {
            IncidenciaPdfCompleta incidenciaPdfCompleta = new IncidenciaPdfCompleta(lopdIncidenciaBean);
            incidenciaPdfCompleta.doCreaFicheroPdf();
            VentanaPdf ventanaPdf = new VentanaPdf(incidenciaPdfCompleta.getUrlDelPdf());
            ventanaPdf.addDialogCloseActionListener(event1 -> {
                Utilidades.borraFichero(incidenciaPdfCompleta.getNombrePdfAbsoluto());
            });
        });
        /**
         * Click en boton de borrar informe de jimena
         */
        botonJimenaInforme.addClickListener(event -> {
            FrmJimenaBorraInf frmJimenaBorraInf = new FrmJimenaBorraInf(lopdIncidenciaBean);
            frmJimenaBorraInf.open();
            frmJimenaBorraInf.addDialogCloseActionListener(e -> {
                if (frmJimenaBorraInf.getBorradoConfirmado() == Boolean.TRUE) {
                    doActualizaGridNotas();
                    doActualizaGriDocumentos();
                    descripcionSolucion.setValue(" Ejecutado proceso de borrado lógico en base de datos");
                    fechaSolucion.setValue(LocalDate.now());
                    resuelta.setValue(Boolean.TRUE);
                    doGrabar();
                }
            });

            frmJimenaBorraInf.addDetachListener(e -> {
                if (frmJimenaBorraInf.getBorradoConfirmado() == Boolean.TRUE) {
                    doActualizaGridNotas();
                    doActualizaGriDocumentos();
                    descripcionSolucion.setValue(" Ejecutado proceso de borrado lógico en base de datos");
                    fechaSolucion.setValue(LocalDate.now());
                    resuelta.setValue(Boolean.TRUE);
                    doGrabar();
                }
            });
        });

        botonJimenaIntereconsulta.addClickListener(event -> {
            FrmInterconsultasBorrar frmInterconsultasBorrar = new FrmInterconsultasBorrar(lopdIncidenciaBean);
            frmInterconsultasBorrar.open();
            frmInterconsultasBorrar.addDialogCloseActionListener(e -> {
                if (frmInterconsultasBorrar.getBorradoConfirmado() == Boolean.TRUE) {
                    doActualizaGridNotas();
                    doActualizaGriDocumentos();
                    descripcionSolucion.setValue(" Ejecutado proceso de borrado lógico en base de datos");
                    fechaSolucion.setValue(LocalDate.now());
                    resuelta.setValue(Boolean.TRUE);
                    doGrabar();
                }
            });
        });

    }

    /**
     *
     * @param notaParam el valor de la nota que se carga en el formulario de la
     * ventana nueva. Cuado cierre la ventana actualiza el lopdIncidenciaGridde
     * notas por si se ha añadido o borrado una nota
     *
     */
    public void doVentanaNota(LopdNotaBean notaParam) {
        FrmLopdNota frmIncidenciasNotas = new FrmLopdNota("300px", notaParam, lopdIncidenciaBean);
        frmIncidenciasNotas.addDialogCloseActionListener(e -> {
            doActualizaGridNotas();
        });
        frmIncidenciasNotas.addDetachListener(e -> {
            doActualizaGridNotas();
        });
        frmIncidenciasNotas.open();

    }

    @Override
    public void doImprimir() {
        IncidenciaPdf incidenciaPdf = new IncidenciaPdf(lopdIncidenciaBean);
        incidenciaPdf.doCreaFicheroPdf();
        Page page = new Page(getUI().get());
        page.open(incidenciaPdf.getUrlDelPdf(), "_blank");
    }
}
