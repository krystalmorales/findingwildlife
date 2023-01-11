package learn.findingwildlife.model;


import java.util.ArrayList;
import java.util.List;


public class Park {
    private int parkId;
    private String parkName;

    private double centerLatitude;

    private double centerLongitude;

    private List<Trail> trails = new ArrayList<>();


    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }


    public List<Trail> getTrails() {
        return new ArrayList<>(trails);
    }

    public void setTrails(List<Trail> trails) {
        this.trails = trails;
    }
}
