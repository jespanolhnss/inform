/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.EquipoAplicacionBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
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
public class EquipoAplicacionDao extends ConexionDao implements Serializable, ConexionInterface<EquipoAplicacionBean> {

    private static final Logger LOGGER = LogManager.getLogger(EquipoAplicacionDao.class);
    private static final long serialVersionUID = 1L;

    public EquipoAplicacionDao() {
        super();
        sql = " SELECT  eqap.id as equipoappid,eqap.fecha as equipoaplicacinfecha,eqap.comentario equipoappcomentario"
                + ",eqap.idequipo as equipoappidequipo  "
                + " ,ap.id as aplicacionid,ap.nombre as aplicacionnombre,ap.proveedor as aplicacionproveedor "
                + " ,ap.ambito as aplicacionambito, ap.gestionUsuarios as aplicaciongestionusuarios"
                + " ,ap. descripcion as aplicaciondescripcion,ap.gfh as aplicaciongfh"
                + " ,ap.fechaInstalacion aplicacionfechaInstalacion,   ap.estado as aplicacionestado"
                + " ,ap. usucambio as aplicacionusucambio,ap. fechacambio as aplicacionfechacambio "
                + " ,gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + " ,gfh.asistencial as gfhasisencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado"
                + " ,prvee.id as proveedorid, prvee.nombre as proveedornombre"
                + " ,prvee.direccion as proveedordirecion,prvee.codpostal as proveedorcodpostal"
                + " ,prvee.telefonos as proveedortelfoonos,prvee.mail as proveedormail"
                + " ,prvee.localidad as proveedorlocalidad,prvee.provincia  as proveedorprovincia"
                + " ,l.codigo as localidadcodigo,l.nombre as localidadnombre,l.codprov as localidadcodpro"
                + " ,p. codigo as provinciacodigo, p.nombre as provincianombre,p.codauto as provinciacodauto  "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado   "
                + " , usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
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
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado"
                + " FROM equipoaplicacion eqap "
                + " JOIN aplicaciones ap ON ap.id=eqap.IDAPLICACION"
                + " LEFT  JOIN gfh  ON gfh.id=ap.gfh"
                + " LEFT  JOIN proveedores prvee ON  prvee.id=ap.proveedor"
                + " LEFT  JOIN localidad l ON l.codigo=prvee.localidad"
                + " LEFT  JOIN provincia p ON p.codigo=l.codprov "
                + " LEFT  JOIN CAUTONOM a ON a.codigo=p.CODAUTO  "
                + " LEFT JOIN usuarios usu ON usu.id=ap.usucambio"
                + " LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
                + " LEFT JOIN gfh gfh ON usu.idgfh = gfh.id"
                + " WHERE 1=1 ";
    }

