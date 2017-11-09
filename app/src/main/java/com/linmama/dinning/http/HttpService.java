package com.linmama.dinning.http;

import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.bean.AppVersionBean;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.LoginBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.ShopSearchBean;
import com.linmama.dinning.bean.SingleAccountBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.base.BaseHttpResult;
import com.linmama.dinning.bean.CompleteOrderBean;
import com.linmama.dinning.bean.NonPayOrderBean;
import com.linmama.dinning.bean.QuitOrderBean;
import com.linmama.dinning.bean.RemindBean;
import com.linmama.dinning.bean.SaleReportBean;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.bean.StoreSettingsBean;
import com.linmama.dinning.bean.UserServerBean;
import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.goods.onsale.ShopItemBean;
import com.linmama.dinning.goods.onsale.ShopTotalBean;
import com.linmama.dinning.shop.bean.SaleRankBean;
import com.linmama.dinning.shop.bean.ShopBean;
import com.linmama.dinning.shop.bean.ShopSaleParseBean;
import com.linmama.dinning.shop.bean.BusinessParseBean;

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
    @POST("login/")
    Observable<BaseHttpResult<LoginBean>> login(@Field("username") String username,
                                                @Field("password") String pwd);
    //2	新订单列表接口
    @POST("newOrderList/")
    Observable<BaseHttpResult<List<LResultNewOrderBean>>> getNewOrder();

    //3	预约单列表接口     order_type：1预约单 0 当日单  range 0今天 1 明天 2全部
    @FormUrlEncoded
    @POST("pendingOrderList/")
    Observable<BaseHttpResult<TakingOrderMenuBean>> getReceivedOrder(@Query("page") int page, @Field("order_type") int order_type
            , @Field("range") String range);

    //处理中订单查询    order_type：1预约单 0 当日单  search 搜索条件 订单号、用户名、电话号码  page 默认1
    @FormUrlEncoded
    @POST("receivedQuery/")
    Observable<BaseHttpResult<TakingOrderMenuBean>> orderQuery(@Query("page") int page, @Field("order_type") int order_type
            , @Field("search") String search);

    //已完成订单查询    order_type：1预约单 0 当日单  search 搜索条件 订单号、用户名、电话号码  page 默认1
    @FormUrlEncoded
    @POST("finishedOrderList/")
    Observable<BaseHttpResult<TakingOrderMenuBean>> getFinishedOrderListData
    (@Query("page") int page, @Field("start") String start, @Field("end") String end);

    //确认订单
    @FormUrlEncoded
    @POST("ensureOrder/")
    Observable<BaseHttpResult> commitOrder(@Field("id") String id);

    //完成订单
    @FormUrlEncoded
    @POST("finishOrder/")
    Observable<BaseHttpResult> finishOrder(@Field("id") String id);

    //店铺管理
    @POST("baseData/")
    Observable<BaseHttpResult<ShopBean>> getShopBaseData();

    //菜品分类列表接口
    @POST("category/")
    Observable<BaseHttpResult<List<MenuCategoryBean>>> getMenuCategory();

    //菜品分类下对应的菜品
    @FormUrlEncoded
    @POST("productList/")
    Observable<BaseHttpResult<ShopTotalBean>> getProductlistById(@Field("id") int id);

    //商品搜索
    @FormUrlEncoded
    @POST("searchList/")
    Observable<BaseHttpResult<List<ShopSearchBean>>> getShopSearchData(@Field("search") String search);

    //下架商品列表
    @POST("undercarriage/")
    Observable<BaseHttpResult<List<ShopItemBean>>> getUnderCarriageData();

    //下架商品
    @FormUrlEncoded
    @POST("underProduct/")
    Observable<BaseHttpResult> underProduct(@Field("id") int id);

    //上架商品列表
    @FormUrlEncoded
    @POST("upProduct/")
    Observable<BaseHttpResult> upProduct(@Field("id") int id);

    // 店铺管理-营业分析
    @POST("businessAnalysis/")
    Observable<BaseHttpResult<ShopSaleParseBean>> getShopParseBaseData();

    //店铺管理-营业分析-历史营业分析 0当月  1 上月
    @FormUrlEncoded
    @POST("historyAnalysis/")
    Observable<BaseHttpResult<List<BusinessParseBean>>> getHistoryAnalysisData(@Field("type") int type);

    //店铺管理-营业分析-菜品分析  type://0 当日  1 昨日 2本月 3 上月
    @FormUrlEncoded
    @POST("productAnalysis/")
    Observable<BaseHttpResult<List<SaleRankBean>>> getProductAnalysisData(@Field("type") int type);

    //我的账单--历史账单
    @FormUrlEncoded
    @POST("historyBillQuery/")
    Observable<BaseHttpResult<List<AccountBeanItem>>> getHistoryBillQueryData(@Query("page") int page, @Field("type") int type);

    //我的账单--账单详情
    @FormUrlEncoded
    @POST("billDetail/")
    Observable<BaseHttpResult<AccountBeanItem>> getBillDetailData( @Field("date") String date);

    //我的账单--账单详情 --订单列表  type: 0//0 正常单  1调整单 默认查询正常单
    @FormUrlEncoded
    @POST("billDetailList")
    Observable<BaseHttpResult<SingleAccountBean>> getBillDetailListData(@Query("page") int page, @Field("date") String date, @Field("type") int type);

    //获取店铺打烊状态
    @GET("storeSettings/")
    Observable<BaseHttpResult<StoreSettingsBean>> getStoreSettings();


    //营业/打烊开关接口
    //op_flag为操作标记，0关闭 1 开启
    @FormUrlEncoded
    @POST("setOpenStatus/")
    Observable<BaseHttpResult> openOrClose(@Field("status") int status);



    //自动接单接口
    //status:当前APP状态 0关闭 1 开启
    @FormUrlEncoded
    @POST("autoEnsureOrder/")
    Observable<BaseHttpResult> openOrCloseOrder(@Field("status") int status);





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

    //8	取消订单接口 Id: id//返回的订单id  Type：0 新订单中取消订单  1 预约单和当日单中取消取订单
    @FormUrlEncoded
    @POST("cancelOrder/")
    Observable<BaseHttpResult> cancelOrder(@Field("id") String id,@Field("type") int type);

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
