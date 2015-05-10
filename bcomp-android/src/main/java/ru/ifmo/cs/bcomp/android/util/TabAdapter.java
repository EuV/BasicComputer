package ru.ifmo.cs.bcomp.android.util;

import android.os.Bundle;
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
import ru.ifmo.cs.bcomp.android.fragment.YetAnotherTab;

import java.util.HashMap;
import java.util.Map;

public class TabAdapter extends FragmentStatePagerAdapter {
    public static final int BC_TAB = 0;
    public static final int IO_TAB = 1;
    public static final int MP_TAB = 2;
    public static final int ASM_TAB = 3;

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
        switch (index) {

            case BC_TAB: {
                Fragment basicTab = new BasicTab();
                tabReferenceMap.put(index, basicTab);
                return basicTab;
            }

            case IO_TAB: {
                Fragment ioTab = new IOTab();
                tabReferenceMap.put(index, ioTab);
                return ioTab;
            }

            case MP_TAB: {
                YetAnotherTab anotherTab = new YetAnotherTab();
                Bundle args = new Bundle();
                args.putString(YetAnotherTab.NAME, "Работа с МПУ");
                anotherTab.setArguments(args);
                return anotherTab;
            }

            case ASM_TAB: {
                Fragment assemblerTab = new AssemblerTab();
                tabReferenceMap.put(index, assemblerTab);
                return assemblerTab;
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
        return 4;
    }
}
