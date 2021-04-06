package com.fsadev.pizzabuilder.models.ingredients;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class Ingredient {
    private final String Nombre,Tipo, ImgURL;
    private final Double Precio;
    private final boolean Disponible;
    private boolean isChecked;

    public Ingredient(DocumentSnapshot snapshot) {
        Nombre = snapshot.getString("nombre");
        Tipo = snapshot.getString("tipo");
        ImgURL = snapshot.getString("imgURL");
        Precio = snapshot.getDouble("precio");
        Disponible = Objects.requireNonNull(snapshot.getBoolean("disponible"));
        isChecked = false;
    }

    public String getTipo() {
        return Tipo;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public Double getPrecio() {
        return Precio;
    }

    public Boolean getDisponibilidad(){return Disponible;}

    public String getNombre() {
        return Nombre;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
