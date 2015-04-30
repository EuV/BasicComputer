package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;
import ru.ifmo.cs.elements.Memory;


public class MemoryFragment extends Fragment {
    private static final int PORTRAIT_ROW_COUNT = 16;
    private static final int LANDSCAPE_ROW_COUNT = 8;

    private BCompHolder bCompHolder;
    private CPU cpu;
    private TextView[] addressRows;
    private TextView[] valueRows;
    private int row_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (cpu == null) {
            cpu = bCompHolder.getCPU();
        }

        View memoryFragment = inflater.inflate(R.layout.memory_fragment, container, false);

        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        row_count = portrait ? PORTRAIT_ROW_COUNT : LANDSCAPE_ROW_COUNT;

        addressRows = new TextView[row_count];
        LinearLayout addressColumn = (LinearLayout) memoryFragment.findViewById(R.id.memory_address_column);
        createRows(addressRows, addressColumn);

        valueRows = new TextView[row_count];
        LinearLayout valueColumn = (LinearLayout) memoryFragment.findViewById(R.id.memory_value_column);
        createRows(valueRows, valueColumn);

        fillMemory();

        return memoryFragment;
    }


    public void fillMemory() {
        Memory memory = cpu.getMemory();
        int page = memory.getAddrValue() & (~(row_count - 1));
        for (int i = 0; i < row_count; i++) {
            addressRows[i].setText(String.format("%03X", page + i));
            valueRows[i].setText(String.format("%04X", memory.getValue(page + i)));
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }


    private void createRows(TextView[] rows, LinearLayout column) {
        Activity activity = getActivity();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f);
        float textSize = getResources().getDimension(R.dimen.register_text_size);

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new TextView(activity);
            rows[i].setLayoutParams(layoutParams);
            rows[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            rows[i].setGravity(Gravity.CENTER);
            rows[i].setTypeface(Typeface.MONOSPACE);
            column.addView(rows[i]);
        }
    }
}
