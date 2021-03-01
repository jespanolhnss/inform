package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.ParametroDao;
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
public final class FrmParametro extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id", "id", 25, "50px", "50px");
    private final TextField codigo = new ObjetosComunes().getTextField("Código", "codigo", 25, "150px", "50px");
    private final TextField descripcion = new ObjetosComunes().getTextField("Descripcion", "descripcion", 100, "300px", "50px");
    private final TextField valor = new ObjetosComunes().getTextField("Valor", "descripcion", 400, "300px", "50px");

    private ParametroBean parametroBean = new ParametroBean();
    private final Binder<ParametroBean> parametroBinder = new Binder<>();
    private final PaginatedGrid<ParametroBean> parametroGrid = new PaginatedGrid<>();

    public FrmParametro() {
        super();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
        if (parametroBinder.writeBeanIfValid(parametroBean)) {
            if (new ParametroDao().doGrabaDatos(parametroBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<ParametroBean> validate = parametroBinder.validate();
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
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new ParametroDao().doBorraDatos(parametroBean);
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
        parametroBean = new ParametroBean();
        parametroBinder.readBean(parametroBean);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        parametroGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        parametroGrid.setHeightByRows(true);
        parametroGrid.setPageSize(14);
        parametroGrid.addColumn(ParametroBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        parametroGrid.addColumn(ParametroBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        parametroGrid.addColumn(ParametroBean::getValor).setAutoWidth(true).setHeader(new Html("<b>Valor</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        ArrayList<ParametroBean> lista = new ArrayList<>();
        lista = new ParametroDao().getLista(buscador.getValue());
        parametroGrid.setItems(lista);
    }

    @Override
    public void doBinderPropiedades() {

        parametroBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ParametroBean::getId, null);

        parametroBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(ParametroBean::getCodigo, ParametroBean::setCodigo);

        parametroBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(ParametroBean::getDescripcion, ParametroBean::setDescripcion);

        parametroBinder.forField(valor)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 400))
                .bind(ParametroBean::getValor, ParametroBean::setValor);

    }

    @Override
    public void doComponenesAtributos() {
        buscador.focus();
        buscador.setLabel("Contenido a buscar");
        buscador.setValueChangeMode(ValueChangeMode.EAGER);
        id.setReadOnly(true);
    }

    @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Gestión de Parámetros de la Base de Datos");

        this.contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("100px", 1),
                new FormLayout.ResponsiveStep("200px", 2));

        this.contenedorFormulario.add(id, codigo);
        this.contenedorFormulario.add(descripcion, 2);
        this.contenedorFormulario.add(valor, 2);

        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, parametroGrid);
    }

    @Override
    public void doCompentesEventos() {
        buscador.addBlurListener(e -> doActualizaGrid());
        parametroGrid.addItemClickListener(event -> {
            parametroBean = event.getItem();
            parametroBinder.readBean(event.getItem());
        }
        );
    }

}
