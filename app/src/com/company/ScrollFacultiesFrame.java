package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class ScrollFacultiesFrame extends JFrame{
    private JButton zobaczButton;
    private JComboBox wydzialComboBox;
    private JList list1;
    private JPanel faculties;
    private JButton powrótDoMenuButton;
    private JButton zarządzajSwoimiPreferencjamiButton;

    public ScrollFacultiesFrame(String str){
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(faculties);
        this.pack();

        List<String> faculty_query = Connect.select_faculties();
        wydzialComboBox.setModel(new DefaultComboBoxModel<String>(faculty_query.toArray(new String[0])));

        zobaczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String faculty = wydzialComboBox.getSelectedItem().toString();
                List<String> majors_query = Connect.select_major(faculty);
                System.out.print(majors_query);
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
                JFrame frame = new MainFrame("Rekrutacja BD2");
                setVisible(false);
                frame.setVisible(true);
                dispose();
            }
        });
        zarządzajSwoimiPreferencjamiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }
}
