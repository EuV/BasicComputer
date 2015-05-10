package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;

import java.util.Set;

public class MainActivity extends ActionBarActivity implements BCompHolder {

    private static final String TAG_BASIC_COMPUTER_INSTANCE = "bcomp_fragment";
    private BCompInstance bci;
    private MemoryFragment memoryFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        bci = (BCompInstance) fm.findFragmentByTag(TAG_BASIC_COMPUTER_INSTANCE);
        if (bci == null) {
            try {
                bci = new BCompInstance();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
            fm.beginTransaction().add(bci, TAG_BASIC_COMPUTER_INSTANCE).commit();
        }

        setContentView(R.layout.main);
        TabAdapter.setup(this);

        BCompVibrator.init(this);

        memoryFragment = (MemoryFragment) fm.findFragmentById(R.id.memory_fragment);
    }


    @Override
    public CPU getCPU() {
        return bci.cpu;
    }


    @Override
    public void tickFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TabAdapter tabAdapter = TabAdapter.getAdapter();

                BasicFragment basicFragment = (BasicFragment) tabAdapter.getFragment(TabAdapter.BC_TAB);
                if (basicFragment != null) {
                    basicFragment.updateViews();
                }

                IOFragment ioFragment = (IOFragment) tabAdapter.getFragment(TabAdapter.IO_TAB);
                if (ioFragment != null) {
                    ioFragment.updateViews();
                }
            }
        });
    }


    @Override
    public void updateMemory() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                memoryFragment.fillMemory();
            }
        });
    }


    @Override
    public void registerNewSignals(Set<ControlSignal> signals) {
        bci.registerNewSignals(signals);
    }


    @Override
    public Set<ControlSignal> getOpenSignals() {
        return bci.getOpenSignals();
    }


    @Override
    public void setTickDelay(long tickDelay) {
        bci.setTickDelay(tickDelay);
    }


    @Override
    public IOCtrl[] getIOControls() {
        return bci.getIOControls();
    }
}
