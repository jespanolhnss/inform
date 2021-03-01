package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.LopdDocumentoBean;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LopdDocumentoDao extends ConexionDao implements Serializable {

    private static final Logger logger = LogManager.getLogger(LopdDocumentoDao.class);
    private static final long serialVersionUID = 1L;

    public LopdDocumentoDao() {
        super();
    }

    public boolean grabaDatos(LopdDocumentoBean documento) {
        boolean actualizado = false;

        if (documento.getId().equals(new Long(0))) {
            actualizado = this.insertaDatos(documento);
        } else {
            actualizado = this.actualizaDatos(documento);
        }
        return actualizado;
    }

    public boolean insertaDatos(LopdDocumentoBean documento) {
        Connection connection = null;
        boolean insertado = false;
        FileInputStream is = null;
        try {
            connection = super.getConexionBBDD();
            documento.setId(getSiguienteId("lopd_documentos"));
            is = new FileInputStream(documento.getFicheroAdjunto());

            sql = " INSERT INTO lopd_documentos (id,idincidencia,fichero,fechacambio,hora,usucambio,estado)"
                    + " VALUES (?,?,?,?,?,?,?)  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, documento.getId());
            statement.setLong(2, documento.getIdIncidenica().getId());
            statement.setBlob(3, is);
            statement.setLong(4, Utilidades.getFechaLong(documento.getFecha()));
            statement.setInt(5, documento.getHora());
            statement.setLong(6, documento.getUsuCambio().getId());
            statement.setInt(7, documento.getEstado());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            doCierraConexion(connection);
        }
        return insertado;
    }

    public boolean actualizaDatos(LopdDocumentoBean documento) {
        Connection connection = null;
        boolean actualizado = false;
        FileInputStream is = null;
        try {
            connection = super.getConexionBBDD();
            documento.setId(getSiguienteId("lopd_documentos"));
            is = new FileInputStream(documento.getFicheroAdjunto());
            sql = " UPDATE  lopd_documentos (file =? ,fechacmabio=?,hora=?,usucambio=?) WHERE id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBlob(1, is);
            statement.setLong(2, Utilidades.getFechaLong(documento.getFecha()));
            statement.setInt(3, documento.getHora());
            statement.setLong(4, documento.getUsuCambio().getId());
            statement.setLong(5, documento.getId());
            actualizado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            doCierraConexion(connection);
        }
        return actualizado;
    }

    public ArrayList<LopdDocumentoBean> getListaDocumentos(LopdIncidenciaBean incidencia) {
        Connection connection = null;
        ArrayList<LopdDocumentoBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM  lopd_documentos  WHERE estado= " + ConexionDao.BBDD_ACTIVOSI + " AND  idincidencia= "
                    + incidencia.getId();
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                LopdDocumentoBean documento = getDocumentoResulset(resulSet, incidencia);
                lista.add(documento);
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));

        } finally {
            doCierraConexion(connection);
        }
        return lista;
    }

    public LopdDocumentoBean getDocumentoResulset(ResultSet resulSet, LopdIncidenciaBean incidencia) {
        LopdDocumentoBean documento = new LopdDocumentoBean();
        try {
            /*
            ID
IDINCIDENCIA
FICHERO
HORA
USUCAMBIO
ESTADO
IFINFORMEJIMENA
FECHACAMBIO

             */
            documento.setId(resulSet.getLong("id"));
            documento.setFecha(Utilidades.getFechaLocalDate(resulSet.getLong("fechacambio")));
            documento.setHora(resulSet.getInt("hora"));
            if (incidencia != null) {
                documento.setIdIncidenica(incidencia);
            } else {
                documento.setIdIncidenica(
                        new LopdIncidenciaDao().getIncidenciaId(resulSet.getLong("idinciencia"), null, null));
            }
            //  documento.setFicheroAdjunto(resulSet.getBlob("fichero"));
            documento.setUsuCambio(new UsuarioDao().getPorId(resulSet.getLong("usucambio")));
            documento.setId(resulSet.getLong("IFINFORMEJIMENA"));
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }

        return documento;
    }

    public File getGeneraFilePdf(LopdDocumentoBean documento) {
        File file = null;
        try {
            FileOutputStream outpu = null;
            String pathname = documento.getPathAbsolutePdf();
            file = new File(pathname);
            outpu = new FileOutputStream(file);
            Blob archivo = new LopdDocumentoDao().getBlobPdfId(documento.getId());
            InputStream inStream = archivo.getBinaryStream();
            int size = (int) archivo.length();
            byte[] buffer = new byte[size];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outpu.write(buffer, 0, length);
            }
            outpu.close();
        } catch (Exception ioe) {
            logger.error(Utilidades.getStackTrace(ioe));
        }
        return file;
    }

    public java.sql.Blob getBlobPdfId(Long id) {
        Connection connection = null;
        java.sql.Blob pdfBlob = null;
        try {
            connection = super.getConexionBBDD();
            sql = "SELECT fichero  FROM lopd_documentos  WHERE id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resulSet = statement.executeQuery();
            if (resulSet.next()) {
                pdfBlob = resulSet.getBlob("fichero");
            }
            statement.close();
            logger.debug("SELECT docubin  FROM informes  WHERE id= " + id);
        } catch (SQLException e) {
            logger.error("SELECT docubin  FROM informes  WHERE id= " + id + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            doCierraConexion(connection);
        }
        return pdfBlob;
    }

}
