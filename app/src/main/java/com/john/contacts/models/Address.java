package com.john.contacts.models;


;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by johns on 7/30/2017.
 */

public class Address implements Serializable
{
    @SerializedName("street")
    @Expose
    private String street="";
    @SerializedName("city")
    @Expose
    private String city="";
    @SerializedName("state")
    @Expose
    private String state="";
    @SerializedName("country")
    @Expose
    private String country="";
    @SerializedName("zipCode")
    @Expose
    private String zipCode="";

    public Address()
    {

    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


}
