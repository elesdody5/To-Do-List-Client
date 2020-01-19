/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statistic;

import home.to_do_list.TaskInfo;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

/**
 * FXML Controller class
 *
 * @author Aml Sakr
 */
public class StatisticListController implements Initializable {

    int numberDoneTasks;
    int numberInprogressTasks;
    int numberExpiredTasks;
    ArrayList<TaskInfo> taskes;

    @FXML
    private PieChart piechart;

    public StatisticListController(int numberDoneTasks, int numberInprogressTasks, int numberExpiredTasks) {
        this.numberDoneTasks = numberDoneTasks;
        this.numberInprogressTasks = numberInprogressTasks;
        this.numberExpiredTasks = numberExpiredTasks;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO   
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Done", numberDoneTasks),
                new PieChart.Data("Inprogress", numberInprogressTasks),
                new PieChart.Data("Expired", numberExpiredTasks));
        piechart.setData(pieChartData);
        piechart.setTitle("Statistic Todo List");
        piechart.setClockwise(true);
        piechart.setLabelLineLength(50);
        piechart.setLabelsVisible(true);
        piechart.setStartAngle(180);

    }

    public void countNumber(ArrayList<TaskInfo> taskes) {
        for (int i = 0; i < taskes.size(); i++) {
            if (taskes.get(i).isStatus()) {
                numberDoneTasks++;
            } else {
                if(compareDate(taskes.get(i).getDeadLine()))
                    numberInprogressTasks++;
                else
                    numberExpiredTasks++;
            }
        }
    }

    private boolean compareDate(String deadLine) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String currentDateString = dateTimeFormatter.format(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = sdf.parse(currentDateString);
            Date deadLineDate = sdf.parse(deadLine);
            if (currentDate.compareTo(deadLineDate) > 0) {
                System.out.println("Task expired");
                return false ;
            } else {
                System.out.println("Task in progress");
                return true ;
            }
        } catch (ParseException ex) {
            Logger.getLogger(StatisticListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false ;
    }

    public void setTaskes(ArrayList<TaskInfo> taskes) {
        this.taskes = taskes;
    }
    
    
}
