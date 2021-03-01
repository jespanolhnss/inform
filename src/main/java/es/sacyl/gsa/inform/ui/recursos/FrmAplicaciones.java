package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.ProveedorBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.AplicacionDao;
import es.sacyl.gsa.inform.dao.AplicacionPerfilDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
@CssImport("./styles/styles.css")
public final class FrmAplicaciones extends FrmMasterPantalla {

    private final Button botonPerfiles = new ObjetosComunes().getBoton("Perfiles", null, VaadinIcon.FILE_TABLE.create());

    private final ComboBox<ProveedorBean> proveedorComboBuscador = new CombosUi().getProveedorCombo(null, null);
    private final ComboBox<GfhBean> gfhcomboBuscador = new CombosUi().getServicioCombo(null, null);

    private final TextField id = new ObjetosComunes().getTextField("Código");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");
    private final ComboBox<ProveedorBean> proveedorCombo = new CombosUi().getProveedorCombo(null, null);

    private final ComboBox<String> ambitoCombo = new CombosUi().getCombodeTabla("Ambito App", null, ComboBean.APPAMBITO, 25);
    private final ComboBox<String> gestionUsuariosComco = new CombosUi().getCombodeTabla("Usuarios", null, ComboBean.APPGESTIONUSUARIOS, 25);

    private final TextArea descripcion = new ObjetosComunes().getTextArea("Descripción ");
    private final ComboBox<GfhBean> gfhCombo = new CombosUi().getServicioCombo(null, null);
    private final DatePicker fechaInstalacion = new ObjetosComunes().getDatePicker("Instalación", null, null);

    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();

    private AplicacionBean aplicacionesBean = null;
    private final Binder<AplicacionBean> aplicacionesBinder = new Binder<>();
    private final PaginatedGrid<AplicacionBean> aplicacionesGrid = new PaginatedGrid<>();
    private final PaginatedGrid<AplicacionPerfilBean> aplicacionesPerfiGrid = new PaginatedGrid<>();
    private ArrayList<AplicacionBean> aplicacionesLista = new ArrayList<>();

