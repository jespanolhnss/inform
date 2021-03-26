package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.treegrid.TreeGrid;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.dao.UbicacionDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public final class FrmBuscaUbicacion extends Dialog {

    private final Button botonCancelar = new ObjetosComunes().getBoton("Cancela", null, VaadinIcon.CLOSE_CIRCLE.create());

    private final TreeGrid<UbicacionBean> ubicacionGrid = new TreeGrid<>();
    private ComboBox<UbicacionBean> ubicacionCombo = null;
    private UbicacionBean ubicacionBean = new UbicacionBean();
    ArrayList<UbicacionBean> ubicacionArrayList = new ArrayList<>();

    public FrmBuscaUbicacion(CentroBean centroBean) {

        this.setWidth("600px");
        this.setHeight("600px");
        ubicacionCombo = new CombosUi().getUbicacionCombo(null,
                centroBean, null, null);
        ubicacionArrayList = new UbicacionDao().getListaPadresCentro(centroBean);
        doGrid();
        doComponentesOrganizacion();
        doCompentesEventos();
    }

    public void doGrid() {
        ubicacionCombo.setItems(ubicacionArrayList);
        ubicacionGrid.setHeightByRows(true);
        ubicacionGrid.setPageSize(20);
        ubicacionGrid.setItems(ubicacionArrayList,
                UbicacionBean::getHijos);
        ubicacionGrid.addHierarchyColumn(UbicacionBean::getPadreString)
                .setHeader("Ubicación Padre");
        ubicacionGrid.addColumn(UbicacionBean::getDescripcion).setHeader("Descripción");
        //  ubicacionGrid.addColumn(UbicacionBean::getNivel).setHeader("Nivel");
        ubicacionGrid.setHeightByRows(true);
        ubicacionGrid.setPageSize(35);
        ubicacionGrid.setItems(ubicacionArrayList,
                UbicacionBean::getHijos);
        ubicacionGrid.expand(ubicacionArrayList);
    }

    public void doComponentesOrganizacion() {
        add(botonCancelar, ubicacionCombo, ubicacionGrid);

    }

    public void doCompentesEventos() {
        botonCancelar.addClickListener(eve -> {
            this.close();
        });
        ubicacionCombo.addValueChangeListener(event -> {
            ArrayList<UbicacionBean> ubicacionArrayList = new ArrayList<>();
            ubicacionArrayList.add(ubicacionCombo.getValue());
            ubicacionGrid.setItems(ubicacionArrayList,
                    UbicacionBean::getHijos);
            ubicacionGrid.expand(ubicacionArrayList);
            ubicacionGrid.expandRecursively(ubicacionArrayList, 5);

        });
        ubicacionGrid.addItemClickListener(event -> {
            ubicacionBean = event.getItem();
            this.close();
        });
    }

    public UbicacionBean getUbicacionBean() {
        return ubicacionBean;
    }

    public void setUbicacionBean(UbicacionBean ubicacionBean) {
        this.ubicacionBean = ubicacionBean;
    }

}
