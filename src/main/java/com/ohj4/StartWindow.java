package com.ohj4;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
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

    private Component setNorthPanel() {
        JPanel northPanel = new JPanel(new GridLayout());
        JPanel northPanelLeft = new JPanel(new GridLayout());
        JPanel northPanelRight = new JPanel(new GridLayout());

        northPanelLeft.add(setButton("Menu", Color.BLACK, "menu"));
        Color buttonPink = new Color(255, 196, 242);
        northPanelLeft.add(setButton("Go Rank!", buttonPink, "rank"));

        northPanel.add(northPanelLeft);

        JTextField listName = new JTextField("Unnamed tier list...");
        Font listNameFont = new Font(listName.getFont().getName(),listName.getFont().getStyle(),20);
        listName.setBorder(BorderFactory.createEmptyBorder(0,20, 0, 20));
        listName.setFont(listNameFont);
        northPanel.add(listName);

        northPanelRight.add(setButton("<html>" + "Take" + "<br>" + "Screenshot" + "</html>", Color.BLACK, "screenshot"));
        
        //TODO: Change the image icon according to the user's choice
        ImageIcon category = new ImageIcon("Breakfast1.png");
        JLabel categoryLabel = new JLabel(category);
        northPanelRight.add(categoryLabel);

        northPanel.add(northPanelRight);
        
        return northPanel;
    }

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


    private JButton setButton(String buttonText, Color buttonColor, String buttonAction) {
        JButton newButton = new JButton(buttonText);
        newButton.setFont(new Font("Arial", Font.PLAIN, 20));
        newButton.setBackground(buttonColor);
        newButton.setBorderPainted(false);
        newButton.setActionCommand(buttonAction);
        if (buttonColor == Color.BLACK) {
            newButton.setForeground(Color.WHITE);
        }
        newButton.setFocusPainted(false);
        newButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // TODO add button actions here
                String command = e.getActionCommand();
                if (command == "menu") {
                    // menu button pressed
                    System.out.println(buttonAction + " pressed.");
                } else if (command == "rank") {
                    // 'go rank' pressed
                    System.out.println(buttonAction + " pressed.");
                } else if (command == "screenshot") {
                    // 'screenshot' pressed
                    System.out.println(buttonAction + " pressed.");
                    BufferedImage image;
                    try {
                        image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                        ImageIO.write(image, "png", new File("C:/Users/sanpi/Lipasto/Ohjelmointi 4/tierapp/screenshot.png"));
                        //TODO: Fix the path and take screenshot only from the application, not the whole screen
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
                }                
            }
            
        });

        return newButton;
    }
    
}
