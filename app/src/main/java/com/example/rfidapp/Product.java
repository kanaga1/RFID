package com.example.rfidapp;

import com.google.gson.annotations.SerializedName;

public class Product {


    @SerializedName("Date")
    private String Date;
    @SerializedName("Total")
    private String Total;
    @SerializedName("Available")
    private String Available;
    @SerializedName("Sold")
    private String Sold;

    public Product(String Date, String Total, String Available, String Sold) {
        this.Date = Date;
        this.Total = Total;
        this.Available = Available;
        this.Sold = Sold;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String Available) {
        this.Available = Available;
    }

    public String getSold() {
        return Sold;
    }

    public void setSold(String Sold) {
        this.Sold = Sold;
    }
}
