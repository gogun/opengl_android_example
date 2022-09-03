package com.example.opengl_example

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.core.content.res.ResourcesCompat
import com.example.opengl_example.ShaderUtils.createProgram
import com.example.opengl_example.ShaderUtils.createShader
import com.example.opengl_example.TextureUtils.loadTexture
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var vertexData: FloatBuffer
    private var aPositionLocation = 0
    private var aTextureLocation = 0
    private var uTextureUnitLocation = 0
    private var uMatrixLocation = 0
    private var programId = 0
    private val mMatrix = FloatArray(16)
    private var texture = 0

    override fun onSurfaceCreated(arg0: GL10, arg1: EGLConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        createAndUseProgram()
        getLocations()
        prepareData()
    }

    private fun createAndUseProgram() {
        val vertexShaderId = createShader(context, GLES20.GL_VERTEX_SHADER, R.raw.vertex_shader)
        val fragmentShaderId = createShader(context, GLES20.GL_FRAGMENT_SHADER, R.raw.fragment_shader)
        programId = createProgram(vertexShaderId, fragmentShaderId)
        GLES20.glUseProgram(programId)
    }

    private fun getLocations() {
        aPositionLocation = GLES20.glGetAttribLocation(programId, "a_Position")
        aTextureLocation = GLES20.glGetAttribLocation(programId, "a_Texture")
        uTextureUnitLocation = GLES20.glGetUniformLocation(programId, "u_TextureUnit")
        uMatrixLocation = GLES20.glGetUniformLocation(programId, "u_Matrix")
    }

    private fun prepareData() {
        val vertices = floatArrayOf(
           // x    y   x   y
            -1f,  1f, 0f, 0f,
            -1f, -1f, 0f, 1f,
             1f,  1f, 1f, 0f,
             1f, -1f, 1f, 1f
        )
        vertexData = ByteBuffer
            .allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(vertices)
        texture = loadTexture(context, R.drawable.box)
    }

    override fun onSurfaceChanged(arg0: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        calculateScale(width, height)
        GLES20.glUniformMatrix4fv(
            uMatrixLocation,
            1,
            false,
            mMatrix,
            0
        )
    }

    private fun calculateScale(screenWidth: Int, screenHeight: Int) {
        Matrix.setIdentityM(mMatrix, 0)
        val screenRatio = screenWidth / screenHeight.toFloat()
        val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.box, null)
        drawable ?: return
        val textureRation = 1f
        var scaleX = textureRation / screenRatio
        var scaleY = 1f
        if (scaleX > 1f) {
            scaleY = 1 / scaleX
            scaleX = 1f
        }
        Matrix.scaleM(
            mMatrix,
            0,
            scaleX,
            scaleY,
            1f
        )
    }

    override fun onDrawFrame(arg0: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        bindData()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        ShaderUtils.checkGlError("end of drawing frame")
    }

    private fun bindData() {
        // координаты вершин
        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation, POSITION_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, vertexData
        )
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        // координаты текстур
        vertexData.position(POSITION_COUNT)
        GLES20.glVertexAttribPointer(
            aTextureLocation, TEXTURE_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, vertexData
        )
        GLES20.glEnableVertexAttribArray(aTextureLocation)

        // помещаем текстуру в target 2D юнита 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)

        // юнит текстуры
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }

    companion object {
        private const val POSITION_COUNT = 2
        private const val TEXTURE_COUNT = 2
        private const val STRIDE = (POSITION_COUNT + TEXTURE_COUNT) * 4
    }
}