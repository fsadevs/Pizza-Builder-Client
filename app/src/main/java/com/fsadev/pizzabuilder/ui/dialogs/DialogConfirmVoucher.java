package com.fsadev.pizzabuilder.ui.dialogs;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.fsadev.pizzabuilder.models.user.UserProgress;
import com.fsadev.pizzabuilder.models.voucher.Voucher;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class DialogConfirmVoucher {
    private final Voucher voucher;
    private final Context context;
    private final Dialog dialog;
    private final UserProgress userProgress;
    private final ConstraintLayout progressIndicatorLayout;
    private final CircularProgressIndicator circularProgressIndicator;
    private TextView userLevel, userPoints;
    private final ImageView loadingImg;
    private final Button btnConfirm;
    private final View divider;

    public DialogConfirmVoucher(Voucher voucher, Context context, UserProgress userProgress) {
        this.voucher = voucher;
        this.context = context;
        this.userProgress = userProgress;
        //crea el dialogo y le asigna la vista
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm_voucher);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Muestra el dialog
        dialog.show();
        // Boton confirmar
        btnConfirm = dialog.findViewById(R.id.confirm_voucher_btnConfirm);
        btnConfirm.setOnClickListener(v -> Purchase(voucher.getVoucherID()));
        // Boton cancelar
        dialog.findViewById(R.id.confirm_voucher_btnCancelar).setOnClickListener(v -> dialog.dismiss());
        //Divisor
        divider = dialog.findViewById(R.id.confirm_voucher_divider);
        //Layout de progreso
        progressIndicatorLayout = dialog.findViewById(R.id.confirm_voucher_resultLayout);
        circularProgressIndicator = dialog.findViewById(R.id.confirm_voucher_indicator);
        loadingImg = dialog.findViewById(R.id.confirm_voucher_loading);
        // Carga los datos
        initializeDialog();
    }
    //Carga los datos del voucher seleccionado
    private void initializeDialog() {
        //Progreso
        userPoints = dialog.findViewById(R.id.confirm_voucher_userPoints);
        userLevel = dialog.findViewById(R.id.confirm_voucher_userLevel);
        //Nombre
        TextView name = dialog.findViewById(R.id.confirm_voucher_name);
        name.setText(String.format("%s en tu %s", voucher.getName(), voucher.getType()));
        //Precio
        TextView price = dialog.findViewById(R.id.confirm_voucher_price);
        price.setText(String.format("%s puntos", voucher.getPrice()));
        //Saldo
        TextView balance = dialog.findViewById(R.id.confirm_voucher_balance);
        balance.setText(String.format("%s puntos", userProgress.getPoints()));

    }

    // Maneja la compra del voucher
    private void Purchase(String voucherID) {
        //Deshabilita el boton
        btnConfirm.setEnabled(false);

        //Mueve el divider hacia la izquierda para "ocultar" el boton de cancelar
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) divider.getLayoutParams();
        params.horizontalBias = 0f;
        divider.setLayoutParams(params);

        // Muestra la capa de progreso
        progressIndicatorLayout.setVisibility(View.VISIBLE);

        // Setea los textviews de nivel y puntos
        userLevel.setText(String.format("Nivel %s", userProgress.getLevel()));
        userPoints.setText(String.format("%s puntos", userProgress.getPoints()));

        // Anima la entrada del progressIndicator
        circularProgressIndicator.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoomin));

        // Setea los valores
        circularProgressIndicator.setProgress(userProgress.getCurrentProgress(),userProgress.getNextLevelRequirement());

        //Loading image
        Glide.with(context).load(R.drawable.anim_loading1).into(loadingImg);

        // Genera la URL para la peticion
        String req = "https://us-central1-pizzabuilderapp.cloudfunctions.net/addvoucher?id="
                + UserInfo.getUserID() + "&vid=" + voucherID;

        // Inicializa la peticion
        RequestQueue queue = Volley.newRequestQueue(context);

        // Crea la string de la peticion
        StringRequest stringRequest = new StringRequest(Request.Method.GET, req, response -> {
            if (response.equals("OK")){
                    SuccessfulPurchase();
            }else if (response.equals("NOPOINTS")){
                    Toast.makeText(context, "No tenés puntos suficientes para la compra", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
            }
        }, error -> {
            //Error en la peticion
           Toast.makeText(context, "Error al comprar el voucher", Toast.LENGTH_SHORT).show();
           dialog.dismiss();
        });
        //Manda la peticion
        queue.add(stringRequest);
    }
    //Compra exitosa
    private void SuccessfulPurchase() {
        // Calcula el progreso despues de la compra
       UserProgress afterProgress = new UserProgress();
       afterProgress.addProgressListener(()->{
           //Carga la segunda parte del gif de loading
           Glide.with(context).load(R.drawable.anim_loading2).listener(new RequestListener<Drawable>() {
               @Override
               public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                   return false;
               }
               @Override
               public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                   if (resource instanceof GifDrawable) {
                       ((GifDrawable)resource).setLoopCount(1); // veces que se repite el gif
                   }
                   return false;
               }
           }).into(loadingImg);

           // Setea el progress indicator
           circularProgressIndicator.setProgress(
                   afterProgress.getCurrentProgress(),
                   afterProgress.getNextLevelRequirement());

           //Anima los puntos del usuario disminuyendo
           ValueAnimator valueAnimator = ValueAnimator.ofInt(userProgress.getPoints(),afterProgress.getPoints());
           valueAnimator.setDuration(1500);
           valueAnimator.addUpdateListener(value -> userPoints.setText(String.format("%s puntos", value.getAnimatedValue().toString())));
           valueAnimator.start();

           //Setea el nivel
           userLevel.setText(String.format("Nivel %s", afterProgress.getLevel()));

           //Habilita el boton pero con la funcion de cerrar el dialog
           btnConfirm.setOnClickListener(v->dialog.dismiss());
           btnConfirm.setEnabled(true);
       });

    }
}
