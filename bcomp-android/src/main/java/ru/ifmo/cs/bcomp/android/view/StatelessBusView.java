package ru.ifmo.cs.bcomp.android.view;

import android.content.Context;
import android.util.AttributeSet;
import ru.ifmo.cs.bcomp.IOCtrl;

import java.util.ArrayList;
import java.util.List;

public class StatelessBusView extends BusView {
    private List<IOCtrl> ioControls = new ArrayList<>();

    public StatelessBusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void addIOCtrl(IOCtrl ioCtrl) {
        ioControls.add(ioCtrl);
    }


    public void redraw() {
        for (IOCtrl ioControl : ioControls) {
            if (ioControl.getFlag() == 1) {
                activate();
                break;
            } else {
                deactivate();
            }
        }
    }
}
