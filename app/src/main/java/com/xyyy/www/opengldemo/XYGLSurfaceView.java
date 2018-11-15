package com.xyyy.www.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author liuml
 * @explain
 * @time 2018/11/15 11:17
 */
public class XYGLSurfaceView extends GLSurfaceView {
    public XYGLSurfaceView(Context context) {
        this(context,null);
    }

    public XYGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置OpenGL版本
        setEGLContextClientVersion(2);
        setRenderer(new XYRender(getContext()));

    }
}
