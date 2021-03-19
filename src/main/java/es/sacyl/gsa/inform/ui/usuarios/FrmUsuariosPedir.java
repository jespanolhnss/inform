package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

public final class FrmUsuariosPedir extends FrmMasterPantalla {

    private static final long serialVersionUID = 1L;

    /* Campos del formulario */
    TextField nombreUsuario = new ObjetosComunes().getTextField("Nombre", "teclea nombre", 25, "100px", "30px");
    TextField apellido1Usuario = new ObjetosComunes().getTextField("Apellido 1", "teclea primer apellido", 25, "100px", "30px");
    TextField apellido2Usuario = new ObjetosComunes().getTextField("Apellido 2", "teclea segundo apellido", 25, "100px", "30px");
    TextField nifUsuario = new ObjetosComunes().getTextField("NIF", "teclea el NIF", 25, "50px", "30px");
    TextField correoUsuario = new ObjetosComunes().getTextField("Correo Electrónico", "teclea el correo electrónico", 25, "100px", "30px");
    ComboBox<CategoriaBean> categoriaUsuario = new CombosUi().getCategoriasUsuarios(null);
    ComboBox<ProvinciaBean> provinciasCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null,
            AutonomiaBean.AUTONOMIADEFECTO);
    CheckboxGroup<CentroTipoBean> tiposCentro = new ObjetosComunes().getTipoCentroCecheckboxGroup();
    CheckboxGroup<CentroBean> centro = new ObjetosComunes().getCentrosCheckboxGroup();
    Accordion aplicacionesAccordion = new Accordion();

    /* Campos para el Grid */
    ComboBox<String> camposFiltro = new CombosUi().getStringCombo("Buscar por campo: ", null, ObjetosComunes.FiltroBusquedaUsuarios, "150px");

    /* Componentes */
    Grid<UsuarioBean> usuariosGrid = new Grid<>();
    UsuarioBean usuarioBean = new UsuarioBean();
    Binder<UsuarioBean> usuarioBinder = new Binder<>();
    ArrayList<UsuarioBean> arrayListUsuarios = new ArrayList<>();
    ArrayList<AplicacionBean> arrayListAplicaciones = new ArrayList<>();

    public FrmUsuariosPedir() {
        super();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
    }

    @Override
    public void doGrabar() {
        try {
            usuarioBinder.writeBean(usuarioBean);
            if (new UsuarioDao().doGrabaDatos(usuarioBean) == true) {
                (new Notification(FrmMasterConstantes.AVISODATOALMACENADO, 4000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMasterConstantes.AVISODATOERRORBBDD, 4000, Notification.Position.MIDDLE)).open();
            }
        } catch (ValidationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    }

    @Override
    public void doGrid() {
        usuariosGrid.addColumn(UsuarioBean::getNombre).setHeader("Nombre");
        usuariosGrid.addColumn(UsuarioBean::getApellido1).setHeader("Apellido 1");
        usuariosGrid.addColumn(UsuarioBean::getApellido2).setHeader("Apellido 2");
        usuariosGrid.addColumn(UsuarioBean::getDni).setHeader("NIF");
        usuariosGrid.addColumn(UsuarioBean::getMail).setHeader("Correo electrónico");

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        arrayListUsuarios = new UsuarioDao().getLista(null);
        usuariosGrid.setItems(arrayListUsuarios);
    }

    @Override
    public void doBinderPropiedades() {
        usuarioBinder.forField(nombreUsuario).asRequired("El nombre es obligatorio").bind(UsuarioBean::getNombre, UsuarioBean::setNombre);
        usuarioBinder.forField(apellido1Usuario).bind(UsuarioBean::getApellido1, UsuarioBean::setApellido1);
        usuarioBinder.forField(apellido2Usuario).bind(UsuarioBean::getApellido2, UsuarioBean::setApellido2);
        usuarioBinder.forField(nifUsuario)
                .asRequired("El NIF es obligatorio")
                .withValidator(
                        name -> Pattern.matches("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])", name),
                        "Name must contain at least three characters")
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);
        usuarioBinder.forField(correoUsuario).bind(UsuarioBean::getMail, UsuarioBean::setMail);
    }

    @Override
    public void doComponenesAtributos() {
        buscador.focus();
        buscador.setLabel("Texto de la búsqueda:");
        aplicacionesAccordion.close();
    }

    @Override
    public void doComponentesOrganizacion() {
        construirAccordion();
        contenedorFormulario.add(nifUsuario);
        contenedorFormulario.add(nombreUsuario);
        contenedorFormulario.add(apellido1Usuario);
        contenedorFormulario.add(apellido2Usuario);
        contenedorFormulario.add(correoUsuario);
        contenedorFormulario.add(categoriaUsuario);
        contenedorFormulario.add(aplicacionesAccordion);
        contenedorBuscadores.add(camposFiltro, buscador);
        contenedorDerecha.add(usuariosGrid);
    }

    @Override
    public void doCompentesEventos() {
        usuariosGrid.addItemClickListener(event -> {
            usuarioBean = event.getItem();
            usuarioBinder.readBean(usuarioBean);
        });

        nifUsuario.addBlurListener(event -> {
            if (!nifUsuario.getValue().isEmpty() && nifUsuario.getValue() != null) {
                usuarioBean = new UsuarioDao().getUsuarioPersigo(nifUsuario.getValue());
                usuarioBinder.readBean(usuarioBean);
            }
        });

        tiposCentro.addValueChangeListener(event -> {
            doCargaCentros(tiposCentro.getSelectedItems());
        });

//        buscador.addBlurListener(event -> {
//            if (buscador.getValue().isEmpty() && camposFiltro.getValue() == null) {
//                arrayListUsuarios = new UsuarioDao().getLista(null);
//            } else if (!buscador.getValue().isEmpty() && camposFiltro.getValue() != null) {
//                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(buscador.getValue().trim(), camposFiltro.getValue());
//            } else if (buscador.getValue().isEmpty() && camposFiltro.getValue() != null) {
//                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(null, camposFiltro.getValue());
//            } else if (!buscador.getValue().isEmpty() && camposFiltro.getValue() == null) {
//                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(buscador.getValue().trim(), null);
//            }
//            usuariosGrid.setItems(arrayListUsuarios);
//        });
    }

    @Override
    public void doImprimir() {

    }

    public void construirAccordion() {
        VerticalLayout tipoCentroLayout = new VerticalLayout();
        tipoCentroLayout.add(tiposCentro);
        aplicacionesAccordion.add("Tipo Centro", tipoCentroLayout);

        VerticalLayout centrosLayout = new VerticalLayout();
        centrosLayout.add(centro);
        aplicacionesAccordion.add("Centros", centrosLayout);

        VerticalLayout galenoLayout = new VerticalLayout();
        Long idGaleno = new Long(9);
        CheckboxGroup<AplicacionPerfilBean> perfilesGaleno
                = new ObjetosComunes().getAplicacionesPerfilesPorIdCheckboxGroup(idGaleno);
        galenoLayout.add(perfilesGaleno);
        aplicacionesAccordion.add("Galeno", galenoLayout);

        VerticalLayout jimenaLayout = new VerticalLayout();
        Long idJimena = new Long(8);
        CheckboxGroup<AplicacionPerfilBean> perfilesJimena
                = new ObjetosComunes().getAplicacionesPerfilesPorIdCheckboxGroup(idJimena);
        jimenaLayout.add(perfilesJimena);
        aplicacionesAccordion.add("Jimena", jimenaLayout);
    }

    private void doCargaCentros(Set<CentroTipoBean> c) {
        ArrayList<CentroBean> alc = new ArrayList();
        for (CentroTipoBean ct : c) {
            alc.addAll(
                    new CentroDao().getLista(null, AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO,
                            null, null, ct, null, ConexionDao.BBDD_ACTIVOSI));
        }

        centro.setItems(alc);
    }
}
