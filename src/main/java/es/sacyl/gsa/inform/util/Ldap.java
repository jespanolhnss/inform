package es.sacyl.gsa.inform.util;

import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.exceptiones.LoginException;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */

/*

* Clase para autentificar usuario a travÃ©s del servidor LDAP

*

 */
public class Ldap {

    private static final Logger logger = LoggerFactory.getLogger(Ldap.class);

    public Ldap() {

    }

    public UsuarioBean loginActiveDirectory(String user, String password) throws LoginException {

        UsuarioBean usuario = new UsuarioBean();

        try {

            Hashtable<String, String> envInicial = new Hashtable<>();

            String fileSeparator = System.getProperty("file.separator");

            String javaHome = System.getProperty("java.home");

            String keystore = null;

            if (javaHome.indexOf("jre") == -1) {

                keystore = javaHome + fileSeparator + "jre" + fileSeparator + "lib" + fileSeparator + "security"
                        + fileSeparator + "ldap";
            } else {
                keystore = javaHome + fileSeparator + "lib" + fileSeparator + "security" + fileSeparator + "ldap";
            }

            /**
             *
             * keystore = javaHome + fileSeparator + "jre" + fileSeparator +
             * "lib" + fileSeparator + "security" + fileSeparator + "ldap"; if
             * (!new File(keystore).exists()) { keystore = javaHome +
             * fileSeparator + "lib" + fileSeparator + "security" +
             * fileSeparator + "ldap"; if (!new File(keystore).exists()) {
             * Notification.show(keystore + " NO ENCONTRADO", 500,
             * Notification.Position.MIDDLE); } }
             */
            System.setProperty("javax.net.ssl.trustStore", keystore);

            System.setProperty("javax.net.ssl.trustStorePassword", "ldap123");

            envInicial.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

            //envInicial.put(Context.PROVIDER_URL, "ldaps://hnssntdc0001.grs.root");
            envInicial.put(Context.PROVIDER_URL, "ldaps://hnssntdc0002.grs.root");

            envInicial.put(Context.SECURITY_AUTHENTICATION, "simple");

            envInicial.put(Context.SECURITY_PRINCIPAL, user + "@GRS.ROOT");

            envInicial.put(Context.SECURITY_CREDENTIALS, password);

            envInicial.put(Context.REFERRAL, "follow");

            envInicial.put("com.sun.jndi.ldap.read.timeout", "3000");

            DirContext ctx = new InitialDirContext(envInicial);

            String filter = "(&(objectClass=user)(samaccountname=" + user + "))";

            SearchControls ctls = new SearchControls();

            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            ctls.setReturningAttributes(new String[]{"sn", "givenName", "uid"});

            NamingEnumeration answer = ctx.search("dc=grs,dc=root", filter, ctls);

            while (answer.hasMoreElements()) {

                SearchResult searchResult = (SearchResult) answer.next();

                Attributes attributes = searchResult.getAttributes();

                Attribute attUid = attributes.get("uid");

                Attribute attNombre = attributes.get("givenName");

                Attribute attApellidos = attributes.get("sn");

                if (attUid != null) {

                    usuario.setDni(String.valueOf(attUid.get()));

                    usuario.setDni(user);

                }

                if (attNombre != null) {

                    usuario.setNombre(String.valueOf(attNombre.get()));

                }

                if (attApellidos != null) {

                    // usuario.setApellidos(String.valueOf(attApellidos.get()));
                    usuario.setApellido1(String.valueOf(attApellidos.get()));

                }

            }

            ctx.close();

        } catch (AuthenticationException e) {

            logger.error("Error de autentificacion", e);

            // new NotificacionInfo("ldap.loginActiveDirectory : Error de autentificacion",
            // true);
            throw new LoginException("Error de autentificacion", e);

        } catch (NamingException e) {

            logger.error("LDAP no funciona NamingException", e);

            Notification.show("ldap.loginActiveDirectory : . Error de NamingException ");

            throw new LoginException("Active Directory no funciona", e);

        } catch (Exception e) {

            logger.error("Error desconocido", e);

            // new NotificacionInfo("ldap.loginActiveDirectory : . Error de NamingException
            // ", true);
            throw new LoginException("Active Directory no funciona", e);

        }

        return usuario;

    }

