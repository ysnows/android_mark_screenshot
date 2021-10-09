package com.windhike.tuto.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.windhike.fastcoding.base.BaseFragment;
import com.windhike.tuto.R;
import com.windhike.tuto.presenter.SettingAdapter;

import butterknife.BindView;

/**
 * author:gzzyj on 2017/8/7 0007.
 * email:zhyongjun@windhike.cn
 */
public class SettingFragment extends BaseFragment {
    private static final int REQUEST_PROJECTION_CODE = 2;
    @BindView(R.id.recycerView)
    RecyclerView recycerView;
    @BindView(R.id.vBack)
    View vBack;
    @BindView(R.id.head)
    LinearLayout head;

    @Override
    public int getLayouId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {
        super.initView();
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycerView.setLayoutManager(manager);
        recycerView.setAdapter(new SettingAdapter(getActivity()));
    }

}
