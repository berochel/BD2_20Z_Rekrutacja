package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

public class ScrollFacultiesFrame extends JFrame{
    private JButton zobaczButton;
    private JComboBox wydzialComboBox;
    private JList list1;
    private JPanel faculties;
    private JButton powrótDoMenuButton;
    private JButton zarządzajSwoimiPreferencjamiButton;
    private JButton ustawPreferencjeButton;
    private JSpinner preferencjaSpinner;
    private int logged_id;

    public ScrollFacultiesFrame(String str, int id){
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(faculties);
        this.pack();
        logged_id = id;

        SpinnerModel sm = new SpinnerNumberModel(1, 1, 5, 1);
        preferencjaSpinner.setModel(sm);
        List<String> faculty_query = Connect.select_faculties();
        wydzialComboBox.setModel(new DefaultComboBoxModel<String>(faculty_query.toArray(new String[0])));

        zobaczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String faculty = wydzialComboBox.getSelectedItem().toString();
                List<String> majors_query = Connect.select_major(faculty);
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < majors_query.size(); i++)
                {
                    listModel.addElement(majors_query.get(i));
                }
                list1.setModel(listModel);
            }
        });
        powrótDoMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new MainFrame("Rekrutacja BD2", logged_id);
                setVisible(false);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                dispose();
            }
        });
        zarządzajSwoimiPreferencjamiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame managePreferencesFrame = new ManageYourApplicationForm("Zarządzaj swoimi preferencjami", logged_id);
                setVisible(false);
                managePreferencesFrame.setVisible(true);
                dispose();
            }
        });
        ustawPreferencjeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String degree = list1.getModel().getElementAt(list1.getSelectedIndex()).toString();
                String faculty = wydzialComboBox.getSelectedItem().toString();
                // dodaj preferencje degree, faculty, index
            }
        });
    }
}
