package com.linmama.dinning.shop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.asynctask.AsyncTaskUtils;
import com.linmama.dinning.utils.asynctask.CallEarliest;
import com.linmama.dinning.utils.asynctask.Callback;
import com.linmama.dinning.utils.asynctask.IProgressListener;
import com.linmama.dinning.utils.asynctask.ProgressCallable;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

import static com.linmama.dinning.base.BaseModel.httpService;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class ShopManagerFragment extends BaseFragment {
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

        if (!PrintDataService.getInstance().isConnection()) {
            AsyncTaskUtils.doProgressAsync(mActivity, ProgressDialog.STYLE_SPINNER, "请稍后...", "正在连接票据打印机",
                    new CallEarliest<Void>() {

                        @Override
                        public void onCallEarliest() throws Exception {

                        }

                    }, new ProgressCallable<Void>() {

                        @Override
                        public Void call(IProgressListener pProgressListener)
                                throws Exception {
                            PrintDataService.getInstance().init();
                            return null;
                        }

                    }, new Callback<Void>() {

                        @Override
                        public void onCallback(Void pCallbackValue) {
                            if (PrintDataService.getInstance().isConnection()) {
                                ViewUtils.showToast(mActivity, "已连接票据打印机");
                                mPrintStatusTv.setText("已连接");
                                mPrintIv.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.icon_has_connect));
                            } else {
                                ViewUtils.showToast(mActivity, "票据打印机连接失败");
                                mPrintStatusTv.setText("未连接");
                                mPrintIv.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.icon_off_connect));
                            }
                        }
                    });
        } else {
            ViewUtils.showToast(mActivity, "已连接票据打印机");
            mPrintStatusTv.setText("已连接");
            mPrintIv.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.icon_has_connect));
        }
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
