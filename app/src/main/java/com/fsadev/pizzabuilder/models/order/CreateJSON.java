package com.fsadev.pizzabuilder.models.order;

import com.fsadev.pizzabuilder.models.pizza.ListPizza;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateJSON {

    public static JSONObject NewJSON(String email) {
        //JSON Objects
        JSONObject jsonObject = new JSONObject();

        final JSONObject payerJSON = new JSONObject();
        JSONArray itemJsonArray = new JSONArray();

        //Lista de items
        for(int i = 0;i<ListPizza.getList().size();i++){
            try{
                //nombre - si el nombre no esta definido lo arma como CartPizza personalizada
                String nombre=ListPizza.getList().get(i).getNombre();
                if (nombre.equals("NN")) {
                 nombre = "CartPizza personalizada - " + (i + 1);
                }
                //Armado del item
                JSONObject itemJSON = new JSONObject();
                itemJSON.put("title", nombre);
                itemJSON.put("description", "Pedido de CartPizza Builder");
                itemJSON.put("quantity", ListPizza.getList().get(i).getCantidad());
                itemJSON.put("currency_id", "ARS");
                itemJSON.put("unit_price", ListPizza.getList().get(i).getPrecio());

                itemJsonArray.put(itemJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {

            //payer, toma el email que tiene el usuario
            payerJSON.put("email", email);

            //Pone items y payer en un solo JSON
            jsonObject.put("items", itemJsonArray);
            jsonObject.put("payer", payerJSON);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
