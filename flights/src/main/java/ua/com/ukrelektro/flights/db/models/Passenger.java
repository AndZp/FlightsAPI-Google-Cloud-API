package ua.com.ukrelektro.flights.db.models;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Passenger {
	@Id
	private long id;

	private String givenName;

	private String familyName;

	private String documentNumber;

	private String email;

	private String phone;

	private List<Key<Flight>> flights = new ArrayList<Key<Flight>>();

	private Passenger() {

	}

	public Passenger(String givenName, String familyName, String documentNumber, String phone, String email) {
		super();
		this.givenName = givenName;
		this.familyName = familyName;
		this.documentNumber = documentNumber;
		this.phone = phone;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Key<Flight>> getFlights() {
		return flights;
	}

	public void addFlightKey(Key<Flight> flightKey) {
		flights.add(flightKey);
	}

	public long getId() {
		return id;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

}
