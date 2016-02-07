package ua.com.ukrelektro.flights.db.models;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
@Cache
public class Flight {

	@Id
	private long id;

	private boolean existFreePlaces;

	private int freePlaces;

	private String code;

	@Index
	private Date dateDepart;

	private Date dateCome;

	@Load
	private Ref<Aircraft> aircraft;

	private String aircraftName;

	@Load
	@Index
	private Ref<City> cityFromRef;

	@Load
	@Index
	private Ref<City> cityToRef;

	private Flight() {

	}

	public Flight(long id, String code, Date dateDepart, Date dateCome, City cityFrom, City cityTo, Aircraft aircraft) {
		this.id = id;
		this.code = code;
		this.dateDepart = dateDepart;
		this.dateCome = dateCome;
		this.cityFromRef = Ref.create(cityFrom);
		this.cityToRef = Ref.create(cityTo);
		this.aircraft = Ref.create(aircraft);
		this.existFreePlaces = true;
		this.aircraftName = aircraft.getName();
		freePlaces = aircraft.getPlaces();

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isExistFreePlaces() {
		return existFreePlaces;
	}

	public int getFreePlaces() {
		return freePlaces;
	}

	public String getCode() {
		return code;
	}

	public Date getDateDepart() {
		return dateDepart;
	}

	public Date getDateCome() {
		return dateCome;
	}

	public Aircraft getAircraft() {
		return aircraft.get();
	}

	public void setAircraft(Aircraft aircraft) {
		this.aircraft = Ref.create(aircraft);
	}

	public String getAircraftName() {
		return aircraftName;
	}

	public City getCityFrom() {
		return cityFromRef.get();
	}

	public City getCityTo() {
		return cityToRef.get();
	}

	public boolean bookPlaces(int placeNumber) {
		if (freePlaces < placeNumber) {
			throw new IllegalArgumentException("Only " + freePlaces + " available");
		}
		freePlaces -= placeNumber;
		if (freePlaces == 0) {
			existFreePlaces = false;
		}
		return true;
	}

	public void giveBackPlaces(int placeNumber) {
		freePlaces += placeNumber;
		if (existFreePlaces == false) {
			existFreePlaces = true;
		}
	}

	public String getWebsafeKey() {
		return Key.create(Flight.class, id).getString();
	}

}
