package com.pangff.animation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-3-13
 * Time: 上午11:29
 * To change this template use File | Settings | File Templates.
 */
public class OrigamiMesh {
    private RectF origamiRect;

    private boolean animationFromBottom;

    private Bitmap topBitmap, bottomBitmap;

    private float factor;

    private Shader contentShader, shadowShader;

    private FloatBuffer vertexBuffer, shadowColorBuffer, textureCoordBuffer;

    private PointF topLeftPoint = new PointF();

    private int[] textureIds;

    public OrigamiMesh() {
        /**
         * 设置多边形buffer
         */
        //一个四边形是12*4，3个四边形，包括上下四边形和阴影的四边形
        ByteBuffer bb = ByteBuffer.allocateDirect(
                12 * 4 * 3);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();

        /**
         * 设置颜色buffer
         */
        //给阴影使用的
        // 一个顶点一个颜色值，共4个顶点
        //一个颜色值有4个浮点数
        //每个浮点数4个字节
        bb = ByteBuffer.allocateDirect(
                16 * 4);
        bb.order(ByteOrder.nativeOrder());
        shadowColorBuffer = bb.asFloatBuffer();

        /**
         * 设置纹理坐标buffer
         */
        //2个多边形
        //每个多边形4个顶点，每个顶点2个坐标值（二维的）
        //每个浮点数4字节
        textureCoordBuffer = ByteBuffer.allocateDirect(4 * 2 * 4 * 2)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int i = 0; i < 2; i++) {
            textureCoordBuffer.put(new float[]{
                    0, 0,
                    0, 1,
                    1, 0,
                    1, 1
            });
        }

        /**
         * 设置内容shader
         */
        String vertexShader =
                "        uniform mat4 uProjectionM;\n" +
                        "attribute vec3 aPosition;\n" +
//                        "attribute vec4 aColor;\n" +
                        "attribute vec2 aTextureCoord;\n" +
//                        "varying vec4 vColor;\n" +
                        "varying vec2 vTextureCoord;\n" +
                        "void main() {\n" +
                        "  gl_Position = uProjectionM * vec4(aPosition, 1.0);\n" +
//                        "  vColor = aColor;\n" +
                        "  vTextureCoord = aTextureCoord;\n" +
                        "}\n";
        String fragmentShader =
                "        precision mediump float;\n" +
//                        "uniform vec4 aColor;\n" +
//                        "varying vec4 vColor;\n" +
                        "varying vec2 vTextureCoord;\n" +
                        "uniform sampler2D sTexture;\n" +
                        "void main() {\n" +
                        "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
//                        "  gl_FragColor.rgb *= vColor.rgb;\n" +
//                        "  gl_FragColor = mix(vColor, gl_FragColor, vColor.a);\n" +
//                        "  gl_FragColor.a = 1.0;\n" +
                        "}\n";

        if(contentShader==null){
            contentShader = new Shader();
            contentShader.setProgram(vertexShader, fragmentShader);
        }

        /**
         * 设置阴影shader
         */
        vertexShader =
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 aColor;" +
                        "varying vec4 vColor;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  vColor = aColor;" +
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}";
        fragmentShader =
                "precision mediump float;" +
                        "varying vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor= vColor;\n" +
                        "}";
        shadowShader = new Shader();
        shadowShader.setProgram(vertexShader, fragmentShader);
    }

    public void setAnimationFromBottom(boolean animationFromBottom) {
        this.animationFromBottom = animationFromBottom;
    }

    public void setOrigamiRect(RectF origamiRect) {
        this.origamiRect = origamiRect;
    }

    public void setBitmap(Bitmap bitmap) {
        //将bitmap设置为上下两部分bitmap
        topBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
        bottomBitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight() / 2);
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public PointF getTopLeftPoint() {
        return topLeftPoint;
    }

    public void draw(float[] projectionMatrix) {
//        Log.d("origami","draw ...");

        this.initPolygonsData();

        this.drawTextures(projectionMatrix);
        this.drawShadow(projectionMatrix);
    }

    private void drawTextures(float[] projectionMatrix) {
//        Log.d("origami","draw texture ");
        /**
         * 绘制头部
         */
        this.contentShader.useProgram();
        GLES20.glUniformMatrix4fv(contentShader.getHandle("uProjectionM"), 1, false, projectionMatrix, 0);

        int aPosition = this.contentShader.getHandle("aPosition");
//        int aColor = this.contentShader.getHandle("aColor");
        int aTextureCoord = this.contentShader.getHandle("aTextureCoord");

        //设置顶点
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false,
                3 * 4, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aPosition);


        if (textureIds == null) {
            textureIds = new int[2];
            GLES20.glGenTextures(textureIds.length, textureIds, 0);

            for (int id : textureIds) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            }
        }

        //上半部分纹理
        GLES20.glVertexAttribPointer(aTextureCoord, 2, GLES20.GL_FLOAT, false,
                0, textureCoordBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoord);

        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, topBitmap, 0);
        GLES20.glDisable(GLES20.GL_TEXTURE_2D);
//        topBitmap.recycle();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[1]);

        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bottomBitmap, 0);
        GLES20.glDisable(GLES20.GL_TEXTURE_2D);
