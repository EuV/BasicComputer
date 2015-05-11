package ru.ifmo.cs.bcomp.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.android.R;

public class MPTab extends GraphicalTab {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.mp_fragment, container, false);

        init((ViewGroup) rootView.findViewById(R.id.mp_views_holder));

        return rootView;
    }


    @Override
    protected void customInit(ViewGroup viewsHolder) {
    }


    @Override
    protected void customUpdateViews() {
    }
}
