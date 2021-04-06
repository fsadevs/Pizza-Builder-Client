package com.fsadev.pizzabuilder.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.common.PermissionUtils;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.fsadev.pizzabuilder.ui.dialogs.DialogMapTutorial;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean isPermissionDenied = false;
    private GoogleMap mMap;
    private LatLng selectedLatLng, currentLatLng;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean isLocated = false;
    private TextInputEditText tbx_address, tbx_dpto, tbx_piso;
    private String deliveryArea="";
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //callback del mapa cargado
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        //Muestra el tutorial del mapa
        new DialogMapTutorial(this);
        //vistas
        tbx_address = findViewById(R.id.map_address);
        tbx_dpto = findViewById(R.id.map_dpto);
        tbx_piso = findViewById(R.id.map_piso);
        //
        locationServiceStatusCheck();
        //Inicializa el cliente de localizacion
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setLocationCallBack();
        //Botones
        btnSave = findViewById(R.id.btn_set_location);
        btnSave.setOnClickListener(v -> saveLocationClick());
        findViewById(R.id.btn_location).setOnClickListener(v -> setFusedLocationClient());
        //Animacion del marker}
        findViewById(R.id.placePicker_img).startAnimation(AnimationUtils.loadAnimation(this,R.anim.map_marker));
    }

    //GUARDA LA DIRECCION EN LA DATABASE
    private void saveLocationClick() {
        String piso = "", dpto = "";
        if (!Objects.requireNonNull(tbx_piso.getText()).toString().isEmpty()) {
            piso = "; Piso " + Objects.requireNonNull(tbx_piso.getText()).toString();
        }
        if (!Objects.requireNonNull(tbx_dpto.getText()).toString().isEmpty()) {
            dpto = " Dpto " + Objects.requireNonNull(tbx_dpto.getText()).toString();
        }
        //String de la direccion
        String address = Objects.requireNonNull(tbx_address.getText()).toString() + piso + dpto;
        //Crea los datos para subir a firestore
        GeoPoint location = new GeoPoint(selectedLatLng.latitude,selectedLatLng.longitude);
        Map<String, Object> data = new HashMap<>();
        data.put("loc", location);
        data.put("address",address);
        data.put("area",deliveryArea);
        //Sube los datos
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.DialogStyle);
        progressDialog.setIcon(R.drawable.logo_small);
        progressDialog.setTitle("CartPizza Builder");
        progressDialog.setMessage("Guardando ubicación actual...");
        progressDialog.show();
        FirebaseFirestore.getInstance().collection("Usuarios")
                .document(UserInfo.getUserID()).update(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        //Guarda localmente
                        UserInfo.setLoc(location);
                        UserInfo.setArea(deliveryArea);
                        UserInfo.setAddress(address);
                        //Cierra el dialogo y termina la actividad
                        progressDialog.dismiss();
                        Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
        });

    }

    // Callback que recibe la ubicacion desde el FusedLocationClient
    private void setLocationCallBack(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Guarda las coordenadas de la ubicacion actual
                Location lastLocation = locationResult.getLastLocation();
                currentLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                // Avisa que se obtuvo una coordenada
                isLocated = true;
                // Anima la camara hasta esas coordenadas
                animateCamera(currentLatLng);

            }
        };
    }

    //Metodo que se invoca cuand el mapa se carga
    @Override
    public void onMapReady(GoogleMap map) {
        // Setea el estilo del mapa
        mMap = map;
        map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.blue));
        //Verifica los permisos
        checkMapPermissions();
        //Setea el listener de ubicacion
        camListener();
        //Pruebas con poligonos
        LatLng pizzaBuilder = new LatLng(-26.1938981,-58.1879776);
        map.addCircle(new CircleOptions()
                .center(pizzaBuilder)
                .radius(3000)
                .strokeColor(Color.RED)
        );
        map.addCircle(new CircleOptions()
                .center(pizzaBuilder)
                .radius(2000)
                .strokeColor(Color.YELLOW)
        );
        map.addCircle(new CircleOptions()
                .center(pizzaBuilder)
                .radius(1000)
                .strokeColor(Color.GREEN)
        );
    }

    // Listener para el movimiento de la camara
    private void camListener() {
        mMap.setOnCameraIdleListener(() -> {
            if (isLocated) {
                // Apaga las solicitudes de ubicacion actual
                fusedLocationClient.removeLocationUpdates(locationCallback);
                btnSave.setEnabled(true);
                isLocated = false;
            }

            // Guarda las coordenadas del punto medio del mapa, seleccionado por el usuario
            selectedLatLng = mMap.getCameraPosition().target;
            getStreetName(selectedLatLng.latitude, selectedLatLng.longitude);
            // Calcula la distancia
            CalculateDistance();
        });
    }
    //Calcula la distancia
    private void CalculateDistance() {
        float[] results = new float[1];
        try {
            Location.distanceBetween(selectedLatLng.latitude, selectedLatLng.longitude,
                    -26.1938981, -58.1879776, results);
            //Verifica en que area queda la ubicacion
            if (results[0]<1000){
                deliveryArea="AREA 1";
            }else if (results[0]<2000){
                deliveryArea="AREA 2";
            }else if(results[0]<3000){
                deliveryArea="AREA 3";
            }else{
                deliveryArea="FUERA DE AREA";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Recibe una coordenada como parametro y devuelve un string con el nombre de la calle
    private void getStreetName(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        //inicializa el geocoder
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // maxResult define que solo devuelva un nombre
            if (addresses.size() > 0) {
                // Verifica que haya recibido un resultado
                String address = addresses.get(0).getAddressLine(0);
                // Crea el string y lo visualiza
                String streetName = address.substring(0, address.indexOf(","));
                tbx_address.setText(streetName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Setea y activa el cliente de ubicacion
    private void setFusedLocationClient() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Requerimiento del metodo
        // Verifica que los permisos esten activos para solicitar la ubicacion actual
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,null);
    }

    //Mueve la camara a la ubicacion actual
    private void animateCamera(LatLng location){
        if (location!=null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
        }
    }

    //Verifica los permisos de ubicacion
    private void checkMapPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                setFusedLocationClient();
            }
        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    // START- PERMISOS DE MAPA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            checkMapPermissions();
        } else {
            isPermissionDenied = true;
        }
    }
    //Verifica si se consedio el permiso
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (isPermissionDenied) {
            // Muestra el dialogo para solicitar el permiso
            PermissionUtils.PermissionDeniedDialog
                    .newInstance(true).show(getSupportFragmentManager(), "dialog");
            isPermissionDenied = false;
        }
    }


    //Verifica que este activada la ubicación
    public void locationServiceStatusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Si no esta activado muestra un dialogo
            buildAlertMessageNoGps();
        }
    }

    // Crea una alerta para activar la ubicacion
    private void buildAlertMessageNoGps() {
        //TODO: CAMBIAR POR EL DIALOGO PERSONALIZADO
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Activar GPS");
        builder.setMessage("El servicio de ubicación está desactivado. Necesitamos activarlo para conocer tu posición actual.")
                .setCancelable(false)
                .setPositiveButton("  ACEPTAR  ", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("  CANCELAR  ", (dialog, id) -> {
                    dialog.cancel();
                    finish();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //MUEVE LA CAMARA A LA UBICACION ACTUAL AL VOLVER A LA APLICACION
    @Override
    protected void onResume() {
        super.onResume();
        animateCamera(currentLatLng);
    }

}