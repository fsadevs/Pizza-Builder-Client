package com.fsadev.pizzabuilder.game.pizzawars.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.game.pizzawars.data.WeaponData;
import com.fsadev.pizzabuilder.game.pizzawars.utils.AchievementUtils;
import com.fsadev.pizzabuilder.game.pizzawars.utils.FontUtils;
import com.fsadev.pizzabuilder.game.pizzawars.utils.ImageUtils;
import com.fsadev.pizzabuilder.game.pizzawars.utils.PreferenceUtils;
import com.fsadev.pizzabuilder.game.pizzawars.views.GameView;


public class GameActivity extends AppCompatActivity
        implements GameView.GameListener, View.OnClickListener {

    private TextView scoreView,highScoreView,hintView,weaponName;
    private ImageView musicView,weaponView,soundView,aboutView,gameTitle,pauseView,stopView;
    private LinearLayout buttonLayout;
    private GameView gameView;
    private ValueAnimator animator;
    private String hintStart;
    private SoundPool soundPool;
    private int explosionId, explosion2Id, buttonId,hissId,upgradeId,replenishId,errorId;
    private SharedPreferences prefs;
    private boolean isSound,isMusic,isPaused;
    private Bitmap soundEnabled,soundDisabled,musicDisabled,play,pause,musicEnabled;
    private MediaPlayer player;
    private ConstraintLayout controlsLayout;
    private AchievementUtils achievementUtils;
    private final Handler handler = new Handler();
    private final Runnable hintRunnable = new Runnable() {

        @Override
        public void run() {
            //Maneja las animaciones en los textos
            if (!gameView.isPlaying() && (animator == null || !animator.isStarted())) {
                if (!hintView.getText().toString().contains("."))
                    hintView.setText(String.format(".%s.", hintStart));
                else if (hintView.getText().toString().contains("..."))
                    hintView.setText(hintStart);
                else if (hintView.getText().toString().contains(".."))
                    hintView.setText(String.format("...%s...", hintStart));
                else if (hintView.getText().toString().contains("."))
                    hintView.setText(String.format("..%s..", hintStart));

                if (highScoreView.getVisibility() == View.VISIBLE){
                    highScoreView.setVisibility(View.GONE);}
                else {
                    highScoreView.setVisibility(View.VISIBLE);
                }
            }
            if (isPaused) {
                if (pauseView.getAlpha() == 1)
                    pauseView.setAlpha(0.5f);
                else pauseView.setAlpha(1f);
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pone la actividad en pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        // Preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Vistas
        weaponView = findViewById(R.id.game_weaponView);
        weaponName = findViewById(R.id.game_weaponName);
        gameTitle = findViewById(R.id.game_title);
        controlsLayout = findViewById(R.id.game_controlsLayout);
        scoreView = findViewById(R.id.title);
        highScoreView = findViewById(R.id.highScore);
        hintView = findViewById(R.id.hint);
        buttonLayout = findViewById(R.id.buttonLayout);
        musicView = findViewById(R.id.music);
        soundView = findViewById(R.id.sound);
        aboutView = findViewById(R.id.about);
        pauseView = findViewById(R.id.pause);
        stopView = findViewById(R.id.stop);
        gameView = findViewById(R.id.game);
        // Boton de disparo
        Button btnFire = findViewById(R.id.game_btnShot);
        btnFire.setOnClickListener(this::Fire);

        // Sonidos
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .build();

        // Carga los sonidos
        explosionId = soundPool.load(this, R.raw.explosion, 1);
        explosion2Id = soundPool.load(this, R.raw.explosion_two, 1);
        buttonId = soundPool.load(this, R.raw.button, 1);
        hissId = soundPool.load(this, R.raw.hiss, 1);
        replenishId = soundPool.load(this, R.raw.replenish, 1);
        upgradeId = soundPool.load(this, R.raw.upgrade, 1);
        errorId = soundPool.load(this, R.raw.error, 1);
        WeaponData.loadSounds(this, soundPool);

        // Texto
        Typeface typeface = FontUtils.getTypeface(this);
        int colorPrimary = ContextCompat.getColor(this, R.color.naranja_base);
        int colorAccent = ContextCompat.getColor(this, R.color.colorAccent);
        // Seteo de fuente del score
        scoreView.setTypeface(typeface);
        scoreView.getPaint().setShader(new LinearGradient(
                0, 0, 0,
                scoreView.getLineHeight(),
                colorAccent,
                colorPrimary,
                Shader.TileMode.REPEAT
        ));

        // Seteo de fuente del puntaje maximo
        highScoreView.setTypeface(typeface);
        highScoreView.getPaint().setShader(new LinearGradient(
                0, 0, 0,
                hintView.getLineHeight(),
                colorAccent,
                colorPrimary,
                Shader.TileMode.REPEAT
        ));
        // Seteo de fuente del texto de ayuda contextual
        hintView.setTypeface(typeface);
        hintView.getPaint().setShader(new LinearGradient(
                0, 0, 0,
                hintView.getLineHeight(),
                colorAccent,
                colorPrimary,
                Shader.TileMode.REPEAT
        ));
        // Texto de ayuda inicial
        hintStart = getString(R.string.hint_start);

        // Boton de silenciar la musica
        isMusic = prefs.getBoolean(PreferenceUtils.PREF_MUSIC, true);
        // Musica activada
        musicEnabled = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_music_enabled), colorAccent, colorPrimary);
        // Musica desactivada
        musicDisabled = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_music_disabled), colorAccent, colorPrimary);
        musicView.setImageBitmap(isMusic ? musicEnabled : musicDisabled);
        // clickListener
        musicView.setOnClickListener(view -> {
            isMusic = !isMusic;
            prefs.edit().putBoolean(PreferenceUtils.PREF_MUSIC, isMusic).apply();
            musicView.setImageBitmap(isMusic ? musicEnabled : musicDisabled);
            //Musica
            if (isMusic){
                player.start();
            }
            else{
                player.pause();
            }
            if (isSound)
                soundPool.play(buttonId, 1, 1, 0, 0, 1);
        });
        // Boton de sonidos
        isSound = prefs.getBoolean(PreferenceUtils.PREF_SOUND, true);
        // Sonido activado
        soundEnabled = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_sound_enabled), colorAccent, colorPrimary);
        // Sonido desactivado
        soundDisabled = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_sound_disabled), colorAccent, colorPrimary);
        soundView.setImageBitmap(isSound ? soundEnabled : soundDisabled);
        // Click listener
        soundView.setOnClickListener(view -> {
            isSound = !isSound;
            prefs.edit().putBoolean(PreferenceUtils.PREF_SOUND, isSound).apply();
            soundView.setImageBitmap(isSound ? soundEnabled : soundDisabled);
            if (isSound)
                soundPool.play(buttonId, 1, 1, 0, 0, 1);
        });

        // Boton de tutorial
        aboutView.setImageBitmap(ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_info), colorAccent, colorPrimary));
        aboutView.setOnClickListener(view -> {
            // Inicia el tutorial
            if (!gameView.isPlaying() && (animator == null || !animator.isStarted())) {
                gameView.setOnClickListener(null);
                gameView.play(true); //Inicia una nueva partida de tutorial
                // Control de visibilidad de la pantalla de inicio
                animateTitle(false);
                if (isSound) {
                    soundPool.play(hissId, 1, 1, 0, 0, 1);
                }
            }
        });

        //Botones de estado
        play = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_play), colorAccent, colorPrimary);
        pause = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_pause), colorAccent, colorPrimary);
        Bitmap stop = ImageUtils.gradientBitmap(ImageUtils.getVectorBitmap(this, R.drawable.ic_stop), colorAccent, colorPrimary);
        // Inicia con el boton como pausa
        pauseView.setImageBitmap(pause);
        // clickListener del boton
        pauseView.setOnClickListener(view -> {
            isPaused = !isPaused;
            //Pausa
            if (isPaused) {
                pauseView.setImageBitmap(play);
                if (!gameView.isTutorial())
                    stopView.setVisibility(View.VISIBLE);
                gameView.onPause(); // pausa el juego
            } else {
                // Reanuda el juego
                pauseView.setImageBitmap(pause);
                pauseView.setAlpha(1f);
                stopView.setVisibility(View.GONE);
                gameView.onResume();
            }
        });
        // Stop
        stopView.setImageBitmap(stop);
        stopView.setOnClickListener(view -> {
            if (isPaused) {
                pauseView.setImageBitmap(pause);
                pauseView.setAlpha(1f);
                gameView.onResume();
                isPaused = false;
            }
            // Termina el juego
            onStop(gameView.score);
            gameView.stop();
        });
        //Recibe el puntaje maximo guardado en Shared Preference
        int highScore = prefs.getInt(PreferenceUtils.PREF_HIGH_SCORE, 0);
        if (highScore > 0) {
            // Muestra el puntaje maximo
            highScoreView.setText(String.format(getString(R.string.score_high), highScore));
        }
        // Reproductor
        player = MediaPlayer.create(this, R.raw.music);
        player.setLooping(true);
        if (isMusic) {
            player.start();
        }
        // Maneja el runnable que hace parpadear los botones
        handler.postDelayed(hintRunnable, 1000);
        //Setea el listener de eventos
        gameView.setListener(this);
        gameView.setOnClickListener(this);
        // Inicializa la clase de logros
        achievementUtils = new AchievementUtils(this);
        // Maneja la pantalla de inicio
        animateTitle(true);

    }
    //----------------------------------------------------------------------------------------------

    // Click en el boton de disparar
    private void Fire(View view) {
        gameView.FireProyectils();
    }


    //Maneja la pantalla de titulo, si el juego comienza la oculta y si termina la muestra
    private void animateTitle(final boolean isVisible) {
        // Oculta el score
        highScoreView.setVisibility(View.GONE);
        //Oculta o muestra el titulo del juego
        if (isVisible){
            gameTitle.setVisibility(View.VISIBLE);
        }else{
            gameTitle.setVisibility(View.GONE);
        }
        // Anima el texto de ayuda
        animator = ValueAnimator.ofFloat(isVisible ? 0 : 1, isVisible ? 1 : 0);
        animator.setDuration(750);
        animator.setStartDelay(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            scoreView.setText(""); // Limpia la vista de score
            hintView.setText(hintStart.substring(0, (int) ((float) valueAnimator.getAnimatedValue() * hintStart.length())));
        });
        animator.start();
        // Oculta o muestra los botones de musica, sonido y tutorial
        if (isVisible) {
            buttonLayout.setVisibility(View.VISIBLE);
            pauseView.setVisibility(View.GONE);
            stopView.setVisibility(View.GONE);
            // Verifica si se completo el tutorial
            if (prefs.getBoolean(PreferenceUtils.PREF_TUTORIAL, true)) {
                aboutView.setVisibility(View.GONE);
            }
            else {
                aboutView.setVisibility(View.VISIBLE);
            }
        }else {
            buttonLayout.setVisibility(View.GONE);
            pauseView.setVisibility(View.VISIBLE);
            stopView.setVisibility(View.GONE);
        }
        // Oculta o muestra el UI del arma
        if (isVisible){
            controlsLayout.setVisibility(View.GONE);
        }else{
            controlsLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPause() {
        // Juego en pausa
        if (isMusic)
            player.pause();
        if (gameView != null && !isPaused)
            gameView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        // Vuelve a poner tod0 en orden
        super.onResume();
        if (isMusic)
            player.start();
        if (gameView != null && !isPaused)
            gameView.onResume();
        // Verifica si el sistema cambio los valores de animacion desde el panel de programador
        if (Settings.Global.getFloat(getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1) != 1) {
            try {
                // Intenta setear los vlaores por defecto
                ValueAnimator.class.getMethod("setDurationScale", float.class).invoke(null, 1f);
            } catch (Throwable t) {
                // Crea una alerta pidiendo al jugador volver a configurar las animaciones manualmente
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_animation_speed)
                        .setMessage(R.string.desc_animation_speed)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            try {
                                startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                            } catch (Exception ignored) {
                            }
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton(android.R.string.cancel, (dialogInterface, i) ->
                                dialogInterface.dismiss())
                        .create()
                        .show();
            }

        }
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onStart(boolean isTutorial) {
        if (achievementUtils != null) {
            achievementUtils.onStart(isTutorial);
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onTutorialFinish() {
        // Cuando se completa el tutorial manda un callback al listener de logros y guarda en prefs
        if (achievementUtils != null) {
            achievementUtils.onTutorialFinish();
        }
        prefs.edit().putBoolean(PreferenceUtils.PREF_TUTORIAL, false).apply();
    }
    // GAME OVER o click en BOTON STOP--------------------------------------------------------------
    @Override
    public void onStop(int score) {

        // Muestra el titulo
        animateTitle(true);
        gameView.setOnClickListener(this);
        //Guarda el puntaje maximo
        int highScore = prefs.getInt(PreferenceUtils.PREF_HIGH_SCORE, 0);
        if (score > highScore) {
            // Si se supero el puntaje maximo personal
            highScore = score;
            // Guarda el puntaje maximo en SharedPref
            prefs.edit().putInt(PreferenceUtils.PREF_HIGH_SCORE, score).apply();

        }
        //Muestra el puntaje maximo
        highScoreView.setText(String.format(getString(R.string.score_high), highScore));
        //Detiene el listener
        if (achievementUtils != null) {
            achievementUtils.onStop(score);
        }
        //Oculta los controles
        controlsLayout.setVisibility(View.GONE);
        //Reseatea el panel del arma
        weaponName.setText(getString(R.string.weapon_pellet));
        weaponView.setImageResource(R.drawable.ic_weapon_pellet);

    }
    // Cuando un ingrediente pasa fuera de la pantalla
    @Override
    public void onIngredientPassed() {
        if (achievementUtils != null)
            achievementUtils.onIngredientPassed();
    }
    // Cuando un ingrediente choca con la nave
    @Override
    public void onIngredientCrashed() {
        if (isSound)
            soundPool.play(explosion2Id, 1, 1, 0, 0, 1);
        if (achievementUtils != null)
            achievementUtils.onIngredientCrashed();
    }
    // Cuando se consigue una mejora de arma
    @Override
    public void onWeaponUpgraded(WeaponData weapon) {
        if (isSound)
            soundPool.play(upgradeId, 1, 1, 0, 0, 1);
        if (achievementUtils != null)
            achievementUtils.onWeaponUpgraded(weapon);
        // Cambia el icono del arma en el hud
        weaponView.setImageResource(weapon.getDrawableRes());
        // Cambia el nombre del arma en el hud
        weaponName.setText(weapon.getName(this));
    }
    // Cuando se consigue una recarga de municion
    @Override
    public void onAmmoReplenished() {
        if (isSound)
            soundPool.play(replenishId, 1, 1, 0, 0, 1);
        if (achievementUtils != null)
            achievementUtils.onAmmoReplenished();
    }
    // Cuando se dispara un proyectil
    @Override
    public void onProjectileFired(WeaponData weapon) {
        if (isSound)
            soundPool.play(weapon.soundId, 1, 1, 0, 0, 1);
        if (achievementUtils != null)
            achievementUtils.onProjectileFired(weapon);
    }
    // Cuando se queda sin municion
    @Override
    public void onOutOfAmmo() {
        if (isSound)
            soundPool.play(errorId, 1, 1, 0, 0, 1);
        if (achievementUtils != null)
            achievementUtils.onOutOfAmmo();
    }
    // Cuando un proyectil impacta con un ingrediente
    @Override
    public void onIngredientHit(int score) {
        scoreView.setText(String.valueOf(score));
        if (isSound)
            soundPool.play(explosionId, 1, 1, 0, 0, 1);
        if (achievementUtils != null)
            achievementUtils.onIngredientHit(score);
    }

    // Click para iniciar el juego------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (!gameView.isPlaying() && (animator == null || !animator.isStarted())) {
            gameView.setOnClickListener(null);

            gameView.play(prefs.getBoolean(PreferenceUtils.PREF_TUTORIAL, true));

            animateTitle(false);
            if (isSound) {
                soundPool.play(hissId, 1, 1, 0, 0, 1);
            }

        }
    }


}
