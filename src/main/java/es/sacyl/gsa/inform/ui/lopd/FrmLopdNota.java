package es.sacyl.gsa.inform.ui.lopd;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdNotaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.LopdNotaDao;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author 06551256M
 */
public final class FrmLopdNota extends FrmMasterVentana {

    private final TextField id = new TextField("Id");
    private final DatePicker fecha = new ObjetosComunes().getDatePicker("Fecha", null, LocalDate.now());
    private final TextArea descripcion = new TextArea("Descripción");

    private final Binder<LopdNotaBean> lopdNotaBinder = new Binder<>();
    private LopdNotaBean lopdNotaBean;
    private final LopdIncidenciaBean lopdIncidenciaBean;

    private Grid<LopdNotaBean> notasGrid = new Grid<>();

    public FrmLopdNota(String ancho, LopdNotaBean notaParam, LopdIncidenciaBean incidenciaParam) {
        super(ancho);
        this.lopdNotaBean = notaParam;
        this.lopdNotaBean.setIdIncidenciaLong(incidenciaParam.getId());
        this.lopdIncidenciaBean = incidenciaParam;
        this.titulo.setText("Notas de la incidencia");
        doComponentesOrganizacion();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doGrid();
        super.doControlBotones(lopdNotaBean);
        lopdNotaBinder.readBean(lopdNotaBean);
    }

    @Override
    public void doGrabar() {
        if (lopdNotaBinder.writeBeanIfValid(lopdNotaBean)) {
            lopdNotaBean.setUsucambio((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
            lopdNotaBean.setFecha(LocalDate.now());
            Long idLong = new LopdNotaDao().grabaDatos(lopdIncidenciaBean, lopdNotaBean);
            if (!idLong.equals(new Long(0))) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
                doCerrar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<LopdNotaBean> validate = lopdNotaBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }
    }

    @Override
    public void doCerrar() {
        this.close();
    }

    @Override
    public void doBorrar() {

        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    lopdNotaBean.setUsucambio((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
                    lopdNotaBean.setFecha(LocalDate.now());
                    lopdNotaBean.setEstado(ConexionDao.BBDD_ACTIVONO);
                    new LopdNotaDao().grabaDatos(lopdIncidenciaBean, lopdNotaBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                    //   close();
                });
        dialog.open();
    }

    @Override
    public void doCancelar() {
        this.close();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        lopdNotaBean = new LopdNotaBean();
        lopdNotaBinder.readBean(lopdNotaBean);
    }

    @Override
    public void doGrid() {
        notasGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

    }

    @Override
    public void doActualizaGrid() {
    }

    @Override
    public void doBinderPropiedades() {
        lopdNotaBinder.forField(id)
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(LopdNotaBean::getId, null);
        lopdNotaBinder.forField(descripcion)
                .bind(LopdNotaBean::getDescripcion, LopdNotaBean::setDescripcion);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(id, fecha, descripcion);
    }

    @Override
    public void doCompentesEventos() {
    }

    @Override
    public void doComponenesAtributos() {
        id.setReadOnly(true);
        descripcion.setWidth("300px");
        descripcion.setHeight("100px");
    }

}
