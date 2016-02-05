package ua.com.ukrelektro.flights.db.helpers;

import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import ua.com.ukrelektro.flights.db.models.Passenger;

public final class PassengerDB extends AbstractBaseDB<Passenger> {

	public Passenger createNewPassenger(final User user, String givenName, String familyName, String documentNumber, String phone) throws UnauthorizedException {

		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}
		String mainEmail = user.getEmail();
		String userId = user.getUserId();
		Passenger passenger = new Passenger(userId, givenName, familyName, documentNumber, phone, mainEmail);
		create(passenger);

		return passenger;
	}

	public Passenger getPassenger(User user) throws UnauthorizedException, NotFoundException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}
		Passenger passenger = getByKey(Passenger.class, user.getUserId());
		if (passenger == null) {
			throw new NotFoundException("This user not registered");
		}
		return passenger;
	}

}
