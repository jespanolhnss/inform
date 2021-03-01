package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroFicheroBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author juannietopajares
 */
public class CentroFicheroDao extends ConexionDao implements Serializable, ConexionInterface<CentroFicheroBean> {

    private static final Logger LOGGER = LogManager.getLogger(AutonomiaDao.class);
    private static final long serialVersionUID = 1L;

    public CentroFicheroDao() {
        super();
        sql = " SELECT *   "
                + " FROM centrosficheros cf WHERE  1=1 ";

    }

    @Override
    public boolean doGrabaDatos(CentroFicheroBean centroFicheroBean) {
        boolean actualizado = false;

        if (centroFicheroBean.getId().equals(new Long(0))) {
            actualizado = this.doInsertaDatos(centroFicheroBean);
        } else {
            //   actualizado = this.doActualizaDatos(centroFicheroBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(CentroFicheroBean centroFicheroBean) {
        Boolean almacenado = false;
        Connection connection = null;
        FileInputStream is = null;
        String idp = "null";
        String ficheroAbsoluto = centroFicheroBean.getPathAbsoluto();
        try {
            connection = super.getConexionBBDD();
            Long id = new ConexionDao().getSiguienteId("centrosficheros");

            File file = Utilidades.iStoFile(centroFicheroBean.getStreamInputStream(), ficheroAbsoluto);
            FileInputStream in = new FileInputStream(file);

            sql = " INSERT INTO centrosficheros (id,centro,descripcion,fichero,nombre,estado,usucambio,fechacambio)"
                    + " VALUES (?,?,?,?,?,?,?,?)  ";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.setLong(2, centroFicheroBean.getCentro().getId());
            statement.setString(3, centroFicheroBean.getDescripcion());
            statement.setBinaryStream(4, in, (int) file.length());
            statement.setString(5, centroFicheroBean.getNombreFichero());
            statement.setInt(6, CentroDao.BBDD_ACTIVOSI);
            if (usuarioBean != null && usuarioBean.getId() != null) {
                statement.setLong(7, this.usuarioBean.getId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }

            statement.setLong(8, Utilidades.getFechaActualLong());

            almacenado = statement.executeUpdate() > 0;
            statement.close();

            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return almacenado;
    }

    @Override
    public ArrayList<CentroFicheroBean> getLista(String cadena) {
        return null;
    }

    public ArrayList<CentroFicheroBean> getLista(CentroBean centroTBean) {
        Connection connection = null;
        ArrayList<CentroFicheroBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM centrosficheros WHERE centro=" + centroTBean.getId();
            sql = sql.concat(" ORDER BY  fechacambio desc ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            //   Image image = null;
            while (resulSet.next()) {
                CentroFicheroBean centroFicheroBean = new CentroFicheroBean();
                centroFicheroBean.setId(resulSet.getLong("id"));
                centroFicheroBean.setDescripcion(resulSet.getString("descripcion"));
                centroFicheroBean.setNombreFichero(resulSet.getString("nombre"));

                centroFicheroBean.setCentro(centroTBean);
                centroFicheroBean.setFechaCambio(Utilidades.getFechaLocalDate(resulSet.getLong("fechaCambio")));
                centroFicheroBean.setEstado(resulSet.getInt("estado"));
                centroFicheroBean.setUsuarioCambio(new UsuarioDao().getPorId(resulSet.getLong("usucambio")));
                Blob blob = resulSet.getBlob("fichero");
                byte[] bytes = blob.getBytes(1l, (int) blob.length());
                centroFicheroBean.setImageBytes(bytes);
                centroFicheroBean.setFicheroBlobs(blob);

                switch (centroFicheroBean.getExtensionFichero()) {
                    case ".pdf":
                        /**
                         * Si es un pdf el nombre de la miniatura es
                         * nombresfichero.jpeg genera el jpg de la miniatura
                         * genera el pdf para montar el link
                         */
                        centroFicheroBean.setNombreFicheroMiniatura(centroFicheroBean.getNombreFicheroNoExtension() + "." + Constantes.MINIATURAEXTENSION);
                        Utilidades.getMinitauraDeUnPdf(centroFicheroBean.getNombreFicheroMiniatura(), blob);
                        Utilidades.bloBtoFile(blob, Constantes.PDFPATHABSOLUTO + centroFicheroBean.getNombreFichero()); // image = new Image(Constantes.PDFPATHRELATIVO + Utilidades.getMinitauraDeUnPdf(nombre, blob), centroFicheroBean.getNombre());
                        //   centroFicheroBean.setImagen(image);
                        break;
                    case ".gif":
                    case ".jpg":
                    case ".png":
                    case ".jpeg":
                        /**
                         * Si es un jpg ... el nombre de la miniatura es
                         * nombresfichero, descarga el fichero para montar el
                         * link
                         */
                        centroFicheroBean.setNombreFicheroMiniatura(centroFicheroBean.getNombreFichero());
                        Utilidades.bloBtoFile(centroFicheroBean.getFicheroBlobs(), centroFicheroBean.getPathAbsoluto());
                        break;
                    default:

                        break;
                }
                //   centroFicheroBean.setImagen(image);
                lista.add(centroFicheroBean);
            }
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    @Override
    public boolean doBorraDatos(CentroFicheroBean centroFicheroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  centrosficheros WHERE ID='" + centroFicheroBean.getId() + "'";
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public CentroFicheroBean getRegistroResulset(ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CentroFicheroBean getPorCodigo(String codigo) {
        return null;
    }

    @Override
    public CentroFicheroBean getPorId(Long id) {
        return null;
    }

    @Override
    public boolean doActualizaDatos(CentroFicheroBean ob) {
        return false;
    }

}
