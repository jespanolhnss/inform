package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.bean.DWIndicadorValor;
import es.sacyl.gsa.inform.dao.DWDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 *
 * Formulario para registrar los valores de los indicadores de recursos tabla
 * DW_RECU_INDICADORES
 *
 *
 */
public final class FrmIndicadoresRecursos extends FrmMasterPantalla {

    private final IntegerField anobuscador = new ObjetosComunes().getIntegerField("Año");
    private final IntegerField mesbuscador = new ObjetosComunes().getIntegerField("Mes");
    private final ComboBox<DWIndicador> indicadorBuscador = new CombosUi().getIndicadoresCombo(null, "RECURSOS");

    private final IntegerField ano = new ObjetosComunes().getIntegerField("Año");
    private final IntegerField mes = new ObjetosComunes().getIntegerField("Mes");

    private final ComboBox<DWIndicador> indicador = new CombosUi().getIndicadoresCombo(null, "RECURSOS");

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
    }

    @Override
    public void doBorrar() {
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
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
        dwindicadorValorGrid.addColumn(DWIndicadorValor::getIndicadorNombre).setAutoWidth(true).setHeader(new Html("<b>Área</b>"));
        dwindicadorValorGrid.addColumn(DWIndicadorValor::getValor).setAutoWidth(true).setHeader(new Html("<b>VAlor</b>"));

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        dwindicadorValorArray = new DWDao().getLista(anobuscador.getValue(), mesbuscador.getValue(), indicadorBuscador.getValue(), "DW_RECURSOS");
        dwindicadorValorGrid.setItems(dwindicadorValorArray);
    }

    @Override
    public void doBinderPropiedades() {
        dwindicadorValorBinder.forField(ano)
                .asRequired()
                .withValidator(year -> year >= 20000 && year < 2050,
                        "Desde 2000 a 2050")
                .bind(DWIndicadorValor::getAno, DWIndicadorValor::setAno);

        dwindicadorValorBinder.forField(ano)
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
        contenedorBuscadores.add(anobuscador, mesbuscador, indicadorBuscador);
        contenedorDerecha.add(dwindicadorValorGrid);
    }

    @Override
    public void doCompentesEventos() {
        dwindicadorValorGrid.addItemClickListener(event -> {
            dwindicadorValor = event.getItem();
            dwindicadorValorBinder.readBean(dwindicadorValor);
            doControlBotones(dwindicadorValor);

        });
    }

}
