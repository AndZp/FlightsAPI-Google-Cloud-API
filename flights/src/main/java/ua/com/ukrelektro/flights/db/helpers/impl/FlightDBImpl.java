package ua.com.ukrelektro.flights.db.helpers.impl;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;
import static ua.com.ukrelektro.flights.utils.StringUtils.isNullOrEmpty;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.db.helpers.CityDB;
import ua.com.ukrelektro.flights.db.helpers.CountryDB;
import ua.com.ukrelektro.flights.db.helpers.FlightDB;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.params.SearchParam;

public final class FlightDBImpl extends AbstractBaseDB<Flight> implements FlightDB {
	private CityDB cityDB;
	private CountryDB countryDB;

	private FlightDBImpl() {
		cityDB = CityDBImpl.getInstance();
		countryDB = CountryDBImpl.getInstance();
	}

	private static FlightDBImpl instance;

	public static FlightDBImpl getInstance() {
		if (instance == null) {
			instance = new FlightDBImpl();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.FlightDB#getAllFlight()
	 */
	@Override
	public List<Flight> getAllFlight() {
		return getQueryAll(Flight.class).order("dateDepart").list();
	}

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.FlightDB#getFlightByWebsafeKey(java.lang.String)
	 */
	@Override
	public Flight getFlightByWebsafeKey(String websafeKey) throws NotFoundException {
		return getByWebSafeKey(websafeKey, Flight.class);

	}

	public List<Flight> getFlightsCityToCityByName(String cityNameFrom, String cityNameTo) throws NotFoundException {
		City cityFrom = cityDB.getCityByName(cityNameFrom);
		City cityTo = cityDB.getCityByName(cityNameTo);

		return getQueryAll(Flight.class).filter("cityFromRef =", cityFrom).filter("cityToRef =", cityTo).order("dateDepart").list();
	}

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.FlightDB#getFlightById(java.lang.Long)
	 */
	@Override
	public Flight getFlightById(Long id) throws NotFoundException {
		Flight flight = getById(Flight.class, id);
		if (flight == null) {
			throw new NotFoundException("Flight with id: " + id + " not registered");
		}
		return flight;
	}

	public List<Flight> getFlightsByDepartDate(Date departDate) {
		Query<Flight> query = ofy().load().type(Flight.class).filter("dateDepart =", departDate);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.FlightDB#getFlightsByFlightParam(ua.com.ukrelektro.flights.params.SearchParam)
	 */
	@Override
	public List<Flight> getFlightsByFlightParam(SearchParam flightParam) throws NotFoundException {
		Query<Flight> queryResults = getQueryAll(Flight.class);
		if (flightParam == null) {
			return queryResults.order("dateDepart").list();
		}
		// Try Querying by fromCity or from Country (all cities in Country)
		queryResults = addQueryByFromDirection("cityFromRef", flightParam, queryResults);

		// Try Querying by toCity or to Country (all cities in Country)
		queryResults = addQueryByToDirection("cityToRef", flightParam, queryResults);

		// Query by Dates
		if (flightParam.getFromDate() != null) {
			queryResults = queryResults.filter("dateDepart >", flightParam.getFromDate());
		}

		if (flightParam.getToDate() != null) {
			queryResults = queryResults.filter("dateDepart <", flightParam.getToDate());
		}

		return queryResults.list();
	}

	// TODO: Refactoring this !
	private Query<Flight> addQueryByFromDirection(String where, SearchParam flightParam, Query<Flight> queryAllFlights) throws NotFoundException {
		if (!isNullOrEmpty(flightParam.getFromCityName())) {
			City city = cityDB.getCityByName(flightParam.getFromCityName());
			queryAllFlights = queryAllFlights.filter(where + " =", city);

		} else if (!isNullOrEmpty(flightParam.getFromCountryName())) {

			Country country = countryDB.getCountryByName(flightParam.getFromCountryName());
			Iterable<Key<City>> queryCitiesKeys = ofy().load().type(City.class).ancestor(country).keys();
			queryAllFlights = queryAllFlights.filter(where + " in", queryCitiesKeys);
		}
		return queryAllFlights;
	}

	private Query<Flight> addQueryByToDirection(String where, SearchParam flightParam, Query<Flight> queryAllFlights) throws NotFoundException {
		if (!isNullOrEmpty(flightParam.getToCityName())) {
			City city = cityDB.getCityByName(flightParam.getToCityName());
			queryAllFlights = queryAllFlights.filter(where + " =", city);

		} else if (!isNullOrEmpty(flightParam.getToCountryName())) {

			Country country = countryDB.getCountryByName(flightParam.getToCountryName());
			Iterable<Key<City>> queryCitiesKeys = ofy().load().type(City.class).ancestor(country).keys();
			queryAllFlights = queryAllFlights.filter(where + " in", queryCitiesKeys);
		}
		return queryAllFlights;
	}

}
