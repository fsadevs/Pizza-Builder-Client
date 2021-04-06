package com.fsadev.pizzabuilder.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.CurrentUser;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private ImageView profilePic;
    private TextView headerUserName;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Instancia el usuario
        GetUser();
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer layout
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Header view
        View headerView = navigationView.getHeaderView(0);
        //Header components
        profilePic = headerView.findViewById(R.id.nav_header_profilePic);
        profilePic.setOnClickListener(v -> {
            navController.navigate(R.id.action_global_nav_profile);
            drawer.close();
        });
        headerUserName = headerView.findViewById(R.id.nav_header_name);

        //Id de los menus para que aparezca el sandwich icon
        //Todos deben ser destinos toplevel
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_community,
                R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        // Navigation controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    //Instancia el usuario y crea un objeto User
    private void GetUser() {
        CurrentUser currentUser = new CurrentUser();
        currentUser.addUserListener(this::GetProfile);
    }

    //Carga los datos del usuario en las vistas
    private void GetProfile() {
        Glide.with(this)
                    .load(UserInfo.getProfilePicURL())
                    .placeholder(R.drawable.ic_profile)
                    .apply(new RequestOptions().override(100, 100))
                    .into(profilePic);
        headerUserName.setText(UserInfo.getName());
    }

    //se encarga de cargar el menu del toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Añade los items al menu del toolbar
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    //Funcionamiento de onClick para el menu del toolbar}
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_menu_cart){
            //llama al fragment carrito
            navController.navigate(R.id.action_global_nav_cart);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    //
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Cierra sesion y manda al login
    public void onClickLogout(MenuItem item){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    //Actualizar nombre desde el profileFragment cuando se guardan los cambios
    public void updateName(String nombre){
        headerUserName.setText(nombre);
    }

}