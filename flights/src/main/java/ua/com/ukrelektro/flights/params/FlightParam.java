package ua.com.ukrelektro.flights.params;

import java.util.Date;

public class FlightParam {
	private String fromCountryName;
	private Long fromCityId;
	private String toCountryName;
	private Long toCityId;
	private Date fromDate;
	private Date toDate;

	public FlightParam() {

	}

	public String getFromCountryName() {
		return fromCountryName;
	}

	public void setFromCountryName(String fromCountryName) {
		this.fromCountryName = fromCountryName;
	}

	public Long getFromCityId() {
		return fromCityId;
	}

	public void setFromCityId(Long fromCityId) {
		this.fromCityId = fromCityId;
	}

	public String getToCountryName() {
		return toCountryName;
	}

	public void setToCountryName(String toCountryName) {
		this.toCountryName = toCountryName;
	}

	public Long getToCityId() {
		return toCityId;
	}

	public void setToCityId(Long toCityId) {
		this.toCityId = toCityId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
