package ua.com.ukrelektro.flights.db.helpers;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;

import ua.com.ukrelektro.flights.db.models.City;

public interface CityDB {

	List<City> getAllCities();

	City getCityByWebsafeKey(String websafeCityKey) throws NotFoundException;

	City getCityByName(String name) throws NotFoundException;

}