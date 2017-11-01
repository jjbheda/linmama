package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.RedDotStatusBean;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/16
 */

public class RedDotStatusModel extends BaseModel {

    public void getRedDotStatusBean(@NonNull final RedDotStatusHint hint) {
        if (null == hint)
            throw new RuntimeException("RedDotStatusHint cannot be null.");
    }

    public interface RedDotStatusHint {
        void successRedDotStatus(RedDotStatusBean bean);

        void failRedDotStatus(String failMsg);
    }
}
