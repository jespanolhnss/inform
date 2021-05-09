package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.EquipoAplicacionBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.ProveedorBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.AplicacionDao;
import es.sacyl.gsa.inform.dao.AplicacionPerfilDao;
import es.sacyl.gsa.inform.dao.AplicacionesDatosDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.EquipoAplicacionDao;
import es.sacyl.gsa.inform.reports.recursos.AplicacionPDF;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.ui.VentanaPdf;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
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
@CssImport("./styles/styles.css")
public final class FrmAplicacion extends FrmMasterPantalla {

    private final Button botonPerfiles = new ObjetosComunes().getBoton(null, null, VaadinIcon.CLIPBOARD_USER.create());
    private final Button equiposButton = new ObjetosComunes().getBoton(null, null, VaadinIcon.DESKTOP.create());
    private final Button datosGenericosButton = new ObjetosComunes().getBoton(null, null, VaadinIcon.INPUT.create());

    private final ComboBox<ProveedorBean> proveedorComboBuscador = new CombosUi().getProveedorCombo(null, null);
    private final ComboBox<GfhBean> gfhcomboBuscador = new CombosUi().getServicioCombo(null, null);
    private final Button botonImprimirBuscador = new ObjetosComunes().getBoton(null, null, VaadinIcon.PRINT.create());

    private final TextField id = new ObjetosComunes().getTextField("Código");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");
    private final ComboBox<ProveedorBean> proveedorCombo = new CombosUi().getProveedorCombo(null, null);

    private final ComboBox<String> ambitoCombo = new CombosUi().getCombodeTabla("Ambito App", null, ComboBean.APPAMBITO, 25);
    private final ComboBox<String> gestionUsuariosComco = new CombosUi().getCombodeTabla("Quién gestiona usuarios?", null, ComboBean.APPGESTIONUSUARIOS, 25);

    private final TextArea descripcion = new ObjetosComunes().getTextArea("Descripción ");
    private final ComboBox<GfhBean> gfhCombo = new CombosUi().getServicioCombo(null, null);
    private final DatePicker fechaInstalacion = new ObjetosComunes().getDatePicker("Instalación", null, null);

    //  private final RadioButtonGroup<String> estadoCombo = new ObjetosComunes().getEstadoRadio();
    private final ComboBox<String> estadoCombo = new CombosUi().getSiNoCombo("Activo");

    private AplicacionBean aplicacionBean = null;
    private final Binder<AplicacionBean> aplicacionesBinder = new Binder<>();

    private PaginatedGrid<DatoGenericoBean> datoGenericoGrid = new PaginatedGrid<>();
    private final PaginatedGrid<AplicacionBean> aplicacionesGrid = new PaginatedGrid<>();
    private final PaginatedGrid<AplicacionPerfilBean> aplicacionesPerfiGrid = new PaginatedGrid<>();
    private ArrayList<AplicacionBean> aplicacionesLista = new ArrayList<>();

    private final PaginatedGrid<EquipoBean> equipoGrid = new GridUi().getEquipoGridPaginado();

    // componentes para gestionar los tabs de la parte inferior
    private final Tab datosTab = new Tab("Datos");
    private final Tab perfilesTab = new Tab("Perfiles");
    private final Tab equipoTab = new Tab("Equipos");
    private final Tabs tabs = new Tabs(datosTab, equipoTab, perfilesTab);
    private final Map<Tab, Component> tabsToPages = new HashMap<>();
    private final Div page1 = new Div();
    private final Div page2 = new Div();
    private final Div page3 = new Div();

