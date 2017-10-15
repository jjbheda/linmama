package com.xcxid.dinning.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xcxid.dinning.R;

/**
 * Created by jingkang on 15/9/21.
 */
public class ActivityUtils {
    private ActivityUtils() {
        throw new AssertionError();
    }

    /**
     * the activity to start-standard
     *
     * @param context
     * @param clazz
     */
    public static void startActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * the activity to start-customed
     *
     * @param context
     * @param clazz
     * @param flags
     */
    public static void startActivity(Context context, Class<?> clazz, int flags) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(flags);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * the activity to start with data
     *
     * @param context
     * @param clazz
     * @param data
     */
    public static void startActivity(Context context, Class<?> clazz, Bundle data, int animIn, int animOut) {
        Intent intent = new Intent(context, clazz);
        intent.putExtras(data);
        context.startActivity(intent);
        if (animIn == 0)
            animIn = R.anim.push_left_in;
        if (animOut == 0)
            animOut = R.anim.push_left_out;
        ((Activity) context).overridePendingTransition(animIn, animOut);
    }

    /**
     * the activity to start with data
     *
     * @param context
     * @param clazz
     * @param data
     */
    public static void startActivity(Context context, Class<?> clazz, Bundle data) {
        Intent intent = new Intent(context, clazz);
        intent.putExtras(data);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * the activity to start with data
     *
     * @param act
     * @param clazz
     * @param requestCode
     */
    public static void startActivityForResult(Activity act, Class<?> clazz, int requestCode) {
        Intent intent = new Intent(act, clazz);
        act.startActivityForResult(intent, requestCode);
        act.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * the activity to start with data
     *
     * @param act
     * @param clazz
     * @param data
     * @param requestCode
     */
    public static void startActivityForResult(Activity act, Class<?> clazz, Bundle data, int requestCode) {
        Intent intent = new Intent(act, clazz);
        intent.putExtras(data);
        act.startActivityForResult(intent, requestCode);
        act.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * the Fragment to start with data
     *
     * @param fragment
     * @param clazz
     * @param data
     * @param requestCode
     */
    public static void startActivityForResult(Fragment fragment, Class<?> clazz, Bundle data, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), clazz);
        intent.putExtras(data);
        fragment.startActivityForResult(intent, requestCode);
//        fragment.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * the Fragment to start for result
     *
     * @param fragment
     * @param clazz
     * @param requestCode
     */
    public static void startActivityForResult(Fragment fragment, Class<?> clazz, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), clazz);
        fragment.startActivityForResult(intent, requestCode);
//        fragment.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
