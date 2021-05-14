package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.ctrl.IpCtrl;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author 06551256M
 */
public final class FrmBuscaInventarioOld extends FrmMasterVentana {

    private final Grid<DatoGenericoBean> inventarioGrid = new Grid<>();
    private ArrayList<DatoGenericoBean> datoGenericoBeansArrayList = new ArrayList<>();
    private String inventarioNumero = null;

    private final ComboBox<String> equipoTipoCombo = new CombosUi().getEquipoTipoCombo(null, 50);
    private final ComboBox<String> equipoMarcaCombo = new CombosUi().getGrupoRamaComboValor(ComboBean.TIPOEQUIPOMARCA, equipoTipoCombo.getValue(), null, "Marca");

    private final TextField inventario = new ObjetosComunes().getTextField("Inventario");
    private final TextField modelo = new ObjetosComunes().getTextField("Modelo");
    private final TextField numeroSerie = new ObjetosComunes().getTextField("N.Serie");
    private final TextField ip = new ObjetosComunes().getTextField("Ip");
    private final TextField nombredominio = new ObjetosComunes().getTextField("Nombre");

    private final TextField dni = new ObjetosComunes().getDni();
    private final TextField nombreusuario = new ObjetosComunes().getTextField("Nombre Usuario habitual");
    private final TextField observaciones = new ObjetosComunes().getTextField("Comentario");

    private final ComboBox<CentroBean> centroCombo = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, null, NivelesAtentionTipoBean.ESPECIALIZADA, null);

    private EquipoBean equipoBean = new EquipoBean();
    private Binder<EquipoBean> equipoBinder = new Binder<>();
    private String ipValor;

    public FrmBuscaInventarioOld(String inventarioNumero, ArrayList<DatoGenericoBean> datoGenericoBeansArrayList) {
        this.inventarioNumero = inventarioNumero;
        this.datoGenericoBeansArrayList = datoGenericoBeansArrayList;
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doControlBotones(null);
        doActualizaGrid();
        doCasaValores();
    }

    @Override
    public void doGrabar() {
        if (equipoBinder.writeBeanIfValid(equipoBean) && IpCtrl.isValid(ip.getValue())) {
            this.close();
        } else {
            BinderValidationStatus<EquipoBean> validate = equipoBinder.validate();
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
    }

    @Override
    public void doCancelar() {
        equipoBean = null;
        ipValor = "";
        this.close();
    }

    @Override
    public void doCerrar() {
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
    }

    @Override
    public void doGrid() {
        inventarioGrid.setHeightByRows(true);
        inventarioGrid.setPageSize(25);
        inventarioGrid.addColumn(DatoGenericoBean::getTipoDato).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));
        inventarioGrid.addColumn(DatoGenericoBean::getValor).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));

    }

    @Override
    public void doActualizaGrid() {
        //  datoGenericoBeansArrayList = new GalenoDao().getEquipo(inventarioNumero);
        inventarioGrid.setItems(datoGenericoBeansArrayList);
    }

    @Override
    public void doBinderPropiedades() {

        equipoBinder.forField(equipoTipoCombo)
                .withNullRepresentation("")
                .asRequired()
                .bind(EquipoBean::getTipo, EquipoBean::setTipo);

        equipoBinder.forField(inventario)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(EquipoBean::getInventario, EquipoBean::setInventario);

        equipoBinder.forField(equipoMarcaCombo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(EquipoBean::getMarca, EquipoBean::setMarca);

        equipoBinder.forField(modelo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(EquipoBean::getModelo, EquipoBean::setModelo);

        equipoBinder.forField(numeroSerie)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(EquipoBean::getNumeroSerie, EquipoBean::setNumeroSerie);

        equipoBinder.forField(centroCombo)
                .asRequired()
                .bind(EquipoBean::getCentro, EquipoBean::setCentro);

    }

    /**
     *
     */
    @Override
    public void doComponenesAtributos() {
        botonAyuda.setVisible(false);
        botonBorrar.setVisible(false);
        botonImprimir.setVisible(false);
        botonLimpiar.setVisible(false);
        equipoMarcaCombo.setValue("");
        equipoTipoCombo.setValue("");
    }

    /**
     *
     */
    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(inventario, equipoTipoCombo, equipoMarcaCombo, modelo,
                numeroSerie, ip, nombredominio, observaciones, centroCombo);
        contenedorDerecha.add(inventarioGrid);

    }

    @Override
    public void doCompentesEventos() {
    }

    /**
     *
     */
    public void doCasaValores() {
        ArrayList<String> lista = new ArrayList<>();
        for (DatoGenericoBean dato : datoGenericoBeansArrayList) {
            switch (dato.getTipoDato()) {
                case "inventario":
                    inventario.setValue(dato.getValor());
                    break;
                case "cod_maqui":
                    inventario.setValue(dato.getValor());
                    break;
                case "nom_maqui":
                    lista = new ComboDao().getListaGruposRamaAprox(ComboBean.TIPOEQUIPOMARCA, 100, dato.getValor());
                    if (lista.size() == 1) {
                        equipoTipoCombo.setValue(lista.get(0));
                    }
                    break;

                case "ip":
                    ip.setValue(dato.getValor());
                    break;
                case "nombre_equipo":
                    nombredominio.setValue(dato.getValor());
                    break;
                case "observaciones":
                    observaciones.setValue(dato.getValor());
                    break;
                case "marca":
                    lista = new ComboDao().getListaGruposRamaValorAprox(ComboBean.TIPOEQUIPOMARCA, dato.getValor(), 100);
                    if (lista.size() == 1) {
                        equipoMarcaCombo.setValue(lista.get(0));
                    }
                    break;
                case "modelo":
                    modelo.setValue(dato.getValor());
                    break;
                case "nserie":
                    numeroSerie.setValue(dato.getValor());
                    break;

                case "edificio":
                    switch (dato.getValor()) {
                        case "A2": //exterior

                        case "H1": //hnss
                            centroCombo.setValue(new CentroDao().getPorId(new Long(3918)));
                            break;
                        case "H2": //Provincial
                            centroCombo.setValue(new CentroDao().getPorId(new Long(3916)));
                            break;
                        case "C3": //CEP estacion
                            centroCombo.setValue(new CentroDao().getPorId(new Long(3914)));
                            break;
                        case "AS": //CEP arenas
                            centroCombo.setValue(new CentroDao().getPorId(new Long(3915)));
                            break;
                    }
                    break;
                case "ubicacion":
                    break;
                case "gfh":
                    break;
            }
        }
    }

    public EquipoBean getEquipoBean() {
        return equipoBean;
    }

    public void setEquipoBean(EquipoBean equipoBean) {
        this.equipoBean = equipoBean;
    }

    public String getIpValor() {
        return ip.getValue();
    }

    public void setIpValor(String ipValor) {
        this.ipValor = ipValor;
    }

}
