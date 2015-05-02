package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;

import static ru.ifmo.cs.bcomp.CPU.Reg.KEY;


public class KeyboardFragment extends Fragment {
    public static final int HEX_SYMBOL_REQUEST = 0;
    public static final int KEYBOARD_WIDTH = 16;
    public static final String HEX_SYMBOL_VALUE = "hex_symbol_value";
    public static final String HEX_SYMBOL_PRESSED_INDEX = "hex_symbol_tag";

    private BCompHolder bCompHolder;
    private CPU cpu;
    private Integer hexSymbolPressedIndex = -1;
    private Button[] hexSymbols;
    private TextView binaryView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (cpu == null) {
            cpu = bCompHolder.getCPU();
        }

        if (savedInstanceState != null) {
            hexSymbolPressedIndex = savedInstanceState.getInt(HEX_SYMBOL_PRESSED_INDEX);
        }

        View keyboardView = inflater.inflate(R.layout.keyboard_fragment, container, false);

        binaryView = (TextView) keyboardView.findViewById(R.id.keyboard_binary);

        hexSymbols = new Button[]{
            (Button) keyboardView.findViewWithTag("0"),
            (Button) keyboardView.findViewWithTag("1"),
            (Button) keyboardView.findViewWithTag("2"),
            (Button) keyboardView.findViewWithTag("3")
        };

        HexSymbolOnClickListener listener = new HexSymbolOnClickListener();

        for (Button hexSymbol : hexSymbols) {
            hexSymbol.setOnClickListener(listener);
        }

        keyboardView.findViewById(R.id.image_button_address).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.startSetAddr();
            }
        });

        keyboardView.findViewById(R.id.image_button_write).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.startWrite();
            }
        });

        fillKeyboard();

        return keyboardView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(HEX_SYMBOL_PRESSED_INDEX, hexSymbolPressedIndex);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (hexSymbolPressedIndex == -1) return;

        if (requestCode == HEX_SYMBOL_REQUEST && resultCode == Activity.RESULT_OK) {
            hexSymbols[hexSymbolPressedIndex].setText(String.format("%X", data.getIntExtra(HEX_SYMBOL_VALUE, 0)));

            StringBuilder sb = new StringBuilder();
            for (int i = hexSymbols.length - 1; i >= 0; i--) {
                sb.append(hexSymbols[i].getText());
            }
            int regValue = Integer.parseInt(sb.toString(), 16);

            updateBinary(regValue);
            cpu.setRegKey(regValue);
        }

        hexSymbolPressedIndex = -1;
    }


    protected void fillKeyboard() {
        int value = cpu.getRegister(KEY).getValue();
        String hexValue = Utils.toHex(value, KEYBOARD_WIDTH);
        for (int i = 0; i < hexSymbols.length; i++) {
            hexSymbols[i].setText("" + hexValue.charAt(hexValue.length() - 1 - i));
        }

        updateBinary(value);
    }


    protected void updateBinary(int value) {
        binaryView.setText(Utils.toBinary(value, KEYBOARD_WIDTH));
    }


    protected class HexSymbolOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (hexSymbolPressedIndex != -1) return;
            BCompVibrator.vibrate();
            hexSymbolPressedIndex = Integer.parseInt((String) view.getTag());
            Intent intent = new Intent(getActivity(), KeyboardPopupActivity.class);
            startActivityForResult(intent, HEX_SYMBOL_REQUEST);
        }
    }
}
