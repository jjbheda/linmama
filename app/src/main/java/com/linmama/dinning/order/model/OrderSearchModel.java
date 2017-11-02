package com.linmama.dinning.order.model;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseHttpResult;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.bean.UserServerBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.login.UserServerModel;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

import java.util.List;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderSearchModel extends BaseModel {

    public void searchOrder(int page,int order_type,String search,final OrderSearchServerHint hint){
        if (hint == null)
            throw new RuntimeException("OrderSearchServerHint cannot be null.");

//        httpService.getReceivedOrder(page,1,"2")
//                .compose(new CommonTransformer<BaseHttpResult<List<TakingOrderBean>>>())
//                .subscribe(new CommonSubscriber<BaseHttpResult<List<TakingOrderBean>>>(LmamaApplication.getInstance()) {
//                    @Override
//                    public void onNext(BaseHttpResult<List<TakingOrderBean>> bean) {
//                        hint.successServer(bean);
//                    }
//
//                    @Override
//                    public void onError(ApiException e) {
//                        super.onError(e);
//                        hint.failServer(e.getMessage());
//                    }
//                });

    }

    public interface OrderSearchServerHint {
        void successServer(BaseHttpResult<List<TakingOrderBean>> bean);

        void failServer(String failMsg);
    }

}
