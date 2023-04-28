package com.ohj4;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.json.JSONArray;

/**
 * The class MyButtonActions implements ActionListener and defines actions for different button
 * commands.
 */
public class MyButtonActions implements ActionListener {

    JFrame window;
    static String filename;

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

            JPopupMenu popupMenu = new StartWindow().setSidebarMenu(this.window);
            popupMenu.setName("popupmenu");
                    
            // Get the menu button lower left corner coordinates for the sidebar menu
            JButton button = (JButton) e.getSource();
            Dimension buttonSize = button.getSize();
            int y = buttonSize.height;
            popupMenu.setPreferredSize(new Dimension((window.getSize().width / 6), 470));
            popupMenu.show(button, 0, y);
            

        } else if (command == "rank") {
            // 'go rank' pressed
            System.out.println(command + " pressed.");

        } else if (command == "screenshot") {
            // 'screenshot' pressed
            Point upleft = window.getLocationOnScreen();

            //Create a folder for screenshots. Returns false if the folder already exists.
            new File("Screenshots").mkdir();

            // Create a Rectangle object from the application window and take a screenshot
            Rectangle rectangle = new Rectangle(upleft.x + 10, upleft.y, 785, 590);
            try {
                BufferedImage screenshot = new Robot().createScreenCapture(rectangle);
                ImageIO.write(screenshot, "png", new File("Screenshots/" + setFilename()));
                Component ssMessage = new StartWindow().setDialogueWindow(this.window, "Screenhot taken!", null, null, 1);
                ssMessage.setVisible(true);
                //TODO: Näytä "Screenshot saved" -ilmoitus
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
        } else if (command == "sidebarimport") {
            // TODO make this a splash screen, or get it to work otherwise
            String dialogtext = (new StringBuilder()).append("<html>Creating new topics:<p>1. Create a new folder for your topic<br>inside /topics/ folder in the root folder.<br>") 
                        .append("2. Place all objects to be tiered inside that created folder.<br>") 
                        .append("Adding new objects into an existing topic:<p>")
                        .append("Place all new objects* to be tiered inside<br>a topic folder of choice in /topics/.<br>")
                        .append("They will be imported automatically.<br>")
                        .append("*Supported object formats: png, jpeg</html>").toString();
            String[] buttonlabels = {"Cancel", "Continue to Import"};
            String[] buttonactions = {"cancel", "importbutton"};
            Component importmessage = new StartWindow().setDialogueWindow(this.window, dialogtext, buttonlabels, buttonactions, 0);
            importmessage.setName("importmessage");
            importmessage.setVisible(true);

        } else if (command == "importbutton") {
            // 'sidemenu import' pressed
            System.out.println(command + " pressed.");

            Component importdialog = new StartWindow().setImportWindow(this.window, "Custom topics to import");
            importdialog.setVisible(true);


        } else if (command == "import") {
            // 'import button' pressed
            System.out.println(command + " pressed.");

            // import chosen topics
            JSONArray importList = StartWindow.getSelectionList();
            StartWindow.clearSelectionList();

            Component source = (Component) e.getSource();
            JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(source);
            dialog.dispose();

            if (new RankLists().importTopics(importList)) {
                Component okmessage = new StartWindow().setDialogueWindow(window, "Topics imported!", null, null, 1);
                okmessage.setVisible(true);
            }

            

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

    public String setFilename() {
        //If the user has not changed the list name, set filename to capture+datetime
            if (filename == null) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
            LocalDateTime ldt = LocalDateTime.now();
            String datetime = ldt.format(formatter);
            filename = "capture" + datetime;
        }
        filename = filename + ".png";
        return filename;
    }

    public static void setListname(String listName) {
        filename = listName;
    }
    
}


    

