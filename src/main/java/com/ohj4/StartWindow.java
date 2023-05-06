package com.ohj4;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

import javax.swing.event.*;

public class StartWindow implements Runnable {

    JFrame window;
    private static JSONArray selectionChoices = new JSONArray();

    @Override
    public void run() {
        window = new JFrame();
        Image logo = Toolkit.getDefaultToolkit().getImage("Logo.png");

        window.setIconImage(logo);
        window.setTitle("Tier List Maker");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setVisible(true);

        window.setLayout(new BorderLayout());
        
        window.add(setNorthPanel(), BorderLayout.NORTH);
        window.add(setWestPanel(true), BorderLayout.WEST);
        window.add(setRows(), BorderLayout.CENTER);

    }

    /** All the screen components go here */

    /**
     * Sets the rank letters on the main screen and in the ranking window.
     * 
     * @params isVertical {@code true} if panel should be vertical, {@code false} if the panel should be horizontal
     */
    private Component setWestPanel(boolean isVertical) {

        JPanel westPanel = new JPanel();
        
        if (isVertical) {
            westPanel.setLayout(new GridLayout(7,1));
            westPanel.setPreferredSize(new Dimension(130,600));
        }
        if (!isVertical) {
            westPanel.setLayout(new GridLayout(1,7));
        }
        
        westPanel.setBackground(new Color(184, 184, 184));

        Color labelRed = new Color(220,40,40);
        westPanel.add(createNewLabel("S", labelRed));

        Color buttonOrange = new Color(191,93,22);
        westPanel.add(createNewLabel("A", buttonOrange));

        Color buttonYellow = new Color(190, 179, 77);
        westPanel.add(createNewLabel("B", buttonYellow));

        Color buttonGreen = new Color(125, 142, 114);
        westPanel.add(createNewLabel("C", buttonGreen));

        Color buttonTurquoise = new Color(113,156,161);
        westPanel.add(createNewLabel("D", buttonTurquoise));

        Color buttonBlue = new Color(115, 130, 168);
        westPanel.add(createNewLabel("E", buttonBlue));

        Color buttonViolet = new Color(174, 124, 182);
        westPanel.add(createNewLabel("F", buttonViolet));

        return westPanel;
    }

    /**
     * Sets the top row of buttons and fields
     */
    private Component setNorthPanel() {
        JPanel northPanel = new JPanel(new GridLayout());
        JPanel northPanelLeft = new JPanel(new GridLayout());
        JPanel northPanelRight = new JPanel(new GridLayout());
        Color buttonPink = new Color(255, 196, 242);

        JButton menubutton = new MyButtons(window).setNorthButton("Menu", Color.BLACK, "menu");
        northPanelLeft.add(menubutton);
        
        JButton gorankbutton = new MyButtons(window).setNorthButton("Go Rank!", buttonPink, "rank");
        northPanelLeft.add(gorankbutton);

        northPanel.add(northPanelLeft);

         //Sets the list name, lets user change the name
         JTextField listNameField = new JTextField("Unnamed tier list...");
         listNameField.getDocument().addDocumentListener(new DocumentListener() {
             public void insertUpdate(DocumentEvent e) {
                 String listName = listNameField.getText();
                 MyButtonActions.setListname(listName);
             }
             public void removeUpdate(DocumentEvent e) {}
             public void changedUpdate(DocumentEvent e) {}
         });
         listNameField.setName("listname");
         Font listNameFont = new Font(listNameField.getFont().getName(),listNameField.getFont().getStyle(),20);
         listNameField.setBorder(BorderFactory.createEmptyBorder(0,20, 0, 20));
         listNameField.setFont(listNameFont);
         northPanel.add(listNameField);

        JButton screenshotbutton = new MyButtons(window).setNorthButton("<html>" + "Take" + "<br>" + "Screenshot" + "</html>", Color.BLACK, "screenshot");
        northPanelRight.add(screenshotbutton);
        
        //TODO: Change the image icon according to the user's choice
        ImageIcon category = new ImageIcon("Breakfast1.png");
        JLabel categoryLabel = new JLabel(category);
        northPanelRight.add(categoryLabel);

        northPanel.add(northPanelRight);
        
        return northPanel;
    }

    /**
     * Sets the rows in the screen, where the ranked pictures go
     */
    private Component setRows() {
        JPanel rows = new JPanel();
        rows.setPreferredSize(new Dimension(570, 600));
        rows.setLayout(new GridLayout(7,1));
        rows.setBackground(new Color(184, 184, 184));

        Color lightGray = new Color(217, 217, 217);
        Color darkGray = new Color(184, 184, 184);

        int rowCounter = 7;
        for (int i=0; i<rowCounter; i++) {
            JPanel newPanel = new JPanel();
            newPanel.setBackground(i % 2 == 0 ? lightGray : darkGray);
            
            rows.add(newPanel);
        }

        return rows;
    }


