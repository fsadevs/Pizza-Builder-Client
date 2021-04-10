package com.fsadev.pizzabuilder.game.pizzawars.utils;

import android.content.Context;
import android.graphics.Typeface;


public class FontUtils {
    // Carda la fuente desde los recursos
    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Gobold-Bold.otf");
    }

}
