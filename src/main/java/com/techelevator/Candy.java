package com.techelevator;

public class Candy extends Product{
    public Candy(String slotIdentifier, String name, double price) {
        super(slotIdentifier, name, price);
        setMessage("Yummy Yummy, So Sweet!");
    }
}
