package com.linmama.dinning.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/2/26
 */
public class CommonActivity extends AppCompatActivity {
    private static Fragment mInstancefragment;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FrameLayout layout = new FrameLayout(this);
        layout.setId((int) (Math.random() * 1000000));
        setContentView(layout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Bundle args = getIntent().getExtras();
        try {
            String fragmentClassName = args.getString(BundleKey.FRAGMENT_CLASS);
            Fragment fragment;
            if (mInstancefragment == null)
                fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            else
                fragment = mInstancefragment;
            Bundle argument = args.getBundle(BundleKey.FRAGMENT_ARGS);
            if (argument != null) fragment.setArguments(argument);
            BaseFragmentManager.addFragmentNotBackStack(getSupportFragmentManager(), fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void start(Activity from, Class<?> fragmentClass, Bundle args) {
        if (from == null || from.isFinishing()) return;
        Intent intent = new Intent(from, CommonActivity.class);
        intent.putExtra(BundleKey.FRAGMENT_CLASS, fragmentClass.getCanonicalName());
        intent.putExtra(BundleKey.FRAGMENT_ARGS, args);
        from.startActivity(intent);
    }
}
