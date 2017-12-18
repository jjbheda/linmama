package com.linmama.dinning.receiver;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.order.model.ReceiveOrderModel;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.SpUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jingkang on 2017/4/26
 */

public class ReceiveOrderService extends IntentService implements ReceiveOrderModel.ReceiveHint, OrderDetailModel.OrderDetailHint {
    public ReceiveOrderService() {
        super("ReceiveOrderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String orderId = intent.getStringExtra(Constants.ORDER_AUTO_RECEIVE);
        ReceiveOrderModel receiveOrderModel = new ReceiveOrderModel();
        receiveOrderModel.receivingOrder(orderId, this);
    }

    @Override
    public void successReceive(String orderId) {
        boolean isAutoPrint = (boolean) SpUtils.get(Constants.AUTO_PRINT, false);
        if (isAutoPrint) {
            OrderDetailModel orderDetailModel = new OrderDetailModel();
            orderDetailModel.getOrderDetail(Integer.parseInt(orderId), this);
        }
    }

    @Override
    public void failReceive(String failMsg) {

    }

    @Override
    public void successOrderDetail(OrderDetailBean bean) {
    }

    @Override
    public void failOrderDetail(String failMsg) {

    }
}
