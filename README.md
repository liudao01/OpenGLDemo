# OpenGLDemo
OpenGLDemo


[TOC]
# 概念:

## 在OpenGL中  顶点坐标系

不管在什么屏幕或者设备 都会映射到这个坐标系上
比如想绘制上半区域 那就从左边的-1 到右边的 1 从0.0 到1.0

![image](//ws3.sinaimg.cn/large/958c5b69ly1fx8peigm2cj20ns0nuwhe.jpg)

更加详细的可以看这个文章
https://blog.piasy.com/2016/06/07/Open-gl-es-android-2-part-1/


在 OpenGL 里有两个最基本的概念：Vertex 和 Fragment。一切图形都从 Vertix 开始，Vertix  序列围成了一个图形。

那什么是 Fragment 呢？为此我们需要了解一下光栅化（Rasterization）：光栅化是把点、线、三角形映射到屏幕上的像素点的过程（每个映射区域叫一个 Fragment），也就是生成 Fragment 的过程。通常一个 Fragment 对应于屏幕上的一个像素，但高分辨率的屏幕可能会用多个像素点映射到一个 Fragment，以减少 GPU 的工作。
## 绘制范围
绘制坐标范围
```java
float[] vertexData = {
        -1f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f
    };

```
绘制上半部分就用上面的数组

## 为坐标分配本地内存地址

```
FloatBuffer vertexBuffer;
    vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    vertexBuffer.position(0);

```
分析:
1. vertexData.length * 4 是内存的长度 因为是float 类型 4字节 所以 乘以4
2. order(ByteOrder.nativeOrder()) 对齐方式.在内存当中 是以打断对其还是小端对齐
3. asFloatBuffer() 转成floatBuffer
4. .put(vertexData); 把顶点的值映射给他 put 进去
5. vertexBuffer.position(0);  最后把指针的变量指向开头 就是0


## 着色器 shader 编写

OpenGL 渲染需要两种 Shader：Vertex Shader 和 Fragment Shader。
1. Vertex 顶点着色器
每个 Vertex 都会执行一遍 Vertex Shader，以确定 Vertex 的最终位置

2. fragment 片元着色器
每个 Fragment 都会执行一次 Fragment Shader，以确定每个 Fragment 的颜色，其 main 函数中必须设置 gl_FragColor 全局变量，它将作为该 Fragment 的最终颜色。

### Vertex Shader

vertex_shader.glsl

```
attribute vec4 av_Position;
void main(){
    gl_Position = av_Position;
}
```
注： attribute 只能在vertex中使用

gl_Position OpenGL规定的写法
av_Position 自己定义的 4个向量  x,y,z,w. w理解为摄像机
attribute 是声明av_position的类型

### Fragment Shader

fragment_shader.glsl

```
precision mediump float;
uniform vec4 af_Potion;
void main(){
    gl_FragColor = af_Potion;
}


```
注： uniform 用于在application中向vertex和fragment中传递值。


分析:
1. gl_FragColor 和上面的类似 OpenGL ES 规定的写法
2. vec4 这里是代表 颜色的 r,g,b,a
3. uniform 用于在application中向vertex和fragment中传递值,比如如果刚开始是红色的,想改成黄色,就要在程序中获取af_Position
4. precision mediump float;  顶点着色器中默认精度  中等的

## OpenGL ES 加载Shader
OpenGL ES加载Shader

1、创建shader（着色器：顶点或片元）
```

	int shader = GLES20.glCreateShader(shaderType);

```
2、加载shader源码并编译shader

```
    GLES20.glShaderSource(shader, source);
	GLES20.glCompileShader(shader);
```

3、检查是否编译成功：

```
GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
```

4、创建一个渲染程序：

```
int program = GLES20.glCreateProgram();
```

5、将着色器程序添加到渲染程序中：

```
GLES20.glAttachShader(program, vertexShader);
```

6、链接源程序：

```
GLES20.glLinkProgram(program);
```


7、检查链接源程序是否成功

```
GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
```

8、得到着色器中的属性：

```
int aPositionHandl  = GLES20.glGetAttribLocation(programId, "av_Position");
//这里av_Position 就是上面对应的av_Position
```


9、使用源程序：

```
GLES20.glUseProgram(programId);
```

10、使顶点属性数组有效 ：

```
GLES20.glEnableVertexAttribArray(aPositionHandl);
```

11、为顶点属性赋值：

```

GLES20.glVertexAttribPointer(aPositionHandl, 2, GLES20.GL_FLOAT, false, 8,
	 vertexBuffer);
```
分析:
1. 这里2 是代表vertexData 中只使用了x,y 坐标系点 所以是2
2. GLES20.GL_FLOAT 声明的是float[] vertexData 所以是Float
3. false 是否做归一化处理 如果vertexData 里面有-3 3 这些 不在-1到1之间的书 把它设置为ture 那么就会强制转化到-1到1之间
4. 8 每个点的大小 vertexData中一个点是两个值 一个值是4个字节(float 类型) 所以是8 ,第一个点就是8的长度 第二个点就是从8开始下一个长度16

12、绘制图形：

```
GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
```

1. GL_TRIANGLES三角形
2. 起点是0 总共有3个点.


效果:

![image](//wx3.sinaimg.cn/large/958c5b69ly1fx8wxb0i1rj20k612gab8.jpg)


代码地址

https://github.com/liudao01/OpenGLDemo/tree/master




