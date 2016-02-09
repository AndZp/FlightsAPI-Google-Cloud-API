package ua.com.ukrelektro.flights.params;

import java.util.Date;

public class SearchParam {
	private String fromCountryName;
	private String fromCityName;
	private String toCountryName;
	private String toCityName;
	private Date fromDate;
	private Date toDate;

	public SearchParam() {

	}

	public String getFromCountryName() {
		return fromCountryName;
	}

	public void setFromCountryName(String fromCountryName) {
		this.fromCountryName = fromCountryName;
	}

	public String getFromCityName() {
		return fromCityName;
	}

	public void setFromCityName(String fromCityName) {
		this.fromCityName = fromCityName;
	}

	public String getToCountryName() {
		return toCountryName;
	}

	public void setToCountryName(String toCountryName) {
		this.toCountryName = toCountryName;
	}

	public String getToCityName() {
		return toCityName;
	}

	public void setToCityName(String toCityName) {
		this.toCityName = toCityName;
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