//        topBitmap.recycle();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4);
    }

    private void drawShadow(float[] projectionMatrix) {
        /**
         * 画阴影部分
         */
        //设置阴影顶点
        shadowShader.useProgram();
        vertexBuffer.position(12 * 2);
        GLES20.glVertexAttribPointer(shadowShader.getHandle("vPosition"), 3, GLES20.GL_FLOAT, false,
                3 * 4, vertexBuffer);
        GLES20.glEnableVertexAttribArray(shadowShader.getHandle("vPosition"));

        //设置顶点颜色
        GLES20.glVertexAttribPointer(shadowShader.getHandle("aColor"), 4, GLES20.GL_FLOAT, false, 0,
                this.shadowColorBuffer);
        GLES20.glEnableVertexAttribArray(shadowShader.getHandle("aColor"));

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        //设置投影矩阵
        GLES20.glUniformMatrix4fv(shadowShader.getHandle("uMVPMatrix"), 1, false, projectionMatrix, 0);
        //画阴影多边形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisable(GLES20.GL_BLEND);
    }

    private void initPolygonsData() {
        float[] topPolygon = null, bottomPolygon = null, shadowPolygon = null;

        float deltaYO = this.factor==0?0:1;//(float) Math.abs((origamiRect.top - origamiRect.bottom) / 2 );
        float deltaY = (float) Math.abs((origamiRect.top - origamiRect.bottom) / 2  * Math.sin(this.factor * Math.PI / 2));

        float zO = 0;
        float xO = Math.abs(5f * origamiRect.left / (5f + zO));
        
        float z = (float) Math.cos(this.factor * Math.PI / 2f);
        float z00 = (float) Math.sin(this.factor * Math.PI / 2f);
        float x = Math.abs(5f * origamiRect.left / (5f + z));
        float xOO = Math.abs(5f * origamiRect.left / (5f - z));

        if (!animationFromBottom) {
            topPolygon = new float[]{
                    origamiRect.left, origamiRect.top, 0,   //左上
                    -x, origamiRect.top - deltaY, 0,        //左下
                    origamiRect.right, origamiRect.top, 0,  //右上
                    x, origamiRect.top - deltaY, 0          //右下
            };

//            bottomPolygon = new float[]{
//                    -x, origamiRect.top - deltaY, 0,
//                    origamiRect.left, origamiRect.top - 2 * deltaY, 0,
//                    x, origamiRect.top - deltaY, 0,
//                    origamiRect.right, origamiRect.top - 2 * deltaY, 0
//            };
            bottomPolygon = new float[]{
                    -xO, origamiRect.top - deltaYO, 0,
                    origamiRect.left, origamiRect.top - 2 * deltaYO, 0,
                    xO, origamiRect.top - deltaYO, 0,
                    origamiRect.right, origamiRect.top - 2 * deltaYO, 0
            };
        } else {
            //从下往上
//            bottomPolygon = new float[]{
//                    -x, origamiRect.bottom + deltaY, 0,
//                    origamiRect.left, origamiRect.bottom, 0,
//                    x, origamiRect.bottom + deltaY, 0,
//                    origamiRect.right, origamiRect.bottom, 0
//            };
//        		Log.e("ddd", "X:"+x);
//        		Log.e("ddd", "deltaY:"+deltaY);
//        		Log.e("ddd", "xO:"+xO);
//        		Log.e("ddd", "deltaYO:"+deltaYO);
//        		
//        		Log.e("ddd", "origamiRect.left:"+origamiRect.left);
//        		Log.e("ddd", "origamiRect.bottom:"+origamiRect.bottom);
//        		Log.e("ddd", "origamiRect.right:"+origamiRect.right);
//        		Log.e("ddd", "origamiRect.top:"+origamiRect.top);
            topPolygon = new float[]{
                    -xOO, origamiRect.bottom + 2 * deltaY, 0,
                    -xO, origamiRect.bottom + deltaYO, 0,
                    xOO, origamiRect.bottom + 2 * deltaY, 0,
                    xO, origamiRect.bottom + deltaYO, 0
            };
            bottomPolygon = new float[]{
                    -xO, origamiRect.top - deltaYO, 0,
                    origamiRect.left, origamiRect.top - 2 * deltaYO, 0,
                    xO, origamiRect.top - deltaYO, 0,
                    origamiRect.right, origamiRect.top - 2 * deltaYO, 0
            };
        }

        //设置左上顶点值
        this.topLeftPoint.x = topPolygon[0];
        this.topLeftPoint.y = topPolygon[1];

        shadowPolygon = bottomPolygon;

        //设置顶点buffer
        vertexBuffer.clear();
        this.vertexBuffer.put(topPolygon);
        this.vertexBuffer.put(bottomPolygon);
        this.vertexBuffer.put(shadowPolygon);
        this.vertexBuffer.position(0);

        //设置阴影顶点颜色buffer
        shadowColorBuffer.clear();
        shadowColorBuffer.put(new float[]{
                0, 0, 0, 1 - factor,
                0, 0, 0, 0,
                0, 0, 0, 1 - factor,
                0, 0, 0, 0
        });

        shadowColorBuffer.position(0);
        textureCoordBuffer.position(0);
    }
}
