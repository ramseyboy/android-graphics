package me.ramseyboy.androidgraphics

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.ramseyboy.androidgraphics.app.R
import me.ramseyboy.androidgraphics.util.ResourceReader
import me.ramseyboy.androidgraphics.util.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {

    companion object {

        private val U_COLOR = "u_Color"

        private val A_POSITION = "a_Position"

        private val POINT_SIZE = "point_size"

        private val POSITION_COMPONENT_COUNT = 2

        private val BYTES_PER_FLOAT = 4
    }

    private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var pointSize: Int = 0
    private val vertextData: FloatBuffer

    private var program: Int = 0

    private var isLoggingEnabled = false

    private val programId: Int = 0

    init {
        isLoggingEnabled = context.resources.getBoolean(R.bool.isLoggingEnabled)

        val tableVerticesWithTriangles = floatArrayOf(//triangle 1
                -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,

                //triangle 2
                -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

                //line 1
                -0.5f, 0f, 0.5f, 0f,

                // mallet
                -0f, -0.25f, 0f, 0.25f,

                // puck
                0f, 0f,

                //border
                0f, -1f, 0f, 1f)

        vertextData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer()

        vertextData.put(tableVerticesWithTriangles)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glClearColor(0f, 0f, 0f, 0f)
        val vertextShaderSource = ResourceReader.readFromResource(context, R.raw.simple_vertex_shader)
        val fragmentShaderSource = ResourceReader.readFromResource(context, R.raw.simple_fragment_shader)

        val vertexShader = ShaderHelper.compileVertexShader(vertextShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
        ShaderHelper.validateProgram(program)
        glUseProgram(program)

        uColorLocation = glGetUniformLocation(program, U_COLOR)
        pointSize = glGetUniformLocation(program, POINT_SIZE)

        aPositionLocation = glGetAttribLocation(program, A_POSITION)

        vertextData.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertextData)

        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_COLOR_BUFFER_BIT)

        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        glDrawArrays(GL_TRIANGLES, 0, 6)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_LINES, 6, 2)

        glUniform1f(pointSize, 20.0f)

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 8, 1)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 9, 1)

        glUniform1f(pointSize, 15.0f)

        glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 10, 1)

        glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_LINES, 11, 2)
    }
}
