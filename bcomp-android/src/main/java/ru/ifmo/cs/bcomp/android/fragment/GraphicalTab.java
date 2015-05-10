package ru.ifmo.cs.bcomp.android.fragment;

import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.android.view.BusView;
import ru.ifmo.cs.bcomp.android.view.RegisterView;
import ru.ifmo.cs.elements.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GraphicalTab extends RootFragment {
    protected List<RegisterView> registerViews = new ArrayList<>();
    protected List<BusView> busViews = new ArrayList<>();

    protected void init(ViewGroup viewsHolder) {
        defaultInit(viewsHolder);
        customInit(viewsHolder);
        updateViews();
    }


    private void defaultInit(ViewGroup viewsHolder) {

        // Find all the default CPU registers available
        for (CPU.Reg reg : CPU.Reg.values()) {
            findAndAddRegister(viewsHolder, cpu.getRegister(reg));
        }

        // Find all the BusViews available
        for (int i = 0; i < viewsHolder.getChildCount(); i++) {
            View view = viewsHolder.getChildAt(i);
            if (view.getClass() == BusView.class) {
                busViews.add((BusView) view);
                bCompHolder.registerNewSignals(((BusView) view).getSignals());
            }
        }
    }


    protected void customInit(ViewGroup viewsHolder) {
        // Implement in successor
    }


    protected void findAndAddRegister(ViewGroup viewsHolder, Register register) {
        RegisterView registerView = (RegisterView) viewsHolder.findViewWithTag(register.name);
        if (registerView == null) return;

        registerView.linkWithRegister(register);
        registerViews.add(registerView);
    }


    public void updateViews() {
        defaultUpdateViews();
        customUpdateViews();
    }


    private void defaultUpdateViews() {
        for (RegisterView registerView : registerViews) {
            registerView.update();
        }

        Set<ControlSignal> openSignals = bCompHolder.getOpenSignals();
        for (BusView busView : busViews) {
            for (ControlSignal busSignal : busView.getSignals()) {
                if (openSignals.contains(busSignal)) {
                    busView.activate();
                    break;
                } else {
                    busView.deactivate();
                }
            }
        }
    }


    protected void customUpdateViews() {
        // Implement in successor
    }
}
