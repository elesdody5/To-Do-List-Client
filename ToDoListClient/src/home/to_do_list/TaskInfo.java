/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elesdody
 */
public class TaskInfo {

    private int id;
    private int listId;
    private String title;
    private String description;
    private Date deadLine;
    private Date startTime;
    private List<String> comment;

    public TaskInfo(int id, int listId, String title, String description, Date deadLine, Date startTime, List<String> comment) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.startTime = startTime;
        this.comment = comment;

    }

    public TaskInfo(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public Date getStartTime() {
        return startTime;
    }

    public List<String> getComment() {
        return comment;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public JSONObject writeTaskInfoObjectAsJson() {
        JSONObject toDoTaskJsonObject = new JSONObject();
        try {
            toDoTaskJsonObject.put("title", this.getTitle());
        } catch (JSONException ex) {
            Logger.getLogger(TaskInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toDoTaskJsonObject;

    }

}
