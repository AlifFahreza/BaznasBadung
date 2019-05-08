package com.baznas.badung;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapterZakatFitrah extends FragmentStatePagerAdapter {

    public PagerAdapterZakatFitrah(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new zakatfitrahform();
        } else if (position == 1) {
            return new zakatfitrahscan();
        }
        throw new IllegalStateException("Position not valid");
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0 || position % 2 == 0) {
            return "Baznas";
        } else if (position % 2 == 1) {
            return "Scan QR Code";
        }
        throw new IllegalStateException("Position not valid");
    }
}
