package model;

import java.util.ArrayList;
import java.util.List;

public record Module(String name, List<Lesson> lectures, List<Lesson> exercises) {

    public void setExercises(ArrayList<Lesson> exercises) {
        this.exercises.clear();
        this.exercises.addAll(exercises);
    }

    public void setLectures(ArrayList<Lesson> lectures) {
        this.lectures.clear();
        this.lectures.addAll(lectures);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
