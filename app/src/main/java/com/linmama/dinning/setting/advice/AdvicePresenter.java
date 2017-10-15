package com.linmama.dinning.setting.advice;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.mvp.IModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/16
 */

public class AdvicePresenter extends BasePresenter<AdviceActivity> implements AdviceContract.AdvicePresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new AdviceModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("SubmitAdvice", models[0]);
        return map;
    }

    @Override
    public void submitAdvice(String url, String content) {
        if (null == getIView())
            return;
        if (getIView().checkNull()) {
            return;
        }
        ((AdviceModel) getiModelMap().get("SubmitAdvice")).submitAdvice(url, content,
                new AdviceModel.AdviceHint() {
                    @Override
                    public void successAdvice(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().submitAdviceSuccess(bean);
                    }

                    @Override
                    public void failAdvice(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().submitAdviceFail(failMsg);
                    }
                });
    }
}
