package com.ohj4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Screenshots {

    JFrame window;
    static String filename;

        
    /**
     * Shows all the screenshot files the user has taken with the application.
     * For every screenshot, an image button and the filename are shown.
     * @return A component that contains buttons to saved screenshots
     */
    public JPopupMenu showScreenshots(JFrame window) {
        JPopupMenu screenshots = new JPopupMenu();
        screenshots.setPreferredSize(new Dimension(570, 600));
        screenshots.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));
        screenshots.setBackground(new Color(184, 184, 184));
        File screenshotDir = new File("Screenshots");
        if (screenshotDir.isDirectory()) {
            for (File imgFile : screenshotDir.listFiles()) {
                try {
                    JPanel oneShot = new JPanel();
                    oneShot.setBackground(new Color(184, 184, 184));
                    oneShot.setLayout(new BorderLayout());
                    BufferedImage image = ImageIO.read(imgFile);
                    BufferedImage topicImg = image.getSubimage(653, 30, 130, 80);
                    ImageIcon icon = new ImageIcon(topicImg);
                    String filename = imgFile.getName();
                    if (filename.length() > 16) {
                        filename = filename.substring(0, 13) + "...";
                    }
                    JButton button = new JButton(icon);
                    button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                    button.addActionListener(new MyButtonActions(window));
                    button.setActionCommand("open " + imgFile.getName());
                    JLabel label = new JLabel(filename, JLabel.CENTER);

                    oneShot.add(button, BorderLayout.CENTER);
                    oneShot.add(label, BorderLayout.SOUTH);
                    screenshots.add(oneShot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return screenshots;
    }

    public void takeScreenshot(JFrame window) {
        Point upleft = window.getLocationOnScreen();

        //Create a folder for screenshots. Returns false if the folder already exists.
        new File("Screenshots").mkdir();

        // Create a Rectangle object from the application window and take a screenshot
        Rectangle rectangle = new Rectangle(upleft.x + 10, upleft.y, 785, 590);
        try {
            BufferedImage screenshot = new Robot().createScreenCapture(rectangle);
            ImageIO.write(screenshot, "png", new File("Screenshots/" + setFilename()));
            Component ssMessage = new StartWindow().setDialogueWindow(window, "Screenhot taken!", null, null, 1);
            ssMessage.setVisible(true);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public String setFilename() {
        //If the user has not changed the list name, set filename to capture+datetime
        if (filename == null || filename.startsWith("capture")) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
            LocalDateTime ldt = LocalDateTime.now();
            String datetime = ldt.format(formatter);
            filename = "capture" + datetime;
        }
        if (!filename.endsWith(".png")) {
            filename = filename + ".png";
        }
        return filename;
    }

    public Component openScreenshot(JFrame dialogOwner, String dialogText) {
        String filename = dialogText.replace("open ", "");
        File image = new File("Screenshots/" + filename);

        JDialog openedShot = new JDialog();
        openedShot.setUndecorated(true); // remove title bar
        openedShot.setMinimumSize(new Dimension(600, 200));
        openedShot.setMaximumSize(new Dimension(600, 500));
        openedShot.setLayout(new BorderLayout());
        openedShot.getContentPane().setBackground(Color.LIGHT_GRAY);
        openedShot.setForeground(Color.BLACK);
        openedShot.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        openedShot.setLocationRelativeTo(dialogOwner);

        // set window text and picture
        try {
            JPanel imgPanel = new JPanel();
            JLabel textLabel = new JLabel(filename);
            BufferedImage img;
            img = ImageIO.read(image);
            Image smallerImg = img.getScaledInstance(450, 320, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(smallerImg);
            JLabel imgLabel = new JLabel(icon);
            imgPanel.add(imgLabel);
            openedShot.add(textLabel, BorderLayout.NORTH);
            openedShot.add(imgPanel, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 50, 50));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        JButton shareButton = new MyButtons(window).setDialogButton("Share", "share");
        JButton deleteButton = new MyButtons(window).setDialogButton("Delete", "delete " + filename);
        JButton backButton = new MyButtons(window).setDialogButton("Close", "close");
        buttonPanel.add(shareButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        openedShot.add(buttonPanel, BorderLayout.CENTER);

        openedShot.pack();

        return openedShot;
    }

    public Boolean delete(JDialog dialog, String filename){
        filename = filename.replace("delete ", "");
        File file = new File("Screenshots/" + filename);
            if (file.exists()) {
                int option = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete " + filename +"?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    if (file.delete()) {
                        JOptionPane.showMessageDialog(dialog, "File deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to delete file.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "File does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return true;
            }
        return false;
    }

}
