package com.fsadev.pizzabuilder.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.fsadev.pizzabuilder.R;

public class DialogMapTutorial {
    Context context;
    private Dialog dialog;

    public DialogMapTutorial(Context context) {
        this.context = context;

        //crea el dialogo y le asigna la vista
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_map_tutorial);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Boton cerrar
        dialog.findViewById(R.id.dialog_btnClose).setOnClickListener(this::Close);
        dialog.show();
    }


    //Cierra el dialogo
    private void Close(View view) {
        dialog.dismiss();
    }
}
