/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;


/**
 *
 * @author Elesdody
 */
public class ToDoList extends Observable {
    private int id ;
    private String title;
    private int ownerId;
    private String deadLine;
    private String startTime;
    private String color;
    private String description;
    private ArrayList<TaskInfo> taskes;
    private ArrayList<User> teamMateInToDoList;

    public ArrayList<User> getTeamMateInToDoList() {
        return teamMateInToDoList;
    }

   

    public ArrayList<TaskInfo> getTasksInTODOList() {
        return taskes;
    }
      public void addTaskToDoList(TaskInfo task)
    {
        taskes.add(task);
    }

    public ToDoList() {
        taskes = new ArrayList<>();
        
    }

    public ToDoList(String title, int ownerId,String startTime, String deadLine,  String color,ArrayList<TaskInfo> tasks) {
        this.title = title;
        this.ownerId = ownerId;
        this.deadLine = deadLine;
        this.startTime = startTime;
        this.color = color;
        this.taskes = tasks;
    }

    public String getColor() {
        return color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

   
    



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getStartTime() {
        return startTime;
    }
    public void notifyList(boolean deleteAll)
    {
        setChanged();
        notifyObservers(deleteAll);
    }
    
}
