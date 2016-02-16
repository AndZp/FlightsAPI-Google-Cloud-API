package ua.com.ukrelektro.flights.db.helpers.impl;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.db.helpers.CountryDB;
import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;

public final class CountryDBImpl extends AbstractBaseDB<Country> implements CountryDB {
	private CountryDBImpl() {
	}

	private static CountryDB instance;

	public static CountryDB getInstance() {
		if (instance == null) {
			instance = new CountryDBImpl();
		}
		return instance;
	}

	@Override
	public List<Country> getAllCountry() {
		return getQueryAll(Country.class).orderKey(false).list();
	}

	@Override
	public List<City> getCitiesByCountry(String countryName) throws NotFoundException {
		Country country = getCountryByName(countryName);
		Query<City> query = ofy().load().type(City.class).ancestor(country).order("name");
		return query.list();
	}

	@Override
	public Country getCountryByName(String name) throws NotFoundException {
		Country country = getByKey(Country.class, name);
		if (country == null) {
			throw new NotFoundException("This country not registered");
		}
		return country;
	}
}
