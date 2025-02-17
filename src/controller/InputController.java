package controller;

import dataBase.DataBase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
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

/**
 * Controller class for the Input screen, managing user interaction and input handling
 */
public class InputController implements Initializable {
    /**
     * Button to edit a {@link Lesson}
     */
    @FXML private Button edit;
    /**
     * Button to delete a {@link Lesson}
     */
    @FXML private Button delete;
    @FXML private Button addLecture;
    @FXML private Button addExercise;
    /**
     * Label to display error messages
     */
    @FXML private Label error;
    /**
     * ChoiceBoxes for selecting {@link Lesson}s days
     */
    @FXML private ChoiceBox<DayOfWeek> dayLecture;
    @FXML private ChoiceBox<DayOfWeek> dayExercise;

    /**
     * TextFields for {@link Module}'s name and {@link Lesson}'s time input
     */
    @FXML private TextField moduleName;
    @FXML private TextField startHourLecture;
    @FXML private TextField startMinuteLecture;
    @FXML private TextField startHourExercise;
    @FXML private TextField startMinuteExercise;
    @FXML private TextField endHourLecture;
    @FXML private TextField endMinuteLecture;
    @FXML private TextField endHourExercise;
    @FXML private TextField endMinuteExercise;

    /**
     * ListViews for displaying all {@link Lesson}s
     */
    @FXML private ListView<Lesson> listViewExercise;
    @FXML private ListView<Lesson> listViewLecture;

    /**
     * Observable lists for storing lessons
     */
    ObservableList<Lesson> optionsExercise = FXCollections.observableArrayList();
    ObservableList<Lesson> optionsLecture = FXCollections.observableArrayList();

    /**
     * Collections for storing text fields and choice boxes
     */
    List<TextField> lectureTextFields;
    List<TextField> exerciseTextFields;
    List<ChoiceBox<DayOfWeek>> dayChoices;

    /**
     * Currently selected {@link Lesson}
     */
    private Lesson selectedExercise;
    private Lesson selectedLecture;

    /**
     * SceneChanger for navigation
     */
    private final SceneChanger sceneChanger = new SceneChanger();

    /**
     * Initializes the controller.
     * Add listeners to all TextFields, ChoiceBoxes and ListViews.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lectureTextFields = List.of(startHourLecture, startMinuteLecture, endHourLecture, endMinuteLecture);
        exerciseTextFields = List.of(startHourExercise, startMinuteExercise, endHourExercise, endMinuteExercise);
        dayChoices = List.of(dayLecture, dayExercise);

        moduleName.textProperty().addListener((observable, oldValue, newValue) -> error.setText(null));
        setUpChoiceBox();
        setUpTime();
        addListenerToListView();
        setEnterActions();
    }

    /**
     * Sets Enter action to all Controls
     */
    private void setEnterActions(){
        setEnterAction(null, moduleName, dayLecture);
        setEnterAction(null, dayLecture, startHourLecture);
        setEnterAction(dayLecture, startHourLecture, startMinuteLecture);
        setEnterAction(startHourLecture, startMinuteLecture, endHourLecture);
        setEnterAction(startMinuteLecture, endHourLecture, endMinuteLecture);
        setEnterAction(endHourLecture, endMinuteLecture, addLecture);
        setEnterAction(null, addLecture, dayExercise);
        setEnterAction(null, dayExercise, startHourExercise);
        setEnterAction(dayExercise, startHourExercise, startMinuteExercise);
        setEnterAction(startHourExercise, startMinuteExercise, endHourExercise);
        setEnterAction(startMinuteExercise, endHourExercise, endMinuteExercise);
        setEnterAction(endHourExercise, endMinuteExercise, addExercise);
        setEnterAction(null, addExercise, dayExercise);
    }

