package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
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

/**
 *
 * @author 06551256M
 */
public class ViewPortada extends VerticalLayout {

    Details informaticosDetails;
    Grid<UsuarioBean> informaticosGrid = new Grid<>();

    public ViewPortada() {

        doCajaInformaticos();
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
    }

    public void doCajaInformaticos() {

        informaticosGrid.addColumn(UsuarioBean::getApellidosNombre);
        informaticosGrid.addColumn(UsuarioBean::getTelefono);
        ArrayList<UsuarioBean> listaUsuarios = new UsuarioDao().getInformaticos();
        informaticosGrid.setItems(listaUsuarios);

        informaticosDetails = new Details("Informáticos (" + listaUsuarios.size() + ")", informaticosGrid);
        informaticosDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

        add(informaticosDetails);
    }
}
