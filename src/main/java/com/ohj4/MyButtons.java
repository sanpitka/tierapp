package com.ohj4;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;

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
        newButton.setVisible(true);

        return newButton;
    }

}