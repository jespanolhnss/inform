package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.LocalidadBean;
import es.sacyl.gsa.inform.bean.ProveedorBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.LocalidadDao;
import es.sacyl.gsa.inform.dao.ProveedorDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public class FrmProveedor extends FrmMasterPantalla {

    // private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, null);
    private final ComboBox<LocalidadBean> localidadCombo = new CombosUi().getLocalidadCombo(null, null, ProvinciaBean.PROVINCIA_DEFECTO, null);

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");
    private final TextField direccion = new ObjetosComunes().getTextField("Direccion");
    private final TextField codpostal = new ObjetosComunes().getTextField("Cod.Postal");
    private final TextField telefonos = new ObjetosComunes().getTextField("Teléfonos");
    private final TextField mail = new ObjetosComunes().getTextField("Mail");

    private final ComboBox<ProvinciaBean> provinciaCombo = new CombosUi().getProvinciaCombo(null, null, null);
    private final ComboBox<LocalidadBean> localidadComco = new CombosUi().getLocalidadCombo(null, null, ProvinciaBean.PROVINCIA_DEFECTO, null);
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();

    private ProveedorBean proveedorBean = null;
    private final Binder<ProveedorBean> proveedorBinder = new Binder<>();
    private final PaginatedGrid<ProveedorBean> proveedorGrid = new PaginatedGrid<>();
    private ArrayList<ProveedorBean> proveedorLista = new ArrayList<>();

    public FrmProveedor() {
        super();
        this.proveedorBean = new ProveedorBean();
        estadoRadio.setItems(ObjetosComunes.SINO);

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
            nombre.focus();
        } else {
            nombre.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (proveedorBinder.writeBeanIfValid(proveedorBean)) {
            if (new ProveedorDao().doGrabaDatos(proveedorBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<ProveedorBean> validate = proveedorBinder.validate();
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
        this.setVisible(false);
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new ProveedorDao().doBorraDatos(proveedorBean);
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
        proveedorBean = new ProveedorBean();
        proveedorBinder.readBean(proveedorBean);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        proveedorGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        proveedorGrid.setHeightByRows(true);
        proveedorGrid.setPageSize(25);
        proveedorGrid.setPaginatorSize(25);
        proveedorGrid.addColumn(ProveedorBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        proveedorGrid.addColumn(ProveedorBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        proveedorGrid.addColumn(ProveedorBean::getEstado).setKey("estado").setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        proveedorGrid.setClassName("error_row");
        proveedorGrid.setClassNameGenerator(auto -> {
            if (auto.getEstado() == 0) {
                //    return "my-style-2";
                return "error_row";
            } else {
                return "my-style-1";
            }

        });
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        proveedorLista = new ProveedorDao().getLista(buscador.getValue());
        proveedorGrid.setItems(proveedorLista);
    }

    @Override
    public void doBinderPropiedades() {
        proveedorBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ProveedorBean::getId, ProveedorBean::setId);

        proveedorBinder.forField(nombre)
                .asRequired()
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(ProveedorBean::getNombre, ProveedorBean::setNombre);

        //  nombre,direccion,codpostal,telefonos,mail,localidad,provincia;
        proveedorBinder.forField(direccion)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(ProveedorBean::getDireccion, ProveedorBean::setDireccion);

        proveedorBinder.forField(codpostal)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(ProveedorBean::getCodpostal, ProveedorBean::setCodpostal);

        proveedorBinder.forField(telefonos)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(ProveedorBean::getTelefonos, ProveedorBean::setTelefonos);

        proveedorBinder.forField(mail)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(ProveedorBean::getMail, ProveedorBean::setMail);

        proveedorBinder.forField(provinciaCombo)
                .bind(ProveedorBean::getProvincia, ProveedorBean::setProvincia);

        proveedorBinder.forField(localidadCombo)
                .bind(ProveedorBean::getLocalidad, ProveedorBean::setLocalidad);

    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Proveedores");
        buscador.setTitle("Dato a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4));

        this.contenedorFormulario.add(id);
        this.contenedorFormulario.add(nombre, 3);

        this.contenedorFormulario.add(nombre, 4);

        this.contenedorFormulario.add(codpostal);
        this.contenedorFormulario.add(telefonos, 2);

        this.contenedorFormulario.add(mail);

        this.contenedorFormulario.add(provinciaCombo, 2);
        this.contenedorFormulario.add(localidadCombo, 2);

        this.contenedorBuscadores.add(buscador, provinciaComboBuscador);
        this.contenedorDerecha.removeAll();

        this.contenedorDerecha.add(contenedorBuscadores, proveedorGrid);
    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        provinciaComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        proveedorGrid.addItemClickListener(event -> {
            proveedorBean = event.getItem();
            //   localidadCombo.setItems(new LocalidadDao().getLista(null, null, proveedorBean.getProvincia()));
            proveedorBinder.readBean(proveedorBean);
            doControlBotones(proveedorBinder);
        }
        );

        provinciaCombo.addValueChangeListener(event -> {
            localidadCombo.setItems(new LocalidadDao().getLista(null, null, provinciaCombo.getValue()));
        });
    }

}
