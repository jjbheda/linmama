package com.linmama.dinning.base;

import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class BaseFragmentManager {
    public static void addFragment(FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(Window.ID_ANDROID_CONTENT, fragment, fragment.getClass().getName());

        transaction.addToBackStack(fragment.getClass().getName());
        transaction.show(fragment).commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager fm, Fragment fragment, int id) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(id, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager fm, Fragment fragment, String tag) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(Window.ID_ANDROID_CONTENT, fragment, tag);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentNotBackStack(FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(android.R.id.content, fragment,fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentNotBackStack(FragmentManager fm, Fragment fragment, int id) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(id, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void removeFragment(FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }

}
