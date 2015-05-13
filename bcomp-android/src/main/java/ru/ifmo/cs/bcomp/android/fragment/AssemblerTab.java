package ru.ifmo.cs.bcomp.android.fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import ru.ifmo.cs.bcomp.Assembler;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.AsmDevProgram;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;


public class AssemblerTab extends RootFragment {
    private final long[] delayPeriods = {0, 10, 50, 200, 500, 1000};
    private int tickDelayIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View assemblerView = inflater.inflate(R.layout.assembler_fragment, container, false);

        final SeekBar animationSpeedBar = (SeekBar) assemblerView.findViewById(R.id.animation_speed_bar);
        final TextView delayTextView = (TextView) assemblerView.findViewById(R.id.delay_text_view);
        final String delayMsgFormat = getResources().getString(R.string.delay_msg_format);

        animationSpeedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tickDelayIndex = Math.min(progress, delayPeriods.length - 1);
                bCompHolder.setTickDelay(delayPeriods[tickDelayIndex]);
                delayTextView.setText(String.format(delayMsgFormat, delayPeriods[tickDelayIndex]));
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
        animationSpeedBar.setProgress(2);
        animationSpeedBar.setProgress(1);


        final Assembler assembler = new Assembler(cpu.getInstructionSet());
        final TextView srcView = (TextView) assemblerView.findViewById(R.id.asm_src_view);
        srcView.setText(AsmDevProgram.PROGRAM);
        srcView.setMovementMethod(new ScrollingMovementMethod());

        assemblerView.findViewById(R.id.asm_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();
            }
        });

        assemblerView.findViewById(R.id.asm_compile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BCompVibrator.vibrate();

                long savedDelay = delayPeriods[tickDelayIndex];
                bCompHolder.setTickDelay(0);
                bCompHolder.setCompilationFlag(true);
                boolean savedClockState = cpu.getClockState();
                cpu.setClockState(true);

                try {
                    assembler.compileProgram(srcView.getText().toString());
                    assembler.loadProgram(cpu);
                    showMessage(getResources().getString(R.string.asm_compile_success));
                } catch (Exception e) {
                    showMessage(e.getMessage());
                }

                bCompHolder.updateMemory();
                bCompHolder.updateTabs();
                bCompHolder.updateKeyboard();

                cpu.setClockState(savedClockState);
                bCompHolder.setCompilationFlag(false);
                bCompHolder.setTickDelay(savedDelay);
            }
        });

        return assemblerView;
    }


    private void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