    @Override
    public EquipoAplicacionBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null, null);
    }

    public static EquipoAplicacionBean getRegistroResulset(ResultSet rs, EquipoBean equipoBean, AplicacionBean aplicacionBean) {
        EquipoAplicacionBean equipoAppBean = new EquipoAplicacionBean();
        try {
            equipoAppBean.setId(rs.getLong("equipoappid"));
            equipoAppBean.setFecha(Utilidades.getFechaLocalDate(rs.getString("equipoaplicacinfecha")));
            equipoAppBean.setComentario(rs.getString("equipoappcomentario"));
            if (equipoBean != null) {
                equipoAppBean.setEquipo(equipoBean);
            } else {
                equipoAppBean.setEquipo(new EquipoDao().getPorId(rs.getLong("equipoappidequipo")));
            }
            if (aplicacionBean != null) {
                equipoAppBean.setAplicacion(aplicacionBean);
            } else {
                equipoAppBean.setAplicacion(new AplicacionDao().getRegistroResulset(rs));
            }

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return equipoAppBean;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public EquipoAplicacionBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            Long id = Long.parseLong(codigo);
            return getPorId(id);
        }
        return null;
    }

    @Override
    public EquipoAplicacionBean getPorId(Long id) {
        Connection connection = null;
        EquipoAplicacionBean equipoappBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND eqap.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    equipoappBean = getRegistroResulset(resulSet, null, null);
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
        return equipoappBean;
    }

    public EquipoAplicacionBean getPorEquipoAppId(EquipoBean equipoBean, AplicacionBean aplicacionBean) {
        Connection connection = null;
        EquipoAplicacionBean equipoappBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND eqap.idequipo='" + equipoBean.getId() + "' AND  eqap.idaplicacion=" + aplicacionBean.getId());
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    equipoappBean = getRegistroResulset(resulSet, null, null);
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
        return equipoappBean;
    }

    @Override
    public boolean doGrabaDatos(EquipoAplicacionBean equipoAplicacionBean) {
        boolean actualizado = false;
        if (this.getPorId(equipoAplicacionBean.getId()) == null) {
            equipoAplicacionBean.setId(this.getSiguienteId("equipoaplicacion"));
            actualizado = this.doInsertaDatos(equipoAplicacionBean);
        } else {
            actualizado = this.doActualizaDatos(equipoAplicacionBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(EquipoAplicacionBean equipoAplicacionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = sql = "INSERT INTO     equipoaplicacion  "
                    + "( id,idequipo,  idaplicacion,  fecha,  comentario)"
                    + " VALUES (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, equipoAplicacionBean.getId());
            statement.setLong(2, equipoAplicacionBean.getEquipo().getId());
            statement.setLong(3, equipoAplicacionBean.getAplicacion().getId());
            statement.setLong(4, Utilidades.getFechaLong(equipoAplicacionBean.getFecha()));
            if (equipoAplicacionBean.getComentario() == null) {
                statement.setNull(5, Types.VARCHAR);
            } else {
                statement.setString(5, equipoAplicacionBean.getComentario());
            }
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException e) {
            LOGGER.error(sql, e);

        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doActualizaDatos(EquipoAplicacionBean equipoAplicacionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = sql = "UPDATE      equipoaplicacion  SET "
                    + "idequipo=?,  idaplicacion=?,  fecha=?,  comentario=?"
                    + " WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, equipoAplicacionBean.getEquipo().getId());
            statement.setLong(2, equipoAplicacionBean.getAplicacion().getId());
            statement.setLong(3, Utilidades.getFechaLong(equipoAplicacionBean.getFecha()));
            if (equipoAplicacionBean.getComentario() == null) {
                statement.setNull(4, Types.VARCHAR);
            } else {
                statement.setString(4, equipoAplicacionBean.getComentario());
            }
            statement.setLong(5, equipoAplicacionBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException e) {
            LOGGER.error(sql, e);

        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doBorraDatos(EquipoAplicacionBean equipoAplicacionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM    equipoaplicacion WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, equipoAplicacionBean.getId());
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

    public boolean doBorraDatos(EquipoBean equipoBean, AplicacionBean aplicacionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM    equipoaplicacion WHERE idequipo=?  AND idaplicacion=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, equipoBean.getId());
            statement.setLong(2, aplicacionBean.getId());
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

    @Override
    public ArrayList<EquipoAplicacionBean> getLista(String texto) {
        return getLista(texto, null, null);
    }

    /**
     *
     * @param texto
     * @param equipoBean
     * @param aplicacionBean
     * @return
     */
    public ArrayList<EquipoAplicacionBean> getLista(String texto, EquipoBean equipoBean, AplicacionBean aplicacionBean) {
        Connection connection = null;
        ArrayList<EquipoAplicacionBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (equipoBean != null) {
                sql = sql.concat(" AND idequipo='" + equipoBean.getId() + "'");
            }
            if (aplicacionBean != null) {
                sql = sql.concat(" AND idaplicacion='" + aplicacionBean.getId() + "'");
            }
            sql = sql.concat(" ORDER BY idequipo  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, equipoBean, aplicacionBean));
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
