package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionAppBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Route("usuarios")
public final class FrmUsuariosPedir extends FrmMasterPantalla {

    private static final long serialVersionUID = 1L;

    /* Campos del formulario */
    TextField idSolicitante = new TextField();
    TextField nifSolicitante = new ObjetosComunes().getDni();
    TextField nombreSolicitante = new ObjetosComunes().getTextField("Nombre", "teclea nombre", 25, "100px", "30px");
    TextField apellido1Solicitante = new ObjetosComunes().getTextField("Apellido 1", "teclea primer apellido", 25, "100px", "30px");
    TextField apellido2Solicitante = new ObjetosComunes().getTextField("Apellido 2", "teclea segundo apellido", 25, "100px", "30px");
    TextField correoSolicitante = new ObjetosComunes().getMail("Correo Electrónico", "Correo del solicitante");
    TextField movilSolicitante = new ObjetosComunes().getMovil();
    TextField telefonoSolicitante = new ObjetosComunes().getTelefono();
    DatePicker fechaSolicitud = new ObjetosComunes().getDatePicker("Fecha Solicitud", "Fecha solicitud", LocalDate.now());
    TextField idUsuario = new TextField();
    TextField nombreUsuario = new ObjetosComunes().getTextField("Nombre", "teclea nombre", 25, "100px", "30px");
    TextField apellido1Usuario = new ObjetosComunes().getTextField("Apellido 1", "teclea primer apellido", 25, "100px", "30px");
    TextField apellido2Usuario = new ObjetosComunes().getTextField("Apellido 2", "teclea segundo apellido", 25, "100px", "30px");
    TextField nifUsuario = new ObjetosComunes().getDni();
    TextField correoUsuario = new ObjetosComunes().getMail("Correo Electrónico", "Correo del usuario");
    TextField correoPrivadoUsuario = new ObjetosComunes().getMail("Correo Electrónico Privado", "Correo privado del usuario");
    TextField telefonoUsuario = new ObjetosComunes().getTelefono();
    TextField movilUsuario = new ObjetosComunes().getMovil();
    ComboBox<CategoriaBean> categoriaUsuario = new CombosUi().getCategoriasUsuarios(null);
    ComboBox<GfhBean> gfhUsuario = new CombosUi().getGfhPorCodigoUsuarios(null);
    ComboBox<String> tipo = new ComboBox();
    ComboBox<ProvinciaBean> provinciasCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null,
            AutonomiaBean.AUTONOMIADEFECTO);
    CheckboxGroup<CentroTipoBean> tiposCentro = new ObjetosComunes().getTipoCentroCecheckboxGroup();
    CheckboxGroup<CentroBean> centro = new ObjetosComunes().getCentrosCheckboxGroup();
    Long idGaleno = new Long(9);
    RadioButtonGroup<AplicacionPerfilBean> perfilesGaleno
            = new ObjetosComunes().getAplicacionesPerfilesPorIdRadioButtonGroup(idGaleno);
    Long idJimena = new Long(8);
    RadioButtonGroup<AplicacionPerfilBean> perfilesJimena
            = new ObjetosComunes().getAplicacionesPerfilesPorIdRadioButtonGroup(idJimena);
    TextField comentario = new TextField("Comentario");

    Label peticionarioLabel = new Label();
    Label usuarioLabel = new Label();
    Label aplicacionesLabel = new Label();
    Accordion aplicacionesAccordion = new Accordion();

    /* Componentes */
    UsuarioBean usuarioBean = new UsuarioBean();
    UsuarioBean solicitanteBean = new UsuarioBean();
    UsuarioPeticionBean peticionBean = new UsuarioPeticionBean();
    //UsuarioPeticionAppBean peticionAppBean = new UsuarioPeticionAppBean();
    Binder<UsuarioBean> usuarioBinder = new Binder<>();
    Binder<UsuarioBean> solicitanteBinder = new Binder<>();
    ArrayList<UsuarioBean> arrayListUsuarios = new ArrayList<>();
    ArrayList<UsuarioPeticionAppBean> aplicacionesArrayList = new ArrayList<>();

    Icon icon = new Icon(VaadinIcon.OFFICE);
    private final Button excelBoton = new ObjetosComunes().getBoton("Excel", ButtonVariant.LUMO_LARGE, icon);

    public FrmUsuariosPedir() {
        super();
        setSizeFull();
        recuperaSolicitante();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
        construirAccordion();
    }

    @Override
    public void doGrabar() {
        usuarioBinder.writeBeanIfValid(usuarioBean);
        String centrosString = "";
        for (CentroBean cadena : centro.getValue()) {
            if (centrosString.length() > 1) {
                centrosString = centrosString.concat(",");
            }
            centrosString = centrosString.concat(cadena.getId().toString());
        }
        peticionBean.setCentros(centrosString);
        peticionBean.setComentario(comentario.getValue());
        peticionBean.setTipo(tipo.getValue());
        if ((new UsuarioDao().doGrabaDatos(usuarioBean) == true)
                && (new UsuarioDao().doGrabaPeticion(usuarioBean, peticionBean) == true)
                && (new UsuarioDao().doGrabaPeticionApp(peticionBean, aplicacionesArrayList))) {
            (new Notification(FrmMasterConstantes.AVISODATOALMACENADO, 4000, Notification.Position.MIDDLE)).open();
            doActualizaGrid();
            doLimpiar();
        } else {
            (new Notification(FrmMasterConstantes.AVISODATOERRORBBDD, 4000, Notification.Position.MIDDLE)).open();
        }
    }

    @Override
    public void doCancelar() {
        this.removeAll();

    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMasterConstantes.AVISOCONFIRMACIONACCION,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new UsuarioDao().doBorraDatos(usuarioBean);
                    Notification.show(FrmMasterConstantes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
        dialog.addDialogCloseActionListener(e -> {
        });
    }

    @Override
    public void doAyuda() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doLimpiar() {
        usuarioBinder.readBean(null);
        perfilesGaleno.removeAll();
    }

    @Override
    public void doGrid() {

    }

    @Override
    public void doActualizaGrid() {

    }

    @Override
    public void doBinderPropiedades() {
        usuarioBinder.forField(idUsuario)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(UsuarioBean::getId, UsuarioBean::setId);
        usuarioBinder.forField(nombreUsuario).asRequired("El nombre es obligatorio").bind(UsuarioBean::getNombre, UsuarioBean::setNombre);
        usuarioBinder.forField(apellido1Usuario).bind(UsuarioBean::getApellido1, UsuarioBean::setApellido1);
        usuarioBinder.forField(apellido2Usuario).bind(UsuarioBean::getApellido2, UsuarioBean::setApellido2);
        usuarioBinder.forField(nifUsuario)
                .asRequired("El NIF es obligatorio")
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);
        usuarioBinder.forField(correoUsuario).bind(UsuarioBean::getMail, UsuarioBean::setMail);
        usuarioBinder.forField(correoPrivadoUsuario).bind(UsuarioBean::getCorreoPrivadoUsuario, UsuarioBean::setCorreoPrivadoUsuario);
        usuarioBinder.forField(telefonoUsuario).bind(UsuarioBean::getTelefono, UsuarioBean::setTelefono);
        usuarioBinder.forField(movilUsuario).bind(UsuarioBean::getMovilUsuario, UsuarioBean::setMovilUsuario);
        usuarioBinder.forField(categoriaUsuario).bind(UsuarioBean::getCategoria, UsuarioBean::setCategoria);
        usuarioBinder.forField(gfhUsuario).bind(UsuarioBean::getGfh, UsuarioBean::setGfh);
        usuarioBinder.forField(tipo).bind(UsuarioBean::getTipo, UsuarioBean::setTipo);
        usuarioBinder.forField(comentario).bind(UsuarioBean::getComentario, UsuarioBean::setComentario);

        solicitanteBinder.forField(idSolicitante)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(UsuarioBean::getId, UsuarioBean::setId);
        solicitanteBinder.forField(nombreSolicitante).asRequired("El nombre es obligatorio").bind(UsuarioBean::getNombre, UsuarioBean::setNombre);
        solicitanteBinder.forField(apellido1Solicitante).bind(UsuarioBean::getApellido1, UsuarioBean::setApellido1);
        solicitanteBinder.forField(apellido2Solicitante).bind(UsuarioBean::getApellido2, UsuarioBean::setApellido2);
        solicitanteBinder.forField(nifSolicitante)
                .asRequired("El NIF es obligatorio")
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);
        solicitanteBinder.forField(correoSolicitante).bind(UsuarioBean::getMail, UsuarioBean::setMail);
        solicitanteBinder.forField(telefonoSolicitante).bind(UsuarioBean::getTelefono, UsuarioBean::setTelefono);
    }

    @Override
    public void doComponenesAtributos() {
        //contenedorFormulario.setSizeFull();
        //contenedorDerecha.setSizeFull();
        buscador.focus();
        buscador.setLabel("Texto de la búsqueda:");
        aplicacionesAccordion.setSizeFull();
        aplicacionesAccordion.close();
        idUsuario.setValue("0");
        centro.setVisible(false);
        tipo.setItems("Alta", "Baja");
        tipo.setLabel("Tipo");
        tipo.setPlaceholder("Alta o Baja");

        comentario.setWidthFull();
        usuarioLabel.setText("DATOS DEL USUARIO: ");

        //comentario.setSizeFull();
        usuarioLabel.setText("DATOS DEL USUARIO: ");

        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3));
        aplicacionesLabel.setText("DATOS DEL ACCESO:");
        contenedorDerecha.setSpacing(true);
        contenedorDerecha.setMargin(true);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorIzquierda.addComponentAtIndex(1, peticionarioLabel);
        contenedorIzquierda.addComponentAtIndex(3, usuarioLabel);
        contenedorFormulario.add(nifUsuario, nombreUsuario, apellido1Usuario, apellido2Usuario);
        contenedorFormulario.add(correoUsuario, correoPrivadoUsuario, telefonoUsuario, movilUsuario);
        contenedorFormulario.add(tipo, gfhUsuario);
        contenedorFormulario.setColspan(gfhUsuario, 2);
        contenedorFormulario.add(categoriaUsuario, 3);
        contenedorFormulario.add(comentario, 3);
        contenedorDerecha.add(aplicacionesLabel);

        contenedorDerecha.add(aplicacionesAccordion);
        //contenedorDerecha.add(excelBoton);

    }

    @Override
    public void doCompentesEventos() {
        nifUsuario.addBlurListener(event -> {
            if (!nifUsuario.getValue().isEmpty() && nifUsuario.getValue() != null) {
                usuarioBean = new UsuarioDao().getUsuarioPersigo(nifUsuario.getValue());
                if (usuarioBean.getDni() != null) {
                    usuarioBinder.readBean(usuarioBean);
                    categoriaUsuario.setValue(usuarioBean.getCategoria());

                }
            } else {
                usuarioBean = new UsuarioDao().getUsuarioNuestro(nifUsuario.getValue());
                if (usuarioBean.getDni() != null) {
                    usuarioBinder.readBean(usuarioBean);
                    categoriaUsuario.setValue(usuarioBean.getCategoria());
                }
            }
        });

        tiposCentro.addValueChangeListener(event -> {
            doCargaCentros(tiposCentro.getSelectedItems());
        });

        perfilesGaleno.addValueChangeListener(event -> {
            UsuarioPeticionAppBean perfil = new UsuarioPeticionAppBean();
            perfil.setIdAplicacion(idGaleno);
            perfil.setIdPerfil(event.getValue().getId());
            aplicacionesArrayList.add(perfil);
        });

        perfilesJimena.addValueChangeListener(event -> {
            UsuarioPeticionAppBean perfil = new UsuarioPeticionAppBean();
            perfil.setIdAplicacion(idJimena);
            perfil.setIdPerfil(event.getValue().getId());
            aplicacionesArrayList.add(perfil);
        });
        /*
        excelBoton.addClickListener(event -> {
            UsuariosExcel ue = new UsuariosExcel();
            Page page = new Page(getUI().get());
            String url = ue.getUrlDelExcel();
            page.open(url, "_blank");
        });
         */
    }

    @Override
    public void doImprimir() {

    }

    public void construirAccordion() {

        VerticalLayout centrosLayout = new VerticalLayout();
        centrosLayout.add(tiposCentro, centro);
        aplicacionesAccordion.add("Centros", centrosLayout);

        VerticalLayout galenoLayout = new VerticalLayout();
        galenoLayout.add(perfilesGaleno);
        aplicacionesAccordion.add("Galeno", galenoLayout);

        VerticalLayout jimenaLayout = new VerticalLayout();
        jimenaLayout.add(perfilesJimena);
        aplicacionesAccordion.add("Jimena", jimenaLayout);

        /*
        VerticalLayout comentarioLayout = new VerticalLayout();
        comentarioLayout.add(comentario);
        aplicacionesAccordion.add("Comentario", comentarioLayout);
         */
    }

    private void doCargaCentros(Set<CentroTipoBean> c) {
        ArrayList<CentroBean> alc = new ArrayList();
        for (CentroTipoBean ct : c) {
            alc.addAll(
                    new CentroDao().getLista(null, AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO,
                            null, null, ct, null, ConexionDao.BBDD_ACTIVOSI));
        }

        centro.setVisible(true);
        centro.setItems(alc);
    }

    private void recuperaSolicitante() {
        solicitanteBean = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
        nifSolicitante.setValue(solicitanteBean.getDni());
        nombreSolicitante.setValue(solicitanteBean.getNombre());
        apellido1Solicitante.setValue(solicitanteBean.getApellido1());
        apellido2Solicitante.setValue(solicitanteBean.getApellido2());
        correoSolicitante.setValue(solicitanteBean.getMail());
        telefonoSolicitante.setValue(solicitanteBean.getTelefono());

        String filiacionSolicitante = "SOLICITANTE: " + solicitanteBean.getNombre() + " "
                + solicitanteBean.getApellido1() + " "
                + solicitanteBean.getApellido2();
        peticionarioLabel.setText(filiacionSolicitante);
        peticionBean.setIdpeticionario(solicitanteBean.getId());
    }

}
