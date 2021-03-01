/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.viajes;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
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
import es.sacyl.gsa.inform.bean.ViajeTecnicoBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.dao.ViajesDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.time.LocalDate;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

public final class FrmViajesRegistrar extends FrmMasterPantalla {

    private static final long serialVersionUID = 1L;

    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<CentroBean> centroComboBuscador = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final DatePicker desde = new ObjetosComunes().getDatePicker("Desde", null, LocalDate.now());
    private final DatePicker hasta = new ObjetosComunes().getDatePicker("Hasta", null, LocalDate.now());


    /* Campos del formulario */
    TextField id = new ObjetosComunes().getTextField("id");
    DatePicker fechaViaje = new ObjetosComunes().getDatePicker("Fecha", "fecha del viaje", LocalDate.now());
    IntegerField horaSalida = new IntegerField("Hora de Salida");
    IntegerField horaLlegada = new IntegerField("Hora de Llegada");
    TextField matricula = new ObjetosComunes().getTextField("Matrícula", "teclea matrícula del vehículo", 25, "100px", "30px");

    /* Componentes para el viaje */
    ViajeBean viajeBean = new ViajeBean();
    Binder<ViajeBean> viajesBinder = new Binder<>();
    String tituloGridViajes = "LISTADO DE VIAJES";
    PaginatedGrid<ViajeBean> viajesGrid = new PaginatedGrid<>();
    ArrayList<ViajeBean> arrayListViajes = new ArrayList<>();

    /* Componentes para el detalle de los centros */
    ViajeCentroBean viajeCentroBean = new ViajeCentroBean();
    String tituloGridCentros = "CENTROS ASOCIADOS AL VIAJE";
    Grid<ViajeCentroBean> viajeCentroGrid = new Grid<>();
    PaginatedGrid<ViajeTecnicoBean> viajeTecnicoGrid = new PaginatedGrid<>();

    ArrayList<ViajeCentroBean> viajeCentrosArrayList = new ArrayList<>();

    HorizontalLayout contenedorBotones2 = new HorizontalLayout();
    Icon icon = new Icon(VaadinIcon.BUILDING);
    Button lanzarVentana = new ObjetosComunes().getBoton("Centros", ButtonVariant.LUMO_PRIMARY, icon);
    Icon tecnicosIcon = new Icon(VaadinIcon.USER);
    Button tecnicosButton = new ObjetosComunes().getBoton("Técnicos", ButtonVariant.LUMO_PRIMARY, tecnicosIcon);

    public FrmViajesRegistrar() {
        super();
        this.setWidthFull();
        doComponentesOrganizacion();
        doGrid();
        doGridTecnico();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
        doControlBotones(null);
    }

    public void doControlBotones(Object obj) {
        if (obj != null) {
            botonBorrar.setEnabled(true);
            botonImprimir.setEnabled(true);
            lanzarVentana.setEnabled(true);
            tecnicosButton.setEnabled(true);
        } else {
            botonBorrar.setEnabled(false);
            botonImprimir.setEnabled(false);
            lanzarVentana.setEnabled(false);
            tecnicosButton.setEnabled(false);
        }
    }

