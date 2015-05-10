package ru.ifmo.cs.bcomp.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;

public class InputRegisterView extends RegisterView {
    protected BCompHolder bCompHolder;

    public InputRegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        bCompHolder = (BCompHolder) context;

        headerView.setBackgroundColor(getResources().getColor(R.color.register_value));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                bCompHolder.switchKeyboard(register);
            }
        });
    }
}
