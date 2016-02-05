package ua.com.ukrelektro.flights.db.models;

import java.util.Date;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
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
public class Reservation {

	@Id
	private long id;

	@Index
	@Load
	private Ref<Flight> flight;

	@Load
	@Parent
	@Index
	private Ref<Passenger> passenger;

	private String addInfo;

	private Date reserveDate;

	@Index
	private String code;

	private Reservation() {
	}

	public Reservation(long id, String code, Flight flight, Passenger passenger) {
		this.id = id;
		this.code = code;
		this.flight = Ref.create(flight);
		this.passenger = Ref.create(passenger);
		this.reserveDate = new Date();
	}

	public String getAddInfo() {
		return addInfo;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}

	public long getId() {
		return id;
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Flight getFlight() {
		return flight.get();
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Passenger getPassenger() {
		return passenger.get();
	}

	public Date getReserveDate() {
		return reserveDate;
	}

	public String getCode() {
		return code;
	}

	// Get a String version of the key
	public String getWebsafeKey() {
		return Key.create(passenger.getKey(), Reservation.class, id).getString();
	}

	@Override
	public String toString() {
		Flight flightObj = flight.get();
		Passenger passObj = passenger.get();

		StringBuilder stringBuilder = new StringBuilder("Reservation ID: " + id + "\n").append("Reservation Code: ").append(code).append("\n");

		stringBuilder.append("Flight Code: ").append(flightObj.getCode()).append("\n");

		stringBuilder.append("From: ").append(flightObj.getCityFrom().getName() + ", " + flightObj.getCityFrom().getCountry().getName()).append("\n");
		stringBuilder.append("To: ").append(flightObj.getCityTo().getName() + ", " + flightObj.getCityTo().getCountry().getName()).append("\n");

		stringBuilder.append("Date Depart: ").append(flightObj.getDateDepart()).append("\n");
		stringBuilder.append("Date Arrival: ").append(flightObj.getDateCome()).append("\n");

		if (addInfo != null) {
			stringBuilder.append("Additional Information: ").append(addInfo).append("\n");
		}

		stringBuilder.append("Passenger: ").append(passObj.getGivenName() + " " + passObj.getGivenName()).append("\n");
		stringBuilder.append("Passport number: ").append(passObj.getDocumentNumber()).append("\n");
		stringBuilder.append("Tel: ").append(passObj.getPhone()).append("\n");

		return stringBuilder.toString();
	}

}
