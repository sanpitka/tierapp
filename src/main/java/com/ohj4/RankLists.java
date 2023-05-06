package com.ohj4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

/**
 * Contains all the logic in getting ranking topics and ranking pictures of a specific topic.
 * 
 */
public class RankLists {

    private JSONArray rankingResults = new JSONArray();
    private int index = 0;
    private JLabel pictureLabel = new JLabel();
    
    public JSONArray getTopicList() {

        // TODO no need for this 
        /* 
        JSONArray topicList = new JSONArray();
        String topicListPath = "topics.json";
        
        // get the content of "topics" folder
        try {

            File topicListFile = new File(topicListPath);
                
            if (!topicListFile.exists()) {
                // if not found, create an new topics.json file with an empty JSONArray
                FileOutputStream fileOutputStream = new FileOutputStream(topicListPath);
                fileOutputStream.write(topicList.toString().getBytes());
                fileOutputStream.close();
            }

            // read the topic list file content to JSONArray
            String fileContent = Files.readString(Paths.get("topics.json"), StandardCharsets.UTF_8);
            topicList = new JSONArray(fileContent); 

        } catch (Exception e) {
            // TODO make this an error dialog for the user
            System.out.println("Error getting topics.json list: " + e);
        }
        return topicList;

        */
        return null;
    }

    public JSONArray getImportTopics() {
        
        // TODO remake this to add topic from user set files

        JSONArray importList = new JSONArray(); // list of topics that are new
        /* JSONArray topicList = new JSONArray(); // list of topics that exist in the list
        String folderpath = "topics/";
        String topicListPath = "topics.json";
        File folder = new File(folderpath);
        
        try {
            
            if (folder.exists() && folder.isDirectory()) {
                
                File[] foldernames = folder.listFiles(); // get the content of "topics" folder
                File topicListFile = new File(topicListPath); // get the topics.json-file
                
                if (!topicListFile.exists()) {
                    // if not found, create an new topics.json file with an empty JSONArray
                    FileOutputStream fileOutputStream = new FileOutputStream(topicListPath);
                    fileOutputStream.write(new JSONArray().toString().getBytes());
                    fileOutputStream.close();
                }

                // read the topic list file content to JSONArray
                String fileContent = Files.readString(Paths.get(topicListPath), StandardCharsets.UTF_8);
                topicList = new JSONArray(fileContent);

                // compare the topics.json file with the 'topics/' folder content
                for (File foldername: foldernames) {

                    JSONObject topic = new JSONObject();
                    topic.put("name", foldername.getName());

                    // search the topic from the topic list file content
                    JSONObject searchResult = searchForObject(topicList, topic);

                    // topic was not found in the topic list file content, mark it in the importList
                    if (searchResult == null) {
                        importList.put(foldername.getName());
                    }
                    else {
                        // topic was found, compare all the files in the topic folder to the topics.json.
                        // if a file is new, import it automatically
                        topic = searchResult;
                        String topicpath = folderpath + foldername.getName() + "/";
                        File topicfile = new File(topicpath);
                        File[] topicfiles = topicfile.listFiles();

                        if (topicfiles != null) {

                            JSONArray fileList = topic.getJSONArray("files");

                            for (File file: topicfiles) {                     
                                if (!searchForFile(topic, file.getName())) {
                                    fileList.put(file.getName());
                                }
                            }

                            topic.put("files", fileList); // add new files to filelist

                            // clean files that don't exist from the topics file list to prevent errors
                            topic = cleanFileList(topic);

                            // replace the topic object with the cleaned one.
                            // index is added to the object when searchForObject()-method finds it in list
                            int i = topic.getInt("index");
                            topic.remove("index"); // it's now safe to remove the index
                            topicList.getJSONObject(i).remove("index");
                            topicList.put(i, topic);
                        }

                        // update topics.json file with the new files
                        FileOutputStream fileOutputStream = new FileOutputStream(topicListPath);
                        fileOutputStream.write(topicList.toString().getBytes());
                        fileOutputStream.close();

                    }

                }
            } else {
                // no topics-folder found, create the folder
                folder.mkdir();
            }


        } catch (Exception e) {
            // TODO make this an error dialog for the user
            System.out.print("Error in topics folder or list: ");
            e.printStackTrace();
        } */

        return importList;
    }

    public boolean importTopics(JSONArray importList) {

        // TODO remake this to get files from user and copy them to topics folder

        
        return false;
        
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
                    String topicName = folder.getName();
                    String imgNamePng = topicName + ".png";
                    String imgNameJpg = topicName + ".jpg";
                    File imgFile = new File(folder + "/" +imgNamePng);
                    if (!imgFile.exists()) {
                        imgFile = new File(folder + "/" +imgNameJpg);
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
                        imgFile = new File(folder + "/" +imgNameJpg);
                        try {
                            ImageIO.write(newImage, "jpg", imgFile);
                        } catch (Exception e) {
                            //TODO: ei mitään printtailuja, joku parempi virhekoodi
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
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
                        row.add(new MyButtons(window).setDialogButton("Choose", "choose " + folder), BorderLayout.EAST);
                        row.setBorder(BorderFactory.createLineBorder(Color.black));

                        selectionPanel.add(row);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
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

    public boolean startNewRank(JFrame window) {
        //TODO: Kysy lupa uuden rankingin aloittamiseen
        return true;
    }

    public JDialog rankPictures (JFrame window, String topicPath) {

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
