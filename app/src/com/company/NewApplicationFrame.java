package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NewApplicationFrame extends JFrame{
    private JPanel new_application;
    private JTextField imieText;
    private JTextField nazwiskoText;
    private JTextField peselText;
    private JTextField miastoText;
    private JTextField kod_pocztowyText;
    private JTextField ulicaText;
    private JTextField numer_domuText;
    private JTextField numer_mieszkaniaText;
    private JTextField hasloText;
    private JButton stwórzAplikacjęNaStudiaButton;
    private JComboBox comboBox1;
    private JTextField data_urodzeniaTextField;
    private JButton powrótDoMenuButton;

    public NewApplicationFrame(String str){
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new_application);
        this.pack();

        List<String> countries = Connect.select_countries();
        comboBox1.setModel(new DefaultComboBoxModel<String>(countries.toArray(new String[0])));

        stwórzAplikacjęNaStudiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String imie = imieText.getText().strip();
                String nazwisko = nazwiskoText.getText().strip();
                String pesel =  peselText.getText().strip();
                String data_urodzenia = data_urodzeniaTextField.getText().strip();
                String haslo = hasloText.getText();
                String miasto = miastoText.getText().strip();
                String kod_pocztowy = kod_pocztowyText.getText().strip();
                String ulica = ulicaText.getText().strip();
                int numer_domu = Integer.parseInt(numer_domuText.getText().strip());
                int numer_mieszkania = 0;
                if(!numer_mieszkaniaText.getText().isEmpty()){
                    numer_mieszkania = Integer.parseInt(numer_mieszkaniaText.getText().strip());
                }
                String kraj = comboBox1.getSelectedItem().toString();
                int idmiasto = addMiasto(miasto, kraj);
                int idadres = addAdres(ulica, idmiasto, kod_pocztowy, numer_domu, numer_mieszkania);
                int idkandydat = addKandydat(imie, nazwisko, pesel, haslo, data_urodzenia, idadres);
                addAplikacja(idkandydat);
            }
        });
        powrótDoMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new MainFrame("Rekrutacja BD2");
                setVisible(false);
                frame.setVisible(true);
                dispose();
            }
        });
    }

    private int addAdres(String ulica, int miasto, String kod_pocztowy, int numer_domu, int numer_mieszkania){
        int id = 0;
        String query = "SELECT id_adresu as id FROM adres WHERE id_miasta = " + miasto + " AND ulica = '" + ulica + "' AND kod_pocztowy = '"
                + kod_pocztowy + "' AND nr_domu = " + numer_domu;
        if(numer_mieszkania != 0){
            query += " AND nr_mieszkania = " + numer_mieszkania;
        } else {
            query += " AND nr_mieszkania is null";
        }
        id = Connect.connect_query(query, "id");
        if(id != 0){
            System.out.print("Address found");
            return id;
        } else {
            System.out.print("Address not found");
            id = Connect.connect_query("SELECT COUNT(*) as id FROM adres", "id");
            id++;
            query = "INSERT INTO adres VALUES (" + id + "," + miasto + ",'" + kod_pocztowy + "','" + ulica + "'," + numer_domu;
            if(numer_mieszkania != 0){
                query += "," + numer_mieszkania + ")";
            } else {
                query += ",null)";
            }
            Connect.insert(query);
            return id;
        }
    }
    private int addMiasto(String miasto, String kraj){
        int id = 0;
        String query = "SELECT id_miasta as id FROM miasto, kraj WHERE miasto.nazwa = '" + miasto + "' AND kraj.nazwa = '" + kraj + "'";
        id = Connect.connect_query(query, "id");
        if(id != 0){
            System.out.print("City found.");
            return id;
        } else {
            System.out.print("City not found.");
            int id_kraju = Connect.connect_query("SELECT id_kraju as id FROM kraj WHERE nazwa = '" + kraj + "'", "id");
            int id_miasta = Connect.connect_query("SELECT COUNT(*) as sum FROM miasto", "sum");
            id_miasta++;
            query = "INSERT INTO miasto VALUES (" + id_miasta + ",'" + miasto + "'," + id_kraju +")";
            Connect.insert(query);
            query = "SELECT id_miasta as id FROM miasto, kraj WHERE miasto.nazwa = '" + miasto + "' AND kraj.nazwa = '" + kraj + "'";
            id = Connect.connect_query(query, "id");
            return id;
        }
    }

    private int addKandydat(String imie, String nazwisko, String pesel, String haslo, String data_urodzenia, int idadres){
        int index = 0;
        String query  = "SELECT id_kandydata FROM kandydat WHERE imie = '" + imie + "' AND nazwisko = '"  + nazwisko
                + "' AND pesel = '" + pesel + "' AND data_urodzenia = '" + data_urodzenia + "'";
        index = Connect.connect_query(query, "id_kandydata");
        int sum = Connect.connect_query("SELECT count(*) as sum FROM kandydat", "sum");
        if(index != 0 && sum > 0){
            System.out.print("Person found");
            return index;
        } else {
            System.out.print("Person not found");
            index = sum + 1;
            query = "INSERT INTO kandydat VALUES (" + index + ",'" + imie + "','" + nazwisko + "','" + pesel +"'," + idadres + ",'"
                    + data_urodzenia + "','" + haslo + "','N', null)";
            Connect.insert(query);
            return index;
        }
    }

    private void addAplikacja(int id_kandydata){
        int index = 0;

        String query  = "SELECT id_aplikacji FROM aplikacja WHERE id_kandydata = "  + id_kandydata;
        index = Connect.connect_query(query, "id_aplikacji");
        int sum = Connect.connect_query("SELECT count(*) as sum FROM aplikacja", "sum");
        if(index != 0 && sum > 0){
            JOptionPane.showMessageDialog(this, "Your application was already submitted.");
        } else {
            index = sum + 1;
            query = "INSERT INTO aplikacja VALUES(" + index + ",(SELECT date('now')),'N'," + id_kandydata  + ")";
            Connect.insert(query);
            JOptionPane.showMessageDialog(this, "Your application was successfully submitted.");
        }
    }
}
