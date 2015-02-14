package ru.ifmo.cs.bcomp.android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements KeyboardFragment.OnKeyboardEventListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupViewPager();
    }

    @Override
    public void onKeyboardPressed() {
        Toast.makeText(this, "Keyboard register pressed!", Toast.LENGTH_SHORT).show();
    }


    protected void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.tab_view_pager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
             return;
        }

        final View contentStub = findViewById(R.id.tab_content_stub);
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


    protected class TabAdapter extends FragmentStatePagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int tab) {
            return new BasicFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
