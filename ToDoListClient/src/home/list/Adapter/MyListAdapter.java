/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list.Adapter;

import home.list.Adapter.Adapter;
import Entity.User;
import home.to_do_list.ToDoList;
import java.util.ArrayList;
import javafx.scene.control.ListView;

/**
 *
 * @author Elesdody
 */
public class MyListAdapter extends Adapter {
   

    public MyListAdapter(ListView<ToDoList> itemListView,ArrayList<User> friends,ArrayList<ToDoList> todoLit) {
      super(itemListView, friends,todoLit);
    }
   
}
