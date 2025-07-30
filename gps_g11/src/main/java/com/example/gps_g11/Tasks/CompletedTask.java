package com.example.gps_g11.Tasks;

import java.util.Objects;

public class CompletedTask {
    public String plantName;
    public  String taskDescription;
    public String completionDate;

    public CompletedTask(String plantName, String taskDescription, String completionDate) {
        this.plantName = plantName;
        this.taskDescription = taskDescription;
        this.completionDate = completionDate;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedTask that = (CompletedTask) o;
        return Objects.equals(plantName, that.plantName) &&
                Objects.equals(taskDescription, that.taskDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plantName, taskDescription);
    }
}
