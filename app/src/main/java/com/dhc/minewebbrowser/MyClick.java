package com.dhc.minewebbrowser;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MyClick implements OnClickListener {
    private TextView textshow;

    //把文本框作为参数传入
    public MyClick(TextView txt) {
        textshow = txt;
    }

    @Override
    public void onClick(View v) {
        //点击后设置文本框显示的文字
        textshow.setText("点击了按钮!");
    }
}