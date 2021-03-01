/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

@Tag("object")
public class EmbeddedPdfDocument extends Component implements HasSize {

    public EmbeddedPdfDocument(StreamResource resource) {
        this();
        getElement().setAttribute("data", resource);
    }

    public EmbeddedPdfDocument(String url) {
        this();
        setHeight("100%");
        getElement().setAttribute("data", url);
        getElement().setAttribute("type", "application/pdf");
    }

    public EmbeddedPdfDocument(String url, String width) {
        this();
        setHeight("100%");
        setWidth(width);
        getElement().setAttribute("data", url);
        getElement().setAttribute("type", "application/pdf");
    }

    protected EmbeddedPdfDocument() {
        getElement().setAttribute("type", "application/pdf");
        setSizeFull();
    }

}
/*
@Route("test")
public class TestView extends Div {
  public TestView() {
    add(new EmbeddedPdfDocument("https://vaadin.com/download/book-of-vaadin/vaadin-7/pdf/book-of-vaadin.pdf"));
    setHeight("100%");
  }
}

@Route("test")
public class TestView extends Div {
  public TestView() {
    add(new EmbeddedPdfDocument("frontend/book-of-vaadin.pdf"));
    setHeight("100%");
  }
}

@Route("test")
public class TestView extends Div {
  public TestView() {
    add(new EmbeddedPdfDocument(new StreamResource("book-of-vaadin.pdf", () -> {
      try {
        return getPdfInputStream();
      } catch (FileNotFoundException e) {
        return new ByteArrayInputStream(new byte[]{});
      }
    })));
    setHeight("100%");
  }

  private InputStream getPdfInputStream() throws FileNotFoundException {
    return new FileInputStream("/home/dennis/workspace/vaadin/src/main/webapp/frontend/book-of-vaadin.pdf");
  }
}


 */
