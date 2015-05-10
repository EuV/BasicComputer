package ru.ifmo.cs.bcomp.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.android.KeyboardPopupActivity;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;
import ru.ifmo.cs.bcomp.android.util.TabAdapter;
import ru.ifmo.cs.elements.Register;

import static ru.ifmo.cs.bcomp.CPU.Reg.KEY;


public class KeyboardFragment extends RootFragment {
    public static final int HEX_SYMBOL_REQUEST = 0;
    public static final int DEVICE_NAME_INDEX = 3;
    public static final int CLOSE_BUTTON_INDEX = 2;
    public static final String CLOSE_BUTTON_SYMBOL = "╳";
    public static final String HEX_SYMBOL_VALUE = "hex_symbol_value";
    public static final String HEX_SYMBOL_PRESSED_INDEX = "hex_symbol_tag";

    private Integer hexSymbolPressedIndex = -1;
    private Button[] hexSymbols;
    private TextView binaryView;
    private Register linkedRegister;
    private Register keyRegister;
    private ImageButton setAddressButton;
    private ImageButton writeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        keyRegister = cpu.getRegister(KEY);

        if (savedInstanceState == null) {
            linkedRegister = keyRegister;
        } else {
            hexSymbolPressedIndex = savedInstanceState.getInt(HEX_SYMBOL_PRESSED_INDEX);
            linkedRegister = bCompHolder.getKeyboardLinkedRegister();
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

        setAddressButton = (ImageButton) keyboardView.findViewById(R.id.image_button_address);
        setAddressButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.startSetAddr();
            }
        });

        writeButton = (ImageButton) keyboardView.findViewById(R.id.image_button_write);
        writeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                cpu.startWrite();
            }
        });

        fillKeyboard();

        return keyboardView;
    }


    public void switchKeyboard(Register register) {
        linkedRegister = register;
        fillKeyboard();
    }


    private void fillKeyboard() {
        boolean isKeyReg = (linkedRegister == keyRegister);

        setButtonsClickable(isKeyReg);

        if (!isKeyReg) {
            hexSymbols[DEVICE_NAME_INDEX].setText(linkedRegister.name.substring(3));
            hexSymbols[CLOSE_BUTTON_INDEX].setText(CLOSE_BUTTON_SYMBOL);
        }

        int symbolsCount = (isKeyReg) ? 4 : 2;
        String hexValue = Utils.toHex(linkedRegister.getValue(), linkedRegister.getWidth());
        for (int i = 0; i < symbolsCount; i++) {
            hexSymbols[i].setText(Character.toString(hexValue.charAt(hexValue.length() - 1 - i)));
        }

        binaryView.setText(Utils.toBinary(linkedRegister.getValue(), linkedRegister.getWidth()));
    }


    private void setButtonsClickable(boolean isClickable) {
        hexSymbols[DEVICE_NAME_INDEX].setClickable(isClickable);
        setAddressButton.setClickable(isClickable);
        writeButton.setClickable(isClickable);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(HEX_SYMBOL_PRESSED_INDEX, hexSymbolPressedIndex);
        bCompHolder.setKeyboardLinkedRegister(linkedRegister);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (hexSymbolPressedIndex == -1) return;

        if (requestCode == HEX_SYMBOL_REQUEST && resultCode == Activity.RESULT_OK) {
            linkedRegister.setValue(data.getIntExtra(HEX_SYMBOL_VALUE, 0), hexSymbolPressedIndex * 4, 4);
            fillKeyboard();

            if (linkedRegister != keyRegister) {
                bCompHolder.updateTab(TabAdapter.IO_TAB);
            }
        }

        hexSymbolPressedIndex = -1;
    }


    private class HexSymbolOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (hexSymbolPressedIndex != -1) return;
            BCompVibrator.vibrate();

            int buttonIndex = Integer.parseInt((String) view.getTag());
            if ((buttonIndex == CLOSE_BUTTON_INDEX) && (linkedRegister != keyRegister)) {
                switchKeyboard(keyRegister);
                return;
            }

            hexSymbolPressedIndex = buttonIndex;
            Intent intent = new Intent(getActivity(), KeyboardPopupActivity.class);
            startActivityForResult(intent, HEX_SYMBOL_REQUEST);
        }
    }
}
