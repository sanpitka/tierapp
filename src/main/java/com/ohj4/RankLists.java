package com.ohj4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contains all the logic in getting ranking topics and ranking pictures of a specific topic.
 * Sends information to graphical components to display.
 */
public class RankLists {
    
    public JSONArray getTopicList() {

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
    }

    public JSONArray getImportTopics() {
        
        JSONArray importList = new JSONArray(); // list of topics that are new
        JSONArray topicList = new JSONArray(); // list of topics that exist in the list
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
        }

        return importList;
    }

    public boolean importTopics(JSONArray importList) {

        String topicListPath = "topics.json";

        try {

            if (importList != null) {

                JSONArray topicList = getTopicList();

                for (int i = 0; i < importList.length(); i++) {
                    JSONObject topic = setNewTopic(importList.getString(i));
                    topicList.put(topic);
                }

                // update topics.json file
                FileOutputStream fileOutputStream = new FileOutputStream(topicListPath);
                fileOutputStream.write(topicList.toString().getBytes());
                fileOutputStream.close();
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Error importing topics " + e);
            return false;
        }
    }
    
    /**
     * The function creates a new JSON object representing a topic and adds its name and files to it,
     * creating a new folder for the topic if it doesn't already exist.
     * 
     * @param topicname A string representing the name of the new topic to be created.
     * @return A JSONObject containing information about a new topic, including its name and a
     * JSONArray of files associated with it. If the topic folder does not exist, it will be created.
     * If there is an error opening the topics folder, null will be returned.
     */
    public JSONObject setNewTopic(String topicname) {
        JSONObject newTopic = new JSONObject();
        String folderpath = "topics/" + topicname + "/";
        File topicfolder = new File(folderpath);

        newTopic.put("name", topicname);

        // search for files in the topic path
        try {

            if (!topicfolder.exists()) {
                // no folder for topic found, create the folder
                topicfolder.mkdir();
            }
            
            if (topicfolder.exists() && topicfolder.isDirectory()) {

                File[] filenames = topicfolder.listFiles();
                JSONArray files = new JSONArray();

                // add all filenames to the topic file array
                for (File file: filenames) {
                    files.put(file.getName());
                }

                newTopic.put("files", files);

            }

        } catch (Exception e) {
            System.out.println("Error opening topics folder" + e);
            return null;
        }
        
        return newTopic;

    }

    // TODO set topic ranking
    private void setTopicRanking() {

    }

    // TODO get topic ranking
    public void getTopicRanking() {

    }

    // TODO set topic icon and logo
    private void setTopicLogo() {
        

    }

    // TODO check pictures
    private boolean validatePicture() {

        return false;
    }

    /**
     * The function removes non-existent files from a topic.
     * 
     * @param topic A JSONObject representing a topic, which contains a name (String) and an array of
     * files (JSONArray).
     * @return the cleaned topic object.
     */
    private JSONObject cleanFileList(JSONObject topic) {

        String folderpath = "topics/" + topic.getString("name") + "/";
        JSONArray filelist = topic.getJSONArray("files"); 
        
        if (filelist != null && filelist.length() > 0) {

            for (int i = 0; i < filelist.length(); i++) {
                String filename = filelist.get(i).toString();
                String filepath = folderpath + filename;
                File file = new File(filepath);

                if (!file.exists()) {
                    filelist.remove(i);
                }
            }
        }

        return topic;
    }

/**
 * The function searches for a JSONObject within a JSONArray based on a matching "name" value.
 * 
 * @param topiclist a list of previously imported topics.
 * @param topic the topic to be searched in the topic list
 * @return returns the found topic if it finds a matching object in the topic list, otherwise returns `null`.
 */
    private JSONObject searchForObject(JSONArray topiclist, JSONObject topic) {

        for (int i = 0; i < topiclist.length(); i++) {

            JSONObject tempObject = topiclist.getJSONObject(i);
            String value = tempObject.getString("name");

            if (value.equalsIgnoreCase(topic.getString("name"))) {
                tempObject.put("index", i);
                return tempObject;
            }
        }

        return null;
    }

    /**
     * The function searches for a specific file in a topic, and returns true if found, otherwise
     * returns false. If there is no file list in the topic, create one and return false.
     * 
     * @param topic A JSONObject representing a topic, which may or may not have a list of files
     * associated with it.
     * @param filename The name of the file that we are searching for in the JSON object.
     * @return The method returns a boolean value, either true or false.
     */
    private boolean searchForFile(JSONObject topic, String filename) {

        if (topic.has("files")) {

            JSONArray files = topic.getJSONArray("files");

            for (int i = 0; i < files.length(); i++) {
                if (files.getString(i).equalsIgnoreCase(filename)) {
                    return true;
                }
            }

        } else {
            // topic has no file list
            topic.put("files", new JSONArray());
        }

        return false;
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
}
