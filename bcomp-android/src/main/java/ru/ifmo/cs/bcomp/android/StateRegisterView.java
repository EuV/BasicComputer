package ru.ifmo.cs.bcomp.android;

import android.content.Context;
import android.util.AttributeSet;
import ru.ifmo.cs.bcomp.Utils;

public class StateRegisterView extends RegisterView {

    public StateRegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void update() {
        if (fullView) {
            super.update();
        } else {
            headerView.setText(register.name);
            valueView.setText(Utils.toBinary(register.getValue(), 1));
        }
    }
}