    public FrmAplicaciones() {
        super();
        this.aplicacionesBean = new AplicacionBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
        } else {
            // codigo.setEnabled(false);
            nombre.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (aplicacionesBinder.writeBeanIfValid(aplicacionesBean)) {
            aplicacionesBean.setValoresAut();
            if (new AplicacionDao().doGrabaDatos(aplicacionesBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<AplicacionBean> validate = aplicacionesBinder.validate();
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
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    aplicacionesBean.setEstado(ConexionDao.BBDD_ACTIVONO);
                    aplicacionesBean.setValoresAut();
                    new AplicacionDao().doBorraDatos(aplicacionesBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doLimpiar();
                });
        dialog.open();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        aplicacionesBean = new AplicacionBean();
        aplicacionesBinder.readBean(aplicacionesBean);
        doActualizaGrid();
        doActualizaGridPerfiles();
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        aplicacionesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        aplicacionesGrid.setHeightByRows(true);
        aplicacionesGrid.setPageSize(14);
        aplicacionesGrid.addColumn(AplicacionBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        aplicacionesGrid.addColumn(AplicacionBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        aplicacionesGrid.addColumn(AplicacionBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        aplicacionesGrid.setClassNameGenerator(auto -> {
            if (auto.getEstado() == 0) {
                //    return "my-style-2";
                return "tr.my-style-1";
            } else {
                return "tr.my-style-2";
            }

        });
        doActualizaGrid();

        aplicacionesPerfiGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        aplicacionesPerfiGrid.setHeightByRows(true);
        aplicacionesPerfiGrid.setPageSize(14);
        aplicacionesPerfiGrid.addColumn(AplicacionPerfilBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        aplicacionesPerfiGrid.addColumn(AplicacionPerfilBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        aplicacionesPerfiGrid.addColumn(AplicacionPerfilBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        aplicacionesPerfiGrid.addComponentColumn(item -> createRemoveButton(aplicacionesPerfiGrid, item))
                .setHeader("Borra");
        aplicacionesPerfiGrid.setClassNameGenerator(apppderfil -> {
            if (apppderfil.getEstado() == 0) {
                //    return "my-style-2";
                return "line-error";
                //   return "my-style-1";
            } else {
                return "my-style-2";
            }
        });

    }

    private Button createRemoveButton(PaginatedGrid<AplicacionPerfilBean> grid, AplicacionPerfilBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Borra", clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        item.setEstado(ConexionDao.BBDD_ACTIVONO);
                        item.setFechacambio(LocalDate.now());
                        item.setUsucambio((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
                        new AplicacionPerfilDao().doBorraDatos(item);
                        doActualizaGridPerfiles();
                    });
            dialog.open();
        });
        return button;
    }

    @Override
    public void doActualizaGrid() {
        aplicacionesLista = new AplicacionDao().getLista(buscador.getValue(), gfhcomboBuscador.getValue(), proveedorComboBuscador.getValue(), null);
        aplicacionesGrid.setItems(aplicacionesLista);
    }

    public void doActualizaGridPerfiles() {
        aplicacionesPerfiGrid.setItems(aplicacionesBean.getListaPerfiles());
    }

    @Override
    public void doBinderPropiedades() {
        aplicacionesBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(AplicacionBean::getId, null);

        aplicacionesBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(AplicacionBean::getNombre, AplicacionBean::setNombre);

        aplicacionesBinder.forField(proveedorCombo)
                .asRequired()
                .bind(AplicacionBean::getProveedor, AplicacionBean::setProveedor);

        aplicacionesBinder.forField(ambitoCombo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(AplicacionBean::getAmbito, AplicacionBean::setAmbito);

        aplicacionesBinder.forField(gestionUsuariosComco)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(AplicacionBean::getGestionUsuarios, AplicacionBean::setGestionUsuarios);

        aplicacionesBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(AplicacionBean::getDescripcion, AplicacionBean::setDescripcion);

        aplicacionesBinder.forField(gfhCombo)
                .asRequired()
                .bind(AplicacionBean::getGfh, AplicacionBean::setGfh);

        aplicacionesBinder.forField(estadoRadio)
                .bind(AplicacionBean::getEstadoString, AplicacionBean::setEstado);

        //id,nombre,proveedor,ambito,gestionusuarios,descripcion,servicio,fechainstalacion,estado,usucambio,fechacambio
    }

    @Override
    public void doComponenesAtributos() {
        buscador.setLabel("Dato a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Aplicaciones");
        contenedorBotones.add(botonPerfiles);
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4),
                new FormLayout.ResponsiveStep("50px", 5),
                new FormLayout.ResponsiveStep("50px", 6));

        this.contenedorFormulario.add(id);
        this.contenedorFormulario.add(nombre, 5);

        this.contenedorFormulario.add(ambitoCombo, 2);
        this.contenedorFormulario.add(gestionUsuariosComco, 2);
        this.contenedorFormulario.add(fechaInstalacion, 2);

        this.contenedorFormulario.add(proveedorCombo, 3);
        this.contenedorFormulario.add(gfhCombo, 3);
        this.contenedorFormulario.add(estadoRadio, 2);
        this.contenedorFormulario.add(descripcion, 4);

        contenedorIzquierda.add(aplicacionesPerfiGrid);
        this.contenedorBuscadores.add(buscador, gfhcomboBuscador, proveedorComboBuscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, aplicacionesGrid);

    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        gfhcomboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        proveedorComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        aplicacionesGrid.addItemClickListener(event -> {
            aplicacionesBean = event.getItem();
            aplicacionesBinder.readBean(event.getItem());
            doControlBotones(aplicacionesBean);
            doActualizaGridPerfiles();
        }
        );

        aplicacionesPerfiGrid.addItemClickListener(event -> {
            AplicacionPerfilBean aplicacionPerfilBean = event.getItem();
            doVentanaPerfil(aplicacionPerfilBean);
        }
        );
        botonPerfiles.addClickListener(event -> {
            AplicacionPerfilBean aplicacionPerfilBean = new AplicacionPerfilBean();
            aplicacionPerfilBean.setAplicacion(aplicacionesBean);
            doVentanaPerfil(aplicacionPerfilBean);
        });
    }

    public void doVentanaPerfil(AplicacionPerfilBean aplicacionPerfilBean) {
        FrmAplicacionesPerfiles frmAplicacionesPerfiles = new FrmAplicacionesPerfiles("500px", aplicacionPerfilBean);

        frmAplicacionesPerfiles.addDialogCloseActionListener(event -> {
            doActualizaGridPerfiles();
        });
        frmAplicacionesPerfiles.open();
    }

}
