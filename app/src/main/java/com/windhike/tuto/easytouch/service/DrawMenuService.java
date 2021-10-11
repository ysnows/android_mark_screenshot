package com.windhike.tuto.easytouch.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.View;

import com.windhike.fastcoding.BaseApplication;
import com.windhike.fastcoding.CommonFragmentActivity;
import com.windhike.tuto.R;
import com.windhike.tuto.easytouch.screenshot.NewScreenShotUtilImpl;
import com.windhike.tuto.easytouch.screenshot.OldScreenShotUtilImpl;
import com.windhike.tuto.easytouch.screenshot.ScreenShotUtil;
import com.windhike.tuto.easytouch.view.ColorHolder;
import com.windhike.tuto.easytouch.view.FloatSettingView;
import com.windhike.tuto.easytouch.view.IconHolder;
import com.windhike.tuto.easytouch.view.MenuHolder;

/**
 * author:gzzyj on 2017/8/7 0007.
 * email:zhyongjun@windhike.cn
 */
public class DrawMenuService extends Service {
    private IconHolder mIconHolder;
    private MenuHolder mMenuHolder;
    private ColorHolder mColorHolder;
    private LocalBroadcastManager mLocalManager;
    private ScreenShotUtil mScreenShot;

    private void createNotificationChannel() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent("android.intent.action.MAIN"); //点击后跳转的界面，可以设置跳转数据

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                //.setContentTitle("SMI InstantView") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("bug pusher is running......") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FloatSettingView.ACTION_CAPTURE_FINISHED);
        filter.addAction(FloatSettingView.ACTION_CAPTURE_END);
        mLocalManager.registerReceiver(mReceiver, filter);
        mIconHolder = new IconHolder(this);
        mMenuHolder = new MenuHolder(this);
        mColorHolder = new ColorHolder(this);
        mIconHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIconHolder.onDestory();
                mMenuHolder.initView();
                mMenuHolder.setMenuCallback(mMenuSwitchHolderCallback);
            }
        });
        mIconHolder.initView();
    }

    private HolderSwitchCallback mMenuSwitchHolderCallback = new HolderSwitchCallback() {

        @Override
        public View.OnClickListener obtainMenuColorPickerListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mMenuHolder.onDestory();
                    mColorHolder.initView();
                    mColorHolder.setMenuCallback(mMenuSwitchHolderCallback);
                }
            };
        }

        @Override
        public View.OnClickListener obtainMenuOutsideListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToIconMode();
                }
            };
        }

        @Override
        public void switchToIconMode() {
            mColorHolder.onDestory();
            mMenuHolder.onDestory();
            mIconHolder.initView();
        }

        @Override
        public void onCaptureRequest() {
            mIconHolder.onDestory();
            mColorHolder.onDestory();
            mMenuHolder.onDestory();
            Context context = DrawMenuService.this;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && (mScreenShot == null || NewScreenShotUtilImpl.data == null)) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(CommonFragmentActivity.BUNDLE_KEY_TRANSLUCENT, true);
                bundle.putBoolean(CommonFragmentActivity.BUNDLE_KEY_FULLSCREEN, false);
                if (!((BaseApplication) getApplication()).isForground) {
                    CommonFragmentActivity.start(context, "com.windhike.tuto.fragment.TranslucentFragment",
                            bundle, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    CommonFragmentActivity.start(context, "com.windhike.tuto.fragment.TranslucentFragment",
                            bundle);
                }
            } else {
                if (mScreenShot == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mScreenShot = new NewScreenShotUtilImpl(context);
                    } else {
                        mScreenShot = OldScreenShotUtilImpl.getInstance(context);
                    }
                }
                mScreenShot.startScreenshot();
            }
        }
    };
    private static final String TAG = "DrawMenuService";
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            final String action = intent.getAction();
            if (FloatSettingView.ACTION_CAPTURE_FINISHED.equals(action)) {
                mMenuSwitchHolderCallback.switchToIconMode();
            }

            if (FloatSettingView.ACTION_CAPTURE_END.equals(action)) {

                if (mScreenShot == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mScreenShot = new NewScreenShotUtilImpl(context);
                    } else {
                        mScreenShot = OldScreenShotUtilImpl.getInstance(context);
                    }
                }
                mScreenShot.startScreenshot();
            }
        }
    };

    public interface HolderSwitchCallback {
        View.OnClickListener obtainMenuColorPickerListener();

        View.OnClickListener obtainMenuOutsideListener();

        void switchToIconMode();

        void onCaptureRequest();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalManager.unregisterReceiver(mReceiver);
        mIconHolder.onDestory();
        mMenuHolder.onDestory();
        mColorHolder.onDestory();
    }
}
