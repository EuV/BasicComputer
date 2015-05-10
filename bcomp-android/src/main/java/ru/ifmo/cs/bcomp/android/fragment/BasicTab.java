package ru.ifmo.cs.bcomp.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.view.RunningCycleView;

public class BasicTab extends GraphicalTab {
    private RunningCycleView runningCycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.basic_fragment, container, false);

        init((ViewGroup) rootView.findViewById(R.id.basic_views_holder));

        return rootView;
    }


    @Override
    protected void customInit(ViewGroup viewsHolder) {
        runningCycleView = (RunningCycleView) viewsHolder.findViewById(R.id.running_cycle);
    }


    @Override
    protected void customUpdateViews() {
        runningCycleView.update(cpu);
    }
}
