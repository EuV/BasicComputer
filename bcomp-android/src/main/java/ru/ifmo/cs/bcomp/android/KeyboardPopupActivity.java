package ru.ifmo.cs.bcomp.android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import ru.ifmo.cs.bcomp.android.fragment.KeyboardFragment;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;

public class KeyboardPopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout firstRow = new LinearLayout(this);
        for (int i = 0x0; i <= 0x7; i++) {
            firstRow.addView(new HexButton(this, i));
        }

        LinearLayout secondRow = new LinearLayout(this);
        for (int i = 0x8; i <= 0xF; i++) {
            secondRow.addView(new HexButton(this, i));
        }

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(firstRow);
        mainLayout.addView(secondRow);
        setContentView(mainLayout);
    }


    private class HexButton extends Button {
        public HexButton(Context context, final int value) {
            super(context);
            setMinimumWidth(0);
            setWidth(70);
            setPadding(0, 0, 0, 0);
            setText(String.format("%X", value));
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BCompVibrator.vibrate();
                    Intent resultData = new Intent();
                    resultData.putExtra(KeyboardFragment.HEX_SYMBOL_VALUE, value);
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                }
            });
        }
    }
}
