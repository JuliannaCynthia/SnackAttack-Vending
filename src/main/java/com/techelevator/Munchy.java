package com.techelevator;

public class Munchy extends Product{
    public Munchy(String slotIdentifier, String name, double price) {
        super(slotIdentifier, name, price);
        setMessage("Crunch Crunch, Yum!");
    }
}
