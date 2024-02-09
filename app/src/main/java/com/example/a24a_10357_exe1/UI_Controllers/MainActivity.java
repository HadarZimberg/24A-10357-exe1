package com.example.a24a_10357_exe1.UI_Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a24a_10357_exe1.Logic.DataManager;
import com.example.a24a_10357_exe1.Logic.GameManager;
import com.example.a24a_10357_exe1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final long DELAY = 1000;
    private ShapeableImageView main_IMG_background;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[] main_IMG_chickens;
    private ShapeableImageView[][] main_IMG_chicken_matrix;
    private ShapeableImageView[] main_IMG_bearrys;
    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private Timer timer;
    private long startTime;
    private boolean timerOn = false;
    private Random random = new Random();
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        main_IMG_background.setImageResource(R.drawable.clouds);
        gameBeginning();
        gameManager = new GameManager(main_IMG_hearts.length);
        main_BTN_left.setOnClickListener(view -> moveToLeft());
        main_BTN_right.setOnClickListener(view -> moveToRight());
        startTimer();
    }

    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void moveToLeft() {
        int place = getPlace();
        if (place != 0) {
            main_IMG_bearrys[place - 1].setVisibility(View.VISIBLE);
            main_IMG_bearrys[place].setVisibility(View.INVISIBLE);
        }
    }

    private void moveToRight() {
        int place = getPlace();
        if (place != main_IMG_bearrys.length - 1) {
            main_IMG_bearrys[place + 1].setVisibility(View.VISIBLE);
            main_IMG_bearrys[place].setVisibility(View.INVISIBLE);
        }
    }

    private int getPlace() {
        int place = -1;
        for (int i = 0; i < main_IMG_bearrys.length; i++)
            if (main_IMG_bearrys[i].getVisibility() == View.VISIBLE)
                place = i;
        return place;
    }

    private void gameBeginning() { //Initialize the visibility of the objects
       randomColumn(); //Selects the first column from which the object will fall

        main_IMG_bearrys[main_IMG_bearrys.length / 2].setVisibility(View.VISIBLE); //At the beginning the player is placed in the center
        for (int i = 0; i < main_IMG_bearrys.length; i++)
            if (i != main_IMG_bearrys.length / 2)
                main_IMG_bearrys[i].setVisibility(View.INVISIBLE);

        for (int i = 0; i < main_IMG_chicken_matrix.length; i++)
            for (int j = 0; j < main_IMG_chicken_matrix[i].length; j++)
                main_IMG_chicken_matrix[i][j].setVisibility(View.INVISIBLE);
    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);

        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        main_IMG_chickens = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_chicken1),
                findViewById(R.id.main_IMG_chicken2),
                findViewById(R.id.main_IMG_chicken3)
        };

        main_IMG_chicken_matrix = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_chicken1_1), findViewById(R.id.main_IMG_chicken2_1), findViewById(R.id.main_IMG_chicken3_1)},
                {findViewById(R.id.main_IMG_chicken1_2), findViewById(R.id.main_IMG_chicken2_2), findViewById(R.id.main_IMG_chicken3_2)},
                {findViewById(R.id.main_IMG_chicken1_3), findViewById(R.id.main_IMG_chicken2_3), findViewById(R.id.main_IMG_chicken3_3)},
                {findViewById(R.id.main_IMG_chicken1_4), findViewById(R.id.main_IMG_chicken2_4), findViewById(R.id.main_IMG_chicken3_4)},
                {findViewById(R.id.main_IMG_chicken1_5), findViewById(R.id.main_IMG_chicken2_5), findViewById(R.id.main_IMG_chicken3_5)}
        };

        main_IMG_bearrys = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_bearry1),
                findViewById(R.id.main_IMG_bearry2),
                findViewById(R.id.main_IMG_bearry3)
        };

        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
    }

    private void updateTimerUI() { //Advance the objects with each passing second
        long currentMillis = System.currentTimeMillis() - startTime;
        int seconds = (int) (currentMillis / DELAY);
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;
        hours %= 24;
        refreshUI(); //Refresh the view
        checkCrash(); //Check if there is a crash
        randomColumn(); //Choose a random column for the next object
    }

    private void randomColumn() { //Selects a random column from which the next object will fall
        int chickenRandom = random.nextInt(main_IMG_chickens.length);
        main_IMG_chickens[chickenRandom].setVisibility(View.VISIBLE);
        for (int i = 0; i < main_IMG_chickens.length; i++) {
            if (i != chickenRandom)
                main_IMG_chickens[i].setVisibility(View.INVISIBLE);
        }
    }

    private void refreshUI() { //Advances the objects down the board
        for (int i = main_IMG_chicken_matrix.length - 1; i >= 0; i--) {
            for (int j = main_IMG_chicken_matrix[i].length - 1; j >= 0; j--) {
                if (i == 0)
                    main_IMG_chicken_matrix[i][j].setVisibility(main_IMG_chickens[j].getVisibility());
                else
                    main_IMG_chicken_matrix[i][j].setVisibility(main_IMG_chicken_matrix[i - 1][j].getVisibility());
            }
        }
    }

    private void startTimer() {
        if (!timerOn) {
            timerOn = true;
            startTime = System.currentTimeMillis();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> updateTimerUI());
                }
            }, 0, DELAY);
        }
    }

    private void stopTimer() {
        timerOn = false;
        timer.cancel();
    }

    private void checkCrash() { //Checks if there is a crash
        for (int i = 0; i < main_IMG_chickens.length; i++) {
            if (isCrash(i)) {
                vibrate();
                toast(DataManager.getToastMessages()[random.nextInt(DataManager.getToastMessages().length)]);
                if (gameManager.getCrashes() < main_IMG_hearts.length) { //update life amount
                    gameManager.setCrashes(gameManager.getCrashes() + 1);
                    int heartIndexToHide = main_IMG_hearts.length - 1 - (main_IMG_hearts.length - gameManager.getCrashes());
                    main_IMG_hearts[heartIndexToHide].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private boolean isCrash(int column) {
        return main_IMG_chicken_matrix[main_IMG_chicken_matrix.length - 1][column].getVisibility() == View.VISIBLE
                && main_IMG_bearrys[column].getVisibility() == View.VISIBLE;
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}