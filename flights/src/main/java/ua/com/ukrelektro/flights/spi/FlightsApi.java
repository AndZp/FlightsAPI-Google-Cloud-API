package ua.com.ukrelektro.flights.spi;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.Constants;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;

@Api(name = "flights", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Flights Backend application.")
public class FlightsApi {

	@ApiMethod(name = "getAllCountries", path = "getAllCountries", httpMethod = HttpMethod.POST)
	public List<Country> getAllCountries() {

		Query<Country> query = ofy().load().type(Country.class).order("__key__");
		return query.list();
	}

	@ApiMethod(name = "getAllCities", path = "getAllCities", httpMethod = HttpMethod.POST)
	public List<City> getAllCities() {
		Query<City> query = ofy().load().type(City.class).order("__key__");
		return query.list();
	}
}
