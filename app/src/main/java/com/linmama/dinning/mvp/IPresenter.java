package com.linmama.dinning.mvp;

/**
 * mvp之P
 * Presenter 表示器: 起着逻辑控制处理的角色,控制各业务流程,负责调控View与Model之间的简介交互。
 *      获取Model层的数据之后构建View层; 收到View层UI上的反馈命令后分发处理逻辑, 交给Model层做业务操作。
 *      也可以决定View层的各种操作。
 */
public interface IPresenter<V extends IView> {

    /**
     * @param view 绑定
     */
    void attachView(V view);

    /**
     * 防止内存的泄漏,清除presenter与activity之间的绑定
     */
    void detachView();

    /**
     * @return 获取View
     */
    IView getIView();

}
