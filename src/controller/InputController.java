package controller;

import dataBase.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Lesson;
import model.Module;
import view.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class InputController implements Initializable {
    @FXML private Button edit;
    @FXML private Button delete;
    @FXML private Label error;
    @FXML private ChoiceBox<DayOfWeek> dayLecture;
    @FXML private ChoiceBox<DayOfWeek> dayExercise;
    @FXML private TextField moduleName;
    @FXML private TextField startHourLecture;
    @FXML private TextField startMinuteLecture;
    @FXML private TextField startHourExercise;
    @FXML private TextField startMinuteExercise;
    @FXML private TextField endHourLecture;
    @FXML private TextField endMinuteLecture;
    @FXML private TextField endHourExercise;
    @FXML private TextField endMinuteExercise;
    @FXML private ListView<Lesson> listViewExercise;
    @FXML private ListView<Lesson> listViewLecture;

    ObservableList<Lesson> optionsExercise = FXCollections.observableArrayList();
    ObservableList<Lesson> optionsLecture = FXCollections.observableArrayList();

    List<TextField> lectureTextFields;
    List<TextField> exerciseTextFields;
    List<ChoiceBox<DayOfWeek>> dayChoices;

    private Lesson selectedExercise;
    private Lesson selectedLecture;
    private final SceneChanger sceneChanger = new SceneChanger();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lectureTextFields = List.of(startHourLecture, startMinuteLecture, endHourLecture, endMinuteLecture);
        exerciseTextFields = List.of(startHourExercise, startMinuteExercise, endHourExercise, endMinuteExercise);
        dayChoices = List.of(dayLecture, dayExercise);

        moduleName.textProperty().addListener((observable, oldValue, newValue) -> error.setText(null));
        for(ChoiceBox<DayOfWeek> choiceBox : dayChoices){
            choiceBox.getItems().addAll(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
            choiceBox.getSelectionModel().selectedItemProperty().
                    addListener((observable, oldValue, newValue) -> error.setText(null));
        }
        for(TextField field : lectureTextFields){
            field.textProperty().addListener((observable, oldValue, newValue) -> error.setText(null));
        }
        for(TextField field : exerciseTextFields){
            field.textProperty().addListener((observable, oldValue, newValue) -> error.setText(null));
        }

        listViewExercise.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> selectedExercise = listViewListener(listViewExercise));

        listViewLecture.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> selectedLecture = listViewListener(listViewLecture));
    }

    private Lesson listViewListener(ListView<Lesson> listView){
        Lesson selectedLesson = listView.getSelectionModel().getSelectedItem();
        if(selectedLesson != null){
            delete.setVisible(true);
            edit.setVisible(true);
        }
        return selectedLesson;
    }

    public void deleteButton(ActionEvent event){
        if(selectedExercise != null){
            removeFromOptions(false);
        }
        if(selectedLecture != null){
            removeFromOptions(true);
        }
        delete.setVisible(false);
        edit.setVisible(false);
    }

    private void removeFromOptions(boolean lecture){
        if(lecture){
            optionsLecture.remove(selectedLecture);
            listViewLecture.getSelectionModel().clearSelection();
        }
        else{
            optionsExercise.remove(selectedExercise);
            listViewExercise.getSelectionModel().clearSelection();
        }
    }

    public void editButton(ActionEvent event){
        if(selectedExercise != null){
            editTextField(selectedExercise,
                    dayExercise,
                    startHourExercise, startMinuteExercise,
                    endHourExercise, endMinuteExercise);
            removeFromOptions(false);
        }
        if(selectedLecture != null){
            editTextField(selectedLecture,
                    dayLecture,
                    startHourLecture, startMinuteLecture,
                    endHourLecture, endMinuteLecture);
            removeFromOptions(true);
        }
        edit.setVisible(false);
        delete.setVisible(false);
    }

    public void editDisplay(Module module, Lesson lecture, Lesson exercise, ActionEvent event){
        moduleName.setText(module.getName());

        optionsLecture.setAll(module.getLectures());
        listViewLecture.setItems(optionsLecture);

        optionsExercise.setAll(module.getExercises());
        listViewExercise.setItems(optionsExercise);

        if(lecture != null){
            selectedLecture = lecture;
            editButton(event);
        }
        else if(exercise != null){
            selectedExercise = exercise;
            editButton(event);
        }
    }

    private void editTextField(Lesson selectedLesson,
                               ChoiceBox<DayOfWeek> day,
                               TextField startHour, TextField startMinute,
                               TextField endHour, TextField endMinute){
        day.setValue(selectedLesson.getDay());
        startHour.setText(String.valueOf(selectedLesson.getStartTime().getHour()));
        startMinute.setText(String.valueOf(selectedLesson.getStartTime().getMinute()));
        endHour.setText(String.valueOf(selectedLesson.getEndTime().getHour()));
        endMinute.setText(String.valueOf(selectedLesson.getEndTime().getMinute()));
    }

    public void addButton(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String buttonId = sourceButton.getId();

        if(buttonId.equals("addExercise")){
            addLesson(listViewExercise, optionsExercise,
                    dayExercise,
                    startHourExercise, startMinuteExercise,
                    endHourExercise, endMinuteExercise,
                    false);
        }
        else if(buttonId.equals("addLecture")) {
            addLesson(listViewLecture, optionsLecture,
                    dayLecture,
                    startHourLecture, startMinuteLecture,
                    endHourLecture, endMinuteLecture,
                    true);
        }
    }

    private void addLesson(ListView<Lesson> listView, ObservableList<Lesson> options,
                           ChoiceBox<DayOfWeek> day,
                           TextField startHour, TextField startMinute,
                           TextField endHour, TextField endMinute,
                           boolean lecture){
        listView.setItems(options);
        LocalTime startTime = formatTime(startHour, startMinute);
        LocalTime endTime = formatTime(endHour, endMinute);
        DayOfWeek selectedDay = day.getValue();
        if(validateDuration(selectedDay, startTime, endTime)){
            options.add(new Lesson(selectedDay, startTime, endTime));
            clearLesson(lecture);
        }
    }

    private void clearLesson(boolean lecture){
        if(lecture){
            dayLecture.setValue(null);
            for(TextField textField : lectureTextFields)
                textField.clear();
        }
        else{
            dayExercise.setValue(null);
            for(TextField textField : exerciseTextFields)
                textField.clear();
        }
    }

    public void done(ActionEvent event) throws IOException {
        if(moduleName.getText().trim().isEmpty()){
            error.setText("Please enter a name for the module");
        }
        else{
            DataBase.modules.add(new Module(moduleName.getText(),
                    sortLesson(optionsLecture),
                    sortLesson(optionsExercise)));
            sceneChanger.changeScene("/view/DisplayScene.fxml", event);
        }
    }

    private List<Lesson> sortLesson(ObservableList<Lesson> options){
        return options.stream().sorted(Comparator.comparing(Lesson::getDay).
                thenComparing(Lesson::getStartTime)).toList();
    }

    private LocalTime formatTime(TextField hour, TextField minute){
        try{
            int hourNumber = Integer.parseInt(hour.getText());
            int minuteNumber = Integer.parseInt(minute.getText());
            if(hourNumber >17 || hourNumber <8 || minuteNumber >=60 || minuteNumber <0 ||
                    (hourNumber==17 && minuteNumber>=1)) {
                error.setText("Please enter a valid time (08:00 - 17:00)");
            }
            else
                return LocalTime.of(hourNumber, minuteNumber);
        }catch (NumberFormatException e){
            error.setText("Please enter a valid time");
        }
        return null;
    }

    private boolean validateDuration(DayOfWeek day, LocalTime start, LocalTime end){
        if(day == null)
            error.setText("Please choose a day");
        if(start != null && end != null && (start.isAfter(end) || start.equals(end)))
            error.setText("Please enter a valid interval");
        return day != null && start != null && end != null && start.isBefore(end);
    }

}
