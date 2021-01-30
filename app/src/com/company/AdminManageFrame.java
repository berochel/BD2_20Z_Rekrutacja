package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AdminManageFrame extends JFrame {
    private JPanel adminForm;
    private JLabel wydzialLabel;
    private JComboBox rekrutacjaComboBox;
    private JButton powrótDoMenuButton;
    private JComboBox turaComboBox;
    private JLabel dataRozpLabel;
    private JLabel dataZakLabel;
    List<String> rekrutacja_query;

    public AdminManageFrame(String str) {
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(adminForm);
        this.pack();

        //String query = "SELECT id_pracownika FROM pracownik WHERE imie = 'Rafał'; ";
        // index = Connect.connect_query_int(query, "id_pracownika");
        //int sum = Connect.connect_query("SELECT count(*) as sum FROM kandydat", "sum");

        rekrutacja_query = Connect.select_rekrutacja();
        rekrutacjaComboBox.setModel(new DefaultComboBoxModel<String>(rekrutacja_query.toArray(new String[0])));


        powrótDoMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new MainFrame("Rekrutacja BD2");
                setVisible(false);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                dispose();
            }
        });
        rekrutacjaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int number = rekrutacjaComboBox.getSelectedIndex();
                //wydzialLabel.setText(String.valueOf(number));

                String temp = rekrutacja_query.get(number);
                int tempIndexBeginning = temp.indexOf(" ");
                int tempIndexEnd = temp.indexOf(".");
                if(tempIndexEnd != -1 && tempIndexBeginning != -1) {
                    temp = temp.substring(tempIndexBeginning+1, tempIndexEnd);
                }
                wydzialLabel.setText(temp);

                /*String query = "SELECT id_wydzialu FROM lista_wydzialow WHERE id_rekrutacji = "+temp+";";
                int index = Connect.connect_query_int(query, "id_wydzialu");

                query = "SELECT Nazwa FROM Wydzial WHERE id_wydzialu = "+index+";";
                String text = Connect.connect_query_string(query, "Nazwa");
                wydzialLabel.setText(text);*/
                String query = "SELECT * from tura WHERE id_rekrutacji = '"+temp+"'";

                List<Map<String, Object>> tura_set = new ArrayList<Map<String, Object>>();
                tura_set = Connect.connect(query);
                for (Map<String, Object> map : tura_set) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + " - " + entry.getValue());
                    }
                }
                List<String> tura_query  = new ArrayList<String>();
                for (Map<String, Object> map : tura_set) {

                    tura_query.add("Tura nr. "+map.get("id_tury"));
                }

                turaComboBox.setModel(new DefaultComboBoxModel<String>(tura_query.toArray(new String[0])));
            }
        });
    }
}
