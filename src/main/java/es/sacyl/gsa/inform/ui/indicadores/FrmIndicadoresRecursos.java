package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.bean.DWIndicadorValor;
import es.sacyl.gsa.inform.dao.DWDao;
import es.sacyl.gsa.inform.dao.DWIndicadorDao;
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
 *
 * Formulario para registrar los valores de los indicadores de recursos tabla
 * DW_RECU_INDICADORES
 *
 * Estos indicadores cambian poco en el tiempo. Por cada cambio se debe añadir
 * una fila con el año mes e indicador que ha cambiado. La seleccion de estos
 * datos se hace tomando el valor mas próximo a la fecha del estudio.
 *
 *
 */
public final class FrmIndicadoresRecursos extends FrmMasterPantalla {

    private final IntegerField anobuscador = new ObjetosComunes().getIntegerField("Año");
    private final IntegerField mesbuscador = new ObjetosComunes().getIntegerField("Mes");
    public ComboBox<String> areaBuscador = new CombosUi().getCombodeTabla("Área Actividad", null, ComboBean.DWAREASINDICADORES, 60);
    //   private final ComboBox<DWIndicador> indicadorBuscador = new CombosUi().getIndicadoresCombo(areaBuscador.getValue(), DWIndicador.TIPORECURSOS);
    private final ComboBox<DWIndicador> indicadorBuscador = new CombosUi().getIndicadoresCombo(areaBuscador.getValue(), DWIndicador.TIPORECURSOS);

    private final IntegerField ano = new ObjetosComunes().getAno();
    private final IntegerField mes = new ObjetosComunes().getMes();

    private final ComboBox<DWIndicador> indicador = new CombosUi().getIndicadoresCombo(null, DWIndicador.TIPORECURSOS);

    private final TextField dimension1 = new ObjetosComunes().getTextField("Dimension 1");
    private final TextField dimension2 = new ObjetosComunes().getTextField("Dimension 2");

    private final IntegerField valor = new ObjetosComunes().getIntegerField("Valor");

    private DWIndicadorValor dwindicadorValor = new DWIndicadorValor();
    public PaginatedGrid<DWIndicadorValor> dwindicadorValorGrid = new PaginatedGrid<>();
    public ArrayList<DWIndicadorValor> dwindicadorValorArray = new ArrayList<>();
    public Binder<DWIndicadorValor> dwindicadorValorBinder = new Binder<>();

    public FrmIndicadoresRecursos() {
        super();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
        if (dwindicadorValorBinder.writeBeanIfValid(dwindicadorValor)) {
            if (new DWDao().doGrabaDatos(dwindicadorValor, "DW_RECU_INDICADORES") == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<DWIndicadorValor> validate = dwindicadorValorBinder.validate();
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
                    new DWDao().doBorraDatos(dwindicadorValor, "DW_RECU_INDICADORES");
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
        dwindicadorValor = new DWIndicadorValor();
        dwindicadorValorBinder.readBean(dwindicadorValor);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        dwindicadorValorGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        dwindicadorValorGrid.setHeightByRows(true);
        dwindicadorValorGrid.setPageSize(25);
        dwindicadorValorGrid.setPaginatorSize(25);
        dwindicadorValorGrid.addColumn(DWIndicadorValor::getAno).setAutoWidth(true).setHeader(new Html("<b>Ano</b>"));
        dwindicadorValorGrid.addColumn(DWIndicadorValor::getMes).setAutoWidth(true).setHeader(new Html("<b>Mes</b>"));
        dwindicadorValorGrid.addColumn(DWIndicadorValor::getIndicadorNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        dwindicadorValorGrid.addColumn(DWIndicadorValor::getValor).setAutoWidth(true).setHeader(new Html("<b>Valor</b>"));

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        dwindicadorValorArray = new DWDao().getLista(anobuscador.getValue(), mesbuscador.getValue(), indicadorBuscador.getValue(), "DW_RECU_INDICADORES");

        dwindicadorValorGrid.setItems(dwindicadorValorArray);
    }

    @Override
    public void doBinderPropiedades() {
        dwindicadorValorBinder.forField(ano)
                .asRequired()
                .withValidator(year -> year >= 2000 && year < 2050,
                        "Desde 2000 a 2050")
                .bind(DWIndicadorValor::getAno, DWIndicadorValor::setAno);

        dwindicadorValorBinder.forField(mes)
                .asRequired()
                .withValidator(mes -> mes >= 1 && mes < 12,
                        "Desde 1 a 12")
                .bind(DWIndicadorValor::getMes, DWIndicadorValor::setMes);

        dwindicadorValorBinder.forField(indicador)
                .asRequired()
                .bind(DWIndicadorValor::getIndicador, DWIndicadorValor::setIndicador);

        dwindicadorValorBinder.forField(valor)
                .asRequired()
                .bind(DWIndicadorValor::getValor, DWIndicadorValor::setValor);

    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("Registro datos de recursos");

    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(ano, mes, indicador, dimension1, dimension2, valor);

        contenedorBuscadores.removeAll();
        contenedorBuscadores.add(anobuscador, mesbuscador, areaBuscador, indicadorBuscador);
        contenedorDerecha.add(dwindicadorValorGrid);
    }

    @Override
    public void doCompentesEventos() {

        areaBuscador.addValueChangeListener(event -> {
            indicadorBuscador.setItems(new DWIndicadorDao().getLista(areaBuscador.getValue(), null, DWIndicador.TIPORECURSOS));
        });
        indicadorBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        dwindicadorValorGrid.addItemClickListener(event -> {
            dwindicadorValor = event.getItem();
            dwindicadorValorBinder.readBean(dwindicadorValor);
            doControlBotones(dwindicadorValor);
        });
    }

}