    public FrmAplicacion() {
        super();
        this.aplicacionBean = new AplicacionBean();
        doComponenesAtributos();
        doComponentesOrganizacion();
        doGrid();
        doGridPerfiles();
        doGridEquipo();
        doGridDatoGenerico();
        doBinderPropiedades();
        doCompentesEventos();
        doControlBotones(null);
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            nombre.focus();
            datosGenericosButton.setEnabled(false);
            botonPerfiles.setEnabled(false);
            equiposButton.setEnabled(false);
        } else {
            datosGenericosButton.setEnabled(true);
            botonPerfiles.setEnabled(true);
            equiposButton.setEnabled(true);
        }
    }

    @Override
    public void doGrabar() {
        if (aplicacionesBinder.writeBeanIfValid(aplicacionBean)) {
            aplicacionBean.setValoresAut();
            if (new AplicacionDao().doGrabaDatos(aplicacionBean) == true) {
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
                    aplicacionBean.setEstado(ConexionDao.BBDD_ACTIVONO);
                    aplicacionBean.setValoresAut();
                    new AplicacionDao().doBorraDatos(aplicacionBean);
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
        aplicacionBean = new AplicacionBean();
        aplicacionesBinder.readBean(aplicacionBean);
        doActualizaGrid();
        equipoGrid.setItems(new ArrayList<>());
        equipoTab.setLabel("Equipos(0)");
        aplicacionesPerfiGrid.setItems(new ArrayList<>());
        perfilesTab.setLabel("Perfieles(0)");
        datoGenericoGrid.setItems(new ArrayList<>());
        datosTab.setLabel("Características (0)");
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {

        AplicacionPDF aplicacionPdf = new AplicacionPDF(aplicacionBean);
        aplicacionPdf.doCreaFicheroPdf();
        VentanaPdf ventanaPdf = new VentanaPdf(aplicacionPdf.getUrlDelPdf());
        ventanaPdf.addDialogCloseActionListener(event1 -> {
            aplicacionPdf.doBorraPdf();
        });

    }

    public void doImprimir(ArrayList<AplicacionBean> listaParam) {
        ArrayList<AplicacionBean> listaPddf = new ArrayList<>();
        for (AplicacionBean app : listaParam) {
            app.setListaDatosGenerico(new AplicacionesDatosDao().getLista(null, app));
            aplicacionBean.setListaEquipoBeans(new EquipoAplicacionDao().getLista(null, null, app));
            listaPddf.add(app);
        }
        AplicacionPDF aplicacionPdf = new AplicacionPDF(listaPddf);
        aplicacionPdf.doCreaFicheroPdf();
        VentanaPdf ventanaPdf = new VentanaPdf(aplicacionPdf.getUrlDelPdf());
        ventanaPdf.addDialogCloseActionListener(event1 -> {
            aplicacionPdf.doBorraPdf();
        });

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

    }

    public void doGridPerfiles() {
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

    public void doGridEquipo() {
        equipoGrid.addComponentColumn(item -> createRemoveButtonU(equipoGrid, item))
                .setHeader("Borra");
        doActualizaGridEquipos();
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

    private Button createRemoveButtonU(Grid<EquipoBean> grid, EquipoBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.MINUS_CIRCLE.create(), clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        new EquipoAplicacionDao().doBorraDatos(item, aplicacionBean);
                        aplicacionBean.setListaEquipoBeans(new EquipoAplicacionDao().getLista(null, null, aplicacionBean));
                        doActualizaGridEquipos();
                    });
            dialog.open();
        });
        return button;
    }

    private Button createRemoveButton(PaginatedGrid<AplicacionPerfilBean> grid, AplicacionPerfilBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.MINUS_CIRCLE.create(), clickEvent -> {
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
        aplicacionesLista = new AplicacionDao().getLista(buscador.getValue(), gfhcomboBuscador.getValue(), proveedorComboBuscador.getValue(), null, false);
        aplicacionesGrid.setItems(aplicacionesLista);
    }

    public void doActualizaGridPerfiles() {
        aplicacionesPerfiGrid.setItems(aplicacionBean.getListaPerfiles());
        perfilesTab.setLabel("Perfiles (" + Integer.toString(aplicacionBean.getListaPerfiles().size()) + ")");
    }

    public void doActualizaGridEquipos() {
        ArrayList<EquipoAplicacionBean> listaAplicacionBeans = aplicacionBean.getListaEquipoBeans();

        ArrayList<EquipoBean> listaEquipoBeans = new ArrayList<>();
        for (EquipoAplicacionBean eqap : listaAplicacionBeans) {
            listaEquipoBeans.add(eqap.getEquipo());
        }
        equipoGrid.setItems(listaEquipoBeans);
        equipoTab.setLabel("Equipos (" + Integer.toString(listaEquipoBeans.size()) + ")");
    }

    public void doActualizaGridDatos() {

        ArrayList<DatoGenericoBean> listaDatosGenerico = new AplicacionesDatosDao().getLista(null, aplicacionBean);

        datoGenericoGrid.setItems(listaDatosGenerico);
        datosTab.setLabel("Caracterísitcas (" + Integer.toString(listaDatosGenerico.size()) + ")");
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
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 99))
                .bind(AplicacionBean::getGestionUsuarios, AplicacionBean::setGestionUsuarios);

        aplicacionesBinder.forField(fechaInstalacion)
                .bind(AplicacionBean::getFechaInstalacion, AplicacionBean::setFechaInstalacion);

        aplicacionesBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 499))
                .bind(AplicacionBean::getDescripcion, AplicacionBean::setDescripcion);

        aplicacionesBinder.forField(gfhCombo)
                .asRequired()
                .bind(AplicacionBean::getGfh, AplicacionBean::setGfh);

        aplicacionesBinder.forField(estadoCombo)
                .bind(AplicacionBean::getEstadoString, AplicacionBean::setEstado);

        //id,nombre,proveedor,ambito,gestionusuarios,descripcion,servicio,fechainstalacion,estado,usucambio,fechacambio
    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Aplicaciones");
        buscador.setLabel("Dato a buscar");
        botonAyuda.setText("");
        botonLimpiar.setText("");
        botonBorrar.setText("");
        botonCancelar.setText("");

        proveedorComboBuscador.setValue(null);
        gfhcomboBuscador.setValue(null);

        page1.setWidthFull();
        page2.setWidthFull();
        page3.setWidthFull();

        datosTab.setVisible(true);
        equipoTab.setVisible(true);
        perfilesTab.setVisible(true);

        page1.setVisible(false);
        page2.setVisible(false);
        page3.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Aplicaciones");
        botonImprimir.setText("");

        contenedorBotones.add(botonPerfiles, equiposButton, datosGenericosButton, botonImprimir);
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4),
                new FormLayout.ResponsiveStep("50px", 5),
                new FormLayout.ResponsiveStep("50px", 6));

        tabsToPages.put(datosTab, page1);
        tabsToPages.put(equipoTab, page2);
        tabsToPages.put(perfilesTab, page3);

        //   Div pages = new Div(page1, page2, page3);
        page1.add(datoGenericoGrid);
        page2.add(equipoGrid);
        page3.add(aplicacionesPerfiGrid);

        this.contenedorFormulario.add(id);
        this.contenedorFormulario.add(nombre, 5);

        this.contenedorFormulario.add(ambitoCombo, 2);
        this.contenedorFormulario.add(gestionUsuariosComco, 2);
        this.contenedorFormulario.add(fechaInstalacion, 2);

        this.contenedorFormulario.add(proveedorCombo, 3);
        this.contenedorFormulario.add(gfhCombo, 2);
        this.contenedorFormulario.add(estadoCombo);

        this.contenedorFormulario.add(descripcion, 6);

        // contenedorIzquierda.add(aplicacionesPerfiGrid);
        this.contenedorIzquierda.removeAll();
        this.contenedorIzquierda.add(contenedorBotones, contenedorFormulario, tabs, page1, page2, page3);

        this.contenedorBuscadores.add(buscador, gfhcomboBuscador, proveedorComboBuscador, botonImprimirBuscador);
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
            aplicacionBean = event.getItem();
            aplicacionBean.setListaPerfiles(new AplicacionPerfilDao().getLista(null, aplicacionBean));
            aplicacionBean.setListaEquipoBeans(new EquipoAplicacionDao().getLista(null, null, aplicacionBean));
            aplicacionBean.setListaDatosGenerico(new AplicacionesDatosDao().getLista(null, aplicacionBean));
            aplicacionesBinder.readBean(event.getItem());
            doControlBotones(aplicacionBean);
            doActualizaGridPerfiles();
            doActualizaGridEquipos();
            doActualizaGridDatos();
            page1.setVisible(true);
        }
        );

        aplicacionesPerfiGrid.addItemClickListener(event -> {
            AplicacionPerfilBean aplicacionPerfilBean = event.getItem();
            doVentanaPerfil(aplicacionPerfilBean);
        }
        );
        botonPerfiles.addClickListener(event -> {
            AplicacionPerfilBean aplicacionPerfilBean = new AplicacionPerfilBean();
            aplicacionPerfilBean.setAplicacion(aplicacionBean);
            doVentanaPerfil(aplicacionPerfilBean);
        });

        equiposButton.addClickListener(event -> {
            FrmAplicaciónEquipo frmAplicaciónEquipo = new FrmAplicaciónEquipo(aplicacionBean);
            frmAplicaciónEquipo.addDialogCloseActionListener(even3t -> {
                aplicacionBean.setListaEquipoBeans(new EquipoAplicacionDao().getLista(null, null, aplicacionBean));
                doActualizaGridEquipos();
            });
            frmAplicaciónEquipo.addDetachListener(even3t -> {
                aplicacionBean.setListaEquipoBeans(new EquipoAplicacionDao().getLista(null, null, aplicacionBean));
                doActualizaGridEquipos();
            });

            frmAplicaciónEquipo.open();
        });

        datosGenericosButton.addClickListener(event -> {
            /*
            FrmAplicacionDatoGenerico frmAplicacionDatoGenerico = new FrmAplicacionDatoGenerico(aplicacionBean);
            frmAplicacionDatoGenerico.addDialogCloseActionListener(event1 -> {
                aplicacionBean.setListaDatosGenerico(frmAplicacionDatoGenerico.getDatoGenericoBeanArray());
                doActualizaGridDatos();
                page1.setVisible(true);
            });

            frmAplicacionDatoGenerico.addDetachListener(event1 -> {
                aplicacionBean.setListaDatosGenerico(frmAplicacionDatoGenerico.getDatoGenericoBeanArray());
                doActualizaGridDatos();
                page1.setVisible(true);
            });
            frmAplicacionDatoGenerico.open();
             */
            doVentanaDatoGenerico(null);
        });

        datoGenericoGrid.addItemClickListener(event -> {
            DatoGenericoBean datoGenericoBean = event.getItem();
            doVentanaDatoGenerico(datoGenericoBean);
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

        equipoGrid.addItemClickListener(event -> {
            EquipoBean equipoBean = event.getItem();
            EquipoAplicacionBean equipoAplicacionBean = new EquipoAplicacionDao().getPorEquipoAppId(equipoBean, aplicacionBean);
            if (equipoAplicacionBean != null) {
                FrmEquipoAplicacion frmEquipoAplicacion = new FrmEquipoAplicacion("600px", equipoAplicacionBean);
                frmEquipoAplicacion.addDialogCloseActionListener(event1 -> {
                    doActualizaGridEquipos();
                });
                frmEquipoAplicacion.addDetachListener(event2 -> {
                    doActualizaGridEquipos();
                });
                frmEquipoAplicacion.open();
            }
        });

        botonImprimirBuscador.addClickListener(event -> {
            doImprimir(aplicacionesLista);
        });
    }

    public void doVentanaDatoGenerico(DatoGenericoBean datoGenericoBean) {
        FrmAplicacionDatoGenerico frmAplicacionDatoGenerico = new FrmAplicacionDatoGenerico(aplicacionBean, datoGenericoBean);
        frmAplicacionDatoGenerico.addDialogCloseActionListener(event1 -> {
            aplicacionBean.setListaDatosGenerico(frmAplicacionDatoGenerico.getDatoGenericoBeanArray());
            doActualizaGridDatos();
            page1.setVisible(true);
        });

        frmAplicacionDatoGenerico.addDetachListener(event1 -> {
            aplicacionBean.setListaDatosGenerico(frmAplicacionDatoGenerico.getDatoGenericoBeanArray());
            doActualizaGridDatos();
            page1.setVisible(true);
        });
        frmAplicacionDatoGenerico.open();
    }

    public void doVentanaPerfil(AplicacionPerfilBean aplicacionPerfilBean) {
        FrmAplicacionPerfiles frmAplicacionesPerfiles = new FrmAplicacionPerfiles("500px", aplicacionPerfilBean);

        frmAplicacionesPerfiles.addDialogCloseActionListener(event -> {
            doActualizaGrid();
            doActualizaGridPerfiles();
        });
        frmAplicacionesPerfiles.addDetachListener(event -> {
            doActualizaGrid();
            doActualizaGridPerfiles();
        });
        frmAplicacionesPerfiles.open();
    }

}
