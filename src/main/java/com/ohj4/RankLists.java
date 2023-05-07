package com.ohj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contains all the logic in getting ranking topics and ranking pictures of a specific topic.
 * 
 */
public class RankLists {

    private static JSONArray rankingResults = new JSONArray();
    private int index = 0;
    private JLabel pictureLabel = new JLabel();
    
    public static JSONArray getRankingResults() {
        return rankingResults;
    }

    public static void clearRankingResults() {
        rankingResults = new JSONArray();
    }
    
    /**
     * The function creates a new JSON object representing a topic and adds its files to it.
     * 
     * @param topicname A string representing the name of the topic to be created.
     * @return A JSONObject containing information about a new topic, including its name and a
     * JSONArray of files associated with it. If the topic folder does not exist, it will be created.
     * If there is an error opening the topics folder, null will be returned.
     */
    public JSONArray getRankingTopic(String topicpath) {
        
        JSONArray newTopic = new JSONArray();
        String folderpath = topicpath + "/";
        File topicfolder = new File(folderpath);

        // search for files in the topic path
        try { 
            if (topicfolder.exists() && topicfolder.isDirectory()) {

                File[] filenames = topicfolder.listFiles();

                // add all filenames to the topic array
                for (File filename: filenames) {

                    // validate that the file is jp(e)g or png first, then add to list
                    if (validatePicture(filename.getPath())) {

                        // also check that the file is not the topic icon
                        String[] pathArray = topicpath.replaceAll("\\\\", "/").split("/");
                        String topicName = pathArray[1]; // get the topic folder name

                        if (filename.getName().equalsIgnoreCase(topicName + ".jpg") || filename.getName().equalsIgnoreCase(topicName + ".jpeg") || filename.getName().equalsIgnoreCase(topicName + ".png")) {

                            break;

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
     * @param filePathToTest The file path of the picture that needs to be validated.
     * @return The method is returning a boolean value, either true or false, depending on whether the
     * file at the given file path is a PNG or JPEG image. If the file is not an image or is of a
     * different image format, the method returns false.
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
     * Opens a window that shows a list of theme icons and names and lets user choose a new topic to rank. 
     * @param window the main
     * @return topic selection window
     */
    public JDialog selectTopic(JFrame window) {

        //Create a topic selection window on the main frame
        JDialog topicSelection = new JDialog();
        topicSelection.setUndecorated(true); // remove title bar
        topicSelection.setMinimumSize(new Dimension(500, 400));
        topicSelection.setMaximumSize(new Dimension(500, 400));
        topicSelection.setLayout(new FlowLayout());
        topicSelection.getContentPane().setBackground(Color.LIGHT_GRAY);
        topicSelection.setForeground(Color.BLACK);
        topicSelection.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        topicSelection.setLocationRelativeTo(window);

        //Create a 
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("Choose a topic you want to rank");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titlePanel.add(titleLabel);

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(Color.LIGHT_GRAY);

        File topicsFolder = new File("topics");
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
                        row.add(new MyButtons(window).setDialogButton("Choose", "choose " + labeltext), BorderLayout.EAST);
                        row.setBorder(BorderFactory.createLineBorder(Color.black));

                        selectionPanel.add(row);

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(titlePanel, "An error occurred in reading images: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        
                    }
                    

                }
            }
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
        buttonPanel.add(new MyButtons(window).setDialogButton("Cancel", "close"), BorderLayout.EAST);
        topicSelection.add(buttonPanel);

        return topicSelection;
    }

    public File setTopicImage(String topicName, JFrame window) {
        String imgNamePng = topicName + ".png";
        String imgNameJpg = topicName + ".jpg";
        File imgFile = new File("topics/" + topicName + "/" +imgNamePng);
        if (!imgFile.exists()) {
            imgFile = new File("topics/" + topicName + "/" +imgNameJpg);
        }
        if (!imgFile.exists()) {
            //If there is no topic image, create one.
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
            imgFile = new File("topics/" + topicName + "/" +imgNameJpg);
            try {
                ImageIO.write(newImage, "jpg", imgFile);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(window, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return imgFile;
    }

    public boolean startNewRank(JFrame window) {
        //TODO: Kysy lupa uuden rankingin aloittamiseen
        return true;
    }

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
     * Creates a folder for new topic and copies JPG and PNG files there from the source folder.
     * @param sourceFolder the folder the user wants to import
     * @param destinationFolder the folder where images are copied
     * @param window the main window
     * @return true if succeeded, false otherwise
     */
    private static boolean copyFolder(File sourceFolder, File destinationFolder, JFrame window) {

        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }
        
        File[] files = sourceFolder.listFiles();

        for (File file : files) {
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
                           
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
                    JOptionPane.showMessageDialog(window, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
     * @param window The JFrame window that the JDialog will be displayed on top of.
     * @param topicPath The file path of the folder containing the pictures to be ranked.
     * @return The method is returning a JDialog object.
     */
    public JDialog rankPictures (JFrame window, String topicPath) throws IOException{

        // get selected topic folder content
        JSONArray topicArray = getRankingTopic(topicPath);
        
        JDialog rankWindow = new JDialog();
        rankWindow.setUndecorated(true); // remove title bar

        if (topicArray != null && topicArray.length() > 0) {

            rankWindow.setMinimumSize(new Dimension(600, 500));
            rankWindow.setMaximumSize(new Dimension(600, 600));
            rankWindow.setLayout(new BorderLayout(5, 5));
            rankWindow.getContentPane().setBackground(Color.LIGHT_GRAY);
            rankWindow.setForeground(Color.BLACK);
            rankWindow.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            rankWindow.setLocationRelativeTo(window);
            
            // display first picture to rank
            // TODO scale the picture to fit
            String filename = topicArray.getJSONObject(index).getString("name");
            ImageIcon currentPicture = new ImageIcon(displayPicture(filename));
            pictureLabel = new JLabel(currentPicture);
            rankWindow.add(pictureLabel, BorderLayout.CENTER);

            // display ranking letters
            JPanel buttonRow = new JPanel();
            buttonRow.setLayout(new GridLayout(1,7));
            buttonRow.setPreferredSize(new Dimension(600,130));
            buttonRow.setBackground(Color.WHITE);
            rankWindow.add(buttonRow, BorderLayout.SOUTH);
            JSONArray labels = createLabelList();

            // create buttons individually to add ActionListener
            for (int i = 0; i < labels.length(); i++) { 
                JButton button = createRankButton(labels.getJSONObject(i).getString("rank"), Color.decode(labels.getJSONObject(i).getString("color")));
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

                        if (index < topicArray.length()) {
                            // create new picture and add it to the rankWindow
                            String filename = topicArray.getJSONObject(index).getString("name");
                            ImageIcon currentPicture = new ImageIcon(displayPicture(filename));
                            pictureLabel.setIcon(currentPicture);
                            rankWindow.add(pictureLabel, BorderLayout.CENTER);
                            
                            rankWindow.revalidate();
                            rankWindow.repaint();
                        } else {
                            // end of ranking, close rankWindow
                            index = 0;
                            rankWindow.dispose();

                            // show splash screen to tell user that ranking ended
                            JDialog ended = new StartWindow().setDialogueWindow(window, "Ranking ended!", null, null, 2);
                            ended.setVisible(true);
                            try {
                                new StartWindow().updateRows(window, rankingResults);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                        }
                    }
                });
                
                }

            } else {
                // topic has no pictures to rank
                JDialog error = new StartWindow().setDialogueWindow(window, "<html>No pictures to rank!<br>Import more pictures or choose another topic</html>", null, null, 3);
                error.setVisible(true);
                rankWindow.dispose();

            }
        
        return rankWindow;
        
    }

    /**
     * Creates the labels for ranks as buttons in the ranking window.
     * 
     * @param label The text that will be displayed on the button.
     * @param backgroundColor The background color of the button.
     * @return A JButton object is being returned.
     */
    private JButton createRankButton(String label, Color backgroundColor) {
        JButton newButton = new JButton(label);
        Font buttonFont = new Font("Arial", Font.PLAIN, 40);
        newButton.setForeground(Color.WHITE);
        newButton.setBackground(backgroundColor);
        newButton.setFont(buttonFont);
        newButton.setBorder(BorderFactory.createEmptyBorder(7,20,20,20));
        newButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newButton.setVerticalTextPosition(SwingConstants.CENTER);
        
        // Set the action command of the button to its label
        newButton.setActionCommand(label);

        return newButton;
    }

    /**
     * This Java function reads an image file from a given filepath and returns the image as an object.
     * 
     * @param filepath The filepath parameter is a String that represents the path to the image file
     * that needs to be displayed.
     * @return The method is returning an Image object. If the file exists and is not a directory, it
     * reads the image from the file and returns it. If the file does not exist or is a directory, it
     * returns null. If an exception occurs, it also returns null.
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
     * @return The method `createLabelList()` returns a `JSONArray` object containing a list of
     * `JSONObject` objects, each representing a label with a color and a rank.
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
