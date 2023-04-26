package com.ohj4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Screenshots {

    JFrame window;
    static String filename;

        
    /**
     * Shows all the screenshot files the user has taken with the application.
     * For every screenshot, an image button and the filename are shown.
     * @return A component that contains buttons to saved screenshots
     */
    public Component showScreenshots() {
        JPanel screenshots = new JPanel();
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

    public static void open() {
        System.out.println("Avataanpa!");
    }
}
