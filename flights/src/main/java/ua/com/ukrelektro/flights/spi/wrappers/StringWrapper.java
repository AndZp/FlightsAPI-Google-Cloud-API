package ua.com.ukrelektro.flights.spi.wrappers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StringWrapper implements Serializable {
	private String string;

	public StringWrapper() {
	}

	public StringWrapper(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
