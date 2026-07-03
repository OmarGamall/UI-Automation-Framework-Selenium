package com.automationexercise.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String username;
    private String email;
    private String password;

    // Account Information
    private String title; // "Mr" or "Mrs"
    private String birthDay; // e.g. "15"
    private String birthMonth; // e.g. "July"
    private String birthYear; // e.g. "1990"
    private boolean newsletter;
    private boolean optin;

    // Address Information
    private String firstName;
    private String lastName;
    private String company;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String city;
    private String zipcode;
    private String mobileNumber;
}
