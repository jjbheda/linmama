package com.linmama.dinning.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.order.order.OrderFragment;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.AlarmManagerUtils;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.NotificationUtils;
import com.linmama.dinning.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by jingkang on 2017/3/20
 */

public class WarnAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        // long intervalMillis = intent.getLongExtra("intervalMillis", 0);
        // if (intervalMillis != 0) {
        //      AlarmManagerUtils.setAlarmTime(context, System.currentTimeMillis() + intervalMillis,
        //          intent);
        // }
        Bundle bundle = intent.getExtras();
        LogUtils.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (AlarmManagerUtils.ALARM_ACTION.equals(intent.getAction())) {
            String msg = intent.getStringExtra(Constants.RECEIVER_MSG);
            int type = intent.getIntExtra(Constants.RECEIVER_TYPE, 0);
            NotificationUtils.showNotification(context, msg, type);
        } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtils.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            dealBundle(bundle);
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtils.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[JPushReceiver] 用户点击打开了通知");
            //打开自定义的Activity
        	Intent i = new Intent(context, MainActivity.class);
        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtils.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtils.d(TAG, "[JPushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtils.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private String[] dealBundle(Bundle bundle) {
        String msgType = "";
        String orderId = "";
//        String alert = "";
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.d("JPushReceiver", "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        if (myKey.equals(Constants.RECEIVER_JPUSH_MSGTYPE)) {
                            msgType = json.optString(myKey);
                            LogUtils.d("msgType", msgType);
                        } else if (myKey.equals(Constants.RECEIVER_JPUSH_DATA)) {
                            JSONObject jsonOrder = json.optJSONObject(myKey);
                            orderId = jsonOrder.optString(Constants.RECEIVER_JPUSH_ORDERID);
                            LogUtils.d("orderId", orderId);
                        }
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e("JPushReceiver", "Get message extra JSON error!");
                }
            }
//            else if (key.equals(JPushInterface.EXTRA_ALERT)) {
//                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_ALERT))) {
//                    LogUtils.d("JPushReceiver", "This message has no Extra data");
//                    continue;
//                }
//                alert = bundle.getString(JPushInterface.EXTRA_ALERT);
//                LogUtils.d("alert", alert);
//            }
        }
        return new String[]{msgType, orderId};
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        boolean isAutoReceiveOrder = (boolean) SpUtils.get(Constants.AUTO_RECEIVE_ORDER, false);
		if (OrderFragment.isForeground && !isAutoReceiveOrder) {
			Intent msgIntent = new Intent(OrderFragment.NEWORDER_REFRESH_ACTION);
			context.sendBroadcast(msgIntent);
		}
        String[] extras = dealBundle(bundle);
        String msg = "您有订单消息";
        if (extras[0].equals("1")) {
            msg = "您有新的小不点自助订单";
            NotificationUtils.showNotification(context, msg, 1);
            if (isAutoReceiveOrder) {
                Intent intent = new Intent(context, ReceiveOrderService.class);
                intent.putExtra(Constants.ORDER_AUTO_RECEIVE, extras[1]);
                context.startService(intent);
            }
        } else if (extras[0].equals("2")) {
            msg = "顾客催单啦";
            NotificationUtils.showNotification(context, msg, 2);
        } else if (extras[0].equals("3")) {
            msg = "有顾客退单";
            NotificationUtils.showNotification(context, msg, 3);
        }
    }

    private String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
