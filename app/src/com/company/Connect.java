package com.company;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class Connect {

    //private static String connect_Path = "jdbc:sqlite:C:\\Users\\Jaroslaw\\IBM\\rationalsdp\\workspace\\BD2_20Z_Rekrutacja\\app\\src\\com\\company\\test.db";
    private static String connect_Path = "jdbc:sqlite:app/src/com/company/test.db";

    public static String connect(String query) {
        Connection conn = null;
        String text = "";
        try {
            // db parameters

            String url = connect_Path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(query);

            /*
            while (rs.next()) {
                text += (rs.getInt("Id_miasta") + " " + rs.getString("Nazwa") + "\n");
            } */


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
        return text;
    }

    public static int connect_query(String query, String column) {
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

    public static String select_from(String table_name, String values){
        String query = "SELECT " + values + " FROM " + table_name;
        return query;
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

    public static String insert_values(String table_name, List<String> values){
        String query = "INSERT INTO " + table_name + " VALUES" + values;
        if(table_name.compareTo("adres") == 0){
            query = insert_values_adres(query, values);
        } else if(table_name.compareTo("aplikacja") == 0){
            query = insert_values_aplikacja(query, values);
        } else if(table_name.compareTo("chec_uczestnictwa") == 0){
            query = insert_values_chec_uczestnictwa(query, values);
        } else if(table_name.compareTo("kandydat") == 0){
            query = insert_values_kandydat(query, values);
        } else if(table_name.compareTo("kierunek_studiow") == 0){
            query = insert_values_kierunek_studiow(query, values);
        } else if(table_name.compareTo("konkurs") == 0){
            query = insert_values_konkurs(query, values);
        } else if(table_name.compareTo("kraj") == 0){
            query = insert_values_kraj(query, values);
        } else if(table_name.compareTo("lista") == 0){
            query = insert_values_lista(query, values);
        } else if(table_name.compareTo("lista_akceptowanych_konkursow") == 0){
            query = insert_values_lista_akceptowanych_konkursow(query, values);
        } else if(table_name.compareTo("lista_kandydat") == 0){
            query = insert_values_lista_kandydat(query, values);
        } else if(table_name.compareTo("lista_laureatow") == 0){
            query = insert_values_lista_laureatow(query, values);
        } else if(table_name.compareTo("lista_wyborow") == 0){
            query = insert_values_lista_wyborow(query, values);
        } else if(table_name.compareTo("lista_wydzialow") == 0){
            query = insert_values_lista_wydzialow(query, values);
        } else if(table_name.compareTo("miasto") == 0){
            query = insert_values_miasto(query, values);
        } else if(table_name.compareTo("pracownik") == 0){
            query = insert_values_pracownik(query, values);
        } else if(table_name.compareTo("przedmiot") == 0){
            query = insert_values_przedmiot(query, values);
        } else if(table_name.compareTo("przelicznik_punktow") == 0){
            query = insert_values_przelicznik_punktow(query, values);
        } else if(table_name.compareTo("realizacja") == 0){
            query = insert_values_realizacja(query, values);
        } else if(table_name.compareTo("rekrutacja") == 0){
            query = insert_values_rekrutacja(query, values);
        } else if(table_name.compareTo("tura") == 0){
            query = insert_values_tura(query, values);
        } else if(table_name.compareTo("wydzial") == 0){
            query = insert_values_wydzial(query, values);
        } else if(table_name.compareTo("wynik_z_przedmiotu") == 0){
            query = insert_values_wynik_z_przedmiotu(query, values);
        } else {
            return "";
        }
        return query;
    }

    private static String insert_values_adres(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_aplikacja(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_chec_uczestnictwa(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_kandydat(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_kierunek_studiow(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_konkurs(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_kraj(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_lista(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_lista_akceptowanych_konkursow(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_lista_kandydat(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_lista_laureatow(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_lista_wyborow(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_lista_wydzialow(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_miasto(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_pracownik(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_przedmiot(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_przelicznik_punktow(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_realizacja(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_rekrutacja(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_tura(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_wydzial(String str, List<String> values){
        //
        return str;
    }
    private static String insert_values_wynik_z_przedmiotu(String str, List<String> values){
        //
        return str;
    }


}