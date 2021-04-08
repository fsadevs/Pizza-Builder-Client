package com.fsadev.pizzabuilder.ui.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.CurrentUser;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.fsadev.pizzabuilder.models.user.UserProgress;
import com.fsadev.pizzabuilder.ui.fragments.profile.ProfileAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }

    private ImageView profileImageView;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private CircularProgressIndicator circularProgressIndicator;
    private TextView txtName,txtPoints,txtLevel;
    private CurrentUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //-----------------------------------------------------------------------------------------
        //Foto de perfil
        profileImageView = root.findViewById(R.id.profile_Image);
        //boton cambiar imagen de perfil
        root.findViewById(R.id.profile_btnChangePic).setOnClickListener(this::CropImage);
        profileImageView.setOnClickListener(this::CropImage);
        //Informacion del usuario
        txtName = root.findViewById(R.id.profile_name);
        txtPoints = root.findViewById(R.id.profile_points);
        txtLevel = root.findViewById(R.id.profile_level);
        //Puntaje del usuario
        circularProgressIndicator = root.findViewById(R.id.profile_progress);
        //View pager - inicializa y setea el adapter
        ProfileAdapter adapter = new ProfileAdapter(this);
        viewPager = root.findViewById(R.id.profile_viewPager);
        viewPager.setAdapter(adapter);
        //Tab Layout
        tabLayout = root.findViewById(R.id.profile_tabLayout);
        setupTabLayout(); //vincula el viewpager con el tablayout
        //Metodos
        VerifyPermissions();
        getUserInfo();
        //-----------------------------------------------------------------------------------------
        return root;
    }

    //Maneja el progreso del usuario
    private void userPoints() {
        UserProgress userProgress = new UserProgress();
        userProgress.addProgressListener(() -> {
            int puntos = userProgress.getPoints();
            //userLevel = {points, level, pointsToNextLevel }
            String strPoints = puntos + " Puntos";
            String strLevel = "Nivel " + userProgress.getLevel();
            //Asigna los puntos a los textviews
            txtPoints.setText(strPoints);
            txtLevel.setText(strLevel);
            //Setea el progress indicator
            circularProgressIndicator.setMaxProgress(userProgress.getNextLevelRequirement());
            circularProgressIndicator.setCurrentProgress(puntos);
            circularProgressIndicator.animate();
        });
    }

    //Setea el tab layout con el viewpager
    private void setupTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                //cambia el nombre de la pestaña
                    String tabName = "";
                    switch (position) {
                        case 0:
                            tabName = "Mis Datos";
                            break;
                        case 1:
                            tabName = "Mi Ubicación";
                            break;
                        case 2:
                            tabName = "Vouchers";
                            break;
                    }
                    tab.setText(tabName);
                }).attach();
    }

    //Recibe la informacion del usuario
    public void getUserInfo() {
        user = new CurrentUser();
        txtName.setText(UserInfo.getName());
        setUserProfileImg(UserInfo.getProfilePicURL());
        userPoints();
    }

    //Carga la imagen de perfil del usuario desde el storage
    private void setUserProfileImg(String imgURL) {
        Glide.with(this)
                .load(imgURL)
                .into(profileImageView);
    }


    //Verifica los permisos
    private void VerifyPermissions() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        if (estadoDePermiso != PackageManager.PERMISSION_GRANTED) {
            // Si no, entonces pedimos permisos
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    2);
        }
    }

    //Abre la actividad para elegir la imagen via camara o galeria
    private void CropImage(View view) {
        CropImage.activity()
                .setActivityTitle("")
                .setAspectRatio(1, 1)
                .setMultiTouchEnabled(true)
                .start(requireContext(), this);
    }

    //Maneja el resultado de la actividad
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Si se selecciono una imagen crea el uri
                Uri resultUri = result.getUri();
                if (resultUri != null) {
                    UploadImage(resultUri); //sube la imagen
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "Error seleccionando la imágen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Sube la imagen al Cloud Storage y actualiza la informacion
    private void UploadImage(Uri resultUri) {
        //Crea las referencias a la storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageRef.child("FotosPerfil/" + UserInfo.getUserID());
        UploadTask uploadTask = profilePicRef.putFile(resultUri);
        //Progreso
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.DialogStyle);
        progressDialog.setTitle("Pizza Builder");
        progressDialog.setMessage("Subiendo imagen de perfil...");
        progressDialog.setIcon(R.drawable.logo_small);
        progressDialog.show();
        // Listeners para el proceso
        uploadTask.addOnFailureListener(exception ->
                //Fallo al subir la foto
                Toast.makeText(getContext(), "Ocurrio un fallo al subir la imágen", Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(taskSnapshot -> {
                    //Foto subida correctamente
                    profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        //actualiza la URL de la foto en la base de datos
                        user.ChangeProfilePicURL(uri.toString());
                        //actualiza la imagen
                        profileImageView.setImageURI(resultUri);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Foto actualizada", Toast.LENGTH_SHORT).show();
                    });
                });
    }


}