    /**
     * Sets the ranking labels in the left-most panel
     */
    private JPanel createNewLabel(String label, Color backgroundColor) {
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel(label, SwingConstants.CENTER);
        Font labelFont = new Font("Arial", Font.PLAIN, 40);
        newLabel.setForeground(Color.WHITE);
        newPanel.setBorder(BorderFactory.createEmptyBorder(7,20,20,20));
        newLabel.setFont(labelFont);
        newPanel.add(newLabel);
        newPanel.setBackground(backgroundColor);
        return newPanel;
    }    

    /**
     * Sets the sidebar menu
     */
    public JPopupMenu setSidebarMenu(JFrame window) {

        JPopupMenu sidebarMenu = new JPopupMenu();
        sidebarMenu.setLayout(new GridLayout(5, 1));
        sidebarMenu.setBackground(Color.BLACK);

        JMenuItem menu_new = new MyButtons(window).setMenuButton("New", "new");
        JMenuItem menu_screenshots = new MyButtons(window).setMenuButton("Screenshots", "screenshots");
        JMenuItem menu_import = new MyButtons(window).setMenuButton("<html>Import<br>Custom</html>", "importbutton");

        sidebarMenu.add(menu_new);
        sidebarMenu.add(menu_screenshots);
        sidebarMenu.add(menu_import);

        sidebarMenu.setBorder(null);

        return sidebarMenu;
    }
    /**
     * Sets a centered dialog window with no title bar. If no buttons are given, the window closes
     * after {@code timeout} seconds. Maximum 5 buttons.
     * <p>
     * <h4>Examples:</h4>
     * Create a 2-button window:
     * <pre><code>setDialogueWindow(mainScreen, "This is a 2 button window!", {"OK", "Cancel"}, {"ok", "cancel"}, 0)</code></pre>
     * Create a splash window that disappears after 5 seconds:
     * <pre><code>setDialogueWindow(mainScreenLowerSection, "This is a splash window!", null, null, 5)</code></pre>
     * @param dialogOwner the component that sets the relative center position of the window. The program screen is recommended.
     *                    {@code null} centers the window in the middle of computer screen.
     * @param dialogText the text to show on the dialog window
     * @param buttonLabels a String array of labels to show on the buttons. {@code buttonLabels} and {@code buttonActions} must be the same size.
     * @param buttonActions a String array of actions for each button. {@code buttonLabels} and {@code buttonActions} must be the same size.
     * @param timeout the time, after which the window disappears. Must be {@code 0} if any buttons are set. Max 10 seconds.
     * 
     * @return the set window component, {@code null} if parameters are incorrect or there are too many buttons
     */
    public JDialog setDialogueWindow(Component dialogOwner, String dialogText, String[] buttonLabels, String[] buttonActions, int timeout) {

        JDialog dialogWindow = new JDialog();
        dialogWindow.setUndecorated(true); // remove title bar

        dialogWindow.setMinimumSize(new Dimension(300, 200));
        dialogWindow.setMaximumSize(new Dimension(500, 400));

        dialogWindow.setLayout(new GridLayout(0, 1, 0, 5));
        dialogWindow.getContentPane().setBackground(Color.LIGHT_GRAY);
        dialogWindow.setForeground(Color.BLACK);
        dialogWindow.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        dialogWindow.setLocationRelativeTo(dialogOwner);

        // set window text
        JLabel windowtext = new JLabel(dialogText, SwingConstants.CENTER);
        windowtext.setFont(new Font("Arial", Font.PLAIN, 20));
        dialogWindow.add(windowtext);

        // window with buttons and no timeout. must have equal amount of labels and actions and 0 timeout
        if (buttonLabels != null && buttonActions != null && buttonLabels.length > 0 && buttonLabels.length == buttonActions.length && timeout == 0) {

            // check that there are not too many buttons
            if (buttonLabels.length > 5) {
                System.out.println("Too many buttons");
                return null;
            }

            dialogWindow.setModal(true); // set that user can't interact with other components on the screen while the dialog is open

            // add a filler panel to push the buttons downward
            JPanel fillerPanel = new JPanel();
            fillerPanel.setBackground(Color.LIGHT_GRAY);
            dialogWindow.add(fillerPanel);

            // add all the buttons in a row
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setBackground(Color.LIGHT_GRAY);

            for (int i = 0; i < buttonLabels.length; i++) {
                
                buttonPanel.add(new MyButtons(this.window).setDialogButton(buttonLabels[i], buttonActions[i]));

            }
            
            dialogWindow.add(buttonPanel);

        } 
        
        // window with no buttons and timeout. must have no buttons and 10 > timeout > 0
        else if ((buttonLabels == null || buttonLabels.length == 0) && (buttonActions == null || buttonActions.length == 0) && timeout > 0) {

            // don't allow too long timeout
            if (timeout > 10) {
                System.out.println("Timeout too long");
                return null;
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Close the dialog window
                    dialogWindow.dispose();
                    // Cancel the Timer
                    timer.cancel();
                }
            }, (long)timeout * 1000);

        } else {

            System.out.println("Incorrect window parameters");
            return null;
        }

        dialogWindow.pack(); // set the size relative to the content

        return dialogWindow;
    }

    public JDialog setImportWindow(Component dialogOwner, String dialogText) {

        // TODO remake this

        // get the list for the topics to import
        //JSONArray importList = new RankLists().getImportTopics();
        JSONArray importList = new JSONArray();
        List<String> list = new ArrayList<>();

        JDialog importWindow = new JDialog();
        importWindow.setUndecorated(true); // remove title bar
        JPanel filler = new JPanel(); // set filler to adjust layout
        filler.setBackground(Color.LIGHT_GRAY);

        importWindow.setMinimumSize(new Dimension(500, 200));
        importWindow.setMaximumSize(new Dimension(500, 500));

        importWindow.setLayout(new BorderLayout(10, 10));
        importWindow.getContentPane().setBackground(Color.LIGHT_GRAY);
        importWindow.setForeground(Color.BLACK);
        importWindow.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        importWindow.setLocationRelativeTo(dialogOwner);

        // set window text
        JLabel windowtext = new JLabel(dialogText, SwingConstants.LEFT);
        windowtext.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        windowtext.setFont(new Font("Arial", Font.PLAIN, 20));
        windowtext.setPreferredSize(new Dimension(300, 40));
        importWindow.add(windowtext, BorderLayout.NORTH);
        
        // set a list element for topics
        JPanel selectionList = new JPanel();
        selectionList.setLayout(new GridLayout(0, 1));
        selectionList.setBackground(Color.LIGHT_GRAY);

        if (importList == null || importList.length() == 0) {

            JLabel error = new JLabel("No topics to import", null, 0);
            error.setFont(new Font("Arial", Font.PLAIN, 20));
            error.setBackground(Color.LIGHT_GRAY);
            selectionList.add(error);

        } else {
            // get the topics from the JSONArray
                for (int i = 0; i < importList.length(); i++) {

                JPanel topicImportList = new JPanel(new BorderLayout(10, 10));
                topicImportList.setBackground(Color.LIGHT_GRAY);
                String topic = importList.getString(i);

                // display the topic name
                JLabel topicName = new JLabel(topic);
                topicName.setBackground(Color.LIGHT_GRAY);
                topicName.setFont(new Font("Arial", Font.PLAIN, 20));
                topicImportList.add(topicName, BorderLayout.CENTER);

                // add the checkbox
                JCheckBox selection = new JCheckBox();
                selection.setPreferredSize(new Dimension(20, 20));
                selection.setBackground(Color.LIGHT_GRAY);
                selection.setName(topic);
                // add a listener to the checkbox to see if it's selected or not
                selection.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // Checkbox was selected
                            list.add(topic);
                            selectionChoices = new JSONArray(list);
                            System.out.println(selectionChoices.toString());

                        } else {
                            // Checkbox was deselected
                            list.remove(topic);
                            selectionChoices = new JSONArray(list);
                            System.out.println(selectionChoices.toString());

                        }
                    }
                });

                topicImportList.add(selection, BorderLayout.EAST);
                selectionList.add(topicImportList);

            }
        }

        // add a scrollbar for easier browsing
        JScrollPane scrollPane = new JScrollPane(selectionList);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        scrollPane.setBackground(Color.LIGHT_GRAY);
        importWindow.add(scrollPane, BorderLayout.CENTER);

        // add a cancel button
        JPanel buttonPanel = new JPanel(new GridLayout(1, 8));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(new MyButtons(window).setDialogButton("Cancel", "cancel"));
        buttonPanel.add(filler); // add fillers to make button smaller
        buttonPanel.add(new MyButtons((JFrame)dialogOwner).setDialogButton("Import selected", "import"));
        importWindow.add(buttonPanel, BorderLayout.SOUTH);

        importWindow.pack();

        return importWindow;
    }

    public static JSONArray getSelectionList() {
        return selectionChoices;
    }

    public static void clearSelectionList() {
        selectionChoices = new JSONArray();
    }

}
