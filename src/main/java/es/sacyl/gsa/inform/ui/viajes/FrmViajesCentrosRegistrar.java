package es.sacyl.gsa.inform.ui.viajes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.ViajeBean;
import es.sacyl.gsa.inform.bean.ViajeCentroBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.ViajesDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FrmViajesCentrosRegistrar extends FrmMasterVentana {

    private static final long serialVersionUID = 1L;

    /* Campos del formulario */
    TextField id = new ObjetosComunes().getTextField("id");
    TextField idViaje = new ObjetosComunes().getTextField("idViaje");
    ComboBox<ProvinciaBean> provinciasCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null,
            AutonomiaBean.AUTONOMIADEFECTO);
    ComboBox<CentroTipoBean> tipoCentroCombo = new CombosUi().getCentroTipoCombo(null);
    ComboBox<CentroBean> centroCombo = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO,
            ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);

    //TextArea preparacion = new ObjetosComunes().getTextArea("Preparación", "datos de la preparación del viaje", 100, "50px", "150px", "50px", "20px");
    TextArea preparacionTextArea = new TextArea("Preparación");
    //TextArea actuacion = new ObjetosComunes().getTextArea("Actuación", "registra el trabajo realizado", 100, "50px", "175px", "50px", "20px");
    TextArea actuacionTextArea = new TextArea("Actuación");
    Icon icon = new Icon(VaadinIcon.BUTTON);
    Button modificarButton = new ObjetosComunes().getBoton("Modificar", ButtonVariant.LUMO_PRIMARY, icon);

    /* Componentes */
    ViajeBean viajeBean = new ViajeBean();
    ViajeCentroBean viajeCentrosBean = new ViajeCentroBean();
    Binder<ViajeCentroBean> viajeCentrosBinder = new Binder<>();
    ArrayList<ViajeCentroBean> arrayListCentrosViaje = new ArrayList<>();

    public FrmViajesCentrosRegistrar(String ancho, ViajeBean viajeBean) {
        super(ancho);
        this.viajeBean = viajeBean;
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
        modificarButton.setVisible(false);
    }

    public FrmViajesCentrosRegistrar(String ancho, ViajeCentroBean viajeCentrosBean) {
        super(ancho);
        //  this.viajeBean=viajeCentrosBean.get
        this.viajeCentrosBean = viajeCentrosBean;
        viajeCentrosBinder.setBean(viajeCentrosBean);
        botonGrabar.setVisible(false);
        modificarButton.addClickListener(e -> doActualizar());
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
        provinciasCombo.setEnabled(false);
        tipoCentroCombo.setEnabled(false);
        //  centroCombo.setEnabled(false);
    }

    @Override
    public void doGrabar() {

        if (viajeCentrosBinder.writeBeanIfValid(viajeCentrosBean)) {
            viajeCentrosBean.setIdViaje(viajeBean.getId());
            if (new ViajesDao().doInsertaUnCentros(viajeCentrosBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
                this.close();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<ViajeCentroBean> validate = viajeCentrosBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }

    }

    public void doActualizar() {
        if (viajeCentrosBinder.writeBeanIfValid(viajeCentrosBean)) {
            if (new ViajesDao().doActualizaUnCentro(viajeCentrosBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
                this.close();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<ViajeCentroBean> validate = viajeCentrosBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }
    }

    @Override
    public void doBorrar() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doCancelar() {
        this.close();
    }

    @Override
    public void doCerrar() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAyuda() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doLimpiar() {
        viajeCentrosBinder.readBean(null);

    }

    @Override
    public void doGrid() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doActualizaGrid() {

    }

    @Override
    public void doBinderPropiedades() {
        viajeCentrosBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeCentroBean::getId, null);
        viajeCentrosBinder.forField(idViaje)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeCentroBean::getIdViaje, null);
        viajeCentrosBinder.forField(centroCombo)
                .asRequired()
                .bind(ViajeCentroBean::getCentroDestino, ViajeCentroBean::setCentroDestino);
        viajeCentrosBinder.forField(preparacionTextArea)
                .asRequired()
                .bind(ViajeCentroBean::getPreparacion, ViajeCentroBean::setPreparacion);
        viajeCentrosBinder.forField(actuacionTextArea)
                .bind(ViajeCentroBean::getActuacion, ViajeCentroBean::setActuacion);
    }

    @Override
    public void doComponenesAtributos() {
        this.setModal(true);
        if (viajeCentrosBean != null && viajeCentrosBean.getIdViaje() != null) {
            this.titulo.setText("Centros del viaje " + viajeCentrosBean.getIdViaje().toString());
        } else if (viajeBean != null && viajeBean.getId() != null) {
            this.titulo.setText("Centros del viaje " + viajeBean.getId().toString());
        } else {
            this.titulo.setText("Centros del viaje ");
        }

        this.tipoCentroCombo.setValue(CentroTipoBean.CENTROTIPOCENTROSALUD);

        contenedorDerecha.setVisible(false);
        preparacionTextArea.setHeight("150px");
        preparacionTextArea.setPlaceholder("Datos de la preparación del viaje");

        actuacionTextArea.setHeight("150px");
        actuacionTextArea.setPlaceholder("registra el trabajo realizado");

    }

    @Override
    public void doComponentesOrganizacion() {

        botonCancelar.setText("Salir");
        contenedorBotones.add(modificarButton);
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4));
        // fila1
        contenedorFormulario.add(provinciasCombo, tipoCentroCombo);
        contenedorFormulario.add(centroCombo, 2);
// fila 2
        contenedorFormulario.add(preparacionTextArea, 2);
// fila 3
        contenedorFormulario.add(actuacionTextArea, 2);
    }

    @Override
    public void doCompentesEventos() {

        provinciasCombo.addValueChangeListener(event -> {

            doCargaCombosCentro();
        });

        tipoCentroCombo.addValueChangeListener(event -> {
            doCargaCombosCentro();
        });

    }

    public void doCargaCombosCentro() {
        centroCombo.setItems(new CentroDao().getLista(null, AutonomiaBean.AUTONOMIADEFECTO, provinciasCombo.getValue(), null,
                null, tipoCentroCombo.getValue(), null, ConexionDao.BBDD_ACTIVOSI));
    }

}
