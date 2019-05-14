package airports;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FAAAirportInfoServiceTest {
	
	private FAAAirportInfoService faaAirportInfoService;
	private String validJSON;
	private String airportCode;
	
	@BeforeEach
	public void setUp() {
		faaAirportInfoService = new FAAAirportInfoService();
		airportCode = "IAH";
		validJSON = "{\"Name\":\"George Bush Intercontinental/houston\",\"City\":\"Houston\",\"State\":\"TX\",\"ICAO\":\"KIAH\",\"IATA\":\"IAH\",\"SupportedAirport\":false,\"Delay\":false,\"DelayCount\":0,\"Status\":[{\"Reason\":\"No known delays for this airport\"}],\"Weather\":{\"Weather\":[{\"Temp\":[\"Partly Cloudy\"]}],\"Visibility\":[10.00],\"Meta\":[{\"Credit\":\"NOAA's National Weather Service\",\"Url\":\"http://weather.gov/\",\"Updated\":\"Last Updated on Feb 24 2019, 6:53 am CST\"}],\"Temp\":[\"49.0 F (9.4 C)\"],\"Wind\":[\"North at 11.5\"]}}";
	}
	
	@Test
	public void givenAirportCodeReturnJSON() throws IOException {
		
		assertTrue(faaAirportInfoService.getJSON(airportCode)
			.contains("\"City\":\"Houston\""));
	}
	
	@Test
	public void createAirportGivenValidJSON() {
		Airport airport = faaAirportInfoService.createAirport(validJSON);
		
		assertEquals(airportCode, airport.getCode());
		assertEquals("Houston", airport.getCity());
		assertEquals("TX", airport.getState());
		assertFalse(airport.isDelay());
		assertEquals("49.0 F (9.4 C)", airport.getTemperature());
		assertEquals("George Bush Intercontinental/houston", airport.getName());
	}
	
	@Test
	public void throwsExceptionIfAirportNameIsNotAvailable() {
		String invalidJSON = "{\"SupportedAirport\":false,\"Delay\":false,\"DelayCount\":0,\"Status\":[{\"Type\":\"\",\"AvgDelay\":\"\",\"ClosureEnd\":\"\",\"ClosureBegin\":\"\",\"MinDelay\":\"\",\"Trend\":\"\",\"MaxDelay\":\"\",\"EndTime\":\"\"}]}";
		
		assertThrows(RuntimeException.class,
			() -> faaAirportInfoService.createAirport(invalidJSON));
	}


	@Test
	public void fetchAirportDataThrowsExceptionFromCreateAirport() throws IOException {
		
		FAAAirportInfoService faaAirportInfoServiceMock = mock(FAAAirportInfoService.class);
		when(faaAirportInfoServiceMock.getJSON(airportCode)).thenReturn(validJSON);
		when(faaAirportInfoServiceMock.createAirport(validJSON))
			.thenThrow(new RuntimeException("Error Parsing JSON"));
		when(faaAirportInfoServiceMock.fetchAirportData(airportCode))
			.thenCallRealMethod();
		
		assertThrows(RuntimeException.class,
			() -> faaAirportInfoServiceMock.fetchAirportData(airportCode));
		verify(faaAirportInfoServiceMock).createAirport(validJSON);
	}
	
	@Test
	public void fetchAirportDataThrowsExceptionFromGetJSON() throws IOException {
		String invalidCode = "\\";
		FAAAirportInfoService faaAirportInfoServiceMock = mock(FAAAirportInfoService.class);
	  when(faaAirportInfoServiceMock.getJSON(invalidCode))
			.thenThrow(new IOException());
		when(faaAirportInfoServiceMock.fetchAirportData(invalidCode))
				.thenCallRealMethod();

		assertThrows(RuntimeException.class,
			() -> faaAirportInfoServiceMock.fetchAirportData(invalidCode));
		verify(faaAirportInfoServiceMock).getJSON(invalidCode);
	}
	
	@Test
	public void verifyFetchAirportDataCallsGetJSON() throws IOException {
		
		FAAAirportInfoService faaAirportInfoServiceMock = Mockito.mock(FAAAirportInfoService.class);
		
		when(faaAirportInfoServiceMock.getJSON(airportCode))
			.thenReturn(validJSON);
		when(faaAirportInfoServiceMock.fetchAirportData(airportCode))
			.thenCallRealMethod();
		
		faaAirportInfoServiceMock.fetchAirportData(airportCode);
		
		verify(faaAirportInfoServiceMock).getJSON(airportCode);
	}
	
	@Test
	public void verifyFetchAirportDataCallsCreateAirport() throws IOException {
		
		FAAAirportInfoService faaAirportInfoServiceMock = Mockito.mock(FAAAirportInfoService.class);
		
		when(faaAirportInfoServiceMock.getJSON(airportCode))
			.thenReturn(validJSON);
		when(faaAirportInfoServiceMock.createAirport(validJSON))
			.thenReturn(new Airport("George Bush Intercontinental/houston"));
		when(faaAirportInfoServiceMock.fetchAirportData(airportCode))
			.thenCallRealMethod();
		
		faaAirportInfoServiceMock.fetchAirportData(airportCode);
		
		verify(faaAirportInfoServiceMock).createAirport(validJSON);
	}
	
	@Test
	public void fetchDataReturnsAirportReturnedByCreateAirport() throws IOException {
		
		Airport sampleAirport = new Airport("George Bush Intercontinental");
		
		FAAAirportInfoService faaAirportInfoServiceMock = Mockito.mock(FAAAirportInfoService.class);
		
		when(faaAirportInfoServiceMock.getJSON(airportCode)).thenReturn(validJSON);
		when(faaAirportInfoServiceMock.createAirport(validJSON))
			.thenReturn(sampleAirport);
		when(faaAirportInfoServiceMock.fetchAirportData(airportCode))
			.thenCallRealMethod();
		
		assertEquals(sampleAirport, faaAirportInfoServiceMock.fetchAirportData(airportCode));
		
	}
	
}
