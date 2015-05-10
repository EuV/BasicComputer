package ru.ifmo.cs.bcomp.android.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.RunningCycle;
import ru.ifmo.cs.bcomp.StateReg;
import ru.ifmo.cs.bcomp.android.R;

public class RunningCycleView extends LinearLayout {
    private static final int COLOR_ACTIVE = Color.RED;
    private static final int COLOR_INACTIVE = Color.BLACK;

    private TextView[] cycles;
    private RunningCycle lastCycle = RunningCycle.NONE;
    private int lastProgram = 0;

    public RunningCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(context);
        title.setText(context.getString(R.string.running_cycle_title));
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.MONOSPACE);
        title.setBackgroundResource(R.color.register_header);
        title.setPadding(5, 0, 5, 0); // TODO it's in pixel for now; move all the style to xml
        title.setTextSize(10);
        addView(title);

        if (isInEditMode()) return;

        String[] cycleNames = context.getResources().getStringArray(R.array.running_cycles);
        cycles = new TextView[cycleNames.length];

        for (int i = 0; i < cycles.length; i++) {
            cycles[i] = new TextView(context);
            cycles[i].setText(cycleNames[i]);
            cycles[i].setTextSize(10); // TODO decode from xml
            cycles[i].setGravity(Gravity.CENTER);
            cycles[i].setTypeface(Typeface.MONOSPACE);
            cycles[i].setBackgroundResource(R.color.register_value);
            addView(cycles[i]);
        }
    }


    public void update(CPU cpu) {
        RunningCycle newCycle = cpu.getRunningCycle();
        if (newCycle != lastCycle) {
            if (lastCycle != RunningCycle.NONE) {
                cycles[lastCycle.ordinal()].setTextColor(COLOR_INACTIVE);
            }
            if (newCycle != RunningCycle.NONE) {
                cycles[newCycle.ordinal()].setTextColor(COLOR_ACTIVE);
            }
            lastCycle = newCycle;
        }

        int newProgram = cpu.getStateValue(StateReg.FLAG_PROG);
        if (newProgram != lastProgram) {
            cycles[cycles.length - 1].setTextColor((newProgram == 0) ? COLOR_INACTIVE : COLOR_ACTIVE);
            lastProgram = newProgram;
        }
    }
}
