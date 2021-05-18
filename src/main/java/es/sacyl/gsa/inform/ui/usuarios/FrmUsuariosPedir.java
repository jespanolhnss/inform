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
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.AplicacionBean;
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
import es.sacyl.gsa.inform.dao.AplicacionDao;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    AplicacionBean galeno = new AplicacionBean(new Long(9));
    RadioButtonGroup<AplicacionPerfilBean> perfilesGaleno
            = new ObjetosComunes().getAplicacionesPerfilesPorIdRadioButtonGroup(galeno.getId());
    AplicacionBean jimena = new AplicacionBean(new Long(8));
    RadioButtonGroup<AplicacionPerfilBean> perfilesJimena
            = new ObjetosComunes().getAplicacionesPerfilesPorIdRadioButtonGroup(jimena.getId());
    TextField comentario = new TextField("Comentario");

    ArrayList<AplicacionBean> listaAplicaciones = new ArrayList<>();
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

    private Map<AplicacionBean, RadioButtonGroup<AplicacionPerfilBean>> mapRadio = new HashMap<>();
    private Map<Long, ArrayList<AplicacionPerfilBean>> mapAppPerfiles = new HashMap<>();

    public FrmUsuariosPedir() {
        super();
        setSizeFull();
        recuperaSolicitante();
        listaAplicaciones = new AplicacionDao().getListaPedir();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
        construirAccordion();

    }

    @Override
    public void doGrabar() {
        if (usuarioBinder.writeBeanIfValid(usuarioBean)) {
            String centrosString = "";
            for (CentroBean cadena : centro.getValue()) {
                if (centrosString.length() > 1) {
                    centrosString = centrosString.concat(",");
                }
                centrosString = centrosString.concat(cadena.getId().toString());
            }
            peticionBean.setFechaSolicitud(LocalDate.now());
            peticionBean.setCentros(centrosString);
            peticionBean.setComentario(comentario.getValue());
            peticionBean.setTipo(tipo.getValue());
            // usamos un Map para guardar la aplicacion y el perfil Que aunque es uno lo guardo en un array

            Iterator<Map.Entry<AplicacionBean, RadioButtonGroup<AplicacionPerfilBean>>> iterator = mapRadio.entrySet().iterator();
            ArrayList<UsuarioPeticionAppBean> arrayListAplicaciones = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<AplicacionBean, RadioButtonGroup<AplicacionPerfilBean>> entry = iterator.next();
                UsuarioPeticionAppBean usuapp = new UsuarioPeticionAppBean();
                if (entry.getValue().getValue() != null) {
                    usuapp.setAplicacion(entry.getKey());
                    usuapp.setPeticion(peticionBean);
                    usuapp.setPerfil(entry.getValue().getValue());
                    arrayListAplicaciones.add(usuapp);
                }
            }
            if ((new UsuarioDao().doGrabaDatos(usuarioBean) == true)
                    && (new UsuarioDao().doGrabaPeticion(usuarioBean, peticionBean) == true)
                    && (new UsuarioDao().doGrabaPeticionApp(peticionBean, arrayListAplicaciones))) {
                (new Notification(FrmMasterConstantes.AVISODATOALMACENADO, 4000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMasterConstantes.AVISODATOERRORBBDD, 4000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<UsuarioBean> validate = usuarioBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
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

        //  peticionBean.forField(tipo).bind(UsuarioPeticionBean::getTipo, UsuarioPeticionBean::setTipo);
        //  peticionBean.forField(comentario).bind(UsuarioPeticionBean::getComentario, UsuarioPeticionBean::setComentario);
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
        //  contenedorFormulario.setColspan(gfhUsuario, 2);
        contenedorFormulario.add(categoriaUsuario, 2);
        contenedorFormulario.add(comentario, 3);
        contenedorDerecha.add(aplicacionesLabel);

        contenedorDerecha.add(aplicacionesAccordion);
        //contenedorDerecha.add(excelBoton);

    }

    public void doCompletaDatosPersigo() {
        UsuarioBean usuarioBeanHnss = new UsuarioDao().getPorDni(nifUsuario.getValue());
        if (usuarioBeanHnss != null && usuarioBeanHnss.getDni() != null) {
            if (usuarioBean.getNombre() == null || usuarioBean.getNombre().isEmpty()) {
                usuarioBean.setNombre(usuarioBeanHnss.getNombre());
            }
            if (usuarioBean.getApellido1() == null || usuarioBean.getApellido1().isEmpty()) {
                usuarioBean.setApellido1(usuarioBeanHnss.getApellido1());
            }
            if (usuarioBean.getApellido2() == null || usuarioBean.getApellido2().isEmpty()) {
                usuarioBean.setApellido1(usuarioBeanHnss.getApellido2());
            }
            if (usuarioBean.getMail() == null || usuarioBean.getMail().isEmpty()) {
                usuarioBean.setMail(usuarioBeanHnss.getMail());
            }
            if (usuarioBean.getMovilUsuario() == null || usuarioBean.getMovilUsuario().isEmpty()) {
                usuarioBean.setMovilUsuario(usuarioBeanHnss.getMovilUsuario());
            }
            if (usuarioBean.getCorreoPrivadoUsuario() == null || usuarioBean.getCorreoPrivadoUsuario().isEmpty()) {
                usuarioBean.setCorreoPrivadoUsuario(usuarioBeanHnss.getCorreoPrivadoUsuario());
            }
            if (usuarioBean.getCategoria() == null) {
                usuarioBean.setCategoria(usuarioBeanHnss.getCategoria());
            }
            if (usuarioBean.getGfh() == null) {
                usuarioBean.setGfh(usuarioBeanHnss.getGfh());
            }
        }
    }

    @Override
    public void doCompentesEventos() {
        nifUsuario.addBlurListener(event -> {
            if (!nifUsuario.getValue().isEmpty() && nifUsuario.getValue() != null) {
                usuarioBean = new UsuarioDao().getUsuarioPersigo(nifUsuario.getValue());
                if (usuarioBean.getDni() != null) {
                    usuarioBinder.readBean(usuarioBean);
                    categoriaUsuario.setValue(usuarioBean.getCategoria());
                    doCompletaDatosPersigo();
                } else {
                    usuarioBean = new UsuarioDao().getPorDni(nifUsuario.getValue());
                    if (usuarioBean.getDni() != null) {
                        usuarioBinder.readBean(usuarioBean);
                        categoriaUsuario.setValue(usuarioBean.getCategoria());
                    }
                }
            } else {

            }
        });

        tiposCentro.addValueChangeListener(event -> {
            doCargaCentros(tiposCentro.getSelectedItems());
        });

        perfilesGaleno.addValueChangeListener(event -> {
            UsuarioPeticionAppBean perfil = new UsuarioPeticionAppBean();
            perfil.setAplicacion(galeno);
            perfil.setIdPerfil(event.getValue().getId());
            aplicacionesArrayList.add(perfil);
        });

        perfilesJimena.addValueChangeListener(event -> {
            UsuarioPeticionAppBean perfil = new UsuarioPeticionAppBean();
            perfil.setAplicacion(jimena);
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

        for (AplicacionBean app : listaAplicaciones) {
            VerticalLayout galenoLayout = new VerticalLayout();
            RadioButtonGroup<AplicacionPerfilBean> perfiles
                    = new ObjetosComunes().getAplicacionesPerfilesPorIdRadioButtonGroup(app.getId());
            galenoLayout.add(perfiles);
            mapRadio.put(app, perfiles);
            aplicacionesAccordion.add(app.getNombre(), galenoLayout);
        }
        /*
        VerticalLayout galenoLayout = new VerticalLayout();
        galenoLayout.add(perfilesGaleno);
        aplicacionesAccordion.add("Galeno", galenoLayout);

        VerticalLayout jimenaLayout = new VerticalLayout();
        jimenaLayout.add(perfilesJimena);
        aplicacionesAccordion.add("Jimena", jimenaLayout);
         */

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
        peticionBean.setPeticionario(solicitanteBean);
    }

}
