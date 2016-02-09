package ua.com.ukrelektro.flights.spi;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.User;

import ua.com.ukrelektro.flights.Constants;
import ua.com.ukrelektro.flights.db.helpers.CityDB;
import ua.com.ukrelektro.flights.db.helpers.CountryDB;
import ua.com.ukrelektro.flights.db.helpers.FlightDB;
import ua.com.ukrelektro.flights.db.helpers.PassengerDB;
import ua.com.ukrelektro.flights.db.helpers.ReservationDB;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.db.models.Passenger;
import ua.com.ukrelektro.flights.db.models.Reservation;
import ua.com.ukrelektro.flights.params.SearchParam;
import ua.com.ukrelektro.flights.spi.wrappers.StringWrapper;
import ua.com.ukrelektro.flights.spi.wrappers.WrappedBoolean;

@Api(name = "flights", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Flights Backend application.")
public class FlightsApi {

	private CityDB cityDB = CityDB.getInstance();
	private CountryDB countryDB = CountryDB.getInstance();
	private FlightDB flightDb = FlightDB.getInstance();
	private PassengerDB passengerDB = PassengerDB.getInstance();
	private ReservationDB reservationDB = ReservationDB.getInstance();

	@ApiMethod(name = "getAllCountries", path = "places/getAllCountries", httpMethod = HttpMethod.GET)
	public List<Country> getAllCountries() {
		return countryDB.getAllCountry();
	}

	@ApiMethod(name = "getAllCities", path = "places/getAllCities", httpMethod = HttpMethod.GET)
	public List<City> getAllCities() {
		return cityDB.getAllCities();
	}

	@ApiMethod(name = "getAllCitiesByCountryName", path = "places/getAllCitiesByCountryName", httpMethod = HttpMethod.GET)
	public List<City> getAllCitiesByCountryName(@Named(value = "country") String countryName) throws NotFoundException {
		return countryDB.getCitiesByCountry(countryName);
	}

	@ApiMethod(name = "getCityByWebsafeKey", path = "places/getCityByWebsafeKey", httpMethod = HttpMethod.GET)
	public City getCityByWebsafeKey(@Named(value = "websafeCityKey") String websafeCityKey) throws NotFoundException {
		return cityDB.getCityByWebsafeKey(websafeCityKey);
	}

	@ApiMethod(name = "getCityByName", path = "places/getCityByName", httpMethod = HttpMethod.GET)
	public City getCityByName(@Named(value = "name") String name) throws NotFoundException {
		return cityDB.getCityByName(name);
	}

	@ApiMethod(name = "getAllFlights", path = "flights/getAllFlights", httpMethod = HttpMethod.GET)
	public List<Flight> getAllFlights() {
		return flightDb.getAllFlight();
	}

	@ApiMethod(name = "getFlightWebsafeByKey", path = "flights/getFlightWebsafeByKey", httpMethod = HttpMethod.GET)
	public Flight getFlightByWebsafeByKey(@Named(value = "websafeFlightKey") String websafeFlightKey) throws NotFoundException {
		return flightDb.getFlightByWebsafeKey(websafeFlightKey);
	}

	@ApiMethod(name = "getFlightsCityToCityByName", path = "flights/getFlightsCityToCityByName", httpMethod = HttpMethod.GET)
	public List<Flight> getFlightsCityToCityByName(@Named(value = "cityFrom") String cityFrom, @Named(value = "cityTo") String cityTo)
			throws NotFoundException {
		return flightDb.getFlightsCityToCityByName(cityFrom, cityTo);
	}

	@ApiMethod(name = "getFlightsByFlightParam", path = "flights/getFlightsByFlightParam", httpMethod = HttpMethod.POST)
	public List<Flight> getFlightsByFlightParam(SearchParam flightParam) throws NotFoundException {
		return flightDb.getFlightsByFlightParam(flightParam);
	}

	@ApiMethod(name = "registerNewPassenger", path = "passenger/registerNewPassenger", httpMethod = HttpMethod.PUT)
	public Passenger registerNewPassenger(final User user, @Named(value = "givenName") String givenName,
			@Named(value = "familyName") String familyName, @Named(value = "documentNumber") String documentNumber,
			@Named(value = "phone") String phone) throws UnauthorizedException {
		return passengerDB.createNewPassenger(user, givenName, familyName, documentNumber, phone);
	}

	@ApiMethod(name = "updatePassengerDetails", path = "passenger/updatePassengerDetails", httpMethod = HttpMethod.POST)
	public Passenger updatePassengerDetails(final User user, @Named(value = "givenName") String givenName,
			@Named(value = "familyName") String familyName, @Named(value = "documentNumber") String documentNumber,
			@Named(value = "phone") String phone, @Named(value = "email") String email) throws UnauthorizedException, NotFoundException {
		return passengerDB.updatePassengerDetails(user, givenName, familyName, documentNumber, phone, email);
	}

	@ApiMethod(name = "updatePassengerAvatar", path = "passenger/updatePassengerAvatar", httpMethod = HttpMethod.POST)
	public Passenger updatePassengerAvatar(final User user, @Named(value = "avatarBlobKey") String avatarBlobKey)
			throws UnauthorizedException, NotFoundException {
		return passengerDB.updatePassengerAvatar(user, avatarBlobKey);
	}

	@ApiMethod(name = "getPassenger", path = "passenger/getPassenger", httpMethod = HttpMethod.POST)
	public Passenger getPassenger(final User user) throws UnauthorizedException, NotFoundException {
		return passengerDB.getPassenger(user);
	}

	@ApiMethod(name = "getPassengerAvatar", path = "passenger/getPassengerAvatar", httpMethod = HttpMethod.POST)
	public StringWrapper getPassengerAvatar(final User user) throws UnauthorizedException, NotFoundException {
		Passenger passenger = passengerDB.getPassenger(user);
		String avatarUrl = ImagesServiceFactory.getImagesService()
				.getServingUrl(ServingUrlOptions.Builder.withBlobKey(new BlobKey(passenger.getAvatarBlobKey())));
		String addParam = "=w100-h100-cc"; // circle avatar
		return new StringWrapper(avatarUrl + addParam);
	}

	@ApiMethod(name = "buyTicketByWebsafeFlightKey", path = "reservation/buyTicketByWebsafeFlightKey", httpMethod = HttpMethod.POST)
	public Reservation buyTicketByWebsafeFlightKey(final User user, @Named(value = "websafeFlightKey") String websafeFlightKey)
			throws UnauthorizedException, NotFoundException {
		Passenger passenger = passengerDB.getPassenger(user);
		Flight flight = flightDb.getFlightByWebsafeKey(websafeFlightKey);
		return reservationDB.buyTicket(passenger, flight);

	}

	@ApiMethod(name = "buyTicketByFlightId", path = "reservation/buyTicketByFlightId", httpMethod = HttpMethod.POST)
	public Reservation buyTicketByFlightId(final User user, @Named(value = "id") Long id) throws UnauthorizedException, NotFoundException {
		Passenger passenger = passengerDB.getPassenger(user);
		Flight flight = flightDb.getFlightById(id);
		return reservationDB.buyTicket(passenger, flight);
	}

	@ApiMethod(name = "getUserReservations", path = "reservation/getUserReservations", httpMethod = HttpMethod.POST)
	public List<Reservation> getUserReservations(final User user) throws NotFoundException, UnauthorizedException {
		Passenger passenger = passengerDB.getPassenger(user);
		List<Reservation> list = reservationDB.getUserReservations(passenger);
		return list;
	}

	@ApiMethod(name = "deleteUserReservationById", path = "reservation/deleteUserReservationById", httpMethod = HttpMethod.DELETE)
	public WrappedBoolean deleteUserReservationById(final User user, @Named(value = "reservationId") Long reservationId)
			throws NotFoundException, UnauthorizedException, ForbiddenException {
		Passenger passenger = passengerDB.getPassenger(user);
		return reservationDB.deleteUserReservationById(passenger, reservationId);
	}

	@ApiMethod(name = "getCsUploadURL", path = "getCsUploadURL", httpMethod = ApiMethod.HttpMethod.POST)
	public StringWrapper getCsUploadURL() {
		String uploadURL = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/cs-upload",
				UploadOptions.Builder.withGoogleStorageBucketName("and-flights-api-bucket"));

		return new StringWrapper(uploadURL);
	}
}
