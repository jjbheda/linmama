package com.xcxid.dinning.http;

import com.xcxid.dinning.base.BaseHttpResult;
import com.xcxid.dinning.bean.AppVersionBean;
import com.xcxid.dinning.bean.CancelBean;
import com.xcxid.dinning.bean.CompleteOrderBean;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.bean.LoginBean;
import com.xcxid.dinning.bean.NewOrderBean;
import com.xcxid.dinning.bean.NonPayOrderBean;
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.bean.QuitOrderBean;
import com.xcxid.dinning.bean.RedDotStatusBean;
import com.xcxid.dinning.bean.RemindBean;
import com.xcxid.dinning.bean.SaleRankBean;
import com.xcxid.dinning.bean.SaleReportBean;
import com.xcxid.dinning.bean.SarchItemBean;
import com.xcxid.dinning.bean.StoreSettingsBean;
import com.xcxid.dinning.bean.TakingOrderBean;
import com.xcxid.dinning.bean.UserServerBean;
import com.xcxid.dinning.goods.category.MenuCategoryBean;
import com.xcxid.dinning.goods.item.MenuItemBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 网络请求的接口都在这里
 */
public interface HttpService {
    //1	登录接口
    @FormUrlEncoded
    @POST("shopUserTokenLogin/")
    Observable<BaseHttpResult<LoginBean>> login(@Field("username") String username,
        @Field("password") String pwd);

    //2	新订单列表接口
    @GET("newOrderList/")
    Observable<BaseHttpResult<NewOrderBean>> getNewOrder(@Query("page") int page);

    //3	已接单列表接口
    @GET("receivedOrderList/")
    Observable<BaseHttpResult<TakingOrderBean>> getReceivedOrder(@Query("page") int page);

    //4	提醒列表接口
    @GET("orderWarnList/")
    Observable<BaseHttpResult<RemindBean>> getWarnOrder();

    //5	退单列表接口
    @GET("refundApplicationList/")
    Observable<BaseHttpResult<List<QuitOrderBean>>> getQuitOrder();

    //6	未支付订单列表接口
    @GET("nonPaymentOrderList/")
    Observable<BaseHttpResult<NonPayOrderBean>> getNonPayOrder();

    //7	接单接口
    @FormUrlEncoded
    @POST("receivingOrder/")
    Observable<BaseHttpResult<DataBean>> receivingOrder(@Field("order_id") String order_id);

    //8	取消订单接口
    @FormUrlEncoded
    @POST("cancelOrder/")
    Observable<BaseHttpResult<CancelBean>> cancelOrder(@Field("order_id") String order_id, @Field("reason") String reason);

    //9	确认支付接口
    @FormUrlEncoded
    @POST("confirmPayment/")
    Observable<BaseHttpResult<DataBean>> confirmPayment(@Field("order_id") String order_id,
        @Field("operation_password") String operation_password);

    //10	设置备菜提醒接口
    @FormUrlEncoded
    @POST("setWarn/")
    Observable<BaseHttpResult<DataBean>> setWarn(@Field("order_id") String order_id,
        @Field("warn_time") String warn_time, @Field("warn_type") String warn_type);

    //11	取消备菜提醒接口
    @FormUrlEncoded
    @POST("cancelWarn/")
    Observable<BaseHttpResult<DataBean>> cancelWarn(@Field("warn_id") String warn_id);

    //12	提醒已知晓接口
    @FormUrlEncoded
    @POST("handleWarn/")
    Observable<BaseHttpResult<DataBean>> handleWarn(@Field("warn_id") String order_id);

    //13	同意退款接口
    @FormUrlEncoded
    @POST("refund/")
    Observable<BaseHttpResult<DataBean>> refund(@Field("refund_id") String refund_id, @Field("operation_password") String operation_password);

    //14	拒绝退款接口
    @FormUrlEncoded
    @POST("refusedRefund/")
    Observable<BaseHttpResult<DataBean>> refusedRefund(@Field("refund_id") String refund_id, @Field("reason") String reason);

    //15	菜品分类列表接口
    @GET("menuCategoryList/")
    Observable<BaseHttpResult<MenuCategoryBean>> getMenuCategory();

    //16	在售菜品列表接口
    @GET("onSellMenuItemList/")
    Observable<BaseHttpResult<MenuItemBean>> getOnSellMenu(@Query("menuCategory") int menuCategory);

    @GET("onSellMenuItemList/")
    Observable<BaseHttpResult<MenuItemBean>> getOnSellMenu();

    //17	菜品上架/下架接口
    @FormUrlEncoded
    @POST("onOrOffItem/")
    Observable<BaseHttpResult<DataBean>> onOrOffItem(@Field("op_flag") String op_flag,
        @Field("item_id") String item_id);

    //18	菜品搜索接口
    @GET("menuItemList/")
    Observable<BaseHttpResult<SarchItemBean>> searchMenuItem(@Query("search") String keyword);

    //19	营业额统计接口
    @GET("turnoverReport/")
    Observable<BaseHttpResult<SaleReportBean>> turnoverReport();

    //20	销售排行接口 ?day=2017-03-08
    @GET("salesRanking/")
    Observable<BaseHttpResult<SaleRankBean>> getSalesRank(@Query("day") String day);

    //21	提交投诉建议接口
    @FormUrlEncoded
    @POST
    Observable<BaseHttpResult<DataBean>> submitAdvice(@Url String url, @Field("content") String content);

    //22	修改登录密码接口
    @FormUrlEncoded
    @POST("modifyPassword/")
    Observable<BaseHttpResult<DataBean>> modifyPassword(@Field("old_password") String old_password,
        @Field("new_password") String new_password);

    //23	修改操作密码接口
    @FormUrlEncoded
    @POST("modifyOperationPassword/")
    Observable<BaseHttpResult<DataBean>> modifyOperationPassword(@Field("old_password") String old_password,
        @Field("new_password") String new_password);

    //24	订单详情接口
    @GET("orderList/{id}/")
    Observable<BaseHttpResult<OrderDetailBean>> getOrderDetail(@Path("id") int orderId);

    //25	所有已下架商品列表接口
    @GET("offMenuItemList/")
    Observable<BaseHttpResult<MenuItemBean>> getOffMenu();

    //26	营业/打烊开关接口
    //op_flag为操作标记，1代表开始营业，2代表打烊
    @FormUrlEncoded
    @POST("openOrClose/")
    Observable<BaseHttpResult<DataBean>> openOrClose(@Field("op_flag") String op_flag);

    //27	获取店铺打烊状态
    @GET("storeSettings/")
    Observable<BaseHttpResult<StoreSettingsBean>> getStoreSettings();

    //28	获取订单状态
    @GET("redDotStatus/")
    Observable<BaseHttpResult<RedDotStatusBean>> getRedDotStatus();

    //32	月销售额排行
    @GET("monthSalesRanking/")
    Observable<BaseHttpResult<SaleRankBean>> getMonthSalesRank(@Query("year") int year, @Query("month") int month);

    @GET
    Observable<BaseHttpResult<AppVersionBean>> getCheckAppVersion(@Url String url);

    @GET("getUserServer/")
    Observable<BaseHttpResult<UserServerBean>> getUserServer(@Query("usercode") String usercode);

    @FormUrlEncoded
    @POST("completeOrder/")
    Observable<BaseHttpResult<DataBean>> completeOrder(@Field("order_id") String orderId);

    @GET("completedOrderList/")
    Observable<BaseHttpResult<CompleteOrderBean>> getCompletedOrderList(@Query("page") int page);
}
