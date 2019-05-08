package com.baznas.badung;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapterZakatMal extends FragmentStatePagerAdapter {

    public PagerAdapterZakatMal(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new zakatmalform();
        } else if (position == 1) {
            fragment = new zakatmalscan();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Baznas";
        } else if (position == 1) {
            title = "Scan QR Code";
        }
        return title;
    }
}
