package ru.ifmo.cs.bcomp.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;
import ru.ifmo.cs.bcomp.android.view.BusView;
import ru.ifmo.cs.bcomp.android.view.RegisterView;
import ru.ifmo.cs.bcomp.android.view.StatelessBusView;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class IOFragment extends RootFragment {
    private List<RegisterView> registerViews = new ArrayList<>();
    private List<BusView> busViews = new ArrayList<>();
    private List<StatelessBusView> statelessBusViews = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View ioView = inflater.inflate(R.layout.io_fragment, container, false);

        // Default registers
        for (CPU.Reg reg : CPU.Reg.values()) {
            Register register = cpu.getRegister(reg);
            RegisterView registerView = (RegisterView) ioView.findViewWithTag(register.name);
            if (registerView == null) continue;

            registerView.linkWithRegister(register);
            registerViews.add(registerView);
        }

        final IOCtrl[] ioControls = bCompHolder.getIOControls();

        // Device registers
        for (int i = 1; i < ioControls.length; i++) {
            Register register = ioControls[i].getRegData();
            RegisterView registerView = (RegisterView) ioView.findViewWithTag(register.name);
            if (registerView == null) continue;

            registerView.linkWithRegister(register);
            registerViews.add(registerView);
        }


        // Device Button 1-3 callbacks and behavior
        String deviceNameCommon = getResources().getString(R.string.device_common);
        for (int i = 1; i < ioControls.length; i++) {
            final Button deviceButton = (Button) ioView.findViewWithTag(deviceNameCommon + i);
            if (deviceButton == null) continue;

            // Enable/disable button due to SETFLAG event
            ioControls[i].addDestination(IOCtrl.ControlSignal.SETFLAG, new DataDestination() {
                @Override
                public void setValue(final int value) {
                    ((Activity) bCompHolder).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deviceButton.setEnabled(value == 0);
                        }
                    });

                }
            });

            // Enable/disable button while fragment (re)creation
            deviceButton.setEnabled(ioControls[i].getFlag() == 0);

            // Fire SETFLAG event when button is clicked
            final int index = i;
            deviceButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BCompVibrator.vibrate();
                    ioControls[index].setFlag();
                    updateViews();
                }
            });
        }

        // Default buses
        ViewGroup viewsHolder = (ViewGroup) ioView.findViewById(R.id.io_views_holder);
        for (int i = 0; i < viewsHolder.getChildCount(); i++) {
            View view = viewsHolder.getChildAt(i);
            if (view.getClass() == BusView.class) {
                busViews.add((BusView) view);
                bCompHolder.registerNewSignals(((BusView) view).getSignals());
            }
        }

        // Stateless buses (they can't be activated/deactivated using (open-)ControlSignal mechanism)
        for (int i = 0; i < viewsHolder.getChildCount(); i++) {
            View view = viewsHolder.getChildAt(i);
            if (view.getClass() == StatelessBusView.class) {
                StatelessBusView busView = (StatelessBusView) view;
                String busTag = busView.getTag().toString();
                for (int j = 1; j < ioControls.length; j++) {
                    if (busTag.contains("" + j)) {
                        busView.addIOCtrl(ioControls[j]);
                    }
                }
                statelessBusViews.add(busView);
            }
        }

        updateViews();

        return ioView;
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

        for (StatelessBusView statelessBusView : statelessBusViews) {
            statelessBusView.redraw();
        }
    }
}
