package com.ohj4;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * The class MyButtonActions implements ActionListener and defines actions for different button
 * commands.
 */
public class MyButtonActions implements ActionListener {

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
            // 'go rank' pressed
            System.out.println(command + " pressed.");
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
}

    

