package com.ohj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contains all the logic in getting ranking topics and ranking pictures of a
 * specific topic.
 * 
 */
public class RankLists {

    private static JSONArray rankingResults = new JSONArray();
    private int index = 0;
    private JLabel pictureLabel = new JLabel();

    public static JSONArray getRankingResults() {
        return rankingResults;
    }

    public static void clearRankingResults(JFrame owner) {
        rankingResults = new JSONArray();

        // clear the screen from pitures
        String[] rowCounter = { "S", "A", "B", "C", "D", "E", "F" };
        for (int i = 0; i < rowCounter.length; i++) {
            // get the row component for a rank
            JPanel row = (JPanel) new StartWindow().findComponentByName(owner, rowCounter[i]);
            if (row != null) {
                row.removeAll(); // clear all components inside the row component
                row.validate();
                row.repaint();
            }
        }
    }

    /**
     * The function creates a new JSON object representing a topic and adds its
     * files to it.
     * 
     * @param topicname A string representing the name of the topic to be created.
     * @return A JSONObject containing information about a new topic, including its
     *         name and a
     *         JSONArray of files associated with it. If the topic folder does not
     *         exist, it will be created.
     *         If there is an error opening the topics folder, null will be
     *         returned.
     */
    public JSONArray getRankingTopic(String topicpath) {

        JSONArray newTopic = new JSONArray();
        String folderpath = "topics/" + topicpath + "/";
        File topicfolder = new File(folderpath);

        // search for files in the topic path
        try {
            if (topicfolder.exists() && topicfolder.isDirectory()) {

                File[] filenames = topicfolder.listFiles();

                // add all filenames to the topic array
                for (File filename : filenames) {

                    // validate that the file is jp(e)g or png first, then add to list
                    if (validatePicture(filename.getPath())) {

                        // also check that the file is not the topic icon
                        if (filename.getName().equalsIgnoreCase(topicpath + ".jpg")
                                || filename.getName().equalsIgnoreCase(topicpath + ".jpeg")
                                || filename.getName().equalsIgnoreCase(topicpath + ".png")) {
                            continue;

                        } else {
                            JSONObject file = new JSONObject();
                            file.put("name", filename.getPath().replaceAll("\\\\", "/"));
                            file.put("rank", "");
                            newTopic.put(file);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error opening topics folder " + e);
            return null;
        }

        return newTopic;

    }

    /**
     * The function validates if a file path is a PNG or JPEG image.
     * 
     * @param filePathToTest The file path of the picture that needs to be
     *                       validated.
     * @return The method is returning a boolean value, either true or false,
     *         depending on whether the
     *         file at the given file path is a PNG or JPEG image. If the file is
     *         not an image or is of a
     *         different image format, the method returns false.
     */
    private boolean validatePicture(String filePathToTest) throws IOException {

        Path path = Paths.get(filePathToTest);
        String mimeType = Files.probeContentType(path);

        if (mimeType != null) {
            if (mimeType.equals("image/png")) {
                return true;
            } else if (mimeType.equals("image/jpeg")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Opens a window that shows a list of theme icons and names and lets user
     * choose a new topic to rank.
     * 
     * @param window the main
     * @return topic selection window
     */
    public JDialog selectTopic(JFrame window) {

        // Create a topic selection window on the main frame
        JDialog topicSelection = new JDialog();
        topicSelection.setUndecorated(true); // remove title bar
        topicSelection.setMinimumSize(new Dimension(500, 400));
        topicSelection.setMaximumSize(new Dimension(500, 400));
        topicSelection.setLayout(new FlowLayout());
        topicSelection.getContentPane().setBackground(Color.LIGHT_GRAY);
        topicSelection.setForeground(Color.BLACK);
        topicSelection.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        topicSelection.setLocationRelativeTo(window);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("Choose a topic you want to rank");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titlePanel.add(titleLabel);

        JPanel selectionPanel = new JPanel();
        String closeButton = "Cancel";
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(Color.LIGHT_GRAY);

        File topicsFolder = new File("topics");
        int topics = 0;
        if (topicsFolder.isDirectory()) {
            for (File folder : topicsFolder.listFiles()) {
                if (folder.isDirectory()) {
                    File imgFile = setTopicImage(folder.getName(), window);
                    JPanel row = new JPanel(new BorderLayout());
                    try {
                        BufferedImage topicImage = ImageIO.read(imgFile);
                        Image scaledImg = topicImage.getScaledInstance(100, 80, 0);
                        JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
                        String labeltext = folder.getName();
                        labeltext = labeltext.replace("topics/", "");
                        JLabel txtLabel = new JLabel(labeltext);
                        txtLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                        txtLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

                        row.add(imgLabel, BorderLayout.WEST);
                        row.add(txtLabel, BorderLayout.CENTER);
                        row.add(new MyButtons(window).setDialogButton("Choose", "choose " + labeltext),
                                BorderLayout.EAST);
                        row.setBorder(BorderFactory.createLineBorder(Color.black));

                        selectionPanel.add(row);
                        topics++;

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(titlePanel,
                                "An error occurred in reading images: " + e.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        }
        if (topics == 0) {
            JLabel emptyFolderLabel = new JLabel("<html>There are no topics to rank yet. <br>Go import some!" +
                    "<br><br>You can import topics by clicking the Import Files <br>button in Menu.</html>");
            emptyFolderLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            selectionPanel.add(emptyFolderLabel);
            closeButton = "OK";
        }

        // add a scrollbar for easier browsing
        JScrollPane scrollPane = new JScrollPane(selectionPanel);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        scrollPane.setBackground(Color.LIGHT_GRAY);

        topicSelection.add(titlePanel);
        topicSelection.add(scrollPane);

        // add a cancel button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(new MyButtons(window).setDialogButton(closeButton, "close"), BorderLayout.EAST);
        topicSelection.add(buttonPanel);

        return topicSelection;
    }

    /**
     * This function sets the topic image for a given topic name, and if no image
     * exists, it creates a
     * new one with the topic name as the text.
     * 
     * @param topicName A String representing the name of the topic for which an
     *                  image is being set or
     *                  created.
     * @param window    A JFrame object representing the window in which any error
     *                  messages will be
     *                  displayed.
     * @return The method is returning a File object.
     */
    public File setTopicImage(String topicName, JFrame window) {
        String imgNamePng = topicName + ".png";
        String imgNameJpg = topicName + ".jpg";
        File imgFile = new File("topics/" + topicName + "/" + imgNamePng);
        if (!imgFile.exists()) {
            imgFile = new File("topics/" + topicName + "/" + imgNameJpg);
        }
        if (!imgFile.exists()) {
            // If there is no topic image, create one.
            BufferedImage newImage = new BufferedImage(100, 80, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = newImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 130, 80);
            g2d.dispose();
            g2d = newImage.createGraphics();
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.setColor(Color.BLACK);
            g2d.drawString(topicName, 5, 45);
            g2d.dispose();
            imgFile = new File("topics/" + topicName + "/" + imgNameJpg);
            try {
                ImageIO.write(newImage, "jpg", imgFile);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(window, "An error occurred: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return imgFile;
    }

    /**
     * The function displays a confirmation dialog box asking for permission to
     * start a new ranking and
     * returns a boolean value.
     * 
     * @param window A JFrame object representing the parent window in which the
     *               confirmation dialog
     *               will be displayed.
     * @return A boolean value of true is being returned.
     */
    public boolean startNewRank(JFrame window) {
        // ask for permission to start new ranking
        String[] buttonlabels = { "Cancel", "Ok" };
        String[] buttonactions = { "close", "newconfirm" };

        JDialog confirmation = new StartWindow().setDialogueWindow(window,
                "<html>Are you sure you want<br>to start a new rank?</html>", buttonlabels, buttonactions, 0);
        confirmation.setVisible(true);
        return true;
    }

    /**
     * This function allows the user to select a folder and copies its contents to a
     * destination folder
     * within the program's directory.
     * 
     * @param window The JFrame window is the graphical user interface window that
     *               the user interacts
     *               with. It is passed as a parameter to the method so that the
     *               JFileChooser dialog can be displayed
     *               on top of it.
     * @return A boolean value is being returned.
     */
    public static boolean importFiles(JFrame window) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
            File sourceFolder = chooser.getSelectedFile();
            File destinationFolder = new File("topics/" + sourceFolder.getName());
            return copyFolder(sourceFolder, destinationFolder, window);
        }
        return false;
    }

    /**
     * Imports images to topics folder.
     * Creates a folder for new topic and copies JPG and PNG files there from the
     * source folder.
     * 
     * @param sourceFolder      the folder the user wants to import
     * @param destinationFolder the folder where images are copied
     * @param window            the main window
     * @return true if succeeded, false otherwise
     */
    private static boolean copyFolder(File sourceFolder, File destinationFolder, JFrame window) {

        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }

        File[] files = sourceFolder.listFiles();

        for (File file : files) {
            if (file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".png")) {

                FileInputStream inputStream;
                try {
                    inputStream = new FileInputStream(file);
                    FileOutputStream outputStream = new FileOutputStream(new File(destinationFolder, file.getName()));

                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    inputStream.close();
                    outputStream.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(window, "An error occurred: " + e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates a dialog window for ranking pictures and allows the user to rank each
     * picture using buttons.
     * 
     * @param dialogOwner The JFrame window that the JDialog will be displayed on
     *                    top.
     * @param topicPath   The file path of the folder containing the pictures to be
     *                    ranked.
     * @return The method is returning a JDialog object.
     */
    public JDialog rankPictures(JFrame dialogOwner, String topicPath) {

        // get selected topic folder content
        JSONArray topicArray = getRankingTopic(topicPath);

        // Shuffle the topic array using Fisher-Yates shuffle method
        Random random = new Random();
        for (int i = topicArray.length() - 1; i >= 0; i--) {
            int j = random.nextInt(i + 1);
            Object object = topicArray.get(j);
            topicArray.put(j, topicArray.get(i));
            topicArray.put(i, object);
        }

        JDialog rankWindow = new JDialog();
        rankWindow.setUndecorated(true); // remove title bar
        rankWindow.setName("rankwindow");
        rankWindow.setModal(true); // don't allow interaction besides the ranking
        // Note: by setting this to modal, the use of menu button when ranking is
        // disabled.
        // If it's not set to modal, the ranking window will not close if the user
        // presses menu and selects new ranking topic

        if (topicArray != null && topicArray.length() > 0) {

            // get the width of the dialogOwner
            int width = dialogOwner.getWidth() - 15;
            int height = dialogOwner.getHeight() - 123;

            rankWindow.setLayout(new BorderLayout());
            rankWindow.setSize(new Dimension(width, height));
            rankWindow.getContentPane().setBackground(Color.LIGHT_GRAY);
            rankWindow.setForeground(Color.BLACK);
            rankWindow.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            // set location to right under the north panels
            int x = 8;
            int y = 120;
            rankWindow.setLocationRelativeTo(dialogOwner);
            rankWindow.setLocation(x, y);

            // display first picture to rank
            String filename = topicArray.getJSONObject(index).getString("name");
            ImageIcon currentPicture = new ImageIcon(displayPicture(filename));
            Image currentImage = currentPicture.getImage(); // get the image

            // scale the picture to be at max 50% of the window
            int scaleHeight = rankWindow.getHeight() / 2;
            ImageIcon scaledIcon = new StartWindow().resizePicture(currentImage, scaleHeight);

            pictureLabel = new JLabel(scaledIcon);
            pictureLabel.setSize(scaledIcon.getIconWidth(), scaledIcon.getIconHeight());
            rankWindow.add(pictureLabel, BorderLayout.CENTER);

            // set a panel for ranking letters and Undo & Skip buttons
            JPanel lowerPanel = new JPanel();
            lowerPanel.setLayout(new GridLayout(2, 1));
            lowerPanel.setBackground(Color.LIGHT_GRAY);

            // display ranking letters
            JPanel buttonRow = new JPanel();
            buttonRow.setLayout(new GridLayout(1, 7));
            buttonRow.setPreferredSize(new Dimension(600, 80));
            buttonRow.setBackground(Color.WHITE);
            lowerPanel.add(buttonRow);
            JSONArray labels = createLabelList();

            // display Undo & Skip buttons
            JPanel lowerbuttons = new JPanel();
            lowerbuttons.setBackground(Color.LIGHT_GRAY);
            JButton undo = new MyButtons(dialogOwner).setDialogButtonWithoutAction("Undo");
            JButton skip = new MyButtons(dialogOwner).setDialogButtonWithoutAction("Skip");
            // don't let undo, if the ranking has just started
            if (index == 0) {
                undo.setEnabled(false);
            }
            if (index == topicArray.length() - 1) {
                skip.setEnabled(false);
            }
            undo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent u) {
                    if (index > 0) {
                        index--;
                        if (index == 0) {
                            undo.setEnabled(false);
                        }
                        skip.setEnabled(true);
                        rankingResults.remove(index);
                        showNextItem(topicArray, scaleHeight, rankWindow, dialogOwner);
                    }
                }
            });

            skip.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent s) {
                    int lastIndex = topicArray.length() - 1;

                    JSONObject current = topicArray.getJSONObject(index);
                    JSONObject last = topicArray.getJSONObject(lastIndex);

                    JSONArray keys = current.names();

                    for (int i = 0; i < keys.length(); i++) {
                        String key = keys.getString(i);
                        Object currentValue = current.get(key);
                        Object lastValue = last.get(key);

                        current.put(key, lastValue);
                        last.put(key, currentValue);
                    }
                    showNextItem(topicArray, scaleHeight, rankWindow, dialogOwner);
                    if (index == topicArray.length() - 1) {
                        skip.setEnabled(false);
                    }
                }
            });

            lowerbuttons.add(undo);
            lowerbuttons.add(skip);
            lowerPanel.add(lowerbuttons);
            rankWindow.add(lowerPanel, BorderLayout.SOUTH);

            // create ranking letter buttons individually to add ActionListener
            for (int i = 0; i < labels.length(); i++) {
                JButton button = createRankButton(labels.getJSONObject(i).getString("rank"),
                        Color.decode(labels.getJSONObject(i).getString("color")));
                buttonRow.add(button);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent b) {

                        // record ranking for current picture
                        String ranking = ((JButton) b.getSource()).getText();
                        topicArray.getJSONObject(index).put("rank", ranking);
                        rankingResults.put(topicArray.getJSONObject(index));

                        // move to next picture
                        index++;
                        undo.setEnabled(true);
                        showNextItem(topicArray, scaleHeight, rankWindow, dialogOwner);
                        if (index == topicArray.length() - 1) {
                            skip.setEnabled(false);
                        }

                    }
                });

            }

        } else {
            // topic has no pictures to rank
            String emptyFolderMessage = "<html>No pictures to rank!<br>Import more pictures or choose another topic</html>";
            JOptionPane.showMessageDialog(dialogOwner, emptyFolderMessage, "Empty folder", JOptionPane.ERROR_MESSAGE);
            rankWindow.setModal(false);

            rankWindow.dispose();

        }

        return rankWindow;

    }

    /**
     * Shows an image of the next item to be ranked.
     * 
     * @param topicArray  the JSONArray for all the items of the chosen topic and
     *                    their grades
     * @param scaleHeight the desirable height of the image
     * @param rankWindow  the JDialog window where the ranking happens
     * @param dialogOwner The JFrame window that the JDialog will be displayed on
     *                    top.
     */
    private void showNextItem(JSONArray topicArray, int scaleHeight, JDialog rankWindow, JFrame dialogOwner) {

        if (index < topicArray.length()) {
            // create new picture and add it to the rankWindow
            String filename = topicArray.getJSONObject(index).getString("name");
            ImageIcon currentPicture = new ImageIcon(displayPicture(filename));
            Image currentImage = currentPicture.getImage();

            // scale the picture to be at max 50% of the window
            ImageIcon scaledIcon = new StartWindow().resizePicture(currentImage, scaleHeight);

            pictureLabel.setIcon(scaledIcon);
            pictureLabel.setSize(scaledIcon.getIconWidth(), scaledIcon.getIconHeight());
            pictureLabel.validate();
            pictureLabel.repaint();

            rankWindow.revalidate();
            rankWindow.repaint();

        } else {
            // end of ranking, close rankWindow
            index = 0;
            rankWindow.dispose();

            // show splash screen to tell user that ranking ended
            JDialog ended = new StartWindow().setDialogueWindow(dialogOwner, "Ranking ended!", null, null, 2);
            ended.setVisible(true);
            try {
                new StartWindow().updateRows(dialogOwner, rankingResults);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Creates the labels for ranks as buttons in the ranking window.
     * 
     * @param label           The text that will be displayed on the button.
     * @param backgroundColor The background color of the button.
     * @return A JButton object is being returned.
     */
    private JButton createRankButton(String label, Color backgroundColor) {
        JButton newButton = new JButton(label);
        Font buttonFont = new Font("Arial", Font.PLAIN, 40);
        newButton.setForeground(Color.WHITE);
        newButton.setBackground(backgroundColor);
        newButton.setFont(buttonFont);
        newButton.setBorder(BorderFactory.createEmptyBorder(7, 20, 20, 20));
        newButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newButton.setVerticalTextPosition(SwingConstants.CENTER);

        // Set the action command of the button to its label
        newButton.setActionCommand(label);

        return newButton;
    }

    /**
     * This Java function reads an image file from a given filepath and returns the
     * image as an object.
     * 
     * @param filepath The filepath parameter is a String that represents the path
     *                 to the image file
     *                 that needs to be displayed.
     * @return The method is returning an Image object. If the file exists and is
     *         not a directory, it
     *         reads the image from the file and returns it. If the file does not
     *         exist or is a directory, it
     *         returns null. If an exception occurs, it also returns null.
     */
    private Image displayPicture(String filepath) {

        try {
            File file = new File(filepath);

            if (file.exists() && !file.isDirectory()) {

                Image picture = ImageIO.read(file);
                return picture;

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method for creating label buttons.
     * 
     * @return The method `createLabelList()` returns a `JSONArray` object
     *         containing a list of
     *         `JSONObject` objects, each representing a label with a color and a
     *         rank.
     */
    private JSONArray createLabelList() {

        JSONArray labels = new JSONArray();
        JSONObject label1 = new JSONObject();
        label1.put("color", "#dc2828");
        label1.put("rank", "S");
        labels.put(label1);

        JSONObject label2 = new JSONObject();
        label2.put("color", "#bf5d16");
        label2.put("rank", "A");
        labels.put(label2);

        JSONObject label3 = new JSONObject();
        label3.put("color", "#beb34d");
        label3.put("rank", "B");
        labels.put(label3);

        JSONObject label4 = new JSONObject();
        label4.put("color", "#7d8e72");
        label4.put("rank", "C");
        labels.put(label4);

        JSONObject label5 = new JSONObject();
        label5.put("color", "#719ca1");
        label5.put("rank", "D");
        labels.put(label5);

        JSONObject label6 = new JSONObject();
        label6.put("color", "#7382a8");
        label6.put("rank", "E");
        labels.put(label6);

        JSONObject label7 = new JSONObject();
        label7.put("color", "#ae7cb6");
        label7.put("rank", "F");
        labels.put(label7);

        return labels;
    }

}
