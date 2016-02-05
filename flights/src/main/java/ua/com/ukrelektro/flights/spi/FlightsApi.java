package ua.com.ukrelektro.flights.spi;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;

import ua.com.ukrelektro.flights.Constants;
import ua.com.ukrelektro.flights.db.helpers.CityDB;
import ua.com.ukrelektro.flights.db.helpers.CountyDB;
import ua.com.ukrelektro.flights.db.helpers.FlightDB;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;

@Api(name = "flights", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Flights Backend application.")
public class FlightsApi {
	private CityDB cityDB = new CityDB();
	private CountyDB countryDB = new CountyDB();
	private FlightDB flightDb = new FlightDB();

	@ApiMethod(name = "getAllCountries", path = "getAllCountries", httpMethod = HttpMethod.POST)
	public List<Country> getAllCountries() {
		return countryDB.getAllCountry();
	}

	@ApiMethod(name = "getAllCities", path = "getAllCities", httpMethod = HttpMethod.POST)
	public List<City> getAllCities() {
		return cityDB.getAllCities();
	}

	@ApiMethod(name = "getAllCitiesByCountryName", path = "getAllCitiesByCountryName", httpMethod = HttpMethod.POST)
	public List<City> getAllCitiesByCountryName(@Named(value = "country") String countryName) throws NotFoundException {
		return countryDB.getCitiesByCountry(countryName);
	}

	@ApiMethod(name = "getCityByWebsafeKey", path = "getCityByWebsafeKey", httpMethod = HttpMethod.POST)
	public City getCityByWebsafeKey(@Named(value = "websafeCityKey") String websafeCityKey) throws NotFoundException {
		return cityDB.getCityByWebsafeKey(websafeCityKey);
	}

	@ApiMethod(name = "getAllFlights", path = "getAllFlights", httpMethod = HttpMethod.POST)
	public List<Flight> getAllFlights() {
		return flightDb.getAllFlight();
	}

	@ApiMethod(name = "getFlightByKey", path = "getFlightByKey", httpMethod = HttpMethod.POST)
	public Flight getFlightByKey(@Named(value = "websafeFlightKey") String websafeFlightKey) throws NotFoundException {
		return flightDb.getFlightByWebsafeKey(websafeFlightKey);
	}

	@ApiMethod(name = "getFlightsCityToCityByName", path = "getFlightsCityToCityByName", httpMethod = HttpMethod.POST)
	public List<Flight> getFlightsCityToCityByName(@Named(value = "cityFrom") String cityFrom, @Named(value = "cityTo") String cityTo) throws NotFoundException {
		return flightDb.getFlightsCityToCityByName(cityFrom, cityTo);
	}

}
