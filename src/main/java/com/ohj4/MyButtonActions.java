package com.ohj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * The class MyButtonActions implements ActionListener and defines actions for different button
 * commands.
 */
public class MyButtonActions implements ActionListener {

    JFrame window;
    static String tiername;

    public MyButtonActions(JFrame window) {
        this.window = window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // TODO add button actions here
        String command = e.getActionCommand();
        if (command == "menu") {
            // menu button pressed
            System.out.println(command + " pressed.");
        } else if (command == "rank") {
            // 'go rank' pressed
            System.out.println(command + " pressed.");
        } else if (command == "screenshot") {
            // 'take screenshot' pressed
            Point upleft = window.getLocationOnScreen();

            //Create a folder for screenshots. Returns false if the folder already exists.
            new File("Screenshots").mkdir();

            // Create a Rectangle object from the application window and take a screenshot
            Rectangle rectangle = new Rectangle(upleft.x + 10, upleft.y, 785, 590);
            try {
                BufferedImage screenshot = new Robot().createScreenCapture(rectangle);
                ImageIO.write(screenshot, "png", new File("Screenshots/" + setFilename()));
                //TODO: Näytä "Screenshot saved" -ilmoitus

            } catch (Exception e1) {
                // TODO Näytä virheviestiruutu
                e1.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }                
    }
    
    public String setFilename() {
        //If the user has not changed the list name, set filename to capture+datetime
            if (tiername == null) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
            LocalDateTime ldt = LocalDateTime.now();
            String datetime = ldt.format(formatter);
            tiername = "capture" + datetime;
        }
        tiername = tiername + ".png";
        return tiername;
    }

    public static void setListname(String listName) {
        tiername = listName;
    }
}


    

