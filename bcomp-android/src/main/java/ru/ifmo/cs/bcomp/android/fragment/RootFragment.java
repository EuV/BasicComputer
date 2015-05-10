package ru.ifmo.cs.bcomp.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;

public class RootFragment extends Fragment {
    protected BCompHolder bCompHolder;
    protected CPU cpu;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cpu = bCompHolder.getCPU();
        return null;
    }
}
