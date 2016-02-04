package ua.com.ukrelektro.flights.spi;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.Constants;
import ua.com.ukrelektro.flights.db.helpers.DbInit;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;

@Api(name = "flights", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Flights Backend application.")
public class FlightsApi {

	@ApiMethod(name = "getAllCountries", path = "getAllCountries", httpMethod = HttpMethod.POST)
	public List<Country> getAllCountries() {
		// new CountryDBHelper().initDB();
		// new CountryDBHelper().initAirCrafts();
		// new CountryDBHelper().initFlights();
		new DbInit().addFlights2toDB();
		Query<Country> query = ofy().load().type(Country.class).orderKey(false);
		return query.list();
	}

	@ApiMethod(name = "getAllCities", path = "getAllCities", httpMethod = HttpMethod.POST)
	public List<City> getAllCities() {
		Query<City> query = ofy().load().type(City.class).orderKey(false);
		return query.list();
	}

	@ApiMethod(name = "getAllCitiesByCountryName", path = "getAllCitiesByCountryName", httpMethod = HttpMethod.POST)
	public List<City> getAllCitiesByCountryName(@Named(value = "country") String countryName) {
		Key<Country> keyCountry = Key.create(Country.class, countryName);
		Query<City> query = ofy().load().type(City.class).ancestor(keyCountry).orderKey(false);
		return query.list();
	}

	@ApiMethod(name = "getCityByKey", path = "getCityByKey", httpMethod = HttpMethod.POST)
	public City getCityByKey(@Named(value = "websafeCityKey") String websafeCityKey) throws NotFoundException {
		Key<City> keyCity = Key.create(websafeCityKey);
		City city = ofy().load().key(keyCity).now();
		if (city == null) {
			throw new NotFoundException("No city found with key: " + websafeCityKey);
		}
		return city;
	}

	@ApiMethod(name = "getAllFlights", path = "getAllFlights", httpMethod = HttpMethod.POST)
	public List<Flight> getAllFlights() {
		return ofy().load().type(Flight.class).order("dateDepart").list();
	}

	@ApiMethod(name = "getFlightByKey", path = "getFlightByKey", httpMethod = HttpMethod.POST)
	public Flight getFlightByKey(@Named(value = "websafeFlightKey") String websafeFlightKey) throws NotFoundException {
		Key<Flight> keyFlight = Key.create(websafeFlightKey);
		Flight flight = ofy().load().key(keyFlight).now();
		if (flight == null) {
			throw new NotFoundException("No flight found with key: " + websafeFlightKey);
		}
		return flight;
	}

	@ApiMethod(name = "getFlightsCityToCityByName", path = "getFlightsCityToCityByName", httpMethod = HttpMethod.POST)
	public List<Flight> getFlightsCityToCityByName(@Named(value = "cityFrom") String cityFrom, @Named(value = "cityTo") String cityTo) throws NotFoundException {

		City cityFromRef = ofy().load().type(City.class).filter("name =", cityFrom).list().get(0);
		City cityToRef = ofy().load().type(City.class).filter("name =", cityTo).list().get(0);
		if (cityFromRef == null || cityFromRef == null) {
			throw new IllegalArgumentException("No found Cities with this name");
		}

		List<Flight> flights = ofy().load().type(Flight.class).filter("cityFromRef =", cityFromRef).filter("cityToRef =", cityToRef).list();
		return flights;
	}

}
