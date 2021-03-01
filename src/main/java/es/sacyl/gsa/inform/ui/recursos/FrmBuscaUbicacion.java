package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.treegrid.TreeGrid;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.dao.UbicacionDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class FrmBuscaUbicacion extends Dialog {

    private final TreeGrid<UbicacionBean> ubicacionGrid = new TreeGrid<>();
    private ComboBox<UbicacionBean> ubicacionCombo = null;
    private UbicacionBean ubicacionBean = new UbicacionBean();

    public FrmBuscaUbicacion(CentroBean centroBean) {

        this.setWidth("500px");
        this.setHeight("300px");
        ubicacionCombo = new CombosUi().getUbicacionCombo(null,
                centroBean, null, null);

        ArrayList<UbicacionBean> ubicacionArrayList = new UbicacionDao().getListaPadresCentro(centroBean);
        ubicacionGrid.setHeightByRows(true);
        ubicacionGrid.setPageSize(20);
        ubicacionGrid.setItems(ubicacionArrayList,
                UbicacionBean::getHijos);
        ubicacionGrid.addHierarchyColumn(UbicacionBean::getPadreString)
                .setHeader("Ubicación Padre");
        ubicacionGrid.addColumn(UbicacionBean::getDescripcion).setHeader("Descripción");
        ubicacionGrid.addColumn(UbicacionBean::getNivel).setHeader("Nivel");
        ubicacionGrid.setHeightByRows(true);
        ubicacionGrid.setPageSize(35);
        ubicacionGrid.setItems(ubicacionArrayList,
                UbicacionBean::getHijos);
        ubicacionGrid.expand(ubicacionArrayList);
        ubicacionGrid.addItemClickListener(event -> {
            ubicacionBean = event.getItem();
            this.close();
        });
        add(ubicacionCombo, ubicacionGrid);
    }

    public UbicacionBean getUbicacionBean() {
        return ubicacionBean;
    }

    public void setUbicacionBean(UbicacionBean ubicacionBean) {
        this.ubicacionBean = ubicacionBean;
    }

}
