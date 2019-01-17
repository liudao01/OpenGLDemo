package com.xyyy.www.opengldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xyyy.www.opengldemo.image_opengl.ImageFilterActivity;

public class MainActivity extends AppCompatActivity {

    private Button tvFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tvFilter = findViewById(R.id.tv_filter);
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageFilterActivity.class);
                startActivity(intent);
            }
        });
    }
}
