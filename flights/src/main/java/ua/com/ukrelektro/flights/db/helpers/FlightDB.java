package ua.com.ukrelektro.flights.db.helpers;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;

import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.params.SearchParam;

public interface FlightDB {

	List<Flight> getAllFlight();

	Flight getFlightByWebsafeKey(String websafeKey) throws NotFoundException;

	Flight getFlightById(Long id) throws NotFoundException;

	List<Flight> getFlightsByFlightParam(SearchParam flightParam) throws NotFoundException;

}