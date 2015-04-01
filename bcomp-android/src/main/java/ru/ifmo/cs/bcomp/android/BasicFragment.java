package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.android.BCompInstance.BCompHolder;
import ru.ifmo.cs.elements.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class BasicFragment extends Fragment {
    private BCompHolder bCompHolder;
    private CPU cpu;
    private RunningCycleView runningCycleView;
    private List<RegisterView> registerViews;
    private List<BusView> busViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (cpu == null) {
            cpu = bCompHolder.getCPU();
        }

        View basicView = inflater.inflate(R.layout.basic_fragment, container, false);

        runningCycleView = (RunningCycleView) basicView.findViewById(R.id.running_cycle);

        registerViews = new ArrayList<>();

        // TODO: find registers like buses?
        for (CPU.Reg reg : CPU.Reg.values()) {
            Register register = cpu.getRegister(reg);
            RegisterView registerView = (RegisterView) basicView.findViewWithTag(register.name);
            if (registerView == null) continue;

            registerView.linkWithRegister(register);
            registerViews.add(registerView);
        }

        busViews = new ArrayList<>();

        ViewGroup viewsHolder = (ViewGroup) basicView.findViewById(R.id.basic_views_holder);
        for (int i = 0; i < viewsHolder.getChildCount(); i++) {
            View view = viewsHolder.getChildAt(i);
            if (view instanceof BusView) {
                busViews.add((BusView) view);
                bCompHolder.registerNewSignals(((BusView) view).getSignals());
            }
        }

        updateViews();

        return basicView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bCompHolder = (BCompHolder) activity;
    }


    public void updateViews() {
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

        runningCycleView.update(cpu);
    }
}
