package com.fsadev.pizzabuilder.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.ui.activities.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseNotificationService extends FirebaseMessagingService {

    //Se llama cuando un nuevo token es recibido
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        // recibe el nuevo token de notificaciones
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        sendRegistrationToServer(token);
                    }
                });
    }

    //Metodo que se invoca cuando el usuario recibe una notificacion y está usando la app
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String message = remoteMessage.getNotification().getBody();
        createNotification(title, message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance();
        createNotificationChannel();
    }

    //Manda el token al firestore para ser usado posteriormente
    private void sendRegistrationToServer(String token) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null) {
            FirebaseFirestore.getInstance().collection("Usuarios")
                    .document(Objects.requireNonNull(user).getUid())
                    .update("token", token);
        }
    }

    //Crea el canal de notificaciones
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificaciones";
            String description = "Notificaciones de CartPizza Builder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CartPizza Builder", name, importance);
            channel.setDescription(description);
            try {
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //CREA LA PLANTILLA DE NOTIFICACION
    private void createNotification(String Title, String Message) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CartPizza Builder")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(Title)
                .setContentText(Message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(173, builder.build());
    }


}