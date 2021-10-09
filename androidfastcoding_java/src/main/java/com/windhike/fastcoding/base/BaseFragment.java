package com.windhike.fastcoding.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author:gzzyj on 2017/7/14 0014.
 * email:zhyongjun@windhike.cn
 */

public abstract class BaseFragment extends Fragment{
    protected View mRootView;
    private Unbinder unbinder;

    public String getTransactionTag() {
        return getClass().getSimpleName();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayouId(),container,false);
            unbinder = ButterKnife.bind(this, mRootView);
            initView();
        }else{
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            parent.removeAllViews();
        }
        return mRootView;
    }

    public void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public abstract int getLayouId();

    public boolean onBackPressed() {
        if (getActivity() != null) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
