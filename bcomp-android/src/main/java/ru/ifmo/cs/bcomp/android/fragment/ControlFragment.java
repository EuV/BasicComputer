package ru.ifmo.cs.bcomp.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import ru.ifmo.cs.bcomp.StateReg;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;
import ru.ifmo.cs.bcomp.android.util.TabAdapter;


public class ControlFragment extends RootFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View controlView = inflater.inflate(R.layout.control_fragment, container, false);

        controlView.findViewById(R.id.image_button_settings).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                // TODO
            }
        });

        controlView.findViewById(R.id.image_button_read).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.startRead();
            }
        });

        controlView.findViewById(R.id.image_button_start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.startStart();
            }
        });

        controlView.findViewById(R.id.image_button_continue).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.startContinue();
            }
        });

        ImageButton runStateButton = (ImageButton) controlView.findViewById(R.id.image_button_run_state);
        runStateButton.setSelected(getRunState());
        runStateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.invertRunState();
                v.setSelected(getRunState());
                bCompHolder.updateTab(TabAdapter.MP_TAB);
            }
        });

        ImageButton clockStateButton = (ImageButton) controlView.findViewById(R.id.image_button_clock_state);
        clockStateButton.setSelected(getClockState());
        clockStateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.invertClockState();
                v.setSelected(getClockState());
            }
        });

        return controlView;
    }


    private boolean getRunState() {
        return (cpu.getStateValue(StateReg.FLAG_RUN) == 1);
    }


    private boolean getClockState() {
        return !cpu.getClockState();
    }
}
