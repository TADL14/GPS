package com.example.gps_g11.GardenPlant;

public class Plant {
    private String name;
    private String imagePath;

    private String indoorOutdoor;
    private String color;
    private boolean flowers;
    private String season;
    private String maintenance;
    private boolean petFriendly;
    private String allergies;
    private String size;
    private String humidity_requirement;
    public Plant(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }



    public Plant(){
    }
    // Constructor
    public Plant(String name, String indoorOutdoor, String color, boolean flowers, String season, String maintenance,
                      boolean petFriendly, String allergies, String size, String humidity_requirement) {
        this.name = name;
        this.indoorOutdoor = indoorOutdoor;
        this.color = color;
        this.flowers = flowers;
        this.season = season;
        this.maintenance = maintenance;
        this.petFriendly = petFriendly;
        this.allergies = allergies;
        this.size = size;
        this.humidity_requirement = humidity_requirement;
    }

    public Plant(String name, String indoorOutdoor, String color, boolean flowers, String season, String maintenance,
                 boolean petFriendly, String allergies, String size, String humidity_requirement, String imagePath) {
        this.name = name;
        this.indoorOutdoor = indoorOutdoor;
        this.color = color;
        this.flowers = flowers;
        this.season = season;
        this.maintenance = maintenance;
        this.petFriendly = petFriendly;
        this.allergies = allergies;
        this.size = size;
        this.humidity_requirement = humidity_requirement;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public void setName(String name) { this.name = name; }

    public String getIndoorOutdoor() { return indoorOutdoor; }
    public void setIndoorOutdoor(String indoorOutdoor) { this.indoorOutdoor = indoorOutdoor; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public boolean isFlowers() { return flowers; }
    public void setFlowers(boolean flowers) { this.flowers = flowers; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getMaintenance() { return maintenance; }
    public void setMaintenance(String maintenance) { this.maintenance = maintenance; }

    public boolean isPetFriendly() { return petFriendly; }
    public void setPetFriendly(boolean petFriendly) { this.petFriendly = petFriendly; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String gethumidity_requirement() { return humidity_requirement; }
    public void sethumidity_requirement(String humidity_requirement) { this.humidity_requirement = humidity_requirement; }

    @Override
    public String toString() {
        return "Plant{" +
                "Name='" + name + '\'' +
                ", IndoorOutdoor='" + indoorOutdoor + '\'' +
                ", Color='" + color + '\'' +
                ", Flowers=" + flowers +
                ", Season='" + season + '\'' +
                ", Maintenance='" + maintenance + '\'' +
                ", PetFriendly=" + petFriendly +
                ", Allergies='" + allergies + '\'' +
                ", Size='" + size + '\'' +
                ", Humidity Requirement='" + humidity_requirement + '\'' +
                '}';
    }
}
