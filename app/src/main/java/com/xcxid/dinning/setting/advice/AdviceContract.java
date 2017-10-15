package com.xcxid.dinning.setting.advice;

import com.xcxid.dinning.bean.DataBean;

/**
 * Created by jingkang on 2017/3/16
 */

public class AdviceContract {

    public interface AdviceView {
        String getContent();

        void submitAdviceSuccess(DataBean bean);

        void submitAdviceFail(String failMsg);
    }

    public interface AdvicePresenter {
        void submitAdvice(String url, String content);
    }
}
