package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import ru.ifmo.cs.bcomp.CPU;

public class MainActivity extends ActionBarActivity implements
    BCompInstance.BCompCallbacks,
    KeyboardFragment.KeyboardCallbacks,
    BasicFragment.BasicCallbacks {

    private static final String TAG_BASIC_COMPUTER_INSTANCE = "bcomp_fragment";
    private BCompInstance bci;
    private MemoryFragment memoryFragment;
    private KeyboardFragment keyboardFragment;
    private BasicFragment basicFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabAdapter.setup(this);

        FragmentManager fm = getSupportFragmentManager();

        memoryFragment = (MemoryFragment) fm.findFragmentById(R.id.memory_fragment);
        keyboardFragment = (KeyboardFragment) fm.findFragmentById(R.id.keyboard_fragment);
        basicFragment = (BasicFragment) TabAdapter.getAdapter().getItem(TabAdapter.BC_TAB);

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
    }


    /* KeyboardFragment callbacks */

    @Override
    public void updateKeyRegister(int value) {
        bci.updateKeyRegister(value);
    }


    /* BasicFragment callbacks */

    @Override
    public CPU getCPU() {
        return bci.cpu;
    }


    /* BCompInstance callbacks */

    @Override
    public void fillMemory(int[] values) {
        memoryFragment.fillMemory(values);
    }

    @Override
    public void fillKeyboard(int value) {
        keyboardFragment.fillKeyboard(value);
    }
}
