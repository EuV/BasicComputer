package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;


public class MemoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View memoryView = inflater.inflate(R.layout.memory_fragment, container, false);

        RecyclerView memoryRecyclerView = (RecyclerView) memoryView.findViewById(R.id.memory_recycler_view);
        memoryRecyclerView.setHasFixedSize(true);
        memoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String[] rows = new String[2048];
        Random random = new Random();
        for (int i = 0; i < rows.length; i++) {
            rows[i] = String.format("%03X | %04X", i, random.nextInt(65535));
        }

        memoryRecyclerView.setAdapter(new MemoryAdapter(rows));

        return memoryView;
    }
}
