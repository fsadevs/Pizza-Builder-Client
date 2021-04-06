package com.fsadev.pizzabuilder.ui.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.fsadev.pizzabuilder.ui.activities.MapsActivity;

public class LocationsFragment extends Fragment {
    private TextView txtAddress,txtArea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_locations, container, false);
        //------------------------------------------------------------------------------------------
        root.findViewById(R.id.locations_openMap).setOnClickListener(this::OpenMap);
        txtAddress = root.findViewById(R.id.location_txtAddress);
        txtArea = root.findViewById(R.id.locations_txtArea);
        //Inicializa el fragment
        getUserSavedLocation();
        //------------------------------------------------------------------------------------------
        return root;
    }

    //Recibe los datos de ubicacion de Firestore
    private void getUserSavedLocation() {
       txtAddress.setText(UserInfo.getAddress());
       txtArea.setText(UserInfo.getArea());
    }

    // abre el mapa
    private void OpenMap(View view) {
       startActivity(new Intent(requireActivity(), MapsActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserSavedLocation();
    }
}