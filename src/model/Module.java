package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a module.
 * Consists of a name, lectures and exercises.
 * Lectures and exercises can be empty.
 */
public class Module{
    public List<Lesson> exercises;
    public List<Lesson> lectures;
    public String name;

    public Module(String name, List<Lesson> lectures, List<Lesson> exercises){
        this.name = name;
        this.lectures = new ArrayList<>(lectures);
        this.exercises = new ArrayList<>(exercises);
    }

    /**
     * Changes the exercises of the module
     *
     * @param exercises The new exercises of the module
     */
    public void setExercises(ArrayList<Lesson> exercises) {
        this.exercises.clear();
        this.exercises.addAll(exercises);
    }

    /**
     * Changes the lectures of the module
     *
     * @param lectures The new lectures of the module
     */
    public void setLectures(ArrayList<Lesson> lectures) {
        this.lectures.clear();
        this.lectures.addAll(lectures);
    }

    /**
     * Overrides the default toString method
     *
     * @return A string that contains the name of the module
     */
    @Override
    public String toString() {
        return this.name;
    }
}
