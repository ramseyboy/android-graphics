package me.ramseyboy.androidgraphics.util

import android.content.Context
import java.io.InputStreamReader
import java.util.*

object ResourceReader {

    fun readFromResource(context: Context, resourceId: Int): String {
        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val reader = InputStreamReader(inputStream)

            return Scanner(inputStream).useDelimiter("\\A").next()

        } catch (e: Exception) {
            throw RuntimeException("Could not open resource: " + resourceId, e)
        }

    }
}