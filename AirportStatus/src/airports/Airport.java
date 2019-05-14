package airports;

public class Airport {
  private final String name;
  private String code;
  private String city;
  private String state;
  private boolean delay;
  private String temperature;

  public Airport(String airportName) {
    name = airportName;
  }

  public Airport(String airportName, String airportCode, String airportCity, String airportState, boolean airportDelay, String airportTemperature) {
    name = airportName;
    code = airportCode;
    city = airportCity;
    state = airportState;
    delay = airportDelay;
    temperature = airportTemperature;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public boolean isDelay() {
    return delay;
  }

  public String getTemperature() {
    return temperature;
  }

}
