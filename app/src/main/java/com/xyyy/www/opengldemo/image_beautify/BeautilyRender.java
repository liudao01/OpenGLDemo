package com.xyyy.www.opengldemo.image_beautify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.xyyy.www.opengldemo.LogUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author liuml
 * @explain
 * @time 2019/1/17 10:38
 */
public class BeautilyRender implements GLSurfaceView.Renderer {

    private int type = 0;

    private float[] matrix = new float[16];

    private Context context;


    public BeautilyRender(Context context) {
        this.context = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //绘制区域
        GLES20.glViewport(0, 0, width, height);
        //旋转
//        Matrix.rotateM(matrix, 0, 180, 1, 0, 0);\

        LogUtil.d("onSurfaceChanged = " + type);

    }

    public void setType(int type) {
        this.type = type;
        LogUtil.d("设置当前type = " + type);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        LogUtil.d("调用绘制");


    }

    private int loadTexture(int src) {
        //创建纹理
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);

//        textureId = textureIds[0];

        //绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);

        //设置参数 环绕（超出纹理坐标范围）：
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        //过滤（纹理像素映射到坐标点
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //图片内容
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), src);
        if (bitmap == null) {
            return 0;
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        return textureIds[0];
    }
}
