package com.sac.campusborrow.model;

/**
 * Created by ionut on 12/10/2017.
 */

public class User {
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String address;
    public double rating;
    public int obiecteLuate;
    public int obiecteOferite;
    public int counterRating;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getObiecteLuate() {
        return obiecteLuate;
    }

    public void setObiecteLuate(int obiecteLuate) {
        this.obiecteLuate = obiecteLuate;
    }

    public int getObiecteOferite() {
        return obiecteOferite;
    }

    public void setObiecteOferite(int obiecteOferite) {
        this.obiecteOferite = obiecteOferite;
    }

    public String getDisplayName() { return firstName+" "+lastName; }

    public int getCounterRating() {
        return counterRating;
    }

    public void setCounterRating(int counterRating) {
        this.counterRating = counterRating;
    }
}