    /*
    para ldap no se usa

    static UsuarioBean loginLDAP(String user, String password) throws LoginException {
        UsuarioBean usuario = new UsuarioBean();
        String url = "ldaps://ldap.hnss.sacyl.es:636/dc=sacyl,dc=es";
        String LDAP_ADMIN_PASSWORD = "ObCejSoafOw0";
        String LDAP_ADMIN_DN = "cn=Application Manager,cn=config";
        String LDAP_FILTRO = "(&(ou:dn:=People)(objectClass=posixAccount)(uid=%s))";
        String userDN = null;
        String fileSeparator = System.getProperty("file.separator");
        String javaHome = System.getProperty("java.home");
        String keystore = null;
       if (javaHome.indexOf("jre") == -1) {
            keystore = javaHome + fileSeparator + "jre" + fileSeparator + "lib" + fileSeparator + "security"
                    + fileSeparator + "ldap";
        } else {
            keystore = javaHome + fileSeparator + "lib" + fileSeparator + "security" + fileSeparator + "ldap";
        }
        System.setProperty("javax.net.ssl.trustStore", keystore);
        System.setProperty("javax.net.ssl.trustStorePassword", "ldap123");
        try {
            Hashtable<String, String> envInicial = new Hashtable<String, String>();
            envInicial.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            envInicial.put(Context.PROVIDER_URL, url);
            envInicial.put(Context.SECURITY_AUTHENTICATION, "simple");
            envInicial.put(Context.SECURITY_PRINCIPAL, LDAP_ADMIN_DN);
            envInicial.put(Context.SECURITY_CREDENTIALS, LDAP_ADMIN_PASSWORD);
            logger.debug("Conectando con url: " + url);
            logger.debug("Conectando con cadena: " + "cn=Application Manager,cn=config");
            DirContext ctxInicial = new InitialDirContext(envInicial);
            SearchControls ctlsInicial = new SearchControls();
            ctlsInicial.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctlsInicial.setReturningAttributes(new String[]{});
            String filterInicial = String.format(LDAP_FILTRO, new Object[]{user});
            NamingEnumeration answerInicial = ctxInicial.search("", filterInicial, ctlsInicial);
            while (answerInicial.hasMore()) {
                SearchResult searchResult = (SearchResult) answerInicial.next();
                userDN = searchResult.getNameInNamespace();
            }
            ctxInicial.close();
            if (userDN == null) {
                logger.warn("Error de autentificacion");
                 Notification.show("ldap.loginLDAP : . Error de autentificacion");
                throw new LoginException("Error de autentificacion");
            }
            Hashtable<String, String> envUser = new Hashtable<String, String>();
            envUser.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            envUser.put(Context.PROVIDER_URL, url);
            envUser.put(Context.SECURITY_AUTHENTICATION, "simple");
            envUser.put(Context.SECURITY_PRINCIPAL, userDN);
            envUser.put(Context.SECURITY_CREDENTIALS, password);
            logger.debug("Conectando con url: " + url);
            logger.debug("Conectando con cadena: " + userDN);
            DirContext ctxUsuario = new InitialDirContext(envUser);
            int counter = 0;
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[]{"uid", "givenName", "sn", "objectClass", "departmentNumber",
                "title", "sacylPlaceCode", "hurhsiclex003Perfil", "sacylAdministrativeCode"});
            String filter = String.format(LDAP_FILTRO, new Object[]{user});
            NamingEnumeration answer = ctxUsuario.search("", filter, ctls);
            while (answer.hasMore()) {
                counter++;
                SearchResult searchResult = (SearchResult) answer.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute attNombre = attributes.get("givenName");
                Attribute attApellidos = attributes.get("sn");
                usuario.setDni(user);
                usuario.setNombre(attNombre.get().toString());
                usuario.setApellido1(attApellidos.get().toString());
            }
            ctxUsuario.close();
            if (counter == 0) {
                logger.warn("No se han encontrado usuarios LDAP [user = " + user + "]");
               Notification.show("ldap.loginLDAP : No se han encontrado usuarios LDAP [user = " + user + "]");
            } else if (counter > 1) {
                logger.warn("Se ha encontrado mÃ¡s de un usuarios LDAP [user = " + user + "]");
                 Notification.show(
                        "ldap.loginLDAP : Se ha encontrado mÃ¡s de un usuarios LDAP [user =  " + user + "]");
            }
            return usuario;
        } catch (AuthenticationException e) {
            logger.error("Error de autentificacion", e);
              Notification.show("ldap.loginLDAP :  AuthenticationException  error de autentificacion ");
            throw new LoginException("Error de autentificacion", e);
        } catch (NamingException e) {
            logger.error("LDAP no funciona", e);
             Notification.show("ldap.loginLDAP :  NamingException  error de conexion ");
            throw new LoginException("LDAP no funciona", e);
        } catch (Exception e) {
            logger.error("Error desconocido", e);
             Notification.show("ldap.loginLDAP :   error desconocido  ");
            throw new LoginException("LDAP no funciona", e);
        }
    }

     */

