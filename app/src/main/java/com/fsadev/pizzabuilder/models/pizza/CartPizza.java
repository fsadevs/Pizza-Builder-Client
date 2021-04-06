package com.fsadev.pizzabuilder.models.pizza;

public class CartPizza {
    private final String Nombre;
    private final String Salsa;
    private final String Queso;
    private final String Toppings;
    private final Double Precio;
    private int Cantidad;

    public CartPizza(String nombre, String salsa, String queso, String toppings, Double precio) {
        Nombre = nombre;
        Salsa = salsa;
        Queso = queso;
        Toppings = toppings;
        Cantidad = 1;
        Precio=precio;
    }



    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getSalsa() {
        return Salsa;
    }

    public String getQueso() {
        return Queso;
    }

    public String getToppings() {
        return Toppings;
    }

    public Double getPrecio() {
        return Precio;
    }

    public int getCantidad() {
        return Cantidad;
    }
}
