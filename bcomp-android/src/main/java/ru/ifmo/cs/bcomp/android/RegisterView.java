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

    protected boolean fullView;
    protected Register register;

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
            setText(getTag().toString() + ((fullView) ? "\n1010 1111 0000 1111" : "\n0"));
        }
    }

    public void update() {
        int value = register.getValue();
        int width = register.getWidth();
        if (fullView) {
            setText(register.fullname + " ("
                + Utils.toHex(value, width) + ")\n"
                + Utils.toBinary(value, width));
        } else {
            setText(register.name + "\n" + Utils.toHex(value, width));
        }
    }

    public void linkWithRegister(Register register) {
        this.register = register;
    }
}
