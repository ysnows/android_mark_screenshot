package com.windhike.tuto.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.view.View;


import com.windhike.annotation.model.PreferenceConnector;
import com.windhike.fastcoding.CommonFragmentActivity;
import com.windhike.fastcoding.base.BaseFragment;
import com.windhike.tuto.R;
import com.windhike.fastcoding.widget.PromptManager;
import com.windhike.tuto.widget.FloatButton;
import com.xiaomi.market.sdk.UpdateResponse;
import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;
import com.xiaomi.market.sdk.XiaomiUpdateListener;
import com.windhike.tuto.easytouch.service.DrawMenuService;

import butterknife.BindView;

/**
 * author:gzzyj on 2017/7/20 0020.
 * email:zhyongjun@windhike.cn
 */
public class MainPageFragment extends BaseFragment {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.fbSetting)
    FloatButton fbSetting;
    private PictureAdapter mAdapter;

    @Override
    public int getLayouId() {
        return R.layout.fragment_tab_viewpager;
    }

    private static final String TAG = "MainPageFragment";
    @Override
    public void initView() {
        super.initView();
//        checkForUpdate();
        mAdapter = new PictureAdapter(getChildFragmentManager());
        viewpager.setAdapter(mAdapter);
        tabs.setupWithViewPager(viewpager);
        fbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonFragmentActivity.start(getActivity(),SettingFragment.class.getName(),null);
            }
        });
    }

    private void checkForUpdate() {
        XiaomiUpdateAgent.setUpdateAutoPopup(true);
        XiaomiUpdateAgent.setUpdateListener(new XiaomiUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.STATUS_UPDATE:
                        // ???????????? UpdateResponse??????????????????????????????
                        // ??????????????????????????????????????????MD5?????????????????????????????????????????????
                        // ???????????? SDK??????????????????????????????????????????
                        XiaomiUpdateAgent.arrange();
                        break;
                    case UpdateStatus.STATUS_NO_UPDATE:
                        // ???????????? UpdateResponse???null
                        break;
                    case UpdateStatus.STATUS_NO_WIFI:
                        // ???????????????WiFi???????????????WiFi??????????????? UpdateResponse???null
                        break;
                    case UpdateStatus.STATUS_NO_NET:
                        // ??????????????? UpdateResponse???null
                        break;
                    case UpdateStatus.STATUS_FAILED:
                        // ????????????????????????????????????????????????????????? UpdateResponse???null
                        break;
                    case UpdateStatus.STATUS_LOCAL_APP_FAILED:
                        // ??????????????????????????????????????????????????? UpdateResponse???null
                        break;
                    default:
                        break;
                }
            }
        });
        XiaomiUpdateAgent.update(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkFloatWindowPermission()){
            PreferenceConnector.writeBoolean(getActivity(),PreferenceConnector.KEY_FLOAT_OPENED,true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getActivity().startForegroundService(new Intent(getActivity(), DrawMenuService.class));
            }else {
                getActivity().startService(new Intent(getActivity(), DrawMenuService.class));
            }
        }else{
            showAppSettingPage();
        }
    }

    private void showAppSettingPage() {
        String title = "????????????";
        String confirm = "?????????";
        String cancel = "??????";
        String appName = getString(R.string.app_name);
        StringBuilder sb = new StringBuilder(String.format("?????????-??????-%s-???????????????",appName));
        sb.append("????????????????????????????????????????????????");
        sb.append(",????????????????????????");
        PromptManager.getInstance(getActivity()).showDialog(title, sb.toString(), cancel, confirm, new PromptManager.OnClickBtnCallback() {
            @Override
            public void confirmClick() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(String.format("package:%s", getActivity().getPackageName())));
                startActivity(intent);
            }

            @Override
            public void cancelClick() {

            }
        });
    }

    public boolean checkFloatWindowPermission(){
        boolean hasPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //23??????????????????????????????????????? ??????????????????
            if(!Settings.canDrawOverlays(getActivity())) {
                hasPermission = false;
            }
        }else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED){
            hasPermission = false;
        }
        return hasPermission;
    }

    private static class PictureAdapter extends FragmentPagerAdapter{
        private String[] titles = {"??????","??????"};

        public PictureAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new AlbumListFragment();
            }
            return new AnnotationListFragment();
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
