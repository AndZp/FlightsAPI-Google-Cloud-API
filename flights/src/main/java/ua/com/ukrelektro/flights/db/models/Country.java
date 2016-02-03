package ua.com.ukrelektro.flights.db.models;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Country {

	@Id
	String name;

	String desc;

	String code;

	private List<String> citiesKeysInCountry = new ArrayList<>(0);

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getCitiesKeysInCountry() {
		return ImmutableList.copyOf(citiesKeysInCountry);
	}

	public void addToCitiesKeysInCountry(String cityKey) {
		citiesKeysInCountry.add(cityKey);
	}

}
