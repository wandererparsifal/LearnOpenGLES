package com.example.test6;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yangming on 18-11-28.
 */
public class MyRender implements GLSurfaceView.Renderer {

    private Camera mCamera;

    private SurfaceTexture mSurfaceTexture;

    private OnFrameListener mOnFrameListener;

    private String mVertexShaderCode;

    private String mFragmentShaderCode;

    private FloatBuffer mVertexBuffer;

    private FloatBuffer mCoordBuffer;

    private int mProgram;

    private int mPositionHandler;

    private int mCoordHandler;

    private int mTextureHandler;

    private int mMatrixHandler;

    private float[] mMVPMatrix = new float[16];

    private final float[] sPos = {
            -1.0f, 1.0f,    //左上角
            -1.0f, -1.0f,   //左下角
            1.0f, 1.0f,     //右上角
            1.0f, -1.0f     //右下角
    };

    private final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    public MyRender(String v, String f) {
        this.mVertexShaderCode = v;
        this.mFragmentShaderCode = f;
    }

    public void setOnFrameListener(OnFrameListener listener) {
        this.mOnFrameListener = listener;
    }

    /**
     * 加载制定shader的方法
     *
     * @param shaderType shader的类型  GLES20.GL_VERTEX_SHADER   GLES20.GL_FRAGMENT_SHADER
     * @param sourceCode shader的脚本
     * @return shader索引
     */
    private int loadShader(int shaderType, String sourceCode) {
        // 创建一个新shader
        int shader = GLES20.glCreateShader(shaderType);
        // 若创建成功则加载shader
        if (shader != 0) {
            // 加载shader的源代码
            GLES20.glShaderSource(shader, sourceCode);
            // 编译shader
            GLES20.glCompileShader(shader);
            // 存放编译成功shader数量的数组
            int[] compiled = new int[1];
            // 获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {//若编译失败则显示错误日志并删除此shader
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createTexture() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    private void createProgram() {
        //将背景设置为灰色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        //申请底层空间
        ByteBuffer bb = ByteBuffer.allocateDirect(sPos.length * 4);
        bb.order(ByteOrder.nativeOrder());
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(sPos);
        mVertexBuffer.position(0);

        ByteBuffer dd = ByteBuffer.allocateDirect(sCoord.length * 4);
        dd.order(ByteOrder.nativeOrder());
        mCoordBuffer = dd.asFloatBuffer();
        mCoordBuffer.put(sCoord);
        mCoordBuffer.position(0); //获取片元着色器的vColor成员的句柄

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);

        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);

        GLES20.glDetachShader(mProgram, vertexShader);
        GLES20.glDetachShader(mProgram, fragmentShader);
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

        //获取顶点着色器的vPosition成员句柄
        mPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");

        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");

        mCoordHandler = GLES20.glGetAttribLocation(mProgram, "vCoordinate");

        mTextureHandler = GLES20.glGetUniformLocation(mProgram, "vTexture");

        GLES20.glEnableVertexAttribArray(mPositionHandler);
        //传入顶点坐标
        GLES20.glVertexAttribPointer(mPositionHandler, 2,
                GLES20.GL_FLOAT, false,
                0, mVertexBuffer);

        GLES20.glEnableVertexAttribArray(mTextureHandler);
        //传入纹理坐标
        GLES20.glVertexAttribPointer(mCoordHandler, 2,
                GLES20.GL_FLOAT, false,
                0, mCoordBuffer);
    }

    private void draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);

        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);

        mSurfaceTexture.updateTexImage();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        createProgram();
        mCamera = Camera.open(0);
        int textureId = createTexture();
        mSurfaceTexture = new SurfaceTexture(textureId);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                if (mOnFrameListener != null) {
                    mOnFrameListener.onFrame();
                }
            }
        });
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();

        GLES20.glViewport(0, 0, width, height);

        float[] mProjectMatrix = new float[16];
        float[] mCameraMatrix = new float[16];

        float ratio = (float) width / height;
        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -ratio, ratio, 1, 7);// 3和7代表远近视点与眼睛的距离，非坐标点
        Matrix.setLookAtM(mCameraMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);// 3代表眼睛的坐标点
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mCameraMatrix, 0);
        Matrix.rotateM(mMVPMatrix, 0, 270, 0, 0, 1);

        parameters.setFocusMode("auto");
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        draw();
    }
}
