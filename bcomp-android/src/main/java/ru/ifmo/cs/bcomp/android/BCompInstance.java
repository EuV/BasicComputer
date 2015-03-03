package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MicroPrograms;
import ru.ifmo.cs.elements.Memory;

import static ru.ifmo.cs.bcomp.CPU.Reg.KEY;

public class BCompInstance extends Fragment {

    public interface BCompCallbacks {
        void fillMemory(int[] values);
        void fillKeyboard(int value);
    }

    private BCompCallbacks callbacks;
    private final BasicComp bcomp;

    public final CPU cpu;


    public BCompInstance() throws Exception {
        bcomp = new BasicComp(MicroPrograms.getMicroProgram(MicroPrograms.DEFAULT_MICROPROGRAM));
        cpu = bcomp.getCPU();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        bcomp.startTimer();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (BCompCallbacks) activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        triggerUpdateUI();
    }


    private void triggerUpdateUI() {
        callbacks.fillKeyboard(cpu.getRegister(KEY).getValue());

        Memory memory = cpu.getMemory();
        int[] memoryValues = new int[memory.getSize()];
        for (int i = 0; i < memoryValues.length; i++) {
            memoryValues[i] = memory.getValue(i);
        }
        callbacks.fillMemory(memoryValues);
    }


    public void updateKeyRegister(int value) {
        cpu.setRegKey(value);
    }
}
