package com.appsdeveloperblog.app.ws.shared.dto;

import java.io.Serializable;

public class AddressDto implements Serializable {

  private static final long serialVersionUID = 8310118171924476396L;
  private long id;
  private String addressId;
  private String city;
  private String country;
  private String streetName;
  private String postalCode;
  private String type;
  private UserDto userDetails;

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(final String streetName) {
    this.streetName = streetName;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public UserDto getUserDetails() {
    return userDetails;
  }

  public void setUserDetails(final UserDto userDetails) {
    this.userDetails = userDetails;
  }

  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(final String addressId) {
    this.addressId = addressId;
  }
}
