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
import com.vaadin.flow.data.value.ValueChangeMode;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.LocalidadBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.LocalidadDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
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
 * @author JuanNieto
 */
public final class FrmLocalidad extends FrmMasterPantalla {

    private final TextField codigo = new ObjetosComunes().getTextField("Codigo");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");

    private final ComboBox<ProvinciaBean> provinciasComobo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<ProvinciaBean> provinciasComoBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);

    private final ComboBox<AutonomiaBean> autonomiaCombo = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);

    private final Binder<LocalidadBean> localidadBinder = new Binder<>();
    private ArrayList<LocalidadBean> localidadArrayList = new ArrayList<>();
    private LocalidadBean localidadBean = new LocalidadBean();
    private final PaginatedGrid<LocalidadBean> localidadGrid = new PaginatedGrid<>();

    public FrmLocalidad() {
        super();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
        if (localidadBinder.writeBeanIfValid(localidadBean)) {
            if (new LocalidadDao().doGrabaDatos(localidadBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<LocalidadBean> validate = localidadBinder.validate();
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
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new LocalidadDao().doBorraDatos(localidadBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
        dialog.addDialogCloseActionListener(e -> {
        });
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        localidadBean = new LocalidadBean();
        localidadBinder.readBean(localidadBean);
    }

    @Override
    public void doGrid() {
        localidadGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        localidadGrid.setHeightByRows(true);
        localidadGrid.setPageSize(14);
        localidadGrid.addColumn(LocalidadBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        localidadGrid.addColumn(LocalidadBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        localidadGrid.addColumn(LocalidadBean::getProvinciaNombre).setAutoWidth(true).setHeader(new Html("<b>Provincia</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        localidadArrayList = new LocalidadDao().getLista(buscador.getValue().trim(),autonomiaComboBuscador.getValue(), provinciasComoBuscador.getValue());
        localidadGrid.setItems(localidadArrayList);
    }

    @Override
    public void doBinderPropiedades() {

        localidadBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 7))
                .bind(LocalidadBean::getCodigo, LocalidadBean::setCodigo);
        localidadBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 40))
                .bind(LocalidadBean::getNombre, LocalidadBean::setNombre);

        localidadBinder.forField(provinciasComobo)
                .withNullRepresentation(new ProvinciaBean(null, ""))
                .asRequired()
                .bind(LocalidadBean::getProvincia, LocalidadBean::setProvincia);
    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("Localidades");
        buscador.focus();
        buscador.setLabel("Contenido a buscar");
        buscador.setValueChangeMode(ValueChangeMode.EAGER);
        this.provinciasComoBuscador.getEmptyValue();
    }

    @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText(LocalidadBean.TITULO);

        this.contenedorFormulario.add(autonomiaCombo, provinciasComobo, codigo, nombre);

        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, localidadGrid);
        this.contenedorBuscadores.add(buscador, autonomiaComboBuscador, provinciasComoBuscador);

    }

    @Override
    public void doCompentesEventos() {
        localidadGrid.addItemClickListener(event -> {
            localidadBean = event.getItem();
            localidadBinder.readBean(event.getItem());
        }
        );
        
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        
        provinciasComoBuscador.addValueChangeListener(valueChangeEvent -> {
            doActualizaGrid();
        });

        autonomiaCombo.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComoboProvincia(provinciasComobo, autonomiaBean);
        });

        autonomiaComboBuscador.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComoboProvincia(provinciasComoBuscador, autonomiaBean);
        });
    }

    public void doActualizaComoboProvincia(ComboBox<ProvinciaBean> combo, AutonomiaBean autonomia) {
        ArrayList<ProvinciaBean> provinciaArrayList = new ProvinciaDao().getLista(null, autonomia);
        combo.setItems(provinciaArrayList);
    }

    @Override
    public void doImprimir() {
    }

}
