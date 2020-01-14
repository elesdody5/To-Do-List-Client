/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

/**
 *
 * @author ghadeerelmahdy
 */
public class Notifications {

    private int id;
    private String fromUserId;
    private String toUserId;
    private String fromUsername;

    //type -> list or task
    private String type;
    //status -> accept or reject
    private String status;
    private String data;
    private String dataName;

    public void setId(int id) {
        this.id = id;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }
public String getfromUserName() {
        return fromUsername;
    }

    public void setfromUserName(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

}
