package com.fsadev.pizzabuilder.models.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserProgress {
    private ProgressListener listener;
    private int Level;
    private int Points;
    private int NextLevelRequirement;
    private int CurrentLevelRequirement, CurrentProgress;

    public UserProgress() {
        //recibe el progreso del usuario
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore.getInstance().collection("Progreso").document(userID)
                .get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot doc = Objects.requireNonNull(task.getResult());
               getProgress(doc);
           }
        });
    }

    private void getProgress(DocumentSnapshot doc) {
        //calcula el progreso
        try {
            Level = Objects.requireNonNull(doc.getDouble("nivel")).intValue();
            Points = Objects.requireNonNull(doc.getDouble("puntos")).intValue();
            //Puntos necesarios para estar en el nivel actual, se usara como piso
            CurrentLevelRequirement = (int) Math.pow(Level, 2) * 1000;
            //Puntos necesarios para subir al siguiente nivel
            NextLevelRequirement = ((int) Math.pow(Level + 1, 2) * 1000) - CurrentLevelRequirement;
            //Progreso del nivel actual, esto es los puntos sobrantes del usuario despues de subir de nivel
            CurrentProgress = Points - CurrentLevelRequirement;
            //Manda el callback
            listener.onSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Getters---------------------------------------------------------------------------------------

    public int getLevel() {
        return Level;
    }

    public int getPoints() {
        return Points;
    }

    public int getNextLevelRequirement() {
        return NextLevelRequirement;
    }

    public int getCurrentProgress(){return CurrentProgress;}

    //interface que se reescribe para diferentes usos-----------------------------------------------
    public interface ProgressListener{
        void onSuccess();
    }
    //Inicializa el listener
    public void addProgressListener(ProgressListener listener){
        this.listener = listener;
    }

}
