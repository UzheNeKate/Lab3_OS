package data;

import lombok.Getter;

public class Location {
    @Getter
    String name;
    @Getter
    String country;

    public Location(Location location){
        this.name = location.getName();
        this.country = location.getCountry();
    }
}
