package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author 06551256M
 */
public class ViewPortada extends VerticalLayout {

    Accordion accordion = new Accordion();

    public ViewPortada() {
        this.add(accordion);
        doCajaInformaticos();
        /*
        this.add(new Grafico1());
        this.add(new Grafico2());
        this.add(new Grafico3());
        this.add(new Grafico4());
        this.add(new Grafico5());
        this.add(new Grafico6());
        this.add(new Grafico7());
        this.add(new Grafico8());
        this.add(new Grafico9());
        this.add(new Grafico10());
        this.add(new Grafico11());
        this.add(new Grafico12());
        this.add(new Grafico14());
         */
    }

    public void doCajaInformaticos() {

        Grid<UsuarioBean> informaticosGrid = new Grid<>();
        informaticosGrid.setWidth("230px");
        informaticosGrid.addColumn(UsuarioBean::getNombreAp1_Ap2_).setAutoWidth(true);
        informaticosGrid.addColumn(UsuarioBean::getTelefono).setAutoWidth(true);
        ArrayList<UsuarioBean> listaUsuarios = new UsuarioDao().getInformaticos();

        Collections.sort(listaUsuarios, new Comparator<UsuarioBean>() {
            @Override
            public int compare(UsuarioBean p1, UsuarioBean p2) {
                return new String(p1.getNombreAp1_Ap2_()).compareTo(new String(p2.getNombreAp1_Ap2_()));
            }
        });
        informaticosGrid.setItems(listaUsuarios);
        informaticosGrid.setHeightByRows(true);
        informaticosGrid.setPageSize(listaUsuarios.size());
        informaticosGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.MATERIAL_COLUMN_DIVIDERS, GridVariant.LUMO_ROW_STRIPES);

        accordion.add("Servicio de informática (" + listaUsuarios.size() + ")       ", informaticosGrid);
        // add(informaticosDetails);
    }
}
