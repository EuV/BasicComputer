package ru.ifmo.cs.bcomp.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;

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

        CheckBox inputSwitcher = (CheckBox) viewsHolder.findViewById(R.id.controlUnitInputSwitcher);
        inputSwitcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (bCompHolder.isInputToControlUnit() ^ isChecked) {
                    BCompVibrator.vibrate();
                    bCompHolder.setInputToControlUnit(isChecked);
                }
            }
        });
    }


    @Override
    protected void customUpdateViews() {
        microMemory.fillMemory();
    }
}
