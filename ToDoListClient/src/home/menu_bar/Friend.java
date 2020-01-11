/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

/**
 *
 * @author Aml Sakr
 */
public class Friend {
    
    private String name ;
    private boolean online ;

    public Friend(String name, boolean online) {
        this.name = name;
        this.online = online;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    
    
}
