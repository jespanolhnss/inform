package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.componentfactory.Tooltip;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.LocalidadBean;
import es.sacyl.gsa.inform.bean.ProveedorBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.LocalidadDao;
import es.sacyl.gsa.inform.dao.ProveedorDao;
import es.sacyl.gsa.inform.dao.ProveedorDatosDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.ui.recursos.FrmProveedorDatoGenerico;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmProveedor extends FrmMasterPantalla {

    // private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, null);
    private final ComboBox<LocalidadBean> localidadCombo = new CombosUi().getLocalidadCombo(null, null, ProvinciaBean.PROVINCIA_DEFECTO, null);

    private final Button datosGenericosButton = new ObjetosComunes().getBoton(null, null, VaadinIcon.INPUT.create());
    private final Tooltip datosGenericosTooltip = new ObjetosComunes().getTooltip(datosGenericosButton, "Registra características del proveedor");

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

    private PaginatedGrid<DatoGenericoBean> datoGenericoGrid = new PaginatedGrid<>();
    private final Tab datosTab = new Tab("Datos");
    private final Tabs tabs = new Tabs(datosTab);
    private final Map<Tab, Component> tabsToPages = new HashMap<>();
    private final Div page1 = new Div();

    public FrmProveedor() {
        super();
        this.proveedorBean = new ProveedorBean();
        estadoRadio.setItems(ObjetosComunes.SINO);

        doComponentesOrganizacion();
        doGrid();
        doGridDatoGenerico();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doControlBotones(null);
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            datosGenericosButton.setEnabled(false);
            nombre.focus();
        } else {
            nombre.focus();
            datosGenericosButton.setEnabled(true);
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

    public void doGridDatoGenerico() {
        datoGenericoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        datoGenericoGrid.setHeightByRows(true);
        datoGenericoGrid.setPageSize(14);
        //   datoGenericoGrid.addColumn(DatoGenericoBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        datoGenericoGrid.addColumn(new ComponentRenderer<>(datoGenerico -> {
            Image img = new Image("icons/unpixel.png", "img");
            if (datoGenerico.getFicheroBlobs() != null) {
                img = new Image("icons/pdf.png", "pdf");
                img.addClickListener(event -> {
                    Page page = new Page(getUI().get());
                    page.open(datoGenerico.getPathRelativo(), "_blank");
                });
            }
            return img;

        })).setHeader("Pdf");
        datoGenericoGrid.addColumn(DatoGenericoBean::getTipoDato).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));
        datoGenericoGrid.addColumn(DatoGenericoBean::getValorAncho25).setAutoWidth(true).setHeader(new Html("<b>Valor</b>"));

        doActualizaGridDatos();

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
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 5))
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
        botonBorrar.setText("");
        botonLimpiar.setText("");
        botonAyuda.setText("");
        botonGrabar.setText("");
        botonCancelar.setText("");
        botonImprimir.setText("");
        this.titulo.setText("Proveedores");
        buscador.setTitle("Dato a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorBotones.add(datosGenericosButton, datosGenericosTooltip);
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4));

        tabsToPages.put(datosTab, page1);

        //   Div pages = new Div(page1, page2, page3);
        page1.add(datoGenericoGrid);

        this.contenedorFormulario.add(id);
        this.contenedorFormulario.add(nombre, 3);

        this.contenedorFormulario.add(nombre, 4);

        this.contenedorFormulario.add(direccion, 4);

        this.contenedorFormulario.add(codpostal);
        this.contenedorFormulario.add(telefonos, 2);

        this.contenedorFormulario.add(mail);

        this.contenedorFormulario.add(provinciaCombo, 2);
        this.contenedorFormulario.add(localidadCombo, 2);

        this.contenedorIzquierda.removeAll();
        this.contenedorIzquierda.add(contenedorBotones, contenedorFormulario, tabs, page1);

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
            doActualizaGridDatos();
            page1.setVisible(true);
        }
        );

        provinciaCombo.addValueChangeListener(event -> {
            localidadCombo.setItems(new LocalidadDao().getLista(null, null, provinciaCombo.getValue()));
        });

        datosGenericosButton.addClickListener(event -> {
            doVentanaDatoGenerico(null);
        });

        /**
         * Gestiona los clic en los tabs ocultando todos y mostrando el del
         * click
         */
        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        datoGenericoGrid.addItemClickListener(event -> {
            DatoGenericoBean datoGenericoBean = event.getItem();
            doVentanaDatoGenerico(datoGenericoBean);
        }
        );
    }

    public void doVentanaDatoGenerico(DatoGenericoBean datoGenericoBean) {
        FrmProveedorDatoGenerico frmProveedorDatoGenerico = new FrmProveedorDatoGenerico(proveedorBean, datoGenericoBean);
        frmProveedorDatoGenerico.addDialogCloseActionListener(event1 -> {
            proveedorBean.setListaDatos(frmProveedorDatoGenerico.getDatoGenericoBeanArray());
            doActualizaGridDatos();
            page1.setVisible(true);
        });

        frmProveedorDatoGenerico.addDetachListener(event1 -> {
            proveedorBean.setListaDatos(frmProveedorDatoGenerico.getDatoGenericoBeanArray());
            doActualizaGridDatos();
            page1.setVisible(true);
        });
        frmProveedorDatoGenerico.open();
    }

    public void doActualizaGridDatos() {

        ArrayList<DatoGenericoBean> listaDatosGenerico = new ProveedorDatosDao().getLista(null, proveedorBean);

        datoGenericoGrid.setItems(listaDatosGenerico);
        datosTab.setLabel("Caracterísitcas (" + Integer.toString(listaDatosGenerico.size()) + ")");
    }
}
