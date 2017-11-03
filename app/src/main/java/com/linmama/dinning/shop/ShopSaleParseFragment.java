package com.linmama.dinning.shop;

import android.widget.TextView;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BaseFragment;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.shop.bean.ShopBean;
import com.linmama.dinning.shop.bean.ShopSaleParseBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

import static com.linmama.dinning.base.BaseModel.httpService;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class ShopSaleParseFragment extends BaseFragment {

    @BindView(R.id.today_date)
    TextView mTodayDate;
    @BindView(R.id.today_turnover)
    TextView mTodayTurnover;
    @BindView(R.id.yestoday_turnover)
    TextView mYestodayTurnover;
    @BindView(R.id.effective_order_num)
    TextView mOrderNum;
    @BindView(R.id.effective_order_price)
    TextView mOrderPrice;
    @BindView(R.id.invalid_order_num)
    TextView mInvalidOrderNum;
    @BindView(R.id.loss_num)
    TextView mLossNum;


    @Override
    protected int getLayoutResID() {
        return R.layout.sale_parse_v2;
    }

    @Override
    protected void initView() {
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date(time);
        String dateStr=format.format(date);
        mTodayDate.setText(dateStr);

        showDialog("加载中...");
        httpService.getShopParseBaseData()
                .compose(new CommonTransformer<ShopSaleParseBean>())
                .subscribe(new CommonSubscriber<ShopSaleParseBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(ShopSaleParseBean bean) {
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
    }

    @Override
    protected void initListener() {

    }

    private void showUI(ShopSaleParseBean bean) {
        mTodayTurnover.setText("￥"+bean.income_now);
        mYestodayTurnover.setText("昨日￥ "+bean.income_yesterday);
        mOrderNum.setText(bean.valid_order+"");
        mOrderPrice.setText("客单价￥"+bean.per_price);
        mLossNum.setText("预计损失￥"+bean.income_invalid+"");
        mInvalidOrderNum.setText(bean.count_invalid+"");
    }

}
