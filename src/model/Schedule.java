package model;

import java.util.*;

/**
 * The algorithm to find a schedule for all {@link Module}s.
 * A {@link Module} is successfully added if all the lectures
 * and one of the exercises can be added.
 */
public class Schedule {
    /**
     * All {@link  Module}s that should be contained in the schedule
     */
    private final model.Module[] modules;

    /**
     * Contains the final schedule stored in a map.
     * String is the name of the {@link Module} with
     * lecturei, where i is the index for each lecture
     * or exercise
     */
    private final HashMap<String, Lesson> result = new HashMap<>();

    /**
     * The construction for this class
     *
     * @param modules The {@link Module}s to find a schedule
     */
    public Schedule(model.Module[] modules){
        this.modules = modules;
    }

    /**
     * Finds a schedule for all {@link Module}s
     *
     * @return The final schedule if all {@link Module}s can be added,
     *         null if one or more {@link Lesson}s cannot be added
     */
    public HashMap<String, Lesson> getResult(){
        if(sortLectures() && findExercises(0, modulesWithExercise())){
            return result;
        }
        return null;
    }

    /**
     * Adds all lectures of all {@link Module}s to the schedule
     *
     * @return True if all lectures can be added,
     *         False if one of the lectures cannot be added
     */
    private boolean sortLectures(){
        for(Module module : modules){
            int i = 1;
            if(module.lectures == null || module.lectures.isEmpty())
                continue;
            for(Lesson lecture : module.lectures) {
                if(anyOverlap(lecture))
                    return false;
                result.put(module.name + " Lecture" + i, lecture);
                i++;
            }
        }
        return true;
    }

    /**
     * Finds one exercise for each {@link Module} that can be added to the schedule
     *
     * @param amount The number of exercises that have been added
     * @param modulesWithExercise All the {@link Module}s that have exercises
     * @return True if there is one exercise for each {@link Module},
     *         False if none of the exercises for a {@link Module} cannot be added
     */
    private boolean findExercises(int amount, model.Module[] modulesWithExercise){
        if(amount == modulesWithExercise.length)
            return true;
        for(Lesson exercise : modulesWithExercise[amount].exercises){
            if(anyOverlap(exercise))
                continue;
            result.put(modulesWithExercise[amount].name + " Exercise", exercise);
            if (findExercises(amount + 1, modulesWithExercise))
                return true;
            result.remove(modulesWithExercise[amount].name + " Exercise");
        }
        return false;
    }

    /**
     * Finds all the {@link Module}s that have exercises
     *
     * @return All the {@link Module}s that have exercises
     */
    private model.Module[] modulesWithExercise(){
        List<model.Module> modulesWithExercise = new ArrayList<>();
        for(model.Module module : modules){
            if(module.exercises == null || module.exercises.isEmpty())
                continue;
            modulesWithExercise.add(module);
        }
        return modulesWithExercise.toArray(new Module[0]);
    }

    /**
     * Checks if 2 lessons are overlapped
     *
     * @param lesson1 The first lesson to check
     * @param lesson2 The second lesson to check
     * @return True if the 2 lessons are overlapped,
     *         False if the 2 lessons are not overlapped
     */
    private boolean overlapping(Lesson lesson1, Lesson lesson2){
        return  lesson1.day() == lesson2.day() &&
                lesson1.startTime().isBefore(lesson2.endTime()) &&
                lesson2.startTime().isBefore(lesson1.endTime());
    }

    /**
     * Checks if a lesson is overlapped with any other lesson of the result schedule {@see #result}
     *
     * @param lesson the lesson to check
     * @return True if the lesson is overlapped,
     *         False if the lesson is not overlapped.
     */
    private boolean anyOverlap(Lesson lesson) {
        return result.values().stream().anyMatch( l -> overlapping(lesson, l) );
    }
}
