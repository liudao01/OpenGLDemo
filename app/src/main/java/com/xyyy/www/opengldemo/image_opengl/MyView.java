package com.xyyy.www.opengldemo.image_opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author liuml
 * @explain
 * @time 2019/1/17 10:24
 */
public class MyView extends GLSurfaceView {



    public MyView(Context context) {
        super(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new MyViewRender(getContext()));
    }


}
