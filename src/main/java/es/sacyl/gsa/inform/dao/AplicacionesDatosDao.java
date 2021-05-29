package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.File;
import java.io.FileInputStream;
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
 * @author 06551256M
 */
public class AplicacionesDatosDao extends ConexionDao implements ConexionInterface<DatoGenericoBean> {

    private static final Logger LOGGER = LogManager.getLogger(AplicacionesDatosDao.class);
    private static final long serialVersionUID = 1L;

    @Override
    public ArrayList<DatoGenericoBean> getLista(String texto) {
        return getLista(null, null);
    }

    public ArrayList<DatoGenericoBean> getLista(String texto, AplicacionBean aplicacionBean) {
        Connection connection = null;
        ArrayList<DatoGenericoBean> lista = new ArrayList<>();
        String sqlDato = "";
        try {
            connection = this.getConexionBBDD();
            sqlDato = "SELECT * FROM aplicacionesdatos WHERE idaplicacion=" + aplicacionBean.getId();
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sqlDato);
                while (resulSet.next()) {
                    DatoGenericoBean dato = getRegistroResulset(resulSet, aplicacionBean);
                    lista.add(dato);
                }
                statement.close();
            }
            LOGGER.debug(sqlDato);
        } catch (SQLException e) {
            LOGGER.error(sqlDato + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    @Override
    public boolean doGrabaDatos(DatoGenericoBean datoGenericoBean) {
        boolean actualizado = false;
        if (this.getPorId(datoGenericoBean.getId()) == null) {
            actualizado = this.doInsertaDatos(datoGenericoBean);
        } else {
            actualizado = this.doActualizaDatos(datoGenericoBean);
        }
        return actualizado;
    }

    public boolean doActualizaDatosFichero(DatoGenericoBean datoGenericoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " update    aplicacionesdatos SET  "
                    + "tipodato=?,valor=?,fichero=?,estado=?,fechacambio=?,usucambio=? "
                    + " WHERE id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, datoGenericoBean.getTipoDato());
            statement.setString(2, datoGenericoBean.getValor());
            String ficheroAbsoluto = datoGenericoBean.getPathAbsoluto();
            File file = Utilidades.iStoFile(datoGenericoBean.getStreamInputStream(), ficheroAbsoluto);
            FileInputStream in = new FileInputStream(file);
            statement.setBinaryStream(3, in, (int) file.length());

            if (datoGenericoBean.getEstado() != null) {
                statement.setInt(4, datoGenericoBean.getEstado());
            } else {
                statement.setNull(4, Types.CHAR);
            }

            statement.setLong(5, Utilidades.getFechaLong(datoGenericoBean.getFechacambio()));
            statement.setLong(6, datoGenericoBean.getUsucambio().getId());
            statement.setLong(7, datoGenericoBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            insertadoBoolean = true;
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
    public boolean doActualizaDatos(DatoGenericoBean datoGenericoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " update    aplicacionesdatos SET  "
                    + "tipodato=?,valor=?,estado=?,fechacambio=?,usucambio=? "
                    + " WHERE id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, datoGenericoBean.getTipoDato());
            statement.setString(2, datoGenericoBean.getValor());
            if (datoGenericoBean.getEstado() != null) {
                statement.setInt(3, datoGenericoBean.getEstado());
            } else {
                statement.setNull(3, Types.CHAR);
            }

            statement.setLong(4, Utilidades.getFechaLong(datoGenericoBean.getFechacambio()));
            statement.setLong(5, datoGenericoBean.getUsucambio().getId());
            statement.setLong(6, datoGenericoBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            insertadoBoolean = true;
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
    public boolean doInsertaDatos(DatoGenericoBean datoGenericoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            datoGenericoBean.setId(this.getSiguienteId("aplicacionesdatos"));
            sql = " INSERT INTO  aplicacionesdatos  "
                    + "( id,idaplicacion,tipodato,valor,estado,fechacambio,usucambio ) "
                    + " VALUES "
                    + "(?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, datoGenericoBean.getId());
            statement.setLong(2, datoGenericoBean.getIdDatoAplicacion());

            statement.setString(3, datoGenericoBean.getTipoDato());
            statement.setString(4, datoGenericoBean.getValor());

            if (datoGenericoBean.getEstado() != null) {
                statement.setInt(5, datoGenericoBean.getEstado());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setLong(6, Utilidades.getFechaLong(datoGenericoBean.getFechacambio()));
            statement.setLong(7, datoGenericoBean.getUsucambio().getId());
            insertadoBoolean = statement.executeUpdate() > 0;
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
    public boolean doBorraDatos(DatoGenericoBean datoGenericoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE    aplicacionesdatos  WHERE id=?  ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, datoGenericoBean.getEstado());
                insertadoBoolean = statement.executeUpdate() > 0;
                statement.close();
            }
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
    public DatoGenericoBean getPorId(Long id) {
        Connection connection = null;
        DatoGenericoBean dato = null;
        if (id != 0) {
            try {
                connection = super.getConexionBBDD();
                sql = "SELECT * FROM aplicacionesdatos WHERE id=" + id;
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        dato = getRegistroResulset(resulSet);
                    }
                    statement.close();
                }
                LOGGER.debug(sql);
            } catch (SQLException e) {
                LOGGER.error(sql + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                LOGGER.error(Utilidades.getStackTrace(e));
            } finally {
                this.doCierraConexion(connection);
            }
        }
        return dato;
    }

    @Override
    public DatoGenericoBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null);
    }

    public static DatoGenericoBean getRegistroResulset(ResultSet rs, AplicacionBean aplicacionBean) {
        DatoGenericoBean dato = null;
        try {
            dato = new DatoGenericoBean();
            dato.setId(rs.getLong("id"));
            dato.setIdDatoAplicacion(rs.getLong("idaplicacion"));
            dato.setValor(rs.getString("valor"));
            dato.setTipoDato(rs.getString("tipodato"));

            Blob blob = rs.getBlob("fichero");

            if (blob != null) {
                byte[] bytes = blob.getBytes(1l, (int) blob.length());
                dato.setImageBytes(bytes);
                dato.setFicheroBlobs(blob);
                dato.setNombreFichero(dato.getId().toString() + "appdato.pdf");

                switch (dato.getExtensionFichero()) {
                    case ".pdf":
                        Utilidades.bloBtoFile(blob, Constantes.PDFPATHABSOLUTO + dato.getNombreFichero()); // image = new Image(Constantes.PDFPATHRELATIVO + Utilidades.getMinitauraDeUnPdf(nombre, blob), centroFicheroBean.getNombre());
                        LOGGER.debug("Fichero path absoluto fichero pdf " + Constantes.PDFPATHABSOLUTO + dato.getNombreFichero());
                        break;
                    /*
                    case ".gif":
                    case ".jpg":
                    case ".png":
                    case ".jpeg":
                        dato.setNombreFicheroMiniatura(dato.getNombreFichero());
                        Utilidades.bloBtoFile(dato.getFicheroBlobs(), dato.getPathAbsoluto());
                        LOGGER.debug("Fichero path absoluto fichero imagen " + dato.getPathAbsoluto());
                        break;
                     */
                    default:
                        break;
                }
            }

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return dato;
    }

    @Override
    public DatoGenericoBean getPorCodigo(String codigo) {
        return null;
    }

}
