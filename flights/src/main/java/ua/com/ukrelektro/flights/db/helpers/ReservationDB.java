package ua.com.ukrelektro.flights.db.helpers;

import static ua.com.ukrelektro.flights.db.service.OfyService.factory;
import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.db.models.Passenger;
import ua.com.ukrelektro.flights.db.models.Reservation;

public final class ReservationDB extends AbstractBaseDB<Reservation> {

	public Reservation buyTicket(final Passenger passenger, final Flight flight) {
		final Queue queue = QueueFactory.getDefaultQueue();

		if (flight.isExistFreePlaces()) {
			throw new IllegalArgumentException("All places on this flight are reserved");
		}

		Reservation reservation = ofy().transact(new Work<Reservation>() {

			@Override
			public Reservation run() {
				Key<Reservation> reservationKey = factory().allocateId(passenger, Reservation.class);

				SecureRandom random = new SecureRandom();
				String code = new BigInteger(30, random).toString(32);

				flight.bookPlaces(1);

				Reservation reservation = new Reservation(reservationKey.getId(), code, flight, passenger);

				ofy().save().entities(passenger, reservation, flight);

				queue.add(ofy().getTransaction(), TaskOptions.Builder.withUrl("/tasks/send_confirmation_email").param("email", passenger.getEmail()).param("reservationInfo", reservation.toString()));

				return reservation;
			}
		});
		return reservation;

	}

	public List<Reservation> getUserReservations(Passenger passenger) {
		Query<Reservation> query = ofy().load().type(Reservation.class).ancestor(passenger);
		return query.list();
	}

	public List<Reservation> getReservationOnNextDayFlight(Date currentDay) {
		// Date dateTest = new Date(1454316000000L);
		Date startDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, 1);
		Date endDate = c.getTime();

		Iterable<Key<Flight>> queryFligths = ofy().load().type(Flight.class).filter("dateDepart >", startDate).filter("dateDepart <", endDate).keys();
		Query<Reservation> query = ofy().load().type(Reservation.class).filter("flight in", queryFligths);

		//
		// Query<Reservation> query =
		// ofy().load().type(Reservation.class).filter("flight.dateDepart >",
		// startDate).filter("flight.dateDepart <", endDate);

		return query.list();
	}

}
