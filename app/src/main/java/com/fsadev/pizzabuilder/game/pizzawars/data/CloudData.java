package com.fsadev.pizzabuilder.game.pizzawars.data;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class CloudData extends DrawerData {

    private int y = -10;
    private final float start;
    private final float end;
    // Constructor
    public CloudData(Paint paint, float start, float end) {
        super(paint);
        this.start = start;
        this.end = end;
    }

    // Retorna la posicion inicial de la nube de 0 a 1
    public float getStart() {
        return start;
    }

    // Retorna el final de la nube de 0 a 1
    public float getEnd() {
        return end;
    }

    // Calcula el proximo frame, retorna null si ya no esta en la pantalla
    public Rect next(float speed, int width, int height) {
        if (y <= height) {
            y++;
        } else return null;

        return new Rect((int) (start * width), y, (int) (end * width), y + 10);
    }

    // Dibuja la nube
    @Override
    public boolean draw(Canvas canvas, float speed) {
        Rect rect = next(speed, canvas.getWidth(), canvas.getHeight());
        if (rect != null) {
            canvas.drawRect(rect, paint(0));
            return true;
        } else return false;
    }
}
