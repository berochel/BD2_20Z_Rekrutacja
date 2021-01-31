package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminManageFormLogin extends JFrame {
    private JPanel adminLogin;
    private JTextField imieTextField;
    private JTextField hasloTextField;
    private JButton logButton;
    private JTextField nazwiskoTextField;

    public AdminManageFormLogin(String str){
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(adminLogin);
        this.pack();

        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imie = imieTextField.getText().strip();
                String nazwisko = nazwiskoTextField.getText().strip();
                String password = hasloTextField.getText().strip();
                String query = "SELECT id_pracownika FROM pracownik WHERE imie = '" + imie + "' AND nazwisko = '"
                        + nazwisko + "' AND haslo = '" + password + "'" ;
                int index = Connect.connect_query_int(query, "id_pracownika");
                int sum = Connect.connect_query_int("SELECT count(*) as sum FROM pracownik", "sum");
                if(index != 0 && sum > 0){
                    JOptionPane.showMessageDialog(null, "Logowanie przebiegło pomyślnie.");
                    JFrame frame = new AdminManageFrame("Rekrutacja BD2 - Panel Administratora");
                    setVisible(false);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Logowanie nie powiodło się.");
                }
                imieTextField.setText("Imię");
                nazwiskoTextField.setText("Nazwisko");
                hasloTextField.setText("Hasło");
            }
        });
    }
}
