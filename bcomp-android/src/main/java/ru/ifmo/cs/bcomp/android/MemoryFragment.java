package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MemoryFragment extends Fragment {
    MemoryAdapter memoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View memoryView = inflater.inflate(R.layout.memory_fragment, container, false);

        RecyclerView memoryRecyclerView = (RecyclerView) memoryView.findViewById(R.id.memory_recycler_view);
        memoryRecyclerView.setHasFixedSize(true);
        memoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        memoryAdapter = new MemoryAdapter();
        memoryRecyclerView.setAdapter(memoryAdapter);

        return memoryView;
    }


    public void fillMemory(int[] values) {
        String[] rows = new String[values.length];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = String.format("%03X | %04X", i, values[i]);
        }
        memoryAdapter.changeDataSet(rows);
        memoryAdapter.notifyDataSetChanged();
    }
}
