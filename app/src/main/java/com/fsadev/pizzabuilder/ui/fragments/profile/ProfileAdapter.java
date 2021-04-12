package com.fsadev.pizzabuilder.ui.fragments.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Objects;

public class ProfileAdapter extends FragmentStateAdapter {
    //Clase que controla los fragments del viewpager del perfil
    //Se vincula con el view pager y retorna los fragments

    public ProfileAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    //Retorna el fragment segun la posicion del tab
    @Override  @NonNull
    public Fragment createFragment(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment= new InfoFragment();
                break;
            case 1:
                fragment = new LocationsFragment();
                break;
            case 2:
                fragment=new VouchersFragment();
                break;

        }
        return Objects.requireNonNull(fragment);
    }
    //Retorna la cantidad de fragments
    @Override
    public int getItemCount() {
        return 3;
    }
}
