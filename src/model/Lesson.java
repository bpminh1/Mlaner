package model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Lesson {
    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Lesson(DayOfWeek day, LocalTime startTime, LocalTime endTime){
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getDay(){
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime(){
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("%s: from %s to %s", getDay(), getStartTime(), getEndTime());
    }
}
