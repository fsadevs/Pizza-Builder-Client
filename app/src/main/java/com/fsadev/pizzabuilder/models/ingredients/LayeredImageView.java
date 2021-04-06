package com.fsadev.pizzabuilder.models.ingredients;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

import java.util.ArrayList;



public class LayeredImageView extends androidx.appcompat.widget.AppCompatImageView {
    //Lista con las imagenes
    ArrayList<Bitmap> mLayers;

    public LayeredImageView(Context context) {
        super(context);
        init();
    }
    //Constructores
    public LayeredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    //Inicia la lista
    public void init() {
        mLayers = new ArrayList<>();
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        super.setAdjustViewBounds(adjustViewBounds);
    }
    //Dibuja las capas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = getImageMatrix();
        if (matrix != null) {
            int numLayers = mLayers.size();
            for (int i = 0; i < numLayers; i++) {
                Bitmap b = mLayers.get(i);

                if (b!=null) {
                    canvas.drawBitmap(b, matrix, null);
                }
            }
        }
    }
    //Redimenciona la imagen y lo carga en la lista
    public void addLayer(Bitmap b) {
       Bitmap resizedBitmap = Bitmap.createScaledBitmap(b, this.getWidth(), this.getHeight(), false);
        mLayers.add(resizedBitmap);
        invalidate();
    }
    //Limpia el canvas
    public void removeAllLayers() {
        mLayers.clear();
        invalidate();
    }

}