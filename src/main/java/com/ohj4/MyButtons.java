package com.ohj4;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * The MyButtons class creates a JButton with customized properties and adds an ActionListener to it.
 */
public class MyButtons extends JPanel {

    JFrame window;    
    public MyButtons(JFrame window) {
        this.window = window;
    }

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
        newButton.addActionListener(new MyButtonActions(this.window));
        newButton.setVisible(true);

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
        newButton.addActionListener(new MyButtonActions(this.window));
        return newButton;
    }

    public JButton setDialogButton(String buttonText, String buttonAction) {

        JButton newButton = new JButton(buttonText);
        newButton.setFont(new Font("Arial", Font.PLAIN, 20));
        newButton.setBackground(Color.LIGHT_GRAY);
        newButton.setForeground(Color.BLACK);
        newButton.setFocusPainted(false);
        newButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5,10,5,10)
        ));
        newButton.setActionCommand(buttonAction);
        newButton.addActionListener(new MyButtonActions(this.window));

        return newButton;
    }

    public JButton setDialogButtonWithoutAction(String buttonText) {

        JButton newButton = new JButton(buttonText);
        newButton.setFont(new Font("Arial", Font.PLAIN, 20));
        newButton.setBackground(Color.LIGHT_GRAY);
        newButton.setForeground(Color.BLACK);
        newButton.setFocusPainted(false);
        newButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5,10,5,10)
        ));

        return newButton;
    }

}
