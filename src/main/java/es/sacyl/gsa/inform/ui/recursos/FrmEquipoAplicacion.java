/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.EquipoAplicacionBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.dao.EquipoAplicacionDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author 06551256M
 */
public final class FrmEquipoAplicacion extends FrmMasterVentana {

    private final ComboBox<AplicacionBean> aplicacionCombo = new CombosUi().getAplicacionCombo(null, null, null, null);
    private final DatePicker fecha = new ObjetosComunes().getDatePicker("Fecha instalación", null, null);
    private final TextArea comentario = new TextArea();
    private final Details equipoDetalle = new ObjetosComunes().getDetails();

    private EquipoAplicacionBean equipoAplicacionBean = new EquipoAplicacionBean();

    private final Binder<EquipoAplicacionBean> equipoAplicacionBinder = new Binder<>();
    private ArrayList<EquipoAplicacionBean> equipoAplicacionArrayList = new ArrayList<>();

    public FrmEquipoAplicacion(String string, EquipoAplicacionBean equipoAplicacionBean) {
        super(string);
        this.equipoAplicacionBean = equipoAplicacionBean;
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
        if (equipoAplicacionBinder.writeBeanIfValid(equipoAplicacionBean)) {
            if (new EquipoAplicacionDao().doGrabaDatos(equipoAplicacionBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
                this.close();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<EquipoAplicacionBean> validate = equipoAplicacionBinder.validate();
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
                    new EquipoAplicacionDao().doBorraDatos(equipoAplicacionBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
    }

    @Override
    public void doCancelar() {
    }

    @Override
    public void doCerrar() {
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        equipoAplicacionBean = new EquipoAplicacionBean();
        equipoAplicacionBinder.readBean(equipoAplicacionBean);
        equipoDetalle.setSummaryText(" ");
        equipoDetalle.setContent(new Span(" "));
        doControlBotones(null);
    }

    @Override
    public void doGrid() {
    }

    @Override
    public void doActualizaGrid() {
    }

    @Override
    public void doBinderPropiedades() {

        equipoAplicacionBinder.forField(aplicacionCombo)
                .asRequired()
                .bind(EquipoAplicacionBean::getAplicacion, EquipoAplicacionBean::setAplicacion);

        equipoAplicacionBinder.forField(fecha)
                .asRequired()
                .bind(EquipoAplicacionBean::getFecha, EquipoAplicacionBean::setFecha);

        equipoAplicacionBinder.forField(comentario)
                .bind(EquipoAplicacionBean::getComentario, EquipoAplicacionBean::setComentario);

    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText(" Aplicaciones instaladas en el  equipo");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("150px", 1),
                new FormLayout.ResponsiveStep("150px", 2));
        contenedorFormulario.add(aplicacionCombo, fecha, comentario, equipoDetalle);
    }

    @Override
    public void doCompentesEventos() {
    }

    public void setDetalleEquipo(EquipoBean equipoBean) {

        equipoDetalle.setContent(new Html(equipoBean.toHtml()));
        equipoDetalle.setSummaryText(equipoBean.getTipo());

    }
}
