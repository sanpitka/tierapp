package com.ohj4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class StartWindow implements Runnable {

    JFrame window;
    static JLabel categoryLabel;
    private static JSONArray selectionChoices = new JSONArray();
    private static JPanel rowPanel = (JPanel)setRows();

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
        window.add(rowPanel, BorderLayout.CENTER);

        JDialog topicSelection = new RankLists().selectTopic(window);
        topicSelection.setName("selection");
        topicSelection.setVisible(true);

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
        northPanel.setPreferredSize(new Dimension(800, 90));
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

        JButton screenshotbutton = new MyButtons(window).setNorthButton("<html>Take<br>Screenshot</html>", Color.BLACK, "screenshot");
        northPanelRight.add(screenshotbutton);
        
        categoryLabel = new JLabel();
        northPanelRight.add(categoryLabel);

        northPanel.add(northPanelRight);
        
        return northPanel;
    }

    /**
     * Sets the rows in the screen, where the ranked pictures go
     */
    private static Component setRows() {

        JPanel rows = new JPanel();
        //rows.setPreferredSize(new Dimension(570, 600));
        rows.setLayout(new GridLayout(7,1));
        rows.setBackground(new Color(184, 184, 184));

        Color lightGray = new Color(217, 217, 217);
        Color darkGray = new Color(184, 184, 184);

        String[] rowCounter = {"S", "A", "B", "C", "D", "E", "F"};

        for (int i=0; i < rowCounter.length; i++) {
            JPanel newRow = new JPanel();
            newRow.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            newRow.setBackground(i % 2 == 0 ? lightGray : darkGray);
            newRow.setName(rowCounter[i]);           
            rows.add(newRow);

        }

        return rows;
 
    }

    /**
     * This function updates the rows of a JFrame with images based on the results of a JSON array.
     * 
     * @param owner A JFrame object that represents the parent frame of the GUI component where the
     * rows will be updated.
     * @param results A JSONArray containing the results of a ranking process, with each element being
     * a JSONObject representing an image and its rank.
     */
    public void updateRows(JFrame owner, JSONArray results) {

        // add the ranked pictures to the correct row, if the ranking has ended
        if (results != null && results.length() > 0) {

            // for each row, collect all the images that go in that row
            String[] rowCounter = {"S", "A", "B", "C", "D", "E", "F"};

            for (int row = 0; row < rowCounter.length; row++) {

                JPanel rankRow = (JPanel)findComponentByName(owner, rowCounter[row]); // find the correct rank row
                int col = 0; // column counter for row cell increment

                // go through the results
                for (int res = 0; res < results.length(); res++) {
                    JSONObject result = results.getJSONObject(res);

                    if (result.has("rank") && result.getString("rank").equalsIgnoreCase(rowCounter[row])) {
                        // result rank matches the row we're in, add picture to row
                        
                        int height = rankRow.getHeight(); // set the image and image label height to the row height
                        ImageIcon imageIcon = new ImageIcon(result.getString("name")); // get the original image
                        Image image = imageIcon.getImage();  // get the Image object

                        // scale the image
                        ImageIcon scaledIcon = resizePicture(image, height);
                        JLabel imgLabel = new JLabel(scaledIcon);
                        imgLabel.setSize(new Dimension(scaledIcon.getIconWidth(), height)); // force the JLabel size to the same as the image

                        // add the image to row, column depending on how many pictures there already are
                        rankRow.add(imgLabel, col++);
                        rankRow.validate();
                        rankRow.repaint();

                    }
                }

            }

      
        } 
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
        JMenuItem menu_import = new MyButtons(window).setMenuButton("<html>Import<br>Files</html>", "import");
        JMenuItem menu_help = new MyButtons(window).setMenuButton("<html>About<br>This App</html>", "help");

        sidebarMenu.add(menu_new);
        sidebarMenu.add(menu_screenshots);
        sidebarMenu.add(menu_import);
        sidebarMenu.add(menu_help);

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
        dialogWindow.setModal(true); // set that user can't interact with other components on the screen while the dialog is open

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

            // add a filler panel to push the buttons downward
            JPanel fillerPanel = new JPanel();
            fillerPanel.setBackground(Color.LIGHT_GRAY);
            dialogWindow.add(fillerPanel);

            // add all the buttons in a row
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setBackground(Color.LIGHT_GRAY);

            for (int i = 0; i < buttonLabels.length; i++) {
                
                buttonPanel.add(new MyButtons((JFrame)dialogOwner).setDialogButton(buttonLabels[i], buttonActions[i]));

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

    public static JSONArray getSelectionList() {
        return selectionChoices;
    }

    public static void clearSelectionList() {
        selectionChoices = new JSONArray();
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
    public Component findComponentByName(Container container, String componentName) {
        
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

    /**
     * This Java function changes the category label's icon based on the given category name.
     * 
     * @param categoryName A String representing the name of the category for which the image needs to
     * be set.
     * @return A `JLabel` object is being returned.
     */
    public JLabel changeCategory(String categoryName) {
        
        File image = new RankLists().setTopicImage(categoryName, window);
        try {
            Image img = ImageIO.read(image);
            Image smallerImg = img.getScaledInstance(130, 90, Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(smallerImg);
            categoryLabel.setIcon(icon);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(window, "An I/O error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return categoryLabel;  
    }

    /**
     * This function creates a JPopupMenu that displays a manual for the Tier List App.
     * 
     * @param dialogOwner The JFrame that owns the JPopupMenu.
     * @return A JPopupMenu containing a manual for the Tier List App.
     */
    public JPopupMenu showManual(JFrame dialogOwner) {
        String manual = "<html><h1>Tier List App manual</h1>" +
        "<br>Tier List App is an easy-to-use tier list maker that allows you to " +
        "rank various subjects <br>in tier lists from the best to the worst. Take " +
        "a screenshot from your tierlist to save it <br>and show it to your friends!" +
        "<br>" + 
        "<br>Tiers" +
        "<br>S - Superb" +
        "<br>A - 2nd best grade" +
        "<br>..." +
        "<br>F - worst grade" +
        "<br>" +
        "<br><h2>Ranking</h2>" +
        "Choose a new topic by clicking the New button in Menu. If there are no topics," +
        "you can <br>import some by clicking Import Files in Menu. Choose a topic and click " +
        "the Go rank <br>button. Rank items by clicking the grade you want. If you want to " +
        "return to the previous <br>item, click undo. You can also skip an item if you like." +
        "<br>When the ranking is finished, you are able to take a look at the tier list in visual form. " +
        "<br>You take a screenshot of your tier list by clicking the Take Screenshot button. Give a " +
        "<br>name to your tier list by replacing the 'Unnamed tier list' text with a name that " +
        "<br>describes your tier list." +
        "<br><h2>Adding topics</h2>" +
        "To add a new topic, choose some images of the category " + 
        "you would like to rank and <br>save them into one folder. The " +
        "allowed formats are JPG, JPEG and PNG. If " +
        "you <br>would like to have a theme image for your category, " +
        "name it according to your folder. <br>For example the theme image " +
        "of folder Folder should be named to Folder.jpg or <br>Folder.png." +
        "<br>You can import your images into Tier List App by clicking Import " +
        "Files button in Menu <br>and choosing the folder you would like to import." +
        "<br><h2>Handling screenshots</h2>" +
        "You can have a look at your screenshots and delete them by clicking Screenshots " +
        "<br>button in Menu. If you want to share your screenshots to your friends or to " +
        "<br>social media, you'll find them in the Screenshots folder of this app.";
        

        int width = dialogOwner.getSize().width / 6 * 5 - 9;
        int height = 474;

        JPopupMenu aboutApp = new JPopupMenu();
        aboutApp.setPreferredSize(new Dimension(width, height));

        JPanel manualPanel = new JPanel();
        manualPanel.setPreferredSize(new Dimension(width, 800));
        manualPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));
        manualPanel.setBackground(new Color(184, 184, 184));
        JLabel manualLabel = new JLabel(manual);
        manualLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        manualPanel.add(manualLabel);
        JScrollPane scrollPane = new JScrollPane(manualPanel);
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setBackground(Color.LIGHT_GRAY);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        aboutApp.add(scrollPane);
        return aboutApp;
    }

    /**
     * This function resizes an image to a specified height while maintaining its aspect ratio.
     * 
     * @param targetImage The image that needs to be resized.
     * @param targetHeight The desired height of the resized image.
     * @return The method is returning an ImageIcon object that contains the scaled image.
     */
    public ImageIcon resizePicture(Image targetImage, int targetHeight) {

        double aspectRatio = (double)targetImage.getWidth(null) / (double)targetImage.getHeight(null);
        
        // calculate the target width based on the aspect ratio
        int targetWidth = (int)(targetHeight*aspectRatio);

        Image scaledImage = targetImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        return scaledIcon;
    }

}
