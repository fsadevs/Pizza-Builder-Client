package com.fsadev.pizzabuilder.game.pizzawars.utils;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.game.pizzawars.data.WeaponData;
import com.fsadev.pizzabuilder.game.pizzawars.views.GameView;

public class AchievementUtils implements GameView.GameListener {

    private final Context context;

    private boolean isTutorial;
    private boolean isOutOfAmmo;
    private int ingredientsHitted;
    private int ingredientsPassed;
    private long outOfAmmoTime;

    public AchievementUtils(Context context) {
        this.context = context;
    }

    @Override
    public void onStart(boolean isTutorial) {
        this.isTutorial = isTutorial;
        isOutOfAmmo = false;
        ingredientsHitted = 0;
        ingredientsPassed = 0;
    }

    @Override
    public void onTutorialFinish() {

        isTutorial = false;
        //Logro: Tutorial completado
        CreateNotification("Tutorial completado!");
    }

    @Override
    public void onStop(int score) {
        isTutorial = false;
    }

    @Override
    public void onIngredientPassed() {
        if (!isTutorial) {
            if (ingredientsHitted == 0 && ingredientsPassed == 0){
//                unlock(R.string.achievement_coward);
            }
            ingredientsPassed++;
            if (ingredientsPassed ==100){
                //Logro: Cinturas
                CreateNotification("Cinturas");
            }
        }
    }

    @Override
    public void onIngredientCrashed() {
        if (!isTutorial && isOutOfAmmo && System.currentTimeMillis() - outOfAmmoTime < 3000){
            //Logro: "Esto no es Space Invaders!"
            CreateNotification("Esto no es Space Invaders");
        }
    }

    @Override
    public void onWeaponUpgraded(WeaponData weapon) {

        if (weapon.equals(WeaponData.WEAPONS[1])){
            //Logro: Una más a mi coleccion
            CreateNotification("Una más a mi colección");
        }
        if (weapon.equals(WeaponData.WEAPONS[5])){
            CreateNotification("A mitad de camino");
        }
        if (weapon.equals(WeaponData.WEAPONS[WeaponData.WEAPONS.length - 1])){
            CreateNotification("Todo un arsenal");
        }
    }

    @Override
    public void onAmmoReplenished() {
        if (!isTutorial) {
            if (isOutOfAmmo){

            }
//                unlock(R.string.achievement_close_call);
            isOutOfAmmo = false;
        }
    }

    @Override
    public void onProjectileFired(WeaponData weapon) {

    }

    @Override
    public void onOutOfAmmo() {
        if (!isTutorial) {
            if (!isOutOfAmmo)
                outOfAmmoTime = System.currentTimeMillis();
            isOutOfAmmo = true;

            if (System.currentTimeMillis() - outOfAmmoTime > 3000){

            }
//                unlock(R.string.achievement_how_did_i_get_here);
        }
    }

    @Override
    public void onIngredientHit(int score) {
        if (!isTutorial && ingredientsHitted == 0 && ingredientsPassed == 0){

        }
        ingredientsHitted++;
        if (ingredientsHitted ==1000){
            //Logro: Asesino de ingredientes
        }
    }

    //Desbloquea un logro
    private void unlock(@StringRes int key) {

    }

    //Crea la notificacion
    private void CreateNotification(String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "PBMsg")
                .setSmallIcon(R.drawable.logo_small)
                .setColor(Color.WHITE)
                .setContentTitle("Logro desbloqueado")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(175, builder.build());
    }
}
