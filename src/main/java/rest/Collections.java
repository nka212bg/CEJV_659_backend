package rest;

import java.io.IOException;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import util.DB;
import util.SessionLinker;

@Path("collections")
@Stateless
public class Collections {

//    @Context
//    private HttpServletResponse response;
    private SessionLinker session = SessionLinker.getInstance();

    @POST
    @Path("add_collection/{session_id}")
    public String addCollection(@PathParam("session_id") String session_id, MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_id = session.getAttribute(session_id, "user_id");

        String collection_name = DB.validateString(String.valueOf(formInput.getFirst("collection_name")));
        String collection_genre = DB.validateString(String.valueOf(formInput.getFirst("collection_genre")));
        String collection_cover = DB.validateString(String.valueOf(formInput.getFirst("collection_cover")));
        String collection_note = DB.validateString(String.valueOf(formInput.getFirst("collection_note")));

        if (user_id == null) {
            return "{\"status\":false,\"message\":\"no user\"}";
        }

        DB.getInstance().setData("INSERT INTO collections ( user_id, collection_name, collection_genre, collection_cover, collection_note ) VALUES( '" + user_id + "', '" + collection_name + "', '" + collection_genre + "', '" + collection_cover + "', '" + collection_note + "' )");
        return "{\"status\":true,\"message\":\"collection added\"}";

    }

    @POST
    @Path("get_collection/{session_id}/{collection_id}")
    public String getCollection(@PathParam("session_id") String session_id, @PathParam("collection_id") String collection_id) throws ClassNotFoundException, SQLException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_id = session.getAttribute(session_id, "user_id");
        collection_id = DB.validateString(collection_id);

        if (user_id == null) {
            return "{\"status\":false,\"message\":\"no user\"}";
        }
        return DB.toJson(DB.getInstance().getDataRoll("SELECT collection_name, collection_genre, collection_cover, collection_note FROM collections WHERE collections.collection_id = '" + collection_id + "' AND collections.user_id ='" + user_id + "'"));
    }

    @POST
    @Path("get_all_collections/{session_id}")
    public String getAllCollections(@PathParam("session_id") String session_id) throws ClassNotFoundException, SQLException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_id = session.getAttribute(session_id, "user_id");

        if (user_id == null) {
            return "{\"status\":false,\"message\":\"no user\"}";
        }
        return DB.toJson(DB.getInstance().getDataList("SELECT collection_name, collection_id, collection_genre, collection_cover, collection_note FROM collections WHERE collections.user_id ='" + user_id + "'"));
    }

    @POST
    @Path("edit_collection/{session_id}/{collection_id}")
    public String editCollection(@PathParam("session_id") String session_id, @PathParam("collection_id") String collection_id, MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_id = session.getAttribute(session_id, "user_id");
        collection_id = DB.validateString(collection_id);

        String collection_name = DB.validateString(String.valueOf(formInput.getFirst("collection_name")));
        String collection_genre = DB.validateString(String.valueOf(formInput.getFirst("collection_genre")));
        String collection_cover = DB.validateString(String.valueOf(formInput.getFirst("collection_cover")));
        String collection_note = DB.validateString(String.valueOf(formInput.getFirst("collection_note")));

        if (user_id == null) {
            return "{\"status\":false,\"message\":\"no user\"}";
        }

        DB.getInstance().setData("UPDATE collections SET collection_name = '" + collection_name + "', collection_genre = '" + collection_genre + "', collection_cover = '" + collection_cover + "', collection_note = '" + collection_note + "' WHERE collections.collection_id = '" + collection_id + "' AND collections.user_id ='" + user_id + "'");
        return "{\"status\":true,\"message\":\"collection edited\"}";

    }

    @POST
    @Path("delete/{session_id}/{collection_id}")
    public String deletCollection(@PathParam("session_id") String session_id, @PathParam("collection_id") String collection_id) throws ClassNotFoundException, SQLException, IOException {
//      response.setHeader("Access-Control-Allow-Origin", "*");

        String user_id = session.getAttribute(session_id, "user_id");
        collection_id = DB.validateString(collection_id);

        if (user_id == null) {
            return "{\"status\":false,\"message\":\"no user\"}";
        }

        DB.getInstance().setData("DELETE FROM collections WHERE collections.collection_id = '" + collection_id + "' AND collections.user_id ='" + user_id + "'");
        return "{\"status\":true,\"message\":\"collection deleted\"}";

    }

}
