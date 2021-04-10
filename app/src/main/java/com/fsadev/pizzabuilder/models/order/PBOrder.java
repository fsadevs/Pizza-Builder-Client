package com.fsadev.pizzabuilder.models.order;

import com.fsadev.pizzabuilder.models.pizza.CartPizza;
import com.fsadev.pizzabuilder.models.user.CurrentUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PBOrder {
    private orderListener mListener;
    //Objeto que controla las ordenes

    public PBOrder(ArrayList<CartPizza> orderList){
        CurrentUser user = new CurrentUser();
        user.addUserListener(()->{
            //Referencia a la nueva orden
            DatabaseReference orderFDB = user.getDatabaseREF().child("Pedidos").push();
            orderFDB.setValue(true);
            String orderID = orderFDB.getKey();
            //Añade el orderID al historial del usuario
            user.getFirestoreREF().update("pedidos", FieldValue.arrayUnion(orderID));
            //Sube la orden a la realtime database
            uploadOrder(orderList,orderFDB);
        });
    }

    //sube la orden a la base de datos
    private void uploadOrder(ArrayList<CartPizza> orderList, DatabaseReference orderREF){
        new Thread(()->{
            Map<String,Object> order = new HashMap<>();
            for(int i=0;i<orderList.size();i++){
                //crea el hashmap para subir
                order.put(String.valueOf(i),orderList.get(i));
            }
            //sube el map a la referencia
            orderREF.setValue(order).addOnCompleteListener(task -> {
                //Verifica que se haya completado la carga
                if (task.isSuccessful() && mListener!=null){
                    mListener.onSuccess();
                }
            });
        }).start();

    }
    public interface orderListener {
        void onSuccess();
    }
    //Inicializa el listener
    public void onSuccessUpload(orderListener listener){
        mListener = listener;
    }

}
