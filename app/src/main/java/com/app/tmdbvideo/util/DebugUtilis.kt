package com.app.tmdbvideo.util

import android.util.Log
import com.app.tmdbvideo.BuildConfig

object LogcatUtils {

    var _charLimit = 2000

    @JvmStatic
    fun v(tag: String?, message: String): Int {
        // If the message is less than the limit just show
        if(BuildConfig.DEBUG){
            if (message.length < _charLimit) {
                return Log.v(tag, message)
            }
            val sections = message.length / _charLimit
            for (i in 0..sections) {
                val max = _charLimit * (i + 1)
                if (max >= message.length) {
                    Log.v(tag, message.substring(_charLimit * i))
                } else {
                    Log.v(tag, message.substring(_charLimit * i, max))
                }
            }
        }
        return 1
    }

    @JvmStatic
    fun e(tag: String?, message: String): Int {
        // If the message is less than the limit just show
        if(BuildConfig.DEBUG){
            if (message.length < _charLimit) {
                return Log.e(tag, message)
            }
            val sections = message.length / _charLimit
            for (i in 0..sections) {
                val max = _charLimit * (i + 1)
                if (max >= message.length) {
                    Log.e(tag, message.substring(_charLimit * i))
                } else {
                    Log.e(tag, message.substring(_charLimit * i, max))
                }
            }
        }
        return 1
    }
}