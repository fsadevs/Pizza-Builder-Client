package com.fsadev.pizzabuilder.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.ui.activities.CheckoutActivity;


public class CommunityFragment extends Fragment {
    public CommunityFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_community, container, false);
        //------------------------------------------------------------------------------------------



        root.findViewById(R.id.btn_Play).setOnClickListener(v -> startActivity(new Intent(getActivity(), CheckoutActivity.class)));

        //------------------------------------------------------------------------------------------
        return root;
    }


}