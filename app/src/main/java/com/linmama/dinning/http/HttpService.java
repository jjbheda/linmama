package com.linmama.dinning.http;

import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.bean.AppVersionBean;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.LoginBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.ShopBaseInfoBean;
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

    //处理中订单 取消订单        type：0 新订单中取消订单  1 预约单和当日单中取消取订单
    @FormUrlEncoded
    @POST("cancelOrder/")
    Observable<BaseHttpResult> cancelOrder(@Field("id") int id,@Field("type") int type);

    //已完成订单查询    搜索接口    搜索条件 订单号、用户名、电话号码
    @FormUrlEncoded
    @POST("finishedOrderListQuery/")
    Observable<BaseHttpResult<TakingOrderMenuBean>> getSearchFinishedOrderListData(@Field("search") String search);

    //已完成订单查询
    @FormUrlEncoded
    @POST("finishedOrderList/")
    Observable<BaseHttpResult<TakingOrderMenuBean>> getFinishedOrderListData
    (@Query("page") int page, @Field("start") String start, @Field("end") String end);

    //已完成订单查询     退款未完成 列表
    @POST("refundOrderList/")
    Observable<BaseHttpResult<TakingOrderMenuBean>> getRefundFailOrderData(@Query("page") int page);

    //已完成订单查询   退款未完成  搜索接口    搜索条件 订单号、用户名、电话号码
    @FormUrlEncoded
    @POST("refundOrderListQuery/")
    Observable<BaseHttpResult<TakingOrderMenuBean>> getSearchRefundFailOrderListData(@Query("page") int page,@Field("search") String search);

    //已完成订单查询     退款未完成 退款
    @FormUrlEncoded
    @POST("refundRetry/")
    Observable<BaseHttpResult> refundRetry(@Field("id") int id);

    //已完成订单查询     取消订单
    @FormUrlEncoded
    @POST("cancelFinishedOrder/")
    Observable<BaseHttpResult> cancelFinishedOrder(@Field("id") int id);

    //确认订单
    @FormUrlEncoded
    @POST("ensureOrder/")
    Observable<BaseHttpResult> commitOrder(@Field("id") String id);

    //完成订单
    @FormUrlEncoded
    @POST("finishOrder/")
    Observable<BaseHttpResult> finishOrder(@Field("id") int id);

    //店铺管理
    @POST("baseData/")
    Observable<BaseHttpResult<ShopBean>> getShopBaseData();

    //菜品分类列表接口
    @POST("category/")
    Observable<BaseHttpResult<List<MenuCategoryBean>>> getMenuCategory();

    //菜品分类下对应的菜品
    @FormUrlEncoded
    @POST("productList/")
    Observable<BaseHttpResult<ShopTotalBean>> getProductlistById(@Query("page") int page, @Field("id") int id);

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

    //店铺管理-营业分析-商品统计  type//0 当日单  1 预约次日单
    @FormUrlEncoded
    @POST("productSalesAnalysis/")
    Observable<BaseHttpResult<List<SaleRankBean>>> getProductAnalysisData(@Field("type") int type);

    //我的账单--历史账单
    @FormUrlEncoded
    @POST("historyBillQuery/")
    Observable<BaseHttpResult<List<AccountBeanItem>>> getHistoryBillQueryData(@Query("page") int page, @Field("type") int type);

    //我的账单--账单详情
    @FormUrlEncoded
    @POST("billDetail/")
    Observable<BaseHttpResult<AccountBeanItem>> getBillDetailData(@Field("date") String date);

    //我的账单--账单详情 --订单列表  type: 0//0 正常单  1调整单 默认查询正常单
    @FormUrlEncoded
    @POST("billDetailList")
    Observable<BaseHttpResult<SingleAccountBean>> getBillDetailListData(@Query("page") int page, @Field("date") String date, @Field("type") int type);

    //shopStatus设置-基本状态信息
    @POST("shopStatus/")
    Observable<BaseHttpResult<ShopBaseInfoBean>> getStoreInfo();

    //营业/打烊开关接口
    //op_flag为操作标记，注意这是表示当前状态 0关闭 1 开启
    @FormUrlEncoded
    @POST("setOpenStatus/")
    Observable<BaseHttpResult> openOrClose(@Field("status") int status);


}
