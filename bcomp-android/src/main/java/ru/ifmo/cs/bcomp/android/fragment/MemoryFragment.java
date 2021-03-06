package ru.ifmo.cs.bcomp.android.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.elements.Memory;


public class MemoryFragment extends RootFragment {
    private static final int PORTRAIT_ROW_COUNT = 16;
    private static final int LANDSCAPE_ROW_COUNT = 8;

    private TextView[] addressRows;
    private TextView[] valueRows;
    private int rowCount;

    private Memory memory;
    private int addressBitWidth;
    private int valueBitWidth;
    private int lastPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View memoryFragment = inflater.inflate(R.layout.memory_fragment, container, false);

        String title = getTag();
        ((TextView) memoryFragment.findViewById(R.id.memory_title)).setText(title);

        boolean isMicroMemory = (title.equals(getResources().getString(R.string.mp_memory)));
        memory = (isMicroMemory) ? cpu.getMicroMemory() : cpu.getMemory();
        addressBitWidth = memory.getAddrWidth();
        valueBitWidth = memory.getWidth();

        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        rowCount = portrait ? PORTRAIT_ROW_COUNT : LANDSCAPE_ROW_COUNT;

        addressRows = new TextView[rowCount];
        LinearLayout addressColumn = (LinearLayout) memoryFragment.findViewById(R.id.memory_address_column);
        createRows(addressRows, addressColumn);

        valueRows = new TextView[rowCount];
        LinearLayout valueColumn = (LinearLayout) memoryFragment.findViewById(R.id.memory_value_column);
        createRows(valueRows, valueColumn);

        updateLastAddress();
        updateMemory();

        return memoryFragment;
    }


    public void memoryEvent(MemoryEvent event) {
        switch (event) {

            case UPDATE_MEMORY:
                updateMemory();
                break;

            case UPDATE_LAST_ADDRESS:
                updateLastAddress();
                break;

            case READ:
                read();
                break;

            case WRITE:
                write();
                break;
        }
    }


    private void updateMemory() {
        for (int i = 0; i < rowCount; i++) {
            updateValue(i);
        }
    }


    private void updateLastAddress() {
        lastPage = getPage();
    }


    private void read() {
        int page = getPage();

        if (page != lastPage) {
            lastPage = page;
            updateMemory();
        }
    }


    private void write() {
        int page = getPage();

        if (page == lastPage) {
            updateValue(memory.getAddrValue() - page);
        } else {
            lastPage = page;
            updateMemory();
        }
    }


    private void updateValue(int offset) {
        addressRows[offset].setText(Utils.toHex(lastPage + offset, addressBitWidth));
        valueRows[offset].setText(Utils.toHex(memory.getValue(lastPage + offset), valueBitWidth));
    }


    private int getPage() {
        return memory.getAddrValue() & (~(rowCount - 1));
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


    public enum MemoryEvent {
        UPDATE_MEMORY,
        UPDATE_LAST_ADDRESS,
        READ,
        WRITE
    }
}
