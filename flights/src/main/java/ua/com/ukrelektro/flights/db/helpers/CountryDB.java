package ua.com.ukrelektro.flights.db.helpers;

import java.util.List;

import com.google.api.server.spi.response.NotFoundException;

import ua.com.ukrelektro.flights.db.models.City;
import ua.com.ukrelektro.flights.db.models.Country;

public interface CountryDB {

	List<Country> getAllCountry();

	List<City> getCitiesByCountry(String countryName) throws NotFoundException;

	Country getCountryByName(String name) throws NotFoundException;

}