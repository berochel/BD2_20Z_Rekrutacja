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
    private JLabel kandydaciLabel;
    private JLabel glownaLabel;
    private JLabel rezerwowaLabel;
    private JComboBox kierunkiComboBox;
    List<String> rekrutacja_query;
    List<String> tura_query  = new ArrayList<>();
    List<Map<String, Object>> tura_set;

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
                String query = "SELECT * from rekrutacja WHERE id_rekrutacji = '"+temp+"'";

                List<Map<String, Object>> rekrutacja_set;
                rekrutacja_set = Connect.connect(query);
                for (Map<String, Object> map : rekrutacja_set) {
                    //column labels are case sensitive
                    dataRozpLabel.setText(String.valueOf(map.get("data_rozp")));
                    dataZakLabel.setText(String.valueOf(map.get("data_zak")));
                }


                query = "SELECT * from tura WHERE id_rekrutacji = '"+temp+"'";


                tura_set = Connect.connect(query);
                //uncomment to print whole list of maps containing result set of query.
                /*for (Map<String, Object> map : tura_set) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + " - " + entry.getValue());
                    }
                }*/
                tura_query.clear();
                for (Map<String, Object> map : tura_set) {

                    tura_query.add("Tura nr."+map.get("id_tury"));
                }
                turaComboBox.setModel(new DefaultComboBoxModel<>(tura_query.toArray(new String[0])));
            }
        });
        turaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int number = turaComboBox.getSelectedIndex();
                String temp = tura_query.get(number);
                int tempIndexBeginning = temp.indexOf(".");
                if(tempIndexBeginning != -1) {
                    temp = temp.substring(tempIndexBeginning+1);
                }
                String query = "";

                List<Map<String, Object>> tempList;

                query = "SELECT * from lista WHERE id_tury = '"+temp+"'";
                tempList = Connect.connect(query);

                if(tempList.size() == 0)
                {
                    kandydaciLabel.setText(String.valueOf(0));
                    glownaLabel.setText(String.valueOf(0));
                    rezerwowaLabel.setText(String.valueOf(0));
                }

                int glownaCount = 0, rezerwowaCount = 0;

                List<Map<String, Object>> kandydaci_set = new ArrayList<>();
                List<Map<String, Object>> tempList2;

                for (Map<String, Object> map : tempList) {
                    query = "SELECT * from lista_kandydat WHERE id_listy = '"+map.get("id_listy")+"'";
                    tempList2 = Connect.connect(query);

                    String tempString = String.valueOf(map.get("rezerwowa"));

                    if(tempString.equals("N"))
                        glownaCount = tempList2.size();
                    else
                        rezerwowaCount = tempList2.size();

                    kandydaci_set.addAll(tempList2);

                }
                kandydaciLabel.setText(String.valueOf(kandydaci_set.size()));
                glownaLabel.setText(String.valueOf(glownaCount));
                rezerwowaLabel.setText(String.valueOf(rezerwowaCount));

                query = "SELECT kierunek_studiow.nazwa, realizacja.kod from kierunek_studiow, realizacja WHERE kierunek_studiow.id_kierunku = realizacja.id_kierunku AND realizacja.id_kierunku IN (SELECT id_kierunku from realizacja WHERE kod IN (SELECT kod_realizacji from lista WHERE id_tury = '"+temp+"'))";
                tempList = Connect.connect(query);

                for (Map<String, Object> map : tempList) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + " - " + entry.getValue());
                    }
                }

                List<String> kierunek_query  = new ArrayList<>();
                for (Map<String, Object> map : tempList) {

                    kierunek_query.add("Kierunek-"+map.get("kod")+", "+map.get("nazwa"));
                }

                kierunkiComboBox.setModel(new DefaultComboBoxModel<>(kierunek_query.toArray(new String[0])));
            }
        });
    }
}
