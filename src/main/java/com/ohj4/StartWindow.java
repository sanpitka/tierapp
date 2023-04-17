package com.ohj4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;

public class StartWindow implements Runnable {

    @Override
    public void run() {
        JFrame window = new JFrame();
        Image logo = Toolkit.getDefaultToolkit().getImage("Logo.png");

        window.setIconImage(logo);
        window.setTitle("Tier List Maker");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setVisible(true);

        window.setLayout(new BorderLayout());
        
        window.add(setNorthPanel(), BorderLayout.NORTH);
        window.add(setWestPanel(), BorderLayout.WEST);
        window.add(setRows(), BorderLayout.CENTER);
    }

    /**
     * Sets the left-side column, where the rank letters are
     */
    private Component setWestPanel() {

        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension(130,600));
        westPanel.setLayout(new GridLayout(7,1));
        westPanel.setBackground(new Color(184, 184, 184));
        

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

    /**
     * Sets the top row of buttons and fields
     */
    private Component setNorthPanel() {
        JPanel northPanel = new JPanel(new GridLayout());
        JPanel northPanelLeft = new JPanel(new GridLayout());
        JPanel northPanelRight = new JPanel(new GridLayout());
        Color buttonPink = new Color(255, 196, 242);

        JButton menubutton = new MyButtons().setNorthButton("Menu", Color.BLACK, "menu");
        northPanelLeft.add(menubutton);
        
        JButton gorankbutton = new MyButtons().setNorthButton("Go Rank!", buttonPink, "rank");
        northPanelLeft.add(gorankbutton);

        northPanel.add(northPanelLeft);

        JTextField listName = new JTextField("Unnamed tier list...");
        Font listNameFont = new Font(listName.getFont().getName(),listName.getFont().getStyle(),20);
        listName.setBorder(BorderFactory.createEmptyBorder(0,20, 0, 20));
        listName.setFont(listNameFont);
        northPanel.add(listName);

        JButton screenshotbutton = new MyButtons().setNorthButton("<html>" + "Take" + "<br>" + "Screenshot" + "</html>", Color.BLACK, "screenshot");
        northPanelRight.add(screenshotbutton);
        
        //TODO: Change the image icon according to the user's choice
        ImageIcon category = new ImageIcon("Breakfast1.png");
        JLabel categoryLabel = new JLabel(category);
        northPanelRight.add(categoryLabel);

        northPanel.add(northPanelRight);
        
        return northPanel;
    }

    /**
     * Sets the rows in the screen, where the ranked pictures go
     */
    private Component setRows() {
        JPanel rows = new JPanel();
        rows.setPreferredSize(new Dimension(570, 600));
        rows.setLayout(new GridLayout(7,1));
        rows.setBackground(new Color(184, 184, 184));

        Color lightGray = new Color(217, 217, 217);
        Color darkGray = new Color(184, 184, 184);

        int rowCounter = 7;
        for (int i=0; i<rowCounter; i++) {
            JPanel newPanel = new JPanel();
            newPanel.setBackground(i % 2 == 0 ? lightGray : darkGray);
            
            rows.add(newPanel);
        }

        return rows;
    }


    /**
     * Sets the ranking labels in the left-most panel
     */
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
    
}
