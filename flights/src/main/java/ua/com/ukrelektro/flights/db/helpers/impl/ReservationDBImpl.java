package ua.com.ukrelektro.flights.db.helpers.impl;

import static ua.com.ukrelektro.flights.db.service.OfyService.factory;
import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import com.google.api.server.spi.response.ForbiddenException;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.db.helpers.ReservationDB;
import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.db.models.Passenger;
import ua.com.ukrelektro.flights.db.models.Reservation;
import ua.com.ukrelektro.flights.spi.wrappers.WrappedBoolean;

public final class ReservationDBImpl extends AbstractBaseDB<Reservation> implements ReservationDB {
	private ReservationDBImpl() {
	}

	private static ReservationDB instance;

	public static ReservationDB getInstance() {
		if (instance == null) {
			instance = new ReservationDBImpl();
		}
		return instance;
	}

	@Override
	public Reservation buyTicket(final Passenger passenger, final Flight flight) {
		final Queue queue = QueueFactory.getDefaultQueue();

		if (!flight.isExistFreePlaces()) {
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

				queue.add(ofy().getTransaction(), TaskOptions.Builder.withUrl("/tasks/send_confirmation_email").param("email", passenger.getEmail())
						.param("reservationInfo", reservation.toString()));

				return reservation;
			}
		});
		return reservation;

	}

	@Override
	public List<Reservation> getPassengerReservations(Passenger passenger) {
		Query<Reservation> query = ofy().load().type(Reservation.class).ancestor(passenger);
		return query.list();
	}

	@Override
	public WrappedBoolean deleteUserReservationById(final Passenger passenger, final Long reservationId) throws ForbiddenException {
		WrappedBoolean result = ofy().transact(new Work<WrappedBoolean>() {

			@Override
			public WrappedBoolean run() {
				Key<Passenger> passKey = Key.create(passenger);
				Key<Reservation> reservationKey = Key.create(passKey, Reservation.class, reservationId);
				Reservation reservation = ofy().load().key(reservationKey).now();
				if (reservation == null) {
					return new WrappedBoolean(false, "You are not have reservation with ID " + reservationId);
				}
				Flight flight = reservation.getFlight();
				flight.giveBackPlaces(1);
				ofy().delete().entity(reservation).now();
				ofy().save().entities(passenger, flight).now();
				return new WrappedBoolean(true);
			}
		});
		if (!result.getResult()) {
			throw new ForbiddenException(result.getReason());
		}

		return result;
	}
}
