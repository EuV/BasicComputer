package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.MicroPrograms;
import ru.ifmo.cs.elements.DataDestination;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BCompInstance extends Fragment {

    public interface BCompHolder {
        CPU getCPU();
        void tickFinished();
        void updateMemory();
        void registerNewSignals(Set<ControlSignal> signals);
        Set<ControlSignal> getOpenSignals();
        void setTickDelay(long tickDelay);
    }

    private class SignalHandler implements DataDestination {
        private final ControlSignal signal;

        public SignalHandler(ControlSignal signal) {
            this.signal = signal;
        }

        @Override
        public void setValue(int ignored) {
            openSignals.add(signal);
        }
    }

    private BCompHolder bCompHolder;
    private final BasicComp bcomp;
    private final Set<ControlSignal> openSignals = new HashSet<>();
    private final Set<ControlSignal> registeredSignals = new HashSet<>();

    public final CPU cpu;

    private long tickDelay = 10;

    public BCompInstance() throws Exception {
        bcomp = new BasicComp(MicroPrograms.getMicroProgram(MicroPrograms.DEFAULT_MICROPROGRAM));
        bcomp.addDestination(ControlSignal.MEMORY_READ, new DataDestination() {
            @Override
            public void setValue(int ignored) {
                if (bCompHolder != null) {
                    // TODO: update only if needed
                    bCompHolder.updateMemory();
                }
            }
        });
        bcomp.addDestination(ControlSignal.MEMORY_WRITE, new DataDestination() {
            @Override
            public void setValue(int ignored) {
                if (bCompHolder != null) {
                    // TODO: update only one cell if possible
                    bCompHolder.updateMemory();
                }
            }
        });

        cpu = bcomp.getCPU();
        cpu.setTickStartListener(new Runnable() {
            @Override
            public void run() {
                openSignals.clear();
            }
        });
        cpu.setTickFinishListener(new Runnable() {
            @Override
            public void run() {
                if (bCompHolder != null) {
                    bCompHolder.tickFinished();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(tickDelay);
                } catch (InterruptedException ignored) {
                }
            }
        });
    }


    public void registerNewSignals(Set<ControlSignal> signals) {
        for (ControlSignal signal : signals) {
            if (registeredSignals.contains(signal)) continue;
            bcomp.addDestination(signal, new SignalHandler(signal));
            registeredSignals.add(signal);
        }
    }


    public Set<ControlSignal> getOpenSignals() {
        return openSignals;
    }


    public void setTickDelay(long tickDelay) {
        this.tickDelay = tickDelay;
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
        bCompHolder = (BCompHolder) activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        bCompHolder = null;
    }
}
