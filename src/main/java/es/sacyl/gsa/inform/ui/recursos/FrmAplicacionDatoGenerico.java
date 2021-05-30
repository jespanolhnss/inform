package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.dao.AplicacionesDatosDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.IOUtils;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmAplicacionDatoGenerico extends FrmMasterVentana {

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private TextArea valor = new ObjetosComunes().getTextArea("Valor");
    private ComboBox<String> tipoDatosComboBox = new CombosUi().getGrupoRamaComboValor(ComboBean.TIPOAPLICACIONDATO, ComboBean.TIPOAPLICACIONDATOAPLICACION, null, null);
    private DatePicker fechaCambio = new ObjetosComunes().getDatePicker(null, null, null);

    private final VerticalLayout contenedorFicheros = new VerticalLayout();
    private HorizontalLayout filaFicheros = new HorizontalLayout();
    private int contadorFicheros = 0;
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private Upload upload = new Upload(buffer);
    private final Div output = new Div();

    private final TextField tecnicoapellidosNombre = new ObjetosComunes().getTextField("Técnico", "", 50, "200px", "80px");

    private AplicacionBean aplicacionBean = new AplicacionBean();
    private DatoGenericoBean datoGenericoBean = new DatoGenericoBean();

    private final Binder<DatoGenericoBean> datoGenericoBinder = new Binder<>();
    private final PaginatedGrid<DatoGenericoBean> datoGenericoGrid = new PaginatedGrid<>();
    private ArrayList<DatoGenericoBean> datoGenericoBeanArray = new ArrayList<>();

    public FrmAplicacionDatoGenerico(AplicacionBean aplicacionBeanParam, DatoGenericoBean datoGenericoBean) {
        super();
        this.aplicacionBean = aplicacionBeanParam;
        if (datoGenericoBean != null) {
            doControlBotones(datoGenericoBean);
            this.datoGenericoBean = datoGenericoBean;
        } else {
            doControlBotones(null);
            this.datoGenericoBean.setIdDatoAplicacion(aplicacionBean.getId());
        }
        doVentana();
        datoGenericoBinder.readBean(datoGenericoBean);
    }

    public void doVentana() {
        upload.setMaxFiles(1);
//        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "application/pdf");
        upload.setAcceptedFileTypes("application/pdf");
        upload.setMaxFiles(5);
        upload.setDropLabel(new Label("Error de fichero"));
        // upload.setAcceptedFileTypes("text/csv");
        //   upload.setMaxFileSize(300);
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doControlBotones(Object obj) {
        if (obj != null) {
            botonBorrar.setEnabled(true);
            upload.setVisible(true);
        } else {
            botonBorrar.setEnabled(false);
            upload.setVisible(false);
        }
    }

    @Override
    public void doGrabar() {
        if (datoGenericoBinder.writeBeanIfValid(datoGenericoBean)) {
            datoGenericoBean.setIdDatoAplicacion(aplicacionBean.getId());
            datoGenericoBean.setValoresAut();
            if (new AplicacionesDatosDao().doGrabaDatos(datoGenericoBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<DatoGenericoBean> validate = datoGenericoBinder.validate();
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
                    new AplicacionesDatosDao().doBorraDatos(datoGenericoBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
    }

    @Override
    public void doCancelar() {
        this.close();
    }

    @Override
    public void doCerrar() {
        this.close();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        datoGenericoBean = new DatoGenericoBean();
        datoGenericoBinder.readBean(datoGenericoBean);
        datoGenericoBean.setIdDatoAplicacion(aplicacionBean.getId());
        fechaCambio.clear();
        tecnicoapellidosNombre.clear();
        upload = new Upload(buffer);

    }

    @Override
    public void doGrid() {
        datoGenericoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        datoGenericoGrid.setHeightByRows(true);
        datoGenericoGrid.setPageSize(25);
        datoGenericoGrid.setPaginatorSize(25);
        datoGenericoGrid.addColumn(DatoGenericoBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        datoGenericoGrid.addColumn(DatoGenericoBean::getTipoDato).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));
        datoGenericoGrid.addColumn(DatoGenericoBean::getValor).setAutoWidth(true).setHeader(new Html("<b>Valor</b>"));
        datoGenericoGrid.setClassName("error_row");
        datoGenericoGrid.setClassNameGenerator(auto -> {
            if (auto.getEstado() == 0) {
                //    return "my-style-2";
                return "error_row";
            } else {
                return "my-style-1";
            }

        });
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        datoGenericoBeanArray = new AplicacionesDatosDao().getLista(null, aplicacionBean);
        datoGenericoGrid.setItems(datoGenericoBeanArray);
    }

    @Override
    public void doBinderPropiedades() {
        datoGenericoBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(DatoGenericoBean::getId, null);

        datoGenericoBinder.forField(tipoDatosComboBox)
                .asRequired()
                .bind(DatoGenericoBean::getTipoDato, DatoGenericoBean::setTipoDato);

        datoGenericoBinder.forField(valor)
                .asRequired()
                .bind(DatoGenericoBean::getValor, DatoGenericoBean::setValor);
    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Datos y características de :" + aplicacionBean.getNombre());
        contenedorIzquierda.getStyle().set("overflow", "auto");
        contenedorFormulario.getStyle().set("overflow", "auto");
        valor.getStyle().set("overflow", "auto");
        valor.setMaxHeight("300px");
        fechaCambio.setEnabled(false);
        tecnicoapellidosNombre.setEnabled(false);
        upload.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("200px", 1),
                new FormLayout.ResponsiveStep("200px", 2));
        contenedorFormulario.add(id, tipoDatosComboBox);
        contenedorFormulario.add(valor, 2);
        contenedorFormulario.add(fechaCambio, tecnicoapellidosNombre);
        contenedorFormulario.add(upload, 2);

        contenedorDerecha.add(datoGenericoGrid);
    }

    @Override
    public void doCompentesEventos() {
        datoGenericoGrid.addItemClickListener(event -> {
            datoGenericoBean = event.getItem();
            datoGenericoBinder.readBean(datoGenericoBean);
            fechaCambio.setValue(datoGenericoBean.getFechacambio());
            tecnicoapellidosNombre.setValue(datoGenericoBean.getUsucambio().getApellidosNombre());
            doControlBotones(datoGenericoBean);
        });
        upload.addAttachListener(e -> {

        });

        upload.addSucceededListener(event -> {
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(),
                    buffer.getInputStream(event.getFileName()));

            showOutput(event.getFileName(), component, output);

            doAlmacenaDato(buffer.getInputStream(event.getFileName()), event.getFileName());
        });

        upload.addAllFinishedListener(e -> {
            //  this.close();
        });
    }

    public ArrayList<DatoGenericoBean> getDatoGenericoBeanArray() {
        return datoGenericoBeanArray;
    }

    public void setDatoGenericoBeanArray(ArrayList<DatoGenericoBean> datoGenericoBeanArray) {
        this.datoGenericoBeanArray = datoGenericoBeanArray;
    }

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Label(text);
    }

    private Component createComponent(String mimeType, String fileName,
            InputStream stream) {
        if (mimeType.startsWith("text")) {
            return createTextComponent(stream);
        } else if (mimeType.startsWith("image")) {
            Image image = new Image();
            try {
                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO
                            .getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            if (reader.getWidth(0) < 100) {
                                image.setWidth(reader.getWidth(0) + "px");
                            } else {
                                image.setWidth(100 + "px");
                            }
                            if (reader.getHeight(0) < 100) {
                                image.setHeight(reader.getHeight(0) + "px");
                            } else {
                                image.setHeight(100 + "px");
                            }

                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return content;
    }

    public void doAlmacenaDato(InputStream stream, String filename) {

        datoGenericoBean.setStreamInputStream(stream);

        datoGenericoBean.setNombreFichero(filename.toLowerCase());
        datoGenericoBean.setNombreFicheroMiniatura(datoGenericoBean.getNombreFicheroNoExtension() + Constantes.MINIATURAEXTENSION);
        datoGenericoBean.setValoresAut();

        new AplicacionesDatosDao().doActualizaDatosFichero(datoGenericoBean);

    }

    private void showOutput(String text, Component content,
            HasComponents outputContainer) {
        //   HtmlComponent p = new HtmlComponent(Tag.P);
        //  p.getElement().setText(text);
        //    outputContainer.add(p);
        //   outputContainer.add(content);
        if (contadorFicheros == 0) {
            filaFicheros = new HorizontalLayout();
            contenedorFicheros.add(filaFicheros);
        }
        filaFicheros.add(content);
        contadorFicheros++;
        if (contadorFicheros == 5) {
            contadorFicheros = 0;
        }
    }
}
