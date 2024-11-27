package controller;

import dataBase.DataBase;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Schedule;
import model.Lesson;
import model.Module;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ScheduleController {


    @FXML AnchorPane success;
    @FXML AnchorPane fail;
    @FXML GridPane grid;

    private HashMap<String, Node> nodes = new HashMap<>();

    public void findResult(){
        Schedule schedule = new Schedule(DataBase.modules.toArray(new Module[0]));
        HashMap<String, Lesson> result = schedule.getResult();
        if(result == null){
            success.setVisible(false);
            fail.setVisible(true);
        }
        else{
            getNodes();
            for(Map.Entry<String, Lesson> entrySet : result.entrySet()){
                ((Text)nodes.get(getRow(entrySet.getValue())+","+getColumn(entrySet.getValue()))).
                        setText(entrySet.getKey());
            }
            success.setVisible(true);
            fail.setVisible(false);
        }
    }

    private void getNodes(){
        for(Node node : grid.getChildren()){
            nodes.put(GridPane.getRowIndex(node)+","+GridPane.getColumnIndex(node), node);
        }
    }

    private int getRow(Lesson lesson){
        LocalTime startTime = lesson.getStartTime();
        if(checkInterval(LocalTime.of(8,0), LocalTime.of(9,50), startTime))
            return 1;
        if(checkInterval(LocalTime.of(9,50), LocalTime.of(11,30), startTime))
            return 2;
        if(checkInterval(LocalTime.of(11,40), LocalTime.of(13,20), startTime))
            return 3;
        if(checkInterval(LocalTime.of(13,30), LocalTime.of(15,10), startTime))
            return 4;
        return 5;
    }

    private int getColumn(Lesson lesson){
        return switch(lesson.getDay()){
            case MONDAY -> 1;
            case TUESDAY -> 2;
            case WEDNESDAY -> 3;
            case THURSDAY -> 4;
            case FRIDAY -> 5;
            default -> 0;
        };
    }

    private boolean checkInterval(LocalTime start, LocalTime end, LocalTime real){
        return real.isAfter(start) && real.isBefore(end);
    }
}
