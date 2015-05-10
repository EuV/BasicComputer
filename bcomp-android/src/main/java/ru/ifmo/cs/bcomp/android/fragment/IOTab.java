package ru.ifmo.cs.bcomp.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.util.BCompVibrator;
import ru.ifmo.cs.bcomp.android.view.StatelessBusView;
import ru.ifmo.cs.elements.DataDestination;

import java.util.ArrayList;
import java.util.List;

public class IOTab extends GraphicalTab {
    private List<StatelessBusView> statelessBusViews = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.io_fragment, container, false);

        init((ViewGroup) rootView.findViewById(R.id.io_views_holder));

        return rootView;
    }


    @Override
    protected void customInit(ViewGroup viewsHolder) {
        final IOCtrl[] ioControls = bCompHolder.getIOControls();

        // Device registers
        for (int i = 1; i < ioControls.length; i++) {
            findAndAddRegister(viewsHolder, ioControls[i].getRegData());
        }


        // Device Button 1-3 callbacks and behavior
        String deviceNameCommon = getResources().getString(R.string.device_common);
        for (int i = 1; i < ioControls.length; i++) {
            final Button deviceButton = (Button) viewsHolder.findViewWithTag(deviceNameCommon + i);
            if (deviceButton == null) continue;

            // Enable/disable button while fragment (re)creation
            deviceButton.setEnabled(ioControls[i].getFlag() == 0);

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

            // Fire SETFLAG event when button is clicked
            final int index = i;
            deviceButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BCompVibrator.vibrate();
                    ioControls[index].setFlag();
                    customUpdateViews();
                }
            });
        }


        // Stateless buses (they can't be activated/deactivated using (open-)ControlSignal mechanism)
        for (int i = 0; i < viewsHolder.getChildCount(); i++) {
            View view = viewsHolder.getChildAt(i);
            if (view.getClass() != StatelessBusView.class) continue;

            StatelessBusView busView = (StatelessBusView) view;
            for (int j = 1; j < ioControls.length; j++) {
                if (busView.getTag().toString().contains(Integer.toString(j))) {
                    busView.addIOCtrl(ioControls[j]);
                }
            }
            statelessBusViews.add(busView);
        }
    }


    @Override
    protected void customUpdateViews() {
        for (StatelessBusView busView : statelessBusViews) {
            busView.redraw();
        }
    }
}
