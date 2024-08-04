package com.suchith.volvo_new;

public class Model_Forklifts {

    private String model_name, model_id, plant_location, start_Date, end_date, duration, active, inactive;

    public Model_Forklifts() {
        // Default constructor required for calls to DataSnapshot.getValue(Model_Forklifts.class)
    }

    public Model_Forklifts(String model_name, String model_id, String plant_location, String start_Date, String end_date, String duration, String active, String inactive) {
        this.model_name = model_name;
        this.model_id = model_id;
        this.plant_location = plant_location;
        this.start_Date = start_Date;
        this.end_date = end_date;
        this.duration = duration;
        this.active = active;
        this.inactive = inactive;
    }

    // Getters and setters

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getPlant_location() {
        return plant_location;
    }

    public void setPlant_location(String plant_location) {
        this.plant_location = plant_location;
    }

    public String getStart_Date() {
        return start_Date;
    }

    public void setStart_Date(String start_Date) {
        this.start_Date = start_Date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    @Override
    public String toString() {
        return "Model_Forklifts{" +
                "model_name='" + model_name + '\'' +
                ", model_id='" + model_id + '\'' +
                ", plant_location='" + plant_location + '\'' +
                ", start_Date='" + start_Date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", duration='" + duration + '\'' +
                ", active='" + active + '\'' +
                ", inactive='" + inactive + '\'' +
                '}';
    }
}