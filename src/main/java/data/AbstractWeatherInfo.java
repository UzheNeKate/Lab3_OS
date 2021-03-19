package data;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractWeatherInfo {

    @Getter
    Location location;

    @Setter
    @Getter
    Boolean fromCache = false;

    public void setWrittenToCache() {
        fromCache = true;
    }
}
