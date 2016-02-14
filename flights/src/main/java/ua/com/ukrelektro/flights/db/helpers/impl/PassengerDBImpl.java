package ua.com.ukrelektro.flights.db.helpers.impl;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import ua.com.ukrelektro.flights.db.helpers.PassengerDB;
import ua.com.ukrelektro.flights.db.models.Passenger;

public final class PassengerDBImpl extends AbstractBaseDB<Passenger> implements PassengerDB {
	private PassengerDBImpl() {
	}

	private static PassengerDB instance;

	public static PassengerDB getInstance() {
		if (instance == null) {
			instance = new PassengerDBImpl();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.PassengerDB#createNewPassenger(com.google.appengine.api.users.User, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Passenger createNewPassenger(final User user, String givenName, String familyName, String documentNumber, String phone)
			throws UnauthorizedException {

		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}
		String mainEmail = user.getEmail();
		String userId = user.getUserId();
		Passenger passenger = new Passenger(userId, givenName, familyName, documentNumber, phone, mainEmail);
		create(passenger);

		return passenger;
	}

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.PassengerDB#getPassenger(com.google.appengine.api.users.User)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.PassengerDB#updatePassengerDetails(com.google.appengine.api.users.User, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Passenger updatePassengerDetails(User user, String givenName, String familyName, String documentNumber, String phone, String email)
			throws UnauthorizedException, NotFoundException {
		Passenger passenger = getPassenger(user);
		passenger.update(givenName, familyName, documentNumber, phone, email);
		ofy().save().entity(passenger);
		return passenger;
	}

	/* (non-Javadoc)
	 * @see ua.com.ukrelektro.flights.db.helpers.impl.PassengerDB#updatePassengerAvatar(com.google.appengine.api.users.User, java.lang.String)
	 */
	@Override
	public Passenger updatePassengerAvatar(User user, String avatarBlobKey) throws UnauthorizedException, NotFoundException {
		Passenger passenger = getPassenger(user);
		passenger.setAvatarBlobKey(avatarBlobKey);
		ofy().save().entity(passenger);
		return passenger;
	}

}
