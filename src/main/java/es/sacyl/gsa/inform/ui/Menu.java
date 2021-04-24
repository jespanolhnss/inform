package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletRequest;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.ctrl.SesionCtrl;
import es.sacyl.gsa.inform.dao.ParametroDao;
import es.sacyl.gsa.inform.ui.covid.FrmTarjetasCribado;
import es.sacyl.gsa.inform.ui.indicadores.FrmIndicadoresCalcular;
import es.sacyl.gsa.inform.ui.indicadores.FrmIndicadoresDefinir;
import es.sacyl.gsa.inform.ui.lopd.FrmLopdIncidenciaGestionar;
import es.sacyl.gsa.inform.ui.lopd.FrmLopdIncidenciaNueva;
import es.sacyl.gsa.inform.ui.lopd.FrmLopdTipos;
import es.sacyl.gsa.inform.ui.recursos.FrmAplicacion;
import es.sacyl.gsa.inform.ui.recursos.FrmEquipos;
import es.sacyl.gsa.inform.ui.recursos.FrmIp;
import es.sacyl.gsa.inform.ui.recursos.FrmVlan;
import es.sacyl.gsa.inform.ui.tablas.FrmAutonomias;
import es.sacyl.gsa.inform.ui.tablas.FrmCentro;
import es.sacyl.gsa.inform.ui.tablas.FrmCentroTipo;
import es.sacyl.gsa.inform.ui.tablas.FrmCombos;
import es.sacyl.gsa.inform.ui.tablas.FrmFuncionalidad;
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
import es.sacyl.gsa.inform.ui.usuarios.FrmUsuarios;
import es.sacyl.gsa.inform.ui.usuarios.FrmUsuariosPedir;
import es.sacyl.gsa.inform.ui.viajes.FrmViajesRegistrar;
import java.time.LocalDateTime;

/**
 *
 * @author 06551256M
 *
 * https://vaadin.com/directory/component/hybridmenu/links
 */
public class Menu extends MenuBar {

    private VerticalLayout contenedorFormularios;
    private UbicacionBean usarioBean;

    private static final String ACTIVIDAD = "Actividad";
    private static final String COVID = "Covid";
    private static final String INDICADORES = "Indicadores";
    private static final String LOPD = "Lopd";
    private static final String RECURSOS = "Recursos";
    private static final String USUARIOS = "Usuarios";
    private static final String TABLAS = "Tablas";

