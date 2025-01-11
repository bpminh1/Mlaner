package model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents a lesson with a specific day and time.
 * Can be either a lecture or an exercise.
 *
 * @param day The day of the lesson
 * @param startTime The start time of the lesson
 * @param endTime The end time of the lesson
 */
public record Lesson(DayOfWeek day, LocalTime startTime, LocalTime endTime) {

    /**
     * Overrides the default toString method.
     *
     * @return A string represents the day and time of the lesson
     */
    @Override
    public String toString() {
        return String.format("%s: from %s to %s", day(), startTime(), endTime());
    }
}
