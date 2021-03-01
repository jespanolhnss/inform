package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.EquipoDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.dao.UbicacionDao;
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
public final class FrmEquipos extends FrmMasterPantalla {

    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<CentroBean> centroComboBuscador = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final Button ayudaUbicacion = new ObjetosComunes().getBoton(null, null, VaadinIcon.QUESTION_CIRCLE.create());
    private final Button ayudaIp = new ObjetosComunes().getBoton(null, null, VaadinIcon.QUESTION_CIRCLE.create());

    private final ComboBox<String> equipoTipoComboBuscador = new CombosUi().getEquipoTipoCombo(null, 50);

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final ComboBox<String> equipoTipoCombo = new CombosUi().getEquipoTipoCombo(null, 50);
    private final ComboBox<String> equipoMarcaCombo = new CombosUi().getGrupoRamaComboValor(ComboBean.TIPOEQUIPOMARCA, equipoTipoCombo.getValue(), null, "Marca");

    private final TextField inventario = new ObjetosComunes().getTextField("Inventario");
    //  private final TextField marca = new ObjetosComunes().getTextField("Marca");
    private final TextField modelo = new ObjetosComunes().getTextField("Modelo");
    private final TextField numeroSerie = new ObjetosComunes().getTextField("N.Serie");

    private final ComboBox<CentroBean> centroCombo = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final ComboBox<UbicacionBean> ubicacionCombo = new CombosUi().getUbicacionCombo(null, centroCombo.getValue(), null, null);
    private final TextField ubicacion = new ObjetosComunes().getTextField("Ubicación");
    //  private final TextField servicio = new ObjetosComunes().getTextField("Servicio");
    private final ComboBox<GfhBean> servicioCombo = new CombosUi().getServicioCombo(null, null);
    private final TextField ip = new ObjetosComunes().getTextField("Ip");
    private final TextField comentario = new ObjetosComunes().getTextField("Comentario");

    private EquipoBean equipoBean = null;
    private final Binder<EquipoBean> equipoBinder = new Binder<>();
    private final PaginatedGrid<EquipoBean> equipoGrid = new PaginatedGrid<>();
    private ArrayList<EquipoBean> equipoArrayList = new ArrayList<>();

    public FrmEquipos() {
        super();
        this.equipoBean = new EquipoBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    /**
     *
     * @param obj
     */
    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {

        } else {

        }
    }

    /**
     *
     */
    @Override
    public void doGrabar() {
        if (equipoBinder.writeBeanIfValid(equipoBean)) {
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
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        equipoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        equipoGrid.setHeightByRows(true);
        equipoGrid.setPageSize(14);
        equipoGrid.addColumn(EquipoBean::getTipo).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>")).setWidth("70px");
        equipoGrid.addColumn(EquipoBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Est</b>")).setWidth("20px");
        equipoGrid.addColumn(EquipoBean::getInventario).setAutoWidth(true).setHeader(new Html("<b>Invent</b>")).setWidth("70px");
        equipoGrid.addColumn(EquipoBean::getMarca).setAutoWidth(true).setHeader(new Html("<b>Marca</b>"));
        equipoGrid.addColumn(EquipoBean::getModelo).setAutoWidth(true).setHeader(new Html("<b>Modelo</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        equipoArrayList = new EquipoDao().getLista(buscador.getValue(), equipoTipoComboBuscador.getValue(),
                centroComboBuscador.getValue(), null, null);
        equipoGrid.setItems(equipoArrayList);
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
        equipoTipoCombo.addValueChangeListener(event -> {
            equipoMarcaCombo.setItems(new ComboDao().getListaGruposRamaValor(ComboBean.TIPOEQUIPOMARCA, event.getValue(), 100));
        });
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

        contenedorDerecha.removeAll();
        contenedorBuscadores.add(equipoTipoComboBuscador, autonomiaComboBuscador, provinciaComboBuscador, centroTipoComboBuscador, centroComboBuscador);
        contenedorDerecha.add(this.contenedorBuscadores, equipoGrid);
        contenedorFormulario.add(id, 2);
        contenedorFormulario.add(equipoTipoCombo, 2);
        contenedorFormulario.add(equipoMarcaCombo, 2);

        contenedorFormulario.add(inventario, 2);
        contenedorFormulario.add(modelo, 2);
        contenedorFormulario.add(numeroSerie, 2);

        contenedorFormulario.add(ip, 5);
        contenedorFormulario.add(ayudaIp);

        contenedorFormulario.add(centroCombo, 3);
        contenedorFormulario.add(servicioCombo, 3);

        contenedorFormulario.add(ubicacionCombo, 5);
        contenedorFormulario.add(ayudaUbicacion);
        contenedorFormulario.add(comentario, 6);
    }

    @Override
    public void doCompentesEventos() {
        autonomiaComboBuscador.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComboProvinicas(provinciaComboBuscador, autonomiaBean);
        });
        equipoTipoComboBuscador.addValueChangeListener(evetn -> {
            doActualizaGrid();
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
        centroCombo.addValueChangeListener(event -> {
            ubicacionCombo.setItems(new UbicacionDao().getLista(null, event.getValue(), null));
            doActualizaGrid();
        });

        equipoGrid.addItemClickListener(event -> {
            equipoBean = event.getItem();
            equipoBinder.readBean(event.getItem());
            doControlBotones(equipoBean);
        }
        );

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
    }

    public void doActualizaComboProvinicas(ComboBox<ProvinciaBean> combo, AutonomiaBean autonomia) {
        ArrayList<ProvinciaBean> privinciaArrayList = new ProvinciaDao().getLista(null, autonomia);
        combo.setItems(privinciaArrayList);
    }

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
}
