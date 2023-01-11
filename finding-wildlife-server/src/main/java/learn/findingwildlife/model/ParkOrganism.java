package learn.findingwildlife.model;

public class ParkOrganism {


    private int parkOrganismId;
    private int parkId;

    private Organism organism;
    private String abundance;
    private Nativeness nativeness;
    private int appUserId;


    public int getParkOrganismId() {
        return parkOrganismId;
    }

    public void setParkOrganismId(int parkOrganismId) {
        this.parkOrganismId = parkOrganismId;
    }

    public int getParkId() {
        return parkId;

    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public String getAbundance() {
        return abundance;
    }

    public void setAbundance(String abundance) {
        this.abundance = abundance;
    }

    public Nativeness getNativeness() {
        return nativeness;
    }

    public void setNativeness(Nativeness nativeness) {
        this.nativeness = nativeness;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }
}
