package ua.com.ukrelektro.flights.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import ua.com.ukrelektro.flights.db.models.Aircraft;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;

public class FlightTest {

	private static final long ID = 10011;

	private static final String CODE = "SOME CODE";

	private static final Date DATE_DEPART = new Date(1454338800000L);

	private static final Date DATE_COME = new Date(1454440000000L);

	private City cityFromTelAviv;

	private City cityToKiev;

	private Aircraft aircraft;

	private Flight flight;

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

	private void initDB() {
		Country countryFromIsr = new Country("Israel", "ISR");
		Country countryToUkr = new Country("Ukraine", "UKR");
		ofy().save().entities(countryFromIsr, countryToUkr);

		countryFromIsr = ofy().load().entity(countryFromIsr).now();
		countryToUkr = ofy().load().entity(countryToUkr).now();

		cityFromTelAviv = new City(61000, "Tel-Aviv", countryFromIsr);
		cityToKiev = new City(41000, "Kiev", countryToUkr);
		ofy().save().entities(countryFromIsr, countryToUkr, cityFromTelAviv, cityToKiev);

		cityFromTelAviv = ofy().load().entity(cityFromTelAviv).now();
		cityToKiev = ofy().load().entity(cityToKiev).now();

		aircraft = new Aircraft(777, "New company", "Model 1", 2);
		ofy().save().entity(aircraft);

		aircraft = ofy().load().entity(aircraft).now();

		flight = new Flight(ID, CODE, DATE_DEPART, DATE_COME, cityFromTelAviv, cityToKiev, aircraft);
		ofy().save().entity(flight);

		flight = ofy().load().entity(flight).now();
		System.err.println("sasd");
	}

	@Test
	public void testGetters() throws Exception {
		assertEquals(aircraft, flight.getAircraft());
		assertEquals(aircraft.getName(), flight.getAircraftName());
		assertEquals(cityFromTelAviv, flight.getCityFrom());
		assertEquals(cityToKiev, flight.getCityTo());
		assertEquals(CODE, flight.getCode());
		assertEquals(DATE_COME, flight.getDateCome());
		assertEquals(DATE_DEPART, flight.getDateDepart());
		assertEquals(aircraft.getPlaces(), flight.getFreePlaces());
		assertEquals(ID, flight.getId());
	}

	@Test
	public void testSetters() throws Exception {
		Aircraft newAircraft = new Aircraft(123, "sdaa", "mod1", 10);
		ofy().save().entity(newAircraft);
		newAircraft = ofy().load().entity(newAircraft).now();
		long newID = 123123;

		flight.setAircraft(newAircraft);
		flight.setId(newID);

		assertEquals(newAircraft, flight.getAircraft());
		assertEquals(newID, flight.getId());
	}

	@Test
	public void test_bookPlaces_CorrectArgs() {
		int beforeBooking = flight.getFreePlaces();
		flight.bookPlaces(1);
		assertEquals(beforeBooking - 1, flight.getFreePlaces());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_bookPlaces_IllegalArgs_ExeptionExpected() {
		int beforeBooking = flight.getFreePlaces();
		flight.bookPlaces(beforeBooking + 1);
	}

	@Test
	public void test_isExistFreePlaces_expectedTrue() {
		assertTrue(flight.isExistFreePlaces());
	}

	@Test
	public void test_isExistFreePlacesAndBookPlaces_expectedTrueAndFalse() {
		assertTrue(flight.isExistFreePlaces());

		int freePlaces = flight.getFreePlaces();
		flight.bookPlaces(freePlaces);

		assertFalse(flight.isExistFreePlaces());
	}

	@Test
	public void test_giveBackPlaces() {
		int beforeBooking = flight.getFreePlaces();
		flight.bookPlaces(1);
		assertEquals(beforeBooking - 1, flight.getFreePlaces());

		flight.giveBackPlaces(1);
		assertEquals(beforeBooking, flight.getFreePlaces());

	}

	@Test
	public void test_getWebsafeKey() {
		String keyFlight = Key.create(Flight.class, ID).getString();
		assertEquals(keyFlight, flight.getWebsafeKey());
	}

}