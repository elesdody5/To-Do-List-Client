/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import authontication.LoginController;
import com.jfoenix.controls.JFXCheckBox;
import home.HomeController;
import home.list.FXMLListController;
import home.list.ToDoForm;
import home.menu_bar.ConnectWithLoginView_MenuBar;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ProgressBar;
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
    private static TaskInfo task;
    static boolean isEdit;
    User currntUser;
    static TaskInfo currentTask = null;
    ToDoList todo = ToDoListController.getTodoList();
    ConnectWithLoginView_MenuBar c;

    public ListAdapterOfTasksList(ListView<TaskInfo> listviewOfTasks) {
        this.listviewOfTasks = listviewOfTasks;
        c = ConnectWithLoginView_MenuBar.getInastance();
        currntUser = new User();

    }

    @Override
    protected void updateItem(TaskInfo task, boolean empty) {
        currntUser.setUserName(c.sendDataToView());
        currntUser.setId(Integer.parseInt(c.sendIdToView()));
        super.updateItem(task, empty);
        if (task != null) {
            JFXCheckBox checkbox = new JFXCheckBox();
            setGraphic(checkbox);
            setText(task.getTitle());
            checkbox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {

                try {
                    updateTaskStatus(newValue, task, checkbox);
                } catch (IOException ex) {
                    Logger.getLogger(ListAdapterOfTasksList.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(ListAdapterOfTasksList.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
            //  this.task=task;
            if (currntUser.getId() == todo.getOwnerId()) {
                setContextMenu(createContextMenu(task));
            } else {
                setContextMenu(createContextMenuOfSharedList(task));

            }

        } else {
            setText("");
            setGraphic(null);
        }

    }

    public static TaskInfo getCurrntTask() {

        return currentTask;
    }

    private ContextMenu createContextMenu(TaskInfo task) {
        ContextMenu contextMenu = new ContextMenu();

//                    System.out.println(currentTask.getId());
        MenuItem edit = new MenuItem("Edit");
        MenuItem share = new MenuItem("Assign ti teamMember");
        MenuItem delete = new MenuItem("Delete");

        contextMenu.getItems().addAll(edit, share, delete);
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
        share.setOnAction((ActionEvent event) -> {
            currentTask = task;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TeamMember.fxml"));
            Parent form = null;
            try {
                form = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ListAdapterOfTasksList.class.getName()).log(Level.SEVERE, null, ex);
            }
            TeamMemberController teamMemberController = loader.getController();

            Scene scene = new Scene(form);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.initStyle(StageStyle.UTILITY);
            stage.initOwner(getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
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
        isEdit = true;
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();

        if (task != null) {
            // to update form ui
            taskInformationViewController.setTaskView(task);
            setText(task.getTitle());

        }

        stage.setOnHidden((WindowEvent event) -> {
            setText(task.getTitle());
            isEdit = false;
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
            int response = server.delete(new String[]{"task", String.valueOf(task.getId())});
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

    private ContextMenu createContextMenuOfSharedList(TaskInfo task) {
        ContextMenu contextMenu = new ContextMenu();

//                    System.out.println(currentTask.getId());
        MenuItem edit = new MenuItem("Edit");

        contextMenu.getItems().addAll(edit);
        edit.setOnAction((ActionEvent event) -> {

            try {
                openForm(task);
            } catch (IOException ex) {
                Logger.getLogger(ListAdapterOfTasksList.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        return contextMenu;
    }

    private void updateTaskStatus(Boolean newValue, TaskInfo task, CheckBox checkBox) throws IOException, JSONException {
        ProgressBar bar = new ProgressBar();
        setGraphic(bar);
        if (newValue) {
            Server server = new Server();
            JSONObject json = createJson(task);
            json.put("status", true);
            int result = server.put(new String[]{"task", task.getId() + ""}, json);
            if (result != -1) {
                setGraphic(checkBox);
            } else {
                showAlert();
                checkBox.setSelected(false);
                setGraphic(checkBox);

            }
        } else {
             Server server = new Server();
            JSONObject json = createJson(task);
            json.put("status", false);
            int result = server.put(new String[]{"task", task.getId() + ""}, json);
            if (result != -1) {
                setGraphic(checkBox);
            } else {
                showAlert();
                checkBox.setSelected(true);
                setGraphic(checkBox);

            }
        }
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Error update task");
        alert.showAndWait();
    }

}
