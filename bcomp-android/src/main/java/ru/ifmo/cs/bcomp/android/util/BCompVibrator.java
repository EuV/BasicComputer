package ru.ifmo.cs.bcomp.android.util;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

public final class BCompVibrator {
    private static final long DURATION_OF_VIBRATION_MS = 40;
    private static Vibrator vibrator;

    private BCompVibrator() {
    }


    public static void init(Activity activity) {
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
    }


    public static void vibrate() {
        if (vibrator != null) {
            vibrator.vibrate(DURATION_OF_VIBRATION_MS);
        }
    }
}
