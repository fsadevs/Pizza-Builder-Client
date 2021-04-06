package com.fsadev.pizzabuilder.models.pizza;

import java.util.ArrayList;


public class ListPizza {
    private static final ArrayList<CartPizza> LIST_CART_PIZZA = new ArrayList<>();
    //Añade una cartPizza
    public static void addPizza(CartPizza cartPizza){
        LIST_CART_PIZZA.add(cartPizza);
    }
    //Borra una pizza
    public static void removePizza(int index){
        LIST_CART_PIZZA.remove(index);
    }
    //Limpia la lista
    public static void clearList(){
        LIST_CART_PIZZA.clear();
    }
    //Retorna la lista
    public static ArrayList<CartPizza> getList(){
        return LIST_CART_PIZZA;
    }
}
