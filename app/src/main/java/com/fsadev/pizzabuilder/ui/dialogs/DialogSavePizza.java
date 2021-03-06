package com.fsadev.pizzabuilder.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.pizza.Pizza;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DialogSavePizza {
    private final Pizza mPizza;
    private final Dialog dialog;
    private final TextInputEditText tbxName;
    private final Context context;
    private final Button btnConfirm;

    public DialogSavePizza(Fragment fragment, Pizza pizza){

        mPizza = pizza;
        context = fragment.getContext();
        //crea el dialogo y le asigna la vista
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_save_pizza);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TextViews---------------------------------------------------------------------------------
        TextView txtSalsa = dialog.findViewById(R.id.save_pizza_Salsa);
        txtSalsa.setText(mPizza.getSauce());
        TextView txtQueso = dialog.findViewById(R.id.save_pizza_Queso);
        txtQueso.setText(mPizza.getCheese());
        TextView txtToppings = dialog.findViewById(R.id.save_pizza_Toppings);
        txtToppings.setText(mPizza.getToppings());
        //TextInput
        tbxName = dialog.findViewById(R.id.save_pizza_name);
        addTextWatcher();
        //Boton confirmar---------------------------------------------------------------------------
        btnConfirm = dialog.findViewById(R.id.save_pizza_btnConfirm);
        btnConfirm.setOnClickListener(v-> ConfirmPizza(fragment));
        //Boton cancelar----------------------------------------------------------------------------
        dialog.findViewById(R.id.save_pizza_btnCancel).setOnClickListener(v-> dialog.dismiss());
        //Muestra el dialogo
        dialog.show();
    }
    //Inicializa el listener
    private void addTextWatcher() {
        tbxName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnConfirm.setEnabled(s.length()>0);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    //Confirma la pizza
    private void ConfirmPizza(Fragment fragment) {
        String name = Objects.requireNonNull(tbxName.getText()).toString();

            //sube la pizza a los favoritos del usuario
            FirebaseFirestore.getInstance().collection("Usuarios")
                    .document(UserInfo.getUserID()).collection("Favoritos")
                    .document(name).set(mPizza).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "A??adido a favoritos", Toast.LENGTH_SHORT).show();
                            //cierra el dialogo
                            NavHostFragment.findNavController(fragment).navigate(R.id.action_nav_builder_to_nav_favorites);
                            dialog.dismiss();
                        }
            });

    }


}
