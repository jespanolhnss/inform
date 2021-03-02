package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.ctrl.SesionCtrl;
import es.sacyl.gsa.inform.ui.covid.FrmTarjetasCribado;
import es.sacyl.gsa.inform.ui.lopd.FrmLopdIncidenciaGestionar;
import es.sacyl.gsa.inform.ui.lopd.FrmLopdIncidenciaNueva;
import es.sacyl.gsa.inform.ui.lopd.FrmLopdTipos;
import es.sacyl.gsa.inform.ui.recursos.FrmAplicaciones;
import es.sacyl.gsa.inform.ui.recursos.FrmEquipos;
import es.sacyl.gsa.inform.ui.recursos.FrmIp;
import es.sacyl.gsa.inform.ui.recursos.FrmVlan;
import es.sacyl.gsa.inform.ui.tablas.FrmAutonomias;
import es.sacyl.gsa.inform.ui.tablas.FrmCentro;
import es.sacyl.gsa.inform.ui.tablas.FrmCentroTipo;
import es.sacyl.gsa.inform.ui.tablas.FrmCombos;
import es.sacyl.gsa.inform.ui.tablas.FrmGerencia;
import es.sacyl.gsa.inform.ui.tablas.FrmGfh;
import es.sacyl.gsa.inform.ui.tablas.FrmLocalidad;
import es.sacyl.gsa.inform.ui.tablas.FrmNivelesAtencion;
import es.sacyl.gsa.inform.ui.tablas.FrmNivelesAtencionTipo;
import es.sacyl.gsa.inform.ui.tablas.FrmParametro;
import es.sacyl.gsa.inform.ui.tablas.FrmProveedor;
import es.sacyl.gsa.inform.ui.tablas.FrmProvincia;
import es.sacyl.gsa.inform.ui.tablas.FrmUbicacion;
import es.sacyl.gsa.inform.ui.tablas.FrmUsuarioCategoria;
import es.sacyl.gsa.inform.ui.tablas.FrmZona;
import es.sacyl.gsa.inform.ui.usuarios.FrmUsuariosPedir;
import es.sacyl.gsa.inform.ui.viajes.FrmViajesRegistrar;
import java.time.LocalDateTime;

/**
 *
 * @author 06551256M
 */
public class Menu extends MenuBar {

    VerticalLayout contenedorFormularios;

    public Menu(VerticalLayout contenedorFormularios) {
        this.contenedorFormularios = contenedorFormularios;

        MenuItem covid = this.addItem("Covid");

        SubMenu covidSubmenu = covid.getSubMenu();
        covidSubmenu.addItem("Tarjetas", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmTarjetasCribado());
        });

        MenuItem viajes = this.addItem("Viajes");

        SubMenu viajesSubmenu = viajes.getSubMenu();
        viajesSubmenu.addItem("Nuevo", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmViajesRegistrar());
        });

        MenuItem usuarios = this.addItem("Usuarios");

        SubMenu usuariosSubMenu = usuarios.getSubMenu();
        usuariosSubMenu.addItem("Nuevo", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmUsuariosPedir());
        });

        MenuItem recursos = this.addItem("Recursos");

        SubMenu recursosSubMenu = recursos.getSubMenu();
        recursosSubMenu.addItem("Aplicaciones", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmAplicaciones());
        });

        recursosSubMenu.addItem("Equipos", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmEquipos());
        });
        recursosSubMenu.addItem("Ip", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmIp());
        });
        recursosSubMenu.addItem("Vlan", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmVlan());
        });

        MenuItem lopd = this.addItem("Lopd");

        SubMenu lopdSubMenu = lopd.getSubMenu();
        lopdSubMenu.addItem("Nueva", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmLopdIncidenciaNueva(new LopdIncidenciaBean(LocalDateTime.now())));
        });

        lopdSubMenu.addItem("Gestionar", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmLopdIncidenciaGestionar());
        });
        lopdSubMenu.addItem("Interconsultas", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmLopdIncidenciaGestionar());
        });

        lopdSubMenu.addItem("Tipos", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmLopdTipos(new LopdTipoBean()));
        });
        MenuItem tablas = this.addItem("Tablas");

        SubMenu tablasSubMenu = tablas.getSubMenu();
        tablasSubMenu.addItem("Autonomias", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmAutonomias());
        });

        tablasSubMenu.addItem("Centros", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmCentro());
        });

        tablasSubMenu.addItem("CentrosTipos", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmCentroTipo());
        });

        tablasSubMenu.addItem("Combos", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmCombos());
        });

        tablasSubMenu.addItem("Gerencias", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmGerencia());
        });
        tablasSubMenu.addItem("Gfh", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmGfh());
        });
        tablasSubMenu.addItem("Localidades", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmLocalidad());
        });

        tablasSubMenu.addItem("Niveles", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmNivelesAtencion());
        });

        tablasSubMenu.addItem("Niveles Tipos", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmNivelesAtencionTipo());
        });
        tablasSubMenu.addItem("Parametros", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmParametro());
        });

        tablasSubMenu.addItem("Proveedores", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmProveedor());
        });
        tablasSubMenu.addItem("Provincias", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmProvincia());
        });

        tablasSubMenu.addItem("Ubicaciones", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmUbicacion(new UbicacionBean()));
        });

        tablasSubMenu.addItem("UsuarioCategorias", (ClickEvent<MenuItem> e) -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmUsuarioCategoria());
        });

        tablasSubMenu = tablas.getSubMenu();
        tablasSubMenu.addItem("Zonas Básicas alud", e -> {
            this.contenedorFormularios.removeAll();
            this.contenedorFormularios.add(new FrmZona());
        });

        this.addItem("Salir", e -> {
            this.removeAll();
            SesionCtrl.doDestruyeSesionUsuario();
            Page page = new Page(getUI().get());
            page.open("http://localhost:8080/inform");
        });
    }

    public VerticalLayout getContenedorFormularios() {
        return contenedorFormularios;
    }

    public void setContenedorFormularios(VerticalLayout contenedorFormularios) {
        this.contenedorFormularios = contenedorFormularios;
    }

}
