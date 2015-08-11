package com.example.administrator.cookiebar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Administrator on 15-7-13.
 */
public class CookieBar {
    private RelativeLayout cookieBarLayout;
    private TextView titleView;
    private TextView action;
    public static final int LENGTH_SHORT = 500;
    public static final int LENGTH_LONG = 2000;
    private static final int ANIMATOR_DURATION = 200;
    private int length;
    private static CookieBar _this;
    private AnimatorSet showAnimator, disAnimator;
    private boolean isDismiss = true;

    public static CookieBar make(Context context, View rootView, String title, int duration) {
        if (rootView == null) {
            throw new NullPointerException();
        }
        ViewGroup rootGroup = (ViewGroup) rootView.getRootView();
        if (rootGroup.getChildCount() > 1) {
            int count = rootGroup.getChildCount();
            for (int i = count - 1; i > 0; i--) {
                rootGroup.removeView(rootGroup.getChildAt(i));
            }
        }
        _this = new CookieBar();
        _this.cookieBarLayout = (RelativeLayout) LayoutInflater.from(context)
                .inflate(R.layout.cookiebar, null);
        _this.titleView = (TextView) _this.cookieBarLayout.findViewById(R.id.title);
        _this.cookieBarLayout.setTranslationY(rootGroup.getMeasuredHeight());
        _this.action = (TextView) _this.cookieBarLayout.findViewById(R.id.action);
        _this.titleView.setText(title);
        _this.length = duration;
        rootGroup.addView(_this.cookieBarLayout);
        _this.cookieBarLayout.getLayoutParams().height = rootView.getMeasuredHeight() / 10;
        _this.showAnimator = new AnimatorSet();
        _this.disAnimator = new AnimatorSet();
        _this.showAnimator.play(ObjectAnimator.ofFloat(rootView, "translationY",
                0.0f, 0 - _this.cookieBarLayout.getLayoutParams().height)).with(
                ObjectAnimator.ofFloat(_this.cookieBarLayout, "translationY",
                        rootGroup.getMeasuredHeight(),
                        rootGroup.getMeasuredHeight() - _this.cookieBarLayout.getLayoutParams().height));
        _this.showAnimator.setDuration(ANIMATOR_DURATION);
        _this.disAnimator.play(ObjectAnimator.ofFloat(rootView, "translationY",
                0 - _this.cookieBarLayout.getLayoutParams().height, 0.0f)).with(
                ObjectAnimator.ofFloat(_this.cookieBarLayout, "translationY",
                        rootGroup.getMeasuredHeight() -
                                _this.cookieBarLayout.getLayoutParams().height,
                        rootGroup.getMeasuredHeight()));
        _this.disAnimator.setDuration(ANIMATOR_DURATION);
        _this.showAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _this.dismiss();
                    }
                }, _this.length);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _this.dismiss();
                v.setOnClickListener(null);
            }
        });
        return _this;
    }

    public void show() {
        _this.isDismiss = false;
        _this.showAnimator.start();
    }

    private void dismiss() {
        if (!_this.isDismiss) {
            _this.disAnimator.start();
            _this.isDismiss = true;
        }
    }

    public CookieBar setAction(String action, View.OnClickListener onClickListener) {
        _this.action.setVisibility(View.VISIBLE);
        _this.action.setText(action);
        _this.action.setOnClickListener(onClickListener);
        return this;
    }

    public CookieBar setActionTextColor(int color) {
        _this.action.setTextColor(color);
        return this;
    }

    public CookieBar setBackgroundColor(int color) {
        _this.cookieBarLayout.setBackgroundColor(color);
        return this;
    }

    public CookieBar setTitleColor(int color) {
        _this.titleView.setTextColor(color);
        return this;
    }
}
