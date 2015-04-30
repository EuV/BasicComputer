package ru.ifmo.cs.bcomp.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.elements.Register;

public class RegisterView extends LinearLayout {
    protected TextView headerView;
    protected TextView valueView;

    protected boolean fullView;
    protected Register register;

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.register_view, this);
        headerView = (TextView) findViewById(R.id.register_header);
        valueView = (TextView) findViewById(R.id.register_value);

        headerView.setTypeface(Typeface.MONOSPACE);
        valueView.setTypeface(Typeface.MONOSPACE);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RegisterView, 0, 0);
        try {
            fullView = attributes.getBoolean(R.styleable.RegisterView_full_view, true);
        } finally {
            attributes.recycle();
        }

        if (isInEditMode()) {
            headerView.setText(getTag().toString());
            valueView.setText((fullView) ? "1010 1111 0000 1111" : "0");
        }
    }

    public void update() {
        int value = register.getValue();
        int width = register.getWidth();
        if (fullView) {
            headerView.setText(register.fullname + " (" + Utils.toHex(value, width) + ")");
            valueView.setText(Utils.toBinary(value, width));
        } else {
            headerView.setText(register.name);
            valueView.setText(Utils.toHex(value, width));
        }
    }

    public void linkWithRegister(Register register) {
        this.register = register;
    }
}
