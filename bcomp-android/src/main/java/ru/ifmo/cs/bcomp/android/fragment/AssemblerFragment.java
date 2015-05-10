package ru.ifmo.cs.bcomp.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;
import ru.ifmo.cs.bcomp.android.R;


public class AssemblerFragment extends Fragment {
    private static final int MIN_DELAY_MS = 10;
    private static final int MAX_DELAY_MS = 1000;

    private BCompHolder bCompHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View assemblerView = inflater.inflate(R.layout.assembler_fragment, container, false);

        final SeekBar animationSpeedBar = (SeekBar) assemblerView.findViewById(R.id.animation_speed_bar);
        final TextView delayTextView = (TextView) assemblerView.findViewById(R.id.delay_text_view);
        final String delayMsgFormat = getResources().getString(R.string.delay_msg_format);

        animationSpeedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int delay = Math.max(MIN_DELAY_MS, MAX_DELAY_MS * progress / seekBar.getMax());
                bCompHolder.setTickDelay(delay);
                delayTextView.setText(String.format(delayMsgFormat, delay));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // NOP
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // NOP
            }
        });

        // TODO: synchronize with BCI (<->) and save in Preferences
        // Trigger delayTextView update
        animationSpeedBar.setProgress(1);
        animationSpeedBar.setProgress(0);

        return assemblerView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }
}
