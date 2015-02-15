package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.MicroPrograms;


public class BCompInstance extends Fragment {

    public interface BCompCallbacks {
        // TBD
    }

    private BCompCallbacks callbacks;
    private final BasicComp bcomp;


    public BCompInstance() throws Exception {
        bcomp = new BasicComp(MicroPrograms.getMicroProgram(MicroPrograms.DEFAULT_MICROPROGRAM));
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
        callbacks = (BCompCallbacks) activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }
}
