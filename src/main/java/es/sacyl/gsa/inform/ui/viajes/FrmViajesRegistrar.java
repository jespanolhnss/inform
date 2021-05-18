package es.sacyl.gsa.inform.ui.viajes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.ViajeBean;
import es.sacyl.gsa.inform.bean.ViajeCentroBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.dao.ViajesDao;
import es.sacyl.gsa.inform.reports.viajes.ViajesListadoResumenPdf;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.vaadin.klaudeta.PaginatedGrid;

public final class FrmViajesRegistrar extends FrmMasterPantalla {

    private static final long serialVersionUID = 1L;

    private final HorizontalLayout contenedorBuscadores1 = new HorizontalLayout();
    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<CentroBean> centroComboBuscador = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final DatePicker desde = new ObjetosComunes().getDatePicker("Desde", null, LocalDate.now());
    private final DatePicker hasta = new ObjetosComunes().getDatePicker("Hasta", null, LocalDate.now());


    /* Campos del formulario */
    TextField id = new ObjetosComunes().getTextField("id");
    DateTimePicker salida = new ObjetosComunes().getDateTimePicker("Salida", null, LocalDateTime.now().now());
    DateTimePicker llegada = new ObjetosComunes().getDateTimePicker("Llegada", null, Utilidades.getFechaHoraLas15horas());

    TextField matricula = new ObjetosComunes().getTextField("Matrícula", "teclea matrícula del vehículo", 25, "100px", "30px");

    /* Componentes para el viaje */
    ViajeBean viajeBean = new ViajeBean();
    Binder<ViajeBean> viajesBinder = new Binder<>();
    String tituloGridViajes = "LISTADO DE VIAJES";
    PaginatedGrid<ViajeBean> viajesGrid = new PaginatedGrid<>();
//    TreeGrid<ViajeBean> viajesGrid = new TreeGrid<>();

    ArrayList<ViajeBean> arrayListViajes = new ArrayList<>();

    /* Componentes para el detalle de los centros */
    ViajeCentroBean viajeCentroBean = new ViajeCentroBean();
    String tituloGridCentros = "CENTROS ASOCIADOS AL VIAJE";
    PaginatedGrid<ViajeCentroBean> viajeCentroGrid = new PaginatedGrid<>();
    PaginatedGrid<UsuarioBean> viajeTecnicoGrid = new PaginatedGrid<>();

    ArrayList<ViajeCentroBean> viajeCentrosArrayList = new ArrayList<>();

    HorizontalLayout contenedorBotones2 = new HorizontalLayout();
    Icon icon = new Icon(VaadinIcon.BUILDING);
    Button lanzarVentana = new ObjetosComunes().getBoton("Centros", ButtonVariant.LUMO_PRIMARY, icon);
    Icon tecnicosIcon = new Icon(VaadinIcon.USER);
    Button tecnicosButton = new ObjetosComunes().getBoton("Técnicos", ButtonVariant.LUMO_PRIMARY, tecnicosIcon);

    // componentes para gestionar los tabs de la parte inferior
    private final Tab centrosTab = new Tab("Centros");
    private final Tab tecnicosTab = new Tab("Tecnicos");

    private final Tabs tabs = new Tabs(centrosTab, tecnicosTab);
    private final Map<Tab, Component> tabsToPages = new HashMap<>();
    private final Div page1 = new Div();
    private final Div page2 = new Div();

    public FrmViajesRegistrar() {
        super();
        this.setWidthFull();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
        doControlBotones(null);
    }

    public void doControlBotones(Object obj) {
        botonImprimir.setVisible(true);
        if (obj != null) {
            botonBorrar.setEnabled(true);
            lanzarVentana.setEnabled(true);
            tecnicosButton.setEnabled(true);
            page1.setVisible(true);
        } else {
            botonBorrar.setEnabled(false);
            lanzarVentana.setEnabled(false);
            tecnicosButton.setEnabled(false);
            page1.setVisible(false);
        }
    }

