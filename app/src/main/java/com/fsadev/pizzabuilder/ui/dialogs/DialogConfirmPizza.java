package com.fsadev.pizzabuilder.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.common.Formatear;
import com.fsadev.pizzabuilder.models.pizza.CartPizza;
import com.fsadev.pizzabuilder.models.pizza.ListPizza;
import com.fsadev.pizzabuilder.models.pizza.Pizza;

public class DialogConfirmPizza {
    //Genera el dialogo para confirmar y elegir la cantidad
    private int Cantidad = 1;
    private final Double Precio;
    private final Context context;
    private final String Salsa;
    private final String Queso;
    private final String Toppings;
    private final Dialog dialog;
    private final TextView txtCantidad;
    private final TextView txtPrecio;

    public DialogConfirmPizza(Fragment fragment, Pizza pizza, Double precio){
        Precio = precio;
        this.context = fragment.getContext();
        Salsa = pizza.getSauce();
        Queso = pizza.getCheese();
        Toppings = pizza.getToppings();

        //crea el dialogo y le asigna la vista
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm_pizza);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //TextViews---------------------------------------------------------------------------------
        TextView txtSalsa = dialog.findViewById(R.id.txt_dialogSalsa);
        txtSalsa.setText(Salsa);
        TextView txtQueso = dialog.findViewById(R.id.txt_dialogQueso);
        txtQueso.setText(Queso);
        TextView txtToppings = dialog.findViewById(R.id.txt_dialogToppings);
        txtToppings.setText(Toppings);
        txtCantidad = dialog.findViewById(R.id.dialog_txtCantidad);
        txtPrecio = dialog.findViewById(R.id.dialog_txtPrecio);
        //Inicializa el precio
        txtPrecio.setText(Formatear.ConvertirAPeso(Precio));
        //Boton Sumar-------------------------------------------------------------------------------
        dialog.findViewById(R.id.dialog_btnAdd).setOnClickListener(v -> {
            Cantidad++;
            UpdateCuantityAndPrice();
        });
        //Boton restar------------------------------------------------------------------------------
        dialog.findViewById(R.id.dialog_btnSubstract).setOnClickListener(v->{
            if(Cantidad >1){
                Cantidad--;
                UpdateCuantityAndPrice();
            }
        });
        //Boton confirmar---------------------------------------------------------------------------
        dialog.findViewById(R.id.dialog_btnClose).setOnClickListener(v-> ConfirmPizza(fragment));
        //Boton cancelar----------------------------------------------------------------------------
        dialog.findViewById(R.id.dialog_btnCancelar).setOnClickListener(v-> dialog.dismiss());
    }
    //Confirma la pizza
    private void ConfirmPizza(Fragment fragment) {
        //crea el objeto sin nombre
        CartPizza cartPizza = new CartPizza("NN",Salsa,Queso,Toppings,Precio);
        //actualiza la cantidad
        cartPizza.setCantidad(Cantidad);
        //añade la cartPizza a la lista
        ListPizza.addPizza(cartPizza);
        //lleva al carrito
        NavHostFragment.findNavController(fragment).navigate(R.id.action_nav_builder_to_nav_cart);
        //cierra el dialogo
        dialog.dismiss();
    }

    //Muestra el dialogo
    public void Show() {
        dialog.show();
    }

    //Actualiza la cantidad y anima el precio
    private void UpdateCuantityAndPrice() {
        txtCantidad.setText(String.valueOf(Cantidad));
        txtPrecio.setText(Formatear.ConvertirAPeso(Precio*Cantidad));
        txtPrecio.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context,R.anim.fastbounce));
    }



}
