package com.company;

import javax.swing.*;
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
    private JButton zobaczButton;
    private JTextField queryTextField;
    private JButton runButton;
    private JTextArea query_result;
    private int logged_id;

    public MainFrame(String title, int id){
        super(title);
        logged_id = id;
        if (id != 0){
            logButton.setText("Wyloguj");
            String query = "SELECT imie FROM kandydat WHERE id_kandydata = " + logged_id ;
            String imie = Connect.connect_query_string(query, "imie");
            query = "SELECT nazwisko FROM kandydat WHERE id_kandydata = " + logged_id ;
            String nazwisko = Connect.connect_query_string(query, "nazwisko");
            loggedStudentLabel.setText(imie + " " + nazwisko);
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(rekrutacja);
        this.pack();


        utwórzKontoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame createApplicatonFrame = new NewApplicationFrame("Nowa aplikacja", logged_id);
                setVisible(false);
                createApplicatonFrame.setVisible(true);
                createApplicatonFrame.setLocationRelativeTo(null);
                dispose();
            }
        });
        przeglądajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(logged_id != 0){
                    JFrame scrollFaculties = new ScrollFacultiesFrame("Przeglądaj kierunki", logged_id);
                    setVisible(false);
                    scrollFaculties.setVisible(true);
                    scrollFaculties.setLocationRelativeTo(null);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(rekrutacja, "Aby uzyskać dostęp musisz być zalogowany.");
                }

            }
        });
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (logged_id == 0) {
                    String imie = imieTextField.getText().strip();
                    String nazwisko = nazwiskoTextField.getText().strip();
                    String password = hasloTextField.getText().strip();
                    String query = "SELECT id_kandydata FROM kandydat WHERE imie = '" + imie + "' AND nazwisko = '"
                            + nazwisko + "' AND haslo = '" + password + "'";
                    int index = Connect.connect_query_int(query, "id_kandydata");
                    int sum = Connect.connect_query_int("SELECT count(*) as sum FROM kandydat", "sum");
                    if (index != 0 && sum > 0) {
                        JOptionPane.showMessageDialog(null, "Logowanie przebiegło pomyślnie.");
                        logButton.setText("Wyloguj");
                        loggedStudentLabel.setText(imie + " " + nazwisko);
                        logged_id = index;
                    } else {
                        JOptionPane.showMessageDialog(null, "Logowanie nie powiodło się.");
                        loggedStudentLabel.setText("Nie zalogowano");
                        logged_id = 0;
                    }
                    imieTextField.setText("Imię");
                    nazwiskoTextField.setText("Nazwisko");
                    hasloTextField.setText("Hasło");

                } else {
                    JOptionPane.showMessageDialog(null, "Wylogowanie przebiegło pomyślnie.");
                    logged_id = 0;
                    imieTextField.setText("Imię");
                    nazwiskoTextField.setText("Nazwisko");
                    hasloTextField.setText("Hasło");
                    loggedStudentLabel.setText("Nie zalogowano");
                    logButton.setText("Zaloguj");
                }
            }
        });
        zarządzajSystememButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(loggedStudentLabel.getText() == "Nie zalogowano"){
                    JFrame adminManageLoginFrame = new AdminManageFormLogin("Przeglądaj kierunki");
                    setVisible(false);
                    adminManageLoginFrame.setVisible(true);
                    adminManageLoginFrame.setLocationRelativeTo(null);
                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(rekrutacja, "Nie posiadasz jako użytkownik takich uprawnień.");
                }
            }
        });
        zobaczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(logged_id != 0){
                    JFrame createApplicatonFrame = new NewApplicationFrame("Przeglądanie aplikacji", logged_id);
                    setVisible(false);
                    createApplicatonFrame.setVisible(true);
                    createApplicatonFrame.setLocationRelativeTo(null);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(rekrutacja, "Dostęp tylko dla zalogowanych użytkowników.");
                }
            }
        });
    }

}
