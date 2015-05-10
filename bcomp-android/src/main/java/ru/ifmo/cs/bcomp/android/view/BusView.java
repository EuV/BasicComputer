package ru.ifmo.cs.bcomp.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.android.R;

import java.util.HashSet;
import java.util.Set;

import static ru.ifmo.cs.bcomp.android.view.BusView.Arrow.*;

public class BusView extends View {
    protected interface Arrow {
        int NONE = 0;
        int LEFT_TOP_TO_BOTTOM = 1;
        int LEFT_BOTTOM_TO_TOP = 2;
        int TOP_RIGHT_TO_LEFT = 3;
        int TOP_LEFT_TO_RIGHT = 4;
        int RIGHT_BOTTOM_TO_TOP = 5;
        int RIGHT_TOP_TO_BOTTOM = 6;
        int BOTTOM_LEFT_TO_RIGHT = 7;
        int BOTTOM_RIGHT_TO_LEFT = 8;
    }

    private static final Paint ACTIVE_PAINT;
    private static final Paint INACTIVE_PAINT;

    static {
        ACTIVE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        ACTIVE_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        ACTIVE_PAINT.setColor(Color.RED);

        INACTIVE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        INACTIVE_PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        INACTIVE_PAINT.setColor(Color.rgb(170, 170, 170));
    }

    private static final int BUS_WIDTH_DP = 3; // TODO: define in xml ?

    private static float BUS_WIDTH_PX = Float.NaN; //  |
    private static float ARROW_SIDE_1 = Float.NaN; // 1V
    private static float ARROW_SIDE_2 = Float.NaN; // 2+2

    private final Set<ControlSignal> signals = new HashSet<>();
    private boolean isActive = false;

    private float busOffsetLeft = Float.NaN;
    private float busOffsetTop = Float.NaN;
    private float busOffsetRight = Float.NaN;
    private float busOffsetBottom = Float.NaN;

    private final boolean left;
    private final boolean top;
    private final boolean right;
    private final boolean bottom;

    private final float leftPadding;
    private final float topPadding;
    private final float rightPadding;
    private final float bottomPadding;

    private final int arrow;
    private final Path arrowPath = new Path();


