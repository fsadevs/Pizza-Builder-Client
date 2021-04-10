package com.fsadev.pizzabuilder.game.pizzawars.data;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

public class BoxData {

    public Bitmap boxBitmap;
    public float x, y, yDiff, rotation, rotationDiff;
    public Rect position;
    public BoxOpenedListener listener;

    // Constructor
    public BoxData(Bitmap boxBitmap, BoxOpenedListener listener) {
        this.boxBitmap = boxBitmap;
        this.listener = listener;
        x = (float) Math.random();
        y = -boxBitmap.getHeight();
        rotationDiff = (float) Math.random() - 0.5f;
        yDiff = (float) (Math.random() * 3) + 1;
    }

    // Abre la caja y da al juegador el contenido
    public void open() {
        listener.onBoxOpened(this);
    }

    /**
     * Calculates the position Matrix of the box to draw in the
     * next frame.
     *
     * @param speed         The speed of the box.
     * @param width         The width of the drawing canvas.
     * @param height        The height of the drawing canvas.
     * @return              The position Matrix of the box. Equals
     *                      null if the box can no longer be drawn
     *                      within the given width/height.
     */
    // Calcula la posicion de la caja en el proximo frame
    public Matrix next(float speed, int width, int height) {
        if ((y - boxBitmap.getHeight()) < height) {
            y += yDiff * speed;
            rotation += rotationDiff;
        } else return null;

        float left = x * width, top = y;
        // Dibuja la caja
        position = new Rect(
                (int) left - (boxBitmap.getWidth() / 2),
                (int) top - (boxBitmap.getHeight() / 2),
                (int) left + (boxBitmap.getWidth() / 2),
                (int) top + (boxBitmap.getHeight() / 2)
        );
        // Posicion de la caja
        Matrix matrix = new Matrix();
        matrix.postTranslate(-boxBitmap.getWidth() / 2f, -boxBitmap.getHeight() / 2f);
        matrix.postRotate(rotation);
        matrix.postTranslate(left, top);
        // Retorna el valor
        return matrix;
    }
    // Listener para la clase
    public interface BoxOpenedListener {
        void onBoxOpened(BoxData box);
    }

}
