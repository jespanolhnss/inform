package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.ctrl.IpCtrl;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.VlanDao;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.NumeroBinario;
import es.sacyl.gsa.inform.util.Utilidades;
import java.util.ArrayList;
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
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();

    private VlanBean vlanBean = null;
    private final Binder<VlanBean> vlanBinder = new Binder<>();
    private final PaginatedGrid<VlanBean> vlanGrid = new PaginatedGrid<>();
    private ArrayList<VlanBean> vlanArrayList = new ArrayList<>();

    public FrmVlan() {
        super();
        this.vlanBean = new VlanBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {

        } else {

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

    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("VLAN's");
        estadoRadio.setValue("S");
        nombre.setMaxLength(20);
        direccion.setMinLength(20);
        mascara.setMaxLength(20);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(id, nombre, direccion, mascara, puertaenlace, estadoRadio);
        contenedorDerecha.add(vlanGrid);
    }

    @Override
    public void doCompentesEventos() {

        direccion.addBlurListener(event -> {
            if (!isDireccionValida(direccion.getValue())) {
                direccion.focus();
            } else {
                String[] valores = direccion.getValue().split("/");
                mascara.setValue(getCalculaMascara(valores[0], valores[1]));
                puertaenlace.setValue(this.getCalcualPuertaEnlace(valores[0], mascara.getValue()));
            }
        });
        vlanGrid.addItemClickListener(new ComponentEventListener<ItemClickEvent<VlanBean>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<VlanBean> event) {
                vlanBean = event.getItem();
                vlanBinder.readBean(event.getItem());
                doControlBotones(vlanArrayList);
            }
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

    /**
     *
     * @param dir en formato 10.10.2.1/24
     * @return Una máscara de red en formato mas1.mas2.cas3.mas4
     *
     * 24 son los unos de la mascara
     *
     * los ceros de la deecha son 32-24
     *
     * Numerobinario tiene un constructor que le pasas el numero de digitos y el
     * tipo y crea un binario con tantos ceros o unos como el número de digitos
     *
     * Numerobinario, tiene el método de rellenar con el caracter por la
     * izquierta con los unos indicados
     *
     * A partir de la cadena de 32 bit trocecamos el nº de 32 bits en grupos de
     * 8 los pasamos cada grupo a a decimal
     *
     */
    public String getCalculaMascara(String dirIp, String unos) {
        String dirMascara1, dirMascara2, dirMascara3, dirMascara4;
        //     String[] valores = direcion.split("/");
        String[] ip = dirIp.split("\\.");
        if (Utilidades.isNumeric(unos)) {
            int numerodeUnos = Integer.parseInt(unos);
            int numerodeCeros = 32 - numerodeUnos;
            String bianarioBase = new NumeroBinario(numerodeCeros, NumeroBinario.ZERO_CHAR).rellenaIzquierda(32, NumeroBinario.ONE_CHAR);
            dirMascara1 = bianarioBase.substring(0, 8);
            dirMascara2 = bianarioBase.substring(8, 16);
            dirMascara3 = bianarioBase.substring(16, 24);
            dirMascara4 = bianarioBase.substring(24, 32);
            return Utilidades.binarioToDecimalString(dirMascara1) + "." + Utilidades.binarioToDecimalString(dirMascara2) + "."
                    + Utilidades.binarioToDecimalString(dirMascara3) + "." + Utilidades.binarioToDecimalString(dirMascara4);
        } else {
            return "";
        }
    }

    /**
     *
     * @param direccion direccion ip base
     * @param mascara máscara de red
     * @return
     *
     * Se convierte la direccion y la máscara en binario, se aplican AND y al
     * resultado se pasa a decimal
     *
     * Ejemplo 220.100.100.10/27
     *
     * IP 220.100.100.10 Máscara de red: 255.255.255.255.192
     *
     * ip binario: 11011100.01100100.01100100.00001010
     *
     * mas binario 11111111.11111111.11111111.11100000
     *
     * -----------------------------------------------
     *
     * Subred (and)11011100.01100100.01100100.00000000
     *
     * En decimal 220.100.100.0
     */
    public String getCalcualPuertaEnlace(String direccion, String mascara) {
        String[] ips = direccion.split("\\.");
        String[] mask = mascara.split("\\.");
        String dirMascara1, dirMascara2, dirMascara3, dirMascara4;

        NumeroBinario ipBinario = new NumeroBinario(
                new NumeroBinario(Integer.parseInt(ips[0])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR)
                        .concat(new NumeroBinario(Integer.parseInt(ips[1])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                        .concat(new NumeroBinario(Integer.parseInt(ips[2])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                        .concat(new NumeroBinario(Integer.parseInt(ips[3])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
        );

        NumeroBinario maskBinario = new NumeroBinario(
                new NumeroBinario(Integer.parseInt(mask[0])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR)
                        .concat(new NumeroBinario(Integer.parseInt(mask[1])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                        .concat(new NumeroBinario(Integer.parseInt(mask[2])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                        .concat(new NumeroBinario(Integer.parseInt(mask[3])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
        );

        String direccionBase = ipBinario.andLogica(maskBinario).getNumero();

        dirMascara1 = direccionBase.substring(0, 8);
        dirMascara2 = direccionBase.substring(8, 16);
        dirMascara3 = direccionBase.substring(16, 24);
        dirMascara4 = direccionBase.substring(24, 32);

        return Utilidades.binarioToDecimalString(dirMascara1) + "." + Utilidades.binarioToDecimalString(dirMascara2) + "."
                + Utilidades.binarioToDecimalString(dirMascara3) + "." + Utilidades.binarioToDecimalString(dirMascara4) + 1;
    }
}
