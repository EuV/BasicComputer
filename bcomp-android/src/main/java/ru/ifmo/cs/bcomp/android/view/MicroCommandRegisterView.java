package ru.ifmo.cs.bcomp.android.view;

import android.content.Context;
import android.util.AttributeSet;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.android.R;

public class MicroCommandRegisterView extends RegisterView {

    public MicroCommandRegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void update() {
        if (fullView) {
            int value = register.getValue();
            int width = register.getWidth();
            String title = getResources().getString(R.string.micro_command_register);
            headerView.setText(title + " (" + Utils.toHex(value, width) + ")");
            valueView.setText(Utils.toBinary(value, width));
        } else {
            super.update();
        }
    }
}
