package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class notifyFrame extends JFrame{
    private JPanel adminNotifyForm;
    private JButton powrotDoMenuButton;
    private JComboBox zgloszenieComboBox;
    private JLabel opisLabel;
    private JLabel idZgloszeniaLabel;
    private JTextField imieTextField;
    private JTextField nazwiskoTextField;
    private JTextField peselTextField;
    private JTextField adresTextField;
    private JTextField dataTextField;
    private JTextField hasloTextField;
    private JLabel czyZlozylLabel;
    private JLabel czyAplikacjaLabel;
    private JLabel dataZlozeniaLabel;
    private JTextField czyOplaconaTextField;
    private JButton zmianaButton;
    private JButton zmianaAplikacjiButton;
    List<String> zgloszenie_query = new ArrayList<>();

    public notifyFrame(String str, int i) {
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(adminNotifyForm);
        this.pack();


        List<Map<String, Object>> tempList;

        String query = "SELECT * from zgloszenie";
        tempList = Connect.connect(query);
        for (Map<String, Object> map : tempList) {

            zgloszenie_query.add("Zgloszenie nr."+map.get("id_zgloszenia"));
        }
        zgloszenieComboBox.setModel(new DefaultComboBoxModel<>(zgloszenie_query.toArray(new String[0])));

        powrotDoMenuButton.addActionListener(e -> {
            JFrame frame = new AdminManageFrame("Rekrutacja BD2");
            setVisible(false);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            dispose();
        });
        zgloszenieComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query;
                List<Map<String, Object>> tempList;

                int number = zgloszenieComboBox.getSelectedIndex();
                String tempZgloszenie = zgloszenie_query.get(number);
                int tempIndexBeginning = tempZgloszenie.indexOf(".");
                if(tempIndexBeginning != -1) {
                    tempZgloszenie = tempZgloszenie.substring(tempIndexBeginning+1);
                }

                query = "SELECT * from zgloszenie WHERE id_zgloszenia = '"+tempZgloszenie+"'";
                tempList = Connect.connect(query);

                idZgloszeniaLabel.setText(String.valueOf(tempList.get(0).get("id_zgloszenia")));
                opisLabel.setText(String.valueOf(tempList.get(0).get("opis")));

                query = "SELECT * from kandydat WHERE id_kandydata = '"+tempList.get(0).get("id_kandydata")+"'";
                tempList = Connect.connect(query);

                imieTextField.setText(String.valueOf(tempList.get(0).get("imie")));
                nazwiskoTextField.setText(String.valueOf(tempList.get(0).get("nazwisko")));
                hasloTextField.setText(String.valueOf(tempList.get(0).get("haslo")));
                peselTextField.setText(String.valueOf(tempList.get(0).get("pesel")));
                adresTextField.setText(String.valueOf(tempList.get(0).get("adres")));
                dataTextField.setText(String.valueOf(tempList.get(0).get("data_urodzenia")));

                String temp = String.valueOf(tempList.get(0).get("czy_zlozyl_dokumenty"));
                if(temp.equals("T"))
                    czyZlozylLabel.setText("Tak");
                else
                    czyZlozylLabel.setText("Nie");

                query = "SELECT * from aplikacja WHERE id_kandydata = '"+tempList.get(0).get("id_kandydata")+"'";
                tempList = Connect.connect(query);
                if(tempList.size() > 0)
                    czyAplikacjaLabel.setText("Tak");
                else
                    czyAplikacjaLabel.setText("Nie");

                dataZlozeniaLabel.setText(String.valueOf(tempList.get(0).get("data_zlozenia")));
                czyOplaconaTextField.setText(String.valueOf(tempList.get(0).get("czy_oplacona")));
            }
        });
        zmianaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query;
                List<Map<String, Object>> tempList;

                int number = zgloszenieComboBox.getSelectedIndex();
                String tempZgloszenie = zgloszenie_query.get(number);
                int tempIndexBeginning = tempZgloszenie.indexOf(".");
                if(tempIndexBeginning != -1) {
                    tempZgloszenie = tempZgloszenie.substring(tempIndexBeginning+1);
                }

                query = "SELECT * from zgloszenie WHERE id_zgloszenia = '"+tempZgloszenie+"'";
                tempList = Connect.connect(query);

                String temp_id = String.valueOf(tempList.get(0).get("id_kandydata"));
                query = "UPDATE kandydat " +
                        "SET imie = '"+imieTextField.getText().strip()+"'," +
                        "nazwisko = '"+nazwiskoTextField.getText().strip()+"'," +
                        "pesel = '"+peselTextField.getText().strip()+"'," +
                        "data_urodzenia = '"+dataTextField.getText().strip()+"'," +
                        "haslo = '"+hasloTextField.getText().strip()+"'" +
                        " WHERE " +
                        "id_kandydata = '"+temp_id+"'";
                tempList = Connect.connect(query);

                query = "DELETE FROM zgloszenie WHERE id_zgloszenia = " + tempZgloszenie;
                Connect.insert(query);
                zgloszenieComboBox.updateUI();

                JOptionPane.showMessageDialog(adminNotifyForm, "Edycja danych kandydata przebiegła pomyślnie.");

            }
        });
        zmianaAplikacjiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query;
                List<Map<String, Object>> tempList;

                int number = zgloszenieComboBox.getSelectedIndex();
                String tempZgloszenie = zgloszenie_query.get(number);
                int tempIndexBeginning = tempZgloszenie.indexOf(".");
                if(tempIndexBeginning != -1) {
                    tempZgloszenie = tempZgloszenie.substring(tempIndexBeginning+1);
                }

                query = "SELECT * from zgloszenie WHERE id_zgloszenia = '"+tempZgloszenie+"'";
                tempList = Connect.connect(query);

                String temp_id = String.valueOf(tempList.get(0).get("id_kandydata"));
                query = "UPDATE aplikacja " +
                        "SET czy_oplacona = '"+czyOplaconaTextField.getText().strip()+"'" +
                        " WHERE " +
                        "id_kandydata = '"+temp_id+"'";
                tempList = Connect.connect(query);

                query = "DELETE FROM zgloszenie WHERE id_zgloszenia = " + tempZgloszenie;
                Connect.insert(query);
                zgloszenieComboBox.updateUI();

                JOptionPane.showMessageDialog(adminNotifyForm, "Edycja aplikacji przebiegła pomyślnie.");

            }
        });
    }
}
