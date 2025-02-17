package model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return day == lesson.day && Objects.equals(endTime, lesson.endTime) && Objects.equals(startTime, lesson.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startTime, endTime);
    }
}
