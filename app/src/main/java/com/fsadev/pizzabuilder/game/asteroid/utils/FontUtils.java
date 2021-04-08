package com.fsadev.pizzabuilder.game.asteroid.utils;

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

    public static Typeface getTypeface(Context context) {
//        return Typeface.createFromAsset(context.getAssets(), "BlackOpsOne-Regular.ttf");
        return Typeface.createFromAsset(context.getAssets(), "Gobold-Bold.otf");

    }

    public static void toast(Context context, String message) {
        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(getTypeface(context));
        textView.getPaint().setShader(new LinearGradient(
                0, 0, 0,
                textView.getLineHeight(),
                ContextCompat.getColor(context, R.color.colorAccent),
                ContextCompat.getColor(context, R.color.color_base2),
                Shader.TileMode.REPEAT
        ));

        Toast toast = new Toast(context);
        toast.setView(textView);
        toast.setGravity(Gravity.BOTTOM, 0, ConversionUtils.getPixelsFromDp(64));
        toast.setDuration(message.length() > 40 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.show();
    }

}
