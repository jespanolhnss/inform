package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
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
public class AplicacionPerfilDao extends ConexionDao implements Serializable, ConexionInterface<AplicacionPerfilBean> {

    private static final Logger LOGGER = LogManager.getLogger(AplicacionPerfilDao.class);
    private static final long serialVersionUID = 1L;

    // id,nombreperfil,idaplicaciones,codigoapp,estado,usucambio.fechacambio
    /**
     *
     */
    public AplicacionPerfilDao() {
        super();
        sql = " SELECT appp.id as appperfileid,appp.nombreperfil as appperfilnombreperfil "
                + " ,appp.idaplicaciones as appperfilidaplicaciones "
                + " ,appp.codigoapp as appperfilcodigo,appp.estado as appperfilestado"
                + " ,appp. usucambio as appperfilusucambio,appp. fechacambio as appperfilfechacambio  "
                + " ,ap.id as aplicacionid,ap.nombre as aplicacionnombre,ap.proveedor as aplicacionproveedor "
                + " ,ap.ambito as aplicacionambito, ap.gestionUsuarios as aplicaciongestionusuarios"
                + " ,ap.descripcion as aplicaciondescripcion,ap.gfh as aplicaciongfh"
                + " ,ap.fechaInstalacion aplicacionfechaInstalacion,ap.estado as aplicacionestado"
                + " ,ap.usucambio as aplicacionusucambio,ap. fechacambio as aplicacionfechacambio  "
                + " ,gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + " ,gfh.asistencial as gfhasisencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado,gfh.gfhpersigo"
                + " ,usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + ",usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + ",usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + ",usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + ",usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + ",usu.idcategoria as usuarioidcategoria,usu.movil as usuariomovil"
                + ",usu.mailprivado as usuariomailprivado,usu.telegram as usuariotegegram"
                + ",usu.solicita as usuariosolicita"
                + ",uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ",uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + ",gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado,gfh.gfhpersigo, gfh.division  as gfhdivision"
                + " FROM aplicacionesPerfiles  appp "
                + " JOIN aplicaciones ap ON ap.id=appp.idaplicaciones "
                + " LEFT  JOIN gfh ON gfh.id=ap.gfh"
                + " LEFT JOIN usuarios usu ON usu.id=appp.usucambio"
                + " LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
                //+ " LEFT JOIN gfh gfh ON usu.idgfh = gfh.id"
                + " WHERE 1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public AplicacionPerfilBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null);
    }

