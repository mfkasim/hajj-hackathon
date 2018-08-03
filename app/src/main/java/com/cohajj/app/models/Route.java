
package com.cohajj.app.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Route {

    @SerializedName("road")
    @Expose
    private Integer road;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("averageTime")
    @Expose
    private Double averageTime;

    List<LatLng> pointList;


    public Integer getRoad() {
        return road;
    }

    public void setRoad(Integer road) {
        this.road = road;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Double averageTime) {
        this.averageTime = averageTime;
    }

    public List<LatLng> getPointList() {

        if (pointList == null)
            pointList = new ArrayList<>();
        return pointList;
    }

    public void setPointList(List<LatLng> pointList) {
        this.pointList = pointList;
    }
}
