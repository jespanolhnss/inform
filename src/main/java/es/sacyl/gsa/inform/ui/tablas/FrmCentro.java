package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroFicheroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.CentroUsuarioBean;
import es.sacyl.gsa.inform.bean.GerenciaBean;
import es.sacyl.gsa.inform.bean.LocalidadBean;
import es.sacyl.gsa.inform.bean.NivelesAtencionBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.ZonaBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.CentroFicheroDao;
import es.sacyl.gsa.inform.dao.CentroUsuarioDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.GerenciaDao;
import es.sacyl.gsa.inform.dao.LocalidadDao;
import es.sacyl.gsa.inform.dao.NivelesAtencionDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author juannietopajares
 */
public final class FrmCentro extends FrmMasterPantalla {

    private static final Logger LOGGER = LogManager.getLogger(FrmCentro.class);

    private final static GerenciaBean GERENCIADEFECTO = new GerenciaDao().getGerenciaDefecto();
    private final Button ficheroBoton = new ObjetosComunes().getBoton(null, null, VaadinIcon.CAMERA.create());
    private final Button usuarioBoton = new ObjetosComunes().getBoton(null, null, VaadinIcon.USERS.create());
    private final VerticalLayout contenedorFotos = new VerticalLayout();
    private final HorizontalLayout contenedorBuscador1 = new HorizontalLayout();
    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<NivelesAtentionTipoBean> nivelAtencionTipoComboBuscador = new CombosUi().getNivelestTipoCombo(null);
    private final ComboBox<NivelesAtencionBean> nivelAtencionComboBuscador = new CombosUi().getNivelAtencionCombo(null);

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final ComboBox<AutonomiaBean> autonomiaCombo = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    //  private final TextField codgeren = new ObjetosComunes().getTextField("CodGeren");
    private final ComboBox<GerenciaBean> gerenciaCombo = new CombosUi().getGerenciaCombo(null, AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO);
    // private final TextField codzona = new ObjetosComunes().getTextField("Cozona");
    private final ComboBox<ZonaBean> zonacombo = new CombosUi().getZonaCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, GERENCIADEFECTO, null);
    private final TextField codigo = new ObjetosComunes().getTextField("Código");
    private final TextField nomcen = new ObjetosComunes().getTextField("Nombre");
    private final TextField nomcorto = new ObjetosComunes().getTextField("Alias");
    private final TextField tipovia = new ObjetosComunes().getTextField("Tipo de via");
    private final TextField callecen = new ObjetosComunes().getTextField("Nombre calle");
    private final IntegerField numcalcen = new ObjetosComunes().getIntegerField("Nº");
    private final TextField otrodircen = new ObjetosComunes().getTextField("otrodircen");
    private final ComboBox<ProvinciaBean> provinciaCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<LocalidadBean> localidadCombo = new CombosUi().getLocalidadCombo(null, AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null);
    private final TextField cpcentro = new ObjetosComunes().getTextField("Cpcentro");
    private final TextField teleprev = new ObjetosComunes().getTextField("teleprev");
    private final ComboBox<CentroTipoBean> centroTipoCombo = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<NivelesAtencionBean> nivelesAtencionCombo = new CombosUi().getNivelAtencionCombo(null);
    private final ComboBox<NivelesAtentionTipoBean> nivelAtencionTipoCombo = new CombosUi().getNivelestTipoCombo(null);
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();
    private final TextField mapgoogle = new ObjetosComunes().getTextField("Google Map");
    private final Image googleMapsImage = new Image("icons/googlemap.jpg", "Mapa");

    private CentroBean centroBean = null;
    private final Binder<CentroBean> centroBinder = new Binder<>();
    private final PaginatedGrid<CentroBean> centroGrid = new PaginatedGrid<>();
    private final PaginatedGrid<CentroFicheroBean> centroFicheroGrid = new PaginatedGrid<>();
    private final PaginatedGrid<CentroUsuarioBean> centroUsuarioGrid = new GridUi().getCentroUsuarioPaginateGrid();

    private ArrayList<CentroBean> centroTArrayList = new ArrayList<>();

    //   private final ArrayList<CentroFicheroBean> centroFicheroArrayList = new ArrayList<>();
    private int miniaturasConntador = 0;
    private final int miniaturasPorFila = 5;
    private HorizontalLayout miniaturasHorizontalLayout = new HorizontalLayout();

    private final String miniaturaHeight = "150px";
    private final String miniaturaWidth = "150px";

    // componentes para gestionar los tabs de la parte inferior
    private final Tab miniaturasTab = new Tab("Miniaturas");
    private final Tab ficherosTab = new Tab("Ficheros");
    private final Tab usuariosTab = new Tab("Usuarios");
    private final Tabs tabs = new Tabs(miniaturasTab, ficherosTab, usuariosTab);
    private final Map<Tab, Component> tabsToPages = new HashMap<>();
    private final Div page1 = new Div();
    private final Div page2 = new Div();
    private final Div page3 = new Div();

    public FrmCentro() {
        super();
        this.centroBean = new CentroBean();
        this.id.setValue("0");
        // googleMapsImage.setWidth("25px");
        // googleMapsImage.setHeight("19px");
        doComponentesOrganizacion();
        doGrid();
        doGridCentroFichero();
        doGridCentroUsuarios();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            codigo.setEnabled(true);
            ficheroBoton.setEnabled(false);
            usuarioBoton.setEnabled(false);
            codigo.focus();
        } else {
            // codigo.setEnabled(false);
            codigo.setReadOnly(true);
            ficheroBoton.setEnabled(true);
            usuarioBoton.setEnabled(true);
        }
    }

    @Override
    public void doGrabar() {
        if (centroBinder.writeBeanIfValid(centroBean)) {
            if (new CentroDao().doGrabaDatos(centroBean, null) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<CentroBean> validate = centroBinder.validate();
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
                    new CentroDao().doBorraDatos(centroBean);
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
        centroBean = new CentroBean();
        centroBinder.readBean(centroBean);
        autonomiaCombo.setValue(AutonomiaBean.AUTONOMIADEFECTO);
        provinciaCombo.setValue(ProvinciaBean.PROVINCIA_DEFECTO);
        gerenciaCombo.setValue(GERENCIADEFECTO);

        // borra el contenido de los tabs
        centroUsuarioGrid.setItems(new ArrayList<>());
        doActualizaGridCentroUsuario();
        centroFicheroGrid.setItems(new ArrayList<>());
        doActualizaGridCentroFichero();
        miniaturasHorizontalLayout.removeAll();
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        centroGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        centroGrid.setHeightByRows(true);
        centroGrid.setPageSize(14);
        centroGrid.addColumn(CentroBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Act</b>"));
        centroGrid.addColumn(CentroBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Cód</b>"));
        centroGrid.addColumn(CentroBean::getTipocentroString).setAutoWidth(true).setHeader(new Html("<b>T</b>"));
        centroGrid.addColumn(CentroBean::getNomcenCorto).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        centroGrid.addColumn(CentroBean::getLocalidadCortoString).setAutoWidth(true).setHeader(new Html("<b>Localidad</b>"));
        doActualizaGrid();
    }

    public void doGridCentroUsuarios() {
        centroUsuarioGrid.addComponentColumn(item -> createRemoveButtonU(centroUsuarioGrid, item))
                .setHeader("Borra");
    }

    private Button createRemoveButtonU(Grid<CentroUsuarioBean> grid, CentroUsuarioBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.MINUS_CIRCLE.create(), clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        new CentroUsuarioDao().doBorraDatos(item);
                        doActualizaGridCentroUsuario();
                    });
            dialog.open();
        });
        return button;
    }

    public void doGridCentroFichero() {
        centroFicheroGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        centroFicheroGrid.setHeightByRows(true);
        centroFicheroGrid.setPageSize(6);
        centroFicheroGrid.setPaginatorSize(16);
        centroFicheroGrid.addColumn(CentroFicheroBean::getFechaCambioFormato).setAutoWidth(true).setHeader(new Html("<b>Fecha</b>"));
        centroFicheroGrid.addColumn(CentroFicheroBean::getDescripcionCorta).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        centroFicheroGrid.addColumn(CentroFicheroBean::getExtensionFichero).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));
        centroFicheroGrid.addColumn(new ComponentRenderer<>(centroFicheroBean -> {
            Image img = null;
            switch (centroFicheroBean.getExtensionFichero()) {
                case ".pdf":
                    img = new Image("icons/pdf.png", "img");
                    break;
                case ".gif":
                case ".jpg":
                case ".png":
                case ".jpeg":
                    img = getCentroFicheroImage(centroFicheroBean);
                    break;
            }
            if (img == null) {
                img = new Image("icons/icon.png", "img");
            }
            img.setWidth("25px");
            img.setHeight("25px");
            return img;

        })).setHeader("Img");
        centroFicheroGrid.addComponentColumn(item -> createRemoveButton(centroFicheroGrid, item))
                .setHeader("Borra");
    }

    /**
     *
     * @param grid
     * @param item
     * @return crea el boton de borrado en el grid de centrofichero con el
     * evento asociado
     */
    private Button createRemoveButton(Grid<CentroFicheroBean> grid, CentroFicheroBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.MINUS_CIRCLE.create(), clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        new CentroFicheroDao().doBorraDatos(item);
                        //    Notification.show(FrmMensajes.AVISODATOBORRADO);
                        doActualizaGridCentroFichero();
                    });
            dialog.open();
        });
        return button;
    }

    /**
     * Actualiza grid de la lista de ficheros que se muestra en el tabs
     */
    public void doActualizaGridCentroFichero() {
        ArrayList<CentroFicheroBean> centroFicheroBeans = new CentroFicheroDao().getLista(centroBean);
        centroBean.setCentroFicheroArrayList(centroFicheroBeans);
        centroFicheroGrid.setItems(centroFicheroBeans);
        doMuestraMiniaturas();
        ficherosTab.setLabel("Ficheros (" + Integer.toString(centroFicheroBeans.size()) + ")");
    }

    /**
     * Actualiza el grid de usuario que se muestra en el tab
     */
    public void doActualizaGridCentroUsuario() {
        ArrayList<CentroUsuarioBean> centroUsuarioBeans = new CentroUsuarioDao().getLista(centroBean);
        centroBean.setCentroUsuarioArrayList(centroUsuarioBeans);
        centroUsuarioGrid.setItems(centroUsuarioBeans);
        usuariosTab.setLabel("Usuario (" + Integer.toString(centroUsuarioBeans.size()) + ")");
    }

    /**
     * Actualiza grid de centros de la derecha
     */
    @Override
    public void doActualizaGrid() {
        centroTArrayList = new CentroDao().getLista(buscador.getValue(), autonomiaComboBuscador.getValue(), provinciaComboBuscador.getValue(), null, nivelAtencionComboBuscador.getValue(), centroTipoComboBuscador.getValue(), nivelAtencionTipoComboBuscador.getValue(), null);
        centroGrid.setItems(centroTArrayList);
    }

    /**
     *
     */
    @Override
    public void doBinderPropiedades() {

        centroBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(CentroBean::getId, null);

        centroBinder.forField(autonomiaCombo)
                .asRequired()
                .bind(CentroBean::getAutonomia, CentroBean::setAutonomia);

        centroBinder.forField(gerenciaCombo)
                .asRequired()
                .bind(CentroBean::getGerencia, CentroBean::setGerencia);

        centroBinder.forField(zonacombo)
                .bind(CentroBean::getZona, CentroBean::setZona);

        centroBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 2))
                .bind(CentroBean::getCodigo, CentroBean::setCodigo);

        centroBinder.forField(nomcen)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 65))
                .bind(CentroBean::getNomcen, CentroBean::setNomcen);

        centroBinder.forField(nomcorto)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 19))
                .bind(CentroBean::getNomcenCorto, CentroBean::setNomcorto);

        centroBinder.forField(tipovia)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 5))
                .bind(CentroBean::getTipovia, CentroBean::setTipovia);

        centroBinder.forField(callecen)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 65))
                .bind(CentroBean::getCallecen, CentroBean::setCallecen);

        centroBinder.forField(numcalcen)
                .asRequired()
                .bind(CentroBean::getNumcalcen, CentroBean::setNumcalcen);

        centroBinder.forField(otrodircen)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(CentroBean::getOtrdirecen, CentroBean::setOtrdirecen);
        centroBinder.forField(localidadCombo)
                .asRequired()
                .bind(CentroBean::getLocalidad, CentroBean::setLocalidad);

        centroBinder.forField(cpcentro)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(CentroBean::getCpcentro, CentroBean::setCpcentro);

        centroBinder.forField(teleprev)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(CentroBean::getTeleprev, CentroBean::setTeleprev);

        centroBinder.forField(centroTipoCombo)
                .asRequired()
                .bind(CentroBean::getTipocentro, CentroBean::setTipocentro);

        centroBinder.forField(nivelesAtencionCombo)
                .asRequired()
                .bind(CentroBean::getNivatencion, CentroBean::setNivatencion);

        centroBinder.forField(estadoRadio)
                .bind(CentroBean::getEstadoString, CentroBean::setEstado);

        centroBinder.forField(mapgoogle)
                .bind(CentroBean::getMapgoogle, CentroBean::setMapgoogle);
    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Centros");
        buscador.setLabel(" Valores a buscar");
        nomcorto.setMaxLength(20);
        nomcorto.setMaxWidth("75px");
        // como se hace borrado lógico con modificar el registro el centro y poner activo=N es lo mismo
        botonBorrar.setVisible(false);
        miniaturasTab.setLabel("Miniatruas");
        ficherosTab.setLabel("Ficheros");
        usuariosTab.setLabel("Usuarios");

        googleMapsImage.setVisible(false);
        page1.setWidthFull();
        page2.setWidthFull();
        page3.setWidthFull();

        miniaturasTab.setVisible(true);
        ficherosTab.setVisible(true);
        usuariosTab.setVisible(true);

        page1.setVisible(false);
        page2.setVisible(false);
        page3.setVisible(false);
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

        page1.add(contenedorFotos);
        page2.add(centroFicheroGrid);
        page3.add(centroUsuarioGrid);

        tabsToPages.put(miniaturasTab, page1);
        tabsToPages.put(ficherosTab, page2);
        tabsToPages.put(usuariosTab, page3);
        //   Div pages = new Div(page1, page2, page3);
        page1.add(miniaturasHorizontalLayout);
        page2.add(centroFicheroGrid);
        page3.add(centroUsuarioGrid);

        this.contenedorIzquierda.removeAll();
        this.contenedorIzquierda.add(contenedorBotones, contenedorFormulario, tabs, page1, page2, page3);
        this.contenedorBotones.add(botonImprimir, ficheroBoton, usuarioBoton);

        // fila 1
        contenedorFormulario.add(id);
        contenedorFormulario.add(autonomiaCombo, 3);
        contenedorFormulario.add(provinciaCombo, 1);
        contenedorFormulario.add(codigo);

        contenedorFormulario.add(gerenciaCombo, 2);
        contenedorFormulario.add(zonacombo, 2);
        contenedorFormulario.add(localidadCombo, 2);

        contenedorFormulario.add(nomcen, 3);
        contenedorFormulario.add(nomcorto, teleprev);

        contenedorFormulario.add(tipovia);
        contenedorFormulario.add(callecen, 3);
        contenedorFormulario.add(numcalcen);
        contenedorFormulario.add(cpcentro);

        contenedorFormulario.add(centroTipoCombo, 2);
        contenedorFormulario.add(nivelAtencionTipoCombo, 2);
        contenedorFormulario.add(nivelesAtencionCombo, 2);

        contenedorFormulario.add(estadoRadio, 2);

        contenedorFormulario.add(mapgoogle, 3);
        contenedorFormulario.add(googleMapsImage);

        // Elementos del buscador y grid
        this.contenedorDerecha.removeAll();
        this.contenedorBuscadores.add(buscador, autonomiaComboBuscador, provinciaComboBuscador);
        this.contenedorBuscador1.add(centroTipoComboBuscador, nivelAtencionTipoComboBuscador);
        this.contenedorDerecha.add(this.contenedorBuscadores, contenedorBuscador1, centroGrid);

    }

    @Override
    public void doCompentesEventos() {

        autonomiaCombo.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComboProvinicas(provinciaCombo, autonomiaBean);
        });

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });
        autonomiaComboBuscador.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComboProvinicas(provinciaComboBuscador, autonomiaBean);
        });

        provinciaCombo.addValueChangeListener(event -> {
            localidadCombo.setItems(new LocalidadDao().getLista(null, autonomiaCombo.getValue(), provinciaCombo.getValue()));
            nivelesAtencionCombo.setItems(new NivelesAtencionDao().getLista(null, nivelAtencionTipoCombo.getValue(), provinciaCombo.getValue(), ConexionDao.BBDD_ACTIVOSI));
        });

        provinciaComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        centroTipoComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        nivelAtencionTipoComboBuscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        nivelAtencionTipoCombo.addBlurListener(event -> {
            nivelesAtencionCombo.clear();
            nivelesAtencionCombo.setItems(new NivelesAtencionDao().getLista(null, nivelAtencionTipoCombo.getValue(), provinciaCombo.getValue(), ConexionDao.BBDD_ACTIVOSI));

        });
        centroGrid.addItemClickListener(event -> {
            centroBean = event.getItem();
            centroBinder.readBean(centroBean);

            if (centroBean.getMapgoogle() != null && !centroBean.getMapgoogle().isEmpty()) {
                googleMapsImage.setVisible(true);
                googleMapsImage.addClickListener(eventimg -> {
                    Page page = new Page(getUI().get());
                    page.open(centroBean.getMapgoogle(), "_blank");
                });

            } else {
                googleMapsImage.setVisible(false);
            }
            // en nivel no es un campo del bean de centro y por eso se actualiza a mano
            nivelAtencionTipoCombo.setValue(centroBean.getNivatencion().getTipo());
            //los ficheros  asociados no se cargan en la lista
            centroBean.setCentroFicheroArrayList(new CentroFicheroDao().getLista(centroBean));
            doActualizaGridCentroFichero();
            doActualizaGridCentroUsuario();
            page1.setVisible(true);
        }
        );

        ficheroBoton.addClickListener(event -> {
            doVentanaFicheros(null);
        });

        usuarioBoton.addClickListener(event -> {
            FrmCentroUsuario frmCentroUsuario = new FrmCentroUsuario(centroBean);
            frmCentroUsuario.setCloseOnEsc(true);
            frmCentroUsuario.setCloseOnOutsideClick(true);
            frmCentroUsuario.setModal(true);
            frmCentroUsuario.addDialogCloseActionListener(e -> {
                doActualizaGridCentroUsuario();
            });
            frmCentroUsuario.addDetachListener(e -> {
                doActualizaGridCentroUsuario();
            });
            frmCentroUsuario.open();
        });

        centroFicheroGrid.addItemClickListener(event -> {
            CentroFicheroBean centroFicheroBean = event.getItem();
            doVentanaFicheros(centroFicheroBean);
            //  getMinitaura(centroFicheroBean);
        }
        );

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

    }

    public void doVentanaFicheros(CentroFicheroBean centroFicheroBean) {
        FrmCentroFichero frmCentroFichero;
        if (centroFicheroBean == null) {
            frmCentroFichero = new FrmCentroFichero(centroBean);
        } else {
            frmCentroFichero = new FrmCentroFichero(centroFicheroBean);
        }
        frmCentroFichero.setCloseOnEsc(true);
        frmCentroFichero.setCloseOnOutsideClick(true);
        frmCentroFichero.setModal(true);
        frmCentroFichero.addDialogCloseActionListener(e -> {
            doActualizaGridCentroFichero();
        });
        frmCentroFichero.addDetachListener(e -> {
            doActualizaGridCentroFichero();
        });
        frmCentroFichero.open();
    }

    public void doActualizaComboProvinicas(ComboBox<ProvinciaBean> combo, AutonomiaBean autonomia) {
        ArrayList<ProvinciaBean> privinciaArrayList = new ProvinciaDao().getLista(null, autonomia);
        combo.setItems(privinciaArrayList);
    }

    public void doMuestraMiniaturas() {
        miniaturasConntador = 0;
        contenedorFotos.removeAll();
        Image image = null;
        for (CentroFicheroBean registro : centroBean.getCentroFicheroArrayList()) {
            if (miniaturasConntador == 0) {
                miniaturasHorizontalLayout = new HorizontalLayout();
                contenedorFotos.add(miniaturasHorizontalLayout);
            }
            //  image = registro.getImagen();
            LOGGER.debug("Path relativo" + registro.getPathRelativoMiniatura());
            LOGGER.debug("Fichero nombre" + registro.getNombreFicheroMiniatura());
            //  System            .out.println(registro.getPathRelativoMiniatura());
            //    System.out.println(registro.getNombreFicheroMiniatura());
            image = new Image(registro.getPathRelativoMiniatura(), registro.getNombreFicheroMiniatura());
            if (image != null) {
                image.setHeight(miniaturaHeight);
                image.setWidth(miniaturaWidth);
                if (registro.getDescripcion() != null) {
                    image.setText(registro.getDescripcion());
                    image.setAlt(registro.getDescripcion());
                }
                image.addClickListener(event -> {
                    Page page = new Page(getUI().get());
                    page.open(registro.getPathRelativo(), "_blank");
                });
                miniaturasHorizontalLayout.add(image);
                miniaturasConntador++;
                if (miniaturasConntador > miniaturasPorFila) {
                    miniaturasConntador = 0;
                }
            }
        }
    }

    /**
     * Muestra las miniaturas de los ficheros asociados para los ficheros pdf
     * genera el jpeg con el método getMinitauraPdf para los jpeg, jpg, png
     */
    public void doMuestraMiniaturas1() {
        miniaturasConntador = 0;
        contenedorFotos.removeAll();
        Image image = null;
        for (CentroFicheroBean registro : centroBean.getCentroFicheroArrayList()) {
            if (miniaturasConntador == 0) {
                miniaturasHorizontalLayout = new HorizontalLayout();
                contenedorFotos.add(miniaturasHorizontalLayout);
            }
            image = getCentroFicheroImage(registro);
            image.setHeight(miniaturaHeight);
            image.setWidth(miniaturaWidth);
            image.setText(registro.getDescripcion());
            image.setAlt(registro.getDescripcion());
            image.addClickListener(event -> {
                Page page = new Page(getUI().get());
                page.open(registro.getPathRelativo(), "_blank");
            });
            miniaturasHorizontalLayout.add(image);
            miniaturasConntador++;
            if (miniaturasConntador > miniaturasPorFila) {
                miniaturasConntador = 0;
            }
        }
    }

    public static Image getCentroFicheroImage(CentroFicheroBean centroFicheroBean) {
        Image image = null;
        switch (centroFicheroBean.getExtensionFichero()) {
            case ".pdf":
                String nombre = Constantes.PDFPATHRELATIVO + centroFicheroBean.getNombreFicheroNoExtension() + Constantes.MINIATURAEXTENSION;
//                image = new Image(Constantes.PDFPATHRELATIVO + getMinitauraPdf(centroFicheroBean), centroFicheroBean.getNombre());
                image = new Image(nombre, centroFicheroBean.getNombreFichero());
                break;
            case ".gif":
            case ".jpg":
            case ".png":
            case ".jpeg":
                image = new Image(centroFicheroBean.getPathRelativo(), centroFicheroBean.getNombreFichero());
                break;
        }
        return image;

    }

    private String getMinitauraPdf(CentroFicheroBean centroFicheroBean) {
        return null;
    }

    /**
     * Muestra las miniaturas de los pdf asociados al centro Utiliza los
     * atributos contadorMiniaturas,filaMiniaturas de modo que continua con la
     * secuencia si se han mostrado fotos previamente
     */
    /*
    public void doMuestraPdf() {
        ArrayList<CentroFicheroBean> listaRegistros = centroBean.getCentroFicheroArrayListPdf();
        if (listaRegistros.size() > 0) {
            for (CentroFicheroBean registro : listaRegistros) {
                if (contadorMiniaturas == 0) {
                    filaMiniaturas = new HorizontalLayout();
                    contenedorFotos.add(filaMiniaturas);
                }
                String nombre = Constantes.PDFPATHRELATIVO + getMinitauraPdf(registro);
                Image image = new Image(nombre, registro.getNombre());

                image.setHeight(miniaturaHeight);
                image.setWidth(miniaturaWidth);
                image.addClickListener(event -> {
                    Page page = new Page(getUI().get());
                    page.open(registro.getPathRelativo(), "_blank");
                });
                filaMiniaturas.add(image);
                contadorMiniaturas++;
                if (contadorMiniaturas == 5) {
                    contadorMiniaturas = 0;
                }
            }
        }

    }
     */
    /**
     *
     * @param centroFicheroBean El centroFicheroBean del que queremos hacer la
     * miniatrua es un fichero con extensión pdf viene ya filtrados
     * @return en nombre del fichero generado
     *
     * el método está copia de
     * https://www.tutorialspoint.com/pdfbox/pdfbox_extracting_image.htm
     */
    /*
    public String getMinitauraPdf(CentroFicheroBean centroFicheroBean) {
        //
        String salida = null;
        String nombre = centroFicheroBean.getNombreFicheroNoExtension() + ".jpg";
        try {
            String filename = centroFicheroBean.getPathAbsoluto();
            Utilidades.bloBtoFile(centroFicheroBean.getFicheroBlobs(), filename);
            File file = new File(filename);
            salida = Constantes.PDFPATHABSOLUTO + nombre;
            PDDocument document = null;
            document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0);
            ImageIO.write(image, Constantes.MINIATURAEXTENSION, new File(salida));

            System.out.println("Image created" + salida);

            document.close();
        } catch (IOException ex) {
            Logger.getLogger(FrmCentro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombre;
    }
     */
}
