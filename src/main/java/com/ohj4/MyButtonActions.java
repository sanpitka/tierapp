package com.ohj4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        } else {
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }                
    }       
}

    

