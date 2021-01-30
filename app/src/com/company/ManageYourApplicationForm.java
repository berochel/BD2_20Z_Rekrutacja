package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageYourApplicationForm extends JFrame {

    private JScrollPane scrollPane1;
    private JLabel imieLabel;
    private JLabel zalogowanoJakoLabel;
    private JLabel headerLabel;
    private JTable table1;
    private JButton usuńWybórButton;
    private JButton wróćDoMenuButton;
    private JPanel preferencje;
    private int logged_id;

    public ManageYourApplicationForm(String str, int id){
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(preferencje);
        this.pack();
        logged_id = id;
        if (logged_id != 0){
            String query = "SELECT imie FROM kandydat WHERE id_kandydata = " + logged_id ;
            String imie = Connect.connect_query_string(query, "imie");
            query = "SELECT nazwisko FROM kandydat WHERE id_kandydata = " + logged_id ;
            String nazwisko = Connect.connect_query_string(query, "nazwisko");
            imieLabel.setText(imie + " " + nazwisko);
        }

        wróćDoMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new MainFrame("Rekrutacja BD2", logged_id);
                setVisible(false);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                dispose();
            }
        });
    }
}
