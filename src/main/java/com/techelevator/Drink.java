package com.techelevator;

public class Drink extends Product {
    public Drink(String slotIdentifier, String name, double price) {
        super(slotIdentifier, name, price);
        setMessage("Glug Glug, Yum!");

    }

}
