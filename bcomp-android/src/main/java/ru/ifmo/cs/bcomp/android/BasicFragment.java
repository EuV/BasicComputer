package ru.ifmo.cs.bcomp.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.elements.Register;

import java.util.ArrayList;
import java.util.List;


public class BasicFragment extends Fragment {

    public interface BasicCallbacks {
        CPU getCPU();
    }

    private BasicCallbacks callbacks;
    private CPU cpu;
    private View basicView;
    private List<RegisterView> registerViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (cpu == null) {
            cpu = callbacks.getCPU();
        }

        basicView = inflater.inflate(R.layout.basic_fragment, container, false);

        registerViews = new ArrayList<>();

        for (CPU.Reg reg : CPU.Reg.values()) {
            Register register = cpu.getRegister(reg);
            RegisterView registerView = (RegisterView) basicView.findViewWithTag(register.name);
            if (registerView == null) continue;

            registerView.linkWithRegister(register);
            registerViews.add(registerView);
        }

        updateRegisterViews();

        return basicView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (BasicCallbacks) activity;
    }


    public void updateRegisterViews() {
        for (RegisterView view : registerViews) {
            view.update();
        }
    }
}