    @Override
    public void doGrabar() {
        lanzarVentana.setEnabled(true);
        tecnicosButton.setEnabled(true);
        try {
            viajesBinder.writeBean(viajeBean);
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

    }

    @Override
    public void doImprimir() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doGrid() {
        viajesGrid.addColumn(ViajeBean::getFecha).setHeader("Fecha");
        viajesGrid.addColumn(ViajeBean::getHoraSalida).setHeader("Salida");
        viajesGrid.addColumn(ViajeBean::getHoraLlegada).setHeader("Llegada");
        viajesGrid.addColumn(ViajeBean::getMatricula).setHeader("Matrícula");
        viajesGrid.setWidthFull();

        viajeCentroGrid.addColumn(ViajeCentroBean::getIdViaje).setHeader("IdViaje").setAutoWidth(true);
        viajeCentroGrid.addColumn(ViajeCentroBean::getNombreCentro).setHeader("Centro").setAutoWidth(true);
        viajeCentroGrid.addColumn(ViajeCentroBean::getPreparacion).setHeader("Preparación").setAutoWidth(true);
        viajeCentroGrid.addColumn(ViajeCentroBean::getActuacion).setHeader("Actuación").setAutoWidth(true);

        doActualizaGrid();
    }

    public void doGridTecnico() {
        PaginatedGrid<UsuarioBean> ipGrid = new PaginatedGrid<>();
        viajeTecnicoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        viajeTecnicoGrid.setHeightByRows(true);
        viajeTecnicoGrid.setPageSize(14);

        viajeTecnicoGrid.addColumn(ViajeTecnicoBean::getTecnicoApellidos).setAutoWidth(true).setHeader(new Html("<b>Usuario</b>"));
        viajeTecnicoGrid.addComponentColumn(item -> createRemoveButton(viajeTecnicoGrid, item))
                .setHeader("Borra");

    }

    private Button createRemoveButton(PaginatedGrid<ViajeTecnicoBean> grid, ViajeTecnicoBean item) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Borra", clickEvent -> {
            final ConfirmDialog dialog = new ConfirmDialog(
                    FrmMensajes.AVISOCONFIRMACIONACCION,
                    FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                    FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                        new ViajesDao().doBorraUnTecnico(item);
                        doActualizaGridTecnicos(viajeBean);
                    });
            dialog.open();
        });
        return button;
    }

    @Override
    public void doActualizaGrid() {
        arrayListViajes = new ViajesDao().getListaViajes(desde.getValue(), hasta.getValue(), centroComboBuscador.getValue(), null, 1);
        viajesGrid.setItems(arrayListViajes);
    }

    @Override
    public void doBinderPropiedades() {
        viajesBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeBean::getId, null);
        viajesBinder.forField(fechaViaje).bind(ViajeBean::getFecha, ViajeBean::setFecha);
        viajesBinder.forField(horaSalida)
                .bind(ViajeBean::getHoraSalida, ViajeBean::setHoraSalida);
        viajesBinder.forField(horaLlegada).bind(ViajeBean::getHoraLlegada, ViajeBean::setHoraLlegada);
        viajesBinder.forField(matricula)
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 8))
                .bind(ViajeBean::getMatricula, ViajeBean::setMatricula);
    }

    @Override
    public void doComponenesAtributos() {
        autonomiaComboBuscador.setVisible(false);

        horaSalida.setPlaceholder("números enteros: 0930");
        horaLlegada.setPlaceholder("números enteros: 1430");

        matricula.setMaxLength(8);

        centroComboBuscador.setWidth("150px");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorIzquierda.setWidth("50%");
        contenedorIzquierda.removeAll();
        contenedorIzquierda.add(contenedorBotones);
        contenedorIzquierda.add(contenedorBotones2);
        contenedorIzquierda.add(contenedorFormulario);
        contenedorIzquierda.add(viajeTecnicoGrid);

        contenedorIzquierda.add(tituloGridCentros);
        contenedorIzquierda.add(viajeCentroGrid);

        /*
        contenedorIzquierda.addComponentAtIndex(3, tecnicoGrid);
        contenedorIzquierda.addComponentAtIndex(2, contenedorBotones2);
         */
        lanzarVentana.setEnabled(false);
        tecnicosButton.setEnabled(false);
        contenedorBotones2.add(lanzarVentana, tecnicosButton);

        contenedorFormulario.add(id, fechaViaje, horaSalida, horaLlegada, matricula);

        contenedorBuscadores.add(autonomiaComboBuscador, provinciaComboBuscador, centroTipoComboBuscador, centroComboBuscador, desde, hasta);
        contenedorDerecha.setWidth("50%");
        contenedorDerecha.add(contenedorBuscadores);
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
            FrmViajesCentrosRegistrar nuevo = new FrmViajesCentrosRegistrar("640px", viajeBean);
            //nuevo.addDialogCloseActionListener(e -> doActualizaGridCentros(viajeBean.getId()));
            //nuevo.addDetachListener(e -> doActualizaGridCentros(viajeCentroBean.getIdViaje()));
            nuevo.addDialogCloseActionListener(e -> {
                doActualizaGridCentros(viajeBean.getId());
                nuevo.close();
            });
            nuevo.open();
        });

        viajesGrid.addItemClickListener(event -> {
            viajeBean = event.getItem();
            viajesBinder.readBean(viajeBean);
            doActualizaGridCentros(viajeBean.getId());
            doActualizaGridTecnicos(viajeBean);
            doControlBotones(viajeBean);
        });

        viajeCentroGrid.addItemClickListener(event -> {
            viajeCentroBean = event.getItem();

            FrmViajesCentrosRegistrar actualizar = new FrmViajesCentrosRegistrar("640px", viajeCentroBean);
            actualizar.addDialogCloseActionListener(e -> doActualizaGridCentros(viajeCentroBean.getIdViaje()));
            actualizar.addDetachListener(e -> {
                doActualizaGridCentros(viajeCentroBean.getIdViaje());
                doActualizaGridTecnicos(viajeBean);
            });

            actualizar.addDialogCloseActionListener(e -> {
                doActualizaGridCentros(viajeCentroBean.getIdViaje());
                doActualizaGridTecnicos(viajeBean);
            });
            actualizar.open();
        }
        );

        tecnicosButton.addClickListener(event
                -> {
            FrmViajesTecnicosRegistrar nuevo = new FrmViajesTecnicosRegistrar("640px", viajeBean);
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

    private void doActualizaGridCentros(Long i) {
        viajeCentrosArrayList = new ViajesDao().getViajeCentros(i);
        viajeCentroGrid.setItems(viajeCentrosArrayList);
    }

    private void doActualizaGridTecnicos(ViajeBean viajeBean) {
        viajeTecnicoGrid.setItems(new ViajesDao().getViajeTecnicos(viajeBean));

    }

}