    @Override
    public void doGrabar() {
        lanzarVentana.setEnabled(true);
        tecnicosButton.setEnabled(true);
        try {
            viajesBinder.writeBean(viajeBean);
            viajeBean.setValoresAut();
            if (new ViajesDao().doGrabaDatos(viajeBean) == true) {
                (new Notification(FrmMasterConstantes.AVISODATOALMACENADO, 3000, Notification.Position.MIDDLE)).open();
                // leo el binder para que pinte del id
                viajesBinder.readBean(viajeBean);
                doActualizaGrid();
                doControlBotones(viajeBean);
                //       doLimpiar();
            } else {
                (new Notification(FrmMasterConstantes.AVISODATOERRORBBDD, 3000, Notification.Position.MIDDLE)).open();
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doCancelar() {
        this.removeAll();
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMasterConstantes.AVISOCONFIRMACIONACCION,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new ViajesDao().doBorraDatos(viajeBean);
                    Notification.show(FrmMasterConstantes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
        dialog.addDialogCloseActionListener(e -> {
        });
    }

    @Override
    public void doAyuda() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doLimpiar() {

        viajesBinder.readBean(null);
        salida.setValue(LocalDateTime.now());
        llegada.setValue(Utilidades.getFechaHoraLas15horas());
        viajeCentroGrid.setItems(new ArrayList<ViajeCentroBean>());
        doActualizaGridCentros(viajeBean);

        viajeTecnicoGrid.setItems(new ArrayList<UsuarioBean>());
        doActualizaGridTecnicos(viajeBean);
    }

    @Override
    public void doImprimir() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doGrid() {
        // grid viaje
        viajesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        //viajesGrid.addColumn(ViajeBean::getId).setHeader("Id");
        //viajesGrid.addColumn(ViajeBean::getSalidaString).setHeader("Salida");
        //       viajesGrid.addColumn(ViajeBean::getLlegada).setHeader("Llegada");
        //  viajesGrid.addColumn(ViajeBean::getMatricula).setHeader("Matrícula");

        viajesGrid.addColumn(ViajeBean::getSalidaString).setHeader("Salida");
        viajesGrid.addColumn(ViajeBean::getMatricula).setHeader("Matrícula");
        viajesGrid.addColumn(ViajeBean::getCentrosNombre).setHeader("Centro");
        viajesGrid.setWidthFull();

        // grid centro
        viajeCentroGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        viajeCentroGrid.addColumn(ViajeCentroBean::getIdViaje).setHeader("IdViaje").setAutoWidth(true);
        viajeCentroGrid.addColumn(ViajeCentroBean::getNombreCentro).setHeader("Centro").setAutoWidth(true);
        viajeCentroGrid.addComponentColumn(item -> createRemoveButtonCen(viajeCentroGrid, item))
                .setHeader("Borra");

        viajeCentroGrid.addColumn(ViajeCentroBean::getPreparacion).setHeader("Preparación").setAutoWidth(true);
        viajeCentroGrid.addColumn(ViajeCentroBean::getActuacion).setHeader("Actuación").setAutoWidth(true);

//grid tecnico
        viajeTecnicoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        viajeTecnicoGrid.setHeightByRows(true);
        viajeTecnicoGrid.setPageSize(14);
        viajeTecnicoGrid.addColumn(UsuarioBean::getApellidosNombre).setAutoWidth(true).setHeader(new Html("<b>Usuario</b>"));
        viajeTecnicoGrid.addComponentColumn(item -> createRemoveButton(viajeTecnicoGrid, item))
                .setHeader("Borra");
        doActualizaGrid();
        doActualizaGridCentros(viajeBean);
        doActualizaGridTecnicos(viajeBean);
    }

    /**
     *
     * @param grid
     * @param item
     * @return Borra un centro del viaje
     */
    private Button createRemoveButtonCen(PaginatedGrid<ViajeCentroBean> grid, ViajeCentroBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.MINUS_CIRCLE.create(), clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        if (new ViajesDao().doBorraUnCentro(item) == true) {
                            Notification.show(FrmMensajes.AVISODATOBORRADO);
                        } else {
                            Notification.show(FrmMensajes.AVISODATOERRORBORRADO);
                        }

                        doActualizaGridCentros(viajeBean);
                    });
            dialog.open();
        });
        return button;
    }