    /**
     * Sets Enter action to one Control
     * When the key Enter is pressed, it will change the focus to the next Control
     * @param previous the previous Control
     * @param current the current focussed Control
     * @param next the next Control to focus on
     */
    private void setEnterAction(Control previous, Control current, Control next){
        current.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case ENTER -> {
                    if (next instanceof Button button) button.fire();
                    Platform.runLater(() -> focusAndShow(next));
                }
                case LEFT -> Platform.runLater(() -> focusAndShow(previous));
                case RIGHT -> {
                    if (!(next instanceof Button))
                        Platform.runLater(() -> focusAndShow(next));
                }
            }
        });
    }

    /**
     * Moves focus to the next TextField or ChoiceBox
     * If {@param control} is a ChoiceBox, shows it
     * @param control the control to move focus to
     */
    private void focusAndShow(Control control){
        if(control != null){
            control.requestFocus();
            if (control instanceof ChoiceBox<?> choiceBox)
                choiceBox.show();
        }
    }

    /**
     * Sets up the ChoiceBoxes to have all weekdays.
     */
    private void setUpChoiceBox(){
        for(ChoiceBox<DayOfWeek> choiceBox : dayChoices){
            choiceBox.getItems().addAll(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
            choiceBox.getSelectionModel().selectedItemProperty().
                    addListener((observable, oldValue, newValue) -> error.setText(null));
        }
    }

    /**
     * Sets up the TextFields for time input.
     */
    private void setUpTime(){
        for(TextField field : lectureTextFields){
            field.textProperty().addListener((observable, oldValue, newValue) -> error.setText(null));
        }
        for(TextField field : exerciseTextFields){
            field.textProperty().addListener((observable, oldValue, newValue) -> error.setText(null));
        }
    }

    /**
     * Adds listeners to the ListViews
     */
    private void addListenerToListView(){
        listViewExercise.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> selectedExercise = listViewListener(listViewExercise));

        listViewLecture.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> selectedLecture = listViewListener(listViewLecture));
    }

    /**
     * Sets up listener to the ListView.
     * @param listView The ListView to be set.
     * @return the selected {@link Lesson} from the ListView
     */
    private Lesson listViewListener(ListView<Lesson> listView){
        Lesson selectedLesson = listView.getSelectionModel().getSelectedItem();
        if(selectedLesson != null){
            delete.setVisible(true);
            edit.setVisible(true);
        }
        return selectedLesson;
    }

    /**
     * Handles the deletion of a selected lesson.
     *
     * @param event The ActionEvent triggered by the delete button.
     */
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

    /**
     * Remove the selected lesson from the ListView
     *
     * @param lecture True if the selected lesson is a lecture
     *                False if the selected lesson is an exercise
     */
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

    /**
     * Handles the edition of a selected lesson.
     *
     * @param event The ActionEvent triggered by the edit button.
     */
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

    /**
     * Sets up the Input screen when this method is called from the Display screen
     *
     * @param module The selected module
     * @param lecture The selected lecture
     * @param exercise The selected exercise
     * @param event The ActionEvent triggered by the edit button
     */
    public void editDisplay(Module module, Lesson lecture, Lesson exercise, ActionEvent event){
        moduleName.setText(module.name);

        optionsLecture.setAll(module.lectures);
        listViewLecture.setItems(optionsLecture);

        optionsExercise.setAll(module.exercises);
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

    /**
     * Changes the value of the TextFields to the selected lesson
     *
     * @param selectedLesson The selected lesson
     * @param day The ChoiceBox for the day
     * @param startHour The TextField for the start hour
     * @param startMinute The TextField for the start minute
     * @param endHour The TextField for the end hour
     * @param endMinute The TextField for the end minute
     */
    private void editTextField(Lesson selectedLesson,
                               ChoiceBox<DayOfWeek> day,
                               TextField startHour, TextField startMinute,
                               TextField endHour, TextField endMinute){
        day.setValue(selectedLesson.day());
        startHour.setText(String.valueOf(selectedLesson.startTime().getHour()));
        startMinute.setText(String.valueOf(selectedLesson.startTime().getMinute()));
        endHour.setText(String.valueOf(selectedLesson.endTime().getHour()));
        endMinute.setText(String.valueOf(selectedLesson.endTime().getMinute()));
    }

    /**
     * Handles the action of the add button
     *
     * @param event The ActionEvent triggered by the add button
     */
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

    /**
     * Add a lesson to the ListView.
     *
     * @param listView The ListView that the lesson should be added
     * @param options The lesson to be added
     * @param day The day of the lesson
     * @param startHour The start hour of the lesson
     * @param startMinute The start minute of the lesson
     * @param endHour The end hour of the lesson
     * @param endMinute The end minute of the lesson
     * @param lecture If true, the lesson to be added is a lecture
     *                If false, the lesson to be added is an exercise
     */
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

    /**
     * Clears the TextFields for time input.
     *
     * @param lecture If true, clears the TextFields for lecture
     *                If false, clears the TextFields for exercise
     */
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

    /**
     * Changes to the Display screen, when user presses done.
     *
     * @param event The ActionEvent triggered by the done button
     * @throws IOException if the FXML file cannot be loaded
     */
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

    /**
     * Sorts the lessons according to the start time.
     *
     * @param options all the lessons to be sorted.
     * @return a list of all lessons in a correct order
     */
    private List<Lesson> sortLesson(ObservableList<Lesson> options){
        return options.stream().sorted(Comparator.comparing(Lesson::day).
                thenComparing(Lesson::startTime)).toList();
    }

    /**
     * Changes the time from the input to a correct time format
     *
     * @param hour The input hour from the user
     * @param minute The input minute from the user
     * @return The {@link LocalTime} of the input time
     */
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

    /**
     * Validates the time of a {@link Lesson}
     *
     * @param day the day of the lesson
     * @param start the start time of the lesson
     * @param end the end time of the lesson
     * @return True if the time of the lesson is correct (start before end),
     *         False if the time of the lesson wrong.
     */
    private boolean validateDuration(DayOfWeek day, LocalTime start, LocalTime end){
        if(day == null)
            error.setText("Please choose a day");
        if(start != null && end != null && (start.isAfter(end) || start.equals(end)))
            error.setText("Please enter a valid interval");
        return day != null && start != null && end != null && start.isBefore(end);
    }
}