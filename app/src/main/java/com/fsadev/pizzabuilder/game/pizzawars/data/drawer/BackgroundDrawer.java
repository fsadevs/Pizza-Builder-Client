package com.fsadev.pizzabuilder.game.pizzawars.data.drawer;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.fsadev.pizzabuilder.game.pizzawars.data.CloudData;
import com.fsadev.pizzabuilder.game.pizzawars.data.DrawerData;
import com.fsadev.pizzabuilder.game.pizzawars.data.ParticleData;

import java.util.ArrayList;
import java.util.List;

public class BackgroundDrawer extends DrawerData {

    private final List<ParticleData> particles;
    private final List<CloudData> clouds;
    private int frame;
    // Constructor
    public BackgroundDrawer(Paint paint, Paint cloudPaint) {
        super(paint, cloudPaint);
        particles = new ArrayList<>();
        particles.add(new ParticleData(paint(0)));
        clouds = new ArrayList<>();
        clouds.add(new CloudData(cloudPaint, 0, 0));
        clouds.add(new CloudData(cloudPaint, 1, 1));
    }

    @Override
    public boolean draw(Canvas canvas, float speed) {
        // Nubes
        for (CloudData cloud : new ArrayList<>(clouds)) {
            if (!cloud.draw(canvas, speed))
                clouds.remove(cloud);
        }
        // Estrellas
        for (ParticleData particle : new ArrayList<>(particles)) {
            if (!particle.draw(canvas, speed))
                particles.remove(particle);
        }
        // Añade las estrellas
        particles.add(new ParticleData(paint(0)));
        // Avanza el frame
        frame++;
        // Cada 10 frames hace:
        if (frame % 10 == 0) {
            frame = 0;
            // Pinta aleatoriamente una nube
            clouds.add(new CloudData(paint(1),
                    (float) Math.max(0, Math.min(canvas.getWidth() - 1, clouds.get(clouds.size() - 2).getStart() + (Math.random() * 0.2) - 0.1)),
                    (float) Math.max(0, Math.min(canvas.getWidth() - 1, clouds.get(clouds.size() - 2).getEnd() + (Math.random() * 0.2) - 0.1))));
            clouds.add(new CloudData(paint(1),
                    (float) Math.max(0, Math.min(canvas.getWidth() - 1, clouds.get(clouds.size() - 2).getStart() + (Math.random() * 0.2) - 0.1)),
                    (float) Math.max(0, Math.min(canvas.getWidth() - 1, clouds.get(clouds.size() - 2).getEnd() + (Math.random() * 0.2) - 0.1))));
        }
        return true;
    }
}
