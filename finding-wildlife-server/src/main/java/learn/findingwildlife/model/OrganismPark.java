package learn.findingwildlife.model;

public class OrganismPark {

    private int organismId;
    private String abundance;
    private Nativeness nativeness;
    private Park park;

    public int getOrganismId() {
        return organismId;
    }

    public void setOrganismId(int organismId) {
        this.organismId = organismId;
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

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }
}
