package com.suchith.volvo_new;

import androidx.annotation.Keep;

@Keep
public class Model_aa_nuts_bolts {

    private String asset_name, asset_id, plant_location, rack_location, cost, available_units, active, inactive;

    public Model_aa_nuts_bolts() {
        // Default constructor required for calls to DataSnapshot.getValue(Model_aa_nuts_bolts.class)
    }

    public Model_aa_nuts_bolts(String asset_name, String asset_id, String plant_location, String rack_location, String cost, String available_units, String active, String inactive) {
        this.asset_name = asset_name;
        this.asset_id = asset_id;
        this.plant_location = plant_location;
        this.rack_location = rack_location;
        this.cost = cost;
        this.available_units = available_units;
        this.active = active;
        this.inactive = inactive;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public void setAsset_name(String asset_name) {
        this.asset_name = asset_name;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getPlant_location() {
        return plant_location;
    }

    public void setPlant_location(String plant_location) {
        this.plant_location = plant_location;
    }

    public String getRack_location() {
        return rack_location;
    }

    public void setRack_location(String rack_location) {
        this.rack_location = rack_location;
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
        return "Model_aa_nuts_bolts{" +
                "asset_name='" + asset_name + '\'' +
                ", asset_id='" + asset_id + '\'' +
                ", plant_location='" + plant_location + '\'' +
                ", rack_location='" + rack_location + '\'' +
                ", cost='" + cost + '\'' +
                ", available_units='" + available_units + '\'' +
                ", active='" + active + '\'' +
                ", inactive='" + inactive + '\'' +
                '}';
    }
}