package com.robert.enachescurobert.robucoffeeshop.models;

public class Order {

    private String name;
    private String location;
    private int numberOfSimpleCoffees ;
    private int numberOfOreoCoffees;
    private int numberOfKitKatCoffees;
    private int numberOfToppingCoffees;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String name, String location, int numberOfSimpleCoffees, int numberOfOreoCoffees, int numberOfKitKatCoffees, int numberOfToppingCoffees) {
        this.name = name;
        this.location = location;
        this.numberOfSimpleCoffees = numberOfSimpleCoffees;
        this.numberOfOreoCoffees = numberOfOreoCoffees;
        this.numberOfKitKatCoffees = numberOfKitKatCoffees;
        this.numberOfToppingCoffees = numberOfToppingCoffees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfSimpleCoffees() {
        return numberOfSimpleCoffees;
    }

    public void setNumberOfSimpleCoffees(int numberOfSimpleCoffees) {
        this.numberOfSimpleCoffees = numberOfSimpleCoffees;
    }

    public int getNumberOfOreoCoffees() {
        return numberOfOreoCoffees;
    }

    public void setNumberOfOreoCoffees(int numberOfOreoCoffees) {
        this.numberOfOreoCoffees = numberOfOreoCoffees;
    }

    public int getNumberOfKitKatCoffees() {
        return numberOfKitKatCoffees;
    }

    public void setNumberOfKitKatCoffees(int numberOfKitKatCoffees) {
        this.numberOfKitKatCoffees = numberOfKitKatCoffees;
    }

    public int getNumberOfToppingCoffees() {
        return numberOfToppingCoffees;
    }

    public void setNumberOfToppingCoffees(int numberOfToppingCoffees) {
        this.numberOfToppingCoffees = numberOfToppingCoffees;
    }
}