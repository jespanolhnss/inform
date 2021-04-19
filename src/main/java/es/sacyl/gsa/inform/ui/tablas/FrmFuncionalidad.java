package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.FuncionalidadBean;
import es.sacyl.gsa.inform.dao.FuncionalidadDAO;
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
public final class FrmFuncionalidad extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getId();
    private final TextField descripcion = new ObjetosComunes().getTextField("Descripción");
    private final TextField textomenu = new ObjetosComunes().getTextField("Texto menú");
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();

    private FuncionalidadBean funcionalidadBean = new FuncionalidadBean();
    private final Binder<FuncionalidadBean> funcionalidadBinder = new Binder<>();
    private final PaginatedGrid<FuncionalidadBean> funcionalidadGrid = new PaginatedGrid<>();
    private ArrayList<FuncionalidadBean> funcionalidadArrayList = new ArrayList<>();

    public FrmFuncionalidad() {
        doComponentesOrganizacion();
        doGrid();
        doActualizaGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();

    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
        } else {
        }
    }

    @Override
    public void doGrabar() {
        if (funcionalidadBinder.writeBeanIfValid(funcionalidadBean)) {
            funcionalidadBean.setValoresAut();
            if (new FuncionalidadDAO().doGrabaDatos(funcionalidadBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<FuncionalidadBean> validate = funcionalidadBinder.validate();
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
                    new FuncionalidadDAO().doBorraDatos(funcionalidadBean);
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
        funcionalidadBean = new FuncionalidadBean();
        funcionalidadBinder.readBean(funcionalidadBean);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        funcionalidadGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        funcionalidadGrid.setHeightByRows(true);
        funcionalidadGrid.setPageSize(14);
        funcionalidadGrid.addColumn(FuncionalidadBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        funcionalidadGrid.addColumn(FuncionalidadBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        funcionalidadArrayList = new FuncionalidadDAO().getLista(null);
        funcionalidadGrid.setItems(funcionalidadArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        funcionalidadBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(FuncionalidadBean::getId, null);

        funcionalidadBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 50))
                .bind(FuncionalidadBean::getDescripcion, FuncionalidadBean::setDescripcion);

        funcionalidadBinder.forField(textomenu)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 50))
                .bind(FuncionalidadBean::getTextomenu, FuncionalidadBean::setTextomenu);

    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Funconalidades");
        this.buscador.setTitle(" Valor a buscar");
        this.buscador.setLabel("Valor a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(id, descripcion, textomenu, estadoRadio);
        contenedorBuscadores.add(buscador);
        contenedorDerecha.add(funcionalidadGrid);
    }

    @Override
    public void doCompentesEventos() {
        funcionalidadGrid.addItemClickListener(event -> {
            funcionalidadBean = event.getItem();
            funcionalidadBinder.readBean(funcionalidadBean);
            doControlBotones(funcionalidadBean);
        }
        );

    }

}
