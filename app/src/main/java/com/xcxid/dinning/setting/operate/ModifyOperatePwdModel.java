package com.xcxid.dinning.setting.operate;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/12
 */

public class ModifyOperatePwdModel extends BaseModel {

    public void modifyOperatePassword(@NonNull String oldPwd, @NonNull String newPwd,
                               @NonNull final ModifyOperatePwdHint hint) {
        if (null == hint)
            throw new RuntimeException("ModifyLoginPwdHint cannot be null.");

        httpService.modifyOperationPassword(oldPwd, newPwd)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successModifyOperatePwd(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failModifyOperatePwd(e.getMessage());
                    }
                });
    }

    public interface ModifyOperatePwdHint {
        void successModifyOperatePwd(DataBean bean);

        void failModifyOperatePwd(String failMsg);
    }
}
