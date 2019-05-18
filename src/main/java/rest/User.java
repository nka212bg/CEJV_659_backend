package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.ejb.Stateless;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import util.DB;
import util.Misc;
import util.SessionLinker;

@Path("user")
@Stateless
public class User {

//    @Context
//    private HttpServletRequest request;
//
    @Context
    private HttpServletResponse response;
    private String user_id;
    private SessionLinker session = SessionLinker.getInstance();

    @POST
    @Path("get_all_users")
    public String getAllUsers() throws ClassNotFoundException, SQLException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return DB.toJson(DB.getInstance().getDataList("SELECT users.user_email, users.user_name, users.user_avatar FROM users"));
    }

    @POST
    @Path("get_user/{session_id}")
    public String getUser(@PathParam("session_id") int session_id) throws ClassNotFoundException, SQLException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String user_id = session.getAttribute(session_id, "user_id");
        if (user_id == null) {
            return "{\"0\":\"empty required field\"}";
        }
        return DB.toJson(DB.getInstance().getDataList("SELECT users.user_email, users.user_name, users.user_avatar FROM users WHERE users.user_id = '" + user_id + "'"));
    }

    @POST
    @Path("edit_user/{session_id}")
    public String editUser(@PathParam("session_id") int session_id, MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
        String user_id = session.getAttribute(session_id, "user_id");
        String user_name = String.valueOf(formInput.getFirst("user_name"));
        String user_password = String.valueOf(formInput.getFirst("user_password"));
        String user_avatar = String.valueOf(formInput.getFirst("user_avatar"));

        if (user_id == null) {
            return "{\"0\":\"login problem\"}";
        }
        if (!user_password.equals("")) {
            DB.getInstance().setData("UPDATE  users SET  user_name = '" + user_name + "',  user_password = '"
                    + user_password + "', user_avatar = '" + user_avatar + "' WHERE  users.user_id = '" + user_id + "'");
            return "{\"1\":\"User edited\"}";
        } else {
            DB.getInstance().setData("UPDATE  users SET  user_name = '" + user_name + "', user_avatar = '" + user_avatar + "' WHERE  users.user_id = '" + user_id + "'");
            return "{\"1\":\"User edited\"}";
        }
    }

    @POST
    @Path("login")
    public String login(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String user_email = String.valueOf(formInput.getFirst("user_email"));
        String user_password = String.valueOf(formInput.getFirst("user_password"));
        System.out.println(" /user_email - " + user_email + " /user_password - " + user_password);

        if (user_email.equals("") || user_password.equals("")) {
            return "{\"0\":\"empty required field\"}";
        }

        Map<String, String> rs = DB.getInstance().getDataRoll("SELECT users.user_id, users.user_name FROM users WHERE users.user_email = '"
                + user_email + "' AND users.user_password = '" + user_password + "'");

        if (rs != null) {
            int session_id = SessionLinker.getInstance().setSession();
            session.setAttribute(session_id, "user_id", rs.get("user_id"));
            return "{\"SessionLinker\":\"" + session_id + "\"}";
        } else {
            return "{\"0\":\"wrong user or email\"}";
        }

    }

    @GET
    @Path("logout/{session_id}")
    public void logout(@PathParam("session_id") String session_id) throws ClassNotFoundException, SQLException, IOException {
        session.invalidate(Integer.valueOf(session_id));
        return;
    }

    @POST
    @Path("register")
    public String register(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String user_email = String.valueOf(formInput.getFirst("user_email"));
        String user_name = String.valueOf(formInput.getFirst("user_name"));
        String user_password = String.valueOf(formInput.getFirst("user_password"));
        String user_avatar = String.valueOf(formInput.getFirst("user_avatar"));
        System.out.println(" /user_email - " + user_email + " /user_name - " + user_name + " /user_password - " + user_password + " /user_avatar - " + user_avatar);

        if (user_email.equals("") || user_name.equals("") || user_password.equals("")) {
            return "{\"0\":\"empty required field\"}";
        }

        Map<String, String> emailCheck = DB.getInstance().getDataRoll("SELECT users.user_id FROM users WHERE users.user_email = '" + user_email + "'");
        if (emailCheck == null) {
            DB.getInstance().setData("INSERT INTO users (user_email, user_name, user_password, user_avatar) VALUES ('"
                    + user_email + "', '" + user_name + "', '" + user_password + "', '" + user_avatar + "')");

            Map<String, String> rs = DB.getInstance().getDataRoll("SELECT users.user_id FROM users WHERE users.user_email = '" + user_email + "'");
            if (rs != null) {
                int session_id = session.setSession();
                session.setAttribute(session_id, "user_id", rs.get("user_id"));
                return "{\"SessionLinker\":\"" + session_id + "\"}";
            }

        } else {
            return "{\"0\":\"this user already exists\"}";
        }
        return null;
    }

}
