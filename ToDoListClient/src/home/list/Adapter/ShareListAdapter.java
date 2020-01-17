/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list.Adapter;

import Entity.User;
import authontication.LoginController;
import home.to_do_list.ToDoList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import server_request.Server;

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
        MenuItem leave = new MenuItem("Leave");
        contextMenu.getItems().addAll(edit, share,leave);
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
        leave.setOnAction((ActionEvent event) -> {
            leaveToDo(item);
        });
        return contextMenu;
    }

    private void leaveToDo(ToDoList todo) {
        try {
            Server server = new Server();
            int response = server.delete(new String[]{"collab",todo.getId()+"",LoginController.UserId+""});
            if (response != -1) {
                        itemListView.getItems().remove(todo);
                        todoList.remove(todo);
                        showAleart(Alert.AlertType.INFORMATION, "Done ", "Leaved Succefully");
                    } else {
                        showAleart(Alert.AlertType.ERROR, "Error ", "cann't leave todo");
                    }
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
            
        }
    }
    
    
}
