package com.ohj4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Contains all the logic in getting ranking topics and ranking pictures of a specific topic.
 * Sends information to graphical components to display.
 */
public class RankLists {

    /**
     * Opens a window that shows a list of theme icons and names and lets user choose a new topic to rank. 
     * @param window the main
     * @return topic selection window
     */
    public JDialog selectTopic(JFrame window) {

        //Create a topic selection window on the main frame
        JDialog topicSelection = new JDialog();
        topicSelection.setUndecorated(true); // remove title bar
        topicSelection.setMinimumSize(new Dimension(500, 400));
        topicSelection.setMaximumSize(new Dimension(500, 400));
        topicSelection.setLayout(new FlowLayout());
        topicSelection.getContentPane().setBackground(Color.LIGHT_GRAY);
        topicSelection.setForeground(Color.BLACK);
        topicSelection.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        topicSelection.setLocationRelativeTo(window);

        //Create a 
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("Choose a topic you want to rank");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titlePanel.add(titleLabel);

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(Color.LIGHT_GRAY);

        File topicsFolder = new File("topics");
        if (topicsFolder.isDirectory()) {
            for (File folder : topicsFolder.listFiles()) {
                if (folder.isDirectory()) {
                    File imgFile = setTopicImage(folder.getName(), window);
                    JPanel row = new JPanel(new BorderLayout());
                    try {
                        BufferedImage topicImage = ImageIO.read(imgFile);
                        Image scaledImg = topicImage.getScaledInstance(100, 80, 0);
                        JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
                        String labeltext = folder.getName();
                        labeltext = labeltext.replace("topics/", "");
                        JLabel txtLabel = new JLabel(labeltext);
                        txtLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                        txtLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

                        row.add(imgLabel, BorderLayout.WEST);
                        row.add(txtLabel, BorderLayout.CENTER);
                        row.add(new MyButtons(window).setDialogButton("Choose", "choose " + labeltext), BorderLayout.EAST);
                        row.setBorder(BorderFactory.createLineBorder(Color.black));

                        selectionPanel.add(row);

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(titlePanel, "An error occurred in reading images: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        
                    }
                    

                }
            }
        }
        // add a scrollbar for easier browsing
        JScrollPane scrollPane = new JScrollPane(selectionPanel);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        scrollPane.setBackground(Color.LIGHT_GRAY);

        topicSelection.add(titlePanel);
        topicSelection.add(scrollPane);

        // add a cancel button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(new MyButtons(window).setDialogButton("Cancel", "close"), BorderLayout.EAST);
        topicSelection.add(buttonPanel);

        return topicSelection;
    }

    public File setTopicImage(String topicName, JFrame window) {
        String imgNamePng = topicName + ".png";
        String imgNameJpg = topicName + ".jpg";
        File imgFile = new File("topics/" + topicName + "/" +imgNamePng);
        if (!imgFile.exists()) {
            imgFile = new File("topics/" + topicName + "/" +imgNameJpg);
        }
        if (!imgFile.exists()) {
            //If there is no topic image, create one.
            BufferedImage newImage = new BufferedImage(100, 80, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = newImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 130, 80);
            g2d.dispose();
            g2d = newImage.createGraphics();
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.setColor(Color.BLACK);
            g2d.drawString(topicName, 5, 45);
            g2d.dispose();
            imgFile = new File("topics/" + topicName + "/" +imgNameJpg);
            try {
                ImageIO.write(newImage, "jpg", imgFile);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(window, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return imgFile;
    }

    public boolean startNewRank(JFrame window) {
        //TODO: Kysy lupa uuden rankingin aloittamiseen
        return true;
    }

    public static boolean importFiles(JFrame window) {
        JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Select Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
                File sourceFolder = chooser.getSelectedFile();
                File destinationFolder = new File("topics/" + sourceFolder.getName());
                return copyFolder(sourceFolder, destinationFolder, window);
            } 
        return false;
    }

    /**
     * Imports images to topics folder.
     * Creates a folder for new topic and copies JPG and PNG files there from the source folder.
     * @param sourceFolder the folder the user wants to import
     * @param destinationFolder the folder where images are copied
     * @param window the main window
     * @return true if succeeded, false otherwise
     */
    private static boolean copyFolder(File sourceFolder, File destinationFolder, JFrame window) {

        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }
        
        File[] files = sourceFolder.listFiles();

        for (File file : files) {
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
                           
                FileInputStream inputStream;
                try {
                    inputStream = new FileInputStream(file);
                    FileOutputStream outputStream = new FileOutputStream(new File(destinationFolder, file.getName()));
                    
                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    
                    inputStream.close();
                    outputStream.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(window, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }
}
