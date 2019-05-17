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

public class DB {

    private static String connPath = "jdbc:mysql://178.128.254.179:3306/vinyl_shelf?zeroDateTimeBehavior=convertToNull";
    private static String dbUser = "vinyl";
    private static String dbPassword = "vinyl";

    private static int connected;
    private static DB instance;

    public static DB getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            Class.forName("com.mysql.jdbc.Driver");
//          Class.forName("com.mysql.cj.jdbc.Driver");
            instance = new DB();
            System.out.println("-- Macke instance of DB getInstance");
        }
        return instance;
    }

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
                    row.put(String.valueOf(md.getColumnLabel(i)), String.valueOf(rs.getObject(i)));
                }
                list.add(row);
            }
            ///*get the active connections ------------*/
            rs = st.executeQuery("SHOW STATUS WHERE `variable_name` = 'Threads_connected'");
            if (rs.next()) {
                connected = Integer.parseInt(rs.getString("Value"));
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
                    row.put(String.valueOf(md.getColumnLabel(i)), String.valueOf(rs.getObject(i)));
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
                    list.add(String.valueOf(rs.getObject(i)));
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

}
