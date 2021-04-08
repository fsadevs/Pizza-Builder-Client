package com.fsadev.pizzabuilder.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.order.CreateJSON;
import com.fsadev.pizzabuilder.models.order.PBOrder;
import com.fsadev.pizzabuilder.models.pizza.ListPizza;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.google.android.material.card.MaterialCardView;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 173;
    //TODO: CAMBIAR A PRODUCCION ANTES DE SACAR LA APP
    //pruebas
    private static final String ACCESS_TOKEN_TEST ="TEST-61229708102133-052918-0f722e950cd5cf567ac750361672936b-576188034" ;
    private static final String PUBLIC_KEY_TEST = "TEST-bd43712c-a2f0-44ba-9ca9-500163d0fbcc";
    //produccion
    private static final String ACCESS_TOKEN = "APP_USR-7619900982330777-032914-2b4d322e2a2f100724d2706789cb93b6-576188034";
    private static final String PUBLIC_KEY = "APP_USR-2a1a9929-3780-4215-b5a1-34326615bb7c";

    private boolean isMercadoPago = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        //Boton confirmar
        findViewById(R.id.checkout_btnMercadoPago).setOnClickListener(this::checkPaymentMethod);
        //Opciones de pago
        RadioGroup radioGroup = findViewById(R.id.checkout_RGroupOptPago);
        radioGroup.setOnCheckedChangeListener(this::setPaymentMethod);
        //card
        MaterialCardView card = findViewById(R.id.checkout_cardLocation);
        card.setOnCheckedChangeListener(this::CheckCardStatus);

    }

    private void CheckCardStatus(MaterialCardView card, boolean b) {
        card.setChecked(true);
    }

    //Cambia la variable de medio de pago
    private void setPaymentMethod(RadioGroup radioGroup, int i) {
        switch (i){
            case (R.id.optEfectivo): //opcion efectivo
                isMercadoPago=false;
                break;
            case (R.id.optMercadoPago): //opcion MercadoPago
                isMercadoPago=true;
                break;
        }
    }

    //Comprueba el medio de pago seleccionado
    private void checkPaymentMethod(View view) {
        if (isMercadoPago) {
            openMercadoPagoCheckOut(UserInfo.getEmail());
        }else{
            PagoEnEfectivo();
        }
    }

    //Pago en Efectivo
    private void PagoEnEfectivo() {
        RegisterOrder();
    }

    //Pago con MercadoPago
    private void openMercadoPagoCheckOut(String email) {
        //Crea el JSON
        JSONObject jsonObject = CreateJSON.NewJSON(email);
        //Crea el request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://api.mercadopago.com/checkout/preferences?access_token=" + ACCESS_TOKEN_TEST;
        //Request del string
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            try {
                Log.i("debinf MainAct", "response JSONObject: "+ response);
                //checkoutPreference id
                String checkoutPreferenceId = response.getString("id");
                //Abre el checkout de MercadoPago
                new MercadoPagoCheckout.Builder(PUBLIC_KEY_TEST, checkoutPreferenceId).build()
                        .startPayment(CheckoutActivity.this,REQUEST_CODE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.i("debinf MainAct", "response ERROR: "+ error.networkResponse.allHeaders)){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        //Inicializa el request
        requestQueue.add(jsonObjectRequest);
    }

    //Controla el resultado del pago con MercadoPago
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                //Comprueba el estado del pago
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                switch (payment.getPaymentStatus()){
                    case Payment.StatusCodes.STATUS_APPROVED:
                        Toast.makeText(this, "Tu pago ha sido aprobado", Toast.LENGTH_SHORT).show();
                        RegisterOrder();
                        break;
                    case Payment.StatusCodes.STATUS_PENDING:
                        Toast.makeText(this, "Tu pago está pendiente", Toast.LENGTH_SHORT).show();
                        RegisterOrder();
                        break;
                    case Payment.StatusCodes.STATUS_REJECTED:
                        Toast.makeText(this, "Se ha rechazado el pago. Por favor, intentá otro método", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case Payment.StatusCodes.STATUS_IN_PROCESS:
                        Toast.makeText(this, "El pago está en proceso...", Toast.LENGTH_SHORT).show();
                        break;
                }

            }else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getStringExtra("mercadoPagoError") != null) {
                    MercadoPagoError mercadoPagoError = JsonUtil.getGson()
                            .fromJson(data.getStringExtra("mercadoPagoError"), MercadoPagoError.class);

                    Toast.makeText(this,"Error en el pago. " +  mercadoPagoError.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //Registra la orden en la base de datos
    private void RegisterOrder() {
        PBOrder order = new PBOrder(ListPizza.getList());
        order.onSuccessUpload(() -> {
            //limpiar lista
            ListPizza.clearList();
            Toast.makeText(this, "Su pedido ha sido registrado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}