package com.fsadev.pizzabuilder.models.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CurrentUser {

    private final DocumentReference FirestoreREF;
    private final DatabaseReference FirebaseREF;
    private DocumentSnapshot userSnapshot;
    private final String userID;
    private UserListener listener;

    public CurrentUser() {
        //Usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         userID = Objects.requireNonNull(user).getUid();
        //Instancia del Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //referencia
        FirestoreREF = db.collection("Usuarios").document(userID);
        FirebaseREF = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID);
        //Creacion del objeto
        FirestoreREF.get().addOnCompleteListener(task->{
            if (task.isSuccessful() && listener!=null){
                userSnapshot = Objects.requireNonNull(task.getResult());
                UserInfo.setUserObject(userSnapshot);
                //callback
                listener.onSuccess();
            }
        });

    }


    //Actualizar datos
    public void UpdateInfo(String nombre, String email, String phoneNumber){
        Map<String, Object> data = new HashMap<>();
        data.put("nombre",nombre);
        data.put("email",email);
        data.put("tel", phoneNumber);
        FirestoreREF.update(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserInfo.setName(nombre);
                UserInfo.setEmail(email);
                UserInfo.setPhoneNumber(phoneNumber);
                //callback
                listener.onSuccess();
            }
        });
    }

    //cambiar la foto de perfil
    public void ChangeProfilePicURL(String picURL) {
        FirestoreREF.update("imgURL", picURL);
    }

    //Retornos--------------------------------------------------------------------------------------

    //retorna el userID
    public String getUserID(){
        return userID;
    }

    //Referencias
    public DocumentReference getFirestoreREF() {
        return FirestoreREF;
    }
    public DatabaseReference getDatabaseREF() {
        return FirebaseREF;
    }

    //interface que se reescribe para diferentes usos-----------------------------------------------
    public interface UserListener{
        void onSuccess();
    }
    //Inicializa el listener
    public void addUserListener(UserListener listener){
        this.listener = listener;
    }


}
