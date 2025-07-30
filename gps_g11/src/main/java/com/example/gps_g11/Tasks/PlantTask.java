package com.example.gps_g11.Tasks;

public class PlantTask {
    private String description;

    private String recurrence; // "daily", "weekly", "monthly", "none

    private Boolean critical;

    private String category;

    public PlantTask(String description, String recurrence) {
        this.description = description;
        this.recurrence = recurrence;
        if (description.equals("Watering") || description.equals("Soil") || description.equals("Light"))
            this.critical = false;
        else {
            if (recurrence.equals("daily")) {
                this.critical = true;
            } else {
                this.critical = false;
            }
        }
        //this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public Boolean isCritical() {
        return critical;
    }

    public String getCategory() {
        return category;
    }
}
