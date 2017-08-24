package in.co.nmsworks.hibernate5.domain;

import java.io.Serializable;

/**
 * Created by kamal (kamal@nmsworks.co.in) on 2/4/17.
 * <p>
 * Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
 * NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
public class Address implements Serializable {

    private int addressId;
    private String streetName;
    private String city;
    private int pincode;
    private String countryName;

    public Address() {
    }

    public Address(String streetName, String city, int pincode, String countryName) {
        this.streetName = streetName;
        this.city = city;
        this.pincode = pincode;
        this.countryName = countryName;
    }

    public Address(String streetName, String city) {
        this.streetName = streetName;
        this.city = city;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", pincode=" + pincode +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}
