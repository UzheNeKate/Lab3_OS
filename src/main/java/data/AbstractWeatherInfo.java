package data;

public abstract class AbstractWeatherInfo {

    Location location;
    boolean fromCache = false;

    public Location getLocation() {
        return location;
    }

    public boolean getFromCache(){
        return fromCache;
    }

    public void setWrittenToCache(){
        fromCache = true;
    }
}

