/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

/**
 *
 * @author ghadeerelmahdy
 */
public class ConnectWithLoginView_MenuBar implements MenuBarModelInterface{

    private String name="ghadeer";
    private String id="1";
    private static ConnectWithLoginView_MenuBar instance;
    private ConnectWithLoginView_MenuBar (){
    }

    //singelton

    public static ConnectWithLoginView_MenuBar getInastance() {
        if (instance == null) {
            instance = new ConnectWithLoginView_MenuBar();
        }
        return instance;
    }

    public void setUserName(String name) {
        this.name = name;
    }
     public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String sendNameToView() {
        return name;
    }
      @Override
    public String sendIdToView() {
        return id;
    }


}
