/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.GerenciaBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.ZonaBean;
import es.sacyl.gsa.inform.dao.ZonaDao;
import es.sacyl.gsa.inform.dao.GerenciaDao;
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
public final class FrmZona extends FrmMasterPantalla {

    private final ComboBox<AutonomiaBean> autonomiaComboBusca = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> privinciaComboBusca = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<GerenciaBean> gerenciaComboBusca = new CombosUi().getGerenciaCombo(null, AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO);

    private final ComboBox<AutonomiaBean> autonomiaCombo = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> privinciaCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<GerenciaBean> gerenciaCombo = new CombosUi().getGerenciaCombo(null, AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO);

    private final TextField codigo = new ObjetosComunes().getTextField("Código", null, 2, "150px", "50px");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre", null, 65, "300px", "50px");

    private ZonaBean zonaBean = null;
    private final Binder<ZonaBean> zonaBinder = new Binder<>();
    private final PaginatedGrid<ZonaBean> zonaGrid = new PaginatedGrid<>();
    private ArrayList<ZonaBean> zonaArrayList = new ArrayList<>();

    public FrmZona() {
        super();
        this.zonaBean = new ZonaBean();
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
            codigo.setEnabled(true);
            codigo.focus();
        } else {
            // codigo.setEnabled(false);
            codigo.setReadOnly(true);
            nombre.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (zonaBinder.writeBeanIfValid(zonaBean)) {
            if (new ZonaDao().doGrabaDatos(zonaBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<ZonaBean> validate = zonaBinder.validate();
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
                    new ZonaDao().doBorraDatos(zonaBean);
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
        zonaBean = new ZonaBean();
        zonaBinder.readBean(zonaBean);
        codigo.setReadOnly(false);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        zonaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        zonaGrid.setHeightByRows(true);
        zonaGrid.setPageSize(14);
        zonaGrid.addColumn(ZonaBean::getAutonomiaString).setAutoWidth(true).setHeader(new Html("<b>Auto</b>"));
        zonaGrid.addColumn(ZonaBean::getProvinciaString).setAutoWidth(true).setHeader(new Html("<b>Prov</b>"));
        zonaGrid.addColumn(ZonaBean::getGerenciaString).setAutoWidth(true).setHeader(new Html("<b>Ger</b>"));
        zonaGrid.addColumn(ZonaBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));

        zonaGrid.addColumn(ZonaBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        zonaArrayList = new ZonaDao().getLista(autonomiaComboBusca.getValue(), privinciaComboBusca.getValue(), gerenciaComboBusca.getValue(), buscador.getValue());
        zonaGrid.setItems(zonaArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        zonaBinder.forField(autonomiaCombo)
                .asRequired()
                .bind(ZonaBean::getAutonomia, ZonaBean::setAutonomia);

        zonaBinder.forField(privinciaCombo)
                .asRequired()
                .bind(ZonaBean::getProvincia, ZonaBean::setProvincia);

        zonaBinder.forField(gerenciaCombo)
                .asRequired()
                .bind(ZonaBean::getGerencia, ZonaBean::setGerencia);

        zonaBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 2))
                .bind(ZonaBean::getCodigo, ZonaBean::setCodigo);

        zonaBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 65))
                .bind(ZonaBean::getNombre, ZonaBean::setNombre);
    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Zonas Básicas de Salud");
        buscador.setLabel(" Valores a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {
        this.contenedorFormulario.add(autonomiaCombo, privinciaCombo, gerenciaCombo, codigo, nombre);

        this.contenedorBuscadores.add(buscador, autonomiaComboBusca, privinciaComboBusca, gerenciaComboBusca);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, zonaGrid);
    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        zonaGrid.addItemClickListener(event -> {
            zonaBean = event.getItem();
            zonaBinder.readBean(event.getItem());
            doControlBotones(zonaGrid);
        }
        );

        /*
            private final ComboBox<AutonomiaBean> autonomiaComboBusca = new ObjetosComunes().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> privinciaComboBusca = new ObjetosComunes().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
         */
        privinciaComboBusca.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        gerenciaComboBusca.addValueChangeListener(event -> {
            doActualizaGrid();
        });
    }
}
