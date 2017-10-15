package com.xcxid.dinning.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class ViewUtils {

    public static void showSnack(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnack(View v, int resid) {
        Snackbar.make(v, resid, Snackbar.LENGTH_LONG).show();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, int resid) {
        Toast.makeText(context, resid, Toast.LENGTH_LONG).show();
    }

    public static View layoutInflater(Context context, int resource, ViewGroup root) {
        return LayoutInflater.from(context).inflate(resource, root, false);
    }

    public static boolean isListEmpty(List list) {
        boolean isEmpty = false;
        if (null == list || list.size() == 0) {
            isEmpty = true;
        }
        return isEmpty;
    }
}
