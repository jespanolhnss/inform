package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.IpDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmIp extends FrmMasterPantalla {

    private final ComboBox<VlanBean> vlanComoboBuscador = new CombosUi().getVlanCombo(null, null);
    private final RadioButtonGroup<String> ipLibre = new ObjetosComunes().getSNRadio("Ip Libre");

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final ComboBox<VlanBean> vlanCombo = new CombosUi().getVlanCombo(null, null);
    private final TextField ip = new ObjetosComunes().getTextField("IP");
    private final TextField equipo = new ObjetosComunes().getTextField("Id Equipo");
    private Details equipoDetalle = new Details();

    private final Button ayudaEquipo = new ObjetosComunes().getBotonMini();

    private IpBean ipBean = null;
    private final Binder<IpBean> ipBinder = new Binder<>();
    private final PaginatedGrid<IpBean> ipGrid = new GridUi().getIpGrid();
    private ArrayList<IpBean> ipArrayList = new ArrayList<>();

    public FrmIp() {
        super();
        this.ipBean = new IpBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    public void doControlBotones(EquipoBean equipoBean) {
        super.doControlBotones(equipoBean);
        if (equipoBean == null) {

        } else {
            this.setDetalleEquipo(equipoBean);
        }
    }

    @Override
    public void doGrabar() {
        if (ipBinder.writeBeanIfValid(ipBean)) {
            ipBean.setValoresAut();
            if (new IpDao().doGrabaDatos(ipBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<IpBean> validate = ipBinder.validate();
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
                    ipBean.setEstado(ConexionDao.BBDD_ACTIVONO);
                    ipBean.setValoresAut();
                    new IpDao().doBorraDatos(ipBean);
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
        ipBean = new IpBean();
        ipBinder.readBean(ipBean);
        id.clear();
        equipoDetalle.setSummaryText(" ");
        equipoDetalle.setContent(new Span(" "));
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        //doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        ipArrayList = new IpDao().getLista(buscador.getValue(), vlanComoboBuscador.getValue(), null, null, ipLibre.getValue());
        ipGrid.setItems(ipArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        ipBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(IpBean::getId, null);

        ipBinder.forField(vlanCombo)
                .asRequired()
                .bind(IpBean::getVlan, IpBean::setVlan);

        ipBinder.forField(ip)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(IpBean::getIp, IpBean::setIp);
    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("IP");

        buscador.setLabel("IP");

    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("150px", 1),
                new FormLayout.ResponsiveStep("150px", 2));

        contenedorFormulario.add(id, vlanCombo, ip, equipo, ayudaEquipo, equipoDetalle);
        contenedorBuscadores.add(buscador, vlanComoboBuscador, ipLibre);
        contenedorDerecha.add(contenedorBuscadores, ipGrid);
    }

    @Override
    public void doCompentesEventos() {

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        vlanComoboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        ipLibre.addValueChangeListener(e -> doActualizaGrid());

        ip.addFocusListener(event -> {
            if (ip.getValue().isEmpty() && vlanCombo.getValue() != null) {
                ip.setValue(Utilidades.getIPClaseC(vlanCombo.getValue().getPuertaenlace().trim()));
            }
        });
        ip.addAttachListener(event -> {
            if (ip.getValue().isEmpty() && vlanCombo.getValue() != null) {
                ip.setValue(Utilidades.getIPClaseC(vlanCombo.getValue().getPuertaenlace().trim()));
            }
        });

        ipGrid.addItemClickListener(event -> {
            ipBean = event.getItem();
            ipBinder.readBean(event.getItem());
            if (ipBean.getEquipo() != null) {
                setDetalleEquipo(ipBean.equipo);
                ip.setValue(ipBean.getEquipo().getId().toString());
            }
            doControlBotones(ipBean);
        }
        );

        vlanComoboBuscador.addValueChangeListener(e -> {
            doActualizaGrid();
        });

        ayudaEquipo.addClickListener(event -> {
            if (ip != null && ip.getId() != null && !ip.getId().equals(new Long(0))) {
                FrmBuscaEquipo frmBuscaEquipo = new FrmBuscaEquipo();
                frmBuscaEquipo.addDialogCloseActionListener(eventAyuda -> {
                    EquipoBean equipoBean = frmBuscaEquipo.getEquipoBean();
                    this.setDetalleEquipo(equipoBean);
                    ipBean.setEquipo(equipoBean);
                    equipo.setValue(equipoBean.getId().toString(0));
                });
                frmBuscaEquipo.addDetachListener(eventAyuda -> {
                    EquipoBean equipoBean1 = frmBuscaEquipo.getEquipoBean();
                    this.setDetalleEquipo(equipoBean1);
                    ipBean.setEquipo(equipoBean1);
                    equipo.setValue(equipoBean1.getId().toString());
                });
                frmBuscaEquipo.open();
            }
        });
    }

    public void setDetalleEquipo(EquipoBean equipoBean) {

        equipoDetalle.setContent(new Html(equipoBean.toHtml()));
        equipoDetalle.setSummaryText(equipoBean.getTipo());
        equipoDetalle.setEnabled(true);
        equipoDetalle.setOpened(true);

    }
}
