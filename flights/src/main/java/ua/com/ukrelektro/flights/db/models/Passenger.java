package ua.com.ukrelektro.flights.db.models;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Passenger {
	@Id
	private String id;

	private String givenName;

	private String familyName;

	private String documentNumber;

	private String email;

	private String phone;

	private Passenger() {

	}

	public Passenger(String id, String givenName, String familyName, String documentNumber, String phone, String email) {
		super();
		this.id = id;
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

	public String getId() {
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
