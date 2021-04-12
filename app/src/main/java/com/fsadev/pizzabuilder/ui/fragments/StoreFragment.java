package com.fsadev.pizzabuilder.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.UserProgress;
import com.fsadev.pizzabuilder.models.voucher.StoreAdapter;
import com.fsadev.pizzabuilder.models.voucher.Voucher;
import com.fsadev.pizzabuilder.ui.dialogs.DialogConfirmVoucher;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class StoreFragment extends Fragment {
    private RecyclerView pizzaRecycler,deliveryRecycler;
    private StoreAdapter pizzaAdapter, deliveryAdapter;
    private ArrayList<Voucher> pizzaList, deliveryList;
    private ProgressDialog progressDialog;
    private UserProgress userProgress;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        // Setea el progressdialog
        generateProgressDialog();

        // Recyclerview de descuentos
        pizzaList = new ArrayList<>();
        pizzaAdapter = new StoreAdapter(pizzaList);
        pizzaRecycler = root.findViewById(R.id.store_DiscountsRecycler);
        pizzaRecycler.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        // Recyclerview de delivery
        deliveryList = new ArrayList<>();
        deliveryAdapter = new StoreAdapter(deliveryList);
        deliveryRecycler = root.findViewById(R.id.store_DeliveryRecycler);
        deliveryRecycler.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        //Recibe los puntos del usuario
        getUserPoints();


        //------------------------------------------------------------------------------------------
        return root;
    }

    // Obtiene el progreso del usuario
    private void getUserPoints() {
        UserProgress userProgress = new UserProgress();
        userProgress.addProgressListener(this::populateRecyclers);
    }

    //Crea el progress dialog
    private void generateProgressDialog() {
        progressDialog = new ProgressDialog(requireContext(),R.style.DialogStyle);
        progressDialog.setTitle("Pizza Builder");
        progressDialog.setIcon(R.drawable.logo_small);
        progressDialog.setMessage("Estamos verificando tus datos para la compra...");
    }

    // Recibe y llena las listas
    private void populateRecyclers() {
        FirebaseFirestore.getInstance().collection("Tienda").get().addOnCompleteListener(task->{
           if (task.isSuccessful()){
               // Clasifica los vouchers por tipo
               for (QueryDocumentSnapshot doc : task.getResult()){
                   String type = doc.getString("tipo");
                   switch (type){
                       case "delivery":
                           deliveryList.add(new Voucher(doc));
                           break;
                       case "pedido":
                           pizzaList.add(new Voucher(doc));
                           break;
                   }
               }
               // Setea el adaptador de vouchers de delivery
               deliveryAdapter.addOnItemClickListener(position -> {
                   Purchase(deliveryList.get(position));
               });
               deliveryRecycler.setAdapter(deliveryAdapter);
               // Setea el adaptador de vouchers de pizza
               pizzaAdapter.addOnItemClickListener(position -> {
                   Purchase(pizzaList.get(position));
               });
               pizzaRecycler.setAdapter(pizzaAdapter);
           }
        });
    }

    // Maneja la compra del voucher
    private void Purchase(Voucher voucher) {
        progressDialog.show();
        userProgress = new UserProgress();
        userProgress.addProgressListener(()->{
            progressDialog.dismiss();
            //Verifica si el usuario cumple los requisitos
            if (userProgress.getPoints() >= voucher.getPrice()) {
                new DialogConfirmVoucher(voucher, requireContext(), userProgress);
            }else{
                Toast.makeText(getContext(), "No tenés puntos suficientes para el canje", Toast.LENGTH_SHORT).show();
            }
        });

    }
}