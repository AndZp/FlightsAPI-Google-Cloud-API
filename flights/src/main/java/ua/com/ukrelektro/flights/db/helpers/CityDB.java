package ua.com.ukrelektro.flights.db.helpers;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;

import ua.com.ukrelektro.flights.db.models.City;

public final class CityDB extends AbstractBaseDB<City> {

	public List<City> getAllCities() {
		return getQueryAll(City.class).order("name").list();
	}

	public City getCityByWebsafeKey(String websafeCityKey) throws NotFoundException {
		return getByWebSafeKey(websafeCityKey, City.class);

	}

}
