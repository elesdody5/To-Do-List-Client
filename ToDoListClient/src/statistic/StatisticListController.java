/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statistic;

import java.net.URL;
import java.util.ResourceBundle;
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

}
