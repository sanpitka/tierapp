package com.ohj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The class MyButtonActions implements ActionListener and defines actions for different button
 * commands.
 */
public class MyButtonActions implements ActionListener {

    JFrame window;

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

            // Check if there already is a sidebarmenu component
            Component component = findComponentByName(window, "glasspane");

            // Sidebarmenu was not found, create a new one
            if (component == null) {

                Component popupMenu = new StartWindow().setSidebarMenu();

                // Create a glass pane and make it transparent to pop the menu on top of the default screen
                JPanel glassPane = new JPanel();
                glassPane.setName("glasspane");
                glassPane.setOpaque(false);
                glassPane.setLayout(null);
                
                // Get the menu button lower left corner coordinates for the sidebar menu
                JButton button = (JButton) e.getSource();
                Dimension buttonSize = button.getSize();
                int y = buttonSize.height;

                // Add the popup menu to the glass pane at a specific location
                glassPane.add(popupMenu);
                popupMenu.setBounds(0, y, (window.getSize().width / 6 - 2), 570);
                
                // Add the glass pane to the root pane of the window and set visibility
                this.window.getRootPane().setGlassPane(glassPane);
                glassPane.setVisible(true);

            } else {
                // if the sidebar menu is currently visible, hide the sidebar and vice versa
                if (component.isVisible()) {
                    component.setVisible(false);
                } else {
                    component.setVisible(true);
                }
            }


        } else if (command == "rank") {
            // 'go rank' pressed
            System.out.println(command + " pressed.");

        } else if (command == "screenshot") {
            // 'screenshot' pressed
            Screenshots shot = new Screenshots();
            shot.takeScreenshot(window);
            
        } else if (command == "screenshots") {
            System.out.println("Let's open the screenshot files!");
        
        } else {
            System.out.println("Unimplemented method " + command);
        }                
    }

    
    /**
     * Search for components inside the screen.
     * 
     * @param container A Container object that represents the container in which the component is
     * located. A Container is a component that can contain other components, such as a JFrame, JPanel,
     * or JDialog.
     * @param componentName The name of the component that we want to find within the given container.
     * @return the component or nested component, or null if the component is not found
     */
    private Component findComponentByName(Container container, String componentName) {
        
        Component[] components = container.getComponents();

        for (Component component : components) {

            if (component.getName() != null && component.getName().equals(componentName)) {
                return component; // Component found
            }

            if (component instanceof Container) {
                Component nestedComponent = findComponentByName((Container) component, componentName);
                if (nestedComponent != null) {
                    return nestedComponent; // Component found in nested container
                }
            }
        }

        return null; // Component not found

    }

    public static void setListname(String listName) {
        Screenshots.filename = listName;
    }
    
}


    

