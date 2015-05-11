package ru.ifmo.cs.bcomp.android.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import ru.ifmo.cs.bcomp.android.MainActivity;
import ru.ifmo.cs.bcomp.android.R;
import ru.ifmo.cs.bcomp.android.fragment.AssemblerTab;
import ru.ifmo.cs.bcomp.android.fragment.BasicTab;
import ru.ifmo.cs.bcomp.android.fragment.IOTab;
import ru.ifmo.cs.bcomp.android.fragment.MPTab;

import java.util.HashMap;
import java.util.Map;

public class TabAdapter extends FragmentStatePagerAdapter {
    public static final int BC_TAB = 0;
    public static final int IO_TAB = 1;
    public static final int MP_TAB = 2;
    public static final int ASM_TAB = 3;

    private static final Class TABS[] = new Class[4];

    static {
        TABS[BC_TAB] = BasicTab.class;
        TABS[IO_TAB] = IOTab.class;
        TABS[MP_TAB] = MPTab.class;
        TABS[ASM_TAB] = AssemblerTab.class;
    }

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
    }


    public Fragment getFragment(int tab) {
        return tabReferenceMap.get(tab);
    }


    @Override
    public Fragment getItem(int index) {
        try {
            Fragment fragment = (Fragment) TABS[index].newInstance();
            tabReferenceMap.put(index, fragment);
            return fragment;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
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
        return 4;
    }
}
