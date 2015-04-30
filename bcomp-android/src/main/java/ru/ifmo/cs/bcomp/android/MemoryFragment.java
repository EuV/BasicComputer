package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;
import ru.ifmo.cs.elements.Memory;


public class MemoryFragment extends Fragment {
    private BCompHolder bCompHolder;
    private CPU cpu;
    private TextView memoryView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (cpu == null) {
            cpu = bCompHolder.getCPU();
        }

        View memoryFragment = inflater.inflate(R.layout.memory_fragment, container, false);

        memoryView = (TextView) memoryFragment.findViewById(R.id.memory_view);
        memoryView.setTypeface(Typeface.MONOSPACE);
        fillMemory();

        return memoryFragment;
    }


    protected void fillMemory() {
        Memory memory = cpu.getMemory();
        int page = memory.getAddrValue() & 0xFFF0;
        StringBuilder rows = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            rows.append(String.format("%03X | %04X\n", page + i, memory.getValue(page + i)));
        }
        memoryView.setText(rows);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }
}
