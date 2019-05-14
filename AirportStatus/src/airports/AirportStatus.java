package airports;

import java.util.*;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class AirportStatus {
	
	private AirportDataProvider service;
	
	public void setAirportService(AirportDataProvider airportDataProvider) {
		service = airportDataProvider;
	}
	
	public List<Airport> sortAirports(List<Airport> airports) {
		return airports.stream()
			.sorted(comparing(Airport::getName))
			.collect(toList());
	}
	
	public AirportStatusResult getAirportsStatus(List<String> airportCodes) {
    var airports = new ArrayList<Airport>();
    var codesWithError = new ArrayList<String>();

		for (String code : airportCodes) {
			try {
				Airport airport = service.fetchAirportData(code);
				airports.add(airport);
			} catch (RuntimeException ex) {
				codesWithError.add(code);
			}
		}

		return new AirportStatusResult(sortAirports(airports), codesWithError);
  }

}
