package es.sacyl.gsa.inform.ui.lopd;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdSujetoBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.PacienteBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.ctrl.MensajesCtrl;
import es.sacyl.gsa.inform.ctrl.SesionCtrl;
import es.sacyl.gsa.inform.dao.LopdIncidenciaDao;
import es.sacyl.gsa.inform.dao.LopdTipoDao;
import es.sacyl.gsa.inform.dao.PacienteDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author juannietopajares
 */
public final class FrmLopdIncidenciaNueva extends FrmMasterPantalla {

    private TextField dni = new ObjetosComunes().getDni();
    private TextField apellidosNombre = new ObjetosComunes().getTextField("Solicitante", "", 50, "200px", "50px");
    private TextField mail = new ObjetosComunes().getMail();
    private TextField telefono = new ObjetosComunes().getTelefono();

    private ComboBox<LopdSujetoBean> comboSujeto = new CombosUi().getLopdSujetoCombo(null);
    private ComboBox<LopdTipoBean> comboTiposIncidencia = new CombosUi().getLopdTipoCombo(null, null, null);

    private final TextField id = new TextField("Id");
    private final Checkbox perdidaDatos = new Checkbox("Pérdida datos");

    private final TextField numerohc = new ObjetosComunes().getNumeroHc();
    private final TextField pacienteApellidos = new ObjetosComunes().getTextField("Paciente", null, 50, "200px", "90px");
    private final DateTimePicker fechaHoraDocumento = new ObjetosComunes().getDateTimePicker("Fecha Hora Documento", null, LocalDateTime.now());
    private final ComboBox<GfhBean> comboServicio = new ObjetosComunes().getServicioCombo(null, false);
    private final TextField descriDocu = new ObjetosComunes().getTextField("Descripción del documento", "", 100, "100px", "60px");

    private final TextArea descripcionError = new ObjetosComunes().getTextArea("Descripción detallada del error", null, 2000, "200px", "100px", "80px", "80px");

    private UsuarioBean usuRegistraBean = new UsuarioBean();
    private UsuarioBean usuarioCambio = new UsuarioBean();
    private PacienteBean paciente = new PacienteBean();

    private final Binder<UsuarioBean> usuRegistraBinder = new Binder<>();
    private final Binder<PacienteBean> pacienteBinder = new Binder<>();
    private final Binder<LopdIncidenciaBean> lopdIncidenciaBinder = new Binder<>();

    private LopdIncidenciaBean lopdIncidenciaBean = new LopdIncidenciaBean();
    private final Grid<LopdIncidenciaBean> lopdIncidenciaGrid = new Grid<>();

    public FrmLopdIncidenciaNueva(LopdIncidenciaBean incidenciaParam) {
        super();
        this.titulo.setText("Nueva incidencia LOPD");
        this.lopdIncidenciaBean = incidenciaParam;
        this.usuarioCambio = SesionCtrl.getSesionUsuario();
        doComponentesOrganizacion();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doGrid();
        lopdIncidenciaBinder.readBean(lopdIncidenciaBean);
        usuRegistraBinder.readBean(lopdIncidenciaBean.getUsuarioRegistra());
        pacienteBinder.readBean(lopdIncidenciaBean.getPaciente());
    }

    public FrmLopdIncidenciaNueva() {
    }

