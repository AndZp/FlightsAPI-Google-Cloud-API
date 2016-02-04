package ua.com.ukrelektro.flights.db.models;

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

	private int freePlaces;

	String company;

	private Aircraft() {
	}

	public Aircraft(String name, int places) {
		this.name = name;
		this.places = places;
		freePlaces = places;
	}

	public void bookPlaces(int numberBookPlaces) {
		if (numberBookPlaces > freePlaces) {
			throw new IllegalArgumentException("Aircraft have only " + freePlaces + " free places");
		}
		freePlaces -= numberBookPlaces;
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

	public int getFreePlaces() {
		return freePlaces;
	}

	public String getCompany() {
		return company;
	}

}
