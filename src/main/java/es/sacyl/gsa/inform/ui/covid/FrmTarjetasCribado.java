package es.sacyl.gsa.inform.ui.covid;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.ParametroDao;
import es.sacyl.gsa.inform.reports.covid.TarjetasCribadoPdf;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.MandaMail;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;

/**
 *
 * @author 06551256M
 */
public final class FrmTarjetasCribado extends FrmMasterPantalla implements Serializable {

    private static final long serialVersionUID = 1L;

    private final IntegerField desdeNumero = new ObjetosComunes().getIntegerField("Desde nº");
    private final IntegerField hastaNumero = new ObjetosComunes().getIntegerField("Desde nº");
    private final TextField cuentaMail = new ObjetosComunes().getTextField("Correo destino", null, 400, "300px", "100px");
    private final static String COVID_CRIBAO_CODIGO = "covid.cribado.numeros"; // Datos de las últimas tarjetas impresas : desde|hasta|fecha
    private final static String COVID_CRIBAO_DESCRIPCION = "Último números tarjetas covid generadas ";
    private final static String CONTENIDO_MAIL = " Adjunto tarjetas de cribado generadas "; // Datos de las últimas tarjetas impresas : desde|hasta|fecha

    private final Label impresionPrevia = new Label();
    private final Label infoVAlores;
    private final Label infoMail = new Label("Si quieres que el pdf se mande por correo, puedes escribir varios mail separados por comas");

    public final Integer MAXIMO_NUMERO_TARJETAS = 10000;

    public FrmTarjetasCribado() {
        this.setWidth("500px");
        this.infoVAlores = new Label("El número máximo de tarjetas a imprimir es de " + Integer.toString(MAXIMO_NUMERO_TARJETAS));
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
    }

    @Override
    public void doCancelar() {
        this.removeAll();
        this.setVisible(false);
    }

    @Override
    public void doBorrar() {
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
    }

    @Override
    public void doImprimir() {
        if (desdeNumero.getValue() <= hastaNumero.getValue()) {
            if ((hastaNumero.getValue() - desdeNumero.getValue()) > MAXIMO_NUMERO_TARJETAS) {
                Notification.show(" El número máximo es " + Integer.toString(MAXIMO_NUMERO_TARJETAS), 2000, Notification.Position.TOP_START);
            } else {
                TarjetasCribadoPdf tarjetasCribadoPdf = new TarjetasCribadoPdf(desdeNumero.getValue(), hastaNumero.getValue());
                tarjetasCribadoPdf.doCreaFicheroPdf();
                Page page = new Page(getUI().get());
                page.open(tarjetasCribadoPdf.getUrlDelPdf(), "_blank");
                ParametroBean parametro = new ParametroDao().getPorCodigo(COVID_CRIBAO_CODIGO);
                parametro.setValor(getCadenaParametro());
                new ParametroDao().doGrabaDatos(parametro);
                if (!cuentaMail.getValue().isEmpty()) {
                    new MandaMail().mandaMailAdjunto(cuentaMail.getValue(),
                            "Tarjetas de cribado desde " + desdeNumero.getValue() + " hasta" + hastaNumero.getValue(),
                            CONTENIDO_MAIL, tarjetasCribadoPdf.getNombrePdfAbsoluto(), tarjetasCribadoPdf.getNombreDelFicheroPdf());
                }
            }
        } else {
            Notification.show(" Orden de valores  incorrecto.");
        }
    }

    @Override
    public void doGrid() {
    }

    @Override
    public void doActualizaGrid() {
    }

    @Override
    public void doBinderPropiedades() {
    }

    @Override
    public void doComponenesAtributos() {
        botonBorrar.setVisible(false);
        botonAyuda.setVisible(false);
        botonLimpiar.setVisible(false);
        botonGrabar.setVisible(false);
        botonImprimir.setVisible(true);
        desdeNumero.setMaxWidth("300px");
        hastaNumero.setMaxWidth("300px");
        setValoresdeParametros();

    }

    public void setValoresdeParametros() {
        ParametroBean parametro = new ParametroDao().getPorCodigo(COVID_CRIBAO_CODIGO);
        if (parametro == null) {
            desdeNumero.setValue(1);
            hastaNumero.setValue(5000);
            parametro = new ParametroBean();
            parametro.setCodigo(COVID_CRIBAO_CODIGO);
            parametro.setDescripcion(COVID_CRIBAO_DESCRIPCION);
            parametro.setValor(getCadenaParametro());
            new ParametroDao().doGrabaDatos(parametro);
            impresionPrevia.setText("Impresión inicial ");
        } else {
            String valor = parametro.getValor();
            String[] valores = valor.split("\\|");
            impresionPrevia.setText(" Impresión previa desde " + valores[0] + " hasta " + valores[1] + " el día " + Utilidades.getFechadd_mm_yyyy(valores[2]));
            if (Utilidades.isNumeric(valores[1])) {
                desdeNumero.setValue(Integer.parseInt(valores[1]) + 1);
                hastaNumero.setValue(Integer.parseInt(valores[1]) + 5000);
            }
        }
    }

    public String getCadenaParametro() {
        String cadena = "";
        if (desdeNumero != null && desdeNumero.getValue() != null) {
            cadena = Integer.toString(desdeNumero.getValue()) + "|";
        } else {
            cadena = "0|";
        }
        if (hastaNumero != null && hastaNumero.getValue() != null) {
            cadena = cadena.concat(Integer.toString(hastaNumero.getValue()) + "|");
        } else {
            cadena = cadena.concat("0|");
        }
        cadena = cadena.concat(Utilidades.getFechaActualString() + "|");
        return cadena;
        //  return+ Integer.toString(hastaNumero.getValue()) + "|" + ;
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorBotones.add(botonImprimir);
        contenedorFormulario.add(impresionPrevia, desdeNumero, hastaNumero, cuentaMail, infoVAlores, infoMail);
    }

    @Override
    public void doCompentesEventos() {
    }

}
