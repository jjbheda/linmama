package com.xcxid.dinning.setting.login;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/12
 */

public class ModifyLoginPwdModel extends BaseModel {

    public void modifyPassword(@NonNull String oldPwd, @NonNull String newPwd,
                               @NonNull final ModifyLoginPwdHint hint) {
        if (null == hint)
            throw new RuntimeException("ModifyLoginPwdHint cannot be null.");

        httpService.modifyPassword(oldPwd, newPwd)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successModifyLoginPwd(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failModifyLoginPwd(e.getMessage());
                    }
                });
    }

    public interface ModifyLoginPwdHint {
        void successModifyLoginPwd(DataBean bean);

        void failModifyLoginPwd(String str);
    }
}
