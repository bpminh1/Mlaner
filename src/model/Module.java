package model;

import java.util.ArrayList;
import java.util.List;

public class Module{
    String name;
    List<Lesson> lectures;
    List<Lesson> exercises;

    public Module(String name, List<Lesson> lectures, List<Lesson> exercises){
        this.name = name;
        this.lectures = lectures;
        this.exercises = exercises;
    }

    public String getName(){
        return name;
    }

    public List<Lesson> getLectures(){
        return lectures;
    }

    public List<Lesson> getExercises(){
        return exercises;
    }

    public void setExercises(ArrayList<Lesson> exercises){
        this.exercises = exercises;
    }

    public void setLectures(ArrayList<Lesson> lectures){
        this.lectures = lectures;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
