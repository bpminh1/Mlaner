package controller;

import dataBase.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.Lesson;
import model.Module;
import view.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller class for the display screen.
 * Displays all the {@link Module}s from the user
 */
public class DisplayController implements Initializable {
    /**
     * Button to edit a {@link Module} or {@link Lesson}
     */
    @FXML private Button edit;
    /**
     *  Button to delete a {@link Module} or {@link Lesson}
     */
    @FXML private Button delete;
    /**
     * The TreeView to show all {@link Module}s
     */
    @FXML private TreeView<Object> treeView;
    /**
     * The TreeItem to store the selected item
     */
    private TreeItem<Object> selectedItem;
    /**
     * The SceneChanger for navigation
     */
    private final SceneChanger sceneChanger = new SceneChanger();

    /**
     * Initializes the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateTree();
        addListenerToTreeView();
    }

    /**
     * Generates the tree view with all {@link Module}s from the {@link DataBase}
     */
    private void generateTree(){
        TreeItem<Object> root = new TreeItem<>();

        for(Module module : DataBase.modules){
            TreeItem<Object> moduleNode = new TreeItem<>(module);
            root.getChildren().add(moduleNode);
            TreeItem<Object> lectureNode = new TreeItem<>("Lecture");
            TreeItem<Object> exerciseNode = new TreeItem<>("Exercise");
            moduleNode.getChildren().addAll(lectureNode, exerciseNode);
            for(Lesson lecture : module.lectures())
                lectureNode.getChildren().add(new TreeItem<>(lecture));
            for(Lesson exercise : module.exercises()){
                exerciseNode.getChildren().add(new TreeItem<>(exercise));
            }
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    /**
     * Adds the listener to the tree view
     */
    private void addListenerToTreeView(){
        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)
                        -> {selectedItem = treeView.getSelectionModel().getSelectedItem();
                    if(selectedItem == null)
                        return;
                    delete.setVisible(true);
                    edit.setVisible(true);
                    if((selectedItem.getValue().equals("Lecture") && ((Module) selectedItem.getParent().getValue()).lectures().isEmpty())||
                            (selectedItem.getValue().equals("Exercise") && ((Module) selectedItem.getParent().getValue()).exercises().isEmpty()))
                        delete.setVisible(false);
                });
    }

    /**
     * Handles the action for the add button
     *
     * @param event The ActionEvent triggered by the add button
     * @throws IOException if the FXML file cannot be loaded
     */
    public void addModule(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/InputScene.fxml", event);
    }

    /**
     * Handles the action for the edit button
     *
     * @param event The ActionEvent triggered by the edit button
     * @throws IOException if the FXML file cannot be loaded
     */
    public void editButton(ActionEvent event) throws IOException {
        Module module;
        Lesson exercise = null;
        Lesson lecture = null;

        if(selectedItem.getValue() instanceof Module)
            module = (Module) selectedItem.getValue();
        else if(selectedItem.getValue().equals("Exercise") || selectedItem.getValue().equals("Lecture"))
            module = (Module) selectedItem.getParent().getValue();
        else{
            module = (Module) selectedItem.getParent().getParent().getValue();
            if(selectedItem.getParent().getValue().equals("Exercise"))
                exercise = (Lesson) selectedItem.getValue();
            else if(selectedItem.getParent().getValue().equals("Lecture"))
                lecture = (Lesson) selectedItem.getValue();
        }

        DataBase.modules.remove(module);

        FXMLLoader loader = sceneChanger.changeScene("/view/InputScene.fxml", event);
        InputController inputController = loader.getController();
        inputController.editDisplay(module,lecture, exercise, event);
    }

    /**
     * Handles the action for the delete button
     *
     * @param event The ActionEvent triggered by the delete button
     */
    public void deleteButton(ActionEvent event){
        if(selectedItem.getValue() instanceof Module)
            DataBase.modules.remove((Module) selectedItem.getValue());
        else if(selectedItem.getValue().equals("Exercise")){
            ((Module) selectedItem.getParent().getValue()).setExercises(new ArrayList<>());
        }
        else if(selectedItem.getValue().equals("Lecture")){
            ((Module) selectedItem.getParent().getValue()).setLectures(new ArrayList<>());
        }
        else{
            Module module = (Module) selectedItem.getParent().getParent().getValue();
            if(selectedItem.getParent().getValue().equals("Exercise")){
                ArrayList<Lesson> exercises = new ArrayList<>(module.exercises());
                exercises.remove((Lesson) selectedItem.getValue());
                module.setExercises(exercises);
            }
            else{
                ArrayList<Lesson> lectures = new ArrayList<>(module.lectures());
                lectures.remove((Lesson) selectedItem.getValue());
                module.setLectures(lectures);
            }
        }

        treeView.getSelectionModel().clearSelection();
        delete.setVisible(false);
        edit.setVisible(false);
        generateTree();
    }

    /**
     * Shows the tree view
     */
    public void showList(){
    }

    /**
     * Handles the action for the confirm button
     *
     * @param event The ActionEvent triggered by the confirm button
     * @throws IOException if the FXML file cannot be loaded
     */
    public void confirm(ActionEvent event) throws IOException {
        FXMLLoader loader = sceneChanger.changeScene("/view/ScheduleScene.fxml", event);

        ScheduleController scheduleController = loader.getController();
        scheduleController.findResult();
    }
}
