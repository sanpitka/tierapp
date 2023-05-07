package com.ohj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * The class MyButtonActions implements ActionListener and defines actions for different button
 * commands.
 */
public class MyButtonActions implements ActionListener {

    JFrame window;
    JDialog rankingWindow;
    static String category = "";

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
        

        if (command.startsWith("choose")) {
            category = command.replace("choose ", "");
            Component source = (Component) e.getSource();
            JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(source);
            dialog.dispose();
            new StartWindow().changeCategory(category);


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
        
        } else if (command == "help") {
            JPopupMenu aboutApp = new StartWindow().showManual(window);
            aboutApp.show(window.getComponentAt(0, 0), window.getSize().width / 6 + 4, 119);

        } else if (command == "import") {

            String importInfoMessage = "<html>To import new topic:" +
                "<br><br>Choose a folder you would like to import." +
                "<br>A folder and JPG, JPEG and PNG files inside the folder" +
                "<br>are imported into Topics folder." +
                "<br><br>To set a theme image for topic, name the theme image" +
                "<br>according to the folder: for example the theme image of " +
                "<br>the folder Folder has to be either Folder.jpg or Folder.png.";

            String importSuccessMessage = "<html>New topic imported successfully!" +
                "<br>Choose Menu -> New to open your new topic.";

            JOptionPane.showMessageDialog(window, importInfoMessage, "Import new topics", JOptionPane.INFORMATION_MESSAGE);
            if (RankLists.importFiles(window)) {
                JOptionPane.showMessageDialog(window, importSuccessMessage, "Topic imported", JOptionPane.INFORMATION_MESSAGE);
            }
        
        } else if (command == "menu") {

            JPopupMenu popupMenu = new StartWindow().setSidebarMenu(window);
            popupMenu.setName("popupmenu");
            
            // Get the menu button lower left corner coordinates for the sidebar menu
            JButton button = (JButton) e.getSource();
            Dimension buttonSize = button.getSize();
            int y = buttonSize.height;
            popupMenu.setPreferredSize(new Dimension((window.getSize().width / 6 - 2), 478));
            popupMenu.show(button, 0, y);

            

        } else if (command.startsWith("new")) {

            if (command == "new") {

                // ask for confirmation
                new RankLists().startNewRank(window);
                
            } else if (command == "newconfirm") {

                // user has confirmed to select new topic
                // close the dialog window
                Component source = (Component) e.getSource();
                JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(source);
                dialog.dispose();
                
                RankLists.clearRankingResults(window); // clear the ranking results
                new StartWindow().updateRows(window, null);
                JDialog topicSelection = new RankLists().selectTopic(this.window);
                topicSelection.setName("selection");
                topicSelection.setVisible(true);
            }
            
        } else if (command.startsWith("open")) {
            Component openedShot = new Screenshots().openScreenshot(window, command);
            openedShot.setVisible(true);

        } else if (command == "rank") {
            // 'go rank' pressed
            // open the ranking dialog with the chosen topic
            if (category != null && !category.isEmpty()) {
                JDialog rankWindow = new RankLists().rankPictures(window, category);
                rankWindow.setVisible(true);
            }


        } else if (command == "screenshot") {
            // 'screenshot' pressed
            new Screenshots().takeScreenshot(window);

        } else if (command == "screenshots") {
            JPopupMenu screenshots = new Screenshots().showScreenshots(window);
            screenshots.setName("screenshots");
            screenshots.show(window.getComponentAt(0, 0), window.getSize().width / 6 + 4, 119);

        }  else {
            System.out.println("Unimplemented method " + command);
        }                
    }

    public static void setListname(String listName) {
        Screenshots.filename = listName;
    }
    
}


    

