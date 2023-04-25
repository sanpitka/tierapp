package com.ohj4;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contains all the logic in getting ranking topics and ranking pictures of a specific topic.
 * Sends information to graphical components to display.
 */
public class RankLists {
    

    // TODO get topic list
    public JSONArray getTopicList() {

        JSONArray topicList = new JSONArray();
        String folderpath = "topics/";
        String topicListPath = "topics.json";
        File folder = new File(folderpath);
        
        // get the content of "topics" folder
        try {

            if (folder.exists() && folder.isDirectory()) {
                
                File[] foldernames = folder.listFiles();
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

                // update the topics.json-file according to the topics-folder content
                for (File foldername: foldernames) {

                    JSONObject topic = new JSONObject();
                    topic.put("name", foldername.getName());

                    // search the topic from the topic list file content
                    JSONObject searchResult = searchForObject(topicList, topic);

                    // topic was not found in the topic list file content, add new topic
                    if (searchResult == null) {
                        topic = setNewTopic(foldername.getName());
                        topicList.put(topic);
                    }
                    else {
                        // topic was found, check that all the files in that topics folder are listed
                        // and that all files in the topics file list are present
                        topic = searchResult;
                        String topicpath = folderpath + foldername.toString() + "/";
                        File topicfile = new File(topicpath);
                        File[] topicfiles = topicfile.listFiles();

                        if (topicfiles != null) {

                            for (File file: topicfiles) {                     
                                if (!searchForFile(topic, file.getName())) {
                                    topic.put("files", file.getName());
                                }
                            }

                            // clean files that don't exist from the topics file list to prevent errors
                            topic = cleanFileList(topic);

                            // replace the topic object with the updated one.
                            // index is added to the object when searchForObject()-method finds it in list
                            int i = topic.getInt("index");
                            topic.remove("index"); // it's now safe to remove the index
                            topicList.getJSONObject(i).remove("index");
                            topicList.put(i, topic);
                        }
                    }

                    // update topics.json file
                    FileOutputStream fileOutputStream = new FileOutputStream(topicListPath);
                    fileOutputStream.write(topicList.toString().getBytes());
                    fileOutputStream.close();

                }

            } else {
                // no topics-folder found, create the folder
                folder.mkdir();
            }

        } catch (Exception e) {
            // TODO make this an error dialog for the user
            System.out.println("Error in topics folder or list:" + e);
        }
        return topicList;
    }
    
    // TODO set new topic
    public JSONObject setNewTopic(String topicname) {
        JSONObject newTopic = new JSONObject();
        String folderpath = "topics/" + topicname + "/";
        File topicfolder = new File(folderpath);

        newTopic.put("name", topicname);

        // search for files in the topic path
        try {
            
            if (topicfolder.exists() && topicfolder.isDirectory()) {

                File[] filenames = topicfolder.listFiles();
                JSONArray files = new JSONArray();

                // add all filenames to the topic file array
                for (File file: filenames) {
                    files.put(file.getName());
                }

                newTopic.put("files", files);

            }
            else {
                // no folder for topic found, create the folder
                topicfolder.mkdir();
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

    public JSONObject cleanFileList(JSONObject topic) {

        String folderpath = "topics/" + topic.getString("name") + "/";
        JSONArray filelist = topic.getJSONArray("files"); 
        
        if (filelist != null && filelist.length() > 0) {

            for (int i = 0; i < filelist.length(); i++) {
                String filename = filelist.getString(i);
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
}
