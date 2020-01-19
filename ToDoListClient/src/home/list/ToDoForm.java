/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list;

import home.to_do_list.ToDoList;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class ToDoForm implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextField title;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    ToDoList todo;
    @FXML
    StackPane pane;
    @FXML
    ColorPicker color;
    @FXML
    TextArea description;
    private boolean edit = false;
    private boolean save =false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        todo = new ToDoList();

    }
    
    @FXML
    private void saveStartDate(Event e) {
        if (startDate.getValue() != null) {
            if (endDate.getValue() != null) {
                if (endDate.getValue().compareTo(startDate.getValue()) < 0) {
                    
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Sorry the DeadLine must be after the start Date");
                    alert.showAndWait();
                    startDate.setValue(null);
                    return;
                }
            }
            String sDate1 = startDate.getValue().getYear() + "-" + startDate.getValue().getMonthValue() + "-" + startDate.getValue().getDayOfMonth();
            todo.setStartTime(sDate1);
            
        }
        
    }
    
    @FXML
    private void saveEndDate(Event e) {
        if (startDate.getValue() != null) {
            if (endDate.getValue() != null) {
                if (endDate.getValue().compareTo(startDate.getValue()) > 0) {
                    String sDate1 = endDate.getValue().getYear() + "-" + endDate.getValue().getMonthValue() + "-" + endDate.getValue().getDayOfMonth();
                    todo.setDeadLine(sDate1);
                    
                } else {
                    endDate.setValue(null);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Sorry the DeadLine must be after the start Date");
                    alert.showAndWait();
                }
                
            }
        } else {
            endDate.setValue(null);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter StartDate first");
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void saveData(MouseEvent e) {
        if (!title.getText().isEmpty() && startDate.getValue() != null && endDate.getValue() != null) {
            
            todo.setTitle(title.getText());
            todo.setColor(color.getValue().toString());
            todo.setDescription(description.getText());
            
            edit = true;
            save=true;
            ((Stage) pane.getScene().getWindow()).close();
            
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter todo data");
            alert.showAndWait();
        }
    }
    
    protected ToDoList getToDo() {
        return todo;
    }
    
    public void setToDo(ToDoList todo) {
        this.todo = todo;
        title.setText(todo.getTitle());
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
        startDate.setValue(LocalDate.parse(todo.getStartTime(), dtf));
        endDate.setValue(LocalDate.parse(todo.getDeadLine(), dtf));
        color.setValue(Color.valueOf(todo.getColor()));
        description.setText(todo.getDescription());
        
    }
    
   public boolean isEdited() {
        return edit;
    }
    public   boolean save()
    {
        return save;
    }
}
