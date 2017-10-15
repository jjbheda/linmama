package com.linmama.dinning.setting.login;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

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