    @Override
    public void doGrabar() {
        try {
            if (lopdIncidenciaBinder.writeBeanIfValid(lopdIncidenciaBean)) {
                lopdIncidenciaBean.setValoresAut();
                lopdIncidenciaBean.setUsuCambio((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
                pacienteBinder.readBean(paciente);
                usuRegistraBinder.writeBean(usuRegistraBean);
                lopdIncidenciaBean.setUsuarioRegistra(usuRegistraBean);
                lopdIncidenciaBean.setPaciente(paciente);
                if (new LopdIncidenciaDao().grabaDatos(lopdIncidenciaBean) == true) {
                    (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                    new MensajesCtrl().doNuevoMensajeIncidencia(lopdIncidenciaBean);
                    doActualizaGrid();
                    doLimpiar();
                    doCancelar();
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
        } catch (ValidationException ex) {
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO);
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
                    lopdIncidenciaBean.setValoresAut();
                    // lopdIncidenciaBean.setUsuCambio(usuarioCambio);
                    //lopdIncidenciaBean.setFechaCambio(LocalDate.now());
                    //lopdIncidenciaBean.setEstado(Boolean.FALSE);
                    new LopdIncidenciaDao().grabaDatos(lopdIncidenciaBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                });
        dialog.open();
        doActualizaGrid();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    /**
     * Inicializo lo beans Lectura de binder desde beans
     */
    public void doLimpiar() {
        //
        lopdIncidenciaBean = new LopdIncidenciaBean();
        usuarioCambio = new UsuarioBean();
        usuRegistraBean = new UsuarioBean();
        paciente = new PacienteBean();
        //
        lopdIncidenciaBinder.readBean(lopdIncidenciaBean);
        pacienteBinder.readBean(paciente);
        usuRegistraBinder.readBean(usuRegistraBean);
    }

    @Override
    public void doGrid() {
        lopdIncidenciaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        lopdIncidenciaGrid.setPageSize(14);
        lopdIncidenciaGrid.addColumn(LopdIncidenciaBean::getFechaHoraString).setAutoWidth(true).setHeader(new Html("<b>Fecha</b>"));
        // grid.addColumn(LopdIncidenciaBean::getResuelta).setAutoWidth(true).setHeader(new Html("<b>Resuelta</b>"));
        lopdIncidenciaGrid.addColumn(new ComponentRenderer<>(lopdIncidencia -> {
            if (lopdIncidencia.getResuelta() == Boolean.TRUE) {
                return new Icon(VaadinIcon.CHECK);
            } else {
                return new Icon(VaadinIcon.CLOSE);
            }
        })).setHeader("Soluc.");
        lopdIncidenciaGrid.addColumn(LopdIncidenciaBean::getPacienteNumerohc).setAutoWidth(true).setHeader(new Html("<b>Nhc</b>"));
        lopdIncidenciaGrid.addColumn(LopdIncidenciaBean::getDescripcionErrorCorto).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        ArrayList<LopdIncidenciaBean> listaIncidencias = new ArrayList<>();
        if (buscador.getValue() != null && !buscador.getValue().isEmpty()) {
            if (Utilidades.isNumero(buscador.getValue())) {
                listaIncidencias = new LopdIncidenciaDao().getListaInicidencias(LocalDate.now().minusDays(30), LocalDate.now(), null, usuRegistraBean, null, null, buscador.getValue());
            } else {
                listaIncidencias = new LopdIncidenciaDao().getListaInicidencias(LocalDate.now().minusDays(3), LocalDate.now(), null, usuRegistraBean, null, buscador.getValue(), null);
            }
        } else {
            listaIncidencias = new LopdIncidenciaDao().getListaInicidencias(LocalDate.now().minusDays(30), LocalDate.now(), null, usuRegistraBean, null, null, null);
        }
        if (listaIncidencias != null && listaIncidencias.size() > 0) {
            lopdIncidenciaGrid.setItems(listaIncidencias);
        }
    }

    @Override
    public void doBinderPropiedades() {

        usuRegistraBinder.forField(dni)
                .asRequired()
                .withNullRepresentation("")
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);
        usuRegistraBinder.forField(apellidosNombre)
                .withNullRepresentation("")
                .bind(UsuarioBean::getApellidosNombre, null);
        usuRegistraBinder.forField(mail)
                .withNullRepresentation("")
                .asRequired()
                .bind(UsuarioBean::getMail, UsuarioBean::setMail);
        usuRegistraBinder.forField(telefono)
                .withNullRepresentation("")
                .asRequired()
                .bind(UsuarioBean::getTelefono, UsuarioBean::setTelefono);

// datos del paciente afectado
        pacienteBinder.forField(numerohc)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 7))
                .bind(PacienteBean::getNumerohc, PacienteBean::setNumerohc);

        pacienteBinder.forField(pacienteApellidos)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 7))
                .bind(PacienteBean::getApellidosnombre, PacienteBean::setApellidosnombre);

        // Datos de la incidencia que no se cambian
        lopdIncidenciaBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(LopdIncidenciaBean::getId, LopdIncidenciaBean::setId);

        lopdIncidenciaBinder.forField(comboTiposIncidencia)
                .asRequired()
                .bind(LopdIncidenciaBean::getTipo, LopdIncidenciaBean::setTipo);

        lopdIncidenciaBinder.forField(fechaHoraDocumento)
                .bind(LopdIncidenciaBean::getFechaHoraDocumento, LopdIncidenciaBean::setFechaHoraDocumento);

        lopdIncidenciaBinder.forField(descriDocu)
                .asRequired()
                .bind(LopdIncidenciaBean::getDescriDocu, LopdIncidenciaBean::setDescriDocu);

        lopdIncidenciaBinder.forField(perdidaDatos)
                .withNullRepresentation(false)
                .bind(LopdIncidenciaBean::getPerdidaDatos, LopdIncidenciaBean::setPerdidaDatos);

        lopdIncidenciaBinder.forField(comboServicio)
                .asRequired()
                .bind(LopdIncidenciaBean::getServicio, LopdIncidenciaBean::setServicio);

        lopdIncidenciaBinder.forField(this.descripcionError)
                .asRequired()
                .bind(LopdIncidenciaBean::getDescripcionError, LopdIncidenciaBean::setDescripcionError);

    }

    @Override
    /**
     * Asinga los atributos individuales a cada componente
     */
    public void doComponenesAtributos() {
        /**
         * Inicialmente se pone el cursos en el campo DNI
         */
        dni.focus();

        /**
         * Por defecto el tipo de sujeto es el paciente
         */
        comboSujeto.setValue(LopdSujetoBean.SUJETO_PACIENTE);
        /**
         *
         */
        doActualizaComboTiposIncidencia(LopdSujetoBean.SUJETO_PACIENTE);
        buscador.setLabel("Valor a busar");
    }

