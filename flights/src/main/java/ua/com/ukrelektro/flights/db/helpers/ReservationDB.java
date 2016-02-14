package ua.com.ukrelektro.flights.db.helpers;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.response.ForbiddenException;

import ua.com.ukrelektro.flights.db.models.Flight;
import ua.com.ukrelektro.flights.db.models.Passenger;
import ua.com.ukrelektro.flights.db.models.Reservation;
import ua.com.ukrelektro.flights.spi.wrappers.WrappedBoolean;

public interface ReservationDB {

	Reservation buyTicket(Passenger passenger, Flight flight);

	List<Reservation> getUserReservations(Passenger passenger);

	List<Reservation> getReservationOnNextDayFlight(Date currentDay);

	WrappedBoolean deleteUserReservationById(Passenger passenger, Long reservationId) throws ForbiddenException;

}