package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AdminManageFrame extends JFrame {
    private JPanel adminForm;
    private JComboBox rekrutacjaComboBox;
    private JButton powrotDoMenuButton;
    private JComboBox turaComboBox;
    private JLabel dataRozpLabel;
    private JLabel dataZakLabel;
    private JLabel kandydaciLabel;
    private JLabel glownaLabel;
    private JLabel rezerwowaLabel;
    private JComboBox kierunkiComboBox;
    private JTextField turaRozpLabel;
    private JTextField turaZakLabel;
    private JLabel kierunekAllLabel;
    private JLabel kierunekGlownaLabel;
    private JLabel kierunekRezerwowaLabel;
    private JButton zgloszeniaButton;
    private JButton zmianaButton;
    List<String> rekrutacja_query;
    List<String> tura_query  = new ArrayList<>();
    List<String> kierunek_query  = new ArrayList<>();
    List<Map<String, Object>> tura_set;

    public AdminManageFrame(String str) {
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(adminForm);
        this.pack();

        rekrutacja_query = Connect.select_rekrutacja();
        rekrutacjaComboBox.setModel(new DefaultComboBoxModel<>(rekrutacja_query.toArray(new String[0])));


        powrotDoMenuButton.addActionListener(e -> {
            JFrame frame = new MainFrame("Rekrutacja BD2", 0);
            setVisible(false);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            dispose();
        });
        rekrutacjaComboBox.addActionListener(e -> {
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

                tura_query.add("Tura nr."+map.get("nr_tury"));
            }
            turaComboBox.setModel(new DefaultComboBoxModel<>(tura_query.toArray(new String[0])));
        });
        turaComboBox.addActionListener(e -> {

            kierunekGlownaLabel.setText(String.valueOf(0));
            kierunekRezerwowaLabel.setText(String.valueOf(0));
            kierunekAllLabel.setText(String.valueOf(0));

            int number = turaComboBox.getSelectedIndex();
            String temp = tura_query.get(number);
            int tempIndexBeginning = temp.indexOf(".");
            if(tempIndexBeginning != -1) {
                temp = temp.substring(tempIndexBeginning+1);
            }
            String query;

            List<Map<String, Object>> tempList;

            query = "SELECT data_zak, data_rozp from tura WHERE id_tury = '"+temp+"'";
            tempList = Connect.connect(query);

            turaRozpLabel.setText(String.valueOf(tempList.get(0).get("data_rozp")));
            turaZakLabel.setText(String.valueOf(tempList.get(0).get("data_zak")));

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
                    glownaCount += tempList2.size();
                else
                    rezerwowaCount += tempList2.size();

                kandydaci_set.addAll(tempList2);

            }
            kandydaciLabel.setText(String.valueOf(kandydaci_set.size()));
            glownaLabel.setText(String.valueOf(glownaCount));
            rezerwowaLabel.setText(String.valueOf(rezerwowaCount));

            query = "SELECT kierunek_studiow.nazwa, realizacja.kod from kierunek_studiow, realizacja WHERE kierunek_studiow.id_kierunku = realizacja.id_kierunku AND realizacja.id_kierunku IN (SELECT id_kierunku from realizacja WHERE kod IN (SELECT kod_realizacji from lista WHERE id_tury = '"+temp+"'))";
            tempList = Connect.connect(query);

            kierunek_query.clear();
            for (Map<String, Object> map : tempList) {

                kierunek_query.add("Kierunek-"+map.get("kod")+", "+map.get("nazwa"));
            }

            kierunkiComboBox.setModel(new DefaultComboBoxModel<>(kierunek_query.toArray(new String[0])));
        });
        kierunkiComboBox.addActionListener(e -> {
            String query;
            List<Map<String, Object>> tempList;

            int number = turaComboBox.getSelectedIndex();
            String tempTura = tura_query.get(number);
            int tempIndexBeginning = tempTura.indexOf(".");
            if(tempIndexBeginning != -1) {
                tempTura = tempTura.substring(tempIndexBeginning+1);
            }

            number = kierunkiComboBox.getSelectedIndex();
            String tempKierunek = kierunek_query.get(number);
            tempIndexBeginning = tempKierunek.indexOf("-");
            int tempIndexEnd = tempKierunek.indexOf(",");
            if(tempIndexEnd != -1 && tempIndexBeginning != -1) {
                tempKierunek = tempKierunek.substring(tempIndexBeginning+1, tempIndexEnd);
            }

            query = "SELECT * from lista_kandydat WHERE id_listy IN (SELECT id_listy from lista WHERE kod_realizacji = '"+tempKierunek+"' AND id_tury = '"+tempTura+"' AND rezerwowa = 'N')";
            tempList = Connect.connect(query);
            int glownaNumber = tempList.size();


            query = "SELECT * from lista_kandydat WHERE id_listy IN (SELECT id_listy from lista WHERE kod_realizacji = '"+tempKierunek+"' AND id_tury = '"+tempTura+"' AND rezerwowa = 'T')";
            tempList = Connect.connect(query);
            int rezerwowaNumber = tempList.size();

            kierunekGlownaLabel.setText(String.valueOf(glownaNumber));
            kierunekRezerwowaLabel.setText(String.valueOf(rezerwowaNumber));
            kierunekAllLabel.setText(String.valueOf(glownaNumber+rezerwowaNumber));
        });

        zgloszeniaButton.addActionListener(e -> {
            JFrame frame = new notifyFrame("Rekrutacja BD2 - Zarządzanie zgłoszeniami", 0);
            setVisible(false);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            dispose();
        });

        zmianaButton.addActionListener(e -> {
            String query;
            List<Map<String, Object>> tempList;

            int number = turaComboBox.getSelectedIndex();
            String tempTura = tura_query.get(number);
            int tempIndexBeginning = tempTura.indexOf(".");
            if(tempIndexBeginning != -1) {
                tempTura = tempTura.substring(tempIndexBeginning+1);
            }
;
            query = "UPDATE tura " +
                    "SET data_zak = '"+turaZakLabel.getText().strip()+"'," +
                    "data_rozp = '"+turaRozpLabel.getText().strip()+"'" +
                    " WHERE " +
                    "id_tury = '"+tempTura+"'";
            tempList = Connect.connect(query);

            JOptionPane.showMessageDialog(adminForm, "Edycja tury przebiegła pomyślnie.");
        });
    }
}
