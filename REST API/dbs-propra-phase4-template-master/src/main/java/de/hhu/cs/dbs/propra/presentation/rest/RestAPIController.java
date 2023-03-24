package de.hhu.cs.dbs.propra.presentation.rest;

import de.hhu.cs.dbs.propra.domain.model.User;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Path("/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class RestAPIController {



    @Inject
    private DataSource dataSource;

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;


    // .\gradlew run


    @Path("nutzer")
    @GET
    public List<Map<String, Object>> getNutzer(@QueryParam("email") @DefaultValue("") String email) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;
        String SQL = "SELECT ROWID, EMailAdresse, Password FROM Nutzer ";

        if (!email.isEmpty()) {
            SQL += " Where Nutzer.EMailAdresse =? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, email);

        } else {
            SQL += ";";
            preparedStatement = connection.prepareStatement(SQL);
        }

        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("email", resultSet.getObject(2));
            entity.put("passwort", resultSet.getObject(3));

            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }


    @Path("nutzer")
    @POST
    public Response addNutzer(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort) {

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer(EMailAdresse,Password) VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.executeUpdate();
            connection.close();
            return Response.status(Response.Status.CREATED)
                    .header("Location", uriInfo.getAbsolutePath() + "/" + getRowIDNutzer(email)).build();


        } catch (SQLException e) {
            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        }
    }

    private String getRowIDNutzer(String email) throws SQLException {
        return getNutzer(email).get(0).get("nutzerid").toString();
    }




    @Path("kunden")
    @GET
    public List<Map<String, Object>> getKunden(@QueryParam("email") @DefaultValue("") String email,
                                               @QueryParam("telefonnummer") @DefaultValue("") String telefonnummer) throws SQLException {


        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;
        String SQL = "SELECT Nutzer.ROWID, Kunde.ROWID,Kunde.EMailAdresse,Nutzer.Password,Kunde.Telefonnumer    FROM Kunde, Nutzer Where Kunde.EMailAdresse=Nutzer.EMailAdresse ";

        if (!email.isEmpty() && !telefonnummer.isEmpty()) {
            SQL += "AND Kunde.EMailAdresse = ?  AND Telefonnumer =? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, telefonnummer);
        } else if (!email.isEmpty()) {
            SQL += "AND Kunde.EMailAdresse = ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, email);
        } else if (!telefonnummer.isEmpty()) {
            SQL += "AND Telefonnumer = ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, telefonnummer);

        } else {
            SQL += ";";
            preparedStatement = connection.prepareStatement(SQL);
        }

        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("kundenid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("telefon", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }


    @Path("kunden")
    @POST
    public Response addKunden(@FormDataParam("email") String email,
                              @FormDataParam("passwort") String passwort,
                              @FormDataParam("telefon") String telefon) throws SQLException {


        Connection connection = dataSource.getConnection();

        try {
            // connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            // so that we can use connection.commit() later and save all the modifications made since the last commit.

            if (getNutzer(email).isEmpty()) {
                addnutzer(email, passwort, connection);
            }
            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Kunde VALUES (?,?);");
            preparedStatement2.setObject(1, email);
            preparedStatement2.setObject(2, telefon);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();

            connection.commit();


        } catch (SQLException e) {

            connection.rollback(); //all the modifications are reverted until the last commit.

            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();

        } finally { // wird immer abgearbeitet
            connection.close();
        }

        return Response.status(Response.Status.CREATED).header("Location", uriInfo.getAbsolutePath() + "/" + getRowIDKunde(email))
                .build();
    }

    private void addnutzer(String email, String password, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
        preparedStatement.setObject(1, email);
        preparedStatement.setObject(2, password);
        preparedStatement.closeOnCompletion();
        preparedStatement.executeUpdate();
    }

    private String getRowIDKunde(String email) throws SQLException {
        return getKunden(email, "").get(0).get("kundenid").toString();
    }


    @Path("projektleiter")
    @GET
    public List<Map<String, Object>> getProjektleiter(@QueryParam("email") @DefaultValue("") String email,
                                                      @QueryParam("gehalt") @DefaultValue("") String gehalt) throws SQLException {


        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;
        String SQL = "SELECT Nutzer.ROWID, Projektleiter.ROWID,Projektleiter.EMailAdresse,Nutzer.Password,Projektleiter.Gehalt FROM " +
                "Projektleiter, Nutzer Where Projektleiter.EMailAdresse=Nutzer.EMailAdresse ";

        if (!email.isEmpty() && !gehalt.isEmpty()) {
            SQL += "AND Projektleiter.EMailAdresse = ?  AND Gehalt > ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, gehalt);
        } else if (!email.isEmpty()) {
            SQL += "AND Projektleiter.EMailAdresse = ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, email);
        } else if (!gehalt.isEmpty()) {
            SQL += "AND Gehalt > ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, gehalt);

        } else {
            SQL += ";";
            preparedStatement = connection.prepareStatement(SQL);
        }

        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("projektleiterid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("gehalt", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }


    @Path("projektleiter")
    @POST
    public Response addProjektleiter(@FormDataParam("email") String email,
                                     @FormDataParam("passwort") String passwort,
                                     @FormDataParam("gehalt") String gehalt) throws SQLException {
        //Integer faza


        Connection connection = dataSource.getConnection();

        try {
            // connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            // so that we can use connection.commit() later and save all the modifications made since the last commit.

            if (getNutzer(email).isEmpty()) {
                addnutzer(email, passwort, connection);
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Projektleiter VALUES (?,?);");
            preparedStatement2.setObject(1, email);
            preparedStatement2.setObject(2, gehalt);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();

            connection.commit();


        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.
            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();

        } finally { // wird immer abgearbeitet
            connection.close();
        }

        return Response.status(Response.Status.CREATED).header("Location", uriInfo.getAbsolutePath() +
                "/" + getProjektleiterid(email)).build();
    }


    private String getProjektleiterid(String email) throws SQLException {
        return getProjektleiter(email, "").get(0)
                .get("projektleiterid").toString();
    }


    @Path("spezialisten")
    @GET
    public List<Map<String, Object>> getSpezialisten(@QueryParam("email") @DefaultValue("") String email,
                                                     @QueryParam("verfuegbar") @DefaultValue("") String Verfuegbarkeitsstatus) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;
        String SQL = "SELECT Nutzer.ROWID, Spezialist.ROWID,Spezialist.EMailAdresse,Nutzer.Password,Spezialist.Verfuegbarkeitsstatus FROM " +
                "Nutzer, Spezialist Where Spezialist.EMailAdresse=Nutzer.EMailAdresse ";

        if (!email.isEmpty() && !Verfuegbarkeitsstatus.isEmpty()) {
            SQL += "AND Spezialist.EMailAdresse = ?  AND Verfuegbarkeitsstatus = ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, Verfuegbarkeitsstatus);
        } else if (!email.isEmpty()) {
            SQL += "AND Spezialist.EMailAdresse = ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, email);
        } else if (!Verfuegbarkeitsstatus.isEmpty()) {
            SQL += "AND Verfuegbarkeitsstatus = ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, Verfuegbarkeitsstatus);

        } else {
            SQL += ";";
            preparedStatement = connection.prepareStatement(SQL);
        }

        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("spezialistid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("Verfuegbarkeitsstatus", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }



    @Path("spezialisten")
    @POST
    public Response addSpezialisten(@FormDataParam("email") String email,
                                    @FormDataParam("passwort") String password,
                                    @FormDataParam("verfuegbarkeitsstatus") String Verfuegbarkeitsstatus) throws SQLException {

        Connection connection = dataSource.getConnection();


        try {
            // connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            // so that we can use connection.commit() later and save all the modifications made since the last commit.

            if (getNutzer(email).isEmpty()) {
                addnutzer(email, password, connection);
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Spezialist VALUES (?,?);");
            preparedStatement2.setObject(1, email);
            preparedStatement2.setObject(2, Verfuegbarkeitsstatus);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();

            connection.commit();


        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.
            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        } finally { // wird immer abgearbeitet
            connection.close();
        }

        return Response.status(Response.Status.CREATED).header("Location", uriInfo.getAbsolutePath() +
                "/" + getSpezialistid(email)).build();
    }


    private String getSpezialistid(String email) throws SQLException {
        return getSpezialisten(email, "").get(0).get("spezialistid").toString();
    }



    @Path("projekte")
    @GET
    public List<Map<String, Object>> getProjekte() throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ROWID,Projektname,Projektdeadline  From Projekt;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new HashMap<>();
            entity.put("projektid", resultSet.getObject(1));
            entity.put("name", resultSet.getObject(2));
            entity.put("deadline", resultSet.getObject(3));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/bewertungen")
    @GET
    public List<Map<String, Object>> getBewertungwen(@PathParam("projektid") Integer projektid) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;


        String SQL = "SELECT b.ROWID, b.Bepunktung, t.Inhalt FROM  Bewertung b ,Projekt p " +
                " LEFT JOIN Text t on b.BewertungID=t.BewertungID " +
                "WHERE b.ProjektID=p.ProjektID AND b.ProjektID= ? ;";

        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setObject(1, projektid);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("bewertungid", resultSet.getObject(1));
            entity.put("punktzahl", resultSet.getObject(2));
            entity.put("text", resultSet.getObject(3));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }


    @Path("projekte/{projektid}/aufgaben")
    @GET
    public List<Map<String, Object>> getAufgaben(@PathParam("projektid") Integer projektid) throws SQLException {

        
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;


        String SQL = "SELECT a.ROWID, a.Deadline, a.Beschreibung,a.Status,a.Vermerk FROM " +
                " Aufgabe a, Projekt p Where p.ProjektID=a.ProjektID  AND a.ProjektID= ? ;";


        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setObject(1, projektid);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("aufgabeid", resultSet.getObject(1));
            entity.put("deadline", resultSet.getObject(2));
            entity.put("beschreibung", resultSet.getObject(3));
            entity.put("status", resultSet.getObject(4));
            entity.put("prioritaet", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/spezialisten")
    @GET
    public List<Map<String, Object>> getSpe(@PathParam("projektid") Integer projektid) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;


        String SQL = "SELECT s.ROWID, s.Verfuegbarkeitsstatus, s.EMailAdresse, n.Password FROM " +
                "Spezialist s,  arbeitet_an a, Nutzer n Where s.EMailAdresse=a.EMailAdresse AND " +
                "s.EMailAdresse=n.EMailAdresse AND a.ProjektID= ? ;";

        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setObject(1, projektid);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("spezialistid", resultSet.getObject(1));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }


    @Path("entwickler")
    @GET
    public List<Map<String, Object>> getEntwickler(@QueryParam("kuerzel") @DefaultValue("") String kuerzel) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;


        String SQL = "SELECT n.ROWID, s.ROWID, e.ROWID,e.EMailAdresse, n.Password,s.Verfuegbarkeitsstatus,e.Kuerzel " +
                "FROM Nutzer n, Spezialist s , Entwickler e " +
                "WHERE  s.EMailAdresse=n.EMailAdresse AND e.EMailAdresse=s.EMailAdresse ";

        if (!kuerzel.isEmpty()) {
            SQL += "AND e.kuerzel= ? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, kuerzel);
        } else {
            SQL += ";";
            preparedStatement = connection.prepareStatement(SQL);
        }

        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("spezialistid", resultSet.getObject(2));
            entity.put("entwicklerid", resultSet.getObject(3));
            entity.put("email", resultSet.getObject(4));
            entity.put("passwort", resultSet.getObject(5));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(6));
            entity.put("kuerzel", resultSet.getObject(7));

            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("entwickler")
    @POST
    public Response addEntwickler(@FormDataParam("email") String email,
                                  @FormDataParam("passwort") String password,
                                  @FormDataParam("verfuegbarkeitsstatus") String verfuegbarkeitsstatus,
                                  @FormDataParam("kuerzel") String kuerzel,
                                  @FormDataParam("benennung") String benennung

    ) throws SQLException {

        Connection connection = dataSource.getConnection();


        try {
            // connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            // so that we can use connection.commit() later and save all the modifications made since the last commit.

            if (getNutzer(email).isEmpty()) {

                addnutzer(email, password, connection);
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Spezialist VALUES (?,?);");
            preparedStatement2.setObject(1, email);
            preparedStatement2.setObject(2, verfuegbarkeitsstatus);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();

            PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO Entwickler VALUES (?,?);");
            preparedStatement3.setObject(1, email);
            preparedStatement3.setObject(2, kuerzel);
            preparedStatement3.closeOnCompletion();
            preparedStatement3.executeUpdate();

            //wenn die Progrtammiersprache nicht vorhanden ist
            if (getProgrammierSprache(benennung).isEmpty()) {

                addProgrammierSprache(benennung, connection);
                connection.commit();
                int spracheID = Integer.parseInt(getProgrammierSprache(benennung).get(0).get("programmierspracheid").toString());
                addBeherrscht(kuerzel, connection, spracheID);

            } else {
                int spracheID = Integer.parseInt(getProgrammierSprache(benennung).get(0).get("programmierspracheid").toString());
                addBeherrscht(kuerzel, connection, spracheID);

            }
            connection.commit();

        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.

            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        } finally { // wird immer abgearbeitet
            connection.close();
        }

        return Response.status(Response.Status.CREATED)
                .header("Location", uriInfo.getAbsolutePath() +
                        "/" + getEntwickler(kuerzel).get(0).get("entwicklerid").toString()).build();
    }

    private void addProgrammierSprache(String benennung, Connection connection) throws SQLException {
        PreparedStatement preparedStatement4 = connection.prepareStatement("INSERT INTO Programmiersprache(Name,Erfahrungsstufe) VALUES (?,?);");
        preparedStatement4.setObject(1, benennung);
        preparedStatement4.setObject(2, 3);
        preparedStatement4.closeOnCompletion();
        preparedStatement4.executeUpdate();
    }

    private void addBeherrscht(String kuerzel, Connection connection, int lastProgrammierspracheId) throws SQLException {
        PreparedStatement preparedStatement5 = connection.prepareStatement("INSERT INTO beherrscht VALUES (?,?);");
        preparedStatement5.setObject(1, kuerzel);
        preparedStatement5.setObject(2, lastProgrammierspracheId);
        preparedStatement5.closeOnCompletion();
        preparedStatement5.executeUpdate();
    }


    @Path("programmiersprache")
    @GET
    public List<Map<String, Object>> getProgrammierSprache(@QueryParam("name") @DefaultValue("") String name) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;
        String SQL = "SELECT ProgrammierspracheID, Name, Erfahrungsstufe FROM Programmiersprache ";

        if (!name.isEmpty()) {
            SQL += " Where Name =? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, name);
        } else {
            SQL += ";";
            preparedStatement = connection.prepareStatement(SQL);
        }
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("programmierspracheid", resultSet.getObject(1));
            entity.put("name", resultSet.getObject(2));
            entity.put("erfahrungsstufe", resultSet.getObject(3));

            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }




    @Path("programmierer")
    @GET
    public Response getEntwickler() {
        return Response.status(Response.Status.MOVED_PERMANENTLY)
                .header("Location", "http://localhost:8080/entwickler").build();

    }




    @RolesAllowed("KUNDE")
    @Path("projekte")
    @POST
    public Response addProjekt(@FormDataParam("name") String name,
                               @FormDataParam("deadline") String deadline

    ) throws SQLException {

        Connection connection = dataSource.getConnection();

        //alexanAea@yahoo.fr
        try {
            // connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            // so that we can use connection.commit() later and save all the modifications made since the last commit.

            User user = (User) securityContext.getUserPrincipal();
            String telefonnummer = getKunden(user.getName(), "").get(0).get("telefon").toString();


            String projektLeiter = "mark69@gmail.com"; //immer dieselber und schon vorhanden in meinem Datenbank
            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Projekt(Projektdeadline, Projektname,Telefonnumer,EMailAdresse) VALUES (?,?,?,?);");
            preparedStatement2.setObject(1, deadline);
            preparedStatement2.setObject(2, name);
            preparedStatement2.setObject(3, telefonnummer);
            preparedStatement2.setObject(4, projektLeiter);

            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.

            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();

        } finally { // wird immer abgearbeitet
            connection.close();
        }


        return Response.status(Response.Status.CREATED)
                .header("Location", uriInfo.getAbsolutePath() +
                        "/" + getProjekteName(name).get(0).get("projektid")).build();
    }



    @GET
    public List<Map<String, Object>> getProjekteName(@QueryParam("name") @DefaultValue("") String name) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;
        String SQL = "SELECT ROWID, Projektname FROM Projekt ";

        if (!name.isEmpty()) {
            SQL += " Where Projekt.Projektname =? ;";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, name);
        } else {
            SQL += ";";
            preparedStatement = connection.prepareStatement(SQL);
        }
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("projektid", resultSet.getObject(1));

            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }







    @RolesAllowed("KUNDE")
    @Path("projekte/{projektid}/bewertungen")
    @POST
    public Response addBewertungen(@PathParam("projektid") Integer projektid,
                                   @FormDataParam("punktzahl") String punktzahl,
                                   @FormDataParam("text") @DefaultValue("") String text
    ) throws SQLException {

        Connection connection = dataSource.getConnection();


        try {
            // connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            // so that we can use connection.commit() later and save all the modifications made since the last commit.


            if (!id_exist("Projekt", projektid)) {

                HashMap<String, String> message = new HashMap<>();
                message.put(("message"), "NOT FOUND");
                return Response.status(Response.Status.NOT_FOUND).entity(message).build();

            }

            User user = (User) securityContext.getUserPrincipal();
            String telefonnummer = getKunden(user.getName(), "").get(0).get("telefon").toString();


            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Bewertung(Bepunktung, Telefonnumer, ProjektID) VALUES (?,?,?);");
            preparedStatement2.setObject(1, punktzahl);
            preparedStatement2.setObject(2, telefonnummer);
            preparedStatement2.setObject(3, projektid);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();
            connection.commit();


            if (!text.isEmpty()) {
                int bewertungId = getLastBewertungID(projektid);
                PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO Text(Inhalt,BewertungID) VALUES (?,?);");
                preparedStatement3.setObject(1, text);
                preparedStatement3.setObject(2, bewertungId);
                preparedStatement3.closeOnCompletion();
                preparedStatement3.executeUpdate();
                connection.commit();

            }


        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.

            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();

        } finally { // wird immer abgearbeitet
            connection.close();
        }

        return Response.status(Response.Status.NO_CONTENT).build();

    }

    public Integer getLastBewertungID(Integer projektid) throws SQLException {
        List<Map<String, Object>> prog = getBewertungwen(projektid);
        return Integer.parseInt(prog.get(prog.size() - 1).get("bewertungid").toString());
    }


    @RolesAllowed("KUNDE")
    @Path("bewertungen/{bewertungid}")
    @PATCH
    public Response aendertBewertungBewertung(@PathParam("bewertungid") Integer bewertungid,
                                              @FormDataParam("punktzahl") Integer punktzahl,
                                              @FormDataParam("text") @DefaultValue("") String text

    ) throws SQLException {

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);


            if (!id_exist("Bewertung", bewertungid)) {
                HashMap<String, String> message = new HashMap<>();
                message.put(("message"), "NOT FOUND");
                return Response.status(Response.Status.NOT_FOUND).entity(message).build();
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE Bewertung  set Bepunktung= ? where ROWID=? ;");
            preparedStatement2.setObject(1, punktzahl);
            preparedStatement2.setObject(2, bewertungid);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();

            if (!text.isEmpty()) {
                if (!bewertungId_exist(bewertungid)) {
                    PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO Text( Inhalt, BewertungID) Values (?,?) ;");
                    preparedStatement3.setObject(1, text);
                    preparedStatement3.setObject(2, bewertungid);
                    preparedStatement3.closeOnCompletion();
                    preparedStatement3.executeUpdate();
                    connection.commit();

                } else {

                    PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE Text  set Inhalt= ? where BewertungID=? ;");
                    preparedStatement3.setObject(1, text);
                    preparedStatement3.setObject(2, bewertungid);
                    preparedStatement3.closeOnCompletion();
                    preparedStatement3.executeUpdate();

                }
            }
            connection.commit();


        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.

            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();

        } finally { // wird immer abgearbeitet
            connection.close();
        }
        return Response.status(Response.Status.NO_CONTENT).build();

    }


    private boolean bewertungId_exist(Integer id) throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  BewertungID From Text WHERE BewertungID = ? ");
            preparedStatement.setObject(1, id);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String entry = resultSet.getString(1);// get id
                resultSet.close();
                if (entry != null) {//um sicher zu sein !
                    return true;
                }
            }
            resultSet.close();
            return false;
        } catch (SQLException e) {
            return false;
        }
    }


    @RolesAllowed("KUNDE")
    @Path("bewertungen/{bewertungid}")
    @DELETE
    public Response loeschtBewertung(@PathParam("bewertungid") Integer bewertungid

    ) throws SQLException {

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);

            //wenn projektid nicht existiert, können wir dann keine Aufgabe hinzufügen
            if (!id_exist("Bewertung", bewertungid)) {
                HashMap<String, String> message = new HashMap<>();
                message.put(("message"), "NOT FOUND");
                return Response.status(Response.Status.NOT_FOUND).entity(message).build();

            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("delete FROM Bewertung WHERE ROWID = ? ;");
            preparedStatement2.setObject(1, bewertungid);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), "NOT FOUND");
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();


        } finally { // wird immer abgearbeitet
            connection.close();
        }
        return Response.status(Response.Status.NO_CONTENT).build();

    }


    @RolesAllowed("PROJEKTLEITER")
    @Path("projekte/{projektid}/aufgaben")
    @POST
    public Response addAufgaben(@PathParam("projektid") Integer projektid,
                                @FormDataParam("deadline") String deadline,
                                @FormDataParam("beschreibung") String beschreibung,
                                @FormDataParam("status") String status,
                                @FormDataParam("prioritaet") String prioritaet
    ) throws SQLException {

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);

            //wenn projektid nicht existiert, können wir dann keine Aufgabe hinzufügen
            if (!id_exist("Projekt", projektid)) {

                HashMap<String, String> message = new HashMap<>();
                message.put(("message"), "NOT FOUND");
                return Response.status(Response.Status.NOT_FOUND).entity(message).build();

            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Aufgabe(Vermerk,Deadline, Status,Beschreibung, ProjektID) VALUES (?,?,?,?,?);");
            preparedStatement2.setObject(1, prioritaet);
            preparedStatement2.setObject(2, deadline);
            preparedStatement2.setObject(3, status);
            preparedStatement2.setObject(4, beschreibung);
            preparedStatement2.setObject(5, projektid);
            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.

            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();

        } finally { // wird immer abgearbeitet
            connection.close();
        }
        return Response.status(Response.Status.NO_CONTENT).build();

    }


    private boolean id_exist(String name, Integer id) throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  rowid FROM " + name + " WHERE rowid = ? ");
            preparedStatement.setObject(1, id);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String entry = resultSet.getString(1);// get rowid
                resultSet.close();
                if (entry != null) {//um sicher zu sein !
                    return true;
                }
            }
            resultSet.close();
            return false;
        } catch (SQLException e) {
            return false;
        }
    }


    @RolesAllowed("PROJEKTLEITER")
    @Path("projekte/{projektid}/spezialisten")
    @POST
    public Response addSpezialist(@PathParam("projektid") Integer projektid,
                                  @FormDataParam("spezialistid") Integer spezialistid
    ) throws SQLException {

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);

            //wenn projektid nicht existiert, können wir dann keine Aufgabe hinzufügen
            if (!id_exist("Projekt", projektid)) {

                HashMap<String, String> message = new HashMap<>();
                message.put(("message"), "NOT FOUND");
                return Response.status(Response.Status.NOT_FOUND).entity(message).build();

            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO arbeitet_an VALUES (?,?);");

            String email = getSpezialistenEmail(spezialistid).get(0).get("email").toString();
            preparedStatement2.setObject(1, email);
            preparedStatement2.setObject(2, projektid);


            preparedStatement2.closeOnCompletion();
            preparedStatement2.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            connection.rollback(); //all the modifications are reverted until the last commit.

            System.err.println(e.getMessage());
            HashMap<String, String> message = new HashMap<>();
            message.put(("message"), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();

        } finally { // wird immer abgearbeitet
            connection.close();
        }
        return Response.status(Response.Status.NO_CONTENT).build();

    }


    @Path("spezialistenemail")
    @GET
    public List<Map<String, Object>> getSpezialistenEmail(@QueryParam("id") Integer id) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement;

        String SQL = "SELECT  Spezialist.ROWID,EMailAdresse FROM Spezialist where " +
                "  Spezialist.ROWID = ?  ;";
        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setObject(1, id);

        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();

            entity.put("spezialistenId", resultSet.getObject(1));
            entity.put("email", resultSet.getObject(2));

            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

}

