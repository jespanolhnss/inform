package es.sacyl.gsa;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.ctrl.LlamdasExternas;
import es.sacyl.gsa.inform.ctrl.SesionCtrl;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.exceptiones.CustomExceptionHandler;
import es.sacyl.gsa.inform.exceptiones.LoginException;
import es.sacyl.gsa.inform.ui.Menu;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Ldap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * The main view contains a button and a click listener.
 */
@Route
@PWA(name = "Inform", shortName = "Inform")
@CssImport(value = "./styles/styles.css", themeFor = "vaadin-grid")
@HtmlImport("frontend://styles/styles.html")
/*
parametros
https://vaadin.com/forum/thread/17101015/webinar-introduccion-a-vaadin-flow-en-espanol-spanish
 */
/**
 * AttachNotifier
 *
 * HasUrlParameter
 */
public class MainView extends VerticalLayout implements AttachNotifier, HasUrlParameter<String> {

    private Location currentLocation = null;

    private HorizontalLayout contenedorMenu = new HorizontalLayout();
    private VerticalLayout contenedorFormularios = new VerticalLayout();
    QueryParameters qm = null;

    public MainView() {
        //   this.getStyle().set("background-color", "#F2F2F2");
        Image image = new Image("icons/fondo.jpg", "Imagen");
        add(image);
        contenedorMenu.setMargin(false);
        contenedorFormularios.setMargin(false);
        contenedorFormularios.setSpacing(false);
        contenedorFormularios.setPadding(false);

        doTimerDa0();

        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("catalina.base"));
        System.out.println(VaadinServlet.getCurrent().getServletInfo());
        System.out.println(((HttpServletRequest) VaadinRequest.getCurrent()).getPathTranslated());

        System.out.println("addr" + ((HttpServletRequest) VaadinRequest.getCurrent()).getLocalAddr());
        System.out.println("name" + ((HttpServletRequest) VaadinRequest.getCurrent()).getLocalName());
        System.out.println("port" + ((HttpServletRequest) VaadinRequest.getCurrent()).getLocalPort());

        this.setMargin(false);
        this.setSpacing(false);
        this.setAlignItems(Alignment.CENTER);
        UsuarioBean usuario = ((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME));
        if (usuario == null) {
            this.doLogin();
        } else {
            domuestraMenu(usuario);
        }
    }

    public void init() {

    }

    // https://vaadin.com/forum/thread/18453061/vaadin-error-handling-14
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().setErrorHandler(new CustomExceptionHandler());
    }

    @Override
    /**
     * Este método no se como funciona pero se usa para recuperar parámetros
     * request en la llamada
     */
    public void setParameter(BeforeEvent event,
            @OptionalParameter String parameter
    ) {
        // called before onAttach
        currentLocation = event.getLocation();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent
    ) {
        super.onAttach(attachEvent);
        qm = currentLocation.getQueryParameters();
        if (qm != null && qm.getParameters().size() > 0) {
            this.add(contenedorFormularios);
            new LlamdasExternas(qm, this);
        }
    }

    public void domuestraMenu(UsuarioBean usuarioBean) {
        this.removeAll();
        this.setMargin(false);
        this.setAlignItems(Alignment.START);
        this.setHeightFull();
        // para que ponga barra de desplazamiento vertical
        this.getStyle().set("overflow", "auto");
        contenedorMenu.setMargin(false);
        contenedorMenu.setSpacing(false);
        contenedorMenu.setHeight("18px");
        contenedorFormularios.setMargin(false);
        contenedorFormularios.setSpacing(false);
        this.add(contenedorMenu);
        this.add(contenedorFormularios);
        contenedorMenu.add(new Menu(contenedorFormularios, usuarioBean));
    }

    /**
     *
     * @param user
     * @param pass
     * @return
     */
    public UsuarioBean authenticate(String user, String pass) {
        //UsuarioBean usuario = new UsuarioDao().getUsuarioDni(user, Boolean.FALSE);
        UsuarioBean usuario = null;
        try {
            Ldap ldap = new Ldap();
            usuario = ldap.loginActiveDirectory(user, pass);
        } catch (LoginException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (usuario != null || usuario.getDni() == null && !usuario.getDni().isEmpty()) {
            usuario = new UsuarioDao().getUsuarioDni(user, Boolean.TRUE);
            SesionCtrl.doCreaSesionUsuario(usuario);
        } else {
            SesionCtrl.doCreaSesionUsuario(usuario);
        }
        return usuario;
    }

    /**
     *
     */
    public void doLogin() {
        LoginForm componentLogin = new LoginForm();
        componentLogin.setForgotPasswordButtonVisible(false);
        componentLogin.setI18n(createEspanolI18n());
        componentLogin.addLoginListener(e -> {
            UsuarioBean usuario = authenticate(e.getUsername(), e.getPassword());
            if (usuario != null) {
                this.domuestraMenu(usuario);
            } else {
                componentLogin.setError(true);
            }
        });
        this.add(componentLogin);
    }

    private LoginI18n createEspanolI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Informática");
        i18n.getHeader().setDescription("Aplicaciones internas del servicio ");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setTitle("Informática GSA");
        i18n.getForm().setSubmit("Conectar");
        i18n.getForm().setPassword("Clave");
        i18n.getForm().setForgotPassword("Resetear clave");
        i18n.getErrorMessage().setTitle("Datos no válidos");
        i18n.getErrorMessage()
                .setMessage("Registra de nuevo el dato del usuario y la clave");
        i18n.setAdditionalInformation(
                " Informática 01-03-2021");
        return i18n;
    }

    public HorizontalLayout getContenedorMenu() {
        return contenedorMenu;
    }

    public void setContenedorMenu(HorizontalLayout contenedorMenu) {
        this.contenedorMenu = contenedorMenu;
    }

    public VerticalLayout getContenedorFormularios() {
        return contenedorFormularios;
    }

    public void setContenedorFormularios(VerticalLayout contenedorFormularios) {
        this.contenedorFormularios = contenedorFormularios;
    }

    /**
     * Cada dos minuto lanza una conexión para que el fw no cierre las
     * conesiones a la bbdd
     *
     */
    public static void doTimerDa0() {
        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            ConexionDao conexionDao = new ConexionDao();

            @Override
            public void run() {
                conexionDao.isTestConexion();
            }
        };
        timerObj.schedule(timerTaskObj, 0, 120000);
    }
}
