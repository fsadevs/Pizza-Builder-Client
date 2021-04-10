package com.fsadev.pizzabuilder.game.pizzawars.data;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

public class IngredientData {

    public Bitmap ingredientBitmap;
    public float x, xDiff, y, yDiff, rotation, rotationDiff;
    public Rect position;

    // Constructor
    public IngredientData(Bitmap ingredientBitmap) {
        this.ingredientBitmap = ingredientBitmap;
        x = (float) Math.random();
        y = -ingredientBitmap.getHeight();
        rotationDiff = (float) Math.random() - 0.5f;
        xDiff = (float) (Math.random() - 0.5) * 0.002f;
        yDiff = (float) (Math.random() * 6) + 1;
    }

    // Calcula la posicion del ingrediente en el siguiente frame
    // Retorna null si ya no esta en la pantalla
    public Matrix next(float speed, int width, int height) {
        if ((y - ingredientBitmap.getHeight()) < height) {
            y += yDiff * speed;
            rotation += rotationDiff;
            x += xDiff * speed;
        } else {
            return null;
        }

        float left = x * width, top = y;
        // Rect donde se dibujara el ingrediente
        position = new Rect(
                (int) left - (ingredientBitmap.getWidth() / 2),
                (int) top - (ingredientBitmap.getHeight() / 2),
                (int) left + (ingredientBitmap.getWidth() / 2),
                (int) top + (ingredientBitmap.getHeight() / 2)
        );

        // Posicion en la matrix
        Matrix matrix = new Matrix();
        matrix.postTranslate(-ingredientBitmap.getWidth() / 2f,
                -ingredientBitmap.getHeight() / 2f);
        matrix.postRotate(rotation);
        matrix.postTranslate(left, top);

        return matrix;
    }

}
