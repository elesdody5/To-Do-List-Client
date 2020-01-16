/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list.Adapter;

import home.list.Adapter.MyListAdapter;
import home.list.Adapter.Adapter;
import Entity.User;
import home.to_do_list.ToDoList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Elesdody
 */
public class ShareListAdapter extends Adapter {


    public ShareListAdapter(ListView<ToDoList> itemListView,ArrayList<User> friends,ArrayList<ToDoList>toDoList) {
        super(itemListView,friends,toDoList);
        
    }
     @Override
    protected void updateItem(ToDoList item, boolean empty) {
        super.updateItem(item, empty);
        if (item!=null&&!empty) {
            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("lists.png")));
            image.setFitHeight(30);
            image.setFitWidth(30);
            setGraphic(image);
            setText(item.getTitle());
           // setContextMenu(createContextMenu(item));

        }
        else
        {
            
            setGraphic(null);
            setText(null);
        }
        
    }

    @Override
    protected ContextMenu createContextMenu(ToDoList item) {
         ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        MenuItem share = new MenuItem("Share");
        contextMenu.getItems().addAll(edit, share);
        edit.setOnAction((ActionEvent event) -> {
            try {
                openForm(item);
            } catch (IOException ex) {
                Logger.getLogger(MyListAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        share.setOnAction((ActionEvent event) -> {
            try {
                openShareList(item);
            } catch (IOException ex) {
                Logger.getLogger(MyListAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return contextMenu;
    }
    
    
}
