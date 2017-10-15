package com.linmama.dinning.setting.advice;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.R;
import com.linmama.dinning.widget.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/13
 */

public class AdviceActivity extends BasePresenterActivity<AdvicePresenter> implements AdviceContract.AdviceView {
    @BindView(R.id.toolTitle)
    Toolbar toolbar;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.etContent)
    ClearEditText etContent;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    protected AdvicePresenter loadPresenter() {
        return new AdvicePresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() > 0) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btnSubmit)
    public void submit(View view) {
        showDialog("加载中...");
        mPresenter.submitAdvice("http://work.xcxid.com/api/submitAdvice/", getContent());
    }

    @Override
    public String getContent() {
        return etContent.getText().toString().trim();
    }

    public boolean checkNull() {
        return TextUtils.isEmpty(getContent());
    }

    @Override
    public void submitAdviceSuccess(DataBean bean) {
        dismissDialog();
        ViewUtils.showSnack(content, "提交成功");
    }

    @Override
    public void submitAdviceFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }
}