    /**
     *
     * @param rs
     * @param aplicacionBean
     * @return
     */
    public static AplicacionPerfilBean getRegistroResulset(ResultSet rs, AplicacionBean aplicacionBean) {
        AplicacionPerfilBean aplicacionPerfilBean = new AplicacionPerfilBean();
        try {
            aplicacionPerfilBean.setId(rs.getLong("appperfileid"));
            aplicacionPerfilBean.setNombre(rs.getString("appperfilnombreperfil"));
            if (aplicacionBean != null) {
                aplicacionPerfilBean.setAplicacion(aplicacionBean);
            } else {
                //  aplicacionPerfilBean.setAplicacion(AplicacionDao.getRegistroResulset(rs, null, null, null));
                aplicacionPerfilBean.setAplicacion(new AplicacionDao().getPorId(rs.getLong("aplicacionid")));
            }
            aplicacionPerfilBean.setCodigo(rs.getString("appperfilcodigo"));
            aplicacionPerfilBean.setEstado(rs.getInt("appperfilestado"));
            aplicacionPerfilBean.setFechacambio(Utilidades.getFechaLocalDate(rs.getLong("appperfilfechacambio")));
            // aplicacionBean.setUsucambio(new UsuarioDao().getPorId(rs.getLong("appperfilusucambio")));
            aplicacionPerfilBean.setUsucambio(new UsuarioDao().getRegistroResulset(rs));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return aplicacionPerfilBean;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public AplicacionPerfilBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            Long id = Long.parseLong(codigo);
            return getPorId(id);
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public AplicacionPerfilBean getPorId(Long id) {
        Connection connection = null;
        AplicacionPerfilBean aplicacionPerfilBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND appp.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    aplicacionPerfilBean = getRegistroResulset(resulSet, null);
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
        return aplicacionPerfilBean;
    }

    /**
     *
     * @param aplicacionPerfilBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(AplicacionPerfilBean aplicacionPerfilBean) {
        boolean actualizado = false;
        if (this.getPorId(aplicacionPerfilBean.getId()) == null) {
            aplicacionPerfilBean.setId(getSiguienteId("aplicacionesperfiles"));
            actualizado = this.doInsertaDatos(aplicacionPerfilBean);
        } else {
            actualizado = this.doActualizaDatos(aplicacionPerfilBean);
        }
        return actualizado;
    }

    /**
     *
     * @param aplicacionPerfilBean
     * @return
     */
    @Override
    public boolean doInsertaDatos(AplicacionPerfilBean aplicacionPerfilBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            // id,nombreperfil,idaplicaciones,codigoapp,estado,usucambio.fechacambio
            sql = " INSERT INTO  aplicacionesperfiles  "
                    + "( id, nombreperfil, idaplicaciones,  codigoapp, estado,usucambio, fechacambio ) "
                    + " VALUES "
                    + "(?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, aplicacionPerfilBean.getId());

            if (aplicacionPerfilBean.getNombre() != null) {
                statement.setString(2, aplicacionPerfilBean.getNombre());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (aplicacionPerfilBean.getAplicacion() != null && aplicacionPerfilBean.getAplicacion().getId() != null) {
                statement.setLong(3, aplicacionPerfilBean.getAplicacion().getId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            if (aplicacionPerfilBean.getCodigo() != null) {
                statement.setString(4, aplicacionPerfilBean.getCodigo());
            } else {
                statement.setNull(4, Types.CHAR);
            }

            if (aplicacionPerfilBean.getEstado() != null) {
                statement.setInt(5, aplicacionPerfilBean.getEstado());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            statement.setLong(6, aplicacionPerfilBean.getUsucambio().getId());
            statement.setLong(7, Utilidades.getFechaLong(aplicacionPerfilBean.getFechacambio()));

            insertadoBoolean = statement.executeUpdate() > 0;
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

    /**
     *
     * @param aplicacionPerfilBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(AplicacionPerfilBean aplicacionPerfilBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            // id,nombreperfil,idaplicaciones,codigoapp,estado,usucambio.fechacambio
            connection = super.getConexionBBDD();
            sql = " UPDATE   aplicacionesperfiles  SET "
                    + " nombreperfil=?,   codigoapp=?,  estado=?,usucambio=?, fechacambio=? "
                    + " WHERE  id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (aplicacionPerfilBean.getAplicacion() != null && aplicacionPerfilBean.getAplicacion().getId() != null) {
                statement.setString(1, aplicacionPerfilBean.getNombre());
            } else {
                statement.setNull(1, Types.CHAR);
            }

            if (aplicacionPerfilBean.getCodigo() != null) {
                statement.setString(2, aplicacionPerfilBean.getCodigo());
            } else {
                statement.setNull(2, Types.CHAR);
            }

            if (aplicacionPerfilBean.getEstado() != null) {
                statement.setInt(3, aplicacionPerfilBean.getEstado());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            statement.setLong(4, aplicacionPerfilBean.getUsucambio().getId());
            statement.setLong(5, Utilidades.getFechaLong(aplicacionPerfilBean.getFechacambio()));
            statement.setLong(6, aplicacionPerfilBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
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

    /**
     *
     * @param aplicacionPerfiBean
     * @return
     */
    @Override
    public boolean doBorraDatos(AplicacionPerfilBean aplicacionPerfiBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            /*
            sql = " UPDATE   aplicacionesperfiles  SET estado=?,usucambio=?, fechacambio=? "
                    + " WHERE  id=? ";
             */
            sql = " DELETE FROM aplicacionesperfiles  WHERE  id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, aplicacionPerfiBean.getId());

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

    /**
     *
     * @param texto
     * @return
     */
    @Override
    public ArrayList<AplicacionPerfilBean> getLista(String texto) {
        return getLista(texto, null);
    }

    /**
     *
     * @param texto
     * @param aplicacionBean
     * @return
     */
    public ArrayList<AplicacionPerfilBean> getLista(String texto, AplicacionBean aplicacionBean) {
        Connection connection = null;
        ArrayList<AplicacionPerfilBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(appp.nombreperfil) like'%" + texto.toUpperCase() + "%'  ");
            }
            if (aplicacionBean != null) {
                sql = sql.concat(" AND appp.idaplicaciones =" + aplicacionBean.getId());
            }
            sql = sql.concat(" ORDER BY appp.nombreperfil  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, aplicacionBean));
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
}
