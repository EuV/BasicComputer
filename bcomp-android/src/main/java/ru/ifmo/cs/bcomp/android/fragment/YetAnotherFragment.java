package ru.ifmo.cs.bcomp.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ru.ifmo.cs.bcomp.android.R;


public class YetAnotherFragment extends Fragment {
    public static final String NAME = "name";
    private TextView fragment_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yet_another_fragment, container, false);
        fragment_description = (TextView) view.findViewById(R.id.fragment_description);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (fragment_description != null && args != null) {
            fragment_description.setText(args.getString(NAME));
        }
    }
}
