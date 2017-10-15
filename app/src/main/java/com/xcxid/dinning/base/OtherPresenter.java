package com.xcxid.dinning.base;

import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.mvp.IPresenter;
import com.xcxid.dinning.mvp.IView;

import java.lang.ref.WeakReference;

public abstract class OtherPresenter<M extends IModel, V extends IView> implements IPresenter {
    private WeakReference actReference;
    protected M iModel;

    public M getiModel() {
        iModel = loadModel();
        return iModel;
    }

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
        try {
            return (V) actReference.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract M loadModel();
}
