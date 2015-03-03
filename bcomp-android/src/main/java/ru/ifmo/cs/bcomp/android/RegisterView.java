package ru.ifmo.cs.bcomp.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.elements.Register;

public class RegisterView extends TextView {
    private boolean fullView;
    private Register register;

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RegisterView, 0, 0);
        try {
            fullView = attributes.getBoolean(R.styleable.RegisterView_full_view, true);
        } finally {
            attributes.recycle();
        }
    }

    public void update() {
        if (fullView) {
            setText(register.fullname + ":\n" + Utils.toBinary(register.getValue(), register.getWidth()));
        } else {
            setText(register.name + ": " + Utils.toHex(register.getValue(), register.getWidth()));
        }
    }

    public void linkWithRegister(Register register) {
        this.register = register;
    }
}
