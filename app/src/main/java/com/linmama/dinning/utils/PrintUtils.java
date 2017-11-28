package com.linmama.dinning.utils;

import android.util.Log;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.OrderOrderMenuBean;
import com.linmama.dinning.bean.OrderPickupTimeBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.url.Constants;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterPos58;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/24.
 */

public class PrintUtils {
    public static void printOrder(final String TAG, final TakingOrderBean bean) {
        if (!PrintDataService.getInstance().isConnection()) {
            PrintDataService.getInstance().connect(new PrintDataService.ConnectCallback() {
                @Override
                public void connectSucess() {
                    ViewUtils.showToast(LmamaApplication.getInstance(), "已连接票据打印机");
                    printOrderData(TAG,bean);
                }

                @Override
                public void connectFailed() {
                    ViewUtils.showToast(LmamaApplication.getInstance(), "票据打印机连接失败");
                }
            });
        } else {
            printOrderData(TAG,bean);
        }
    }


    public static void printOrderData(String TAG, TakingOrderBean bean){

        final StringBuilder builder = new StringBuilder();
        if (bean == null)
            return;
        String str1 = "      林 妈 妈 早 餐 ";
        builder.append("\n");
        // 0 取消 1已取消 2 已退款
        builder.append(bean.is_ensure_order.equals("0") ? "         未接单" : "       已接单");
        if (bean.is_for_here.equals("0")) {
            builder.append("（自取）");
        } else {
            builder.append("（堂食）");
        }
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        String orderTypestr ="";
        if (bean.ordertype == 0) {
            orderTypestr = "当日单：";
        } else if (bean.ordertype == 1) {
            orderTypestr = "预约单：";
        } else if (bean.ordertype == 10){
            orderTypestr = "单号：";
        }
        builder.append(orderTypestr);
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

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("" + bean.user.user_tel);
        stringBuilder2.append("\n");
        stringBuilder2.append("---------------------------");

        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        if (printNum == 2) {
            printData(str1,builder.toString(),stringBuilder2.toString());
            printData(str1,builder.toString(),stringBuilder2.toString());
        } else if (printNum == 3) {
            printData(str1,builder.toString(),stringBuilder2.toString());
            printData(str1,builder.toString(),stringBuilder2.toString());
            printData(str1,builder.toString(),stringBuilder2.toString());
        } else {
            printData(str1,builder.toString(),stringBuilder2.toString());
        }

        Log.d(TAG, builder.toString());
    }

    public static void printNewOrder(String TAG, LResultNewOrderBean bean) {
        final StringBuilder builder = new StringBuilder();
        String str1 = "      林 妈 妈 早 餐 ";
        builder.append("\n");

        builder.append("       已接单");
        if (bean.is_for_here.equals("0")){
            builder.append("（自取）");
        } else {
            builder.append("（堂食）");
        }
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        if (bean.order_type.equals("1"))     {  //1预约单 0当日单
            builder.append("预约单:");
        } else {
            builder.append("当日单:");
        }

        builder.append(bean.serial_number);
        builder.append("\n");

        builder.append("下单时间:"+bean.order_datetime_bj);

        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("    菜品");
        builder.append("    数量");
        builder.append("    金额");
        builder.append("\n");
        for (OrderOrderMenuBean bean1:bean.order_list){
            builder.append("    "+bean1.date);
            builder.append("\n");
            for (OrderGoodBean goodBean : bean1.goods_list) {
                builder.append("    " + goodBean.name);
                builder.append("    " + goodBean.amount);
                builder.append("    " + goodBean.total_price);
                builder.append("\n");
            }
            builder.append("\n");
        }
        builder.append("\n");

        builder.append("---------------------------");
        builder.append("\n");
        if (!bean.remark.equals("")) {
            builder.append("    备注："+bean.remark);
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
        }

        builder.append("               消费金额："+bean.pay_amount);
        builder.append("\n");
        builder.append("\n");
        builder.append("取餐时间：");
        for (OrderPickupTimeBean bean1:bean.pickup_list) {
            builder.append(""+bean1.pickup_date+ " "+bean1.pickup_start_time+"-"+bean1.pickup_end_time);
            builder.append("\n");
        }
        builder.append("\n");
        builder.append(""+bean.place.place_name);
        builder.append("\n");
        builder.append(""+bean.place.place_address);
        builder.append("\n");
        builder.append(""+bean.user.user_name);
        builder.append("\n");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("" + bean.user.user_tel);
        stringBuilder2.append("\n");
        stringBuilder2.append("---------------------------");

        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        if (printNum == 2) {
            printData(str1,builder.toString(),stringBuilder2.toString());
            printData(str1,builder.toString(),stringBuilder2.toString());
        } else if (printNum == 3) {
            printData(str1,builder.toString(),stringBuilder2.toString());
            printData(str1,builder.toString(),stringBuilder2.toString());
            printData(str1,builder.toString(),stringBuilder2.toString());
        } else {
            printData(str1,builder.toString(),stringBuilder2.toString());
        }
        Log.d(TAG,builder.toString());
    }

    public static void printData(final String str1,final  String str2,final String str3) {
        MainActivity.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {
                List<byte[]> list = new ArrayList<>();
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.printAndFeedForward(2));
                list.add(DataForSendToPrinterPos58.selectCharacterSize(4));
                list.add(strTobytes(str1));
                list.add(DataForSendToPrinterPos58.printAndFeedLine());
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(2));
                list.add(strTobytes(str2));
                list.add(DataForSendToPrinterPos58.printAndFeedLine());

                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(3));
                list.add(strTobytes(str3));

                return list;
            }
        });
    }

    /**
     * 字符串转byte数组
     * */
    public static byte[] strTobytes(String str){
        byte[] b=null,data=null;
        try {
            b = str.getBytes("utf-8");
            data=new String(b,"utf-8").getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;

}
}

