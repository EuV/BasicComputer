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

import java.util.HashMap;
import java.util.Map;

public class TabAdapter extends FragmentStatePagerAdapter {
    public static final int BC_TAB = 0;
    public static final int IO_TAB = 1;
    public static final int MP_TAB = 2;

    private static TabAdapter adapter;

    Map<Integer, Fragment> tabReferenceMap = new HashMap<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    public static TabAdapter getAdapter() {
        return adapter;
    }


    public static void setup(MainActivity activity) {
        adapter = new TabAdapter(activity.getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.tab_view_pager);
        viewPager.setAdapter(adapter);

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


    public Fragment getFragment(int tab) {
        return tabReferenceMap.get(tab);
    }


    @Override
    public Fragment getItem(int index) {
        switch (index) {

            case BC_TAB: {
                Fragment basicFragment = new BasicFragment();
                tabReferenceMap.put(index, basicFragment);
                return basicFragment;
            }

            case IO_TAB:
            case MP_TAB: {
                YetAnotherFragment fragment = new YetAnotherFragment();
                Bundle args = new Bundle();
                args.putString(YetAnotherFragment.NAME, "Работа с " + ((index == 1) ? "ВУ" : "МПУ"));
                fragment.setArguments(args);
                return fragment;
            }
        }

        return null;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        tabReferenceMap.put(position, fragment);
        return fragment;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        tabReferenceMap.remove(position);
    }


    @Override
    public int getCount() {
        return 3;
    }
}
