package controller;

import dataBase.DataBase;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Schedule;
import model.Lesson;
import model.Module;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleController {


    @FXML AnchorPane success;
    @FXML AnchorPane fail;
    @FXML GridPane grid;

    private HashMap<String, Node> getNodes(){
        HashMap<String, Node> nodes = new HashMap<>();
        for(Node node : grid.getChildren()){
            nodes.put(GridPane.getRowIndex(node)+","+GridPane.getColumnIndex(node), node);
        }
        return nodes;
    }

    public void findResult(){
        Schedule schedule = new Schedule(DataBase.modules.toArray(new Module[0]));
        HashMap<String, Lesson> result = schedule.getResult();
        if(result == null){
            success.setVisible(false);
            fail.setVisible(true);
        }
        else{
            HashMap<String, Node> nodes = getNodes();
            for(Map.Entry<String, Lesson> entrySet : result.entrySet()){
                for(int row : getRows(entrySet.getValue())){
                    String text = entrySet.getKey();
                    if(text.matches(".*Lecture\\d+$"))
                        text = text.substring(0, text.length()-1);

                    Node node = nodes.get(row+","+getColumn(entrySet.getValue()));
                    ((Text)node).setText(text);

                    Tooltip tooltip = new Tooltip(text + "\n" + entrySet.getValue().toString());
                    tooltip.setShowDelay(Duration.millis(0));
                    tooltip.setShowDuration(Duration.minutes(2));

                    Tooltip.install(node, tooltip);
                }
            }
            success.setVisible(true);
            fail.setVisible(false);
        }
    }

    private List<Integer> getRows(Lesson lesson){
        LocalTime startTime = lesson.getStartTime();
        LocalTime endTime = lesson.getEndTime();
        int startRow = getRow(startTime);
        int endRow = getRow(endTime);
        List<Integer> rows = new ArrayList<>();
        rows.add(startRow);
        while(startRow != endRow)
            rows.add(++startRow);
        return rows;
    }

    private int getRow(LocalTime time){
        LocalTime[] localTimeList = new LocalTime[]{
                LocalTime.of(8,0),
                LocalTime.of(9,50),
                LocalTime.of(11,40),
                LocalTime.of(13, 30),
                LocalTime.of(15, 20),
                LocalTime.of(17,0)};
        if(checkInterval(localTimeList[0], localTimeList[1], time))
            return 1;
        if(checkInterval(localTimeList[1], localTimeList[2], time))
            return 2;
        if(checkInterval(localTimeList[2], localTimeList[3], time))
            return 3;
        if(checkInterval(localTimeList[3], localTimeList[4], time))
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
        return (real.isAfter(start) || real.equals(start)) && real.isBefore(end);
    }
}
