package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MicroPrograms;

public class BCompInstance extends Fragment {

    public interface BCompHolder {
        CPU getCPU();
        void tickFinished();
    }

    private BCompHolder bCompHolder;
    private final BasicComp bcomp;

    public final CPU cpu;


    public BCompInstance() throws Exception {
        bcomp = new BasicComp(MicroPrograms.getMicroProgram(MicroPrograms.DEFAULT_MICROPROGRAM));
        cpu = bcomp.getCPU();
        cpu.setTickFinishListener(new Runnable() {
            @Override
            public void run() {
                if (bCompHolder != null) {
                    bCompHolder.tickFinished();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        bcomp.startTimer();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        bCompHolder = null;
    }
}
