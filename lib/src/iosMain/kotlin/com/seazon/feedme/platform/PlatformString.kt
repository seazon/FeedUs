package com.seazon.feedme.platform

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual object PlatformString {

    actual fun format(format: String, vararg args: Any?): String {
        var returnString = ""
        val regEx = "%[\\d|.]*[sdf]|[%]".toRegex()
        val singleFormats = regEx.findAll(format).map {
            it.groupValues.first()
        }.asSequence().toList()
        val newStrings = format.split(regEx)
        for (i in 0 until args.count()) {
            val arg = args[i]
            returnString += when (arg) {
                is Double -> NSString.stringWithFormat(
                    newStrings[i] + singleFormats[i],
                    args[i] as Double
                )

                is Int -> NSString.stringWithFormat(
                    newStrings[i] + singleFormats[i],
                    args[i] as Int
                )

                else -> NSString.stringWithFormat(newStrings[i] + "%@", args[i])
            }
        }

        return returnString
    }
}