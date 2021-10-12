package com.windhike.tuto.easytouch.view;

import android.content.Context;
import android.graphics.PixelFormat;
import androidx.core.view.ViewCompat;
import com.windhike.tuto.easytouch.service.DrawMenuService;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.windhike.tuto.R;

/**
 * author:gzzyj on 2017/8/7 0007.
 * email:zhyongjun@windhike.cn
 */
public class ColorHolder extends BaseTouchViewHolder{
    private Context mContext;
    private ColorPickerView mColorPickerView;
    private DrawMenuService.HolderSwitchCallback mCallback;
    public ColorHolder(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void initView() {
        if (mColorPickerView != null) {
            mWindowManager.addView(mColorPickerView,getViewLayoutParams());
            return;
        }
        mColorPickerView = new ColorPickerView(mContext);
        mWindowManager.addView(mColorPickerView,getViewLayoutParams());
    }

    public void setMenuCallback(DrawMenuService.HolderSwitchCallback callback) {
        mCallback = callback;
        mColorPickerView.setSettingCallback(mCallback);
    }

    @Override
    public WindowManager.LayoutParams getViewLayoutParams() {
        WindowManager.LayoutParams windowLayoutParams = new WindowManager.LayoutParams();
//        windowLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        windowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        windowLayoutParams.format = PixelFormat.RGBA_8888;
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        windowLayoutParams.windowAnimations= R.style.IconViewAnimator;
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
//        windowLayoutParams.alpha = 0.6f;
        windowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        return windowLayoutParams;
    }

    @Override
    public View getRootView() {
        return mColorPickerView;
    }

    @Override
    public void onDestory() {
        if(mColorPickerView !=null&& ViewCompat.isAttachedToWindow(mColorPickerView)) {
            mWindowManager.removeView(mColorPickerView);
        }
    }
}
