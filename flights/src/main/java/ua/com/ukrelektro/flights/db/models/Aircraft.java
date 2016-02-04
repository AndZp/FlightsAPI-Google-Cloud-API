package ua.com.ukrelektro.flights.db.models;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Aircraft {

	@Id
	private long id;

	private String name;

	private int places;

	private String company;

	private String model;

	private Aircraft() {
	}

	public Aircraft(long id, String company, String model, int places) {
		this.id = id;
		this.company = company;
		this.model = model;
		this.name = company + " " + model;
		this.places = places;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPlaces() {
		return places;
	}

	public String getCompany() {
		return company;
	}

	public String getModel() {
		return model;
	}

	public String getWebsafeKey() {
		return Key.create(Aircraft.class, id).getString();
	}
}
