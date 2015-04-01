package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.StateReg;


public class ControlFragment extends Fragment {
    private BCompInstance.BCompHolder bCompHolder;
    private CPU cpu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (cpu == null) {
            cpu = bCompHolder.getCPU();
        }

        View controlView = inflater.inflate(R.layout.control_fragment, container, false);

        controlView.findViewById(R.id.control_read).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cpu.startRead();
            }
        });

        controlView.findViewById(R.id.control_start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cpu.startStart();
            }
        });

        controlView.findViewById(R.id.control_continue).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cpu.startContinue();
            }
        });

        ((Switch) controlView.findViewById(R.id.control_run_state)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean running = (cpu.getRegister(CPU.Reg.STATE).getValue(StateReg.FLAG_RUN) == 1);

                // We shouldn't just call invertRunState() because the change event is also fired during fragment recreation
                if (running ^ isChecked) {
                    cpu.invertRunState();
                    // TODO: Redraw State Register on the MP tab (there's its full view there)
                }
            }
        });

        ((CheckBox) controlView.findViewById(R.id.control_clock_state)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cpu.getClockState() ^ !isChecked) {
                    cpu.invertClockState();
                }
            }
        });

        return controlView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompInstance.BCompHolder) activity;
    }
}
