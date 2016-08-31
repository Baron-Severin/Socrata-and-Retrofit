package com.severin.baron.socrata_and_retrofit.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by erikrudie on 8/9/16.
 */
public class BuildingLocation {

    @SerializedName("type")
    private String type;
    @SerializedName("coordinates")
    private double[] coordinates;


    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }
}
