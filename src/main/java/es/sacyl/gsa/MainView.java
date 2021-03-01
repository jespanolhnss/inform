package es.sacyl.gsa;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletRequest;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.ctrl.LlamdasExternas;
import es.sacyl.gsa.inform.ctrl.SesionCtrl;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.Menu;
import java.util.Enumeration;

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

    //  private static final Logger LOGGER = LogManager.getLogger(MainView.class);
    private final HorizontalLayout contenedorMenu = new HorizontalLayout();
    private final VerticalLayout contenedorFormularios = new VerticalLayout();
    QueryParameters qm = null;

    public MainView() {
        Image image = new Image("icons/fondo.jpg", "DummyImage");
        add(image);
        //  LOGGER.info(" Inicio mainview " + LocalDateTime.now());

        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("catalina.base"));
        System.out.println(VaadinServlet.getCurrent().getServletInfo());
        //  System.out.println(  VaadinServlet.getCurrent().getInitParameterNames());
        /* Pruebas 11-02-2021 */
        Notification.show("hola");
        VaadinServletRequest req = (VaadinServletRequest) VaadinService.getCurrentRequest();
        String parametro = req.getParameter("nhc");
        String u = VaadinService.getCurrentRequest().getParameter("nhc");
        /* fin de las pruebas */
        Enumeration<String> enumerar = VaadinServlet.getCurrent().getInitParameterNames();
        while (enumerar.hasMoreElements()) {
            System.out.println(enumerar.nextElement());
        }

        System.out.println(System.getProperty("catalina.base"));
        System.out.println(VaadinServlet.getCurrent().getServletInfo());
        //  System.out.println(  VaadinServlet.getCurrent().getInitParameterNames());

        this.setMargin(false);
        this.setSpacing(false);
        this.setAlignItems(Alignment.CENTER);

        this.doLogin();

    }

    @Override
    /**
     * Este método no se como funciona pero se usa para recuperar parámetros en
     * la llamada
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

    public void domuestraMenu() {
        this.removeAll();
        this.setMargin(false);
        this.setAlignItems(Alignment.START);
        contenedorMenu.setMargin(false);
        contenedorMenu.setSpacing(false);
        contenedorMenu.setHeight("18px");
        contenedorFormularios.setMargin(false);
        contenedorFormularios.setSpacing(false);
        this.add(contenedorMenu);
        this.add(contenedorFormularios);
        contenedorMenu.add(new Menu(contenedorFormularios));
    }

    public boolean authenticate(String user, String passe3) {
        UsuarioBean usuario = new UsuarioDao().getUsuarioDni(user, Boolean.FALSE);
        if (usuario != null) {
            SesionCtrl.doCreaSesionUsuario(usuario);
        } else {
            SesionCtrl.doCreaSesionUsuario(usuario);
        }
        return true;
    }

    public void doLogin() {
        LoginForm componentLogin = new LoginForm();
        componentLogin.setForgotPasswordButtonVisible(false);
        componentLogin.setI18n(createEspanolI18n());
        componentLogin.addLoginListener(e -> {
            boolean isAuthenticated = authenticate(e.getUsername(), e.getPassword());
            if (isAuthenticated) {
                this.domuestraMenu();
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
        i18n.getHeader().setDescription("Aplicaciones interna del servicio ");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setTitle("Informática GSA");
        i18n.getForm().setSubmit("Conectar");
        i18n.getForm().setPassword("Clave");
        i18n.getForm().setForgotPassword("Resetear clave");
        i18n.getErrorMessage().setTitle("Datos no válidos");
        i18n.getErrorMessage()
                .setMessage("Registra de nuevo el dato del usuario y la clave");
        i18n.setAdditionalInformation(
                " Informática 2021");
        return i18n;
    }
}
