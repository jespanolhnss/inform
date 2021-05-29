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
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.EquipoDocTecnica;
import es.sacyl.gsa.inform.dao.AplicacionesDatosDao;
import es.sacyl.gsa.inform.dao.EquipoDocTecnicaDao;
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
public final class FrmEquipoDocTecnica extends FrmMasterVentana {

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final TextArea valor = new ObjetosComunes().getTextArea("Valor");
    private final ComboBox<String> tipoDatosComboBox = new CombosUi().getGrupoRamaCombo(ComboBean.EQUIPODOCTECNICOTIPO, null, null);
    private final DatePicker fechaCambio = new ObjetosComunes().getDatePicker(null, null, null);

    private final VerticalLayout contenedorFicheros = new VerticalLayout();
    private HorizontalLayout filaFicheros = new HorizontalLayout();
    private int contadorFicheros = 0;
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private final Upload upload = new Upload(buffer);
    private final Div output = new Div();

    private final TextField tecnicoapellidosNombre = new ObjetosComunes().getTextField("Técnico", "", 50, "200px", "80px");

    private EquipoBean equipoBean = new EquipoBean();
    private EquipoDocTecnica equipoDocTecnica = new EquipoDocTecnica();

    private final Binder<EquipoDocTecnica> equipoDocTecnicaBinder = new Binder<>();
    private final PaginatedGrid<EquipoDocTecnica> equipoDocTecnicaGrid = new PaginatedGrid<>();
    private ArrayList<EquipoDocTecnica> equipoDocTecnicaArray = new ArrayList<>();

    public FrmEquipoDocTecnica(EquipoBean equipoParam) {
        super();
        equipoBean = equipoParam;
        if (equipoDocTecnica != null) {
            doControlBotones(equipoDocTecnica);
            this.equipoDocTecnica.setMarca(equipoBean.getMarca());
            this.equipoDocTecnica.setModelo(equipoBean.getModelo());
            this.equipoDocTecnica.setTipoEquipo(equipoBean.getTipo());
        } else {
            doControlBotones(null);
        }
        doVentana();
        equipoDocTecnicaBinder.readBean(equipoDocTecnica);
    }

    public void doVentana() {
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("application/pdf");
        upload.setMaxFiles(5);
        upload.setDropLabel(new Label("Error de fichero"));
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
        if (equipoDocTecnicaBinder.writeBeanIfValid(equipoDocTecnica)) {
            equipoDocTecnica.setValoresAut();
            if (new EquipoDocTecnicaDao().doGrabaDatos(equipoDocTecnica) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<EquipoDocTecnica> validate = equipoDocTecnicaBinder.validate();
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
                    new EquipoDocTecnicaDao().doBorraDatos(equipoDocTecnica);
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
        equipoDocTecnica = new EquipoDocTecnica();
        equipoDocTecnicaBinder.readBean(equipoDocTecnica);
        this.equipoDocTecnica.setMarca(equipoBean.getMarca());
        this.equipoDocTecnica.setModelo(equipoBean.getModelo());
        this.equipoDocTecnica.setTipoEquipo(equipoBean.getTipo());
        fechaCambio.clear();
        tecnicoapellidosNombre.clear();

    }

    @Override
    public void doGrid() {
        equipoDocTecnicaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        equipoDocTecnicaGrid.setHeightByRows(true);
        equipoDocTecnicaGrid.setPageSize(25);
        equipoDocTecnicaGrid.setPaginatorSize(25);
        equipoDocTecnicaGrid.addColumn(EquipoDocTecnica::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        equipoDocTecnicaGrid.addColumn(EquipoDocTecnica::getTipoDato).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));
        equipoDocTecnicaGrid.addColumn(EquipoDocTecnica::getValor).setAutoWidth(true).setHeader(new Html("<b>Valor</b>"));
        equipoDocTecnicaGrid.setClassName("error_row");
        equipoDocTecnicaGrid.setClassNameGenerator(auto -> {
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
        equipoDocTecnicaArray = new EquipoDocTecnicaDao().getLista(null, equipoBean);
        equipoDocTecnicaGrid.setItems(equipoDocTecnicaArray);
    }

    @Override
    public void doBinderPropiedades() {
        equipoDocTecnicaBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(DatoGenericoBean::getId, null);

        equipoDocTecnicaBinder.forField(tipoDatosComboBox)
                .asRequired()
                .bind(DatoGenericoBean::getTipoDato, DatoGenericoBean::setTipoDato);

        equipoDocTecnicaBinder.forField(valor)
                .asRequired()
                .bind(DatoGenericoBean::getValor, DatoGenericoBean::setValor);
    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Documentos técnicos del equipo :" + equipoBean.getTipo() + " " + equipoBean.getMarca() + " " + equipoBean.getModelo());
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

        contenedorDerecha.add(equipoDocTecnicaGrid);
    }

    @Override
    public void doCompentesEventos() {
        equipoDocTecnicaGrid.addItemClickListener(event -> {
            equipoDocTecnica = event.getItem();
            equipoDocTecnicaBinder.readBean(equipoDocTecnica);
            fechaCambio.setValue(equipoDocTecnica.getFechacambio());
            tecnicoapellidosNombre.setValue(equipoDocTecnica.getUsucambio().getApellidosNombre());
            doControlBotones(equipoDocTecnica);
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

    private void showOutput(String text, Component content,
            HasComponents outputContainer) {

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

    public void doAlmacenaDato(InputStream stream, String filename) {

        equipoDocTecnica.setStreamInputStream(stream);

        equipoDocTecnica.setNombreFichero(filename.toLowerCase());
        equipoDocTecnica.setNombreFicheroMiniatura(equipoDocTecnica.getNombreFicheroNoExtension() + Constantes.MINIATURAEXTENSION);
        equipoDocTecnica.setValoresAut();

        new AplicacionesDatosDao().doActualizaDatosFichero(equipoDocTecnica);

    }
}
