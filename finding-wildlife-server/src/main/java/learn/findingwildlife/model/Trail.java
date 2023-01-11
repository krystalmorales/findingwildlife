package learn.findingwildlife.model;

public class Trail {
    private int trailId;
    private String trailName;
    private int trailNumber;
    private double distance;

    private int parkId;

    public Trail() {
    }

    public Trail(int trailId, String trailName, int parkId) {
        this.trailId = trailId;
        this.trailName = trailName;
        this.parkId = parkId;
    }

    public int getTrailId() {
        return trailId;
    }

    public void setTrailId(int trailId) {
        this.trailId = trailId;
    }

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public int getTrailNumber() {
        return trailNumber;
    }

    public void setTrailNumber(int trailNumber) {
        this.trailNumber = trailNumber;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }
}
