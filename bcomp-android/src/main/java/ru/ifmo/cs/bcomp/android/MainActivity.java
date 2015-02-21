package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements BCompInstance.BCompCallbacks, KeyboardFragment.KeyboardCallbacks {

    private static final String TAG_BASIC_COMPUTER_INSTANCE = "bcomp_fragment";
    private BCompInstance bci;
    private MemoryFragment memory;
    private KeyboardFragment keyboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabAdapter.setup(this);

        FragmentManager fm = getSupportFragmentManager();

        memory = (MemoryFragment) fm.findFragmentById(R.id.memory_fragment);
        keyboard = (KeyboardFragment) fm.findFragmentById(R.id.keyboard_fragment);

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


    /* BCompInstance callbacks */

    @Override
    public void fillMemory(int[] values) {
        memory.fillMemory(values);
    }

    @Override
    public void fillKeyboard(int value) {
        keyboard.fillKeyboard(value);
    }
}
