package com.example.openaiclient.ui;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import com.example.openaiclient.R;

import java.util.Objects;

public class MyProgressDialog extends Dialog {
    private int layoutResId;
    private LottieAnimationView lottieAnimationView;

    public MyProgressDialog(@NonNull Context context) {
        super(context);
    }

    public MyProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public MyProgressDialog(@NonNull Context context, int themeResId,int layoutResId){
        super(context, themeResId);
        this.layoutResId = layoutResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layoutResId!=0){
            setContentView(layoutResId);
        }else {
            setContentView(R.layout.lottie_dialog);
        }
        lottieAnimationView = findViewById(R.id.base_lottie_anim);
        setDialogStyle();

    }

    public void removeAnim() {
        if (lottieAnimationView != null) {
            lottieAnimationView.clearAnimation();
        }
    }


    private void setDialogStyle() {
        WindowManager.LayoutParams layoutParams = Objects.requireNonNull(getWindow()).getAttributes();
        layoutParams.gravity = Gravity.CENTER; //设置dialog居中
        Resources resources = getContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int width = displayMetrics.widthPixels; //获取屏幕宽度
        int height = displayMetrics.heightPixels; //获取屏幕高度
        getWindow().setLayout(2*width / 3, 2*width / 3); //设置宽高
        setCanceledOnTouchOutside(false);//设置dialog点击空白可取消
    }

}
