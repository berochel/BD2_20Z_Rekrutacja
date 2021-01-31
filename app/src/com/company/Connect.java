package com.company;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Connect {

    private static String connect_Path = "jdbc:sqlite:app/src/com/company/rekrutacja.db";

    public static List<Map<String, Object>> connect(String query) {
        Connection conn = null;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;

        try {
            // db parameters

            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement statement  = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (resultSet.next()) {
                row = new HashMap<String, Object>();
                for (int i = 1; i <= columnsNumber; i++) {
                    row.put(rsmd.getColumnName(i), resultSet.getObject(i));
                }
                resultList.add(row);
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return resultList;
    }

    public static int connect_query_int(String query, String column) {
        Connection conn = null;
        int index = 0;
        try {
            // db parameters
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                index = rs.getInt(column);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return index;
    }
    public static String connect_query_string(String query, String column) {
        Connection conn = null;
        String index = "";
        try {
            // db parameters
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                index = rs.getString(column);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return index;
    }
    public static List<String> select_faculties(){
        Connection conn = null;
        List<String> faculties = new ArrayList<String>();
        try {
            // db parameters
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT nazwa FROM wydzial");

            while (rs.next()) {
                faculties.add(rs.getString("nazwa"));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return faculties;
    }

    public static List<String> select_major(String faculty){
        Connection conn = null;
        List<String> majors = new ArrayList<String>();
        try {
            // db parameters
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT kierunek_studiow.nazwa FROM kierunek_studiow, wydzial WHERE kierunek_studiow.id_wydzialu = wydzial.id_wydzialu AND wydzial.nazwa = '" + faculty + "'");

            while (rs.next()) {
                majors.add(rs.getString("nazwa"));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return majors;
    }

    public static List<String> select_countries(){
        Connection conn = null;
        List<String> countries = new ArrayList<String>();
        try {
            // db parameters
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT nazwa FROM kraj");
            while (rs.next()) {
                countries.add(rs.getString("nazwa"));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return countries;
    }
    public static List<String> select_rekrutacja(){
        Connection conn = null;
        List<String> rekrutacje = new ArrayList<String>();
        try {
            // db parameters
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT id_rekrutacji FROM rekrutacja");
            while (rs.next()) {
                rekrutacje.add("Rekrutacja "+rs.getString("id_rekrutacji")+".");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return rekrutacje;
    }
    public static int count_table(String table_name){
        Connection conn = null;
        int sum = 0;
        try {
            // db parameters
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT COUNT(*) as sum FROM " + table_name);
            while (rs.next()) {
                sum += rs.getInt("sum");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return sum;
    }

    public static void insert(String query){
        Connection conn = null;
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(query);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("not connected");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}