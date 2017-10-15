package com.xcxid.dinning.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.bluetooth.BTDeviceBean;

import java.util.ArrayList;

/**
 * Created by jingkang onOrOff 2017/3/13
 */

public class BTDeviceAdapter extends BaseAdapter {
    private ArrayList<BTDeviceBean> mDevices;
    private LayoutInflater mInflater;
    private Context mContext;

    public BTDeviceAdapter(ArrayList<BTDeviceBean> devices, Context context) {
        this.mDevices = devices;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDevices != null ? mDevices.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.lv_item_btdevices, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.btName);
            holder.status = (TextView) view.findViewById(R.id.btStatus);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BTDeviceBean bean = mDevices.get(i);
        if (null != bean) {
            BluetoothDevice device = bean.getDevice();
            holder.name.setText(device.getName());
            if (bean.isBonded()) {
                holder.status.setText("已配对");
            } else {
                holder.status.setText("未配对");
            }
        }
        return view;
    }

    private static class ViewHolder {
        private TextView name;
        private TextView status;
    }
}
