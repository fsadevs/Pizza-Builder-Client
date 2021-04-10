package com.fsadev.pizzabuilder.game.pizzawars.data.drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.StringRes;

import com.fsadev.pizzabuilder.game.pizzawars.data.DrawerData;

import java.util.ArrayList;
import java.util.List;



public class MessageDrawer extends DrawerData {

    private static final long MESSAGE_TRANSITION = 250;

    private final List<String> messages;
    private long messageTime;
    private final Paint paint;
    // Constructor
    public MessageDrawer(Paint paint) {
        this.paint = paint;
        messages = new ArrayList<>();
    }


    // Muestra los mensajes
    public void drawMessage(Context context, @StringRes int messageRes) {
        drawMessage(context.getString(messageRes));
    }

    // Dibuja el mensaje
    public void drawMessage(String message) {
        messages.add(message);
        if (messages.size() == 1)
            messageTime = System.currentTimeMillis();
    }

    // Borra los mensajes
    public void clear() {
        while (messages.size() > 1)
            messages.remove(1);
    }

    @Override
    public boolean draw(Canvas canvas, float speed) {
        long diff = Math.abs(System.currentTimeMillis() - messageTime);
        // Si hay un mensaje que mostrar
        if (messages.size() > 0) {
            String str = messages.get(0);
            // Setea la duracion del mensaje en funcion del largo del texto a mostrar
            long delay = Math.max(3000, str.length() * 100);
            if (diff < delay) {
                if (diff < MESSAGE_TRANSITION) {
                    paint.setAlpha((int) (255 * ((float) diff / MESSAGE_TRANSITION)));
                } else if (delay - diff < MESSAGE_TRANSITION) {
                    paint.setAlpha((int) (255 * ((float) (delay - diff) / MESSAGE_TRANSITION)));
                } else paint.setAlpha(255);
                // Maneja el offset
                float width = paint.measureText(str);
                int offsetX = (int) ((width / 2) - (width * ((float) diff / delay)));
                // Dibuja el texto
                canvas.drawText(messages.get(0), (canvas.getWidth() / 2f) + offsetX,
                        canvas.getHeight() / 2f, paint);
            } else {
                // Remueve todo
                messages.remove(0);
                if (messages.size() > 0)
                    messageTime = System.currentTimeMillis();
            }
        }

        return true;
    }
}
