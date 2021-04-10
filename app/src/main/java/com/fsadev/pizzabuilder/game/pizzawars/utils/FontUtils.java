package com.fsadev.pizzabuilder.game.pizzawars.utils;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.fsadev.pizzabuilder.R;


public class FontUtils {
    // Carda la fuente desde los recursos
    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Gobold-Bold.otf");
    }

}
