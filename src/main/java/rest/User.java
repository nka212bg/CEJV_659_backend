package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import util.DB;
import util.SessionLinker;

@Path("user")
@Stateless
public class User {

//    @Context
//    private HttpServletResponse response;
    private SessionLinker session = SessionLinker.getInstance();

    @POST
    @Path("get_user/{session_id}")
    public String getUser(@PathParam("session_id") String session_id) throws ClassNotFoundException, SQLException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_id = session.getAttribute(session_id, "user_id");

        if (user_id == null) {
            return null;
        }
        return DB.toJson(DB.getInstance().getDataRoll("SELECT users.user_name, users.user_avatar FROM users WHERE users.user_id = '" + user_id + "'"));
    }

    @POST
    @Path("edit_user/{session_id}")
    public String editUser(@PathParam("session_id") String session_id, MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {

        String user_id = session.getAttribute(session_id, "user_id");

        String user_name = DB.validateString(String.valueOf(formInput.getFirst("user_name")));
        String user_password = DB.validateString(String.valueOf(formInput.getFirst("user_password")));
        String user_avatar = DB.validateString(String.valueOf(formInput.getFirst("user_avatar")));

        if (user_id == null) {
            return "{\"status\":false,\"message\":\"no user\"}";
        }
        if (!user_password.equals("")) {
            DB.getInstance().setData("UPDATE  users SET  user_name = '" + user_name + "',  user_password = '"
                    + user_password + "', user_avatar = '" + user_avatar + "' WHERE  users.user_id = '" + user_id + "'");
            return "{\"status\":true,\"message\":\"user edited\"}";
        } else {
            DB.getInstance().setData("UPDATE  users SET  user_name = '" + user_name + "', user_avatar = '" + user_avatar + "' WHERE  users.user_id = '" + user_id + "'");
            return "{\"status\":true,\"message\":\"user edited\"}";
        }
    }

    @POST
    @Path("delete/{session_id}")
    public String deleteUser(@PathParam("session_id") String session_id, MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {

        String user_id = session.getAttribute(session_id, "user_id");

        if (user_id == null) {
            return "{\"status\":false,\"message\":\"no user\"}";
        }

        DB.getInstance().setData("DELETE FROM vinyls WHERE vinyls.collection_id = (SELECT collection_id FROM collections WHERE collections.user_id = '" + user_id + "')");
        DB.getInstance().setData("DELETE FROM collections WHERE collections.user_id = '" + user_id + "'");
        DB.getInstance().setData("DELETE FROM users WHERE users.user_id = '" + user_id + "'");

        return "{\"status\":true,\"message\":\"user deleted\"}";

    }

    @POST
    @Path("login")
    public String login(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_email = DB.validateString(String.valueOf(formInput.getFirst("user_email")));
        String user_password = DB.validateString(String.valueOf(formInput.getFirst("user_password")));

        System.out.println(" /user_email - " + user_email + " /user_password - " + user_password);

        if (user_email.equals("") || user_password.equals("")) {
            return "{\"status\":false,\"message\":\"empty required field\"}";
        }

        Map<String, String> rs = DB.getInstance().getDataRoll("SELECT users.user_id, users.user_name, users.user_avatar FROM users WHERE users.user_email = '"
                + user_email + "' AND users.user_password = '" + user_password + "'");

        if (rs != null) {
            String session_id = session.setSession();
            session.setAttribute(session_id, "user_id", rs.get("user_id"));

            System.out.println("--|_O‿O_|--  " + session.getSessionAll());

            return "{\"SessionLinker\":\"" + session_id + "\",\"user_name\":\"" + rs.get("user_name") + "\",\"user_avatar\":\"" + rs.get("user_avatar") + "\"}";
        } else {
            System.out.println("--|_O‿O_|--  " + session.getSessionAll());
            return "{\"status\":false,\"message\":\"wrong user or password\"}";
        }

    }

    @POST
    @Path("register")
    public String register(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_email = DB.validateString(String.valueOf(formInput.getFirst("user_email")));
        String user_name = DB.validateString(String.valueOf(formInput.getFirst("user_name")));
        String user_password = DB.validateString(String.valueOf(formInput.getFirst("user_password")));
        String user_avatar = DB.validateString(String.valueOf(formInput.getFirst("user_avatar")));

        System.out.println(" /user_email - " + user_email + " /user_name - " + user_name + " /user_password - " + user_password + " /user_avatar - " + user_avatar);

        if (user_email.equals("") || user_name.equals("") || user_password.equals("")) {
            return "{\"status\":false,\"message\":\"empty required field\"}";
        }

        Map<String, String> emailCheck = DB.getInstance().getDataRoll("SELECT users.user_id FROM users WHERE users.user_email = '" + user_email + "'");
        if (emailCheck == null) {
            DB.getInstance().setData("INSERT INTO users (user_email, user_name, user_password, user_avatar) VALUES ('"
                    + user_email + "', '" + user_name + "', '" + user_password + "', '" + user_avatar + "')");

            Map<String, String> rs = DB.getInstance().getDataRoll("SELECT users.user_id FROM users WHERE users.user_email = '" + user_email + "'");
            if (rs != null) {
                String session_id = session.setSession();
                session.setAttribute(session_id, "user_id", rs.get("user_id"));
                return "{\"SessionLinker\":\"" + session_id + "\",\"user_name\":\"" + user_name + "\",\"user_avatar\":\"" + user_avatar + "\"}";
            }

        } else {
            return "{\"status\":false,\"message\":\"this user already exists\"}";
        }
        return null;
    }

}
