package com.fsadev.pizzabuilder.ui.fragments.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.CurrentUser;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.fsadev.pizzabuilder.ui.activities.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    private CurrentUser user;
    private TextInputEditText tbxName, tbxEmail, tbxPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_information, container, false);
        // Inflate the layout for this fragment
        tbxName = root.findViewById(R.id.profile_tbx_nombre);
        tbxEmail = root.findViewById(R.id.profile_tbx_email);
        tbxPhone = root.findViewById(R.id.profile_tbx_phone);
        //boton guardar
        root.findViewById(R.id.info_save).setOnClickListener(this::UpdateInfo);

        getUserInfo();
        //------------------------------------------------------------------------------------------
        return root;

    }

    //Actualiza los datos del usuario
    private void UpdateInfo(View view) {
        //Progreso del guardado
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("CartPizza Builder");
        progressDialog.setMessage("Guardando tus datos...");
        progressDialog.setIcon(R.drawable.logo_small);
        progressDialog.show();
        //Strings
        String nombre = Objects.requireNonNull(tbxName.getText()).toString();
        user.UpdateInfo(
                nombre,
                Objects.requireNonNull(tbxEmail.getText()).toString(),
                Objects.requireNonNull(tbxPhone.getText()).toString()
        );
        user.addUserListener(() -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Se guardaron los cambios", Toast.LENGTH_SHORT).show();});
        //Actualiza el nombre en el profileFragment
        try {
            //obtiene el fragment parent
            Fragment parent = getParentFragment();
            if (parent != null) {
                TextView txtName = parent.requireView().findViewById(R.id.profile_name);
                txtName.setText(nombre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Actualiza el nombre en homeActivity
        ((HomeActivity) requireActivity()).updateName(nombre);

    }

    //Recibe la informacion del usuario
    private void getUserInfo() {
        user = new CurrentUser();
        user.addUserListener(this::setUserInfo);
    }

    //Carga la informacion del usuario
    private void setUserInfo() {
        tbxName.setText(UserInfo.getName());
        tbxEmail.setText(UserInfo.getEmail());
        tbxPhone.setText(UserInfo.getPhoneNumber());
    }
}