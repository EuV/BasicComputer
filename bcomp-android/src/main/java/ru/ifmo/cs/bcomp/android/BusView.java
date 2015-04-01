package ru.ifmo.cs.bcomp.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import ru.ifmo.cs.bcomp.ControlSignal;

import java.util.HashSet;
import java.util.Set;

public class BusView extends View {
    private static final Paint ACTIVE_PAINT;
    private static final Paint INACTIVE_PAINT;
    private static final int BUS_WIDTH_DP = 3;
    private static int BUS_WIDTH_PX = 0;

    static {
        ACTIVE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        ACTIVE_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        ACTIVE_PAINT.setColor(Color.RED);

        INACTIVE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        INACTIVE_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        INACTIVE_PAINT.setColor(Color.rgb(170, 170, 170));
    }

    private final Set<ControlSignal> signals = new HashSet<>();
    private boolean isActive = false;

    private final boolean left;
    private final boolean top;
    private final boolean right;
    private final boolean bottom;

    private final float leftPadding;
    private final float topPadding;
    private final float rightPadding;
    private final float bottomPadding;


    public BusView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (BUS_WIDTH_PX == 0) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            BUS_WIDTH_PX = Math.round(BUS_WIDTH_DP * displayMetrics.density);
        }

        String[] signalNames = getTag().toString().split(" ");
        for (String signalName : signalNames) {
            signals.add(ControlSignal.valueOf(signalName));
        }

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BusView, 0, 0);
        try {
            left = attributes.getBoolean(R.styleable.BusView_left, false);
            top = attributes.getBoolean(R.styleable.BusView_top, false);
            right = attributes.getBoolean(R.styleable.BusView_right, false);
            bottom = attributes.getBoolean(R.styleable.BusView_bottom, false);
            leftPadding = attributes.getDimension(R.styleable.BusView_left_padding, 0);
            topPadding = attributes.getDimension(R.styleable.BusView_top_padding, 0);
            rightPadding = attributes.getDimension(R.styleable.BusView_right_padding, 0);
            bottomPadding = attributes.getDimension(R.styleable.BusView_bottom_padding, 0);
        } finally {
            attributes.recycle();
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
        Paint paint = (isActive) ? ACTIVE_PAINT : INACTIVE_PAINT;
        float rightOffset = canvas.getWidth() - rightPadding;
        float bottomOffset = canvas.getHeight() - bottomPadding;

        if (left) canvas.drawRect(leftPadding, topPadding, leftPadding + BUS_WIDTH_PX, bottomOffset, paint);
        if (top) canvas.drawRect(leftPadding, topPadding, rightOffset, topPadding + BUS_WIDTH_PX, paint);
        if (right) canvas.drawRect(rightOffset - BUS_WIDTH_PX, topPadding, rightOffset, bottomOffset, paint);
        if (bottom) canvas.drawRect(leftPadding, bottomOffset - BUS_WIDTH_PX, rightOffset, bottomOffset, paint);
    }
}
