package ua.com.ukrelektro.flights.db.helpers;

import static ua.com.ukrelektro.flights.db.service.OfyService.factory;
import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;

import ua.com.ukrelektro.flights.db.models.Aircraft;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;
import ua.com.ukrelektro.flights.db.models.Flight;

public class DbInit {

	public List<Country> getAllCountries() {
		return ofy().load().type(Country.class).order("key").list();
	}

	public void initDB() {
		Country isr = new Country("Israel", "ISR");
		Country usa = new Country("USA", "USA");
		Country rus = new Country("Russia", "RUS");
		Country urk = new Country("Ukraine", "UKR");
		Country fra = new Country("France", "FRA");
		Country ital = new Country("Italy", "ITL");
		Country jap = new Country("Japan", "JPA");
		ofy().save().entities(isr, usa, rus, urk, fra, ital, jap);

		City q = new City(61000, "Tel-Aviv", ofy().load().key(Key.create(Country.class, "Israel")).now());
		City w = new City(10292, "Eilat", ofy().load().key(Key.create(Country.class, "Israel")).now());
		City e = new City(10001, "New-York", ofy().load().key(Key.create(Country.class, "USA")).now());
		City r = new City(60290, "Chicago", ofy().load().key(Key.create(Country.class, "USA")).now());
		City t = new City(83843, "Moscow", ofy().load().key(Key.create(Country.class, "Russia")).now());
		City u = new City(41000, "Kiev", ofy().load().key(Key.create(Country.class, "Ukraine")).now());
		City y = new City(79750, "Odessa", ofy().load().key(Key.create(Country.class, "Ukraine")).now());
		City i = new City(79000, "Lviv", ofy().load().key(Key.create(Country.class, "Ukraine")).now());
		City o = new City(75008, "Paris", ofy().load().key(Key.create(Country.class, "France")).now());
		City p = new City(30161, "Rome", ofy().load().key(Key.create(Country.class, "Italy")).now());
		City a = new City(90100, "Tokyo", ofy().load().key(Key.create(Country.class, "Japan")).now());

		ofy().save().entities(isr, usa, rus, urk, fra, ital, jap, q, w, e, r, t, y, u, i, o, p, a);

	}

	public void initAirCrafts() {

		Key<Aircraft> conferenceKey = factory().allocateId(Aircraft.class);
		Aircraft a = new Aircraft(conferenceKey.getId(), "Boeing", "747-800", 100);
		ofy().save().entity(a);

		conferenceKey = factory().allocateId(Aircraft.class);
		a = new Aircraft(conferenceKey.getId(), "Boeing", "777-100", 200);
		ofy().save().entity(a);

		conferenceKey = factory().allocateId(Aircraft.class);
		a = new Aircraft(conferenceKey.getId(), "Cessna", "182T", 10);
		ofy().save().entity(a);

		conferenceKey = factory().allocateId(Aircraft.class);
		a = new Aircraft(conferenceKey.getId(), "Airbus", "A365", 10);
		ofy().save().entity(a);

		conferenceKey = factory().allocateId(Aircraft.class);
		a = new Aircraft(conferenceKey.getId(), "Gulfstream", "G650", 7);
		ofy().save().entity(a);

		conferenceKey = factory().allocateId(Aircraft.class);
		a = new Aircraft(conferenceKey.getId(), "LittleOne", "02", 2);
		ofy().save().entity(a);
	}

	public void initFlights() {
		City t = ofy().load().type(City.class).filter("name =", "Kiev").list().get(0);
		City f = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		Aircraft air = ofy().load().type(Aircraft.class).list().get(0);

		Key<Flight> conferenceKey = factory().allocateId(Flight.class);

		Flight a = new Flight(conferenceKey.getId(), "GCE-001", new Date(1454316000000L), new Date(1454323200000L), f, t, air);
		ofy().save().entity(a);

		City to = ofy().load().type(City.class).filter("name =", "Odessa").list().get(0);
		City from = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		Aircraft air2 = ofy().load().type(Aircraft.class).list().get(2);
		Key<Flight> key = factory().allocateId(Flight.class);
		Flight b = new Flight(key.getId(), "DAO-111", new Date(1454338800000L), new Date(1454355600000L), from, to, air2);
		ofy().save().entity(b);

		from = ofy().load().type(City.class).filter("name =", "Kiev").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(1);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "JSP-710", new Date(1454338800000L), new Date(1454355600000L), from, to, air2);
		ofy().save().entity(b);

		from = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Lviv").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(3);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "JSR-333", new Date(1454338800000L), new Date(1454355600000L), from, to, air2);
		ofy().save().entity(b);

		from = ofy().load().type(City.class).filter("name =", "Eilat").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Paris").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(4);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "ODA-520", new Date(1454338800000L), new Date(1454355600000L), from, to, air2);
		ofy().save().entity(b);

		from = ofy().load().type(City.class).filter("name =", "Lviv").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Rome").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(5);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "AQW-560", new Date(1454338800000L), new Date(1454355600000L), from, to, air2);
		ofy().save().entity(b);
		////////////////////////////////////////////////////////////////////////////////////

		from = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Odessa").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(1);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "PPA-120", new Date(1454407800000L), new Date(1454419200000L), from, to, air2);
		ofy().save().entity(b);

		from = ofy().load().type(City.class).filter("name =", "Chicago").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(0);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "CTA-920", new Date(1454407800000L), new Date(1454419200000L), from, to, air2);
		ofy().save().entity(b);

		from = ofy().load().type(City.class).filter("name =", "Kiev").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Eilat").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(3);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "KTE-235", new Date(1454407800000L), new Date(1454419200000L), from, to, air2);
		ofy().save().entity(b);

	}

	public void addFlights2toDB() {
		City from = ofy().load().type(City.class).filter("name =", "Kiev").list().get(0);
		City to = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		Aircraft air2 = ofy().load().type(Aircraft.class).list().get(4);
		Key<Flight> key = factory().allocateId(Flight.class);
		Flight b = new Flight(key.getId(), "KTA-111", new Date(1454407800000L), new Date(1454419200000L), from, to, air2);
		ofy().save().entity(b);

		from = ofy().load().type(City.class).filter("name =", "Tel-Aviv").list().get(0);
		to = ofy().load().type(City.class).filter("name =", "Kiev").list().get(0);
		air2 = ofy().load().type(Aircraft.class).list().get(2);
		key = factory().allocateId(Flight.class);
		b = new Flight(key.getId(), "TAK-022", new Date(1454407800000L), new Date(1454419200000L), from, to, air2);
		ofy().save().entity(b);
	}
}
