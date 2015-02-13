package ru.ifmo.cs.bcomp.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends ActionBarActivity implements KeyboardFragment.OnKeyboardEventListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        RecyclerView memoryView = (RecyclerView) findViewById(R.id.memory_recycler_view);
        memoryView.setHasFixedSize(true);
        memoryView.setLayoutManager(new LinearLayoutManager(this));


        String[] rows = new String[2048];
        Random random = new Random();
        for (int i = 0; i < rows.length; i++) {
            rows[i] = String.format("%03X | %04X", i, random.nextInt(65535));
        }

        memoryView.setAdapter(new MemoryAdapter(rows));
    }

    @Override
    public void onKeyboardPressed() {
        Toast.makeText(this, "Keyboard register pressed!", Toast.LENGTH_SHORT).show();
    }
}
