
package com.cohajj.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostReport {

    @SerializedName("roadID")
    @Expose
    private Integer roadID;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("EndTime")
    @Expose
    private String endTime;
    @SerializedName("duration")
    @Expose
    private Double duration;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("numOfPeople")
    @Expose
    private Integer numOfPeople;
    @SerializedName("emerg_id")
    @Expose
    private Integer emergId;

    public Integer getRoadID() {
        return roadID;
    }

    public void setRoadID(Integer roadID) {
        this.roadID = roadID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(Integer numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public Integer getEmergId() {
        return emergId;
    }

    public void setEmergId(Integer emergId) {
        this.emergId = emergId;
    }

}
