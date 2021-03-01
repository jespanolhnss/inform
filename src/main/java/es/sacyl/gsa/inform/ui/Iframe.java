
package es.sacyl.gsa.inform.ui;

/**
 *
 * @author 06551256M
 */

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;

@SuppressWarnings("serial")
@Tag("iframe")
public class Iframe extends Component implements HasSize {

   public Iframe() {
   }

   public Iframe(String src) {
      setSrc(src);
   }

   public void setSrc(String src) {
      getElement().setProperty("src", src);
   }
}