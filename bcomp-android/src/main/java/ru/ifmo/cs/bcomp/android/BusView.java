package ru.ifmo.cs.bcomp.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.ControlSignal;

import java.util.HashSet;
import java.util.Set;

public class BusView extends TextView {
    private static final Paint ACTIVE_PAINT;
    private static final Paint INACTIVE_PAINT;

    static {
        ACTIVE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        ACTIVE_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        ACTIVE_PAINT.setColor(Color.RED);

        INACTIVE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        INACTIVE_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        INACTIVE_PAINT.setColor(Color.GRAY);
    }

    private final Set<ControlSignal> signals = new HashSet<>();
    private boolean isActive = false;


    public BusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        String[] signalNames = getTag().toString().split(" ");
        for (String signalName : signalNames) {
            signals.add(ControlSignal.valueOf(signalName));
        }
    }


    public void activate() {
        if (isActive) return;
        isActive = true;
        invalidate();
    }


    public void deactivate() {
        if (!isActive) return;
        isActive = false;
        invalidate();
    }


    public Set<ControlSignal> getSignals() {
        return signals;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = (isActive) ? ACTIVE_PAINT : INACTIVE_PAINT;
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int yOffset = 23;
        int busWidth = 5;
        canvas.drawRect(0, yOffset, canvasWidth, yOffset + busWidth, paint);
        canvas.drawRect(canvasWidth - busWidth, yOffset, canvasWidth, canvasHeight - yOffset, paint);
        canvas.drawRect(0, canvasHeight - yOffset - busWidth, canvasWidth, canvasHeight - yOffset, paint);
    }
}
