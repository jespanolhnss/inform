package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.dao.DWIndicadorDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
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
public final class FrmIndicadoresDefinir extends FrmMasterPantalla {

    public ComboBox<String> areaBuscador = new CombosUi().getStringCombo("Área activiadad", null, DWIndicador.DWINDICADORAREAACTIVIDAD, null);

    public TextField codigo = new ObjetosComunes().getTextField("Código");
    public TextField nombre = new ObjetosComunes().getTextField("Nombre");
    public ComboBox<String> area = new CombosUi().getStringCombo("Área activiadad", null, DWIndicador.DWINDICADORAREAACTIVIDAD, null);
    public IntegerField orden = new ObjetosComunes().getIntegerField("Orden");
    public RadioButtonGroup<String> visible = new ObjetosComunes().getSNRadio("Calculado");
    public RadioButtonGroup<String> calculado = new ObjetosComunes().getSNRadio("Calculado");
    public TextField formula = new ObjetosComunes().getTextField("Fórmula");

    public IntegerField item = new ObjetosComunes().getIntegerField("Item jimena catálogo");

    public TextField codivarhis = new ObjetosComunes().getTextField("Codivarhis ");
    public TextField tablahis = new ObjetosComunes().getTextField("Tabla his (est_servi)");

    public TextArea sql = new ObjetosComunes().getTextArea("sql");
    public TextArea descricion = new ObjetosComunes().getTextArea("descricion");

    public DWIndicador dwindicador = new DWIndicador();
    public PaginatedGrid<DWIndicador> dwindicadorGrid = new PaginatedGrid<>();
    public ArrayList<DWIndicador> dwindicadorArray = new ArrayList<>();
    public Binder<DWIndicador> dwindicadorBinder = new Binder<>();

    public FrmIndicadoresDefinir() {
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

            codigo.setReadOnly(true);
            nombre.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (dwindicadorBinder.writeBeanIfValid(dwindicador)) {
            if (new DWIndicadorDao().doGrabaDatos(dwindicador) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<DWIndicador> validate = dwindicadorBinder.validate();
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
                    new DWIndicadorDao().doBorraDatos(dwindicador);
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
        dwindicador = new DWIndicador();
        dwindicadorBinder.readBean(dwindicador);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        dwindicadorGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        dwindicadorGrid.setHeightByRows(true);
        dwindicadorGrid.setPageSize(25);
        dwindicadorGrid.setPaginatorSize(25);
        dwindicadorGrid.addColumn(DWIndicador::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        dwindicadorGrid.addColumn(DWIndicador::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        dwindicadorGrid.addColumn(DWIndicador::getArea).setKey("Area").setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));

    }

    @Override
    public void doActualizaGrid() {
        dwindicadorArray = new DWIndicadorDao().getLista(areaBuscador.getValue(), buscador.getValue());
        dwindicadorGrid.setItems(dwindicadorArray);
    }

    @Override
    public void doBinderPropiedades() {
        dwindicadorBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 6))
                .bind(DWIndicador::getCodigo, DWIndicador::setCodigo);

        dwindicadorBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 50))
                .bind(DWIndicador::getNombre, DWIndicador::setNombre);

        dwindicadorBinder.forField(area)
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(DWIndicador::getArea, DWIndicador::setArea);

        dwindicadorBinder.forField(orden)
                .bind(DWIndicador::getOrden, DWIndicador::setOrden);

        dwindicadorBinder.forField(calculado)
                .bind(DWIndicador::getCalculado, DWIndicador::setCalculado);

        dwindicadorBinder.forField(visible)
                .bind(DWIndicador::getVisible, DWIndicador::setVisible);

        dwindicadorBinder.forField(formula)
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 200))
                .bind(DWIndicador::getFormula, DWIndicador::setFormula);

        dwindicadorBinder.forField(item)
                .bind(DWIndicador::getItem, DWIndicador::setItem);

        dwindicadorBinder.forField(codivarhis)
                .withConverter(new StringToIntegerConverter(FrmMasterConstantes.AVISONUMERO))
                .bind(DWIndicador::getCodivarhis, DWIndicador::setCodivarhis);

        dwindicadorBinder.forField(tablahis)
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 50))
                .bind(DWIndicador::getTablahis, DWIndicador::setTablahis);

        dwindicadorBinder.forField(sql)
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 500))
                .bind(DWIndicador::getSql, DWIndicador::setSql);

        dwindicadorBinder.forField(descricion)
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 3000))
                .bind(DWIndicador::getSql, DWIndicador::setSql);

    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("Definición de indicadores ");
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
        contenedorFormulario.add(codigo);
        contenedorFormulario.add(nombre, 4);
        contenedorFormulario.add(area);

        contenedorFormulario.add(orden, visible, calculado, item, codivarhis, tablahis);
        contenedorFormulario.add(formula, 6);
        contenedorFormulario.add(sql, 6);
        contenedorFormulario.add(descricion, 6);

        contenedorBuscadores.removeAll();
        contenedorBuscadores.add(buscador, areaBuscador);
        contenedorDerecha.add(dwindicadorGrid);
    }

    @Override
    public void doCompentesEventos() {

        codigo.addBlurListener(event -> {
            DWIndicador indicadornew = new DWIndicadorDao().getPorCodigo(codigo.getValue());
            if (indicadornew != null) {
                Notification.show(" Ese código ya existe ");
                dwindicador = indicadornew;
                dwindicadorBinder.readBean(indicadornew);
                doControlBotones(dwindicador);
            }
        });
        dwindicadorGrid.addItemClickListener(event -> {
            dwindicador = event.getItem();
            dwindicadorBinder.readBean(dwindicador);
            doControlBotones(dwindicador);
        });

        buscador.addBlurListener(event -> {
            if (!buscador.getValue().isEmpty()) {
                doActualizaGrid();
            }
        });

        areaBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
    }

}
