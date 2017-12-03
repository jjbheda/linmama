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
                    printOrderData(TAG, bean);
                }

                @Override
                public void connectFailed() {
                    ViewUtils.showToast(LmamaApplication.getInstance(), "票据打印机连接失败");
                }
            });
        } else {
            printOrderData(TAG, bean);
        }
    }


    public static void printOrderData(String TAG, TakingOrderBean bean) {

        final StringBuilder title_builder = new StringBuilder();
        if (bean == null)
            return;
        title_builder.append("          #" + bean.order_no + " 林妈妈");
        title_builder.append("\n");
        title_builder.append("          自营早餐商城"+"\n");

        final StringBuilder builder_sub_title = new StringBuilder();
        String orderTypestr = "";
        if (bean.ordertype == 0) {
            orderTypestr = "        当日单 ";
        } else if (bean.ordertype == 1) {
            orderTypestr = "        预约单 ";
        } else if (bean.ordertype == 10) {
            orderTypestr = "";
        }
        builder_sub_title.append(orderTypestr);
        if (bean.is_for_here.equals("0")) {
            builder_sub_title.append("（自取）");
        } else {
            builder_sub_title.append("（堂食）");
        }
        StringBuilder builder_order_no = new StringBuilder();
        builder_order_no.append("\n");
        builder_order_no.append("  单号：");
        builder_order_no.append(bean.serial_number);
        builder_order_no.append("\n");
        builder_order_no.append("  下单时间:" + bean.order_datetime_bj+"\n");
        StringBuilder builder_remark = new StringBuilder();
        if (!bean.remark.equals("")) {
            builder_remark.append("---------------------------");
            builder_remark.append("      备注：" + bean.remark+"\n");
        }
        StringBuilder builder_star = new StringBuilder();
        builder_star.append ("  ***************************"+"\n");
        builder_star.append("    " +bean.pickup.pickup_date + " " + bean.pickup.pickup_start_time + "-" + bean.pickup.pickup_end_time+"\n");
        StringBuilder builder_good = new StringBuilder();
        for (OrderGoodBean bean1 : bean.goods_list) {
            builder_good.append("    " + bean1.name);
            builder_good.append("     " + bean1.amount);
            builder_good.append("     " + bean1.total_price+"\n");
        }

        StringBuilder builder_account = new StringBuilder();
        builder_account.append("  ---------------------------"+"\n");
        builder_account.append("              消费金额："+bean.pay_amount+""+"\n");

//        StringBuilder builder_account_num = new StringBuilder();
//        builder_account_num.append(bean.pay_amount+""+"\n");

        StringBuilder builder = new StringBuilder();
        builder.append("  ***************************"+"\n");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("  " + bean.place.place_name);
        stringBuilder2.append("\n");
        stringBuilder2.append("  " + bean.place.place_address);
        stringBuilder2.append("\n");
        stringBuilder2.append("  " + (bean.user.user_tel));
        stringBuilder2.append("\n");
        stringBuilder2.append("  " + bean.user.user_name);
        stringBuilder2.append("\n");
        stringBuilder2.append("  ---------------------------");
        stringBuilder2.append("\n");
        stringBuilder2.append("\n");
        stringBuilder2.append("\n");

        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        if (printNum == 2) {
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
        } else if (printNum == 3) {
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
        } else {
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
        }

    }

    public static void printNewOrder(String TAG, LResultNewOrderBean bean) {
        final StringBuilder title_builder = new StringBuilder();
        if (bean == null)
            return;
        title_builder.append("          #" + bean.order_no + " 林妈妈");
        title_builder.append("\n");
        title_builder.append("          自营早餐商城"+"\n");

        final StringBuilder builder_sub_title = new StringBuilder();
        String orderTypestr = "     当日单 ";
        builder_sub_title.append(orderTypestr);
        // 0 取消 1已取消 2 已退款
        if (bean.is_for_here.equals("0")) {
            builder_sub_title.append("（自取）"+"\n");
        } else {
            builder_sub_title.append("（堂食）"+"\n");
        }
        StringBuilder builder_order_no = new StringBuilder();
        builder_order_no.append("\n");
        builder_order_no.append("  单号：");
        builder_order_no.append(bean.serial_number);
        builder_order_no.append("\n");
        builder_order_no.append("  下单时间:" + bean.order_datetime_bj+"\n");

        StringBuilder builder_remark = new StringBuilder();
        if (!bean.remark.equals("")) {
            builder_remark.append("  ---------------------------"+"\n");
            builder_remark.append("      备注：" + bean.remark+"\n");
        }
        StringBuilder builder_star = new StringBuilder();
        builder_star.append ("  ***************************"+"\n");
        StringBuilder builder_good = new StringBuilder();
        for (OrderOrderMenuBean bean1 : bean.order_list) {
            builder_good.append("    " + bean1.date+"\n");
            for (OrderGoodBean goodBean : bean1.goods_list) {
                builder_good.append("    " + goodBean.name);
                builder_good.append("       " + goodBean.amount);
                builder_good.append("      " + goodBean.total_price+"\n");
            }
        }
        StringBuilder builder_account = new StringBuilder();
        builder_account.append("  ---------------------------"+"\n");
        builder_account.append("            消费金额："+bean.pay_amount+""+"\n");


//        StringBuilder builder_account_num = new StringBuilder();
//        builder_account_num.append(bean.pay_amount+""+"\n");

        StringBuilder builder = new StringBuilder();
        builder.append("  ***************************"+"\n");

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("  " + bean.place.place_name);
        stringBuilder2.append("\n");
        stringBuilder2.append("  " + bean.place.place_address);
        stringBuilder2.append("\n");
        stringBuilder2.append("  " + bean.user.user_tel);
        stringBuilder2.append("\n");
        stringBuilder2.append("  " + bean.user.user_name);
        stringBuilder2.append("\n");
        stringBuilder2.append("  ---------------------------");
        stringBuilder2.append("\n");
        stringBuilder2.append("\n");
        stringBuilder2.append("\n");
        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        if (printNum == 2) {
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
        } else if (printNum == 3) {
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
        } else {
            printData(title_builder.toString(),builder_sub_title.toString(),builder_order_no.toString(),builder_remark.toString(),builder_star.toString(),builder_good.toString(),
                    builder_account.toString(),builder.toString(),stringBuilder2.toString());
        }
    }

    public static void printData(final String str1, final String str2, final String str3, final String str4,final String str5, final String str6,
                                 final String builder_account, final String str8,final String str9) {
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
                list.add(DataForSendToPrinterPos58.selectCharacterSize(2));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(1));
                list.add(strTobytes(str1));

                list.add(DataForSendToPrinterPos58.printAndFeed(1));
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(0));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(1));
                list.add(strTobytes(str2));

                list.add(DataForSendToPrinterPos58.printAndFeed(1));
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(0));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(0));
                list.add(strTobytes(str3));

                if (!str4.equals("")) {
                    list.add(DataForSendToPrinterPos58.printAndFeed(1));
                    list.add(DataForSendToPrinterPos58.selectCharacterSize(2));
                    list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(1));
                    list.add(strTobytes(str4));
                }

                list.add(DataForSendToPrinterPos58.printAndFeed(1));
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(0));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(0));
                list.add(strTobytes(str5));

                list.add(DataForSendToPrinterPos58.printAndFeed(0));
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(1));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(0));
                list.add(strTobytes(str6));

                list.add(DataForSendToPrinterPos58.printAndFeed(0));
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(1));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(1));
                list.add(strTobytes(builder_account));

//                list.add(DataForSendToPrinterPos58.printAndFeed(0));
//                list.add(DataForSendToPrinterPos58.initializePrinter());
//                list.add(DataForSendToPrinterPos58.selectCharacterSize(1));
//                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(1));
//                list.add(strTobytes(str8));

                list.add(DataForSendToPrinterPos58.printAndFeed(1));
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(0));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(0));
                list.add(strTobytes(str8));

                list.add(DataForSendToPrinterPos58.printAndFeed(1));
                list.add(DataForSendToPrinterPos58.initializePrinter());
                list.add(DataForSendToPrinterPos58.selectCharacterSize(1));
                list.add(DataForSendToPrinterPos58.selectOrCancelBoldModel(1));
                list.add(strTobytes(str9));

                return list;
            }
        });
    }

    /**
     * 字符串转byte数组
     */
    public static byte[] strTobytes(String str) {
        byte[] b = null, data = null;
        try {
            b = str.getBytes("utf-8");
            data = new String(b, "utf-8").getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;

    }

    private static String transform(String str){
        StringBuilder sb  = new StringBuilder();
        for (int i = 0;i<str.length();i++) {
            char c = str.charAt(i);
            sb.append(c+" ");
        }
        return sb.toString();
    }
}

