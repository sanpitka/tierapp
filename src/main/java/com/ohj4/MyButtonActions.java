package com.ohj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.json.JSONArray;

/**
 * The class MyButtonActions implements ActionListener and defines actions for different button
 * commands.
 */
public class MyButtonActions implements ActionListener {

    JFrame window;

    public MyButtonActions(JFrame window) {
        this.window = window;
    }

    /**
     * All the button actions of this application in alfabetical order.
     * Invoked when action occurs.
     * 
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String command = e.getActionCommand();

        if (command.startsWith("choose ")) {
            // close the topic selection dialog
            Component source = (Component) e.getSource();
            JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(source);
            dialog.dispose();

            // open the ranking dialog with the chosen topic
            String target = command.replace("choose ", "");
            try {
                JDialog rankWindow = new RankLists().rankPictures(window,target);
                rankWindow.setVisible(true);
            } catch (Exception io) {
                io.printStackTrace();
            }

        } else if (command == "close") {

            // close the dialog window
            Component source = (Component) e.getSource();
            JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(source);
            dialog.dispose();

        } else if (command.startsWith("delete")) {
            Component source = (Component) e.getSource();
            JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(source);
            Boolean deleted = new Screenshots().delete(dialog, command);
            if (deleted) {
                dialog.dispose();
            }
            //TODO: Jos jää aikaa, palaa Screenshots-ikkunaan!
        
        } else if (command == "import") {
            // 'import button' pressed
            System.out.println(command + " pressed.");
            

        } else if (command == "importbutton") {
            // 'sidemenu import' pressed
            System.out.println(command + " pressed.");

            Component importdialog = new StartWindow().setImportWindow(this.window, "Custom topics to import");
            importdialog.setVisible(true);
        
        } else if (command == "menu") {
            
            JPopupMenu popupMenu = new StartWindow().setSidebarMenu(this.window);
            popupMenu.setName("popupmenu");
            
            // Get the menu button lower left corner coordinates for the sidebar menu
            JButton button = (JButton) e.getSource();
            Dimension buttonSize = button.getSize();
            int y = buttonSize.height;
            popupMenu.setPreferredSize(new Dimension((window.getSize().width / 6 - 2), 478));
            popupMenu.show(button, 0, y);

        } else if (command == "new" || command == "newconfirm") {

            if (command == "new" && RankLists.getRankingResults() != null && RankLists.getRankingResults().length() > 0) {

                // there is a ranking in progress, ask for confirmation
                new RankLists().startNewRank(window);
                
            } else if (command == "newconfirm" || (command == "new" && (RankLists.getRankingResults() == null || RankLists.getRankingResults().length() == 0))) {

                // there are no ranking results, or the user has confirmed to select new topic
                if (command == "newconfirm") {
                    // close the dialog window
                    Component source = (Component) e.getSource();
                    JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(source);
                    dialog.dispose();
                }
                RankLists.clearRankingResults(); // clear the ranking results
                new StartWindow().updateRows(window, null);
                JDialog topicSelection = new RankLists().selectTopic(this.window);
                topicSelection.setName("selection");
                topicSelection.setVisible(true);
            }
            
        } else if (command.startsWith("open")) {
            Component openedShot = new Screenshots().openScreenshot(this.window, command);
            openedShot.setVisible(true);

        } else if (command == "rank") {
            // 'go rank' pressed
            System.out.println(command + " pressed.");


        } else if (command == "screenshot") {
            // 'screenshot' pressed
            new Screenshots().takeScreenshot(window);

        } else if (command == "screenshots") {
            JPopupMenu screenshots = new Screenshots().showScreenshots(this.window);
            screenshots.setName("screenshots");
            screenshots.setPreferredSize(new Dimension((window.getSize().width / 6 * 5 - 9), 478));
            screenshots.show(window.getComponentAt(0, 0), window.getSize().width / 6 + 4, 115);
        
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

        }  else {
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


    

