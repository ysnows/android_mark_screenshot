package com.windhike.tuto.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.windhike.annotation.model.PreferenceConnector;
import com.windhike.fastcoding.CommonFragmentActivity;
import com.windhike.fastcoding.base.BaseFragment;
import com.windhike.tuto.R;
import com.zyongjun.easytouch.ScreenRecorder;
import com.zyongjun.easytouch.screenshot.NewScreenShotUtilImpl;
import com.zyongjun.easytouch.view.FloatSettingView;

/**
 * author:gzzyj on 2017/9/9 0009.
 * email:zhyongjun@windhike.cn
 */
public class TranslucentFragment extends BaseFragment {
    private static final int REQUEST_PROJECTION_CODE = 2;
    private LocalBroadcastManager mLocalBroadcastManager;
    private boolean hasRequestCapture;

    public static void start(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CommonFragmentActivity.BUNDLE_KEY_TRANSLUCENT, true);
        bundle.putBoolean(CommonFragmentActivity.BUNDLE_KEY_FULLSCREEN, true);
        CommonFragmentActivity.start(context, TranslucentFragment.class.getName(), bundle);
    }

    @Override
    public int getLayouId() {
        return R.layout.fragment_translucent;
    }

    @Override
    public void initView() {
        super.initView();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        requestCapturePermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_PROJECTION_CODE) {

                mLocalBroadcastManager.sendBroadcast(new Intent(FloatSettingView.ACTION_CAPTURE_FINISHED));
            }
            onBackPressed();
            return;
        }
        if (requestCode == REQUEST_PROJECTION_CODE) {
            hasRequestCapture = true;
            PreferenceConnector.writeBoolean(getActivity(), PreferenceConnector.KEY_CAPTURE_OPENED, true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Intent service = new Intent(getContext(), ScreenRecorder.class);
                service.putExtra("code", resultCode);
                service.putExtra("data", data);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getActivity().startForegroundService(service);
                } else {
                    NewScreenShotUtilImpl.setData(data);
                    mLocalBroadcastManager.sendBroadcast(new Intent(FloatSettingView.ACTION_CAPTURE_END));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasRequestCapture) {
            onBackPressed();
        }
    }

    private boolean requestCapturePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getActivity().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_PROJECTION_CODE);
            return true;
        }
        return false;
    }

}
