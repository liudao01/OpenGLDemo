package com.xyyy.www.opengldemo.image_beautify;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author liuml
 * @explain
 * @time 2019/1/17 10:24
 */
public class BeautilyView extends GLSurfaceView {


    private BeautilyRender render;

    public BeautilyView(Context context) {
        super(context, null);
    }

    public BeautilyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        render = new BeautilyRender(getContext());
        setRenderer(render);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setFilter(int type) {
        if (render != null) {
            render.setType(type);
            requestRender();//手动刷新 调用一次
        }
    }


}