    /**
     *
     * @param grid
     * @param item
     * @return Borra un técnico del viaje
     */
    private Button createRemoveButton(PaginatedGrid<UsuarioBean> grid, UsuarioBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.MINUS_CIRCLE.create(), clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        if (new ViajesDao().doBorraUnTecnico(viajeBean, item) == true) {
                            Notification.show(FrmMensajes.AVISODATOBORRADO);
                        } else {
                            Notification.show(FrmMensajes.AVISODATOERRORBORRADO);
                        }
                        doActualizaGridTecnicos(viajeBean);
                    });
            dialog.open();
        });
        return button;
    }

    @Override
    public void doActualizaGrid() {
        arrayListViajes = new ViajesDao().getListaViajes(desde.getValue(), hasta.getValue(), centroComboBuscador.getValue(), null, 1);
        //    viajesGrid.setItems(arrayListViajes);

        viajesGrid.setItems(arrayListViajes);
        //  viajesGrid.setItems(arrayListViajes, ViajeBean::getListraCentrosTree);
        //  viajesGrid.expand(arrayListViajes);

    }

    @Override
    public void doBinderPropiedades() {
        viajesBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeBean::getId, null);
        viajesBinder.forField(salida).bind(ViajeBean::getSalida, ViajeBean::setSalida);

        viajesBinder.forField(llegada)
                .withValidator(value -> value.isAfter(salida.getValue()), "Llegada debe ser  posterior")
                .bind(ViajeBean::getLlegada, ViajeBean::setLlegada);

        viajesBinder.forField(matricula)
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 8))
                .bind(ViajeBean::getMatricula, ViajeBean::setMatricula);
    }

    /**
     *
     */
    @Override
    public void doComponenesAtributos() {
        contenedorIzquierda.setWidth("50%");
        contenedorDerecha.setWidth("50%");
        autonomiaComboBuscador.setVisible(false);

        provinciaComboBuscador.setLabel("");
        centroTipoComboBuscador.setLabel("");
        centroTipoComboBuscador.setValue(CentroTipoBean.CENTROTIPOCENTROSALUD);
        centroTipoComboBuscador.setPlaceholder("Tipo de centro");

        centroComboBuscador.setLabel("");
        centroComboBuscador.setPlaceholder("Elige centro");
        desde.setLabel("");
        desde.setPlaceholder("De fecha");
        hasta.setLabel("");
        hasta.setPlaceholder("hasta fecha");

        botonImprimir.setVisible(true);
        id.setWidth("150px");
        id.setMaxWidth("150px");
        id.setMinWidth("150px");

        matricula.setMaxLength(8);

        page1.setWidthFull();
        page2.setWidthFull();

        centrosTab.setVisible(true);
        tecnicosTab.setVisible(true);

        page1.setVisible(false);
        page2.setVisible(false);
    }

    /**
     *
     */
    @Override
    public void doComponentesOrganizacion() {
        contenedorIzquierda.setWidth("50%");
        contenedorIzquierda.removeAll();

        page1.add(viajeCentroGrid);
        page2.add(viajeTecnicoGrid);

        tabsToPages.put(centrosTab, page1);
        tabsToPages.put(tecnicosTab, page2);

        contenedorIzquierda.add(contenedorBotones, contenedorBotones2, contenedorFormulario, tabs, page1, page2);

        lanzarVentana.setEnabled(false);
        tecnicosButton.setEnabled(false);
        contenedorBotones2.add(lanzarVentana, tecnicosButton);

        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("150px", 1),
                new FormLayout.ResponsiveStep("150px", 2));
        contenedorFormulario.add(id, salida, llegada, matricula);

        contenedorBuscadores.add(autonomiaComboBuscador, provinciaComboBuscador, centroTipoComboBuscador, centroComboBuscador);
        contenedorBuscadores1.add(desde, hasta, botonImprimir);

        contenedorDerecha.add(contenedorBuscadores);
        contenedorDerecha.add(contenedorBuscadores1);
        contenedorDerecha.add(tituloGridViajes);
        contenedorDerecha.add(viajesGrid);
    }

    @Override
    public void doCompentesEventos() {
        autonomiaComboBuscador.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComboProvinicas(provinciaComboBuscador, autonomiaBean);
        });

        provinciaComboBuscador.addValueChangeListener(event -> {
            doActualizaComboCentro();
        });

        centroTipoComboBuscador.addValueChangeListener(event -> {
            doActualizaComboCentro();
        });

        centroComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        desde.addValueChangeListener(e -> doActualizaGrid());

        hasta.addValueChangeListener(e -> doActualizaGrid());

        lanzarVentana.addClickListener(event -> {
            FrmViajesCentrosRegistrar frmViajesCentrosRegistrar = new FrmViajesCentrosRegistrar("640px", viajeBean);

            frmViajesCentrosRegistrar.addDetachListener(e -> {
                doActualizaGridCentros(viajeBean);
                doActualizaGridTecnicos(viajeBean);
            });

            frmViajesCentrosRegistrar.addDialogCloseActionListener(e -> {
                doActualizaGridCentros(viajeBean);
                doActualizaGridTecnicos(viajeBean);
            });
            frmViajesCentrosRegistrar.open();
        });

        viajesGrid.addItemClickListener(event -> {
            viajeBean = event.getItem();
            viajesBinder.readBean(viajeBean);
            doActualizaGridCentros(viajeBean);
            doActualizaGridTecnicos(viajeBean);
            doControlBotones(viajeBean);
            page1.setVisible(true);
        });

        viajeCentroGrid.addItemClickListener(event -> {
            viajeCentroBean = event.getItem();

            FrmViajesCentrosRegistrar frmViajesCentrosRegistrar = new FrmViajesCentrosRegistrar("740px", viajeCentroBean);

            frmViajesCentrosRegistrar.addDetachListener(e -> {
                doActualizaGridCentros(viajeBean);
                doActualizaGridTecnicos(viajeBean);
            });

            frmViajesCentrosRegistrar.addDialogCloseActionListener(e -> {
                doActualizaGridCentros(viajeBean);
                doActualizaGridTecnicos(viajeBean);
            });

            frmViajesCentrosRegistrar.open();
        }
        );

        tecnicosButton.addClickListener(event
                -> {
            FrmViajesTecnicosRegistrar nuevo = new FrmViajesTecnicosRegistrar(viajeBean);
            nuevo.addDialogCloseActionListener(e -> {
                doActualizaGridTecnicos(viajeBean);
                nuevo.close();
            });
            nuevo.addDetachListener(e -> {
                doActualizaGridTecnicos(viajeBean);
                nuevo.close();
            });
            nuevo.open();
        }
        );

        /**
         * Gestiona los clic en los tabs ocultando todos y mostrando el del
         * click
         */
        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        botonImprimir.addClickListener(event -> {
            ViajesListadoResumenPdf viajesListadoResumen = new ViajesListadoResumenPdf(desde.getValue(), hasta.getValue());
            viajesListadoResumen.doCreaFicheroPdf();
            Page page = new Page(getUI().get());
            page.open(viajesListadoResumen.getUrlDelPdf(), "_blank");
        });

    }

    public void doActualizaComboProvinicas(ComboBox<ProvinciaBean> combo, AutonomiaBean autonomia) {
        ArrayList<ProvinciaBean> privinciaArrayList = new ProvinciaDao().getLista(null, autonomia);
        combo.setItems(privinciaArrayList);
    }

    public void doActualizaComboCentro() {
        ArrayList<CentroBean> centroArrayList = new CentroDao().getLista(null, autonomiaComboBuscador.getValue(),
                provinciaComboBuscador.getValue(), null, null, centroTipoComboBuscador.getValue(), null, ConexionDao.BBDD_ACTIVOSI);

        centroComboBuscador.setItems(centroArrayList);

        if (centroArrayList.size() > 0) {
            centroComboBuscador.setValue(centroArrayList.get(0));
        }
    }

    private void doActualizaGridCentros(ViajeBean viajeBean) {
        viajeCentrosArrayList = new ViajesDao().getViajeCentros(viajeBean);
        viajeCentroGrid.setItems(viajeCentrosArrayList);
        centrosTab.setLabel("Centros (" + viajeCentrosArrayList.size() + ")");
    }

    private void doActualizaGridTecnicos(ViajeBean viajeBean) {
        ArrayList<UsuarioBean> usuarioBeans = new ViajesDao().getListaTecnicosViaje(viajeBean);
        viajeTecnicoGrid.setItems(usuarioBeans);
        tecnicosTab.setLabel("Técnicos (" + usuarioBeans.size() + ")");
    }

}