 /*
    public static void main(String[] args) {
       String url = "ldaps://ldap.hnss.sacyl.es:636/dc=sacyl,dc=es";
        String LDAP_ADMIN_PASSWORD = "ObCejSoafOw0";
        String LDAP_ADMIN_DN = "cn=Application Manager,cn=config";
        String LDAP_FILTRO = "(&(ou:dn:=People)(objectClass=posixAccount)(uid=%s))";
        String userDN = null;
        String user = "13152398D";
        String password = "eerror!";
        String fileSeparator = System.getProperty("file.separator");
        String javaHome = System.getProperty("java.home");
        String keystore = null;
        if (javaHome.indexOf("jre") == -1) {
           keystore = javaHome + fileSeparator + "jre" + fileSeparator + "lib" + fileSeparator + "security"
                    + fileSeparator + "ldap";
        } else {
            keystore = javaHome + fileSeparator + "lib" + fileSeparator + "security" + fileSeparator + "ldap";
        }
        System.setProperty("javax.net.ssl.trustStore", keystore);
        System.setProperty("javax.net.ssl.trustStorePassword", "ldap123");
        try {
            Hashtable<String, String> envInicial = new Hashtable<String, String>();
            envInicial.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            envInicial.put(Context.PROVIDER_URL, url);
            envInicial.put(Context.SECURITY_AUTHENTICATION, "simple");
            envInicial.put(Context.SECURITY_PRINCIPAL, LDAP_ADMIN_DN);
            envInicial.put(Context.SECURITY_CREDENTIALS, LDAP_ADMIN_PASSWORD);
            logger.debug("Conectando con url: " + url);
            logger.debug("Conectando con cadena: " + "cn=Application Manager,cn=config");
            DirContext ctxInicial = new InitialDirContext(envInicial);
            SearchControls ctlsInicial = new SearchControls();
            ctlsInicial.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctlsInicial.setReturningAttributes(new String[]{});
            String filterInicial = String.format(LDAP_FILTRO, new Object[]{user});
            NamingEnumeration answerInicial = ctxInicial.search("", filterInicial, ctlsInicial);
            while (answerInicial.hasMore()) {



                SearchResult searchResult = (SearchResult) answerInicial.next();

                userDN = searchResult.getNameInNamespace();



            }



            ctxInicial.close();



            if (userDN == null) {

                logger.error("No se encuentra el usuario");

            }




            Hashtable<String, String> envUser = new Hashtable<String, String>();



            envUser.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

            envUser.put(Context.PROVIDER_URL, url);

            envUser.put(Context.SECURITY_AUTHENTICATION, "simple");

            envUser.put(Context.SECURITY_PRINCIPAL, userDN);

            envUser.put(Context.SECURITY_CREDENTIALS, password);



            logger.debug("Conectando con url: " + url);

            logger.debug("Conectando con cadena: " + userDN);



            DirContext ctxUsuario = new InitialDirContext(envUser);



            int counter = 0;



            // Comenzamos a buscar

            SearchControls ctls = new SearchControls();

            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            ctls.setReturningAttributes(new String[]{"uid", "givenName", "sn", "objectClass", "departmentNumber",

                "title", "sacylPlaceCode", "hurhsiclex003Perfil", "sacylAdministrativeCode"});

            String filter = String.format(LDAP_FILTRO, new Object[]{user});

            NamingEnumeration answer = ctxUsuario.search("", filter, ctls);

            while (answer.hasMore()) {

                counter++;

                SearchResult searchResult = (SearchResult) answer.next();

                Attributes attributes = searchResult.getAttributes();

                Attribute attNombre = attributes.get("givenName");

                Attribute attApellidos = attributes.get("sn");

                logger.info(

                        "Login correcto usuario " + attNombre.get().toString() + " " + attApellidos.get().toString());

            }



            ctxUsuario.close();



            if (counter == 0) {

                logger.warn("No se han encontrado usuarios LDAP [user = " + user + "]");

            } else if (counter > 1) {

                logger.warn("Se ha encontrado mÃ¡s de un usuarios LDAP [user = " + user + "]");

            }



        } catch (AuthenticationException e) {

            // Error con la contraseÃ±a

            logger.warn("Error de autentificaciÃ³n");

        } catch (NamingException e) {

            logger.error("Problem getting attribute:" + e.toString());

        }



    }

     */
}
