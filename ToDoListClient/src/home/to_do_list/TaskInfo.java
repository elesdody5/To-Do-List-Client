/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sara
 */
public class TaskInfo extends Observable{

    private int id;
    private int listId;
    private String title;
    private String description;
    private String deadLine;
    private String startTime;
    private String comment;
    private boolean status ;

    public TaskInfo(int id, int listId, String title, String description, String deadLine, String startTime,String comment) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.startTime = startTime;
        this.comment = comment;

    }

    public TaskInfo(String title,int listId) {
        this.listId=listId;
        this.title = title;
    }

    TaskInfo() {

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

    public String getDeadLine() {
        return deadLine;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
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

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public JSONObject writeTaskInfoObjectAsJson() {
        JSONObject toDoTaskJsonObject = new JSONObject();
        try {
            toDoTaskJsonObject.put("title", this.getTitle());
            toDoTaskJsonObject.put("listId", this.listId);
            toDoTaskJsonObject.put("description", description);
            toDoTaskJsonObject.put("deadLine", deadLine);
            toDoTaskJsonObject.put("startTime", startTime);
            toDoTaskJsonObject.put("comment", comment);
            toDoTaskJsonObject.put("id", id);


            
        } catch (JSONException ex) {
            Logger.getLogger(TaskInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toDoTaskJsonObject;

    }

}
