package ru.ifmo.cs.bcomp.android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class TabAdapter extends FragmentStatePagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    public static void setup(MainActivity activity) {
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.tab_view_pager);
        viewPager.setAdapter(new TabAdapter(activity.getSupportFragmentManager()));

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return;
        }

        adjustViewPager(activity, viewPager);
    }


    protected static void adjustViewPager(MainActivity activity, final ViewPager viewPager) {
        final View contentStub = activity.findViewById(R.id.tab_content_stub);
        contentStub.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewGroup.LayoutParams params = viewPager.getLayoutParams();
                    params.height = contentStub.getHeight();
                    viewPager.setLayoutParams(params);
                }
            });
    }


    @Override
    public Fragment getItem(int tab) {
        switch (tab) {

            case 0: {
                return new BasicFragment();
            }

            case 1:
            case 2: {
                YetAnotherFragment fragment = new YetAnotherFragment();
                Bundle args = new Bundle();
                args.putString(YetAnotherFragment.NAME, "Работа с " + ((tab == 1) ? "ВУ" : "МПУ"));
                fragment.setArguments(args);
                return fragment;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
