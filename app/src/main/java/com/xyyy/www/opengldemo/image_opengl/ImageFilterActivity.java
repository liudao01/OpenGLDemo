package com.xyyy.www.opengldemo.image_opengl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xyyy.www.opengldemo.R;

public class ImageFilterActivity extends AppCompatActivity {

    private Button btUseFilter;
    private MyView myview;

    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        initView();
    }

    private void initView() {
        btUseFilter = findViewById(R.id.bt_use_filter);
        myview = findViewById(R.id.myview);

        btUseFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myview.setFilter(type++);
            }
        });
    }
}
