package ru.ifmo.cs.bcomp.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.android.KeyboardPopupActivity;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;
import ru.ifmo.cs.bcomp.android.util.TabAdapter;
import ru.ifmo.cs.bcomp.android.view.HexButtonListView;
import ru.ifmo.cs.elements.Register;

import static ru.ifmo.cs.bcomp.android.fragment.MemoryFragment.MemoryEvent.UPDATE_LAST_ADDRESS;
import static ru.ifmo.cs.bcomp.android.fragment.MemoryFragment.MemoryEvent.UPDATE_MEMORY;

public class KeyboardFragment extends RootFragment {
    public static final int HEX_BUTTON_REQUEST = 0;
    public static final int DEVICE_NAME_INDEX = 3;
    public static final int CLOSE_BUTTON_INDEX = 2;
    public static final String CLOSE_BUTTON_SYMBOL = "╳";
    public static final String HEX_BUTTON_POSITIONS = "hex_button_positions";
    public static final String HEX_BUTTON_VALUE = "hex_button_value";
    public static final String HEX_BUTTON_PRESSED_INDEX = "hex_button_tag";

    private Integer hexButtonPressedIndex;
    private HexButtonListView[] hexButtons;
    private int[] hexButtonPositions;
    private TextView binaryView;
    private Register linkedRegister;
    private Register keyRegister;
    private ImageButton setAddressButton;
    private ImageButton writeButton;

    private final HexButtonOnItemClickListener onItemClickListener = new HexButtonOnItemClickListener();
    private final HexButtonOnScrollListener onScrollListener = new HexButtonOnScrollListener();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        keyRegister = cpu.getRegister(CPU.Reg.KEY);

        // It might be saved previously but lost due to OutOfMemoryError
        // TODO: Close Keyboard pop-up in this case if exists
        // TODO: Use Parcelable?
        linkedRegister = bCompHolder.getKeyboardLinkedRegister();

        if (savedInstanceState == null || linkedRegister == null) {
            linkedRegister = keyRegister;
            hexButtonPressedIndex = -1;
        } else {
            hexButtonPressedIndex = savedInstanceState.getInt(HEX_BUTTON_PRESSED_INDEX);
            hexButtonPositions = savedInstanceState.getIntArray(HEX_BUTTON_POSITIONS);
        }

        View keyboardView = inflater.inflate(R.layout.keyboard_fragment, container, false);

        binaryView = (TextView) keyboardView.findViewById(R.id.keyboard_binary);

        hexButtons = new HexButtonListView[]{
            (HexButtonListView) keyboardView.findViewWithTag("0"),
            (HexButtonListView) keyboardView.findViewWithTag("1"),
            (HexButtonListView) keyboardView.findViewWithTag("2"),
            (HexButtonListView) keyboardView.findViewWithTag("3")
        };

        for (int i = 0; i < hexButtons.length; i++) {
            hexButtons[i].setOnItemClickListener(onItemClickListener);
            hexButtons[i].setOnScrollListener(onScrollListener);
            if (hexButtonPositions == null) {
                hexButtons[i].setSelection(HexButtonListView.DEFAULT_POSITION);
            } else {
                hexButtons[i].setSelection(hexButtonPositions[i]);
            }
        }

