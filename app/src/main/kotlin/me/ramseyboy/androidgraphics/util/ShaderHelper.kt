package me.ramseyboy.androidgraphics.util

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.util.Log.*

object ShaderHelper {

    private val TAG = "ShaderHelper"

    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
    }

    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
    }

    private fun compileShader(type: Int, shaderCode: String): Int {
        val shaderObjectId = glCreateShader(type)

        if (shaderObjectId == 0) {
            d(TAG, "Could not create new shader")

            return 0
        }
        glShaderSource(shaderObjectId, shaderCode)
        glCompileShader(shaderObjectId)

        val compileStatus = IntArray(1)
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)

        d(TAG, glGetShaderInfoLog(shaderObjectId))

        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId)

            w(TAG, "Compilation of shader failed")
            return 0
        }


        return shaderObjectId
    }

    fun linkProgram(vertextShaderId: Int, fragmentShaderId: Int): Int {
        val programObjectId = glCreateProgram()

        if (programObjectId == 0) {
            w(TAG, "Could not create new program")

            return 0
        }

        glAttachShader(programObjectId, vertextShaderId)
        glAttachShader(programObjectId, fragmentShaderId)

        glLinkProgram(programObjectId)

        val linkStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0)

        d(TAG, glGetProgramInfoLog(programObjectId))
        if (linkStatus[0] == 0) {
            w(TAG, "Linking was unsuccessful")

            return 0
        }

        return programObjectId
    }

    fun validateProgram(programObjectid: Int): Boolean {
        glValidateProgram(programObjectid)

        val validateStatus = IntArray(1)

        glGetProgramiv(programObjectid, GL_VALIDATE_STATUS, validateStatus, 0)
        v(TAG, "Results of validating program: " + validateStatus[0] + "\nLog:" + glGetProgramInfoLog(programObjectid))

        return validateStatus[0] != 0
    }
}