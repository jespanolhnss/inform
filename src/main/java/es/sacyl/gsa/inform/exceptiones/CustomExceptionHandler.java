package es.sacyl.gsa.inform.exceptiones;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 06551256M
 */
public class CustomExceptionHandler implements ErrorHandler {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CustomExceptionHandler.class);

    @Override
    public void error(ErrorEvent errorEvent) {
        //All important logic exceptions should have been caught before reaching this point.
        //Exceptions reaching this are an indication something went really wrong
        Throwable t = errorEvent.getThrowable();
        logger.error("Uncaught UI exception", errorEvent.getThrowable());
        if (UI.getCurrent() != null) {
            UI.getCurrent().access(() -> {
                Notification.show("We are sorry, but an internal error occurred? Please contact support.");
            });
        }
    }

}
