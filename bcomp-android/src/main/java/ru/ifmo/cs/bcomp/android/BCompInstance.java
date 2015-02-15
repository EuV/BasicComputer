package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;


public class BCompInstance extends Fragment {

    public interface BCompCallbacks {
        // TBD
    }

    private BCompCallbacks callbacks;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
