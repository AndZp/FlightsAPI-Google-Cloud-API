package ua.com.ukrelektro.flights.db.helpers;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.cmd.Query;

import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;

public final class CountyDB extends AbstractBaseDB<Country> {

	public List<Country> getAllCountry() {
		return getQueryAll(Country.class).orderKey(false).list();
	}

	public List<City> getCitiesByCountry(String countryName) throws NotFoundException {
		Country country = getCountryByName(countryName);
		Query<City> query = ofy().load().type(City.class).ancestor(country).order("name");
		return query.list();
	}

	public Country getCountryByName(String name) throws NotFoundException {
		Country country = getByKey(Country.class, name);
		if (country == null) {
			throw new NotFoundException("This country not registered");
		}
		return country;
	}
}
