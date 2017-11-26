package com.linmama.dinning.utils;

import android.util.Log;

import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.url.Constants;

/**
 * Created by jiangjingbo on 2017/11/24.
 */

public class PrintUtils {
    public static void printOrder(String TAG,TakingOrderBean bean) {
        final StringBuilder builder = new StringBuilder();
        if (bean == null)
            return;

//        if (!PrintDataService.isConnection()) {
//            return;
//        }
        builder.append("         林妈妈早餐 ");
        builder.append("\n");
        // 0 取消 1已取消 2 已退款
        builder.append(bean.is_ensure_order.equals("0") ? "         未接单" : "       已接单");
        if (bean.is_for_here.equals(0)) {
            builder.append("（自取）");
        } else {
            builder.append("（堂食）");
        }
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("预约单:");
        builder.append(bean.serial_number);
        builder.append("\n");

        builder.append("下单时间:" + bean.order_datetime_bj);

        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("    菜品");
        builder.append("    数量");
        builder.append("    金额");
        builder.append("\n");
        builder.append("    " + bean.pickup.pickup_date);
        builder.append("\n");
        for (OrderGoodBean bean1 : bean.goods_list) {
            builder.append("    " + bean1.name);
            builder.append("    " + bean1.amount);
            builder.append("    " + bean1.total_price);
            builder.append("\n");
        }

        builder.append("---------------------------");
        builder.append("\n");
        if (!bean.remark.equals("")) {
            builder.append("    备注：" + bean.remark);
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
        }

        builder.append("               消费金额：" + bean.pay_amount);
        builder.append("\n");
        builder.append("\n");

        builder.append("取餐时间：" + bean.pickup.pickup_date + " " + bean.pickup.pickup_start_time + "-" + bean.pickup.pickup_end_time);
        builder.append("\n");
        builder.append("" + bean.place.place_name);
        builder.append("\n");
        builder.append("" + bean.place.place_address);
        builder.append("\n");
        builder.append("" + bean.user.user_name);
        builder.append("\n");
        builder.append("" + bean.user.user_tel);
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("\n");

        String printData = builder.toString();
        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        if (printNum == 2) {
            builder.append(printData);
        } else if (printNum == 3) {
            builder.append(printData);
            builder.append(printData);
        }
        PrintDataService.send(builder.toString());
        Log.d(TAG, builder.toString());
    }
}
