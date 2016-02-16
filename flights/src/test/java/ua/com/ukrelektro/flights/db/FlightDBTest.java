package ua.com.ukrelektro.flights.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import ua.com.ukrelektro.flights.db.helpers.impl.FlightDBImpl;
import ua.com.ukrelektro.flights.db.models.Aircraft;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.params.SearchParam;

public class FlightDBTest {

	private static final long ID2 = 222;

	private static final long ID1 = 111;

	FlightDBImpl flightDB = FlightDBImpl.getInstance();

	private Date dateDepart1;

	private Date dateCome1;

	private Date dateDepart2;

	private Date dateCome2;

	private City cityTelAviv;

	private City cityKiev;

	private Aircraft aircraft;

	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	private Flight flight1;
	private Flight flight2;

	/**
	 * The helper here is intentionally use 0 for the percentage, since we test
	 * our global queries.
	 */
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(0));

	Closeable session;

	@BeforeClass
	public static void setUpBeforeClass() {
		// Reset the Factory so that all translators work properly.
		ObjectifyService.setFactory(new ObjectifyFactory());
	}

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		session = ObjectifyService.begin();
		initDB();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
		session.close();
	}

	private void initDB() throws ParseException {
		Country countryFromIsr = new Country("Israel", "ISR");
		Country countryToUkr = new Country("Ukraine", "UKR");
		ofy().save().entities(countryFromIsr, countryToUkr);

		countryFromIsr = ofy().load().entity(countryFromIsr).now();
		countryToUkr = ofy().load().entity(countryToUkr).now();

		cityTelAviv = new City(61000, "Tel-Aviv", countryFromIsr);
		cityKiev = new City(41000, "Kiev", countryToUkr);
		ofy().save().entities(countryFromIsr, countryToUkr, cityTelAviv, cityKiev);

		cityTelAviv = ofy().load().entity(cityTelAviv).now();
		cityKiev = ofy().load().entity(cityKiev).now();

		aircraft = new Aircraft(777, "New company", "Model 1", 2);
		ofy().save().entity(aircraft);

		aircraft = ofy().load().entity(aircraft).now();

		dateDepart1 = dateFormat.parse("15/02/2016");
		dateCome1 = dateFormat.parse("16/02/2016");

		dateDepart2 = dateFormat.parse("17/02/2016");
		dateCome2 = dateFormat.parse("18/02/2016");

		flight1 = new Flight(ID1, "AAA", dateDepart1, dateCome1, cityTelAviv, cityKiev, aircraft);
		flight2 = new Flight(ID2, "BBB", dateDepart2, dateCome2, cityKiev, cityTelAviv, aircraft);

		ofy().save().entities(flight1, flight2).now();
		// flight1 = ofy().load().entity(flight1).now();
		// flight2 = ofy().load().entity(flight2).now();

	}

	@Test
	public void test_getAllFlight() {
		List<Flight> flights = flightDB.getAllFlight();
		assertFalse(flights.isEmpty());
		assertEquals(2, flights.size());
		assertTrue(flights.contains(flight1));
		assertTrue(flights.contains(flight2));
	}

	@Test
	public void test_getFlightByID_CorrectID() throws NotFoundException {
		Flight flightTest = flightDB.getFlightById(ID1);

		assertNotNull(flightTest);
		assertEquals(flight1, flightTest);
	}

	@Test(expected = NotFoundException.class)
	public void test_getFlightByID_illegalID() throws NotFoundException {
		long illegalId = 123123123;
		flightDB.getFlightById(illegalId);
	}

	@Test
	public void test_getFlightsByParam_EmptyParam_ExpectedAllFlights() throws NotFoundException {
		SearchParam param = new SearchParam();

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(2, flightsResult.size());
		assertTrue(flightsResult.contains(flight1));
		assertTrue(flightsResult.contains(flight2));
	}

	@Test
	public void test_getFlightsByParam_FromCountryName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setFromCountryName("Ukraine");

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(1, flightsResult.size());
		assertTrue(flightsResult.contains(flight2));
	}

	@Test(expected = NotFoundException.class)
	public void test_getFlightsByParam_FromCountryName_IllegalCountryName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setFromCountryName("SOME COUNTRY NAME");
		flightDB.getFlightsByFlightParam(param);
	}

	@Test(expected = NotFoundException.class)
	public void test_getFlightsByParam_ToCountryName_IllegalCountryName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setToCountryName("SOME COUNTRY NAME");
		flightDB.getFlightsByFlightParam(param);
	}

	@Test
	public void test_getFlightsByParam_ToCountryName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setToCountryName("Ukraine");

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(1, flightsResult.size());
		assertTrue(flightsResult.contains(flight1));
	}

	@Test
	public void test_getFlightsByParam_FromCityName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setFromCityName("Kiev");

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(1, flightsResult.size());
		assertTrue(flightsResult.contains(flight2));
	}

	@Test
	public void test_getFlightsByParam_ToCityName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setToCityName("Kiev");

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(1, flightsResult.size());
		assertTrue(flightsResult.contains(flight1));
	}

	@Test(expected = NotFoundException.class)
	public void test_getFlightsByParam_FromCityName_IllegalCountryName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setFromCityName("SOME COUNTRY NAME");
		flightDB.getFlightsByFlightParam(param);
	}

	@Test(expected = NotFoundException.class)
	public void test_getFlightsByParam_ToCityName_IllegalCountryName() throws NotFoundException {
		SearchParam param = new SearchParam();
		param.setToCityName("SOME COUNTRY NAME");
		flightDB.getFlightsByFlightParam(param);
	}

	@Test
	public void test_getFlightsByParam_FromDate() throws NotFoundException, ParseException {
		SearchParam param = new SearchParam();
		param.setFromDate(dateFormat.parse("16/02/2016"));

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(1, flightsResult.size());
		assertTrue(flightsResult.contains(flight2));
	}

	@Test
	public void test_getFlightsByParam_ToDate() throws NotFoundException, ParseException {
		SearchParam param = new SearchParam();
		param.setToDate(dateFormat.parse("16/02/2016"));

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(1, flightsResult.size());
		assertTrue(flightsResult.contains(flight1));
	}

	@Test
	public void test_getFlightsByParam_fromDate_expectedEmptyList() throws NotFoundException, ParseException {
		SearchParam param = new SearchParam();
		param.setFromDate(dateFormat.parse("19/01/2099"));

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertTrue(flightsResult.isEmpty());
	}

	@Test
	public void test_getFlightsByParam_AllParam() throws NotFoundException, ParseException {
		SearchParam param = new SearchParam();
		param.setFromCountryName("Ukraine");
		param.setFromCityName("Kiev");
		param.setToCountryName("Israel");
		param.setToCityName("Tel-Aviv");

		param.setFromDate(dateFormat.parse("16/01/2016"));
		param.setToDate(dateFormat.parse("19/01/2016"));

		List<Flight> flightsResult = flightDB.getFlightsByFlightParam(param);
		assertFalse(flightsResult.isEmpty());
		assertEquals(1, flightsResult.size());
		assertTrue(flightsResult.contains(flight2));
	}
}