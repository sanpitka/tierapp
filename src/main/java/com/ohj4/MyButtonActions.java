package com.ohj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The class MyButtonActions implements ActionListener and defines actions for different button
 * commands.
 */
public class MyButtonActions implements ActionListener {

    JFrame ikkuna;

    public MyButtonActions(JFrame myWindow) {
        ikkuna = myWindow;
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
            // 'go rank' pressed            
            Point upleft = ikkuna.getLocationOnScreen();
            
            // Create a Rectangle object from the bounds and take a screenshot
            Rectangle rectangle = new Rectangle(upleft.x + 10, upleft.y, 785, 590);
            try {
                BufferedImage screenshot = new Robot().createScreenCapture(rectangle);
                ImageIO.write(screenshot, "png", new File("screenshot.png"));

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } else {
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }                
    }      



    public static Rectangle getWindowPosition(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle windowRect = window.getBounds();
        int x = (int) ((screenSize.getWidth() - windowRect.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - windowRect.getHeight()) / 2);
        return new Rectangle(x, y, (int) windowRect.getWidth(), (int) windowRect.getHeight());
    }
}


    