        setAddressButton = (ImageButton) keyboardView.findViewById(R.id.image_button_address);
        setAddressButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                if (bCompHolder.isInputToControlUnit()) {
                    cpu.runSetMAddr();
                    bCompHolder.updateTab(TabAdapter.MP_TAB);
                } else {
                    cpu.startSetAddr();
                }
            }
        });

        writeButton = (ImageButton) keyboardView.findViewById(R.id.image_button_write);
        writeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
                if (bCompHolder.isInputToControlUnit()) {
                    bCompHolder.microMemoryEvent(UPDATE_LAST_ADDRESS);
                    cpu.runMWrite();
                    bCompHolder.microMemoryEvent(UPDATE_MEMORY);
                    bCompHolder.updateTab(TabAdapter.MP_TAB);
                } else {
                    cpu.startWrite();
                }
            }
        });

        // FIXME: HexButtonListView#getCurrentPosition() returns 0 at this time, which
        // FIXME: causes a bug with external devices while screen rotation
        updateKeyboard();

        return keyboardView;
    }


    public void switchKeyboard(Register register) {
        linkedRegister = register;
        updateKeyboard();
    }


    public void updateKeyboard() {
        boolean isKeyReg = (linkedRegister == keyRegister);

        setButtonsClickable(isKeyReg);

        if (!isKeyReg) {
            hexButtons[DEVICE_NAME_INDEX].smoothScrollBy(0, 0);
            hexButtons[DEVICE_NAME_INDEX].setText(linkedRegister.name.substring(3));
            hexButtons[DEVICE_NAME_INDEX].invalidate();

            hexButtons[CLOSE_BUTTON_INDEX].smoothScrollBy(0, 0);
            hexButtons[CLOSE_BUTTON_INDEX].setText(CLOSE_BUTTON_SYMBOL);
            hexButtons[CLOSE_BUTTON_INDEX].invalidate();
        }

        int buttonsCount = (isKeyReg) ? 4 : 2;
        String hexValue = Utils.toHex(linkedRegister.getValue(), linkedRegister.getWidth());
        for (int i = 0; i < buttonsCount; i++) {
            hexButtons[i].scrollToValue(Character.digit(hexValue.charAt(hexValue.length() - 1 - i), 16));
        }

        updateBinaryView();
    }


    private void updateBinaryView() {
        binaryView.setText(Utils.toBinary(linkedRegister.getValue(), linkedRegister.getWidth()));
    }


    private void setButtonsClickable(boolean isClickable) {
        hexButtons[DEVICE_NAME_INDEX].setScrollable(isClickable);
        hexButtons[DEVICE_NAME_INDEX].setOnItemClickListener(isClickable ? onItemClickListener : null);
        hexButtons[CLOSE_BUTTON_INDEX].setScrollable(isClickable);
        setAddressButton.setClickable(isClickable);
        writeButton.setClickable(isClickable);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(HEX_BUTTON_PRESSED_INDEX, hexButtonPressedIndex);
        bCompHolder.setKeyboardLinkedRegister(linkedRegister);

        hexButtonPositions = new int[hexButtons.length];
        for (int i = 0; i < hexButtons.length; i++) {
            hexButtonPositions[i] = hexButtons[i].getCurrentPosition();
        }
        outState.putIntArray(HEX_BUTTON_POSITIONS, hexButtonPositions);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (hexButtonPressedIndex == -1) return;

        if (requestCode == HEX_BUTTON_REQUEST && resultCode == Activity.RESULT_OK) {
            linkedRegister.setValue(data.getIntExtra(HEX_BUTTON_VALUE, 0), hexButtonPressedIndex * 4, 4);
            updateKeyboard();

            if (linkedRegister != keyRegister) {
                bCompHolder.updateTab(TabAdapter.IO_TAB);
            }
        }

        hexButtonPressedIndex = -1;
    }


    private class HexButtonOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (hexButtonPressedIndex != -1) return;
            BCompVibrator.vibrate();

            int buttonIndex = Integer.parseInt(parent.getTag().toString());
            if ((buttonIndex == CLOSE_BUTTON_INDEX) && (linkedRegister != keyRegister)) {
                switchKeyboard(keyRegister);
                return;
            }

            hexButtonPressedIndex = buttonIndex;
            Intent intent = new Intent(getActivity(), KeyboardPopupActivity.class);
            startActivityForResult(intent, HEX_BUTTON_REQUEST);
        }
    }


    private class HexButtonOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                HexButtonListView hexBtnView = (HexButtonListView) view;
                int position = hexBtnView.getCurrentPosition();

                if (hexBtnView.targetPosition != null && hexBtnView.targetPosition != position) {
                    return;
                }

                hexBtnView.smoothScrollToPosition(position);
                hexBtnView.targetPosition = null;

                linkedRegister.setValue(position % 16, Integer.valueOf(view.getTag().toString()) * 4, 4);
                if (linkedRegister != keyRegister) {
                    bCompHolder.updateTab(TabAdapter.IO_TAB);
                }

                updateBinaryView();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }
}
