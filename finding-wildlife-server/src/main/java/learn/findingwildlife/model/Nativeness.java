package learn.findingwildlife.model;

public enum Nativeness {

    NATIVE("Native"),

    NONNATIVE("Non-native"),
    UNKNOWN("Unknown");

    private final String name;

    Nativeness(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Nativeness findByName(String name) {

        for(Nativeness nativeness : Nativeness.values()) {
            if(nativeness.getName().equalsIgnoreCase(name)) {
                return nativeness;
            }
        }
        String message = "Current input for nativeness does not exist";
        throw new RuntimeException(message);
    }
}
