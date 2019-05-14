package airports;

import java.util.*;

public class AirportStatusResult {
	public final List<Airport> airports;
	public final List<String> airportCodesWithError;
	
	public AirportStatusResult(
	  List<Airport> airportList, List<String> codesWithError) {
		airports = airportList;
		airportCodesWithError = codesWithError;
	}
}
