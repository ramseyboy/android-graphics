package me.ramseyboy.androidgraphics

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class AirHockeyActivity : Activity() {

    private var glSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView?.setEGLContextClientVersion(2)
        glSurfaceView?.setRenderer(AirHockeyRenderer(this))
        setContentView(glSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView?.onPause()
    }
}
