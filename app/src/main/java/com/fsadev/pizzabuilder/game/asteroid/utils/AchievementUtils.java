package com.fsadev.pizzabuilder.game.asteroid.utils;

import android.content.Context;

import androidx.annotation.StringRes;

import com.fsadev.pizzabuilder.game.asteroid.data.WeaponData;
import com.fsadev.pizzabuilder.game.asteroid.views.GameView;
import com.google.android.gms.common.api.GoogleApiClient;

public class AchievementUtils implements GameView.GameListener {

    private final Context context;

    private boolean isTutorial;
    private boolean isOutOfAmmo;

    private int asteroidsHit;
    private int asteroidsPassed;
    private long outOfAmmoTime;

    public AchievementUtils(Context context, GoogleApiClient apiClient) {
        this.context = context;
    }

    @Override
    public void onStart(boolean isTutorial) {
        this.isTutorial = isTutorial;
        isOutOfAmmo = false;
        asteroidsHit = 0;
        asteroidsPassed = 0;
    }

    @Override
    public void onTutorialFinish() {
        isTutorial = false;
    }

    @Override
    public void onStop(int score) {
        isTutorial = false;

    }

    @Override
    public void onAsteroidPassed() {
        if (!isTutorial) {
            if (asteroidsHit == 0 && asteroidsPassed == 0){

            }
//                unlock(R.string.achievement_coward);
            asteroidsPassed++;
        }
    }

    @Override
    public void onAsteroidCrashed() {
        if (!isTutorial && isOutOfAmmo && System.currentTimeMillis() - outOfAmmoTime < 3000){

        }
//            unlock(R.string.achievement_almost_had_it);
    }

    @Override
    public void onWeaponUpgraded(WeaponData weapon) {
        if (weapon.equals(WeaponData.WEAPONS[5])){

        }
//            unlock(R.string.achievement_halfway_there);
        if (weapon.equals(WeaponData.WEAPONS[WeaponData.WEAPONS.length - 1])){

        }
//            unlock(R.string.achievement_particle_beam);
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
    public void onAsteroidHit(int score) {
        if (!isTutorial && asteroidsHit == 0 && asteroidsPassed == 0)
//            unlock(R.string.achievement_savage);
        asteroidsHit++;
    }

    private void unlock(@StringRes int key) {

    }
}
