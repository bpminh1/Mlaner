package model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public record Lesson(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
    @Override
    public String toString() {
        return String.format("%s: from %s to %s", day(), startTime(), endTime());
    }
}
