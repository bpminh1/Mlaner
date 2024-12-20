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

public class DisplayController implements Initializable {
    @FXML private Button edit;
    @FXML private Button delete;
    @FXML private TreeView<Object> treeView;

    private TreeItem<Object> selectedItem;
    private SceneChanger sceneChanger = new SceneChanger();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateTree();

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

    public void generateTree(){
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

    public void addModule(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/InputScene.fxml", event);
    }

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

    public void showList(){
    }

    public void confirm(ActionEvent event) throws IOException {
        FXMLLoader loader = sceneChanger.changeScene("/view/ScheduleScene.fxml", event);

        ScheduleController scheduleController = loader.getController();
        scheduleController.findResult();
    }
}
