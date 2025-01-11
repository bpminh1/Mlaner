package controller;

import dataBase.DataBase;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
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

/**
 * Controller class for the Schedule screen
 */
public class ScheduleController {
    /**
     * The Pane when a schedule is successfully created
     */
    @FXML AnchorPane success;
    /**
     * The Pane when a schedule cannot be created
     */
    @FXML AnchorPane fail;
    /**
     * The grid to position each {@link Lesson}
     */
    @FXML GridPane grid;

    /**
     * Create a map for each node of the grid
     *
     * @return The map with the position of each node
     */
    private HashMap<String, Node> getNodes(){
        HashMap<String, Node> nodes = new HashMap<>();
        for(Node node : grid.getChildren()){
            nodes.put(GridPane.getRowIndex(node)+","+GridPane.getColumnIndex(node), node);
        }
        return nodes;
    }

    /**
     * Find the result for the given database
     */
    public void findResult(){
        Schedule schedule = new Schedule(DataBase.modules.toArray(new Module[0]));
        HashMap<String, Lesson> result = schedule.getResult();

        if(result == null){
            success.setVisible(false);
            fail.setVisible(true);
        }
        else{
            placeSchedule(result);
            success.setVisible(true);
            fail.setVisible(false);
        }
    }

    /**
     * Place the found schedule in the correct position of the grid
     *
     * @param result The final schedule
     */
    private void placeSchedule(HashMap<String, Lesson> result){
        HashMap<String, Node> nodes = getNodes();
        for(Map.Entry<String, Lesson> entrySet : result.entrySet()){
            for(int row : getRows(entrySet.getValue())){
                String text = entrySet.getKey();
                if(text.matches(".*Lecture\\d+$"))
                    text = text.substring(0, text.length()-1);

                Node node = nodes.get(row+","+getColumn(entrySet.getValue()));
                ((Text)node).setText(text);

                setUpToolTip(text, entrySet, node);
            }
        }
    }

    /**
     * Set up the tooltip for each lesson
     *
     * @param text Name of the lesson
     * @param entrySet The EntrySet of the result map
     * @param node The node where to put the ToolTip
     */
    private void setUpToolTip(String text, Map.Entry<String, Lesson> entrySet, Node node){
        Tooltip tooltip = new Tooltip(text + "\n" + entrySet.getValue().toString());
        tooltip.setFont(Font.font("Comic Sans MS"));
        tooltip.setShowDelay(Duration.millis(0));
        tooltip.setShowDuration(Duration.minutes(2));

        Tooltip.install(node, tooltip);
    }

    /**
     * Finds all the rows that the lesson should be put
     *
     * @param lesson The lesson to check
     * @return All the rows for the lesson
     */
    private List<Integer> getRows(Lesson lesson){
        LocalTime startTime = lesson.startTime();
        LocalTime endTime = lesson.endTime();
        int startRow = getRow(startTime);
        int endRow = getRow(endTime);
        List<Integer> rows = new ArrayList<>();
        rows.add(startRow);
        while(startRow != endRow)
            rows.add(++startRow);
        return rows;
    }

    /**
     * Gets the row that has the time of a lesson
     *
     * @param time The time to check
     * @return The row
     */
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

    /**
     * Gets the column that has the day of the lesson
     *
     * @param lesson The lesson to check
     * @return The column
     */
    private int getColumn(Lesson lesson){
        return switch(lesson.day()){
            case MONDAY -> 1;
            case TUESDAY -> 2;
            case WEDNESDAY -> 3;
            case THURSDAY -> 4;
            case FRIDAY -> 5;
            default -> 0;
        };
    }

    /**
     * Checks if the lesson is between the start and end time
     *
     * @param start The start time according to the pre-defined time
     * @param end The end time according to the pre-defined time
     * @param real The real time of the lesson
     * @return True if {@param real} is between {@param start} and {@param end}
     */
    private boolean checkInterval(LocalTime start, LocalTime end, LocalTime real){
        return (real.isAfter(start) || real.equals(start)) && real.isBefore(end);
    }
}
