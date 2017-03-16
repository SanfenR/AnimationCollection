package com.example.bannerview.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedList;

/**
 * Created on 2017/3/16
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class BannerView extends LinearLayout implements Runnable {

    public static final int DEFAULT_MAX_COUNT = 3;
    public static final long DEFAULT_DELETE_TIME = 8 * 1000;

    public int maxCount = DEFAULT_MAX_COUNT;
    public int currentCount = 0;

    public boolean isAutoRemoveView = true;
    public long deleteTime = DEFAULT_DELETE_TIME;

    public float weight = 1.0f;

    LinkedList<View> list = new LinkedList<>();

    Handler mHandler = new Handler();

    public void isAutoRemoveView(boolean bool) {
        isAutoRemoveView = bool;
        if (isAutoRemoveView) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(this, deleteTime);
        }
    }


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setAnimation();
    }


    public void addBannerView(View view) {
        if (list.size() == DEFAULT_MAX_COUNT) {
            removeBannerView();
        }
        list.add(view);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, weight));
        addView(view);

    }


    public void removeBannerView() {
        if (list.size() > 0) {
            View view = list.removeFirst();
            removeView(view);
        }
    }


    public void setAnimation() {
        LayoutTransition mTransition = new LayoutTransition();
        mTransition.setDuration(1000);
        mTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
        mTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
        setLayoutTransition(mTransition);
    }


    @Override
    public void run() {
        removeBannerView();
        if (isAutoRemoveView) {
            mHandler.postDelayed(this, deleteTime);
        }
    }
}
