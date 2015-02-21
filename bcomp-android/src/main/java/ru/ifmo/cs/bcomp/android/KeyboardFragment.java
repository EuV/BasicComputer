package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;


public class KeyboardFragment extends Fragment {

    public interface KeyboardCallbacks {
        void onKeyboardPressed();
    }

    public static final int HEX_SYMBOL_REQUEST = 0;
    public static final String HEX_SYMBOL_VALUE = "hex_symbol_value";
    public static final String HEX_SYMBOL_PRESSED_TAG = "hex_symbol_tag";

    private KeyboardCallbacks callbacks;
    private View keyboardView;
    private String hexSymbolPressedTag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            hexSymbolPressedTag = savedInstanceState.getString(HEX_SYMBOL_PRESSED_TAG);
        }

        keyboardView = inflater.inflate(R.layout.keyboard_fragment, container, false);

        keyboardView.findViewById(R.id.ok_keyboard_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onKeyboardPressed();
            }
        });

        HexSymbolOnClickListener listener = new HexSymbolOnClickListener();
        keyboardView.findViewById(R.id.keyboard_hex_0).setOnClickListener(listener);
        keyboardView.findViewById(R.id.keyboard_hex_1).setOnClickListener(listener);
        keyboardView.findViewById(R.id.keyboard_hex_2).setOnClickListener(listener);
        keyboardView.findViewById(R.id.keyboard_hex_3).setOnClickListener(listener);

        return keyboardView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (KeyboardCallbacks) activity;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(HEX_SYMBOL_PRESSED_TAG, hexSymbolPressedTag);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (hexSymbolPressedTag == null) return;

        if (requestCode == HEX_SYMBOL_REQUEST && resultCode == Activity.RESULT_OK) {

            View symbolView = null;
            switch (hexSymbolPressedTag) {
                case "hex_0":
                    symbolView = keyboardView.findViewById(R.id.keyboard_hex_0);
                    break;
                case "hex_1":
                    symbolView = keyboardView.findViewById(R.id.keyboard_hex_1);
                    break;
                case "hex_2":
                    symbolView = keyboardView.findViewById(R.id.keyboard_hex_2);
                    break;
                case "hex_3":
                    symbolView = keyboardView.findViewById(R.id.keyboard_hex_3);
                    break;
            }

            if (symbolView != null) {
                ((TextView) symbolView).setText(String.format("%X", data.getIntExtra(HEX_SYMBOL_VALUE, 0)));
            }
        }

        hexSymbolPressedTag = null;
    }


    protected class HexSymbolOnClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (hexSymbolPressedTag != null) return;
            hexSymbolPressedTag = (String) view.getTag();
            Intent intent = new Intent(getActivity(), KeyboardPopupActivity.class);
            startActivityForResult(intent, HEX_SYMBOL_REQUEST);
        }
    }
}
