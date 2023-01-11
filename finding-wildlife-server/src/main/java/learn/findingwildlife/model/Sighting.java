package learn.findingwildlife.model;


import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDate;
import java.time.LocalTime;

public class Sighting {
    private int sightingId;
    private Organism organism;
    private int organismId;
    private Park park;
    private int parkId;
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private String comments;
    private double latitude;
    private double longitude;
    private int appUserId;

    public Sighting() {
    }

    public Sighting(int sightingId, int organismId, int parkId) {
        this.sightingId = sightingId;
        this.organismId = organismId;
        this.parkId = parkId;
    }

    public Sighting(int sightingId, Organism organism, Park park) {
        this.sightingId = sightingId;
        this.organism = organism;
        this.park = park;
    }

    public Sighting(int sightingId, Organism organism, Park park, LocalDate date) {
        this.sightingId = sightingId;
        this.organism = organism;
        this.park = park;
        this.date = date;
    }

    public Sighting(int sightingId, Organism organism, Park park, int appUserId) {
        this.sightingId = sightingId;
        this.organism = organism;
        this.park = park;
        this.appUserId = appUserId;
    }

    public int getSightingId() {
        return sightingId;
    }

    public void setSightingId(int sightingId) {
        this.sightingId = sightingId;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;

    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public int getOrganismId() {
        return organismId;
    }
    public void setOrganismId(int organismId) {
        this.organismId = organismId;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public Park getPark() {
        return park;
    }
    public void setPark(Park park) {
        this.park = park;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

}
