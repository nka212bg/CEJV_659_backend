package rest;

import java.io.IOException;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedMap;
import util.DB;
import util.SessionLinker;

@Path("vinyls")
@Stateless
public class Vinyls {

    private SessionLinker session = SessionLinker.getInstance();

    @POST
    @Path("add_vinyl")
    public void addVinyl(MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {

        String collection_id = DB.validateString(String.valueOf(formInput.getFirst("collection_id")));
        String vinyl_artist_name = DB.validateString(String.valueOf(formInput.getFirst("vinyl_artist_name")));
        String vinyl_album_name = DB.validateString(String.valueOf(formInput.getFirst("vinyl_album_name")));
        String vinyl_album_cover = DB.validateString(String.valueOf(formInput.getFirst("vinyl_album_cover")));
        String vinyl_year = DB.validateString(String.valueOf(formInput.getFirst("vinyl_year")));
        String vinyl_condition = DB.validateString(String.valueOf(formInput.getFirst("vinyl_condition")));
        String vinyl_note = DB.validateString(String.valueOf(formInput.getFirst("vinyl_note")));

        DB.getInstance().setData("INSERT INTO vinyls(collection_id, vinyl_artist_name, vinyl_album_name, vinyl_album_cover, vinyl_year, vinyl_condition, vinyl_note ) VALUES( '" + collection_id + "', '" + vinyl_artist_name + "', '" + vinyl_album_name + "', '" + vinyl_album_cover + "', '" + vinyl_year + "', '" + vinyl_condition + "', '" + vinyl_note + "')");

    }

    @POST
    @Path("get_vinyl/{vinyl_id}")
    public String getVinyl(@PathParam("vinyl_id") String vinyl_id) throws ClassNotFoundException, SQLException {

        vinyl_id = DB.validateString(vinyl_id);
        return DB.toJson(DB.getInstance().getDataRoll("SELECT * FROM vinyls WHERE vinyl_id = '" + vinyl_id + "'"));
    }

    @POST
    @Path("get_all_vinyls/{collection_id}")
    public String getAllVinyls(@PathParam("collection_id") String collection_id) throws ClassNotFoundException, SQLException {

        collection_id = DB.validateString(collection_id);
        return DB.toJson(DB.getInstance().getDataList("SELECT * FROM vinyls WHERE vinyls.collection_id = '" + collection_id + "'"));
    }

    @POST
    @Path("edit_vinyl/{vinyl_id}")
    public void editVinyl(@PathParam("vinyl_id") String vinyl_id, MultivaluedMap<String, String> formInput) throws ClassNotFoundException, SQLException, IOException {

        vinyl_id = DB.validateString(vinyl_id);

        String vinyl_artist_name = DB.validateString(String.valueOf(formInput.getFirst("vinyl_artist_name")));
        String vinyl_album_name = DB.validateString(String.valueOf(formInput.getFirst("vinyl_album_name")));
        String vinyl_album_cover = DB.validateString(String.valueOf(formInput.getFirst("vinyl_album_cover")));
        String vinyl_year = DB.validateString(String.valueOf(formInput.getFirst("vinyl_year")));
        String vinyl_condition = DB.validateString(String.valueOf(formInput.getFirst("vinyl_condition")));
        String vinyl_note = DB.validateString(String.valueOf(formInput.getFirst("vinyl_note")));

        DB.getInstance().setData("UPDATE vinyls SET vinyl_artist_name = '" + vinyl_artist_name + "', vinyl_album_name = '" + vinyl_album_name + "', vinyl_album_cover = '" + vinyl_album_cover + "', vinyl_year = '" + vinyl_year + "', vinyl_condition = '" + vinyl_condition + "', vinyl_note = '" + vinyl_note + "' WHERE vinyls.vinyl_id = '" + vinyl_id + "'");

    }

    @POST
    @Path("delete/{vinyl_id}")
    public void deletCollection(@PathParam("vinyl_id") String vinyl_id) throws ClassNotFoundException, SQLException, IOException {

        vinyl_id = DB.validateString(vinyl_id);

        DB.getInstance().setData("DELETE FROM vinyls WHERE vinyls.vinyl_id ='" + vinyl_id + "'");

    }

}
