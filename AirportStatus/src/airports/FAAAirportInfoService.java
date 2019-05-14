package airports;

import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class FAAAirportInfoService implements AirportDataProvider {

	public String getJSON(String airportCode) throws IOException {
		String faaServiceUrl = "https://soa.smext.faa.gov/asws/api/airport/status/";

		var faaURL = faaServiceUrl + airportCode;

		try (Scanner scanner = new Scanner(new URL(faaURL).openStream())) {
			return scanner.nextLine();
		}
	}

	public Airport createAirport(String responseString) {
		JsonObject jsonObject = new Gson().fromJson(responseString, JsonObject.class);

		String name = jsonObject.get("Name").getAsString();
		String city = jsonObject.get("City").getAsString();
		String state = jsonObject.get("State").getAsString();
		boolean delay = jsonObject.get("Delay").getAsBoolean();
		String code = jsonObject.get("IATA").getAsString();
		String temperature = jsonObject.get("Weather").getAsJsonObject().get("Temp")
			.getAsJsonArray().get(0).getAsString();

		return new Airport(name, code, city, state, delay, temperature);
	}

	@Override
	public Airport fetchAirportData(String airportCode) {
		try {
			return createAirport(getJSON(airportCode));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