    /**
     *
     * @param sujeto
     *
     * Actualiza la lista de incidencias en funcion del sujeto. El usuario que
     * hace el registro ve sus incidecias y su estado
     */
    public void doActualizaComboTiposIncidencia(LopdSujetoBean sujeto) {
        comboTiposIncidencia.setItems(new LopdTipoDao().getListaIncidenciaTipos(sujeto, null, null, Boolean.TRUE));
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

        //   contenedorIzquierda.removeAll();
        //  contenedorIzquierda.add(contenedorBotones, contenedorFormulario);
        contenedorBotones.add(botonImprimir);
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
        contenedorFormulario.add(comboSujeto, 2);
        contenedorFormulario.add(comboTiposIncidencia, 4);
        /**
         * Fila 3
         */
        contenedorFormulario.add(fechaHoraDocumento, 3);
        contenedorFormulario.add(comboServicio, 3);
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
         * Frame de la derecha
         */
        contenedorDerecha.removeAll();
        contenedorBuscadores.removeAll();
        contenedorBuscadores.add(buscador);
        contenedorDerecha.add(contenedorBuscadores, lopdIncidenciaGrid);
    }

    /**
     * Cuando rellena el dni recupera de la bbdd si existe actualiza el grid
     */
    public void doSaltaDni() {

        dni.setValue(dni.getValue().toUpperCase());
        usuRegistraBean = new UsuarioDao().getUsuarioDni(dni.getValue().trim(), Boolean.FALSE);
        if (usuRegistraBean != null) {
            usuRegistraBinder.readBean(usuRegistraBean);
            doActualizaGrid();
        } else {
            if (!dni.getValue().isEmpty()) {
                Notification.show(FrmMensajes.USUARIONOEXISTE + dni.getValue());
                dni.focus();
            }
        }
    }

    public void docuentadni() {
        if (dni.getValue().length() == 9) {
            doSaltaDni();
        }
    }

    @Override
    public void doCompentesEventos() {

        // Cuando cambia el valor den campo comprueba el nº de caracteres. Si ha llegado a 9 salta automáticamente
        dni.addValueChangeListener(event -> {
            //   if (event.getValue().length() == 9) {
            docuentadni();
            // }
            //  Notification.show(Integer.toHexString(dni.getValue().length()));
        }
        );

        dni.addBlurListener(event -> doSaltaDni());
        /**
         * Cuando selecciona una incidencia la recupera en el formulario
         */
        lopdIncidenciaGrid.addItemClickListener(event
                -> {
            lopdIncidenciaBean = event.getItem();
            usuRegistraBean = lopdIncidenciaBean.getUsuarioRegistra();
            paciente = lopdIncidenciaBean.getPaciente();
            lopdIncidenciaBinder.readBean(event.getItem());
            usuRegistraBinder.readBean(usuRegistraBean);
            pacienteBinder.readBean(paciente);
            doControlBotones(lopdIncidenciaBean);
            /**
             * Si la incidencia esta resuleta no se puede modificar
             */
            if (lopdIncidenciaBean.getResuelta() == true) {
                botonGrabar.setEnabled(false);
            }
        }
        );

        /**
         * Cambio de valor en el comobo de sujeto
         */
        comboSujeto.addValueChangeListener(event
                -> {
            doActualizaComboTiposIncidencia(event.getValue());
        }
        );
        /**
         * Caundo rellena el número de historia
         */
        numerohc.addBlurListener(e
                -> {
            if (!numerohc.isEmpty()) {
                paciente = new PacienteDao().getPacienteNhc(numerohc.getValue());
                if (paciente == null) {
                    Notification.show(FrmMensajes.PACIENTENOEXISTENHC + numerohc.getValue());
                    numerohc.clear();
                    numerohc.focus();
                } else {
                    pacienteBinder.readBean(paciente);
                    descriDocu.focus();
                }
            }
        }
        );

        buscador.addValueChangeListener(e
                -> doActualizaGrid());

    }

    @Override
    public void doImprimir() {
    }

    public TextField getDni() {
        return dni;
    }

    public void setDni(TextField dni) {
        this.dni = dni;
    }

    public TextField getApellidosNombre() {
        return apellidosNombre;
    }

    public void setApellidosNombre(TextField apellidosNombre) {
        this.apellidosNombre = apellidosNombre;
    }

    public TextField getMail() {
        return mail;
    }

    public void setMail(TextField mail) {
        this.mail = mail;
    }

    public TextField getTelefono() {
        return telefono;
    }

    public void setTelefono(TextField telefono) {
        this.telefono = telefono;
    }

    public ComboBox<LopdSujetoBean> getComboSujeto() {
        return comboSujeto;
    }

    public void setComboSujeto(ComboBox<LopdSujetoBean> comboSujeto) {
        this.comboSujeto = comboSujeto;
    }

    public ComboBox<LopdTipoBean> getComboTiposIncidencia() {
        return comboTiposIncidencia;
    }

    public void setComboTiposIncidencia(ComboBox<LopdTipoBean> comboTiposIncidencia) {
        this.comboTiposIncidencia = comboTiposIncidencia;
    }

}
