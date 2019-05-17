package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import util.DB;
import util.Misc;

@Path("user")
public class User {

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    private String user_id;

    @GET
    @Path("get_all_users")
    //  api/user/get_all_users
    public String getAllUsers() throws ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=utf-8");
        return DB.toJson(DB.getInstance().getDataList("SELECT users.user_email, users.user_name, users.user_avatar FROM users"));
    }

    @GET
    @Path("get_user")
    //  api/user/get_user
    public String getUser() throws ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=utf-8");
        user_id = (String) request.getSession().getAttribute("user_id");
        if (user_id == null) {
            return null;
        }
        return DB.toJson(DB.getInstance().getDataList("SELECT users.user_email, users.user_name, users.user_avatar FROM users WHERE users.user_id = '" + user_id + "'"));
    }

    @POST
    @Path("edit_user")
    //  api/user/edit_user
    public void editUser(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        user_id = (String) request.getSession().getAttribute("user_id");
        String user_name = String.valueOf(formInput.getFirst("user_name"));
        String user_password = String.valueOf(formInput.getFirst("user_password"));
        String user_avatar = String.valueOf(formInput.getFirst("user_avatar"));

        if (user_id == null || user_name.equals("")) {
            return;
        }
        if (!user_password.equals("")) {
            DB.getInstance().setData("UPDATE  users SET  user_name = '" + user_name + "',  user_password = '"
                    + user_password + "', user_avatar = '" + user_avatar + "' WHERE  users.user_id = '" + user_id + "'");
            Misc.setSystemMessage(response, "message_good", "<b>Success</b><br>User edited");
            response.sendRedirect(Misc.referer(request) + "/collections.jsp");
            return;
        } else {
            DB.getInstance().setData("UPDATE  users SET  user_name = '" + user_name + "', user_avatar = '" + user_avatar + "' WHERE  users.user_id = '" + user_id + "'");
            Misc.setSystemMessage(response, "message_good", "<b>Success</b><br>User edited");
            response.sendRedirect(Misc.referer(request) + "/collections.jsp");
            return;
        }

    }

    @POST
    @Path("login")
    //  api/user/login
    public void login(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {

        String user_email = String.valueOf(formInput.getFirst("user_email"));
        String user_password = String.valueOf(formInput.getFirst("user_password"));
        System.out.println(" /user_email - " + user_email + " /user_password - " + user_password);

        if (user_email.equals("") || user_password.equals("")) {
            Misc.setSystemMessage(response, "message_bad", "<b>Unsuccess</b><br>Empty required field");
            response.sendRedirect(Misc.referer(request) + "/login.jsp");
            return;
        }

        Map<String, String> rs = DB.getInstance().getDataRoll("SELECT users.user_id, users.user_name FROM users WHERE users.user_email = '"
                + user_email + "' AND users.user_password = '" + user_password + "'");

        if (rs != null) {
            request.getSession().setAttribute("user_id", rs.get("user_id"));
            request.getSession().setAttribute("user_name", rs.get("user_name"));
            request.getSession().setAttribute("user_email", user_email);
            response.setHeader("user_id", rs.get("user_id"));

            response.sendRedirect(Misc.referer(request) + "/collections.jsp");
            return;
        } else {
            System.out.println("--|_O‿O_|--  wrong user or password");
            Misc.setSystemMessage(response, "message_bad", "<b>Unsuccess</b><br>wrong user or password");
            response.sendRedirect(Misc.referer(request) + "/login.jsp");
            return;
        }

    }

    @GET
    @Path("logout")
    //  api/user/logout
    public void logout() throws ClassNotFoundException, SQLException, IOException {
        System.out.println("--|_O‿O_|--  logout");

        request.getSession().invalidate();
        response.sendRedirect(Misc.referer(request) + "/login.jsp");
        return;
    }

    @POST
    @Path("register")
    //  api/user/register
    public void register(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
        String user_email = String.valueOf(formInput.getFirst("user_email"));
        String user_name = String.valueOf(formInput.getFirst("user_name"));
        String user_password = String.valueOf(formInput.getFirst("user_password"));
        String user_avatar = String.valueOf(formInput.getFirst("user_avatar"));
        System.out.println(" /user_email - " + user_email + " /user_name - " + user_name + " /user_password - " + user_password + " /user_avatar - " + user_avatar);

        if (user_email.equals("") || user_name.equals("") || user_password.equals("")) {
            Misc.setSystemMessage(response, "message_bad", "<b>Unsuccess</b><br>Empty required field");
            response.sendRedirect(Misc.referer(request) + "/register.jsp");
            return;
        }

        Map<String, String> emailCheck = DB.getInstance().getDataRoll("SELECT users.user_id FROM users WHERE users.user_email = '" + user_email + "'");
        if (emailCheck == null) {
            DB.getInstance().setData("INSERT INTO users (user_email, user_name, user_password, user_avatar) VALUES ('"
                    + user_email + "', '" + user_name + "', '" + user_password + "', '" + user_avatar + "')");

            Map<String, String> rs = DB.getInstance().getDataRoll("SELECT users.user_id FROM users WHERE users.user_email = '" + user_email + "'");
            if (rs != null) {
                request.getSession().setAttribute("user_id", rs.get("user_id"));
                request.getSession().setAttribute("user_name", user_name);
                request.getSession().setAttribute("user_email", user_email);
                
                response.sendRedirect(Misc.referer(request) + "/collections.jsp");
                return;
            }

        } else {
            Misc.setSystemMessage(response, "message_bad", "<b>Unsuccess</b><br>This user already exist");
            response.sendRedirect(Misc.referer(request) + "/register.jsp");
            return;
        }

    }

}
