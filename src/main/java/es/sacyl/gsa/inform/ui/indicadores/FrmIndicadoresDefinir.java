package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.ComboBean;
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

    public ComboBox<String> areaBuscador = new CombosUi().getCombodeTabla("Área Actividad", null, ComboBean.DWAREASINDICADORES, 60);
    public ComboBox<String> tipoBuscador = new CombosUi().getCombodeTabla("Tipo indicador", null, ComboBean.DWTIPOINDICADOR, 60);

    public TextField codigo = new ObjetosComunes().getTextField("Código");
    public TextField nombre = new ObjetosComunes().getTextField("Nombre");
    public ComboBox<String> area = new CombosUi().getCombodeTabla("Área Actividad", null, ComboBean.DWAREASINDICADORES, 60);
    public ComboBox<String> tipo = new CombosUi().getCombodeTabla("Tipo indicador", null, ComboBean.DWTIPOINDICADOR, 60);
    public IntegerField orden = new ObjetosComunes().getIntegerField("Orden");
    public ComboBox<String> visible = new CombosUi().getSiNoCombo("Visible");
    //  public RadioButtonGroup<String> visible = new ObjetosComunes().getSNRadio("Visible");
    public ComboBox<String> calculado = new CombosUi().getSiNoCombo("Calculado");
    //   public RadioButtonGroup<String> calculado = new ObjetosComunes().getSNRadio("Calculado");
    public TextField formula = new ObjetosComunes().getTextField("Fórmula");

    public TextField item = new ObjetosComunes().getTextField("Item jimena catálogo");

    public TextField codivarhis = new ObjetosComunes().getTextField("Codivarhis ");
    public TextField tablahis = new ObjetosComunes().getTextField("Tabla his (est_servi)");

    public TextArea sql = new ObjetosComunes().getTextArea("sql");
    public TextArea descripcion = new ObjetosComunes().getTextArea("Descripción");

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
            codigo.setEnabled(false);

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
        dwindicadorGrid.addColumn(DWIndicador::getArea).setKey("Area").setAutoWidth(true).setHeader(new Html("<b>Área</b>"));
        dwindicadorGrid.addColumn(DWIndicador::getTipo).setKey("Tipo").setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        dwindicadorArray = new DWIndicadorDao().getLista(areaBuscador.getValue(), buscador.getValue(), tipoBuscador.getValue());
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
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(DWIndicador::getArea, DWIndicador::setArea);

        dwindicadorBinder.forField(tipo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(DWIndicador::getTipo, DWIndicador::setTipo);

        dwindicadorBinder.forField(orden)
                .withNullRepresentation(0)
                .bind(DWIndicador::getOrden, DWIndicador::setOrden);

        dwindicadorBinder.forField(calculado)
                .withNullRepresentation("")
                .bind(DWIndicador::getCalculado, DWIndicador::setCalculado);

        dwindicadorBinder.forField(visible)
                .withNullRepresentation("")
                .bind(DWIndicador::getVisible, DWIndicador::setVisible);

        dwindicadorBinder.forField(formula)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 200))
                .bind(DWIndicador::getFormula, DWIndicador::setFormula);

        dwindicadorBinder.forField(item)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(DWIndicador::getItem, DWIndicador::setItem);

        dwindicadorBinder.forField(codivarhis)
                .withNullRepresentation("")
                .withConverter(new StringToIntegerConverter(FrmMasterConstantes.AVISONUMERO))
                .bind(DWIndicador::getCodivarhis, DWIndicador::setCodivarhis);

        dwindicadorBinder.forField(tablahis)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 50))
                .bind(DWIndicador::getTablahis, DWIndicador::setTablahis);

        dwindicadorBinder.forField(sql)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 500))
                .bind(DWIndicador::getSql, DWIndicador::setSql);

        dwindicadorBinder.forField(descripcion)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 0, 3000))
                .bind(DWIndicador::getDescripcion, DWIndicador::setDescripcion);

    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("Definición de indicadores ");
        buscador.setLabel("Dato a buscar");
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
        contenedorFormulario.add(nombre, 5);

        contenedorFormulario.add(area, 2);
        contenedorFormulario.add(tipo, 2);
        contenedorFormulario.add(orden);
        contenedorFormulario.add(visible);

        contenedorFormulario.add(calculado, item, codivarhis);
        contenedorFormulario.add(tablahis, 3);

        contenedorFormulario.add(formula, 6);
        contenedorFormulario.add(sql, 6);
        contenedorFormulario.add(descripcion, 6);

        contenedorBuscadores.removeAll();
        contenedorBuscadores.add(buscador, areaBuscador, tipoBuscador);
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

        /**
         * Cambia el combo del tipo de indicador
         */
        tipoBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
    }

}
