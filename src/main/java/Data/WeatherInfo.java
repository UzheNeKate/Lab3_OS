package Data;

public class WeatherInfo {
    Weather current;
    Location location;

    public Weather getWeather() {
        return current;
    }

    public void setWeather(Weather weather) {
        this.current = weather;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

