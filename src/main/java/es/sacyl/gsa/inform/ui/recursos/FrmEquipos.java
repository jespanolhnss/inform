package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.EquipoAplicacionBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.EquipoAplicacionDao;
import es.sacyl.gsa.inform.dao.EquipoDao;
import es.sacyl.gsa.inform.dao.ParametroDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.dao.UbicacionDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmEquipos extends FrmMasterPantalla {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(FrmEquipos.class);
    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<CentroBean> centroComboBuscador = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final Button ayudaUbicacion = new ObjetosComunes().getBotonMini();
    private final Button ayudaIp = new ObjetosComunes().getBotonMini();
    private final Button aplicacionButton = new ObjetosComunes().getBoton("App", null, VaadinIcon.FILE_TABLE.create());
    private final Button datosGenericosButton = new ObjetosComunes().getBoton("Dat", null, VaadinIcon.TABLE.create());

    private final Button etiquetaButton = new ObjetosComunes().getBoton(null, null, VaadinIcon.BARCODE.create());

    private final ComboBox<String> equipoTipoComboBuscador = new CombosUi().getEquipoTipoCombo(null, 50);

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final ComboBox<String> equipoTipoCombo = new CombosUi().getEquipoTipoCombo(null, 50);
    private final ComboBox<String> equipoMarcaCombo = new CombosUi().getGrupoRamaComboValor(ComboBean.TIPOEQUIPOMARCA, equipoTipoCombo.getValue(), null, "Marca");

    private final TextField inventario = new ObjetosComunes().getTextField("Inventario");
    //  private final TextField marca = new ObjetosComunes().getTextField("Marca");
    private final TextField modelo = new ObjetosComunes().getTextField("Modelo");
    private final TextField numeroSerie = new ObjetosComunes().getTextField("N.Serie");
    private final TextField macAdress = new ObjetosComunes().getTextField("Mac");
    private final TextField nombredominio = new ObjetosComunes().getTextField("Nombre");
    private final TextField dni = new ObjetosComunes().getDni();
    private final TextField nombreusuario = new ObjetosComunes().getTextField("Nombre Usuario habitual");

    private final ComboBox<CentroBean> centroCombo = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final ComboBox<UbicacionBean> ubicacionCombo = new CombosUi().getUbicacionCombo(null, centroCombo.getValue(), null, null);
    private final TextField ubicacion = new ObjetosComunes().getTextField("Ubicación");
    //  private final TextField servicio = new ObjetosComunes().getTextField("Servicio");
    private final ComboBox<GfhBean> servicioCombo = new CombosUi().getServicioCombo(null, null);
    private final TextField ip = new ObjetosComunes().getTextField("Ip");
    private final TextField comentario = new ObjetosComunes().getTextField("Comentario");

    private UsuarioBean usuarioHabitual = null;
    private EquipoBean equipoBean = null;
    private final Binder<EquipoBean> equipoBinder = new Binder<>();
    private final PaginatedGrid<EquipoBean> equipoGrid = new GridUi().getEquipoGridPaginado();
    private ArrayList<EquipoBean> equipoArrayList = new ArrayList<>();

    private final PaginatedGrid<EquipoAplicacionBean> equipoAplicacionGrid = new PaginatedGrid<>();
    private final PaginatedGrid<DatoGenericoBean> datosGenericosGrid = new GridUi().getDatosGenericosGridPaginado();

    // componentes para gestionar los tabs de la parte inferior
    private final Tab datosGenericosTab = new Tab("Datos");
    private final Tab aplicacionesTab = new Tab("Aplicaciones");
    private final Tab intervencinesTab = new Tab("Intervenciones");

    private final Tabs tabs = new Tabs(datosGenericosTab, aplicacionesTab, intervencinesTab);
    private final Map<Tab, Component> tabsToPages = new HashMap<>();
    private final Div page1 = new Div();
    private final Div page2 = new Div();
    private final Div page3 = new Div();

    public FrmEquipos() {
        super();
        this.equipoBean = new EquipoBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doControlBotones(null);
    }

    /**
     *
     * @param obj
     */
    public void doControlBotones(EquipoBean obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            aplicacionButton.setEnabled(false);
            datosGenericosButton.setEnabled(false);
            aplicacionesTab.setEnabled(false);
            page1.setVisible(false);
            etiquetaButton.setEnabled(false);
        } else {
            datosGenericosButton.setEnabled(true);
            etiquetaButton.setEnabled(true);
            if (obj.getTipo().equals(EquipoBean.TIPOCPU)) {
                aplicacionButton.setEnabled(true);
                aplicacionesTab.setEnabled(true);
            } else {
                aplicacionButton.setEnabled(false);
                aplicacionesTab.setEnabled(false);
                page1.setVisible(false);
            }
        }
    }

    /**
     *
     */
    @Override
    public void doGrabar() {
        if (equipoBinder.writeBeanIfValid(equipoBean)) {
            equipoBean.setUsuario(usuarioHabitual);
            equipoBean.setValoresAut();
            if (new EquipoDao().doGrabaDatos(equipoBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<EquipoBean> validate = equipoBinder.validate();
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
                    equipoBean.setEstado(ConexionDao.BBDD_ACTIVONO);
                    equipoBean.setValoresAut();
                    new EquipoDao().doBorraDatos(equipoBean);
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
        equipoBean = new EquipoBean();
        // repitimos valores mas frecuentes
        equipoBean.setCentro(centroCombo.getValue());
        equipoBean.setTipo(equipoTipoCombo.getValue());
        equipoBean.setMarca(equipoMarcaCombo.getValue());
        equipoBean.setModelo(modelo.getValue());
        equipoBinder.readBean(equipoBean);
        doControlBotones(null);
        doActualizaGridAplicacion();
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        doActualizaGrid();
        // grid eqipo aplicación
        equipoAplicacionGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        equipoAplicacionGrid.setHeightByRows(true);
        equipoAplicacionGrid.addColumn(EquipoAplicacionBean::getApliacacionString).setAutoWidth(true).setHeader(new Html("<b>Aplicación</b>")).setWidth("70px");
        equipoAplicacionGrid.addColumn(EquipoAplicacionBean::getFecha).setAutoWidth(true).setHeader(new Html("<b>Fecha</b>")).setWidth("70px");
        equipoAplicacionGrid.addComponentColumn(item -> createRemoveButton(equipoAplicacionGrid, item))
                .setHeader("Borra");
    }

    public void doGridDatosGenericos() {

    }

    private Button createRemoveButton(PaginatedGrid<EquipoAplicacionBean> grid, EquipoAplicacionBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.MINUS_CIRCLE.create(), clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        new EquipoAplicacionDao().doBorraDatos(item);
                        doActualizaGridAplicacion();
                    });
            dialog.open();
        });
        return button;
    }

    @Override
    public void doActualizaGrid() {
        equipoArrayList = new EquipoDao().getLista(buscador.getValue(), equipoTipoComboBuscador.getValue(),
                centroComboBuscador.getValue(), null, null);
        equipoGrid.setItems(equipoArrayList);
    }

    public void doActualizaGridAplicacion() {
        ArrayList<EquipoAplicacionBean> equipoAplicacionArrayList = new EquipoAplicacionDao().getLista(null, equipoBean, null);
        equipoBean.setAplicacinesArrayList(equipoAplicacionArrayList);
        equipoAplicacionGrid.setItems(equipoAplicacionArrayList);
        equipoAplicacionGrid.setHeightByRows(true);
        if (equipoAplicacionArrayList.size() > 0) {
            equipoAplicacionGrid.setPageSize(equipoAplicacionArrayList.size());
        }
    }

    public void doActualizaGridDatosGenericos() {
        datosGenericosGrid.setItems(equipoBean.getDatosGenericoBeans());
        datosGenericosGrid.setHeightByRows(true);
        if (equipoBean.getDatosGenericoBeans().size() > 0) {
            datosGenericosGrid.setPageSize(equipoBean.getDatosGenericoBeans().size());
        }
    }

    @Override
    public void doBinderPropiedades() {
        equipoBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(EquipoBean::getId, null);

        equipoBinder.forField(equipoTipoCombo)
                .withNullRepresentation("")
                .asRequired()
                .bind(EquipoBean::getTipo, EquipoBean::setTipo);

        equipoBinder.forField(inventario)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(EquipoBean::getInventario, EquipoBean::setInventario);

        equipoBinder.forField(equipoMarcaCombo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(EquipoBean::getMarca, EquipoBean::setMarca);

        equipoBinder.forField(modelo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(EquipoBean::getModelo, EquipoBean::setModelo);

        equipoBinder.forField(numeroSerie)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(EquipoBean::getNumeroSerie, EquipoBean::setNumeroSerie);

        equipoBinder.forField(macAdress)
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 17))
                .bind(EquipoBean::getMacadress, EquipoBean::setMacadress);

        equipoBinder.forField(ip)
                .withNullRepresentation("")
                .bind(EquipoBean::getIpsCadena, null);

        equipoBinder.forField(comentario)
                .withNullRepresentation("")
                .bind(EquipoBean::getComentario, EquipoBean::setComentario);

        equipoBinder.forField(centroCombo)
                .asRequired()
                .bind(EquipoBean::getCentro, EquipoBean::setCentro);

        equipoBinder.forField(ubicacionCombo)
                .bind(EquipoBean::getUbicacion, EquipoBean::setUbicacion);
    }

    @Override
    public void doComponenesAtributos() {
        autonomiaComboBuscador.setVisible(Boolean.FALSE);
        ubicacionCombo.setLabel("Ubicación");
        buscador.setLabel("Valores a buscar");

        page1.setWidthFull();
        page2.setWidthFull();
        page3.setWidthFull();

        datosGenericosTab.setVisible(true);
        aplicacionesTab.setVisible(true);
        intervencinesTab.setVisible(true);

        page1.setVisible(false);
        page2.setVisible(false);
        page3.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorBotones.add(aplicacionButton, datosGenericosButton, etiquetaButton);
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4),
                new FormLayout.ResponsiveStep("50px", 5),
                new FormLayout.ResponsiveStep("50px", 6));

        page1.add(datosGenericosGrid);
        page2.add(equipoAplicacionGrid);
        //  page3.add(equipoAplicacionGrid);

        tabsToPages.put(datosGenericosTab, page1);
        tabsToPages.put(aplicacionesTab, page2);
        tabsToPages.put(intervencinesTab, page3);

        contenedorIzquierda.add(tabs, page1, page2, page3);

        contenedorDerecha.removeAll();
        contenedorBuscadores.add(equipoTipoComboBuscador, autonomiaComboBuscador, provinciaComboBuscador, centroTipoComboBuscador, centroComboBuscador);
        contenedorDerecha.add(this.contenedorBuscadores, equipoGrid);

        contenedorFormulario.add(id, 2);
        contenedorFormulario.add(equipoTipoCombo, 2);
        contenedorFormulario.add(equipoMarcaCombo, 2);

        contenedorFormulario.add(inventario, 2);
        contenedorFormulario.add(modelo, 2);
        contenedorFormulario.add(numeroSerie, 2);

        contenedorFormulario.add(ip, 3);
        contenedorFormulario.add(ayudaIp);
        contenedorFormulario.add(macAdress);
        contenedorFormulario.add(nombredominio);

        contenedorFormulario.add(centroCombo, 3);
        contenedorFormulario.add(servicioCombo, 3);

        contenedorFormulario.add(ubicacionCombo, 5);
        contenedorFormulario.add(ayudaUbicacion);
        contenedorFormulario.add(dni);
        contenedorFormulario.add(nombreusuario, 5);

        contenedorFormulario.add(comentario, 6);

    }

    @Override
    public void doCompentesEventos() {
        /**
         * Esta campo esta inicialmente oculto y este evento no debería ocurrir
         * pero por si en algún momento se hace visible para mantener el
         * funcionamiento de la lógica de la pantalla
         */
        autonomiaComboBuscador.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComboProvinicas(provinciaComboBuscador, autonomiaBean);
        });

        /**
         * Cuando se cambia el valor en el combo de tipo de equipo se actualiza
         * la lista de valores del grid
         *
         */
        equipoTipoComboBuscador.addValueChangeListener(evetn -> {
            doActualizaGrid();
        });

        /**
         * Si cambia el valor del combo de tipo de equipo, actualiza el combo de
         * marcas.
         *
         */
        equipoTipoCombo.addValueChangeListener(event -> {
            equipoMarcaCombo.setItems(new ComboDao().getListaGruposRamaValor(ComboBean.TIPOEQUIPOMARCA, event.getValue(), 100));
            if (equipoTipoCombo.getValue().equals(EquipoBean.TIPOCPU) || equipoTipoCombo.getValue().equals(EquipoBean.TIPOTELEFONO)) {
                dni.setVisible(true);
                nombreusuario.setVisible(true);
            } else {
                dni.setVisible(false);
                nombreusuario.setVisible(false);
            }
        });

        /**
         * Si cambia la provinca en el combo buscador actualiza el combo de
         * centros
         */
        provinciaComboBuscador.addValueChangeListener(event -> {
            doActualizaComboCentro();
        });

        /**
         * Si cambia en tipo de centro en el combo actualiza el combo de centros
         */
        centroTipoComboBuscador.addValueChangeListener(event -> {
            doActualizaComboCentro();
        });

        /**
         * Si cambia el valor en el combo de centros, actualiza el grid con la
         * lista de equipos asociados a ese centros
         *
         */
        centroComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        /**
         * Si cambia el valor del combo de centro en el formulario actualiza los
         * valores de combo hijo de ubicaciones.
         */
        centroCombo.addValueChangeListener(event -> {
            ubicacionCombo.setItems(new UbicacionDao().getLista(null, event.getValue(), null));
            doActualizaGrid();
        });

        /**
         * Cuado seleccina un equipo en e grid, actualiza el bean, hace visibles
         * los botones, actualiza los valores del formulario, actualiza las
         * aplicaciones asociada y los datos genéricos del equipos
         */
        equipoGrid.addItemClickListener(event -> {
            equipoBean = new EquipoBean();
            equipoBean = event.getItem();
            equipoBinder.readBean(event.getItem());
            // Estos datos sólo los carga cuando hace clic en el grid
            equipoBean.setDatosGenericoBeans(new EquipoDao().getListaDatosGenericos(equipoBean));
            doControlBotones(equipoBean);
            doActualizaGridAplicacion();
            doActualizaGridDatosGenericos();
            page1.setVisible(true);
            if (equipoBean.getTipo().equals(EquipoBean.TIPOCPU) || equipoBean.getTipo().equals(EquipoBean.TIPOTELEFONO)) {
                dni.setVisible(true);
                nombreusuario.setVisible(true);
            } else {
                dni.setVisible(false);
                nombreusuario.setVisible(false);
            }
        }
        );

        /**
         * Ventana de ayuda para elegir ubicación
         */
        ayudaUbicacion.addClickListener(event -> {
            if (centroCombo.getValue() != null) {
                FrmBuscaUbicacion frmBuscaUbicacion = new FrmBuscaUbicacion(centroCombo.getValue());
                frmBuscaUbicacion.addDialogCloseActionListener(eventAyuda -> {
                    ubicacionCombo.setValue(frmBuscaUbicacion.getUbicacionBean());
                });
                frmBuscaUbicacion.addDetachListener(eventAyuda -> {
                    ubicacionCombo.setValue(frmBuscaUbicacion.getUbicacionBean());
                });
                frmBuscaUbicacion.open();
            } else {
                Notification.show("Debes elegir centro");
            }
        });

        /**
         * Ventana de ayuda para elegir IP
         */
        ayudaIp.addClickListener(evento -> {
            if (equipoBean != null && equipoBean.getId() != null && !equipoBean.getId().equals(new Long(0))) {
                FrmBuscaIp frmBuscaIp = new FrmBuscaIp(equipoBean);
                frmBuscaIp.addDialogCloseActionListener(eventAyuda -> {
                    ip.setValue(equipoBean.getIpsCadena());
                    doActualizaGrid();
                });
                frmBuscaIp.addDetachListener(eventAyuda -> {
                    ip.setValue(equipoBean.getIpsCadena());
                    doActualizaGrid();
                });
                frmBuscaIp.open();
            }
        }
        );

        /**
         * Ventana para añadir aplicaciones el equipo tiene que ser cpu
         */
        aplicacionButton.addClickListener(event -> {
            EquipoAplicacionBean equipoAplicacionBean = new EquipoAplicacionBean();
            equipoAplicacionBean.setEquipo(equipoBean);
            FrmEquipoAplicacion frmEquipoAplicacion = new FrmEquipoAplicacion("500px", equipoAplicacionBean);
            frmEquipoAplicacion.addDialogCloseActionListener(eventAyuda -> {
                ip.setValue(equipoBean.getIpsCadena());
                doActualizaGrid();
            });
            frmEquipoAplicacion.addDetachListener(eventAyuda -> {
                ip.setValue(equipoBean.getIpsCadena());
                doActualizaGrid();
            });
            frmEquipoAplicacion.open();
        });

        /**
         * Cuando hace clic en el botó de datos genéricos abre la ventana para
         * editar los campos
         */
        datosGenericosButton.addClickListener(event -> {
            FrmDatosGenerico frmDatosGenerico = new FrmDatosGenerico("500px", equipoBean);
            frmDatosGenerico.addDialogCloseActionListener(eventAyuda -> {
                equipoBean.setDatosGenericoBeans(frmDatosGenerico.getDatosGenericoBeans());
                doActualizaGridDatosGenericos();
            });
            frmDatosGenerico.addDetachListener(eventAyuda -> {
                equipoBean.setDatosGenericoBeans(frmDatosGenerico.getDatosGenericoBeans());
                doActualizaGridDatosGenericos();
            });
            frmDatosGenerico.open();
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

        etiquetaButton.addClickListener(event
                -> {
            //    imprimirZebra("");
            //   print();
            imprimeEtiqueta(equipoBean);
        });

        /**
         * Si hay valor en el campo usuario habitual, verifica que exista
         */
        dni.addBlurListener(event -> {
            if (!dni.getValue().isEmpty()) {
                usuarioHabitual = new UsuarioDao().getUsuarioDni(dni.getValue(), Boolean.FALSE);
                if (usuarioHabitual == null) {
                    Notification.show("Usuario no encontgrado");
                    dni.focus();
                } else {
                    nombreusuario.setValue(usuarioHabitual.getApellidosNombre());
                }
            } else {
                nombreusuario.clear();
            }
        });
        dni.addValueChangeListener(event -> {
            if (dni.getValue().isEmpty()) {
                nombreusuario.clear();
            }
        });
    }

    public void doActualizaComboProvinicas(ComboBox<ProvinciaBean> combo, AutonomiaBean autonomia) {
        ArrayList<ProvinciaBean> privinciaArrayList = new ProvinciaDao().getLista(null, autonomia);
        combo.setItems(privinciaArrayList);
    }

    /**
     *
     */
    public void doActualizaComboCentro() {
        ArrayList<CentroBean> centroArrayList = new CentroDao().getLista(null, autonomiaComboBuscador.getValue(),
                provinciaComboBuscador.getValue(), null, null, centroTipoComboBuscador.getValue(), null, ConexionDao.BBDD_ACTIVOSI);
        centroCombo.setItems(centroArrayList);
        centroComboBuscador.setItems(centroArrayList);
        if (centroArrayList.size() > 0) {
            centroCombo.setValue(centroArrayList.get(0));
            centroComboBuscador.setValue(centroArrayList.get(0));
        }
    }

    /*
    private void imprimirZebra(String etiqueta) {
        String sDatos = " ^XSETCUT,MODE,1 "
                + "^Q25,3"
                + "^W45"
                + "^D:ultimo:"
                + "^E28"
                + "^L"
                + "AB,28,0,1,1,1,0,:paciente:"
                + "AB,0,159,1,1,1,3,:contenedor:"
                + "BQ,48,52,2,5,113,0,3,:cb:"
                + "AB,30,20,1,1,1,0,NHC: :nhc:"
                + "AB,188,20,1,1,1,0,HAB: :hab:"
                + "AB,249,181,1,1,1,3,:fecha:"
                + "AB,278,179,1,1,1,3,:hora:"
                + "AB,326,187,1,1,1,3,:servicio:"
                + "Es        ";

        String[] datos = new String[]{"^XA", "^FO0,0 \n", "^FO50,50^ADN,30,20^CI10^FR^FD88888888^FS \n", "^BY1,2^FO50,100^BCN,50,N,Y,N^FR^FDmmmmmmm^FS \n", "^XZ \n"};
        ;

        try {
            // Creamos el socket.
            java.net.Socket socket = new Socket("10.37.14.188", 9100);
            socket.setSoTimeout(1000 * 60 * 3); // 3 minutos

            // Abrimos un buffer.
            BufferedWriter bfWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF8"));

            for (String dato : datos) {
                char[] d = sDatos.toCharArray();
                for (int i = 0; i < d.length; i++) {
                    bfWriter.write(d[i]);
                }
            }

            bfWriter.close();

            // Cerramos el socket
            socket.close();
        } catch (UnknownHostException ex) {

            Logger.getLogger(FrmEquipos.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SocketException ex) {
            Logger.getLogger(FrmEquipos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FrmEquipos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void print() {
        try (Socket socket = new Socket("10.37.14.188", 9100)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String sDatos = "^XA\n^FO0,0 \n^FO50,50^ADN,30,20^CI10^FR^FD88888888^FS \n^BY1,2^FO50,100^BCN,50,N,Y,N^FR^FDmmmmmmm^FS \n^XZ \n";
            byte[] bytes = sDatos.getBytes();

            if (sDatos != null && !sDatos.equals("")) {
                char[] d = sDatos.toCharArray();
                for (int i = 0; i < d.length; i++) {
                    out.write(d[i]);
                }
            }

            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
     */
    public void imprimeEtiqueta(EquipoBean equipoBean) {
        String inventario = "", ip = "", sn = "";
        String valores = new ParametroDao().getPorCodigo(ParametroBean.PRINT_ETIQUETAS).getValor();
        String ipPrinter = null;
        try {
            int puerto = Integer.parseInt(valores.split(",")[1]);
            ipPrinter = valores.split(",")[0];
            if (equipoBean.getInventario() != null) {
                inventario = equipoBean.getInventario();
            }
            if (equipoBean.getIpsCadena() != null) {
                ip = equipoBean.getIpsCadena();
            }
            if (equipoBean.getNumeroSerie() != null) {
                sn = equipoBean.getNumeroSerie();
            }

            Socket clientSocket = new Socket(ipPrinter, puerto);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String[] datos = new String[]{"^XA",
                "^BY1,2^FO50,40^BCN,50,N,Y,N^FR^FD" + inventario + "^FS",
                "^FO50,80^ADN,40,20^FD" + ip + "^FS ",
                "^FO50,120^ADN,40,15^FD" + sn + "^FS ",
                /*
                "^FO50,50^ADN,40,20^FD" + inventario + "^FS",
                "^FO50,75^ADN,40,20^FD" + ip + "^FS",
                "^FO50,100^ADN,40,20^FD" + sn + "^FS",
                "^FO50,75^ADN,40,20^CI10^FR^FD" + ip + "^FS",
                "^FO50,125^ADN,40,20^CI10^FR^FD" + sn + "^FS",
                 */
                "^XZ"};
            //"^BY1,2^FO50,100^BCN,50,N,Y,N^FR^FD88888888^FS\n",

            for (String dato : datos) {
                char[] d = dato.toCharArray();
                for (int i = 0; i < d.length; i++) {
                    out.write(d[i]);
                    //    System.out.println(d[i]);
                }
            }
            out.flush();
            out.close();
            clientSocket.close();
        } catch (UnknownHostException e) {
            LOGGER.error("Error con impresora:" + ipPrinter + Utilidades.getStackTrace(e));
        } catch (IOException e) {
            LOGGER.error("Sin conexion con impresora" + ipPrinter + Utilidades.getStackTrace(e));
        }
    }

}
