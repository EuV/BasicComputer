package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements BCompInstance.BCompCallbacks, KeyboardFragment.KeyboardCallbacks {

    private static final String TAG_BASIC_COMPUTER_INSTANCE = "bcomp_fragment";
    private BCompInstance bci;
    private MemoryFragment memory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabAdapter.setup(this);

        FragmentManager fm = getSupportFragmentManager();

        memory = (MemoryFragment) fm.findFragmentById(R.id.memory_fragment);

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
    public void onKeyboardPressed() {
        Toast.makeText(this, "Keyboard register pressed!", Toast.LENGTH_SHORT).show();
    }


    /* BCompInstance callbacks */

    @Override
    public void fillMemory(int[] values) {
        memory.fillMemory(values);
    }
}
