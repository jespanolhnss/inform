package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.graficos.Grafico1;
import es.sacyl.gsa.inform.ui.graficos.Grafico10;
import es.sacyl.gsa.inform.ui.graficos.Grafico11;
import es.sacyl.gsa.inform.ui.graficos.Grafico12;
import es.sacyl.gsa.inform.ui.graficos.Grafico14;
import es.sacyl.gsa.inform.ui.graficos.Grafico2;
import es.sacyl.gsa.inform.ui.graficos.Grafico3;
import es.sacyl.gsa.inform.ui.graficos.Grafico4;
import es.sacyl.gsa.inform.ui.graficos.Grafico5;
import es.sacyl.gsa.inform.ui.graficos.Grafico6;
import es.sacyl.gsa.inform.ui.graficos.Grafico7;
import es.sacyl.gsa.inform.ui.graficos.Grafico8;
import es.sacyl.gsa.inform.ui.graficos.Grafico9;
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
        //     doGraficos();

    }

    public void doGraficos() {
        add(new Grafico1());
        add(new Grafico2());
        add(new Grafico3());
        add(new Grafico4());
        add(new Grafico5());
        add(new Grafico6());
        add(new Grafico7());
        add(new Grafico8());
        add(new Grafico9());
        add(new Grafico10());
        add(new Grafico11());
        add(new Grafico12());
        add(new Grafico14());

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
        accordion.close();
        // add(informaticosDetails);
    }
}
