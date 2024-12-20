package model;

import java.util.*;

public class Schedule {
    private final model.Module[] modules;
    private final HashMap<String, Lesson> result = new HashMap<>();

    public Schedule(model.Module[] modules){
        this.modules = modules;
    }

    public HashMap<String, Lesson> getResult(){
        if(sortLectures() && findExercises(0, modulesWithExercise())){
            return result;
        }
        return null;
    }

    private boolean sortLectures(){
        for(Module module : modules){
            int i = 1;
            if(module.lectures() == null || module.lectures().isEmpty())
                continue;
            for(Lesson lecture : module.lectures()) {
                if(anyOverlap(lecture))
                    return false;
                result.put(module.name() + " Lecture" + i, lecture);
                i++;
            }
        }
        return true;
    }

    private boolean findExercises(int amount, model.Module[] modulesWithExercise){
        if(amount == modulesWithExercise.length)
            return true;
        for(Lesson exercise : modulesWithExercise[amount].exercises()){
            if(anyOverlap(exercise))
                continue;
            result.put(modulesWithExercise[amount].name() + " Exercise", exercise);
            if (findExercises(amount + 1, modulesWithExercise))
                return true;
            result.remove(modulesWithExercise[amount].name() + " Exercise");
        }
        return false;
    }

    private model.Module[] modulesWithExercise(){
        List<model.Module> modulesWithExercise = new ArrayList<>();
        for(model.Module module : modules){
            if(module.exercises() == null || module.exercises().isEmpty())
                continue;
            modulesWithExercise.add(module);
        }
        return modulesWithExercise.toArray(new Module[0]);
    }

    private boolean overlapping(Lesson lesson1, Lesson lesson2){
        return  lesson1.day() == lesson2.day() &&
                lesson2.startTime().isBefore(lesson1.endTime()) &&
                lesson1.startTime().isBefore(lesson2.startTime());
    }

    private boolean anyOverlap(Lesson lesson) {
        return result.values().stream().anyMatch( l -> overlapping(lesson, l) );
    }
}
