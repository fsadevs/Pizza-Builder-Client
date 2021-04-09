package com.fsadev.pizzabuilder.game.asteroid.views;

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
import com.fsadev.pizzabuilder.game.asteroid.data.AsteroidData;
import com.fsadev.pizzabuilder.game.asteroid.data.BoxData;
import com.fsadev.pizzabuilder.game.asteroid.data.ProjectileData;
import com.fsadev.pizzabuilder.game.asteroid.data.WeaponData;
import com.fsadev.pizzabuilder.game.asteroid.data.drawer.AsteroidDrawer;
import com.fsadev.pizzabuilder.game.asteroid.data.drawer.BackgroundDrawer;
import com.fsadev.pizzabuilder.game.asteroid.data.drawer.MessageDrawer;
import com.fsadev.pizzabuilder.game.asteroid.utils.FontUtils;
import com.fsadev.pizzabuilder.game.asteroid.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;



public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {

    private final Paint paint;
    private final Paint accentPaint;
    private final SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Thread thread;

    private final Bitmap shipBitmap;
    private float shipPositionX = 0.5f;
    private float shipPositionY = -1f;
    private float shipPositionStartX;
    private float shipRotation;

    private final Bitmap boxBitmap;
    private final List<BoxData> boxes;

    private WeaponData weapon;
    private final List<ProjectileData> projectiles;
    private long projectileTime;

    private final BackgroundDrawer background;
    private final AsteroidDrawer asteroids;
    private final MessageDrawer messages;

    private ValueAnimator animator;
    private GameListener listener;
    private boolean isPlaying;
    public int score;
    private float speed = 1;
    private float ammo;
    private ValueAnimator ammoAnimator;

    private int tutorial;

    private static final int TUTORIAL_NONE = 0;
    private static final int TUTORIAL_MOVE = 1;
    private static final int TUTORIAL_UPGRADE = 2;
    private static final int TUTORIAL_ASTEROID = 3;
    private static final int TUTORIAL_REPLENISH = 4;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    //Constructor
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolder = getHolder();
        //Colores
        int colorPrimaryLight = ContextCompat.getColor(getContext(), R.color.colorPrimaryLight);
        int colorPrimary = ContextCompat.getColor(getContext(), R.color.naranja_base);
        int colorAccent = ContextCompat.getColor(getContext(), R.color.colorAccent);
        int cloudColor = ContextCompat.getColor(getContext(), R.color.cloud);
        //--
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        accentPaint = new Paint();
        accentPaint.setColor(colorPrimaryLight);
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
        //Inicializa el fondo
        background = new BackgroundDrawer(paint, cloudPaint);
        //Inicializa los asteroides
        asteroids = new AsteroidDrawer(getContext(), colorAccent, colorPrimary, paint, accentPaint);
        //Inicialia los mensajes
        messages = new MessageDrawer(textPaint);
        //Inicializa los proyectiles
        projectiles = new ArrayList<>();

        //Nave
        shipBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.logo_small);
        //Caja de municiones
        boxBitmap = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(getContext(), R.drawable.ic_box), colorAccent, colorPrimary);
        boxes = new ArrayList<>();
    }
    //Setea el listener
    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null)
                    return;

                canvas.drawColor(Color.BLACK);

                // ----- tutorial nonsense ------

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
                } else if (asteroids.size() == 0 && tutorial == TUTORIAL_ASTEROID) {
                    asteroids.makeNew();
                    messages.clear();
                    messages.drawMessage(getContext(), R.string.msg_destroy_asteroid);
                } else if (boxes.size() == 0 && tutorial == TUTORIAL_REPLENISH) {
                    BoxData box = new BoxData(boxBitmap, box2 -> {
                       ammo = weapon.capacity;
                       messages.clear();
                       messages.drawMessage(getContext(), R.string.msg_come_back_anytime);
                       tutorial = TUTORIAL_NONE;
                       asteroids.setMakeAsteroids(true);
                        new Handler(Looper.getMainLooper()).post(() -> listener.onTutorialFinish());
                    });

                    box.x = 0.5f;
                    box.yDiff = 2;
                    boxes.add(box);
                    messages.clear();
                    messages.drawMessage(getContext(), R.string.msg_move_near_ammunition);
                }

                // ----- end tutorial nonsense --------

                //Dibuja el fondo
                background.draw(canvas, speed);

                if (asteroids.draw(canvas, speed)) {
                    new Handler(Looper.getMainLooper()).post(() -> listener.onAsteroidPassed());
                }
                //Maneja las cajas
                for (BoxData box : new ArrayList<>(boxes)) {
                    Matrix matrix = box.next(speed, canvas.getWidth(), canvas.getHeight());
                    if (matrix != null)
                        canvas.drawBitmap(box.boxBitmap, matrix, paint);
                    else boxes.remove(box);
                }
                //Posicion de la nave
                float left = canvas.getWidth() * shipPositionX;
                float top = canvas.getHeight() - (shipBitmap.getHeight() * shipPositionY);

                Matrix matrix = new Matrix();
                matrix.postTranslate(-shipBitmap.getWidth() / 2f, -shipBitmap.getHeight() / 2f);
                matrix.postRotate(shipRotation);
                matrix.postTranslate(left, top);
                canvas.drawBitmap(shipBitmap, matrix, paint);

                Rect position = new Rect(
                        (int) left - (shipBitmap.getWidth() / 2),
                        (int) top - (shipBitmap.getWidth() / 2),
                        (int) left + (shipBitmap.getWidth() / 2),
                        (int) top + (shipBitmap.getWidth() / 2)
                );
                //Crea los proyectiles
                for (ProjectileData projectile : new ArrayList<>(projectiles)) {
                    Rect rect = projectile.next(speed*3, canvas.getWidth(), canvas.getHeight());
                    if (rect != null) {
                        AsteroidData asteroid = asteroids.asteroidAt(rect);
                        //Comprueba la colision
                        if (isPlaying && asteroid != null) {
                            //Destruye el proyectil y el asteroide
                            projectiles.remove(projectile);
                            asteroids.destroy(asteroid);

                            if (tutorial == TUTORIAL_ASTEROID) // more tutorial nonsense
                                tutorial++;
                            //Aumenta la velocidad
                            speed += 0.02;
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (listener != null)
                                    listener.onAsteroidHit(++score);
                                //Cambio de armas
                                int scoreRequirement = 20; //cantidad de enemigos derrotados para un cambio de arma
                                if (score % scoreRequirement == 0 && (score / scoreRequirement) < (WeaponData.WEAPONS.length - 1)) {
                                    final WeaponData weapon = WeaponData.WEAPONS[score / scoreRequirement];
                                    boxes.add(new BoxData(weapon.getBitmap(getContext()), box -> {
                                        GameView.this.weapon = weapon;
                                        if (weapon.capacity < ammo)
                                            ammo = weapon.capacity;
                                        //Muestra el mensaje del arma equipada
                                        messages.drawMessage(String.format(getContext().getString(R.string.msg_weapon_equipped), weapon.getName(getContext())));
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            if (listener != null)
                                                listener.onWeaponUpgraded(weapon);
                                        });
                                    }));
                                }
                                //Caja de municion
                                if (score % 10 == 0) {
                                    boxes.add(new BoxData(boxBitmap, box -> new Handler(Looper.getMainLooper()).post(() -> {
                                        ammoAnimator = ValueAnimator.ofFloat(ammo, Math.min(ammo + 20, weapon.capacity));
                                        ammoAnimator.setDuration(250);
                                        ammoAnimator.setInterpolator(new DecelerateInterpolator());
                                        ammoAnimator.addUpdateListener(valueAnimator -> ammo = (float) valueAnimator.getAnimatedValue());
                                        ammoAnimator.start();
                                        //callback para eventos al conseguir municion
                                        if (listener != null)
                                            listener.onAmmoReplenished();
                                    })));
                                }
                            });
                        }

                        canvas.drawRect(rect, paint);
                    } else projectiles.remove(projectile);
                }

                if (isPlaying) {
                    if (tutorial == TUTORIAL_NONE || tutorial > TUTORIAL_UPGRADE) { // kinda tutorial nonsense but don't worry about it
                        accentPaint.setAlpha(100);
                        canvas.drawRect(0, (float) canvas.getHeight() - 5, (float) canvas.getWidth(), (float) canvas.getHeight(), accentPaint);
                        accentPaint.setAlpha(255);
                        canvas.drawRect(0, (float) canvas.getHeight() - 5, canvas.getWidth() * (ammo / weapon.capacity), (float) canvas.getHeight(), accentPaint);
                    }

                    AsteroidData asteroid = asteroids.asteroidAt(position);
                    if (asteroid != null) {
                        if (tutorial > TUTORIAL_NONE) {
                            messages.clear();
                            messages.drawMessage(getContext(), R.string.msg_dont_get_hit);
                            asteroids.destroy(asteroid);
                        } else {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (listener != null) {
                                    listener.onAsteroidCrashed();
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

                messages.draw(canvas, speed);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * Start a new game! Wheeeeeeeeee!
     *
     * @param isTutorial        Should this game start with a tutorial?
     */
    public void play(boolean isTutorial) {
        if (isTutorial) { // tutorial. nonsense.
            if (tutorial == TUTORIAL_NONE) {
                tutorial = TUTORIAL_MOVE;
                messages.clear();
                messages.drawMessage(getContext(), R.string.msg_press_to_move);
            }
        } else tutorial = TUTORIAL_NONE;

        isPlaying = true;
        score = 0;
        speed = 1;
        ammo = 15;
        shipPositionX = 0.5f;
        shipRotation = 0;
        asteroids.setMakeAsteroids(!isTutorial);
        projectiles.clear();
        boxes.clear();

        if (animator != null && animator.isStarted())
            animator.cancel();

        ValueAnimator animator = ValueAnimator.ofFloat(shipPositionY, 1);
        animator.setDuration(150);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(valueAnimator -> shipPositionY = (float) valueAnimator.getAnimatedValue());
        animator.start();

        if (!isTutorial) // TUTORIAL NONSENSE!!!!!
            weapon = WeaponData.WEAPONS[0];
        setOnTouchListener(this);

        if (listener != null)
            listener.onStart(isTutorial);
    }

    /**
     * Stop the game, reset everything.
     */
    public void stop() {
        isPlaying = false;
        setOnTouchListener(null);
        asteroids.setMakeAsteroids(false);

        if (animator != null && animator.isStarted())
            animator.cancel();

        ValueAnimator animator = ValueAnimator.ofFloat(shipPositionY, -1f);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            shipPositionY = (float) valueAnimator.getAnimatedValue();
            shipRotation = 720 * valueAnimator.getAnimatedFraction();
        });
        if (tutorial > TUTORIAL_NONE) { // probably also tutorial nonsense
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

    /**
     * Determine if a game is being played.
     *
     * @return Whether a game is being played.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Determine whether the tutorial is currently
     * being played.
     *
     * @return Whether the tutorial is currently being played.
     */
    public boolean isTutorial() {
        return tutorial > TUTORIAL_NONE;
    }

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

    public void onResume() {
        if (thread != null)
            onPause();

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            {

                //Movimiento de la nave
                if (animator != null && animator.isStarted())
                    animator.cancel();
                //Detecta el lado del touch o si el boton fue precionado
               if (event.getX() > getWidth() / 2f ) {

                    if (shipPositionX < 1) {
                        animator = ValueAnimator.ofFloat(shipPositionX, shipPositionX + 1);
                    }
                    else{
                        return false;
                    }
                } else if (shipPositionX > 0) {
                    animator = ValueAnimator.ofFloat(shipPositionX, shipPositionX - 1);
                }

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
            case MotionEvent.ACTION_UP: {
                if (animator != null && animator.isStarted())
                    animator.cancel();

                if (tutorial == TUTORIAL_MOVE) { // tutorial nonsense! yay!
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

    //Interface
    public interface GameListener {
        void onStart(boolean isTutorial);

        void onTutorialFinish();

        void onStop(int score);

        void onAsteroidPassed();

        void onAsteroidCrashed();

        void onWeaponUpgraded(WeaponData weapon);

        void onAmmoReplenished();

        void onProjectileFired(WeaponData weapon);

        void onOutOfAmmo();

        void onAsteroidHit(int score);

    }

    //Dispara proyectiles
    public void FireProyectils(){
        //Disparo del proyectil
        if ((tutorial == TUTORIAL_NONE || tutorial > TUTORIAL_UPGRADE)) {
            if (ammoAnimator != null && ammoAnimator.isStarted())
                ammoAnimator.end();
            if (ammo > 0) {
                weapon.fire(projectiles, shipPositionX, shipBitmap.getHeight() * shipPositionY * 1.5f);
                if (listener != null)
                    listener.onProjectileFired(weapon);

                ammoAnimator = ValueAnimator.ofFloat(ammo, ammo - 1);
                ammoAnimator.setDuration(250);
                ammoAnimator.setInterpolator(new DecelerateInterpolator());
                ammoAnimator.addUpdateListener(valueAnimator -> ammo = (float) valueAnimator.getAnimatedValue());
                ammoAnimator.start();
            } else if (tutorial > TUTORIAL_NONE && boxes.size() == 0) { // definitely tutorial nonsense
                messages.clear();
                messages.drawMessage(getContext(), R.string.msg_too_many_projectiles);
                messages.drawMessage(getContext(), R.string.msg_free_refill);
                boxes.add(new BoxData(boxBitmap, box -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        ammoAnimator = ValueAnimator.ofFloat(ammo, weapon.capacity);
                        ammoAnimator.setDuration(250);
                        ammoAnimator.setInterpolator(new DecelerateInterpolator());
                        ammoAnimator.addUpdateListener(valueAnimator ->
                                ammo = (float) valueAnimator.getAnimatedValue());
                        ammoAnimator.start();

                        if (listener != null)
                            listener.onAmmoReplenished();
                    });
                }));
            } else if (listener != null) {
                messages.drawMessage(getContext(), R.string.msg_out_of_ammo);
                listener.onOutOfAmmo();
            }

        } else projectileTime = System.currentTimeMillis();
    }


    //Controla la nave
    public void ControlShip(MotionEvent event, int side){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            {
                //Movimiento de la nave
                if (animator != null && animator.isStarted())
                    animator.cancel();
                //Detecta el lado del touch o si el boton fue precionado
                if (side==1) {
                    if (shipPositionX < 1) {
                        animator = ValueAnimator.ofFloat(shipPositionX, shipPositionX + 1);
                    }

                } else if (shipPositionX > 0) {
                    animator = ValueAnimator.ofFloat(shipPositionX, shipPositionX - 1);
                }

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
            case MotionEvent.ACTION_UP: {
                if (animator != null && animator.isStarted())
                    animator.cancel();

                if (tutorial == TUTORIAL_MOVE) { // tutorial nonsense! yay!
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
    }


}