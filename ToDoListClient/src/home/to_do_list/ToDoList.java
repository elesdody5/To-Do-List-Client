/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Elesdody
 */
public class ToDoList {
    private int id ;
    private String title;
    private int ownerId;
    private Date deadLine;
    private Date startTime;
   private List<TaskInfo> tasksInTODOList;

    public List<TaskInfo> getTasksInTODOList() {
        return tasksInTODOList;
    }

    public void setTasksInTODOList(List<TaskInfo> tasksInTODOList) {
        this.tasksInTODOList = tasksInTODOList;
    }
    public ToDoList(int id, String title, int ownerId, Date deadLine, Date startTime, int itemId) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.deadLine = deadLine;
        this.startTime = startTime;
    }
    public ToDoList ()
    {
       tasksInTODOList=new ArrayList<TaskInfo>();
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

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public Date getDeadLine() {
        return deadLine;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void addTaskToDoList(TaskInfo task)
    {
        tasksInTODOList.add(task);
    }
}
