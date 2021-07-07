package com.my.spinningarrow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.my.spinningarrow.listeners.DoubleClickListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    Sensor accelerometer;
    ObjectAnimator imageViewObjectAnimator;

    private boolean isPressed = false;
    private Button button1, button2, button3, button4;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageview = findViewById(R.id.imageView);

        imageViewObjectAnimator = new ObjectAnimator();
        imageViewObjectAnimator.setTarget(imageview);
        imageViewObjectAnimator.setPropertyName("rotation");


        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);


        button1.setOnTouchListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                button2.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);
                isPressed = true;
                rotateToTarget(135f);
            }

            @Override
            public void onSingleClick() {
                button2.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);
                isPressed = true;
                rotateToTarget(315f);
            }

            @Override
            public void onRelease() {
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);
                isPressed = false;
                rotateInfinite();
            }
        });

        button2.setOnTouchListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                button1.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);
                isPressed = true;
                rotateToTarget(225f);
            }

            @Override
            public void onSingleClick() {
                button1.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);
                isPressed = true;
                rotateToTarget(45f);
            }

            @Override
            public void onRelease() {
                button1.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);
                isPressed = false;
                rotateInfinite();
            }
        });

        button3.setOnTouchListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                button1.setEnabled(false);
                button2.setEnabled(false);
                button4.setEnabled(false);
                isPressed = true;
                rotateToTarget(45f);

            }

            @Override
            public void onSingleClick() {
                button1.setEnabled(false);
                button2.setEnabled(false);
                button4.setEnabled(false);
                isPressed = true;
                rotateToTarget(225f);
            }

            @Override
            public void onRelease() {
                button1.setEnabled(true);
                button2.setEnabled(true);
                button4.setEnabled(true);
                isPressed = false;
                rotateInfinite();
            }
        });

        button4.setOnTouchListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                isPressed = true;
                rotateToTarget(315f);

            }

            @Override
            public void onSingleClick() {
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                isPressed = true;
                rotateToTarget(135f);
            }

            @Override
            public void onRelease() {
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                isPressed = false;
                rotateInfinite();
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] g;
        g = event.values.clone();

        double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);

        // Normalize the accelerometer vector
        g[0] = (float) (g[0] / norm_Of_g);
        g[1] = (float) (g[1] / norm_Of_g);
        g[2] = (float) (g[2] / norm_Of_g);

        int inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));

        if (inclination < 25 || inclination > 155)
        {
            // device is flat
            if(!imageViewObjectAnimator.isRunning() && !isPressed) {
                rotateInfinite();
            }
            if(!isPressed){
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);
            }
        }
        else
        {
            // device is not flat
            Log.d(TAG, "onSensorChanged: device is not flat");
            if(imageViewObjectAnimator.isRunning()) {
                imageViewObjectAnimator.end();
            }
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void rotateInfinite() {
        imageViewObjectAnimator.setFloatValues(0f, 360f);
        imageViewObjectAnimator.setDuration(500);
        imageViewObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        imageViewObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        imageViewObjectAnimator.setInterpolator(new AccelerateInterpolator());
        imageViewObjectAnimator.start();
    }

    private void rotateToTarget(float target) {
        imageViewObjectAnimator.cancel();
        imageViewObjectAnimator.setFloatValues(target);
        imageViewObjectAnimator.setDuration(500);
        imageViewObjectAnimator.setRepeatCount(0);
        imageViewObjectAnimator.start();
    }

}