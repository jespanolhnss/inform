package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdSujetoBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.PacienteBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LopdIncidenciaDao extends ConexionDao {

    private static final Logger logger = LogManager.getLogger(LopdIncidenciaDao.class);

    public LopdIncidenciaDao() {
        super();
    }

    public boolean grabaDatos(LopdIncidenciaBean incidencia) {
        boolean actualizado = false;

        if (incidencia.getId().equals(new Long(0))) {
            actualizado = this.insertaDatos(incidencia);
        } else {
            actualizado = this.actualizaDatos(incidencia);
        }
        return actualizado;
    }

    public boolean actualizaDatos(LopdIncidenciaBean incidencia) {
        Connection connection = null;
        boolean insertado = false;
        try {
            connection = super.getConexionBBDD();

            sql = " UPDATE  lopd_incidencias set  idusuario=?, tipo=?,fecha=?,hora=?, numerohc=? "
                    + ",idDocumento=?, fechaDocu=?,horaDocu=?,servicio=?,	descriDocu=?,	perdidaDatos=?"
                    + ",descripcionError=?, descripcionSolucion=?,fechaCambio=?,usuCambio=?, resuelta=? , fechasolucion=?   "
                    + ", idusuarioafectado=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, incidencia.getUsuarioRegistra().getId());
            statement.setLong(2, incidencia.getTipo().getId());
            statement.setLong(3, Long.parseLong(incidencia.getFechaHora().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            statement.setInt(4, Integer.parseInt(incidencia.getFechaHora().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

            if (incidencia.getPaciente() != null && incidencia.getPaciente().getNumerohc() != null) {
                statement.setString(5, incidencia.getPaciente().getNumerohc());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            if (incidencia.getIdDocumento() != null && !incidencia.getIdDocumento().isEmpty()) {
                statement.setString(6, incidencia.getIdDocumento());
            } else {
                statement.setNull(6, Types.CHAR);
            }

            if (incidencia.getFechaHoraDocumento() == null) {
                statement.setNull(7, Types.INTEGER);
                statement.setNull(8, Types.INTEGER);
            } else {
                statement.setLong(7, Long.parseLong(incidencia.getFechaHoraDocumento().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
                statement.setInt(8, Integer.parseInt(incidencia.getFechaHoraDocumento().format(DateTimeFormatter.ofPattern("HHmm"))));

            }

            if (incidencia.getServicio() != null) {
                statement.setLong(9, incidencia.getServicio().getId());
            } else {
                statement.setNull(9, Types.INTEGER);
            }

            if (incidencia.getDescriDocu() != null && !incidencia.getDescriDocu().isEmpty()) {
                statement.setString(10, incidencia.getDescriDocu());
            } else {
                statement.setNull(10, Types.VARCHAR);
            }

            if (incidencia != null && incidencia.getPerdidaDatos() == true) {
                statement.setBoolean(11, Boolean.TRUE);
            } else {
                statement.setBoolean(11, Boolean.FALSE);
            }

            statement.setString(12, incidencia.getDescripcionError());

            if (incidencia.getDescripcionSolucion() != null && !incidencia.getDescripcionSolucion().isEmpty()) {
                statement.setString(13, incidencia.getDescripcionSolucion());
            } else {
                statement.setNull(13, Types.VARCHAR);
            }
            statement.setLong(14, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

            statement.setLong(15, incidencia.getUsuCambio().getId());

            statement.setBoolean(16, incidencia.getResuelta());
            if (incidencia.getFechaSolucion() != null) {
                statement.setLong(17, Long.parseLong(incidencia.getFechaSolucion().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

            } else {
                statement.setNull(17, Types.DOUBLE);
            }

            if (incidencia.getUsuarioAfectado() != null) {
                statement.setLong(18, incidencia.getUsuarioAfectado().getId());
            } else {
                statement.setNull(18, Types.DOUBLE);
            }
            statement.setLong(19, incidencia.getId());

            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    public boolean actualizaRespuesta(LopdIncidenciaBean incidencia) {
        Connection connection = null;
        boolean insertado = false;
        try {
            connection = super.getConexionBBDD();

            sql = " UPDATE  lopd_incidencias set   descripcionSolucion=?,fechaCambio=?,usuCambio=?, resuelta=? "
                    + ", fechasolucion=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (incidencia.getDescripcionSolucion() != null && !incidencia.getDescripcionSolucion().isEmpty()) {
                statement.setString(1, incidencia.getDescripcionSolucion());
            } else {
                statement.setNull(1, Types.VARCHAR);
            }

            statement.setLong(2, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

            statement.setLong(3, incidencia.getUsuCambio().getId());

            statement.setBoolean(4, incidencia.getResuelta());
            if (incidencia.getFechaSolucion() != null) {
                statement.setLong(5, Long.parseLong(incidencia.getFechaSolucion().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            } else {
                statement.setNull(5, Types.DOUBLE);
            }
            statement.setLong(6, incidencia.getId());

            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    public boolean insertaDatos(LopdIncidenciaBean incidencia) {
        Connection connection = null;
        boolean insertado = false;
        Long id;
        try {
            connection = super.getConexionBBDD();
            id = this.getSiguienteId("lopd_incidencias");
            incidencia.setId(id);

            sql = " INSERT INTO lopd_incidencias (id, idusuario,  tipo,fecha,hora, numerohc "
                    + ",idDocumento, fechaDocu,horaDocu,servicio,	descriDocu,	perdidaDatos"
                    + ",descripcionError, descripcionSolucion,fechaCambio,usuCambio"
                    + ",resuelta,fechasolucion,estado,idusuarioafectado)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, incidencia.getId());
                statement.setLong(2, incidencia.getUsuarioRegistra().getId());
                statement.setLong(3, incidencia.getTipo().getId());
                statement.setLong(4, Long.parseLong(incidencia.getFechaHora().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
                statement.setLong(5, Long.parseLong(incidencia.getFechaHora().format(DateTimeFormatter.ofPattern("HHmm"))));

                if (incidencia.getPaciente() != null && incidencia.getPaciente().getNumerohc() != null && !incidencia.getPaciente().getNumerohc().isEmpty()) {
                    statement.setString(6, incidencia.getPaciente().getNumerohc());
                } else {
                    statement.setNull(6, Types.INTEGER);
                }

                if (incidencia.getIdDocumento() != null && !incidencia.getIdDocumento().isEmpty()) {
                    statement.setString(7, incidencia.getIdDocumento());
                } else {
                    statement.setNull(7, Types.CHAR);
                }

                if (incidencia.getFechaHoraDocumento() == null) {
                    statement.setNull(8, Types.INTEGER);
                    statement.setNull(9, Types.INTEGER);
                } else {
                    statement.setLong(8, Long.parseLong(incidencia.getFechaHoraDocumento().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
                    statement.setLong(9, Long.parseLong(incidencia.getFechaHoraDocumento().format(DateTimeFormatter.ofPattern("HHmm"))));
                }

                if (incidencia.getServicio() != null) {
                    statement.setLong(10, incidencia.getServicio().getId());
                } else {
                    statement.setNull(10, Types.INTEGER);
                }

                if (incidencia.getDescriDocu() != null && !incidencia.getDescriDocu().isEmpty()) {
                    statement.setString(11, incidencia.getDescriDocu());
                } else {
                    statement.setNull(11, Types.VARCHAR);
                }
                if (incidencia.getPerdidaDatos() == null) {
                    statement.setBoolean(12, Boolean.FALSE);
                } else {
                    statement.setBoolean(12, Boolean.TRUE);
                }

                statement.setString(13, incidencia.getDescripcionError());

                if (incidencia.getDescripcionSolucion() != null && !incidencia.getDescripcionSolucion().isEmpty()) {
                    statement.setString(14, incidencia.getDescripcionSolucion());
                } else {
                    statement.setNull(14, Types.VARCHAR);
                }
                statement.setLong(15, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

                statement.setLong(16, incidencia.getUsuCambio().getId());

                statement.setBoolean(17, incidencia.getResuelta());
                if (incidencia.getFechaSolucion() != null) {
                    statement.setLong(18, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

                } else {
                    statement.setNull(18, Types.DOUBLE);
                }

                statement.setInt(19, ConexionDao.BBDD_ACTIVOSI);

                if (incidencia.getUsuarioAfectado() != null) {
                    statement.setLong(20, incidencia.getUsuarioAfectado().getId());
                } else {
                    statement.setNull(20, Types.DOUBLE);
                }
                insertado = statement.executeUpdate() > 0;
            }
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));

        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    public LopdIncidenciaBean getIncidenciaId(Long id, UsuarioBean usuario, PacienteBean paciente) {
        Connection connection = null;
        LopdIncidenciaBean incidencia = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT i.*,t.id as idtipo,t.descripcion as descripciontipo " + " FROM lopd_incidencias i "
                    + " JOIN lopd_tipos t ON t.id=i.tipo " + " JOIN usuarios u On u.id=i.idusuario " + " WHERE id= "
                    + id;

            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    LopdTipoBean tipoI = new LopdTipoBean();
                    tipoI.setId(resulSet.getLong("idtipo"));
                    tipoI.setDescripcion(resulSet.getString("descripciontipo "));
                    incidencia = getResulset(resulSet, usuario, paciente, tipoI);
                }
            }
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return incidencia;
    }

    /**
     *
     * @param desde
     * @param hasta
     * @param tipo
     * @param usuario
     * @param pendiente
     * @param texto
     * @param numerohc
     * @return
     */
    public ArrayList<LopdIncidenciaBean> getListaInicidencias(LocalDate desde, LocalDate hasta, LopdTipoBean tipo,
            UsuarioBean usuario, String pendiente, String texto, String numerohc) {
        Connection connection = null;
        ArrayList<LopdIncidenciaBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            sql = " SELECT i.*,t.id as idtipo,t.descripcion as descripciontipo,t.sujeto  " + " FROM lopd_incidencias i "
                    + " JOIN lopd_tipos t ON t.id=i.tipo " + " JOIN usuarios u On u.id=i.idusuario " + " WHERE 1=1 ";
            if (desde != null) {
                sql = sql.concat(" AND fecha>=" + desde.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            }

            if (hasta != null) {
                sql = sql.concat(" AND fecha<=" + hasta.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            }

            if (tipo != null) {
                sql = sql.concat(" AND tipo=" + Long.toString(tipo.getId()));
            }

            if (usuario != null) {
                sql = sql.concat(" AND idusuario=" + usuario.getId());
            }
            if (numerohc != null && !numerohc.isEmpty()) {
                sql = sql.concat(" AND numerohc='" + numerohc + "'");
            }
            if (pendiente != null) {
                if (pendiente.toUpperCase().equals("S")) {
                    sql = sql.concat(" AND resuelta= 0");
                } else {
                    sql = sql.concat(" AND resuelta=1 ");
                }
            }
            if (texto != null && !texto.isEmpty()) {
                if (Utilidades.isNumero(texto)) {
                    sql = sql.concat(" OR ( id=" + texto + " OR numerohc=" + texto + " ) ");
                } else {
                    sql = sql.concat(" OR UPPER(i.descripcionerror) like '%" + texto.toUpperCase() + "%'");
                }
            }
            sql = sql.concat(" order by i.id desc");
            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);

                while (resulSet.next()) {

                    LopdIncidenciaBean incidencia = getResulset(resulSet, usuario, null, null);
                    lista.add(incidencia);
                }
            }

        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    public LopdIncidenciaBean getResulset(ResultSet rs, UsuarioBean usuario, PacienteBean paciente, LopdTipoBean tipo) {
        LopdIncidenciaBean incidencia = new LopdIncidenciaBean();
        LocalDate date;
        try {
            incidencia.setId(rs.getLong("id"));

            if (usuario == null) {
                incidencia.setUsuarioRegistra(new UsuarioDao().getPorId(rs.getLong("idusuario")));
            } else {
                incidencia.setUsuarioRegistra(usuario);
            }

            if (tipo == null) {
                incidencia.setTipo(new LopdTipoDao().getPorId(rs.getLong("tipo")));
            } else {
                incidencia.setTipo(tipo);
            }

            incidencia.setSujeto(LopdSujetoBean.getTipoSujeto(incidencia.getTipo()));

            incidencia.setFechaHora(Utilidades.getFechaHoraLocalDateTime(rs.getLong("fecha"), rs.getInt("hora")));

            if (paciente == null) {
                if (rs.getString("numerohc") != null) {
                    incidencia.setPaciente(new PacienteDao().getPacienteNhc(rs.getString("numerohc")));
                }
            } else {
                incidencia.setPaciente(paciente);
            }

            incidencia.setIdDocumento(rs.getString("iddocumento"));
            incidencia.setFechaHoraDocumento(Utilidades.getFechaHoraLocalDateTime(rs.getLong("fechadocu"), rs.getInt("horadocu")));
            if (rs.getLong("servicio") != 0) {
                incidencia.setServicio(new GfhDao().getPorId(rs.getLong("servicio")));
            }

            incidencia.setPerdidaDatos(rs.getBoolean("perdidadatos"));

            incidencia.setDescriDocu(rs.getString("descridocu"));
            incidencia.setDescripcionError(rs.getString("descripcionerror"));
            incidencia.setDescripcionSolucion(rs.getString("descripcionsolucion"));
            incidencia.setResuelta(rs.getBoolean("resuelta"));
            if (rs.getLong("fechasolucion") != 0) {
                date = Utilidades.getFechaLocalDate(rs.getLong("fechasolucion"));
                incidencia.setFechaSolucion(date);
            }
            incidencia.setUsuarioAfectado(new UsuarioDao().getPorId(rs.getLong("idusuarioafectado")));
            incidencia.setUsuCambio(new UsuarioDao().getPorId(rs.getLong("usucambio")));
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }

        return incidencia;
    }

    public boolean doBorraDatos(LopdIncidenciaBean lopdIncidenciaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  lopd_incidencias WHERE id='" + lopdIncidenciaBean.getId() + "'";
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

}
