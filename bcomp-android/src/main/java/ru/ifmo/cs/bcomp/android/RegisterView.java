package ru.ifmo.cs.bcomp.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.elements.Register;

public class RegisterView extends TextView {
    private static final Typeface REGISTER_TYPEFACE = Typeface.create("Courier New", Typeface.NORMAL);

    private boolean fullView;
    private Register register;

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setTypeface(REGISTER_TYPEFACE);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RegisterView, 0, 0);
        try {
            fullView = attributes.getBoolean(R.styleable.RegisterView_full_view, true);
        } finally {
            attributes.recycle();
        }

        if (isInEditMode()) {
            setText(getTag().toString() + ((fullView) ? ": 1010 1111 0000 1111" : ": ABCD"));
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
