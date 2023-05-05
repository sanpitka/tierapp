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

        JPopupMenu screenshotsMenu = new JPopupMenu();

        JPanel screenshotsPanel = new JPanel();
        screenshotsPanel.setPreferredSize(new Dimension(570, 600));
        screenshotsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));
        screenshotsPanel.setBackground(new Color(184, 184, 184));

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
                    screenshotsPanel.add(oneShot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        JScrollPane scrollPane = new JScrollPane(screenshotsPanel);
        scrollPane.setPreferredSize(new Dimension(570, 600));
        scrollPane.setBackground(Color.LIGHT_GRAY);

        screenshotsMenu.add(scrollPane);

        return screenshotsMenu;
    }

    /**
     * Takes a screenshot of the list that is showing on the screen.
     * @param window the main window of the application
     */
    public void takeScreenshot(JFrame window) {
        Point upleft = window.getLocationOnScreen();

        //Create a folder for screenshots. Returns false if the folder already exists.
        new File("Screenshots").mkdir();

        // Create a Rectangle object from the application window and take a screenshot
        Rectangle rectangle = new Rectangle(upleft.x + 10, upleft.y, 785, 590);
        try {
            BufferedImage screenshot = new Robot().createScreenCapture(rectangle);
            ImageIO.write(screenshot, "png", new File("Screenshots/" + setFilename()));
            Component ssMessage = new StartWindow().setDialogueWindow(window, "<html>" + "Screenshot saved" + 
                "<br>" + "to the Screenshots folder!" + "</html>", null, null, 1);
            ssMessage.setVisible(true);
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(window, "There was an exception, try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
    }

    /**
     * Set filename of the screenshot to the list name given by user. If the user has not
     * changed the list name, set filename to capture+datetime.
     * @return filename
     */
    public String setFilename() {
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

    /**
     * Open a screenshot for view. Let the user share or delete screenshot.
     * @param dialogOwner the application window
     * @param filename open command with the name of the file 
     * @return JDialog window with image and Delete and Close buttons
     */
    public Component openScreenshot(JFrame dialogOwner, String filename) {
        filename = filename.replace("open ", "");
        File image = new File("Screenshots/" + filename);

        //Create a new JDialog panel
        JDialog openedShot = new JDialog();
        openedShot.setUndecorated(true); // remove title bar
        openedShot.setMinimumSize(new Dimension(450, 200));
        openedShot.setMaximumSize(new Dimension(450, 500));
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
            JOptionPane.showMessageDialog(dialogOwner, "There was an error in I/O.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // add buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        JButton deleteButton = new MyButtons(window).setDialogButton("Delete", "delete " + filename);
        JButton backButton = new MyButtons(window).setDialogButton("Close", "close");
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        openedShot.add(buttonPanel, BorderLayout.SOUTH);

        openedShot.pack();

        return openedShot;
    }

    /**
     * Delete a chosen screenshot file
     * @param dialog the current JDialog panel
     * @param filename delete command and the name of the file that will be deleted
     * @return true if deletion succeeded or file does not exist
     */
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
