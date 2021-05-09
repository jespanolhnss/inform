package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 *
 * @author juannietopajares
 */
public class VentanaPdf extends Dialog {

    public VentanaPdf(String url) {
        this.setWidthFull();
        this.setHeightFull();
        Button botonCancelar = new ObjetosComunes().getBoton("Cancela", null, VaadinIcon.CLOSE_CIRCLE.create());
        botonCancelar.addClickListener(e -> {
            this.close();
        });
        add(botonCancelar);
        add(new EmbeddedPdfDocument(url));
        open();

    }
}
