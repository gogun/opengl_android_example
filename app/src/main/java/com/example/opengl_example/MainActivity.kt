package com.example.opengl_example

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(OpenGLRenderer(this))
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        setContentView(glSurfaceView)
    }

}