    public Menu(VerticalLayout contenedorFormularios, UsuarioBean usuarioBean) {
        this.contenedorFormularios = contenedorFormularios;
        this.usarioBean = usarioBean;

        if (usuarioBean.tieneLaFuncionalidad(ACTIVIDAD) == true) {
            MenuItem viajes = this.addItem(ACTIVIDAD);
            SubMenu viajesSubmenu = viajes.getSubMenu();
            viajesSubmenu.addItem("Viajes", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmViajesRegistrar());
            });
        }
        if (usuarioBean.tieneLaFuncionalidad(COVID)) {
            MenuItem covid = this.addItem(COVID);
            SubMenu covidSubmenu = covid.getSubMenu();
            covidSubmenu.addItem("Tarjetas", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmTarjetasCribado());
            });
        }
        if (usuarioBean.tieneLaFuncionalidad(INDICADORES)) {
            MenuItem indicadores = this.addItem(INDICADORES);
            SubMenu indicadoresSubmenu = indicadores.getSubMenu();
            indicadoresSubmenu.addItem("Definición  ", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmIndicadoresDefinir());
            });
            indicadoresSubmenu.addItem("ETL His ", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmIndicadoresCalcular());
            });

        }
        if (usuarioBean.tieneLaFuncionalidad(LOPD)) {
            MenuItem lopd = this.addItem(LOPD);

            SubMenu lopdSubMenu = lopd.getSubMenu();
            lopdSubMenu.addItem("Nueva", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmLopdIncidenciaNueva(new LopdIncidenciaBean(LocalDateTime.now())));
            });

            lopdSubMenu.addItem("Gestionar", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmLopdIncidenciaGestionar());
            });
            ;
            lopdSubMenu.addItem("Tipos Incidencias", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmLopdTipos(new LopdTipoBean()));
            });
        }
        if (usuarioBean.tieneLaFuncionalidad(RECURSOS)) {
            MenuItem recursos = this.addItem(RECURSOS);
            SubMenu recursosSubMenu = recursos.getSubMenu();
            recursosSubMenu.addItem("Aplicaciones", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmAplicacion());
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
        }
        if (usuarioBean.tieneLaFuncionalidad(USUARIOS)) {
            MenuItem usuarios = this.addItem(USUARIOS);
            SubMenu usuariosSubMenu = usuarios.getSubMenu();
            usuariosSubMenu.addItem("Pedir", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmUsuariosPedir());
            });

            usuariosSubMenu.addItem("Solicitudes", e -> {
            });
            usuariosSubMenu.addItem("Gestionar", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmUsuarios());
            });

        }
        if (usuarioBean.tieneLaFuncionalidad(TABLAS)) {
            MenuItem tablas = this.addItem(TABLAS);

            SubMenu tablasSubMenu = tablas.getSubMenu();
            tablasSubMenu.addItem("Autonomias", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmAutonomias());
            });

            tablasSubMenu.addItem("Centros", (ClickEvent<MenuItem> e) -> {
                Menu.this.contenedorFormularios.removeAll();
                FrmCentro frmCentro = new FrmCentro();
                Menu.this.contenedorFormularios.add(frmCentro);
            });

            tablasSubMenu.addItem("CentrosTipos", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmCentroTipo());
            });

            tablasSubMenu.addItem("Combos", (ClickEvent<MenuItem> e) -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmCombos());
            });

            tablasSubMenu.addItem("Funcionalidad", (ClickEvent<MenuItem> e) -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmFuncionalidad());
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

            tablasSubMenu.addItem("Categorias Personal", (ClickEvent<MenuItem> e) -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmUsuarioCategoria());
            });

            tablasSubMenu = tablas.getSubMenu();
            tablasSubMenu.addItem("Zonas Básicas Salud", e -> {
                this.contenedorFormularios.removeAll();
                this.contenedorFormularios.add(new FrmZona());
            });

        }

        //  UsuarioBean usuarioBean = ((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
        String itemNombre = "Misc";
        if (usuarioBean
                != null && usuarioBean.getNombre()
                != null && !usuarioBean.getNombre().isEmpty()) {
            itemNombre = usuarioBean.getNombre();
        }
        MenuItem miscelanea = this.addItem(itemNombre);

        SubMenu miscelaneaSubMenu = miscelanea.getSubMenu();

        miscelaneaSubMenu.addItem(
                "Navegador", e -> {
                    Grid<DatoGenericoBean> grid = new Grid();
                    grid.addColumn(DatoGenericoBean::getTipoDato);
                    grid.addColumn(DatoGenericoBean::getValor);
                    //    grid.setItems(Utilidades.getInformacionCliente(getUI()));
                    //  VentanaPdf ventanaPdf = new VentanaPdf(itemNombre);
                }
        );

        miscelaneaSubMenu.addItem(
                "Salir", e -> {
                    SesionCtrl.doDestruyeSesionUsuario();
                    Page page = new Page(getUI().get());
                    String url, adr, port;
                    VaadinRequest currentRequest = VaadinRequest.getCurrent();
                    VaadinServletRequest vaadinServletRequest = null;
                    if (currentRequest instanceof VaadinServletRequest) {
                        vaadinServletRequest = (VaadinServletRequest) currentRequest;
                        adr = vaadinServletRequest.getLocalAddr();
                        if (adr.charAt(0) == "0".charAt(0)) {
                            adr = "localhost";
                        }
                        port = Integer.toString(vaadinServletRequest.getLocalPort());
                        url = "http://" + adr + ":" + port + "/inform";
                    } else {
                        adr = new ParametroDao().getPorCodigo(ParametroBean.URL_INSTANCIASERVIDOR).getValor();
                        url = "http://" + adr + "/inform";
                    }
                    // falta las instrucciones para matar la sesion
                    page.open(url);
                }
        );

        this.contenedorFormularios.removeAll();
        this.contenedorFormularios.add(new ViewPortada());

    }

    public VerticalLayout getContenedorFormularios() {
        return contenedorFormularios;
    }

    public void setContenedorFormularios(VerticalLayout contenedorFormularios) {
        this.contenedorFormularios = contenedorFormularios;
    }

}
