package com.xyyy.www.opengldemo.image_opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.xyyy.www.opengldemo.LogUtil;
import com.xyyy.www.opengldemo.R;
import com.xyyy.www.opengldemo.XYShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author liuml
 * @explain
 * @time 2019/1/17 10:38
 */
public class MyViewRender implements GLSurfaceView.Renderer {

    private int type = 0;

    private float[] matrix = new float[16];
    //顶点坐标
    private final float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };


    //纹理坐标
    //纹理坐标
    private final float[] textureData = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };

    private Context context;

    private FloatBuffer vertexBuffer;//顶点buffer
    private FloatBuffer textureBuffer;//纹理buffer
    private int program;
    private int avPosition;//顶点坐标
    private int afPosition;//纹理坐标
    private int textureId;//纹理id保存


    public MyViewRender(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);


        textureBuffer = ByteBuffer.allocateDirect(textureData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureData);
        textureBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //加载shader
        String vertexSource = XYShaderUtil.readRawText(context, R.raw.img_vertex_shader);
        String fragmentSource = XYShaderUtil.readRawText(context, R.raw.fragment_shader1);
//        String fragmentSource = XYShaderUtil.readRawText(context, R.raw.img_fragment_shader);
        program = XYShaderUtil.createProgram(vertexSource, fragmentSource);

        if (program > 0) {
            avPosition = GLES20.glGetAttribLocation(program, "av_Position");
            afPosition = GLES20.glGetAttribLocation(program, "af_Position");

            //加载纹理
            loadTexture(R.mipmap.test);

        }


    }


    private int getCurrentProgram( int type) {
        String vertexSource = XYShaderUtil.readRawText(context, R.raw.img_vertex_shader);
        String fragmentSource;
        if (type % 2 == 0) {
            fragmentSource = XYShaderUtil.readRawText(context, R.raw.fragment_shader1);
        } else {
            fragmentSource = XYShaderUtil.readRawText(context, R.raw.fragment_shader2);
        }

        program = XYShaderUtil.createProgram(vertexSource, fragmentSource);

        if (program > 0) {
            avPosition = GLES20.glGetAttribLocation(program, "av_Position");
            afPosition = GLES20.glGetAttribLocation(program, "af_Position");
            //加载纹理
            loadTexture(R.mipmap.test);
        }
        return program;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //绘制区域
        GLES20.glViewport(0, 0, width, height);
        //旋转
//        Matrix.rotateM(matrix, 0, 180, 1, 0, 0);\

        LogUtil.d("onSurfaceChanged = "+type);

    }

    public void setType(int type) {
        this.type = type;
        LogUtil.d("设置当前type = "+type);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        LogUtil.d("调用绘制");
        getCurrentProgram(type);
        //清屏缓冲区
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //利用颜色清屏
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);


        //让program可用
        GLES20.glUseProgram(program);
        //顶点坐标
        GLES20.glEnableVertexAttribArray(avPosition);
        GLES20.glVertexAttribPointer(avPosition, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        //纹理坐标
        GLES20.glEnableVertexAttribArray(afPosition);
        GLES20.glVertexAttribPointer(afPosition, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
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
