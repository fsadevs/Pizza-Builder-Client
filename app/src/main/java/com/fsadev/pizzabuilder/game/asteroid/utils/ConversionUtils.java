package com.fsadev.pizzabuilder.game.asteroid.utils;

import android.content.res.Resources;

public class ConversionUtils {

    //Clase estatica para convertir unidades
    public static int getPixelsFromDp(float dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }

    public static float getDpFromPixels(int pixels) {
        return pixels / Resources.getSystem().getDisplayMetrics().density;
    }

    public static int getPixelsFromSp(float sp) {
        return (int) (Resources.getSystem().getDisplayMetrics().scaledDensity * sp);
    }

    public static float getSpFromPixels(int pixels) {
        return pixels / Resources.getSystem().getDisplayMetrics().scaledDensity;
    }

}
