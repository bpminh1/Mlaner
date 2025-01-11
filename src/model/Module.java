package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a module.
 * Consists of a name, lectures and exercises.
 * Lectures and exercises can be empty.
 *
 * @param name The name of the module
 * @param lectures The lectures of the module
 * @param exercises The exercises of the module
 */
public record Module(String name, List<Lesson> lectures, List<Lesson> exercises) {

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
