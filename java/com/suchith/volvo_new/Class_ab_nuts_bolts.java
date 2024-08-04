package com.suchith.volvo_new;

public class Class_ab_nuts_bolts {
    private String Employee_Name;
    private String Asset_ID;
    private String Rack_Location;
    private String Quantities;
    private String farmDate;

    // Default constructor required for calls to DataSnapshot.getValue(Class_ab_nuts_bolts.class)
    public Class_ab_nuts_bolts() {
    }

    public Class_ab_nuts_bolts(String employee_Name, String asset_ID, String rack_Location, String quantities, String farmDate) {
        Employee_Name = employee_Name;
        Asset_ID = asset_ID;
        Rack_Location = rack_Location;
        Quantities = quantities;
        this.farmDate = farmDate;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }

    public String getAsset_ID() {
        return Asset_ID;
    }

    public void setAsset_ID(String asset_ID) {
        Asset_ID = asset_ID;
    }

    public String getRack_Location() {
        return Rack_Location;
    }

    public void setRack_Location(String rack_Location) {
        Rack_Location = rack_Location;
    }

    public String getQuantities() {
        return Quantities;
    }

    public void setQuantities(String quantities) {
        Quantities = quantities;
    }

    public String getFarmDate() {
        return farmDate;
    }

    public void setFarmDate(String farmDate) {
        this.farmDate = farmDate;
    }
}