package com.ohj4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class StartWindow implements Runnable {

    @Override
    public void run() {
        JFrame window = new JFrame();

        window.setTitle("Tier List Maker");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setVisible(true);

        window.setLayout(new BorderLayout());

        window.add(setNorthPanel(), BorderLayout.NORTH);
        window.add(setWestPanel(), BorderLayout.WEST);
    }

    private Component setWestPanel() {

        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension(100, 600));
        westPanel.setLayout(new GridLayout(7,1));

        Color labelRed = new Color(220,40,40);
        westPanel.add(createNewLabel("S", labelRed));

        Color buttonOrange = new Color(191,93,22);
        westPanel.add(createNewLabel("A", buttonOrange));

        Color buttonYellow = new Color(190, 179, 77);
        westPanel.add(createNewLabel("B", buttonYellow));

        Color buttonGreen = new Color(125, 142, 114);
        westPanel.add(createNewLabel("C", buttonGreen));

        Color buttonTurquoise = new Color(113,156,161);
        westPanel.add(createNewLabel("D", buttonTurquoise));

        Color buttonBlue = new Color(115, 130, 168);
        westPanel.add(createNewLabel("E", buttonBlue));

        Color buttonViolet = new Color(174, 124, 182);
        westPanel.add(createNewLabel("F", buttonViolet));

        return westPanel;
    }

    private Component setNorthPanel() {
        JPanel northPanel = new JPanel(new GridBagLayout());
        northPanel.setPreferredSize(new Dimension(800, 80));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 100;
        c.ipady = 80;

        northPanel.add(setButton("Menu", Color.BLACK), c);

        Color buttonPink = new Color(255, 196, 242);
        c.gridx = 1;
        northPanel.add(setButton("GO RANK!", buttonPink), c);

        /*
        JTextField listName = new JTextField("Unnamed tier list...");
        listName.setPreferredSize(new Dimension(200, 70));
        Font listNameFont=new Font(listName.getFont().getName(),listName.getFont().getStyle(),16);
        listName.setBorder(BorderFactory.createEmptyBorder(0,20, 0, 20));
        listName.setFont(listNameFont);
        northPanel.add(listName);

        northPanel.add(setButton("<html>" + "Take" + "<br>" + "Screenshot" + "</html>", Color.BLACK));

        ImageIcon breakfast = new ImageIcon("Breakfast1.png");
        JButton imgButton = new JButton(breakfast);
        northPanel.add(imgButton);*/
        
        return northPanel;
    }

    private JPanel createNewLabel(String label, Color backgroundColor) {
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel(label, SwingConstants.CENTER);
        Font labelFont=new Font("Arial", Font.PLAIN, 40);
        newLabel.setForeground(Color.WHITE);
        newPanel.setBorder(BorderFactory.createEmptyBorder(7,20,20,20));
        newLabel.setFont(labelFont);
        newPanel.add(newLabel);
        newPanel.setBackground(backgroundColor);
        return newPanel;
    }


    private JButton setButton(String buttonText, Color buttonColor) {
        JButton newButton = new JButton(buttonText);
        newButton.setFont(new Font("Arial", Font.PLAIN, 20));
        newButton.setBackground(buttonColor);
        if (buttonColor == Color.BLACK) {
            newButton.setForeground(Color.WHITE);
        }
        return newButton;
    }
    
}
