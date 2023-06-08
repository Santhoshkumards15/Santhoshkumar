package com.barathsandy.food;

public class Donations {

    private String donationType;
    private String donationWeight;
    private String donationVehivle;
    private String fName;
    private String Key;
    private String riderid;
    private String riderdate;
    private String ridertime;
    private String riderkey;

    private String Latitude;
    private String Longitude;
    private String Status;
    private int state;
    private String userID;
    private String connectingkey;
    private String donatorconnectingkey;

    private String Donationdate;
    private String Donationtime;


    private String address;
    private String street;
    private String mobile;



    public Donations() {
    }

    public String getRiderkey() {
        return riderkey;
    }

    public void setRiderkey(String riderkey) {
        this.riderkey = riderkey;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDonationdate() {
        return Donationdate;
    }

    public String getfName() {
        return fName;
    }

    public String getConnectingkey() {
        return connectingkey;
    }

    public String getDonatorconnectingkey() {
        return donatorconnectingkey;
    }

    public void setDonatorconnectingkey(String donatorconnectingkey) {
        this.donatorconnectingkey = donatorconnectingkey;
    }

    public void setConnectingkey(String connectingkey) {
        this.connectingkey = connectingkey;
    }

    public String getRiderid() {
        return riderid;
    }

    public String getRiderdate() {
        return riderdate;
    }

    public void setRiderdate(String riderdate) {
        this.riderdate = riderdate;
    }

    public String getRidertime() {
        return ridertime;
    }

    public void setRidertime(String ridertime) {
        this.ridertime = ridertime;
    }

    public void setRiderid(String riderid) {
        this.riderid = riderid;
    }

    public String getKey() {
        return Key;
    }
    public void setKey(String key) {
        Key = key;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setDonationdate(String donationdate) {
        Donationdate = donationdate;
    }

    public String getDonationtime() {
        return Donationtime;
    }

    public void setDonationtime(String donationtime) {
        Donationtime = donationtime;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getDonationWeight() {
        return donationWeight;
    }

    public void setDonationWeight(String donationWeight) {
        this.donationWeight = donationWeight;
    }

    public String getDonationVehivle() {
        return donationVehivle;
    }

    public void setDonationVehivle(String donationVehivle) {
        this.donationVehivle = donationVehivle;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
