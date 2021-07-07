package com.my.spinningarrow.listeners;

import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public abstract class DoubleClickListener implements View.OnTouchListener {
    private static final long DEFAULT_QUALIFICATION_SPAN = 200;
    private boolean isSingleEvent;
    private long doubleClickQualificationSpanInMillis;
    private long timestampLastClick;
    private Handler handler;
    private Runnable runnable;
    private boolean isPressed = false;

    public DoubleClickListener() {
        doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN;
        timestampLastClick = 0;
        handler = new Handler();
        runnable = () -> {
            if (isSingleEvent && isPressed) {
                onSingleClick();
            }
        };
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                // Start
                Log.d("TEST", "DOWN");
                if((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickQualificationSpanInMillis) {
                    isSingleEvent = false;
                    handler.removeCallbacks(runnable);
                    onDoubleClick();
                 break;
                }

                isSingleEvent = true;
                handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN);
                timestampLastClick = SystemClock.elapsedRealtime();
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                // End
                Log.d("TEST", "UP");
                onRelease();
                break;
        }

        return false;
    }

    //    @Override
//    public void onClick(View v) {
//        if((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickQualificationSpanInMillis) {
//            isSingleEvent = false;
//            handler.removeCallbacks(runnable);
//            onDoubleClick();
//            return;
//        }
//
//        isSingleEvent = true;
//        handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN);
//        timestampLastClick = SystemClock.elapsedRealtime();
//    }

    public abstract void onDoubleClick();
    public abstract void onSingleClick();
    public abstract void onRelease();
}
