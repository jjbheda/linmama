package com.linmama.dinning.bluetooth;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BaseActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.linmama.dinning.widget.MyAlertDialog;

import java.util.ArrayList;

/**
 * Created by jiangjingbo on 2017/12/13.
 */

public class PrintAdapter extends BaseAdapter {
    public static String TAG = "PrintAdapter";
    ArrayList<PrintBean> mData = new ArrayList<>();
    private BaseActivity mContext;
    private LayoutInflater mInflater;

    private String currentSn = "";

    public PrintAdapter(ArrayList<PrintBean> data, BaseActivity context) {
        this.mData = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        currentSn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
    }

    @Override
    public int getCount() {
            return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.lv_item_btdevices, parent, false);
            holder.name = (TextView) view.findViewById(R.id.btName);
            holder.status = (TextView) view.findViewById(R.id.btStatus);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final PrintBean bean = mData.get(position);
        if (null != bean) {
          holder.name.setText(bean.printSn);
          holder.status.setText(bean.conStatus?"在线":"未连接");

          if (currentSn.equals(bean.printSn)) {
              holder.status.setText("在线  已连接");
          }
        }
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                MyAlertDialog mAlert = new MyAlertDialog(mContext).builder()
//                        .setTitle("是否删除")
//                        .setConfirmButton("是", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mData.remove(bean);
//                                notifyDataSetChanged();
//
//                                try {
//                                    ArrayList<PrintBean> arrayList = SpUtils.getObject(Constants.PRINT_DEVEICES);
//                                    if (arrayList == null){
//                                        arrayList = new ArrayList<>();
//                                    } else {
//                                        arrayList.remove(bean);
//                                    }
//                                    SpUtils.setObject(Constants.PRINT_DEVEICES,arrayList);
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }).setPositiveButton("否", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        });
//                mAlert.show();
//                return true;
//            }
//        });

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.showDialog("","检测中");
//                HandlerThread thread = new HandlerThread("NetWork2");
//                thread.start();
//                Handler handler = new Handler(thread.getLooper());
//                //延迟一秒后进行
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        Log.d(TAG, "请求打印机接口");
//                        if (FeiEPrinterUtils.queryPrinterStatus(bean.printSn)) {
//                            ViewUtils.showToast(mContext, "票据打印机连接成功");
//                            holder.status.setText("已连接");
//                            SpUtils.setObject(Constants.PRINT_DEVEICES_SELECTED, bean.printSn);
//                        } else {
//                            ViewUtils.showToast(mContext, "票据打印机连接失败");
//                            Log.d(TAG, "票据打印机连接失败");
//                            holder.status.setText("未连接");
//                        }
//                        mContext.dismissDialog();
//                    }
//                },100);
//            }
//        });
        return view;
    }

    private static class ViewHolder {
        private TextView name;
        private TextView status;
    }
}
