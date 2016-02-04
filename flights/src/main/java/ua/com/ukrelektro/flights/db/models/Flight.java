package ua.com.ukrelektro.flights.db.models;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Flight {

	@Id
	private long id;

	private boolean existFreePlaces;

	private String code;

	@Index
	private Date dateDepart;

	@Index
	private Date dateCome;

	private Key<Aircraft> aircraftKey;

	@Index
	private Key<City> cityFromKey;

	@Index
	private Key<City> cityToKey;
}
