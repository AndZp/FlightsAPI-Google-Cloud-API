package ua.com.ukrelektro.flights.db.service;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import ua.com.ukrelektro.flights.db.models.Aircraft;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.db.models.Passenger;
import ua.com.ukrelektro.flights.db.models.Reservation;

/**
 * Custom Objectify Service that this application should use.
 */
public class OfyService {
	/**
	 * This static block ensure the entity registration.
	 */
	static {
		factory().register(City.class);
		factory().register(Country.class);
		factory().register(Aircraft.class);
		factory().register(Flight.class);
		factory().register(Reservation.class);
		factory().register(Passenger.class);

	}

	/**
	 * Use this static method for getting the Objectify service object in order
	 * to make sure the above static block is executed before using Objectify.
	 * 
	 * @return Objectify service object.
	 */
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	/**
	 * Use this static method for getting the Objectify service factory.
	 * 
	 * @return ObjectifyFactory.
	 */
	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}
}
