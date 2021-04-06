package com.fsadev.pizzabuilder.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fsadev.pizzabuilder.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 147; //numero random para el intent de seleccionar la cuenta de Google
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Instancia del Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Instancia del Firestore
         db = FirebaseFirestore.getInstance();
        // Configuración Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_token))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        //Boton de iniciar sesión
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> SignInWithGoogle());

    }

    //abre un nuevo intent para que el usuario seleccione la cuenta de Google
    private void SignInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //callback para manejar la respuesta luego de que el usuario elige la cuenta
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(Objects.requireNonNull(account).getIdToken());

                } catch (ApiException e) {
                    // Manda un error si no se puede autenticar
                   LoginFailedMessage();
                }
            }else{
                LoginFailedMessage();
            }
        }
    }

    //Inicia sesión en Firebase con los datos de Google
    private void firebaseAuthWithGoogle(String idToken) {
        progressDialog = new ProgressDialog(this,R.style.DialogStyle);
        progressDialog.setIcon(R.drawable.logo_small);
        progressDialog.setTitle("CartPizza Builder");
        progressDialog.setMessage("Iniciando sesión con Google. Por favor, espere...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //si el inicio es exitoso lleva al usuario al looby
                        loginSuccesful();
                    } else {
                        // Mensaje si la autenticación falla
                        progressDialog.dismiss();
                        LoginFailedMessage();

                    }
                });
    }

    //Mensaje de error al iniciar sesión
    private void LoginFailedMessage() {
        Toast.makeText(LoginActivity.this,
                "Ha ocurrido un error iniciando sesión.",
                Toast.LENGTH_SHORT).show();
    }

    //Inicio de sesión exitoso
    private void loginSuccesful() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        //Comprueba si el usuario existe en Firestore
        db.collection("Usuarios").document(Objects.requireNonNull(currentUser).getUid()).get()
               .addOnSuccessListener(documentSnapshot -> {
                   if (documentSnapshot.exists()){
                       //si el documento existe manda al usuario al HomeActivity
                       Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                        StartHomeActivity();
                   }else{
                       //crea el usuario en Firestore
                       CreateNewUser(currentUser);
                   }
               });
        
    }

    //Sube los datos del nuevo usuario al Firestore
    private void CreateNewUser(FirebaseUser currentUser) {
        //recibe el token de notificaciones
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token="";
             if(task.isSuccessful()){
                 token = task.getResult();
             }
            Map<String, Object> user = new HashMap<>();
            user.put("name",  currentUser.getDisplayName());
            user.put("email", currentUser.getEmail());
            user.put("tel", null);
            user.put("imgURL", Objects.requireNonNull(currentUser.getPhotoUrl()).toString());
            user.put("token",token);
            user.put("address","undefined");
            user.put("loc",new GeoPoint(0,0));
            user.put("area","undefined");
            //sube el contenido
            db.collection("Usuarios").document(currentUser.getUid()).set(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(LoginActivity.this, "Registro completado", Toast.LENGTH_SHORT).show();
                        StartHomeActivity();
                    });
        });

    }


    //Inicia el HomeActivity
    private void StartHomeActivity() {
        progressDialog.dismiss();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.zoomin, R.anim.fadeout);
        finish();
    }


}