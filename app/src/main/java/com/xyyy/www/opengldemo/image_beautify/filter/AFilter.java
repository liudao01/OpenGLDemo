package com.xyyy.www.opengldemo.image_beautify.filter;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import com.xyyy.www.opengldemo.LogUtil;
import com.xyyy.www.opengldemo.utils.OpenGlUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author liuml
 * @explain
 * @time 2019/1/18 14:49
 */
public abstract class AFilter {

    //传入的Resources
    protected Resources mRes;

    /**
     * 程序句柄
     */
    protected int mProgram;
    /**
     * 顶点坐标句柄
     */
    protected int mHPosition;
    /**
     * 纹理坐标句柄
     */
    protected int mHCoord;
    /**
     * 总变换矩阵句柄
     */
    protected int mHMatrix;
    /**
     * 默认纹理贴图句柄
     */
    protected int mHTexture;

    private int textureType = 0;      //默认使用Texture2D0
    private int textureId = 0;

    /**
     * 顶点坐标Buffer
     */
    protected FloatBuffer mVerBuffer;

    /**
     * 纹理坐标Buffer
     */
    protected FloatBuffer mTexBuffer;


    private int program;
    private int avPosition;//顶点坐标
    private int afPosition;//纹理坐标

//    private float[] matrix = Arrays.copyOf(OM, 16);

    public AFilter(Resources mRes) {
        this.mRes = mRes;
        initBuffer();
    }


    //顶点坐标
    private float pos[] = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };

    //纹理坐标
    private float[] coord = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };

    /**
     * 实现此方法，完成程序的创建，可直接调用createProgram来实现
     */
    protected abstract void onCreate();

    protected abstract void onSizeChanged(int width, int height);

    /**
     * Buffer初始化
     */
    protected void initBuffer(){
        ByteBuffer a= ByteBuffer.allocateDirect(32);
        a.order(ByteOrder.nativeOrder());
        mVerBuffer=a.asFloatBuffer();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);

        ByteBuffer b= ByteBuffer.allocateDirect(32);
        b.order(ByteOrder.nativeOrder());
        mTexBuffer=b.asFloatBuffer();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }



    public void draw(){
        Log.e("videoo", "---卡主了？ 16  "+getClass());
        onClear();
        Log.e("videoo", "---卡主了？ 17");
        onUseProgram();
        Log.e("videoo", "---卡主了？ 18");
        onSetExpandData();
        Log.e("videoo", "---卡主了？ 19");
        onBindTexture();
        Log.e("videoo", "---卡主了？ 20");
        onDraw();
        Log.e("videoo", "---卡主了？ 21");
    }
    /**
     *
     * 1.清除画布
     */
    protected void onClear(){

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * 2.使用GL程序
     */
    protected void onUseProgram() {
        //让program可用
        GLES20.glUseProgram(mProgram);
    }

    /**
     * 3. 设置其他扩展数据
     */
    protected void onSetExpandData() {
//        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0);
    }
    /**
     * 4. 绑定默认纹理
     */
    protected void onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTextureId());
        GLES20.glUniform1i(mHTexture, textureType);
    }

    /**
     * 5. 启用顶点坐标和纹理坐标进行绘制
     */
    protected void onDraw() {

        //顶点坐标
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition, 2, GLES20.GL_FLOAT, false, 0, mVerBuffer);
        //纹理坐标
        GLES20.glEnableVertexAttribArray(mHCoord);
        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHCoord);
    }


    public final int getTextureId() {
        return textureId;
    }
    public final void setTextureId(int textureId) {
        this.textureId = textureId;
    }



    /**
     * 加载shader
     * @param shaderType
     * @param source
     * @return
     */
    public static int loadShader(int shaderType, String source) {

        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compile = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compile, 0);
            if (compile[0] != GLES20.GL_TRUE) {
                LogUtil.e("shader compile error");
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }


    /**
     * 创建GL程序 获取
     * @param vertex
     * @param fragment
     */
    protected final void createProgram(String vertex, String fragment){
        mProgram= uCreateGlProgram(vertex,fragment);
        mHPosition= GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHCoord= GLES20.glGetAttribLocation(mProgram,"vCoord");
        mHMatrix= GLES20.glGetUniformLocation(mProgram,"vMatrix");
        mHTexture= GLES20.glGetUniformLocation(mProgram,"vTexture");
    }

    /**
     * 从资源文件里创建GL程序
     * @param vertex
     * @param fragment
     */
    protected final void createProgramByAssetsFile(String vertex, String fragment) {
        createProgram(OpenGlUtils.uRes(vertex), OpenGlUtils.uRes(fragment));
    }

    /**
     * 创建GL程序
     * @param vertexSource
     * @param fragmentSource
     * @return
     */
    public static int uCreateGlProgram(String vertexSource, String fragmentSource) {

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }
        //创建一个渲染程序
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            //将着色器程序添加到渲染程序中
            GLES20.glAttachShader(program,vertexShader);
            GLES20.glAttachShader(program,fragmentShader);
            //链接源程序
            GLES20.glLinkProgram(program);
            int[] lineSatus = new int[1];
            //检查链接源程序是否成功
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, lineSatus, 0);
            if (lineSatus[0] != GLES20.GL_TRUE) {
                LogUtil.e("link program error");
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

}
