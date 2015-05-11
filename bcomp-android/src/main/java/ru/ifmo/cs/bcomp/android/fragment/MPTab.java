package ru.ifmo.cs.bcomp.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.android.R;

public class MPTab extends GraphicalTab {
    private MemoryFragment microMemory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.mp_fragment, container, false);

        init((ViewGroup) rootView.findViewById(R.id.mp_views_holder));

        return rootView;
    }


    @Override
    protected void customInit(ViewGroup viewsHolder) {
        microMemory = (MemoryFragment) getChildFragmentManager().findFragmentById(R.id.mp_memory_fragment);
    }


    @Override
    protected void customUpdateViews() {
        microMemory.fillMemory();
    }
}
