package com.xcxid.dinning.base;

import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.mvp.IPresenter;
import com.xcxid.dinning.mvp.IView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public abstract class BasePresenter<V extends IView> implements IPresenter {
    private WeakReference actReference;

    @Override
    public void attachView(IView iView) {
        actReference = new WeakReference(iView);
    }

    @Override
    public void detachView() {
        if (actReference != null) {
            actReference.clear();
            actReference = null;
        }
    }

    @Override
    public V getIView() {
        V v;
        try {
            v = (V) actReference.get();
        } catch (Exception e) {
            v = null;
        }
        return v;
    }

    public abstract HashMap<String, IModel> getiModelMap();

    /**
     * @param models
     * @return
     * 添加多个model,如有需要
     */
    public abstract HashMap<String, IModel> loadModelMap(IModel... models);

}
