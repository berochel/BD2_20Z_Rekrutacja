package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{
    private JPanel rekrutacja;
    private JButton utwórzKontoButton;
    private JTextField imieTextField;
    private JTextField hasloTextField;
    private JButton logButton;
    private JTextField nazwiskoTextField;
    private JButton przeglądajButton;
    private JButton zarządzajSystememButton;
    private JLabel loggedStudentLabel;
    private JTextField queryTextField;
    private JButton runButton;
    private JTextArea query_result;

    public MainFrame(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(rekrutacja);
        this.pack();


        utwórzKontoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame createApplicatonFrame = new NewApplicationFrame("Nowa aplikacja");
                setVisible(false);
                createApplicatonFrame.setVisible(true);
                dispose();
            }
        });
        przeglądajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(loggedStudentLabel.getText() != "Nie zalogowano"){
                    JFrame scrollFaculties = new ScrollFacultiesFrame("Przeglądaj kierunki");
                    setVisible(false);
                    scrollFaculties.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(rekrutacja, "Aby uzyskać dostęp musisz być zalogowany.");
                }

            }
        });
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String imie = imieTextField.getText().strip();
                String nazwisko = nazwiskoTextField.getText().strip();
                String password = hasloTextField.getText().strip();
                String query = "SELECT id_kandydata FROM kandydat WHERE imie = '" + imie + "' AND nazwisko = '"
                        + nazwisko + "' AND haslo = '" + password + "'" ;
                int index = Connect.connect_query(query, "id_kandydata");
                int sum = Connect.connect_query("SELECT count(*) as sum FROM kandydat", "sum");
                if(index != 0 && sum > 0){
                    JOptionPane.showMessageDialog(rekrutacja, "Logowanie przebiegło pomyślnie.");
                    loggedStudentLabel.setText(imie + " " + nazwisko);
                } else {
                    JOptionPane.showMessageDialog(rekrutacja, "Logowanie nie powiodło się.");
                    loggedStudentLabel.setText("Nie zalogowan0");
                }
                imieTextField.setText("Imię");
                nazwiskoTextField.setText("Nazwisko");
                hasloTextField.setText("Hasło");
            }
        });
    }

}
