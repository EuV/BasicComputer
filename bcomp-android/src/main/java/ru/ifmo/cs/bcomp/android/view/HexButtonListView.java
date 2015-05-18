package ru.ifmo.cs.bcomp.android.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.android.R;

public class HexButtonListView extends ListView {
    public static final int DEFAULT_POSITION = Integer.MAX_VALUE / 2 + 1;

    private final TextView cells[] = new TextView[16];
    private boolean textWasChanged = false;
    private boolean scrollable = true;
    private int cellHalfHeight;

    public Integer targetPosition = null;

    public HexButtonListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        float textSize = getResources().getDimension(R.dimen.hex_button_text_size);

        for (int i = 0; i < cells.length; i++) {
            cells[i] = new TextView(getContext());
            cells[i].setText(Utils.toHex(i, 1));
            cells[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            cells[i].setTypeface(Typeface.MONOSPACE);
            cells[i].setGravity(Gravity.CENTER);
            cells[i].setBackgroundResource(R.drawable.control_button);
        }

        setAdapter(new HexButtonAdapter());
    }


    public void scrollToValue(int newValue) {
        int oldPosition = getCurrentPosition();
        int oldValue = oldPosition % cells.length;

        if (oldPosition == 0) return;

        if (textWasChanged) {
            TextView cell = (TextView) getItemAtPosition(oldPosition);
            cell.setText(Utils.toHex(oldValue, 1));
            cell.invalidate();
            textWasChanged = false;
        }

        if (newValue == oldValue) return;


        int newPositionAbove;
        int newPositionBelow;

        if (newValue < oldValue) {
            newPositionAbove = oldPosition - (oldValue - newValue);
            newPositionBelow = newPositionAbove + cells.length;
        } else {
            newPositionBelow = oldPosition + (newValue - oldValue);
            newPositionAbove = newPositionBelow - cells.length;
        }

        int newPosition;

        if (oldPosition - newPositionAbove < cells.length / 2) {
            newPosition = newPositionAbove;
        } else {
            newPosition = newPositionBelow;
        }

        targetPosition = newPosition;
        smoothScrollToPosition(newPosition);
    }


    public void setText(String text) {
        TextView cell = (TextView) getItemAtPosition(getCurrentPosition());
        cell.setText(text);
        textWasChanged = true;
    }


    public int getCurrentPosition() {
        int position = getFirstVisiblePosition();
        TextView cell = (TextView) getItemAtPosition(position);
        if (cell.getBottom() < cellHalfHeight) position++;
        return position;
    }


    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !scrollable) {
            return true; // Don't scroll
        }
        return super.dispatchTouchEvent(motionEvent);
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        for (TextView cell : cells) {
            cell.setWidth(width);
            cell.setHeight(height);
        }
        cellHalfHeight = height / 2;
    }


    private class HexButtonAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return cells[position % cells.length];
        }

        @Override
        public long getItemId(int position) {
            return cells[position % cells.length].getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return cells[position % cells.length];
        }
    }
}
