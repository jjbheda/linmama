package com.xcxid.dinning.mvp;

/**
 * mvp之v
 * View 视图: 负责显示数据、提供友好界面跟用户交互。
 * Activity和Fragment以及View的子类体现在这一层:
 *      Activity加载UI视图、设置监听再交由Presenter处理一些工作,需要持有相应的Presenter。
 *      在每一次有相应交互的时候,调用Presenter的相关方法。
 */
public interface IView {

}