    public BusView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Float.isNaN(BUS_WIDTH_PX)) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            BUS_WIDTH_PX = BUS_WIDTH_DP * displayMetrics.density;
            ARROW_SIDE_1 = BUS_WIDTH_PX * 2.0f;
            ARROW_SIDE_2 = BUS_WIDTH_PX * 1.5f;
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

            arrow = attributes.getInt(R.styleable.BusView_arrow, NONE);
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
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        configureBuses(width, height);
        configureArrows(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = (isActive) ? ACTIVE_PAINT : INACTIVE_PAINT;

        if (left) canvas.drawRect(busOffsetLeft, busOffsetTop, busOffsetLeft + BUS_WIDTH_PX, busOffsetBottom, paint);
        if (top) canvas.drawRect(busOffsetLeft, busOffsetTop, busOffsetRight, busOffsetTop + BUS_WIDTH_PX, paint);
        if (right) canvas.drawRect(busOffsetRight - BUS_WIDTH_PX, busOffsetTop, busOffsetRight, busOffsetBottom, paint);
        if (bottom) canvas.drawRect(busOffsetLeft, busOffsetBottom - BUS_WIDTH_PX, busOffsetRight, busOffsetBottom, paint);

        if (arrow == NONE) return;
        canvas.drawPath(arrowPath, paint);
    }


    private void configureBuses(int canvasWidth, int canvasHeight) {
        busOffsetLeft = leftPadding;
        busOffsetTop = topPadding;
        busOffsetRight = canvasWidth - rightPadding;
        busOffsetBottom = canvasHeight - bottomPadding;

        switch (arrow) {
            case NONE:
                break;

            case TOP_RIGHT_TO_LEFT:
            case BOTTOM_RIGHT_TO_LEFT:
                busOffsetLeft += ARROW_SIDE_1;
                break;

            case LEFT_BOTTOM_TO_TOP:
            case RIGHT_BOTTOM_TO_TOP:
                busOffsetTop += ARROW_SIDE_1;
                break;

            case TOP_LEFT_TO_RIGHT:
            case BOTTOM_LEFT_TO_RIGHT:
                busOffsetRight -= ARROW_SIDE_1;
                break;

            case LEFT_TOP_TO_BOTTOM:
            case RIGHT_TOP_TO_BOTTOM:
                busOffsetBottom -= ARROW_SIDE_1;
                break;
        }
    }


    private void configureArrows(int canvasWidth, int canvasHeight) {
        float halfBusWidth = BUS_WIDTH_PX / 2;
        float rightOffset = canvasWidth - rightPadding;
        float bottomOffset = canvasHeight - bottomPadding;

        arrowPath.reset();

        switch (arrow) {
            case NONE:
                break;

            case LEFT_TOP_TO_BOTTOM:
                arrowPath.moveTo(leftPadding + halfBusWidth, bottomOffset);
                arrowPath.lineTo(leftPadding + halfBusWidth - ARROW_SIDE_2, bottomOffset - ARROW_SIDE_1);
                arrowPath.lineTo(leftPadding + halfBusWidth + ARROW_SIDE_2, bottomOffset - ARROW_SIDE_1);
                break;

            case LEFT_BOTTOM_TO_TOP:
                arrowPath.moveTo(leftPadding + halfBusWidth, topPadding);
                arrowPath.lineTo(leftPadding + halfBusWidth + ARROW_SIDE_2, topPadding + ARROW_SIDE_1);
                arrowPath.lineTo(leftPadding + halfBusWidth - ARROW_SIDE_2, topPadding + ARROW_SIDE_1);
                break;

            case TOP_RIGHT_TO_LEFT:
                arrowPath.moveTo(leftPadding, topPadding + halfBusWidth);
                arrowPath.lineTo(leftPadding + ARROW_SIDE_1, topPadding + halfBusWidth - ARROW_SIDE_2);
                arrowPath.lineTo(leftPadding + ARROW_SIDE_1, topPadding + halfBusWidth + ARROW_SIDE_2);
                break;

            case TOP_LEFT_TO_RIGHT:
                arrowPath.moveTo(rightOffset, topPadding + halfBusWidth);
                arrowPath.lineTo(rightOffset - ARROW_SIDE_1, topPadding + halfBusWidth + ARROW_SIDE_2);
                arrowPath.lineTo(rightOffset - ARROW_SIDE_1, topPadding + halfBusWidth - ARROW_SIDE_2);
                break;

            case RIGHT_BOTTOM_TO_TOP:
                arrowPath.moveTo(rightOffset - halfBusWidth, topPadding);
                arrowPath.lineTo(rightOffset - halfBusWidth + ARROW_SIDE_2, topPadding + ARROW_SIDE_1);
                arrowPath.lineTo(rightOffset - halfBusWidth - ARROW_SIDE_2, topPadding + ARROW_SIDE_1);
                break;

            case RIGHT_TOP_TO_BOTTOM:
                arrowPath.moveTo(rightOffset - halfBusWidth, bottomOffset);
                arrowPath.lineTo(rightOffset - halfBusWidth - ARROW_SIDE_2, bottomOffset - ARROW_SIDE_1);
                arrowPath.lineTo(rightOffset - halfBusWidth + ARROW_SIDE_2, bottomOffset - ARROW_SIDE_1);
                break;

            case BOTTOM_LEFT_TO_RIGHT:
                arrowPath.moveTo(rightOffset, bottomOffset - halfBusWidth);
                arrowPath.lineTo(rightOffset - ARROW_SIDE_1, bottomOffset - halfBusWidth + ARROW_SIDE_2);
                arrowPath.lineTo(rightOffset - ARROW_SIDE_1, bottomOffset - halfBusWidth - ARROW_SIDE_2);

                break;

            case BOTTOM_RIGHT_TO_LEFT:
                arrowPath.moveTo(leftPadding, bottomOffset - halfBusWidth);
                arrowPath.lineTo(leftPadding + ARROW_SIDE_1, bottomOffset - halfBusWidth - ARROW_SIDE_2);
                arrowPath.lineTo(leftPadding + ARROW_SIDE_1, bottomOffset - halfBusWidth + ARROW_SIDE_2);
                break;
        }

        if (arrow != NONE) {
            arrowPath.close();
        }
    }
}
