package com.company;

import javax.swing.*;


public class AdminManageFrame extends JFrame {
    private JPanel adminForm;

    public AdminManageFrame(String str){
        super(str);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(adminForm);
        this.pack();
    }
}
