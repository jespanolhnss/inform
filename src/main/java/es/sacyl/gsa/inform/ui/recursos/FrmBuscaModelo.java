package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.grid.GridVariant;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public class FrmBuscaModelo extends FrmMasterVentana {

    private String grupo;
    private String modelo = null;
    private PaginatedGrid<String> ramaGrid = new PaginatedGrid<>();

    public FrmBuscaModelo(String grupo) {
        super("500px");
        this.titulo.setText(grupo);
        this.grupo = grupo;
        doGrid();
        doActualizaGrid();
        doComponenesAtributos();
        doComponentesOrganizacion();
        doCompentesEventos();
        doBinderPropiedades();
    }

    @Override
    public void doGrabar() {
    }

    @Override
    public void doBorrar() {
    }

    @Override
    public void doCancelar() {
        this.close();
    }

    @Override
    public void doCerrar() {
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
    }

    @Override
    public void doGrid() {

        ramaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        ramaGrid.setHeightByRows(true);
        ramaGrid.setPageSize(25);
        ramaGrid.setPaginatorSize(25);
        ramaGrid.addColumn((String) -> {
            return String; //To change body of generated lambdas, choose Tools | Templates.
        }).setHeader("Modelo");
        //ramaGrid.addColumn(grupo);

    }

    @Override
    public void doActualizaGrid() {
        ArrayList<String> lista = new ComboDao().getListaGruposRamaValor(ComboBean.TIPOEQUIPOMARCAMODELO, grupo, 100);
        ramaGrid.setItems(lista);
    }

    @Override
    public void doBinderPropiedades() {
    }

    @Override
    public void doComponenesAtributos() {
        botonAyuda.setVisible(false);
        botonBorrar.setVisible(false);
        botonGrabar.setVisible(false);
        botonLimpiar.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        this.add(ramaGrid);
    }

    @Override
    public void doCompentesEventos() {
        ramaGrid.addItemClickListener(event -> {
            modelo = event.getItem();
            this.close();
        });

    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

}
