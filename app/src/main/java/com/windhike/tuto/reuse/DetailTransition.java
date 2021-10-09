package com.windhike.tuto.reuse;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.transition.ChangeBounds;
import androidx.transition.TransitionSet;

/**
 * author:gzzyj on 2017/9/17 0017.
 * email:zhyongjun@windhike.cn
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DetailTransition extends TransitionSet {
    public DetailTransition() {
        init();
    }

    // 允许资源文件使用
    public DetailTransition(Context context, AttributeSet attrs) {
        super();
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds());
//                addTransition(new ChangeTransform()).
//                addTransition(new ChangeImageTransform());
    }
}


