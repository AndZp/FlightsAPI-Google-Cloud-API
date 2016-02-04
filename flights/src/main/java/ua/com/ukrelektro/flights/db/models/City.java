package ua.com.ukrelektro.flights.db.models;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class City {

	@Id
	private long postCode;

	@Index
	private String name;

	private String desc;

	@Parent
	@Load
	@Index
	private Ref<Country> country;

	private City() {
	}

	public City(long postCode, String name, Country country) {
		this.postCode = postCode;
		this.name = name;
		this.country = Ref.create(country);
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Country getCountry() {
		return country.get();
	}

	// Get a String version of the key
	public String getWebsafeKey() {
		return Key.create(country.getKey(), City.class, name).getString();
	}

	@Override
	public String toString() {
		return name + country.get().name;
	}

}
