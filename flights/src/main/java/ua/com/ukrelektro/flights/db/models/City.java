package ua.com.ukrelektro.flights.db.models;

import static ua.com.ukrelektro.flights.db.service.OfyService.ofy;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class City {

	@Id
	String name;

	String desc;

	@Parent
	private Key<Country> countryKey;

	private City() {
	}

	public City(String name, String countryNameKey) {
		this.name = name;
		this.countryKey = Key.create(Country.class, countryNameKey);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Key<Country> getCountryKey() {
		return countryKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return name + ofy().load().key(countryKey).now();
	}

}
