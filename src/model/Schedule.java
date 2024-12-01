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
            if(module.getLectures() == null && module.getLectures().isEmpty())
                continue;
            for(Lesson lecture : module.getLectures()) {
                if(overlap(lecture))
                    return false;
                result.put(module.getName() + " lecture" + i, lecture);
                i++;
            }
        }
        return true;
    }

    private boolean findExercises(int amount, model.Module[] modulesWithExercise){
        if(amount == modulesWithExercise.length)
            return true;
        for(Lesson exercise : modulesWithExercise[amount].getExercises()){
            if(overlap(exercise))
                continue;
            result.put(modulesWithExercise[amount].getName() + " exercise", exercise);
            if (findExercises(amount + 1, modulesWithExercise))
                return true;
            result.remove(modulesWithExercise[amount].getName() + " exercise");
        }
        return false;
    }

    private model.Module[] modulesWithExercise(){
        List<model.Module> modulesWithExercise = new ArrayList<>();
        for(model.Module module : modules){
            if(module.getExercises() == null || module.getExercises().isEmpty())
                continue;
            modulesWithExercise.add(module);
        }
        return modulesWithExercise.toArray(new Module[0]);
    }

    private boolean overlap(Lesson lesson1, Lesson lesson2){
        return  lesson1.getDay() == lesson2.getDay() &&
                lesson2.getStartTime().isBefore(lesson1.getEndTime()) &&
                lesson1.getEndTime().isAfter(lesson2.getStartTime());
    }

    private boolean overlap(Lesson lesson){
        for(Lesson lessonToCheck : result.values()){
            if(overlap(lesson, lessonToCheck))
                return true;
        }
        return false;
    }
}
