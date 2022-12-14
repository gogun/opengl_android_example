package com.example.opengl_example

import android.content.Context
import android.opengl.GLES20
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.opengl.GLUtils
import java.nio.IntBuffer

object TextureUtils {
    fun loadTexture(context: Context, resourceId: Int): Int {
        // создание объекта текстуры
        val textureIds = IntBuffer.allocate(1)
        GLES20.glGenTextures(1, textureIds)
        if (textureIds[0] == 0) {
            return 0
        }

        // получение Bitmap
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureIds)
            return 0
        }

        // настройка объекта текстуры
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()

        // сброс target
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        return textureIds[0]
    }
}