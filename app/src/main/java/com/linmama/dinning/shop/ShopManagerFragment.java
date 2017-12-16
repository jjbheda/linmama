package com.linmama.dinning.shop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BaseFragment;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.goods.GoodsFragment;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.shop.account.AccountFragment;
import com.linmama.dinning.shop.bean.ShopBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.asynctask.AsyncTaskUtils;
import com.linmama.dinning.utils.asynctask.CallEarliest;
import com.linmama.dinning.utils.asynctask.Callback;
import com.linmama.dinning.utils.asynctask.IProgressListener;
import com.linmama.dinning.utils.asynctask.ProgressCallable;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

import static com.linmama.dinning.base.BaseModel.httpService;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class ShopManagerFragment extends BaseFragment {
    public static String TAG = "ShopManagerFragment";
    @BindView(R.id.shop_status)
    TextView mShopStatus;
    @BindView(R.id.shop_status_iv)
    ImageView mShopStatusIv;
    @BindView(R.id.today_revenues)
    TextView mTodayRevenues;
    @BindView(R.id.today_order_tv)
    TextView mTodayOrderNum;

    @BindView(R.id.shop_name_tv)
    TextView mShopName;
    @BindView(R.id.icon_shop_name)
    ImageView mIconShop;


    @BindView(R.id.goods_rt)
    RelativeLayout mGoodsRt;
    @BindView(R.id.shop_parse_rt)
    RelativeLayout mShopParse;
    @BindView(R.id.reconciliation_rt)
    RelativeLayout mShopReconciliation;

    @BindView(R.id.print_status_tv)
    TextView mPrintStatusTv;

    @BindView(R.id.iv_print)
    ImageView mPrintIv;

    @Override
    protected int getLayoutResID() {
        return R.layout.store_manager_v2;
    }

    @OnClick(R.id.goods_rt)
    public void turnToGoods(){
        CommonActivity.start(mActivity,GoodsFragment.class,new Bundle());
    }

    @OnClick(R.id.shop_parse_rt)
    public void turnToShopParse(){
        CommonActivity.start(mActivity,ShopSaleParseFragment.class,new Bundle());
    }

    @OnClick(R.id.reconciliation_rt)
    public void turnToReconciliation(){
        CommonActivity.start(mActivity,AccountFragment.class,new Bundle());
    }

    //handler 处理返回的请求结果
    Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isConnect = data.getBoolean("IS_CONNECT");
            if (isConnect) {
                mPrintStatusTv.setText("已连接");
                mPrintIv.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.icon_has_connect));
            } else {
                mPrintStatusTv.setText("未连接");
                mPrintIv.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.icon_off_connect));
            }
        }
    };

    @Override
    protected void initView() {
        showDialog("加载中...");
        httpService.getShopBaseData()
                .compose(new CommonTransformer<ShopBean>())
                .subscribe(new CommonSubscriber<ShopBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(ShopBean bean) {
                        dismissDialog();
                        if (bean != null){
                            showUI(bean);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        dismissDialog();
                    }
                });
        showDialog("检测中...");
        Handler handler = new Handler(mActivity.getMainLooper());
        //延迟一秒后进行
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "请求打印机接口");
                String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
                boolean isConnect = false;
                if (FeiEPrinterUtils.queryPrinterStatus()) {
                    isConnect = true;
//                    Toast.makeText(MainActivity.this, "票据打印机连接成功", Toast.LENGTH_SHORT).show();
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("IS_CONNECT",isConnect);
                msg.setData(data);
                updateHandler.sendMessage(msg);

            }
        },100);
    }

    @Override
    protected void initListener() {
    }

    public void showUI(ShopBean bean){
        if (bean.is_open.equals("1")) {
            mShopStatus.setText("营业中");
            mShopStatusIv.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.icon_on_sale));
        } else {
            mShopStatus.setText("打烊");
            mShopStatusIv.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.icon_off_sale));
        }

        mTodayRevenues.setText(bean.income);
        mTodayOrderNum.setText(bean.total_ords+"");
        mShopName.setText(bean.shop_name);
        Picasso.with(mActivity).load(bean.shop_logo).placeholder(R.mipmap.baobao).into(mIconShop);
    }
}
