package com.ohj4;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * The MyButtons class creates a JButton with customized properties and adds an ActionListener to it.
 */
public class MyButtons extends JPanel {
    
    public JButton setNorthButton(String buttonText, Color buttonColor, String buttonAction) {
        JButton newButton = new JButton(buttonText);
        newButton.setFont(new Font("Arial", Font.PLAIN, 20));
        newButton.setBackground(buttonColor);
        newButton.setBorderPainted(false);
        newButton.setActionCommand(buttonAction);
        if (buttonColor == Color.BLACK) {
            newButton.setForeground(Color.WHITE);
        }
        newButton.setFocusPainted(false);
        newButton.addActionListener(new MyButtonActions());

        return newButton;
    }

    public JMenuItem setMenuButton(String buttonText, String buttonAction) {

        JMenuItem newButton = new JMenuItem(buttonText);
        newButton.setFont(new Font("Arial", Font.PLAIN, 20));
        newButton.setBackground(Color.BLACK);
        newButton.setBorderPainted(false);
        newButton.setBorder(null);
        newButton.setActionCommand(buttonAction);
        newButton.setForeground(Color.WHITE);
        newButton.addActionListener(new MyButtonActions());
        
        return newButton;
    }

}
