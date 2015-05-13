package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import ru.ifmo.cs.bcomp.*;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.Register;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BCompInstance extends Fragment {
    private static final long EXTRA_UPDATES_DELAY_MS = 500;

    public interface BCompHolder {
        CPU getCPU();
        void updateTabs();
        void updateTab(int index);
        void updateMemory();
        void updateKeyboard();
        void registerNewSignals(Set<ControlSignal> signals);
        Set<ControlSignal> getOpenSignals();
        void setTickDelay(long tickDelay);
        IOCtrl[] getIOControls();
        void switchKeyboard(Register register);
        void setKeyboardLinkedRegister(Register register);
        Register getKeyboardLinkedRegister();
        boolean isInputToControlUnit();
        void setInputToControlUnit(boolean isInputToControlUnit);
        void setCompilationFlag(boolean running);
    }

    private class SignalHandler implements DataDestination {
        private final ControlSignal signal;

        public SignalHandler(ControlSignal signal) {
            this.signal = signal;
        }

        @Override
        public void setValue(int ignored) {
            synchronized (openSignals) {
                openSignals.add(signal);
            }
        }
    }

    private BCompHolder bCompHolder;
    private final BasicComp bcomp;
    private final CPU cpu;
    private final HashSet<ControlSignal> openSignals = new HashSet<>();
    private final Set<ControlSignal> registeredSignals = new HashSet<>();
    private Register keyboardLinkedRegister;
    private boolean isInputToControlUnit = false;
    private boolean compilationIsRunning = false;

    private long tickDelay = 10;

    public BCompInstance() throws Exception {
        bcomp = new BasicComp(MicroPrograms.getMicroProgram(MicroPrograms.DEFAULT_MICROPROGRAM));

        // Perform views updating based on appropriate events but don't animate
        // app at all in case of zero tick delay due to overloading of the CPU
        bcomp.addDestination(ControlSignal.MEMORY_READ, new DataDestination() {
            @Override
            public void setValue(int ignored) {
                if (bCompHolder != null && tickDelay != 0) {
                    bCompHolder.updateMemory();
                }
            }
        });
        bcomp.addDestination(ControlSignal.MEMORY_WRITE, new DataDestination() {
            @Override
            public void setValue(int ignored) {
                if (bCompHolder != null && tickDelay != 0) {
                    bCompHolder.updateMemory();
                }
            }
        });

        cpu = bcomp.getCPU();
        cpu.setTickStartListener(new Runnable() {
            @Override
            public void run() {
                synchronized (openSignals) {
                    openSignals.clear();
                }
            }
        });
        cpu.setTickFinishListener(new Runnable() {
            @Override
            public void run() {
                if (bCompHolder != null && tickDelay != 0) {
                    bCompHolder.updateTabs();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(tickDelay);
                } catch (InterruptedException ignored) {
                }
            }
        });

        // In case of zero tick delay update views when BComp program stops
        cpu.setCPUStopListener(new Runnable() {
            @Override
            public void run() {
                if (bCompHolder != null && tickDelay == 0 && !compilationIsRunning) {
                    bCompHolder.updateMemory();
                    bCompHolder.updateTabs();
                }
            }
        });

        // If tick delay is equal to zero and BComp program has no halt command,
        // a user won't se any UI updates, so it's needed to add extra updates to improve UX
        new AsyncTask<Void, Void, Void>() {
            @SuppressWarnings("InfiniteLoopStatement")
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    while (true) {
                        TimeUnit.MILLISECONDS.sleep(EXTRA_UPDATES_DELAY_MS);
                        if (bCompHolder != null && tickDelay == 0 && cpu.isRunning() && !compilationIsRunning) {
                            bCompHolder.updateMemory();
                            bCompHolder.updateTabs();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }


    public CPU getCPU() {
        return cpu;
    }


    public void registerNewSignals(Set<ControlSignal> signals) {
        for (ControlSignal signal : signals) {
            if (registeredSignals.contains(signal)) continue;
            bcomp.addDestination(signal, new SignalHandler(signal));
            registeredSignals.add(signal);
        }
    }


    // Cloning (and therefore synchronization) is needed because views updating may occurs at any time.
    // Since views drawing takes a lot of time and Sets pass by reference,
    // openSignals Set could become inconsistent during bus updating.
    @SuppressWarnings("unchecked")
    public Set<ControlSignal> getOpenSignals() {
        synchronized (openSignals) {
            return (HashSet<ControlSignal>) openSignals.clone();
        }
    }


    public void setTickDelay(long tickDelay) {
        this.tickDelay = tickDelay;
    }


    public IOCtrl[] getIOControls() {
        return bcomp.getIOCtrls();
    }


    public void setKeyboardLinkedRegister(Register register) {
        keyboardLinkedRegister = register;
    }


    public Register getKeyboardLinkedRegister() {
        return keyboardLinkedRegister;
    }


    public boolean isInputToControlUnit() {
        return isInputToControlUnit;
    }


    public void setInputToControlUnit(boolean isInputToControlUnit) {
        this.isInputToControlUnit = isInputToControlUnit;
    }


    public void setCompilationFlag(boolean running) {
        compilationIsRunning = running;
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
