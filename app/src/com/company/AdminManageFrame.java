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
                JFrame frame = new MainFrame("Rekrutacja BD2", 0);
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
                String query = "SELECT * from rekrutacja WHERE id_rekrutacji = '"+temp+"'";

                List<Map<String, Object>> rekrutacja_set;
                rekrutacja_set = Connect.connect(query);
                for (Map<String, Object> map : rekrutacja_set) {
                    //column labels are case sensitive
                    dataRozpLabel.setText(String.valueOf(map.get("data_rozp")));
                    dataZakLabel.setText(String.valueOf(map.get("data_zak")));
                }


                query = "SELECT * from tura WHERE id_rekrutacji = '"+temp+"'";

                List<Map<String, Object>> tura_set;
                tura_set = Connect.connect(query);
                /*for (Map<String, Object> map : tura_set) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + " - " + entry.getValue());
                    }
                }*/
                List<String> tura_query  = new ArrayList<>();
                for (Map<String, Object> map : tura_set) {

                    tura_query.add("Tura nr. "+map.get("id_tury"));
                }

                turaComboBox.setModel(new DefaultComboBoxModel<>(tura_query.toArray(new String[0])));
            }
        });
    }
}
