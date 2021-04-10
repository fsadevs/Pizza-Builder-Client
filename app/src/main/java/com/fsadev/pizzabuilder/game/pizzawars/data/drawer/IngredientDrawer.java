package com.fsadev.pizzabuilder.game.pizzawars.data.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.game.pizzawars.data.IngredientData;
import com.fsadev.pizzabuilder.game.pizzawars.data.DrawerData;
import com.fsadev.pizzabuilder.game.pizzawars.data.ParticleData;
import com.fsadev.pizzabuilder.game.pizzawars.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class IngredientDrawer extends DrawerData {

    private final List<IngredientData> ingredients;
    List<ParticleData> particles;
    private final Bitmap ingredientBitmap;
    private final Bitmap ingredientBitmap2;
    private final Bitmap ingredientBitmap3;
    private final Bitmap ingredientBitmap4;
    private final Bitmap ingredientBitmap5;
    private long ingredientTime;
    private float ingredientLength;
    private boolean shouldMakeIngredients;

    // Constructor
    public IngredientDrawer(Context context, int colorAccent, int colorPrimary, Paint ingredientPaint, Paint particlePaint) {
        super(ingredientPaint, particlePaint);
        // Listas de las particulas y los ingredientes
        ingredients = new ArrayList<>();
        particles = new ArrayList<>();
        //Crea los bitmap de cada ingrediente
        ingredientBitmap = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(context, R.drawable.ic_cheesee), colorAccent, colorPrimary);
        ingredientBitmap2 = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(context, R.drawable.ic_tomato), Color.RED, colorPrimary);
        ingredientBitmap3 = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(context, R.drawable.ic_mushroom), Color.YELLOW, Color.WHITE);
        ingredientBitmap4 = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(context, R.drawable.ic_bell_pepper), Color.GREEN, Color.YELLOW);
        ingredientBitmap5 = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(context, R.drawable.ic_egg), colorAccent, colorPrimary);
    }

    // Setea si el drawer debe generar ingredientes
    public void setMakeIngredients(boolean shouldMakeIngredients) {
        this.shouldMakeIngredients = shouldMakeIngredients;
        ingredientLength = 3000;
        if (shouldMakeIngredients) {
            ingredients.clear();
        }
    }

    // Crea un nuevo ingrediente
    public void makeNew() {
        // Ajusta el tiempo al sistema
        ingredientTime = System.currentTimeMillis();
        // Genera un numero aleatorio y con eso selecciona un modelo de ingrediente
        int randomNum = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        switch (randomNum){
            case 1:
                ingredients.add(new IngredientData(ingredientBitmap));
                break;
            case 2:
                ingredients.add(new IngredientData(ingredientBitmap2));
                break;
            case 3:
                ingredients.add(new IngredientData(ingredientBitmap3));
                break;
            case 4:
                ingredients.add(new IngredientData(ingredientBitmap4));
                break;
            case 5:
                ingredients.add(new IngredientData(ingredientBitmap5));
                break;
        }
    }

    // Retorna la cantidad de ingredientes visibles en la pantalla
    public int size() {
        return ingredients.size();
    }

    // Determina si hay un ingrediente en la posicion que toma como parametro
    // Si es asi retorna el ingrediente
    // Esta es la funcion principal del detector de colisiones
    public IngredientData ingredientAtPosition(Rect position) {
        for (IngredientData ingredient : ingredients) {
            if (ingredient.position != null && position.intersect(ingredient.position)) {
                return ingredient;
            }
        }
        return null;
    }

   // Destruye el ingrediente
    public void destroy(IngredientData asteroid) {
        ingredients.remove(asteroid);
        // Cantidad de particulas generadas en la explosion
        int explosionParticles = 30;
        //Genera las particulas
        for (int i = 0; i < explosionParticles; i++) {
            particles.add(new ParticleData(paint(1), asteroid.x, asteroid.y));
        }
    }

    // Dibuja el ingrediente
    @Override
    public boolean draw(Canvas canvas, float speed) {
        boolean isPassed = false;
        // Por cada uno de los ingredientes
        for (IngredientData ingredient : new ArrayList<>(ingredients)) {
            Matrix matrix = ingredient.next(speed, canvas.getWidth(), canvas.getHeight());
            if (matrix != null) {
                canvas.drawBitmap(ingredient.ingredientBitmap, matrix, paint(0));
            } else {
                // Si el ingrediente salio de la pantalla
                isPassed = true;
                ingredients.remove(ingredient);
                if (ingredientLength > 750) {
                    ingredientLength -= (ingredientLength * 0.1);
                }
            }
        }
        // Particulas
        for (ParticleData particle : new ArrayList<>(particles)) {
            if (!particle.draw(canvas, 1))
                particles.remove(particle);
        }
        // Determina si generar un nuevo ingrediente o no
        if (shouldMakeIngredients && System.currentTimeMillis() - ingredientTime > ingredientLength) {
            makeNew();
        }
        return isPassed;
    }
}
