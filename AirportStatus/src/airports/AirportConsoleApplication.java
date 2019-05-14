package airports;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.util.stream.Collectors.*;

public class AirportConsoleApplication {
	private static String filePath;
	private static List<String> codes;

	public static void main(String[] args) {
		filePath = args[0];
		
		if (filePath == null) {
			System.out.println("Please provide valid file path.");
			return;
		}

		try {
			codes = readAirportCodeFromFile();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		AirportStatus airportStatus = new AirportStatus();
		airportStatus.setAirportService(new FAAAirportInfoService());
		printFormattedOutput(airportStatus.getAirportsStatus(codes));
	}
	
	private static List<String> readAirportCodeFromFile() throws IOException {
			return Files.lines(Paths.get(filePath)).collect(toList());
	}
	
	private static void printFormattedOutput(AirportStatusResult airportStatusResult) {
		String format = "%1$-50s %2$-20s %3$-15s %4$-20s %5$-5s\n";
		System.out.format(format, "Name", "City", "State", "Temperature", "Delay");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		
		for (Airport airport : airportStatusResult.airports) {
			System.out.format(format, airport.getName(), airport.getCity(), airport.getState(),
				airport.getTemperature(), airport.isDelay() ? "^-^" : "");
		}
		
		System.out.println("\n\n");
		System.out.println("Error getting details for:");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for (String code : airportStatusResult.airportCodesWithError) {
			System.out.println(code);
		}
	}
}
