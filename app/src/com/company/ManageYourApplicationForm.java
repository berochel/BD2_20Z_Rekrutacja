package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        Object columns[] = {"Preferencja", "Kierunek", "Wydział", "Liczba miejsc"};
        Object data[][] = {{"1","AAA","EITI","50"},{"2","BBB","Mechatro","140"},{"3","CCC","MINI","35"},{"4","CCC","MINI","35"},{"5","CCC","MINI","35"}};

        for (int i = 1; i<6; i++){
            String query = "SELECT wybor" + i + " as wybor FROM aplikacja WHERE id_kandydata = " + logged_id;
            int index = Connect.connect_query_int(query, "wybor");
            if(index == 0){
                data[i-1][1] = "null";
                data[i-1][2] = "null";
                data[i-1][3] = "null";
            }else{
                query = "SELECT nazwa FROM kierunek_studiow WHERE id_kierunku = " + index;
                String degree = Connect.connect_query_string(query, "nazwa");
                query = "SELECT id_wydzialu FROM kierunek_studiow WHERE id_kierunku = " + index;
                int facultyid = Connect.connect_query_int(query, "id_wydzialu");
                query = "SELECT nazwa FROM wydzial WHERE id_wydzialu = " + facultyid;
                String faculty = Connect.connect_query_string(query, "nazwa");

                data[i-1][1] = degree;
                data[i-1][2] = faculty;
            }

        }
        String query = "SELECT wybor";
        DefaultTableModel model = new DefaultTableModel(data, columns);
        table1.setModel(model);
        table1.setDefaultEditor(Object.class, null);

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
        usuńWybórButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int row = table1.getSelectedRow();
                Object position = table1.getModel().getValueAt(row, 0);
                System.out.print(position.toString());
                String query = "UPDATE aplikacja SET wybor" + position + " = null WHERE id_kandydata = " + logged_id;
                Connect.insert(query);
                data[row][1] = "null";
                data[row][2] = "null";
                data[row][3] = "null";
                model.fireTableDataChanged();
            }
        });
    }
}
