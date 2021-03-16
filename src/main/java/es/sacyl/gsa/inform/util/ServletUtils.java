package es.sacyl.gsa.inform.util;

import com.vaadin.flow.server.VaadinServlet;
import java.io.File;
import java.net.URL;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class ServletUtils {

    private static final Logger LOGGER = LogManager.getLogger(ServletUtils.class);

    public static File getBaseDirectory(VaadinServlet servlet) {
        final String realPath = getResourcePath(servlet.getServletContext(), "/");
        if (realPath == null) {
            return null;
        }
        return new File(realPath);
    }

    public static String getResourcePath(ServletContext servletContext,
            String path) {
        String resultPath = null;
        resultPath = servletContext.getRealPath(path);
        if (resultPath != null) {
            return resultPath;
        } else {
            try {
                final URL url = servletContext.getResource(path);
                resultPath = url.getFile();
            } catch (final Exception e) {
                LOGGER.error("Could not find resource path " + path, Utilidades.getStackTrace(e));
            }
        }
        return resultPath;
    }
}
