package ua.com.ukrelektro.flights.db.helpers;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Flight;

public final class FlightDB extends AbstractBaseDB<Flight> {

	public List<Flight> getAllFlight() {
		return getQueryAll(Flight.class).order("dateDepart").list();
	}

	public Flight getFlightByWebsafeKey(String websafeKey) throws NotFoundException {
		return getByWebSafeKey(websafeKey, Flight.class);

	}

	public List<Flight> getFlightsCityToCityByName(String cityNameFrom, String cityNameTo) {
		City cityFrom = ofy().load().type(City.class).filter("name =", cityNameFrom).first().now();
		City cityTo = ofy().load().type(City.class).filter("name =", cityNameTo).first().now();
		if (cityFrom == null || cityFrom == null) {
			throw new IllegalArgumentException("No found Cities with this name");
		}

		return getQueryAll(Flight.class).filter("cityFromRef =", cityFrom).filter("cityToRef =", cityTo).order("dateDepart").list();
	}

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

}
