package learn.findingwildlife.model;


import java.util.ArrayList;
import java.util.List;

public class Organism {
    private int organismId;
    private String scientificName;
    private String commonName;
    private String category;
    private List<OrganismPark> parks = new ArrayList<>();
    private int appUserId;

    public Organism() {
    }

    public Organism(int organismId, String commonName) {
        this.organismId = organismId;
        this.commonName = commonName;
    }

    public int getOrganismId() {
        return organismId;
    }

    public void setOrganismId(int organismId) {
        this.organismId = organismId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<OrganismPark> getParks() {
        return new ArrayList<>(parks);
    }

    public void setParks(List<OrganismPark> parks) {
        this.parks = parks;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }
}
