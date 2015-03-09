package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;
import ru.ifmo.cs.elements.Memory;


public class MemoryFragment extends Fragment {
    private BCompHolder bCompHolder;
    private CPU cpu;
    private MemoryAdapter memoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (cpu == null) {
            cpu = bCompHolder.getCPU();
        }

        View memoryView = inflater.inflate(R.layout.memory_fragment, container, false);

        RecyclerView memoryRecyclerView = (RecyclerView) memoryView.findViewById(R.id.memory_recycler_view);
        memoryRecyclerView.setHasFixedSize(true);
        memoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        memoryAdapter = new MemoryAdapter();
        fillMemory();
        memoryRecyclerView.setAdapter(memoryAdapter);

        return memoryView;
    }


    protected void fillMemory() {
        Memory memory = cpu.getMemory();
        String[] rows = new String[memory.getSize()];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = String.format("%03X | %04X", i, memory.getValue(i));
        }
        memoryAdapter.changeDataSet(rows);
        memoryAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }
}
