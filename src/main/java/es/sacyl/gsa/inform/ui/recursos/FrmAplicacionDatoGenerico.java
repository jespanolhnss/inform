package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.dao.AplicacionesDatosDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
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
public final class FrmAplicacionDatoGenerico extends FrmMasterVentana {

    private TextArea valor = new ObjetosComunes().getTextArea("Valor");
    private ComboBox<String> tipoDatosComboBox = new CombosUi().getGrupoRamaComboValor(ComboBean.TIPOAPLICACIONDATO, ComboBean.TIPOAPLICACIONDATOAPLICACION, null, null);
    private DatePicker fechaCambio = new ObjetosComunes().getDatePicker(null, null, null);

    private final TextField tecnicoapellidosNombre = new ObjetosComunes().getTextField("Técnico", "", 50, "200px", "80px");

    private AplicacionBean aplicacionBean = new AplicacionBean();
    private DatoGenericoBean datoGenericoBean = new DatoGenericoBean();

    private final Binder<DatoGenericoBean> datoGenericoBinder = new Binder<>();
    private final PaginatedGrid<DatoGenericoBean> datoGenericoGrid = new PaginatedGrid<>();
    private ArrayList<DatoGenericoBean> datoGenericoBeanArray = new ArrayList<>();

    public FrmAplicacionDatoGenerico(AplicacionBean aplicacionBeanParam, DatoGenericoBean datoGenericoBean) {
        super();
        this.aplicacionBean = aplicacionBeanParam;
        if (datoGenericoBean != null) {
            this.datoGenericoBean = datoGenericoBean;

        } else {
            this.datoGenericoBean.setIdDatoAplicacion(aplicacionBean.getId());
        }
        doVentana();
        datoGenericoBinder.readBean(datoGenericoBean);
    }

    public void doVentana() {
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
        if (datoGenericoBinder.writeBeanIfValid(datoGenericoBean)) {
            datoGenericoBean.setIdDatoAplicacion(aplicacionBean.getId());
            datoGenericoBean.setValoresAut();
            if (new AplicacionesDatosDao().doGrabaDatos(datoGenericoBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<DatoGenericoBean> validate = datoGenericoBinder.validate();
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
                    new AplicacionesDatosDao().doBorraDatos(datoGenericoBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
    }

    @Override
    public void doCancelar() {
        this.close();
    }

    @Override
    public void doCerrar() {
        this.close();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        datoGenericoBean = new DatoGenericoBean();
        datoGenericoBinder.readBean(datoGenericoBean);
        datoGenericoBean.setIdDatoAplicacion(aplicacionBean.getId());
        fechaCambio.clear();
        tecnicoapellidosNombre.clear();

    }

    @Override
    public void doGrid() {
        datoGenericoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        datoGenericoGrid.setHeightByRows(true);
        datoGenericoGrid.setPageSize(25);
        datoGenericoGrid.setPaginatorSize(25);
        datoGenericoGrid.addColumn(DatoGenericoBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        datoGenericoGrid.addColumn(DatoGenericoBean::getTipoDato).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));
        datoGenericoGrid.addColumn(DatoGenericoBean::getValor).setAutoWidth(true).setHeader(new Html("<b>Valor</b>"));
        datoGenericoGrid.setClassName("error_row");
        datoGenericoGrid.setClassNameGenerator(auto -> {
            if (auto.getEstado() == 0) {
                //    return "my-style-2";
                return "error_row";
            } else {
                return "my-style-1";
            }

        });
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        datoGenericoBeanArray = new AplicacionesDatosDao().getLista(null, aplicacionBean);
        datoGenericoGrid.setItems(datoGenericoBeanArray);
    }

    @Override
    public void doBinderPropiedades() {
        datoGenericoBinder.forField(tipoDatosComboBox)
                .asRequired()
                .bind(DatoGenericoBean::getTipoDato, DatoGenericoBean::setTipoDato);

        datoGenericoBinder.forField(valor)
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(DatoGenericoBean::getValor, DatoGenericoBean::setValor);
    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Apliación:" + aplicacionBean.getNombre());
        fechaCambio.setEnabled(false);
        tecnicoapellidosNombre.setEnabled(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(tipoDatosComboBox, valor, fechaCambio, tecnicoapellidosNombre);
        contenedorDerecha.add(datoGenericoGrid);
    }

    @Override
    public void doCompentesEventos() {
        datoGenericoGrid.addItemClickListener(event -> {
            datoGenericoBean = event.getItem();
            datoGenericoBinder.readBean(datoGenericoBean);
            fechaCambio.setValue(datoGenericoBean.getFechacambio());
            tecnicoapellidosNombre.setValue(datoGenericoBean.getUsucambio().getApellidosNombre());
            doControlBotones(datoGenericoBean);
        });

    }

    public ArrayList<DatoGenericoBean> getDatoGenericoBeanArray() {
        return datoGenericoBeanArray;
    }

    public void setDatoGenericoBeanArray(ArrayList<DatoGenericoBean> datoGenericoBeanArray) {
        this.datoGenericoBeanArray = datoGenericoBeanArray;
    }

}
