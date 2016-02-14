package ua.com.ukrelektro.flights.db.helpers;

import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import ua.com.ukrelektro.flights.db.models.Passenger;

public interface PassengerDB {

	Passenger createNewPassenger(User user, String givenName, String familyName, String documentNumber, String phone) throws UnauthorizedException;

	Passenger getPassenger(User user) throws UnauthorizedException, NotFoundException;

	Passenger updatePassengerDetails(User user, String givenName, String familyName, String documentNumber, String phone, String email)
			throws UnauthorizedException, NotFoundException;

	Passenger updatePassengerAvatar(User user, String avatarBlobKey) throws UnauthorizedException, NotFoundException;

}