package ru.ifmo.cs.bcomp.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.ControlSignal;

import java.util.HashSet;
import java.util.Set;

public class BusView extends TextView {
    private final Set<ControlSignal> signals = new HashSet<>();

    public BusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        String[] signalNames = getTag().toString().split(" ");
        for (String signalName : signalNames) {
            signals.add(ControlSignal.valueOf(signalName));
        }
    }


    public void mark(boolean active) {
        setText(active ? "Active" : "Inactive");
    }


    public Set<ControlSignal> getSignals() {
        return signals;
    }
}
