package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.ctrl.IpCtrl;
import es.sacyl.gsa.inform.ctrl.VlanCtrl;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.IpDao;
import es.sacyl.gsa.inform.dao.VlanDao;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmVlan extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id");

    private final TextField direccion = new ObjetosComunes().getTextField("Direccion");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");
    private final TextField puertaenlace = new ObjetosComunes().getTextField("Puerta enlace");
    private final TextField mascara = new ObjetosComunes().getTextField("Mascara");
    private final TextField ultimaip = new ObjetosComunes().getTextField("Ultima direccion");
    private final TextField broadcast = new ObjetosComunes().getTextField("Broadcast");
    private final TextField numeroDirecciones = new ObjetosComunes().getTextField("NºDirecciones");
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();

    private final Button generaIp = new ObjetosComunes().getBoton("Ip", null, VaadinIcon.COG.create());

    private VlanBean vlanBean = null;
    private final Binder<VlanBean> vlanBinder = new Binder<>();
    private final PaginatedGrid<VlanBean> vlanGrid = new PaginatedGrid<>();
    private ArrayList<VlanBean> vlanArrayList = new ArrayList<>();

    private final PaginatedGrid<IpBean> ipGrid = new GridUi().getIpGrid();
    private final PaginatedGrid<EquipoBean> equipoGrid = new GridUi().getEquipoGridPaginado();
    // componentes para gestionar los tabs de la parte inferior
    private final Tab ipsTab = new Tab("Ips");
    private final Tab equiposTab = new Tab("Equipos");

    private final Tabs tabs = new Tabs(ipsTab, equiposTab);
    private final Map<Tab, Component> tabsToPages = new HashMap<>();
    private final Div page1 = new Div();
    private final Div page2 = new Div();

    public FrmVlan() {
        super();
        this.vlanBean = new VlanBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doControlBotones(null);
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            generaIp.setEnabled(false);
        } else {
            generaIp.setEnabled(true);
        }
    }

    @Override
    public void doGrabar() {
        if (vlanBinder.writeBeanIfValid(vlanBean)) {
            vlanBean.setValoresAut();
            if (new VlanDao().doGrabaDatos(vlanBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<VlanBean> validate = vlanBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    vlanBean.setEstado(ConexionDao.BBDD_ACTIVONO);
                    vlanBean.setValoresAut();
                    new VlanDao().doBorraDatos(vlanBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        vlanBean = new VlanBean();
        vlanBinder.readBean(vlanBean);
        doControlBotones(null);

    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        vlanGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        vlanGrid.setHeightByRows(true);
        vlanGrid.setPageSize(14);
        vlanGrid.addColumn(VlanBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>")).setWidth("70px");
        vlanGrid.addColumn(VlanBean::getDireccion).setAutoWidth(true).setHeader(new Html("<b>Dirección</b>")).setWidth("70px");
        vlanGrid.addColumn(VlanBean::getPuertaenlace).setAutoWidth(true).setHeader(new Html("<b>Puerta</b>")).setWidth("70px");
        vlanGrid.addColumn(VlanBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Est</b>")).setWidth("20px");
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        vlanArrayList = new VlanDao().getLista(buscador.getValue());
        vlanGrid.setItems(vlanArrayList);
    }

    public void doActualizaGridIps() {
        ipGrid.setItems(vlanBean.getIpsDelRango());
    }

    @Override
    public void doBinderPropiedades() {
        vlanBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(VlanBean::getId, null);

        vlanBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(VlanBean::getNombre, VlanBean::setNombre);

        vlanBinder.forField(mascara)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(VlanBean::getMascara, VlanBean::setMascara);

        vlanBinder.forField(direccion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(VlanBean::getDireccion, VlanBean::setDireccion);

        vlanBinder.forField(puertaenlace)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(
                        ip -> IpCtrl.siValid(ip),
                        "Dirección no válida")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(VlanBean::getPuertaenlace, VlanBean::setPuertaenlace);

        vlanBinder.forField(broadcast)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(
                        ip -> IpCtrl.siValid(ip),
                        "Dirección no válida")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(VlanBean::getBroadcast, VlanBean::setBroadcast);
        vlanBinder.forField(ultimaip)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(
                        ip -> IpCtrl.siValid(ip),
                        "Dirección no válida")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(VlanBean::getUltimaIp, VlanBean::setUltimaIp);
        vlanBinder.forField(numeroDirecciones)
                .withNullRepresentation("")
                .withConverter(new StringToIntegerConverter(FrmMensajes.AVISONUMERO))
                .bind(VlanBean::getNumeroDirecciones, VlanBean::setNumeroDirecciones);
    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("VLAN's");

        estadoRadio.setValue("S");
        nombre.setMaxLength(20);
        direccion.setMinLength(20);
        mascara.setMaxLength(20);

        page1.setWidthFull();
        page2.setWidthFull();

        ipsTab.setVisible(true);
        equiposTab.setVisible(true);

        page1.setVisible(false);
        page2.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        page1.add(ipGrid);
        page2.add(equipoGrid);
        //  page3.add(equipoAplicacionGrid);
        tabsToPages.put(ipsTab, page1);
        tabsToPages.put(equiposTab, page2);
        contenedorIzquierda.add(tabs, page1, page2);

        contenedorBotones.add(generaIp);

        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3),
                new FormLayout.ResponsiveStep("50px", 4));

        contenedorFormulario.add(id, nombre, direccion, mascara, puertaenlace, ultimaip, broadcast, numeroDirecciones, estadoRadio);
        contenedorDerecha.add(vlanGrid);
    }

    @Override
    public void doCompentesEventos() {

        direccion.addBlurListener(event -> {
            if (!isDireccionValida(direccion.getValue())) {
                direccion.focus();
            } else {
                String[] valores = direccion.getValue().split("/");
                mascara.setValue(VlanCtrl.getCalculaMascara(valores[0], valores[1]));
                // puertaenlace.setValue(this.getCalcualPuertaEnlace(valores[0], mascara.getValue()));
                puertaenlace.setValue(VlanCtrl.getCalculaPuertaEnlace(valores[0], mascara.getValue()));
                broadcast.setValue(VlanCtrl.getCalculaBroadcast(valores[0], valores[1]));
                ultimaip.setValue(VlanCtrl.getCalculaUltimaIp(valores[0], valores[1]));
                numeroDirecciones.setValue(VlanCtrl.getCalculaNumeroDirecciones(valores[1]).toString());
            }
        });
        vlanGrid.addItemClickListener(new ComponentEventListener<ItemClickEvent<VlanBean>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<VlanBean> event) {
                vlanBean = event.getItem();
                vlanBinder.readBean(event.getItem());
                doControlBotones(vlanArrayList);
                // actualiza el grid con las ips de la vlan
                vlanBean.setIpsDelRango(new IpDao().getLista(null, vlanBean, null, null, null));
                doActualizaGridIps();
            }
        });

        generaIp.addClickListener(event -> {
            new VlanCtrl().doGeneraIpsDelRango(vlanBean);
            vlanBean.setIpsDelRango(new IpDao().getLista(null, vlanBean, null, null, null));
            doActualizaGridIps();
            page1.setVisible(true);
            ipsTab.setSelected(true);

        });
        /**
         * Gestiona los clic en los tabs ocultando todos y mostrando el del
         * click
         */
        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });
    }

    public Boolean isDireccionValida(String direcion) {
        String[] valores = direcion.split("/");
        if (valores.length != 2) {
            Notification.show("Error composición máscara. El formatro es direccion /valor " + valores.length);
            return false;
        }
        if (!IpCtrl.siValid(valores[0])) {
            Notification.show("Error direccin IP");
            return false;
        }
        if (Utilidades.isNumero(valores[1])) {
            Integer valor = Integer.parseInt(valores[1]);
            if (valor < 21 || valor > 30) {
                Notification.show("Valores entre 21 y 30 ");
                return false;
            }
        } else {
            Notification.show("Error subdirección ");
            return false;
        }
        return true;
    }

}
