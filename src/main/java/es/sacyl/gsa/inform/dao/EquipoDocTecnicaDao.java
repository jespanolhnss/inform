package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.EquipoDocTecnica;
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
public class EquipoDocTecnicaDao extends ConexionDao implements ConexionInterface<EquipoDocTecnica> {

    private static final Logger LOGGER = LogManager.getLogger(EquipoDocTecnicaDao.class);
    private static final long serialVersionUID = 1L;

    @Override
    public EquipoDocTecnica getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null);
    }

    public static EquipoDocTecnica getRegistroResulset(ResultSet rs, EquipoBean equipoBeean) {
        EquipoDocTecnica dato = null;
        try {
            dato = new EquipoDocTecnica();
            dato.setId(rs.getLong("id"));
            dato.setTipoEquipo(rs.getString("tipoequipo"));
            dato.setMarca(rs.getString("marca"));
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
    public EquipoDocTecnica getPorId(Long id) {
        Connection connection = null;
        EquipoDocTecnica dato = null;
        if (id != 0) {
            try {
                connection = super.getConexionBBDD();
                sql = "SELECT * FROM equiposdocumentos WHERE id=" + id;
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
    public boolean doGrabaDatos(EquipoDocTecnica equipoDocTecnica) {
        boolean actualizado = false;
        if (this.getPorId(equipoDocTecnica.getId()) == null) {
            actualizado = this.doInsertaDatos(equipoDocTecnica);
        } else {
            actualizado = this.doActualizaDatos(equipoDocTecnica);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(EquipoDocTecnica equipoDocTecnica) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            equipoDocTecnica.setId(this.getSiguienteId("aplicacionesdatos"));
            sql = " INSERT INTO  equiposdocumentos  "
                    + "( id,tipodato,valor,estado,fechacambio,usucambio,marca,modelo,tipoequipo ) "
                    + " VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, equipoDocTecnica.getId());

            statement.setString(2, equipoDocTecnica.getTipoDato());
            statement.setString(3, equipoDocTecnica.getValor());

            if (equipoDocTecnica.getEstado() != null) {
                statement.setInt(4, equipoDocTecnica.getEstado());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            statement.setLong(5, Utilidades.getFechaLong(equipoDocTecnica.getFechacambio()));
            statement.setLong(6, equipoDocTecnica.getUsucambio().getId());

            statement.setString(7, equipoDocTecnica.getMarca());
            statement.setString(8, equipoDocTecnica.getModelo());
            statement.setString(9, equipoDocTecnica.getTipoEquipo());

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
    public boolean doActualizaDatos(EquipoDocTecnica equipoDocTecnica) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " update    equiposdocumentos SET  "
                    + "tipodato=?,valor=?,estado=?,fechacambio=?,usucambio=? "
                    + " WHERE id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, equipoDocTecnica.getTipoDato());
            statement.setString(2, equipoDocTecnica.getValor());
            if (equipoDocTecnica.getEstado() != null) {
                statement.setInt(3, equipoDocTecnica.getEstado());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            statement.setLong(4, Utilidades.getFechaLong(equipoDocTecnica.getFechacambio()));
            statement.setLong(5, equipoDocTecnica.getUsucambio().getId());
            statement.setLong(6, equipoDocTecnica.getId());
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
    public boolean doBorraDatos(EquipoDocTecnica equipoDocTecnica) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE    equiposdocumentos  WHERE id=?  ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, equipoDocTecnica.getId());
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

    public boolean doActualizaDatosFichero(EquipoDocTecnica equipoDocTecnica) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " update    equiposdocumentos SET  "
                    + "tipodato=?,valor=?,fichero=?,estado=?,fechacambio=?,usucambio=? "
                    + " WHERE id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, equipoDocTecnica.getTipoDato());
            statement.setString(2, equipoDocTecnica.getValor());
            String ficheroAbsoluto = equipoDocTecnica.getPathAbsoluto();
            File file = Utilidades.iStoFile(equipoDocTecnica.getStreamInputStream(), ficheroAbsoluto);
            FileInputStream in = new FileInputStream(file);
            statement.setBinaryStream(3, in, (int) file.length());

            if (equipoDocTecnica.getEstado() != null) {
                statement.setInt(4, equipoDocTecnica.getEstado());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            statement.setLong(5, Utilidades.getFechaLong(equipoDocTecnica.getFechacambio()));
            statement.setLong(6, equipoDocTecnica.getUsucambio().getId());
            statement.setLong(7, equipoDocTecnica.getId());
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
    public ArrayList<EquipoDocTecnica> getLista(String texto) {
        return getLista(null, null);
    }

    public ArrayList<EquipoDocTecnica> getLista(String texto, EquipoBean equipoBean) {
        Connection connection = null;
        ArrayList<EquipoDocTecnica> lista = new ArrayList<>();
        String sqlDato = "";
        try {
            connection = this.getConexionBBDD();
            sqlDato = "SELECT * FROM equiposdocumentos WHERE idquipo=" + equipoBean.getId();
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sqlDato);
                while (resulSet.next()) {
                    EquipoDocTecnica dato = getRegistroResulset(resulSet, equipoBean);
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
    public EquipoDocTecnica getPorCodigo(String codigo) {
        return null;
    }
}
