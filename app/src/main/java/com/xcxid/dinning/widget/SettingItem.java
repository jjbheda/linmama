package com.xcxid.dinning.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcxid.dinning.R;

/**
 * Created by jingkang on 2017/3/4
 */

public class SettingItem extends RelativeLayout {
    private TextView title;
    private TextView subTitle;
    private ImageView leftIcon;
    private ImageView rightIcon;

    public SettingItem(Context context) {
        super(context, null);
        init(context);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingItem);
        Drawable _leftIcon = ta.getDrawable(R.styleable.SettingItem_setLeftIcon);
        leftIcon.setImageDrawable(_leftIcon);
        String _title = ta.getString(R.styleable.SettingItem_setTitle);
        title.setText(_title);
        String _subTitle = ta.getString(R.styleable.SettingItem_setSubTitle);
        subTitle.setText(_subTitle);
        Drawable _rightIcon = ta.getDrawable(R.styleable.SettingItem_setRightIcon);
        rightIcon.setImageDrawable(_rightIcon);
        ta.recycle();
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setSubTitle(String subTitle) {
        this.subTitle.setText(subTitle);
    }

    public void setSubTitle(int subTitle) {
        this.subTitle.setText(subTitle);
    }

    public void setRightIcon(int resId) {
        this.rightIcon.setImageResource(resId);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.setting_item, this);
        title = (TextView) this.findViewById(R.id.title);
        subTitle = (TextView) this.findViewById(R.id.subTitle);
        leftIcon = (ImageView) this.findViewById(R.id.leftIcon);
        rightIcon = (ImageView) this.findViewById(R.id.rightIcon);
    }

}
