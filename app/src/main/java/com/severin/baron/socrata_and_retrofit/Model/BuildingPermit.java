package com.severin.baron.socrata_and_retrofit.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by erikrudie on 8/9/16.
 */
public class BuildingPermit {

    // @SerializedName contains a JSON key connected to the value with
    // which a variable will be inflated
    @SerializedName("permit_type")
    private String permit_type;
    @SerializedName("category")
    private String category;
    @SerializedName("value")
    private int value;
    // BuildingLocation is a custom class, and so requires its own
    // @SerializedName annotations
    @SerializedName("location")
    private BuildingLocation location;
    @SerializedName("work_type")
    private String work_type;
    @SerializedName("description")
    private String description;

    // Note that no specialized constructor or setters are required

    public String getPermit_type() {
        return permit_type;
    }

    public String getCategory() {
        return category;
    }

    public int getValue() {
        return value;
    }

    public String getLocationType() {
        return location.getType();
    }

    public double[] getCoordinates() {
        return location.getCoordinates();
    }

    public String getWork_type() {
        return work_type;
    }

    public BuildingLocation getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
