package com.fsadev.pizzabuilder.game.pizzawars.data;

import android.graphics.Rect;

public class ProjectileData {

    public float x, y, xDiff, yDiff;

    // Constructor
    public ProjectileData(float x, float y, float xDiff, float yDiff) {
        this.x = x;
        this.y = y;
        this.xDiff = xDiff;
        this.yDiff = yDiff;
    }

    // Calcula la posiccion en el siguiente frame
    // Retorna null si no se puede dibujar en la pantalla
    public Rect next(float speed, int width, int height) {
        if (x >= 0 && x <= 1 && y >= 0 && y <= height) {
            x += xDiff * speed;
            y += yDiff * speed;
        } else {
            return null;
        }
        float left = x * width;
        float top = height - y;
        // Tamaño del proyectil
        int size = 5;
        return new Rect((int) left - size, (int) top - size, (int) left + size, (int) top + size);
    }

}
