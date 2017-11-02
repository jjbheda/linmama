package com.linmama.dinning.shop;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BaseFragment;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

import static com.linmama.dinning.base.BaseModel.httpService;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class ShopManagerFragment extends BaseFragment {
    @BindView(R.id.shop_status)
    TextView mShopStatus;
    @BindView(R.id.today_revenues)
    TextView mTodayRevenues;
    @BindView(R.id.today_order_tv)
    TextView mTodayOrderNum;

    @BindView(R.id.shop_name_tv)
    TextView mShopName;
    @BindView(R.id.icon_shop_name)
    ImageView mIconShop;


    @Override
    protected int getLayoutResID() {
        return R.layout.store_manager_v2;
    }

    @Override
    protected void initView() {
        httpService.getShopBaseData()
                .compose(new CommonTransformer<ShopBean>())
                .subscribe(new CommonSubscriber<ShopBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(ShopBean bean) {
                        if (bean != null){
                            showUI(bean);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                });
    }

    @Override
    protected void initListener() {

    }

    public void showUI(ShopBean bean){
        mShopStatus.setText(bean.is_open.equals("1")?"营业中":"打烊");
        mTodayRevenues.setText(bean.income);
        mTodayOrderNum.setText(bean.total_ords+"");
        mShopName.setText(bean.shop_name);
        Picasso.with(mActivity).load(bean.shop_logo).placeholder(R.mipmap.baobao).into(mIconShop);

    }
}
