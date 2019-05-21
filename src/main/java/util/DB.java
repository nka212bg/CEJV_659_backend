package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 * @author Nikolay Krasimirov Atanasov
 */
public class DB {

    private static String connPath = "jdbc:mysql://178.128.254.179:3306/vinyl_shelf?zeroDateTimeBehavior=convertToNull";
    private static String dbUser = "vinyl";
    private static String dbPassword = "vinyl";

    private static int connected;
    private static DB instance;

    /**
     * @return Singleton - getting instance of the class
     */
    public static DB getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            Class.forName("com.mysql.jdbc.Driver");
            instance = new DB();
            System.out.println("-- Macke instance of DB getInstance");
        }
        return instance;
    }

    /**
     * @param query
     * @return List of maps from the DB
     */
    public List<Map> getDataList(String query) throws SQLException, ClassNotFoundException {
        try {
            Connection conn = DriverManager.getConnection(connPath, dbUser, dbPassword);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            List<Map> list = new ArrayList();
            while (rs.next()) {
                Map<String, String> row = new HashMap();
                for (int i = 1; i <= columns; ++i) {
                    row.put(String.valueOf(md.getColumnLabel(i)), decodeString(String.valueOf(rs.getObject(i))));
                }
                list.add(row);
            }
            ///*get the active connections [no need for now]------------*/
            // rs = st.executeQuery("SHOW STATUS WHERE `variable_name` = 'Threads_connected'");
            // if (rs.next()) {
            // connected = Integer.parseInt(rs.getString("Value"));
            // }
            conn.close();
            st.close();
            rs.close();
            return list;
        } catch (SQLException e) {
            System.out.println("Error from DB.getDataList: " + e);
        }
        return null;
    }

    /**
     * @param query
     * @return Map from the DB (Use for single row data)
     */
    public Map<String, String> getDataRoll(String query) throws SQLException, ClassNotFoundException {
        try {
            Connection conn = DriverManager.getConnection(connPath, dbUser, dbPassword);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            Map<String, String> row = new HashMap();
            if (rs.next()) {
                for (int i = 1; i <= columns; ++i) {
                    row.put(String.valueOf(md.getColumnLabel(i)), decodeString(String.valueOf(rs.getObject(i))));
                }
                conn.close();
                st.close();
                rs.close();
                return row;
            }
        } catch (SQLException e) {
            System.out.println("Error from DB.getDataRoll: " + e);
        }
        return null;
    }

    /**
     * @param query
     * @return Array list from the DB (Use for single row data (only values without keys))
     */
    public List<String> getDataArray(String query) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(connPath, dbUser, dbPassword);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            List<String> list = new ArrayList();
            while (rs.next()) {
                for (int i = 1; i <= columns; ++i) {
                    list.add(decodeString(String.valueOf(rs.getObject(i))));
                }
            }
            conn.close();
            st.close();
            rs.close();
            return list;
        } catch (SQLException e) {
            System.out.println("Error from DB.getDataList: " + e);
        }
        return null;
    }

    /**
     * @param query
     * Set data to the database
     */
    public void setData(String query) throws SQLException, ClassNotFoundException {
        try {
            Connection conn = DriverManager.getConnection(connPath, dbUser, dbPassword);

            PreparedStatement p = conn.prepareStatement(query);
            p.executeUpdate();
            conn.close();
            p.close();
            System.out.println("-- Threads_connected: " + connected);
            return;
        } catch (SQLException e) {
            System.out.println("Error from DB.setData: " + e);
        }
    }

    /**
     * @param list
     * @return Converts the data from a query to JS array
     */
    public static String toArray(List<String> list) {
        String a = "[";
        for (int i = 0; i < list.size(); i++) {
            a += "\"" + list.get(i) + "\"";
            if (i != list.size() - 1) {
                a += ",";
            }
        }
        return a + "]";
    }

    /**
     * @param list
     * @return Converts the data from a query to array of JSONs [{},{}]
     */
    public static String toJson(List<Map> list) {
        String a = "[";
        for (int i = 0; i < list.size(); i++) {
            a += "{";
            Iterator<Map<String, String>> mapVal = list.get(i).entrySet().iterator();
            while (mapVal.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) mapVal.next();
                a += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"";
                if (mapVal.hasNext()) {
                    a += ",";
                }
            }
            if (i != list.size() - 1) {
                a += "},";
            }
        }
        return a + "}]";
    }

    /**
     * @param map
     * @return Converts the data from a query to JSON (Use for single row data)
     */
    public static String toJson(Map<String, String> map) {
        String a = "{";
        Iterator<Map.Entry<String, String>> mapVal = map.entrySet().iterator();
        while (mapVal.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) mapVal.next();
            a += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"";
            if (mapVal.hasNext()) {
                a += ",";
            }
        }
        return a + "}";
    }

    /**
     * @param string
     * @return Sanitize the user input before pass to the query
     */
    public static String validateString(String string) {
        if (string != null) {
            return trim(string)
                    .replace("\"", "&#34;")
                    .replace("'", "&#39;")
                    .replace("\\", "&#92;")
                    .replace("`", "&#96;")
                    .replace("´", "&#180;")
                    .replace("\n", " ")
                    .replace("\r", " ");
        }
        return null;
    }

    /**
     *
     * @param string
     * @return Decode the sanitized data from the database
     */
    public static String decodeString(String string) {
        if (string != null) {
            return trim(string)
                    .replace("&#34;", "\\\"")
                    .replace("&#39;", "'")
                    .replace("&#92;", "\\\\")
                    .replace("&#96;", "`")
                    .replace("&#180;", "´");
        }
        return null;
    }
}
