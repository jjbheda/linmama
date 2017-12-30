package com.linmama.dinning.utils.printer;

import android.content.Context;
import android.os.HandlerThread;

import com.linmama.dinning.base.BaseActivity;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.OrderOrderMenuBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.printer.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class FeiEPrinterUtils {

    public static final String URL = "http://api.feieyun.cn/Api/Open/";//不需要修改

    public static final String USER = "jjbheda@163.com";//*必填*：账号名
    public static final String UKEY = "43IYVpZFAcZIBrNt";//*必填*: 注册账号后生成的UKEY
    public static final String SN = "217509939";//*必填*：打印机编号，必须要在管理后台里添加打印机或调用API接口添加之后，才能调用API

    //**********测试时，打开下面注释掉方法的即可,更多接口文档信息,请访问官网开放平台查看**********
    //=====================以下是函数实现部分================================================

    private static String addprinter(String snlist){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printerAddlist"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("printerContent",snlist));

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    //方法1
    public static String print(String sn){
        //标签说明：
        //单标签:
        //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
        //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
        //成对标签：
        //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
        //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
        //拼凑订单内容时可参考如下格式
        //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式

        String content;
        content = "<CB>测试打印</CB><BR>";
        content += "名称　　　　　 单价  数量 金额<BR>";
        content += "--------------------------------<BR>";
        content += "饭　　　　　　 1.0    1   1.0<BR>";
        content += "炒饭　　　　　 10.0   10  10.0<BR>";
        content += "蛋炒饭　　　　 10.0   10  100.0<BR>";
        content += "鸡蛋炒饭　　　 100.0  1   100.0<BR>";
        content += "番茄蛋炒饭　　 1000.0 1   100.0<BR>";
        content += "西红柿蛋炒饭　 1000.0 1   100.0<BR>";
        content += "西红柿鸡蛋炒饭 100.0  10  100.0<BR>";
        content += "备注：加辣<BR>";
        content += "--------------------------------<BR>";
        content += "合计：xx.0元<BR>";
        content += "送货地点：广州市南沙区xx路xx号<BR>";
        content += "联系电话：13888888888888<BR>";
        content += "订餐时间：2016-08-08 08:08:08<BR>";
        content += "<QR>http://www.dzist.com</QR>";

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));
        nvps.add(new BasicNameValuePair("content",content));
        nvps.add(new BasicNameValuePair("times","1"));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    //方法4
    public static boolean queryPrinterStatus(){

        String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
        if (sn.equals(""))
            return false;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryPrinterStatus"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));

        CloseableHttpResponse response = null;
        String result = "";
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String msg = "";
        try {
            JSONObject jsonObject = new JSONObject(result.toString());
            msg = jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (msg == null) {
            return false;
        } else {
            return msg.contains("在线");
        }
    }

    //标签说明：
    //单标签:
    //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
    //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
    //成对标签：
    //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
    //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
    //拼凑订单内容时可参考如下格式
    //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式
    private static String getOrderPrintData(TakingOrderBean bean) {

        final StringBuilder builder = new StringBuilder();
        if (bean == null)
            return "";
        builder.append("<L><C><BOLD>#" + bean.order_no + "林妈妈自营早餐商城</L></C></BOLD><BR>");

        String orderTypestr = "";
        if (bean.ordertype == 0) {
            orderTypestr = "<L><C><BOLD>当日单";
        } else if (bean.ordertype == 1) {
            orderTypestr = "<L><C><BOLD>预约单 ";
        } else if (bean.ordertype == 10) {
            orderTypestr = "<L><C><BOLD>";
        }
        builder.append(orderTypestr);
        if (bean.is_for_here.equals("0")) {
            builder.append("（自取)</L></BOLD></C><BR>");
        } else {
            builder.append("（堂食)</L></BOLD></C><BR>");
        }
        builder.append(" 单号：");
        builder.append(bean.serial_number+"<BR>");
        builder.append(" 下单时间:" + bean.order_datetime_bj+"<BR>");
        if (!bean.remark.equals("")) {
            builder.append("  ---------------------------<BR>");
            builder.append("<L><BOLD>   备注：" + bean.remark+"</L></BOLD><BR>");
        }
        builder.append ("  ***************************"+"<BR>");
        builder.append("   <L>"+bean.pickup.pickup_date + " " + bean.pickup.pickup_start_time + "-" + bean.pickup.pickup_end_time+"</L><BR>");
        for (OrderGoodBean bean1 : bean.goods_list) {
            builder.append("   <L>" + getShopName(bean1.name)+"</L>");
            builder.append("<L>    " + bean1.amount+"</L>");
            builder.append("<L>    " + bean1.total_price+"</L><BR>");
        }

        builder.append("  ---------------------------"+"<BR>");
        builder.append("             消费金额："+" <L><BOLD>"+bean.pay_amount+""+"</L></BOLD><BR>");

//        StringBuilder builder_account_num = new StringBuilder();
//        builder_account_num.append(bean.pay_amount+""+"\n");

        builder.append("  ***************************"+"<BR>");
        builder.append("  <B>" + bean.place.place_name+"</B>");
        builder.append("<BR>");
        builder.append("  <B>" + bean.place.place_address+"</B><BR>");
        builder.append("  <B>" + (bean.user.user_tel)+"</B><BR>");
        builder.append("  <B>" + bean.user.user_name+"</B><BR>");
        builder.append("  ---------------------------");
       return builder.toString();
    }

    /**
     *
     * @param bean
     * @param innerMenuBeanIndex   新订单拆分打印
     * @return
     */

    //标签说明：
    //单标签:
    //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
    //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
    //成对标签：
    //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
    //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
    //拼凑订单内容时可参考如下格式
    //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式
    private static String getNewOrderPrintData(LResultNewOrderBean bean,int innerMenuBeanIndex) {

        final StringBuilder builder = new StringBuilder();
        if (bean == null)
            return "";
        builder.append("<L><C><BOLD>#" + bean.order_no + "林妈妈自营早餐商城</L></C></BOLD><BR>");

        String orderTypestr = ""+(bean.order_type.equals("1") ? "<L><C><BOLD>预约单":"<L><C><BOLD>当日单");
        builder.append(orderTypestr);
        // 0 取消 1已取消 2 已退款
        if (bean.is_for_here.equals("0")) {
            builder.append("（自取）</L><C></BOLD><BR>");
        } else {
            builder.append("（堂食）</L></C></BOLD><BR>");
        }
        builder.append(" 单号：");
        builder.append(bean.serial_number+"<BR>");
        builder.append(" 下单时间:" + bean.order_datetime_bj+"<BR>");

        if (!bean.remark.equals("")) {
            builder.append("  ---------------------------<BR>");
            builder.append("<L>   <BOLD>备注：" + bean.remark+"</L><BOLD><BR>");
        }
        builder.append ("  ***************************"+"<BR>");

        OrderOrderMenuBean bean1 = bean.order_list.get(innerMenuBeanIndex);
        if (bean.pickup_list.get(innerMenuBeanIndex)!=null) {
            builder.append("  <L>" + bean.pickup_list.get(innerMenuBeanIndex).pickup_date+" "+
                    bean.pickup_list.get(innerMenuBeanIndex).pickup_start_time+ "-" + bean.pickup_list.get(innerMenuBeanIndex).pickup_end_time+"</L><BR>");
        }
        double price = 0.00;
        for (OrderGoodBean goodBean : bean1.goods_list) {
            builder.append("   <L>" + getShopName(goodBean.name)+"</L>");
            builder.append("<L>    " + goodBean.amount+"</L>");
            builder.append("<L>    " + goodBean.total_price+"</L><BR>");
            if (Double.parseDouble(goodBean.total_price) != 0) {
                price = price + Double.parseDouble(goodBean.total_price);
            }
        }

        builder.append("  ---------------------------"+"<BR>");
        builder.append("             消费金额："+" <L><BOLD>"+ price + ""+"</L></BOLD><BR>");

//        StringBuilder builder_account_num = new StringBuilder();
//        builder_account_num.append(bean.pay_amount+""+"\n");

        builder.append ("  ***************************"+"<BR>");

        builder.append("  <B>" + bean.place.place_name+"</B>");
        builder.append("<BR>");
        builder.append("  <B>" + bean.place.place_address+"</B><BR>");
        builder.append("  <B>" + (bean.user.user_tel)+"</B><BR>");
        builder.append("  <B>" + bean.user.user_name+"</B><BR>");
        builder.append("  ---------------------------");
        return builder.toString();
    }


    //生成签名字符串
    private static String signature(String USER,String UKEY,String STIME){
        String s = DigestUtils.sha1Hex(USER+UKEY+STIME);
        return s;
    }

    private static String getShopName (String name){
        Integer name_len  = 0;								//字符串长度，给初始值0
        char[] char_name = name.toCharArray();				//将字符串转为char[]数组
        for (int i = 0; i < char_name.length; i++) {		//遍历数组判断字符是否为中文
            if(isChinese(char_name[i])){
                name_len += 2;								//中文字节长度为2
            }else{
                name_len ++;								//英文字节长度为1
            }
        }
        Integer add_len = 10 - name_len;						//10是预计字符占用的最大长度，即最多十个汉字或10个英文字符，add_len是需要补充的长度
        for(int i = 0; i < add_len; i++) {
            name+= " ";									//循环得到的需要补充长度的空格，加在字符后边
        }

       return name;
    }

    public static void FeiprintOrderWithLoading(final BaseActivity context, final TakingOrderBean bean) {
        HandlerThread thread = new HandlerThread("NetWork");
        thread.start();
        context.showDialog("","加载中");
        android.os.Handler handler = new android.os.Handler(thread.getLooper());
        //延迟一秒后进行
        handler.postDelayed(new Runnable() {
            public void run() {
                String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
                String printData = getOrderPrintData(bean);
                printOrder(printData,sn);
                context.dismissDialog();
            }
        },100);

    }

    public static void FeiprintNewOrderWithLoading(final BaseActivity context, final LResultNewOrderBean bean) {
        HandlerThread thread = new HandlerThread("NetWork");
        thread.start();
        context.showDialog("","加载中");
        android.os.Handler handler = new android.os.Handler(thread.getLooper());
        //延迟一秒后进行
        handler.postDelayed(new Runnable() {
            public void run() {
                String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
                for (int i = 0;i<bean.order_list.size();i++) {
                    String printData = getNewOrderPrintData(bean,i);
                    printOrder(printData,sn);
                }
                context.dismissDialog();
            }
        },100);

    }

    private static void printOrder(String orderData,String sn) {

//        String content = getOrderPrintData(bean);
//        String content = "<CB>测试打印</CB><BR>";
        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));
        nvps.add(new BasicNameValuePair("content",orderData));
        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        nvps.add(new BasicNameValuePair("times",""+printNum));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //通过这个方法，可以判断出字符是中文还是英文，，，中文就给需要补充的空格数量加2，否则加1，，，，这样排版就能出来了
    // 根据Unicode编码判断中文汉字和符号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }






}
