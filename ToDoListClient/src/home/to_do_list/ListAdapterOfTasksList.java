/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import authontication.LoginController;
import com.jfoenix.controls.JFXCheckBox;
import home.list.FXMLListController;
import home.list.ToDoForm;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 *
 * @author sara
 */
public class ListAdapterOfTasksList extends ListCell<TaskInfo> {

   private ListView<TaskInfo> listviewOfTasks;
   private TaskInfo task;
   static boolean isEdit;
    public ListAdapterOfTasksList(ListView<TaskInfo> listviewOfTasks) {
        this.listviewOfTasks = listviewOfTasks;
    }

    @Override
    protected void updateItem(TaskInfo task, boolean empty) {
        super.updateItem(task, empty);
        if (task != null) {
            JFXCheckBox checkbox = new JFXCheckBox();
            setGraphic(checkbox);
            setText(task.getTitle());
          //  this.task=task;
            setContextMenu(createContextMenu(task));

        }
        else
        { setText("");
        setGraphic(null);
        }

    }
private   TaskInfo getCurrntTask()
{
     task = listviewOfTasks.getSelectionModel().getSelectedItem();
    return task;
}
    private ContextMenu createContextMenu(TaskInfo task) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit, delete);
        edit.setOnAction((ActionEvent event) -> {

            try {
                openForm(task);
            } catch (IOException ex) {
                Logger.getLogger(ListAdapterOfTasksList.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        delete.setOnAction((ActionEvent event) -> {
            delete(task);
        });

        return contextMenu;
    }

    private void openForm(TaskInfo task) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("taskInformationView.fxml"));
        Parent form = loader.load();
        TaskInformationViewController taskInformationViewController = loader.getController();
        taskInformationViewController.setTask(task);
        Scene scene = new Scene(form);
        Stage stage = new Stage();
        stage.setScene(scene);
        isEdit=true;
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        
        if (task != null) {
            // to update form ui
            taskInformationViewController.setTaskView(task);
        }

        stage.setOnHidden((WindowEvent event) -> {
            
        });
    }

    private void showAleart(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private int updateInServer(JSONObject json) {

        try {
            Server server = new Server();
            int response = server.put(new String[]{"task"}, json);
            return response;
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
            return -1;
        }

    }

    private void delete(TaskInfo task) {
        try {
            Server server = new Server();
            int response = server.delete(new String[]{"task",String.valueOf(task.getId())});
            if (response != -1) {
                listviewOfTasks.getItems().remove(task);

                showAleart(Alert.AlertType.INFORMATION, "Done ", "deleted Succefully");
            } else {
                showAleart(Alert.AlertType.ERROR, "Error ", "cann't delete todo");
            }
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");

        }
    }

    private JSONObject createJson(TaskInfo task) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("ID", task.getId());
        json.put("TodoId", task.getListId());
        json.put("Title", task.getTitle());
        json.put("StartTime", task.getStartTime());
        json.put("DeadLine", task.getDeadLine());
        json.put("Descreption", task.getDescription());
        json.put("Comment", task.getComment());

        return json;
    }
     static boolean isEdited() {
        return isEdit;
    }


}
