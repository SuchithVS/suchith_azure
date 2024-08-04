package com.suchith.volvo_new;

import androidx.annotation.Keep;

@Keep
public class Model_volvo_xc90 {

    private String spare_name, spare_id, plant_location, vendor_information, cost, available_units, active, inactive;



    public Model_volvo_xc90() {
        // Default constructor required for calls to DataSnapshot.getValue(Model_volvo_xc90.class)
    }

    public Model_volvo_xc90(String spare_name, String spare_id, String plant_location, String vendor_information, String cost, String available_units, String active, String inactive) {
        this.spare_name = spare_name;
        this.spare_id = spare_id;
        this.plant_location = plant_location;
        this.vendor_information = vendor_information;
        this.cost = cost;
        this.available_units = available_units;
        this.active = active;
        this.inactive = inactive;
    }

    public String getSpare_name() {
        return spare_name;
    }

    public void setSpare_name(String spare_name) {
        this.spare_name = spare_name;
    }

    public String getSpare_id() {
        return spare_id;
    }

    public void setSpare_id(String spare_id) {
        this.spare_id = spare_id;
    }

    public String getPlant_location() {
        return plant_location;
    }

    public void setPlant_location(String plant_location) {
        this.plant_location = plant_location;
    }

    public String getvendor_information() {
        return vendor_information;
    }

    public void setvendor_information(String vendor_information) {
        this.vendor_information = vendor_information;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAvailable_units() {
        return available_units;
    }

    public void setAvailable_units(String available_units) {
        this.available_units = available_units;
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
        return "Model_volvo_xc90{" +
                "spare_name='" + spare_name + '\'' +
                ", spare_id='" + spare_id + '\'' +
                ", plant_location='" + plant_location + '\'' +
                ", vendor_information='" + vendor_information + '\'' +
                ", cost='" + cost + '\'' +
                ", available_units='" + available_units + '\'' +
                ", active='" + active + '\'' +
                ", inactive='" + inactive + '\'' +
                '}';
    }
}