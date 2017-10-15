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
        StringBuilder builder = new StringBuilder();
        BigDecimal bd = null;
        String fullname = (String) SpUtils.get(Constants.USER_FULLNAME, "");
        if (!TextUtils.isEmpty(fullname)) {
            builder.append("      ");
            builder.append(fullname);
            builder.append("\n");
        }
        if (null != bean) {
            builder.append("      ");
            String payStatus = bean.getPay_status();
            String payChannel = bean.getPay_channel();
            if (payStatus.equals("1")) {
                builder.append("未支付");
            } else if (payStatus.equals("2")) {
                if (payChannel.equals("1")) {
                    builder.append("已在线支付");
                } else if (payChannel.equals("2")) {
                    builder.append("已吧台支付");
                }
            }
            String diningWay = bean.getDining_way();
            if (diningWay.equals("1")) {
                builder.append("(堂食)");
            } else if (diningWay.equals("2")) {
                builder.append("(外带)");
            }
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
            String serialNumber = bean.getSerial_number();
            builder.append("  NO:");
            builder.append(serialNumber);
            builder.append("\n");
            String deskNum = bean.getDesk_num();
            builder.append("桌号:");
            builder.append(deskNum);
            builder.append("    ");
            int diningNum = bean.getDine_num();
            builder.append("人数:");
            builder.append(diningNum);
            builder.append("\n");
            String orderDatetimeBj = bean.getOrder_datetime_bj();
            builder.append("时间:");
            builder.append(orderDatetimeBj);
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
            builder.append("菜品");
            builder.append("      数量");
//            builder.append("  价格");
            builder.append("    金额");
            builder.append("\n");
            builder.append("\n");
        }
        if (null != bean && bean.getOrderItems() != null) {
            List<OrderItemsBean> items = bean.getOrderItems();
            for (OrderItemsBean item : items) {
                builder.append(item.getName());
                builder.append("    ");
                int num = item.getNum();
                builder.append(num);
                builder.append("    ");
                String cost = item.getClosing_cost();
                BigDecimal costBd = new BigDecimal(cost);
                if (num > 1) {
                    costBd = costBd.multiply(new BigDecimal(num));
                }
                if (null == bd) {
                    bd = new BigDecimal(0);
                    bd = bd.add(costBd);
                } else {
                    bd = bd.add(costBd);
                }
                builder.append(costBd.toString());
                builder.append("\n");
            }
            builder.append("---------------------------");
            builder.append("\n");
        }
        if (null != bean ) {
            String remark = bean.getRemark();
            if (!TextUtils.isEmpty(remark)) {
                builder.append("备注:");
                builder.append(remark);
                builder.append("\n");
            }
        }
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("消费金额: ");
        if (bd != null) {
            builder.append(bd.toString());
        }
        builder.append("\n");
        builder.append("应收金额: ");
        if (bd != null) {
            builder.append(bd.toString());
        }
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("小不点点餐  www.xcxid.com");
        builder.append("\n");
        builder.append("      欢迎下次光临");
        builder.append("\n");
        builder.append("\n");
        if (PrintDataService.isConnection()) {
            String printData = builder.toString();
            int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
            if (printNum == 2) {
                builder.append(printData);
            } else if (printNum == 3) {
                builder.append(printData);
                builder.append(printData);
            }
            PrintDataService.send(builder.toString());
        }
    }

    @Override
    public void failOrderDetail(String failMsg) {

    }
}
