package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements KeyboardFragment.OnKeyboardEventListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onKeyboardPressed() {
        Toast.makeText(this, "Keyboard register pressed!", Toast.LENGTH_SHORT).show();
    }
}
