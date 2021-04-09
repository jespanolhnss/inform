package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import es.sacyl.gsa.inform.bean.CentroFicheroBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.dao.CentroFicheroDao;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author 06551256M
 */
public final class FrmCentroFichero extends FrmMasterVentana {

    private final VerticalLayout contenedorFicheros = new VerticalLayout();
    private HorizontalLayout filaFicheros = new HorizontalLayout();
    private int contadorFicheros = 0;
    private final TextField descripcion = new ObjetosComunes().getTextField("Descripcion");
    private final Label centro = new Label();
    //  private final TextField descripcion = new ObjetosComunes().getTextField("Descripcion");
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private final Div output = new Div();
    //  private final ArrayList<Upload> fileArrayList = new ArrayList<>();
    private final Upload upload = new Upload(buffer);
    private CentroFicheroBean centroFicheroBean = new CentroFicheroBean();

    private Binder<CentroFicheroBean> centroFicheroBinder = new Binder<>();

    public FrmCentroFichero(CentroBean centroTBean) {
        super("500px");
        centroFicheroBean.setCentro(centroTBean);
        doPantalla();
    }

    public FrmCentroFichero(CentroFicheroBean centroFicheroBean) {
        super("500px");
        this.centroFicheroBean = centroFicheroBean;
        /*
        Si está editando una imagen sólo puede subir para saber que tupla de la
        bbdd tenemos que actualizar
         */
        upload.setMaxFiles(1);
        doPantalla();
        centroFicheroBinder.readBean(centroFicheroBean);
    }

    public void doPantalla() {
        doControlBotones(centroFicheroBean);
        centro.setText(centroFicheroBean.getCentro().getNomcen());
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "application/pdf");
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

    public void doControlBotones(CentroBean obj) {
        if (obj != null && obj.getId() != null && !obj.getId().equals(new Long(0))) {
            botonBorrar.setEnabled(true);
        } else {
            botonBorrar.setEnabled(false);
        }
    }

    @Override
    public void doGrabar() {

    }

    @Override
    public void doCancelar() {
        //  this.removeAll();

        //  System.out.println(this.getParent().getClass().getName());
        //   ((FrmCentro) this.getParent().get()).doMuestraMiniaturas1();
        //  ((FrmCentro) this.getParent().get()).doActualizaGridCentroFichero();
        this.close();

    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new CentroFicheroDao().doBorraDatos(centroFicheroBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    this.close();
                });
        dialog.open();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
    }

    @Override
    public void doGrid() {
    }

    @Override
    public void doActualizaGrid() {
    }

    @Override
    public void doBinderPropiedades() {
        centroFicheroBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(CentroFicheroBean::getDescripcion, CentroFicheroBean::setDescripcion);
    }

    @Override
    public void doComponenesAtributos() {
        descripcion.focus();
        descripcion.setAutofocus(true);
        botonLimpiar.setVisible(false);
        //  upload.setVisible(false);

    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(centro, descripcion, upload, contenedorFicheros);
        if (centroFicheroBean.getNombreFichero() != null && !centroFicheroBean.getNombreFichero().isEmpty()) {
            switch (centroFicheroBean.getExtensionFichero()) {
                case ".pdf":
                    //      contenedorFormulario.add(new EmbeddedPdfDocument(centroFicheroBean.getUrlFichero()));
                    break;
                case ".gif":
                case ".jpg":
                case ".png":
                case ".jpeg":
                    Image image = FrmCentro.getCentroFicheroImage(centroFicheroBean);
                    if (image != null) {
                        contenedorFormulario.add(image);
                    }
                    break;
            }

        }
//        if (centroFicheroBean.getImagen() != null) {
        //           contenedorFormulario.add(centroFicheroBean.getImagen());
        //      }
    }

    @Override
    public void doCompentesEventos() {
        /*
        descripcion.addBlurListener(event -> {
            if (descripcion.getValue() == null || descripcion.isEmpty()) {
                Notification.show(FrmMensajes.AVISODATOABLIGATORIO);
                descripcion.focus();
            }
        });
         */
        upload.addAttachListener(e -> {
            if (descripcion.getValue().isEmpty()) {
                Notification.show(FrmMensajes.AVISODATOABLIGATORIO);
                descripcion.focus();
            }
        });

        upload.addSucceededListener(event -> {
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(),
                    buffer.getInputStream(event.getFileName()));

            showOutput(event.getFileName(), component, output);

            doAlmacenaDato(buffer.getInputStream(event.getFileName()), event.getFileName());
        });

        upload.addAllFinishedListener(e -> {
            this.close();
        });
    }

    public void doAlmacenaDato(InputStream stream, String filename) {
        centroFicheroBean.setStreamInputStream(stream);
        if (descripcion.getValue().isEmpty()) {
            descripcion.setValue("?");
        }

        centroFicheroBean.setDescripcion(descripcion.getValue());
        centroFicheroBean.setNombreFichero(filename.toLowerCase());
        centroFicheroBean.setNombreFicheroMiniatura(centroFicheroBean.getNombreFicheroNoExtension() + Constantes.MINIATURAEXTENSION);
        centroFicheroBean.setValoresAut();

        new CentroFicheroDao().doGrabaDatos(centroFicheroBean);
        // new CentroTDao().doInsertaFichero(centroTBean, stream, filename, descripcion.getValue());
    }

    @Override
    public void doCerrar() {
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

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Label(text);
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
