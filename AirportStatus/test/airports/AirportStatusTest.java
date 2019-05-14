package airports;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirportStatusTest {

	private AirportStatus airportStatus;
	private AirportDataProvider airportDataProviderMock;

	private Airport iah = new Airport("George Bush Intercontinental");
	private Airport iad = new Airport("Washington Dulles Intl");
	private Airport ord = new Airport("Chicago O'hare Intl");

	@BeforeEach
	public void setUp() {
		airportStatus = new AirportStatus();
		airportDataProviderMock = mock(AirportDataProvider.class);
		airportStatus.setAirportService(airportDataProviderMock);
	}

	@Test
	public void canaryTest() {
		assertTrue(true);
	}

	@Test
	public void airportsAreSorted() {
		assertAll(
			() -> assertEquals(List.of(),
				airportStatus.sortAirports(List.of())),
			() -> assertEquals(List.of(iah),
				airportStatus.sortAirports(List.of(iah))),
			() -> assertEquals(List.of(iah, iad),
				airportStatus.sortAirports(List.of(iad, iah))),
			() -> assertEquals(List.of(iah, iad),
				airportStatus.sortAirports(List.of(iah, iad))),
			() -> assertEquals(List.of(ord, iah, iad),
				airportStatus.sortAirports(List.of(iad, iah, ord))));
	}
	
	@Test
	public void emptyListOfAirportsIfNoAirportCodeGiven() {
		assertEquals(List.of(),
			airportStatus.getAirportsStatus(List.of()).airports);
	}
	
	@Test
	public void getOneAirportIfOneValidAirportCodeGiven() {
		when(airportDataProviderMock.fetchAirportData("IAH"))
			.thenReturn(iah);
		
		assertEquals(List.of(iah),
			airportStatus.getAirportsStatus(List.of("IAH")).airports);
	}
	
	@Test
	public void getTwoAirportsIfTwoValidAirportCodesGiven() {
		when(airportDataProviderMock.fetchAirportData("IAH"))
			.thenReturn(iah);
		when(airportDataProviderMock.fetchAirportData("IAD"))
			.thenReturn(iad);
		
		assertEquals(List.of(iah, iad),
			airportStatus.getAirportsStatus(List.of("IAH", "IAD")).airports);
	}
	
	@Test
	public void twoAirportsInSortedOrderWhenTwoAirportCodeGiven(){
		when(airportDataProviderMock.fetchAirportData("IAH"))
			.thenReturn(iah);
		when(airportDataProviderMock.fetchAirportData("IAD"))
			.thenReturn(iad);
		
		assertEquals(List.of(iah, iad),
			airportStatus.getAirportsStatus(List.of("IAD", "IAH")).airports);
	}
	
	@Test
	public void threeAirportsInSortedOrderWhenThreeAirportCodeGiven(){
		when(airportDataProviderMock.fetchAirportData("IAH"))
			.thenReturn(iah);
		when(airportDataProviderMock.fetchAirportData("IAD"))
			.thenReturn(iad);
		when(airportDataProviderMock.fetchAirportData("ORD"))
			.thenReturn(ord);
		
		assertEquals(List.of(ord, iah, iad),
			airportStatus.getAirportsStatus(List.of("IAD", "ORD", "IAH")).airports);
	}
	
	@Test
	public void oneAirportCodeGivenIsInvalid() {
		String invalidCode = "INVALID";
		
		when(airportDataProviderMock.fetchAirportData(invalidCode))
			.thenThrow(new RuntimeException("Invalid Code"));
		
		assertEquals(List.of(invalidCode),
			airportStatus.getAirportsStatus(
			List.of(invalidCode)).airportCodesWithError);
	}
	
	@Test
	public void twoAirportCodesGivenSecondIsInvalid(){
		String invalidCode = "INVALID";
		
		when(airportDataProviderMock.fetchAirportData("IAH"))
			.thenReturn(iah);
		when(airportDataProviderMock.fetchAirportData(invalidCode))
			.thenThrow(new RuntimeException("Invalid Code"));
		
		AirportStatusResult result =
			airportStatus.getAirportsStatus(List.of("IAH", invalidCode));
		
		assertEquals(List.of(invalidCode), result.airportCodesWithError);
		assertEquals(List.of(iah), result.airports);
	}
	
	@Test
	public void threeAirportCodesGivenSecondIsInvalid() {
		String invalidCode = "INVALID";
		
		when(airportDataProviderMock.fetchAirportData("IAH"))
			.thenReturn(iah);
		when(airportDataProviderMock.fetchAirportData(invalidCode))
			.thenThrow(new RuntimeException("Invalid Code"));
		when(airportDataProviderMock.fetchAirportData("IAD"))
			.thenReturn(iad);
		
		AirportStatusResult result =
			airportStatus.getAirportsStatus(List.of("IAH", invalidCode, "IAD"));
		
		assertEquals(List.of(invalidCode), result.airportCodesWithError);
		assertEquals(List.of(iah,iad), result.airports);
	}
	
	@Test
	public void threeAirportCodesGivenFirstIsInvalidAndThirdRunsIntoNetworkError() {
		String invalidCode = "INVALID";
		String iahCode = "IAH";
		
		when(airportDataProviderMock.fetchAirportData(invalidCode))
			.thenThrow(new RuntimeException("Invalid Code"));
		when(airportDataProviderMock.fetchAirportData("IAD"))
			.thenReturn(iad);
		when(airportDataProviderMock.fetchAirportData(iahCode))
			.thenThrow(new RuntimeException("Network Error"));
		
		AirportStatusResult result =
			airportStatus.getAirportsStatus(List.of(invalidCode, "IAD", iahCode));
		
		assertEquals(List.of(invalidCode, iahCode), result.airportCodesWithError);
		assertEquals(List.of(iad), result.airports);
	}
	
}