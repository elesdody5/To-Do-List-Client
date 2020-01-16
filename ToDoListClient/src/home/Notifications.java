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
    private int fromUserId;
    private int toUserId;
    private int dataId ;
    //type -> list or task
    private int type;

   
    //status -> accept 1 or reject 0 or noResponse -1
    private int status;
    private String dataName;
    private String fromUsername;

    public void setFromUserName(String fromUserName) {
        this.fromUsername = fromUserName;
    }

    public String getFromUserName() {
        return fromUsername;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(String data) {
        this.dataName = data;
    }

    public int getId() {
        return id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getData() {
        return dataName;
    }
     public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getDataId() {
        return dataId;
    }

}
