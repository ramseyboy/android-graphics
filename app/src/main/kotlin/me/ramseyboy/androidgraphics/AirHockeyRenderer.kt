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

        private val A_COLOR = "a_Color"
        private val COLOR_COMPONENT_COUNT = 3

        private val A_POSITION = "a_Position"

        private val POSITION_COMPONENT_COUNT = 2

        private val BYTES_PER_FLOAT = 4

        private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    private var aColorLocation = 0
    private var aPositionLocation = 0

    private val vertextData: FloatBuffer

    private var program: Int = 0

    private var isLoggingEnabled = false

    private val programId: Int = 0

    init {
        isLoggingEnabled = context.resources.getBoolean(R.bool.isLoggingEnabled)

        val tableVerticesWithTriangles = floatArrayOf(
                //X Y R G B

                //triangle fan
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

                //line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,

                // mallet
                0f, -0.25f, 0f, 0f, 1f,
                0f, 0.25f, 1f, 0f, 0f
        )

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

        aColorLocation = glGetAttribLocation(program, A_COLOR)

        aPositionLocation = glGetAttribLocation(program, A_POSITION)

        vertextData.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertextData)

        glEnableVertexAttribArray(aPositionLocation)


        vertextData.position(POSITION_COMPONENT_COUNT)
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertextData)

        glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_COLOR_BUFFER_BIT)

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        glDrawArrays(GL_LINES, 6, 2)

        glDrawArrays(GL_POINTS, 8, 1)

        glDrawArrays(GL_POINTS, 9, 1)
    }
}
