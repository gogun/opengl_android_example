package com.example.opengl_example

import android.content.Context
import android.content.res.Resources.NotFoundException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import kotlinx.coroutines.runBlocking

object FileUtils {
    fun readTextFromRaw(context: Context, resourceId: Int): String {

        val inputStream = context.resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use(BufferedReader::readText)
    }
}