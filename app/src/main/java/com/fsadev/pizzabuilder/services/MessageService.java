package com.fsadev.pizzabuilder.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.fsadev.pizzabuilder.ui.activities.HomeActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageService extends Service {
    DatabaseReference messageRef;
    //REQUIRED EMPTY METHOD
    public MessageService() { }
    //REQUIRED BIND METHOD RETURN NULL
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        messageRef = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(UserInfo.getUserID()).child("Chat");
        createNotificationChannel();
        setupListener();
    }

    //AL INICIAR SERVICIO
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    //CREA EL CANAL PARA RECIBIR LAS NOTIFICACIONES
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificaciones";
            String description = "Servicio de mensajeria de Pizza Builder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("PBMsg", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //CREA LA PLANTILLA DE NOTIFICACION
    private void createNotification(String content) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "PBMsg")
                .setSmallIcon(R.drawable.logo_small)
                .setColor(Color.WHITE)
                .setContentTitle("Nuevo mensaje de Pizza Buider")
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(174, builder.build());
    }

    //LISTENER DE CAMBIOS EN EL HISTORIAL
    private void setupListener() {
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Verifica que sea un mensaje nuevo y del servidor
                boolean isReaded = (boolean) snapshot.child("readed").getValue();
                boolean isMe = (boolean) snapshot.child("me").getValue();
                //Crea la notificacion
                if (!isReaded && !isMe) {
                    String content = snapshot.child("content").getValue().toString();
                    createNotification(content);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

}
