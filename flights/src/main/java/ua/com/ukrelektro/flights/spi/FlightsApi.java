package ua.com.ukrelektro.flights.spi;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import ua.com.ukrelektro.flights.Constants;
import ua.com.ukrelektro.flights.db.helpers.CityDB;
import ua.com.ukrelektro.flights.db.helpers.CountyDB;
import ua.com.ukrelektro.flights.db.helpers.FlightDB;
import ua.com.ukrelektro.flights.db.helpers.PassengerDB;
import ua.com.ukrelektro.flights.db.helpers.ReservationDB;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.db.models.Passenger;
import ua.com.ukrelektro.flights.db.models.Reservation;
import ua.com.ukrelektro.flights.params.FlightParam;
import ua.com.ukrelektro.flights.spi.wrappers.WrappedBoolean;

@Api(name = "flights", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Flights Backend application.")
public class FlightsApi {

	private CityDB cityDB = new CityDB();
	private CountyDB countryDB = new CountyDB();
	private FlightDB flightDb = new FlightDB();
	private PassengerDB passengerDB = new PassengerDB();
	private ReservationDB reservationDB = new ReservationDB();

	@ApiMethod(name = "getAllCountries", path = "getAllCountries", httpMethod = HttpMethod.GET)
	public List<Country> getAllCountries() {
		return countryDB.getAllCountry();
	}

	@ApiMethod(name = "getAllCities", path = "getAllCities", httpMethod = HttpMethod.GET)
	public List<City> getAllCities() {
		return cityDB.getAllCities();
	}

	@ApiMethod(name = "getAllCitiesByCountryName", path = "getAllCitiesByCountryName", httpMethod = HttpMethod.GET)
	public List<City> getAllCitiesByCountryName(@Named(value = "country") String countryName) throws NotFoundException {
		return countryDB.getCitiesByCountry(countryName);
	}

	@ApiMethod(name = "getCityByWebsafeKey", path = "getCityByWebsafeKey", httpMethod = HttpMethod.GET)
	public City getCityByWebsafeKey(@Named(value = "websafeCityKey") String websafeCityKey) throws NotFoundException {
		return cityDB.getCityByWebsafeKey(websafeCityKey);
	}

	@ApiMethod(name = "getAllFlights", path = "getAllFlights", httpMethod = HttpMethod.GET)
	public List<Flight> getAllFlights() {
		return flightDb.getAllFlight();
	}

	@ApiMethod(name = "getFlightByKey", path = "getFlightByKey", httpMethod = HttpMethod.GET)
	public Flight getFlightByKey(@Named(value = "websafeFlightKey") String websafeFlightKey) throws NotFoundException {
		return flightDb.getFlightByWebsafeKey(websafeFlightKey);
	}

	@ApiMethod(name = "getFlightsCityToCityByName", path = "getFlightsCityToCityByName", httpMethod = HttpMethod.GET)
	public List<Flight> getFlightsCityToCityByName(@Named(value = "cityFrom") String cityFrom, @Named(value = "cityTo") String cityTo) throws NotFoundException {
		return flightDb.getFlightsCityToCityByName(cityFrom, cityTo);
	}

	@ApiMethod(name = "getFlightsByFlightParam", path = "getFlightsByFlightParam", httpMethod = HttpMethod.POST)
	public List<Flight> getFlightsByFlightParam(FlightParam flightParam) throws NotFoundException {
		return flightDb.getFlightsByFlightParam(flightParam);
	}

	@ApiMethod(name = "registerNewPassenger", path = "registerNewPassenger", httpMethod = HttpMethod.PUT)
	public Passenger registerNewPassenger(final User user, @Named(value = "givenName") String givenName, @Named(value = "familyName") String familyName, @Named(value = "documentNumber") String documentNumber, @Named(value = "phone") String phone)
			throws UnauthorizedException {
		return passengerDB.createNewPassenger(user, givenName, familyName, documentNumber, phone);
	}

	@ApiMethod(name = "updatePassengerDetails", path = "updatePassengerDetails", httpMethod = HttpMethod.PUT)
	public Passenger updatePassengerDetails(final User user, @Named(value = "givenName") String givenName, @Named(value = "familyName") String familyName, @Named(value = "documentNumber") String documentNumber, @Named(value = "phone") String phone,
			@Named(value = "email") String email) throws UnauthorizedException, NotFoundException {
		return passengerDB.updatePassengerDetails(user, givenName, familyName, documentNumber, phone, email);
	}

	@ApiMethod(name = "getPassenger", path = "getPassenger", httpMethod = HttpMethod.POST)
	public Passenger getPassenger(final User user) throws UnauthorizedException, NotFoundException {
		return passengerDB.getPassenger(user);
	}

	@ApiMethod(name = "buyTicketByWebsafeFlightKey", path = "buyTicketByWebsafeFlightKey", httpMethod = HttpMethod.POST)
	public Reservation buyTicketByWebsafeFlightKey(final User user, @Named(value = "websafeFlightKey") String websafeFlightKey) throws UnauthorizedException, NotFoundException {
		Passenger passenger = passengerDB.getPassenger(user);
		Flight flight = flightDb.getFlightByWebsafeKey(websafeFlightKey);
		return reservationDB.buyTicket(passenger, flight);

	}

	@ApiMethod(name = "buyTicketByFlightId", path = "buyTicketByFlightId", httpMethod = HttpMethod.POST)
	public Reservation buyTicketByFlightId(final User user, @Named(value = "id") Long id) throws UnauthorizedException, NotFoundException {
		Passenger passenger = passengerDB.getPassenger(user);
		Flight flight = flightDb.getFlightById(id);
		return reservationDB.buyTicket(passenger, flight);
	}

	@ApiMethod(name = "getUserReservations", path = "getUserReservations", httpMethod = HttpMethod.POST)
	public List<Reservation> getUserReservations(final User user) throws NotFoundException, UnauthorizedException {
		Passenger passenger = passengerDB.getPassenger(user);
		List<Reservation> list = reservationDB.getUserReservations(passenger);
		return list;
	}

	@ApiMethod(name = "deleteUserReservationById", path = "deleteUserReservationById", httpMethod = HttpMethod.DELETE)
	public WrappedBoolean deleteUserReservationById(final User user, @Named(value = "reservationId") Long reservationId) throws NotFoundException, UnauthorizedException, ForbiddenException {
		Passenger passenger = passengerDB.getPassenger(user);
		return reservationDB.deleteUserReservationById(passenger, reservationId);
	}

	/*
	 * @ApiMethod(name = "getFlightsByDepartDate", path =
	 * "getFlightsByDepartDate", httpMethod = HttpMethod.POST) public
	 * List<Flight> getFlightsByDepartDate() throws NotFoundException { //
	 * TestDate Date departDate = new Date(1454316000000L); return
	 * flightDb.getFlightsByDepartDate(departDate); }
	 */

	/**
	 * TEST METHOD. Testing query work by date depart;
	 * 
	 * @return
	 * @throws NotFoundException
	 */
	@ApiMethod(name = "getReservationFromCurrentToNextDay", path = "getReservationFromCurrentToNextDay", httpMethod = HttpMethod.POST)
	public List<Reservation> getReservationFromCurrentToNextDay() throws NotFoundException, UnauthorizedException {
		Date currentDay = new Date(1454198400000L); // 31 01 2016 0:00:00

		List<Reservation> list = reservationDB.getReservationOnNextDayFlight(currentDay);
		return list;
	}

}
