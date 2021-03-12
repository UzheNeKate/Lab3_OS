package data;

public class Location {
    String name;
    String country;

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Location(Location location){
        this.name = location.getName();
        this.country = location.getCountry();
    }
}
