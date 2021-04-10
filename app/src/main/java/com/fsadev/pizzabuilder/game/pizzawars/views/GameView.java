package com.fsadev.pizzabuilder.game.pizzawars.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.core.content.ContextCompat;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.game.pizzawars.data.BoxData;
import com.fsadev.pizzabuilder.game.pizzawars.data.IngredientData;
import com.fsadev.pizzabuilder.game.pizzawars.data.ProjectileData;
import com.fsadev.pizzabuilder.game.pizzawars.data.WeaponData;
import com.fsadev.pizzabuilder.game.pizzawars.data.drawer.BackgroundDrawer;
import com.fsadev.pizzabuilder.game.pizzawars.data.drawer.IngredientDrawer;
import com.fsadev.pizzabuilder.game.pizzawars.data.drawer.MessageDrawer;
import com.fsadev.pizzabuilder.game.pizzawars.utils.FontUtils;
import com.fsadev.pizzabuilder.game.pizzawars.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;



public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {

    private final Paint paint, accentPaint;
    private final SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Thread thread;
    //Nave
    private final Bitmap shipBitmap;
    private float shipPositionX = 0.5f;
    private float shipPositionY = -1f;
    private float shipPositionStartX;
    private float shipRotation;
    //Caja de municion
    private final Bitmap boxBitmap;
    private final List<BoxData> boxes;
    //Arma
    private WeaponData weapon;
    private final List<ProjectileData> projectiles;
    private long projectileTime;
    //Drawers
    private final BackgroundDrawer background;
    private final IngredientDrawer ingredients;
    private final MessageDrawer messages;
    //Valores del juego
    private ValueAnimator animator;
    private GameListener listener;
    private boolean isPlaying;
    public int score, ingredientsPassedCount;
    private float speed = 1; // velocidad inicial del juego
    private float ammo;
    private ValueAnimator ammoAnimator;
    // Tutorial
    private int tutorial;
    private static final int TUTORIAL_NONE = 0;
    private static final int TUTORIAL_MOVE = 1;
    private static final int TUTORIAL_UPGRADE = 2;
    private static final int TUTORIAL_ASTEROID = 3;
    private static final int TUTORIAL_REPLENISH = 4;
    // Constructores
    public GameView(Context context) {
        this(context, null);
    }
    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolder = getHolder();

        //Colores
        int colorPrimaryLight = ContextCompat.getColor(getContext(), R.color.colorPrimaryLight);
        int colorPrimary = ContextCompat.getColor(getContext(), R.color.naranja_base);
        int colorAccent = ContextCompat.getColor(getContext(), R.color.colorAccent);
        int cloudColor = ContextCompat.getColor(getContext(), R.color.cloud);

        //Paint del proyectil
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        //Paint de las particulas de explosion
        accentPaint = new Paint();
        accentPaint.setColor(colorAccent);
        accentPaint.setStyle(Paint.Style.FILL);
        accentPaint.setAntiAlias(true);

        //Nubes
        Paint cloudPaint = new Paint();
        cloudPaint.setColor(cloudColor);
        cloudPaint.setAlpha(50);
        cloudPaint.setStyle(Paint.Style.FILL);
        cloudPaint.setAntiAlias(true);

        //Textos
        Paint textPaint = new Paint();
        textPaint.setColor(colorPrimaryLight);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(40);
        textPaint.setTypeface(FontUtils.getTypeface(context));

        //Inicializa el drawer del fondo
        background = new BackgroundDrawer(paint, cloudPaint);

        //Inicializa el drawer de ingredientes
        ingredients = new IngredientDrawer(getContext(), colorAccent, colorPrimary, paint, accentPaint);

        //Inicialia el drawer de los mensajes
        messages = new MessageDrawer(textPaint);

        //Inicializa los proyectiles
        projectiles = new ArrayList<>();

        //Nave
        shipBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.logo_small);
        //Caja de municiones
        boxBitmap = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(getContext(), R.drawable.ic_game_ammo_box), colorAccent, colorPrimary);
        boxes = new ArrayList<>();
    }

    //Setea el listener de eventos
    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    return;
                }
                canvas.drawColor(Color.BLACK);

                // TUTORIAL 1RA PARTE --------------------------------------------------------------

                if (boxes.size() == 0 && tutorial == TUTORIAL_UPGRADE) {
                    BoxData box = new BoxData(WeaponData.WEAPONS[0].getBitmap(getContext()), box2 -> {
                        weapon = WeaponData.WEAPONS[0];
                        ammo = weapon.capacity;
                        tutorial++;
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (listener != null)
                                listener.onWeaponUpgraded(weapon);
                        });
                    });

                    box.x = 0.5f;
                    box.yDiff = 2;
                    boxes.add(box);
                    messages.clear();
                    messages.drawMessage(getContext(), R.string.msg_move_near_weapon);
                } else if (ingredients.size() == 0 && tutorial == TUTORIAL_ASTEROID) {
                    ingredients.makeNew();
                    messages.clear();
                    messages.drawMessage(getContext(), R.string.msg_destroy_asteroid);
                } else if (boxes.size() == 0 && tutorial == TUTORIAL_REPLENISH) {
                    BoxData box = new BoxData(boxBitmap, box2 -> {
                       ammo = weapon.capacity;
                       messages.clear();
                       messages.drawMessage(getContext(), R.string.msg_come_back_anytime);
                       tutorial = TUTORIAL_NONE;
                       ingredients.setMakeIngredients(true);
                        new Handler(Looper.getMainLooper()).post(() -> listener.onTutorialFinish());
                    });

                    box.x = 0.5f;
                    box.yDiff = 2;
                    boxes.add(box);
                    messages.clear();
                    messages.drawMessage(getContext(), R.string.msg_move_near_ammunition);
                }
                //FIN DEL TUTORIAL------------------------------------------------------------------

                //Dibuja el fondo-------------------------------------------------------------------
                background.draw(canvas, speed);

                //Evento: paso un asteroide---------------------------------------------------------
                if (ingredients.draw(canvas, speed)) {
                    new Handler(Looper.getMainLooper()).post(() -> listener.onIngredientPassed());
                    //Aumenta el contador de ingredientes que pasaron
                    ingredientsPassedCount++;
                    //Si el listener esta activo y pasaron 20 ingredientes crea una caja de municion
                    if (listener != null && ingredientsPassedCount % 20 == 0) {
                        CreateAmmoBox();
                    }
                }
                //Maneja las cajas------------------------------------------------------------------
                for (BoxData box : new ArrayList<>(boxes)) {
                    //Velocidad de caida de la caja
                    int boxSpeed = 2;
                    Matrix matrix = box.next(boxSpeed, canvas.getWidth(), canvas.getHeight());
                    if (matrix != null)
                        canvas.drawBitmap(box.boxBitmap, matrix, paint);
                    else boxes.remove(box);
                }
                //Posicion de la nave---------------------------------------------------------------
                float left = canvas.getWidth() * shipPositionX;
                float top = canvas.getHeight() - (shipBitmap.getHeight() * shipPositionY);

                Matrix matrix = new Matrix();
                matrix.postTranslate(-shipBitmap.getWidth() / 2f, -shipBitmap.getHeight() / 2f);
                matrix.postRotate(shipRotation);
                matrix.postTranslate(left, top);
                canvas.drawBitmap(shipBitmap, matrix, paint);
                //Dibuja la nave en la nueva posicion-----------------------------------------------
                Rect position = new Rect(
                        (int) left - (shipBitmap.getWidth() / 2),
                        (int) top - (shipBitmap.getWidth() / 2),
                        (int) left + (shipBitmap.getWidth() / 2),
                        (int) top + (shipBitmap.getWidth() / 2)
                );
                //Bucle que dibuja los proyectiles--------------------------------------------------
                for (ProjectileData projectile : new ArrayList<>(projectiles)) {
                    //Atributos del proyectil
                    int projectileSpeed = 5; // velocidad del proyectil

                    // Posicion actual del proyectil
                    Rect rect = projectile.next(projectileSpeed, canvas.getWidth(), canvas.getHeight());

                    if (rect != null) {
                        //Comprueba la colision de un proyectil con un ingrediente
                        IngredientData ingredient = this.ingredients.ingredientAtPosition(rect);
                        // Si hay una colision
                        if (isPlaying && ingredient != null) {

                            //Destruye el proyectil y el enemigo
                            projectiles.remove(projectile);
                            this.ingredients.destroy(ingredient);

                            // Si el tutorial esta en la parte de asteroides avanza al siguiente punto
                            if (tutorial == TUTORIAL_ASTEROID) {
                                tutorial++;
                            }

                            //Aumenta la velocidad del juego
                            speed += 0.02;

                            // Maneja los del frame actual
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (listener != null) {
                                    //Aumenta el score
                                    listener.onIngredientHit(++score);
                                }
                                // Maneja las mejoras de armas
                                int scoreRequirement = 20; //cantidad de enemigos derrotados para un cambio de arma
                                if (score % scoreRequirement == 0 && (score / scoreRequirement) < (WeaponData.WEAPONS.length - 1)) {
                                    // Determina el arma que se debe entregar
                                    final WeaponData weapon = WeaponData.WEAPONS[score / scoreRequirement];
                                    // Crea la caja con el arma
                                    CreateWeaponBox(weapon);
                                }
                                //Crea una caja de municion si se destruyeron 10 enemigos
                                if (score % 10 == 0) {
                                    CreateAmmoBox();
                                }
                            });
                        }
                        //Dibuja el proyectil
                        canvas.drawRect(rect, paint);
                    } else {
                        // Si el proyectil no existe destruye el valor
                        projectiles.remove(projectile);
                    }
                }

                // TUTORIAL 2DA PARTE --------------------------------------------------------------
                if (isPlaying) {
                    if (tutorial == TUTORIAL_NONE || tutorial > TUTORIAL_UPGRADE) {
                        accentPaint.setAlpha(100);
                        canvas.drawRect(0, (float) canvas.getHeight() - 5, (float) canvas.getWidth(), (float) canvas.getHeight(), accentPaint);
                        accentPaint.setAlpha(255);
                        canvas.drawRect(0, (float) canvas.getHeight() - 5, canvas.getWidth() * (ammo / weapon.capacity), (float) canvas.getHeight(), accentPaint);
                    }
                    IngredientData ingredient = ingredients.ingredientAtPosition(position);
                    if (ingredient != null) {
                        if (tutorial > TUTORIAL_NONE) {
                            messages.clear();
                            messages.drawMessage(getContext(), R.string.msg_dont_get_hit);
                            ingredients.destroy(ingredient);
                        } else {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (listener != null) {
                                    listener.onIngredientCrashed();
                                    listener.onStop(score);
                                }
                                stop();
                            });
                        }
                    }
                    for (final BoxData box : new ArrayList<>(boxes)) {
                        if (box.position != null && position.intersect(box.position)) {
                            box.open();
                            boxes.remove(box);
                        }
                    }
                }
                // FIN TUTORIAL---------------------------------------------------------------------


                //Dibuja en el canvas---------------------------------------------------------------
                messages.draw(canvas, speed);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    // Crea una nueva caja de mejora de arma
    private void CreateWeaponBox(WeaponData weapon) {
        boxes.add(new BoxData(weapon.getBitmap(getContext()), box -> {
            GameView.this.weapon = weapon;
            if (weapon.capacity < ammo) {
                ammo = weapon.capacity;
            }
            //Muestra el mensaje del arma equipada
            messages.drawMessage(String.format(getContext().getString(R.string.msg_weapon_equipped), weapon.getName(getContext())));
            // Listener de upgrade de arma
            new Handler(Looper.getMainLooper()).post(() -> {
                if (listener != null)
                    listener.onWeaponUpgraded(weapon);
            });
        }));
    }

    //Crea una caja de municion---------------------------------------------------------------------
    private void CreateAmmoBox() {
        boxes.add(new BoxData(boxBitmap, box -> new Handler(Looper.getMainLooper()).post(() -> {
            ammoAnimator = ValueAnimator.ofFloat(ammo, Math.min(ammo + 20, weapon.capacity));
            ammoAnimator.setDuration(250);
            ammoAnimator.setInterpolator(new DecelerateInterpolator());
            ammoAnimator.addUpdateListener(valueAnimator -> ammo = (float) valueAnimator.getAnimatedValue());
            ammoAnimator.start();
            //callback para eventos al conseguir municion
            if (listener != null) {
                listener.onAmmoReplenished();
            }
        })));
    }

    //Comienza el juego ----------------------------------------------------------------------------
    public void play(boolean isTutorial) {
        if (isTutorial) {
            // Tutorial de movimiento
            if (tutorial == TUTORIAL_NONE) {
                tutorial = TUTORIAL_MOVE;
                messages.clear();
                messages.drawMessage(getContext(), R.string.msg_press_to_move);
            }
        } else tutorial = TUTORIAL_NONE;
        // Reset de valores
        isPlaying = true;
        score = 0;
        speed = 1;
        ammo = 15;
        ingredientsPassedCount = 0;
        shipPositionX = 0.5f;
        shipRotation = 0;
        ingredients.setMakeIngredients(!isTutorial);
        projectiles.clear();
        boxes.clear();
        // Reset de animator
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
        //Animacion de aparecer la nave
        ValueAnimator animator = ValueAnimator.ofFloat(shipPositionY, 1);
        animator.setDuration(150);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(valueAnimator -> shipPositionY = (float) valueAnimator.getAnimatedValue());
        animator.start();
        //Si no es el tutorial te entrega un arma al empezar
        if (!isTutorial) {
            weapon = WeaponData.WEAPONS[0];
        }
        //Setea el listener para el movimiento
        setOnTouchListener(this);
        // Evento de tutorial
        if (listener != null) {
            listener.onStart(isTutorial);
        }
    }

    // Game Over - resea ---------------------------------------------------------------------------
    public void stop() {
        isPlaying = false;
        setOnTouchListener(null);
        ingredients.setMakeIngredients(false);
        // Para el animador
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
        // Revienta la nave
        ValueAnimator animator = ValueAnimator.ofFloat(shipPositionY, -1f);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            shipPositionY = (float) valueAnimator.getAnimatedValue();
            shipRotation = 720 * valueAnimator.getAnimatedFraction();
        });

        if (tutorial > TUTORIAL_NONE) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    messages.clear();
                    messages.drawMessage(getContext(), R.string.msg_dont_get_hit);
                    play(true);
                }
            });
        }
        animator.start();

        ValueAnimator animator1 = ValueAnimator.ofFloat(speed, 1);
        animator1.setDuration(150);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.addUpdateListener(valueAnimator -> speed = (float) valueAnimator.getAnimatedValue());
        animator1.start();
    }

    //Determina si el juego esta en marcha
    public boolean isPlaying() {
        return isPlaying;
    }


    // Verifica si el tutorial esta siendo jugado
    public boolean isTutorial() {
        return tutorial > TUTORIAL_NONE;
    }
    // Pausa
    public void onPause() {
        if (thread == null)
            return;

        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException ignored) {
        }
        thread = null;
    }
    // Resumen de la aplicacion
    public void onResume() {
        if (thread != null)
            onPause();

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }
    // Touch de movimiento ------------------------------------------------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // Mientras se este tocando la pantalla
            {
                // Detiene cualquier animacion de la nave
                if (animator != null && animator.isStarted()) {
                    animator.cancel();
                }
                //Detecta el lado del touch o si el boton fue precionado
               if (event.getX() > getWidth() / 2f ) {
                    // Si todavia hay pantalla a la derecha
                    if (shipPositionX < 1) {
                        // Mueve a la derecha
                        animator = ValueAnimator.ofFloat(shipPositionX, shipPositionX + 1);
                    }
                    else{
                        // No se mueve
                        return false;
                    }
                    // Si todavia hay pantalla a la izquierda
                } else if (shipPositionX > 0) {
                   // Mueve a la izquierda
                    animator = ValueAnimator.ofFloat(shipPositionX, shipPositionX - 1);
                }

                // Anima el movimiento lateral de la nave
                animator.setDuration((long) (1000 / speed));
                animator.setStartDelay(50);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.addUpdateListener(valueAnimator -> {
                    float newX = (float) valueAnimator.getAnimatedValue();
                    if (newX <= 0)
                        shipPositionX = 0;
                    else if (newX >= 1)
                        shipPositionX = 1;
                    else shipPositionX = newX;
                });

                animator.start();
                shipPositionStartX = shipPositionX;
                break;
        }
            case MotionEvent.ACTION_UP: { // Al levantar el toque de pantalla
                // Cancela cualquier animacion
                if (animator != null && animator.isStarted()) {
                    animator.cancel();
                }
                // Tutorial de movimiento
                if (tutorial == TUTORIAL_MOVE) {
                    if (System.currentTimeMillis() - projectileTime > 500)
                        tutorial++;
                    else {
                        messages.clear();
                        messages.drawMessage(getContext(), R.string.msg_hold_distance);
                    }
                }

                float newX = shipPositionX + ((shipPositionX - shipPositionStartX) / 1.5f);
                if (newX <= 0)
                    newX = 0;
                else if (newX >= 1)
                    newX = 1;
                //Anima la desaceleracion de la nave
                animator = ValueAnimator.ofFloat(shipPositionX, newX);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.setDuration((long) (500 / speed));
                animator.addUpdateListener(valueAnimator -> {
                    float newX1 = (float) valueAnimator.getAnimatedValue();
                    if (newX1 <= 0)
                        shipPositionX = 0;
                    else if (newX1 >= 1)
                        shipPositionX = 1;
                    else shipPositionX = newX1;
                });

                animator.start();
                break;
            }
        }
        return true;
    }

    //Interface para vincular los eventos con el GameActivity---------------------------------------
    public interface GameListener {
        void onStart(boolean isTutorial);

        void onTutorialFinish();

        void onStop(int score);

        void onIngredientPassed();

        void onIngredientCrashed();

        void onWeaponUpgraded(WeaponData weapon);

        void onAmmoReplenished();

        void onProjectileFired(WeaponData weapon);

        void onOutOfAmmo();

        void onIngredientHit(int score);

    }

    //Dispara proyectiles --------------------------------------------------------------------------
    public void FireProyectils(){
        // Si no esta en el tutorial
        if ((tutorial == TUTORIAL_NONE || tutorial > TUTORIAL_UPGRADE)) {
            // Para la animacion de la municion
            if (ammoAnimator != null && ammoAnimator.isStarted()) {
                ammoAnimator.end();
            }
            // Si hay municion dispara
            if (ammo > 0) {
                // Disparo
                weapon.fire(projectiles, shipPositionX, shipBitmap.getHeight() * shipPositionY * 1.5f);
                // Evento de disparo
                if (listener != null) {
                    listener.onProjectileFired(weapon);
                }
                //Actualiza la municion
                ammoAnimator = ValueAnimator.ofFloat(ammo, ammo - 1);
                ammoAnimator.setDuration(250);
                ammoAnimator.setInterpolator(new DecelerateInterpolator());
                ammoAnimator.addUpdateListener(valueAnimator -> ammo = (float) valueAnimator.getAnimatedValue());
                ammoAnimator.start();

            } else if (tutorial > TUTORIAL_NONE && boxes.size() == 0) {
                // Si esta en el tutorial y esta sin municion
                messages.clear();
                messages.drawMessage(getContext(), R.string.msg_too_many_projectiles);
                messages.drawMessage(getContext(), R.string.msg_free_refill);
                //Crea una caja de municion
                CreateAmmoBox();
            } else if (listener != null) {
                messages.drawMessage(getContext(), R.string.msg_out_of_ammo);
                listener.onOutOfAmmo();
            }

        } else {
            projectileTime = System.currentTimeMillis();
        }
    }


}
