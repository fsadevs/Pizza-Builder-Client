package com.fsadev.pizzabuilder.models.ingredients;

import java.util.ArrayList;

public class ToppingsList {

    public static String getStringList(ArrayList<Ingredient> lista){
        //Bucle para recorrer todos los toppings y detectar los seleccionados
        StringBuilder strToppings= new StringBuilder();
        for (Ingredient item : lista){
            if(item.isChecked()){
                strToppings.append(item.getNombre()).append(", ");
            }
        }
        //Verifica si se selecciono algun topping evaluando el parametro length
        if(!strToppings.toString().equals("")) {
            //Quita la coma al final
            return strToppings.toString().substring(0,strToppings.length()-2);
        }
        return "Ninguno";
    }
}
