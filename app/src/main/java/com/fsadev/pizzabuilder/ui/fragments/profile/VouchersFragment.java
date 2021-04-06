package com.fsadev.pizzabuilder.ui.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.CurrentUser;
import com.fsadev.pizzabuilder.models.voucher.Voucher;
import com.fsadev.pizzabuilder.models.voucher.VoucherAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class VouchersFragment extends Fragment {

    private ArrayList<Voucher> voucherList;
    private VoucherAdapter adapter;
    private ConstraintLayout noVoucherLayout;

    public VouchersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment__vouchers, container, false);
        //Vista
        noVoucherLayout = root.findViewById(R.id.vouchers_emptyLayout);
        //boton ir a la tienda
        root.findViewById(R.id.vouchers_goToStore).setOnClickListener(this::goToStore);
        //recycler
        RecyclerView recyclerView = root.findViewById(R.id.vouchers_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        voucherList = new ArrayList<>();
        adapter = new VoucherAdapter(voucherList);
        recyclerView.setAdapter(adapter);
        //-----
        getUser();

        //------------------------------------------------------------------------------------------
        return root;
    }

    //Lleva a la tienda
    private void goToStore(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_profile_to_nav_store);
    }

    //Recibe los vouchers del usuario (si los tiene) y los pone en una lista
    private void getUser() {
        CurrentUser user = new CurrentUser();
        user.addUserListener(() -> GetVouchers(user));
    }

    private void GetVouchers(CurrentUser user) {

        String userID = user.getUserID();
        //consulta a la base de datos los vouchers donde el usuario sea el propietario
        Query voucherQuery = FirebaseFirestore.getInstance()
                .collection("Vouchers")
                .whereEqualTo("propietario",userID);
        //ejecuta la consulta
        voucherQuery.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                //bucle que itera en cada documento obtenido
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Voucher voucher = new Voucher(document);
                    voucherList.add(voucher);
                    adapter.notifyItemInserted(voucherList.size() - 1);
                }
                //verifica si la lista tiene elementos
                if (voucherList.size() == 0) {
                    NoVouchers();
                }
            }
        });
    }


    //Si el usuario no tiene vouchers
    private void NoVouchers() {
        noVoucherLayout.setVisibility(View.VISIBLE);
    }
}