package com.fsadev.pizzabuilder.models.pizza;

import com.google.firebase.firestore.DocumentSnapshot;

public class Pizza {
    private final String Sauce;
    private final String Cheese;
    private final String Toppings;
    private String Name;

    //Crea la pizza con un snapshot
    public Pizza(DocumentSnapshot doc) {
        Sauce = doc.getString("sauce");
        Cheese = doc.getString("cheese");
        Toppings = doc.getString("toppings");
        Name = doc.getId();
    }
    //Crea la pizza con todos sus parametros
    public Pizza(String sauce, String cheese, String toppings, String name) {
        Sauce = sauce;
        Cheese = cheese;
        Toppings = toppings;
        Name = name;
    }
    //Crea una pizza sin nombre

    public Pizza(String sauce, String cheese, String toppings) {
        Sauce = sauce;
        Cheese = cheese;
        Toppings = toppings;
        Name = "NN";
    }

    public String getName(){return Name;}
    public String getSauce() {
        return Sauce;
    }

    public String getCheese() {
        return Cheese;
    }

    public String getToppings() {
        return Toppings;
    }

    public void setName(String name) {
        Name = name;
    }
}
