package io.codearte.jfairy.producer.person.locale.zh;

import io.codearte.jfairy.producer.person.Address;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;

/**
 * io.codearte.jfairy.producer.person.locale.zh.ZhAddress
 *
 * @author lhfcws
 * @since 2017/3/2
 */
public class ZhAddress implements Address {

	private final String streetNumber;

	private final String street;

	private final String apartmentNumber;

	private final String city;

	private final String postalCode;

	public ZhAddress(String streetNumber, String street, String apartmentNumber, String city, String postalCode) {
		this.streetNumber = streetNumber;
		this.street = street;
		this.apartmentNumber = apartmentNumber;
		this.city = city;
		this.postalCode = postalCode;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public String getStreet() {
		return street;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getAddressLine1() {
		String line = city + "市" +  street + streetNumber + "号";
		if (apartmentNumber.length() > 0)
			return line + " " + apartmentNumber + "房";
		else
			return line;
	}

	public String getAddressLine2() {
		return "邮编 " + postalCode;
	}

	@Override
	public String toString() {
		return getAddressLine1() + LINE_SEPARATOR + getAddressLine2();
	}
}
