package ru.ifmo.cs.bcomp.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.android.R;

public class ALUView extends TextView {
    private final Path aluPath = new Path();
    private final Paint PAINT;

    public ALUView(Context context, AttributeSet attrs) {
        super(context, attrs);
        PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        PAINT.setStyle(Paint.Style.FILL_AND_STROKE);
        PAINT.setColor(getResources().getColor(R.color.register_header));
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        int half = width / 2;
        int offset = height / 3;
        int grooveOffset = offset / 3;

        int xPoints[] = new int[]{half - grooveOffset, half, half + grooveOffset, width, width - offset, offset};
        int yPoints[] = new int[]{0, offset, 0, 0, height, height};

        aluPath.moveTo(0, 0);
        for (int i = 0; i < xPoints.length; i++) {
            aluPath.lineTo(xPoints[i], yPoints[i]);
        }
        aluPath.close();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(aluPath, PAINT);
        super.onDraw(canvas);
    }
}
