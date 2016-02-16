package ua.com.ukrelektro.flights.db.helpers.impl;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;

import ua.com.ukrelektro.flights.db.helpers.CityDB;
import ua.com.ukrelektro.flights.db.models.City;

public final class CityDBImpl extends AbstractBaseDB<City> implements CityDB {

	private CityDBImpl() {
	}

	private static CityDB instance;

	public static CityDB getInstance() {
		if (instance == null) {
			instance = new CityDBImpl();
		}
		return instance;
	}

	@Override
	public List<City> getAllCities() {
		return getQueryAll(City.class).order("name").list();
	}

	@Override
	public City getCityByWebsafeKey(String websafeCityKey) throws NotFoundException {
		return getByWebSafeKey(websafeCityKey, City.class);
	}

	@Override
	public City getCityByName(String name) throws NotFoundException {
		City city = ofy().load().type(City.class).filter("name", name).first().now();
		if (city == null) {
			throw new NotFoundException("City with name " + name + " not registered");
		}
		return city;
	}
}
