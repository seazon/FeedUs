package com.seazon.feedme.lib.network

import io.ktor.http.decodeURLQueryComponent
import io.ktor.http.encodeURLParameter

//import java.io.InputStream
//import java.io.UnsupportedEncodingException
//import java.lang.IllegalArgumentException
//import java.lang.StringBuilder
//import java.net.URLEncoder
//import java.util.ArrayList
//import java.util.Locale

object HttpUtils {
    const val DEFAULT_CHARSET: String = "UTF-8"
    const val CONNECT_TIMEOUT: Int = 15000
    const val READ_TIMEOUT: Int = 20000
    const val BUFFER_SIZE: Int = 4096
    private val CHARSET_CHARACTER =
        arrayOf("content=\"text/html;charset=", "content='text/html;charset=", "charset=\"")
    private val CHARSET_CHARACTER_SPECIAL_INDEX = 1
    private const val PARAMETER_SEPARATOR = "&"
    private const val NAME_VALUE_SEPARATOR = "="
    val PAGE_MAX_SIZE: Int = 1024 * 1024 * 3

//    /**
//     * 此方法在使用完InputStream后会关闭它。
//     *
//     * @param is
//     * @param charset
//     * @param checked
//     * @return
//     * @throws Exception
//     */
//    @Throws(Exception::class)
//    fun toString(`is`: InputStream?, charset: String?, checked: Boolean): String {
//        var charset = charset
//        var checked = checked
//        if (checked) {
////            LogUtils.debug("charset:" + charset)
//        }
//        val byteArrayList: MutableList<ByteArray> = ArrayList<ByteArray>()
//
//        try {
//            var realSize = -1
//            var totalSize = 0
//            val buffer = ByteArray(BUFFER_SIZE)
//            var data: ByteArray?
//            while ((`is`!!.read(buffer).also { realSize = it }) != -1) {
//                totalSize += realSize
//                if (totalSize > PAGE_MAX_SIZE) {
//                    val info = "this page is too large, size:" + totalSize + " byte"
////                    LogUtils.warn(info)
//                    return info
//                }
//
//                data = ByteArray(realSize)
//                System.arraycopy(buffer, 0, data, 0, realSize)
//                byteArrayList.add(data)
//
//                if (!checked) {
//                    charset = getCharset(String(buffer, charset(charset!!)))
//                    if (charset == null) {
//                        charset = DEFAULT_CHARSET
//                    } else if (DEFAULT_CHARSET == charset) {
//                        checked = true
//                    } else {
////                        LogUtils.debug("charset:" + charset)
//                        checked = true
//                        continue
//                    }
//                }
//            }
//
//            return String(sysCopy(totalSize, byteArrayList), charset(charset!!))
//        } finally {
//            if (`is` != null) {
//                `is`.close()
//            }
//        }
//    }

//    /**
//     * 将多个byte数组合并成一个
//     *
//     * @param totalSize
//     * @param byteArrayList
//     * @return
//     */
//    private fun sysCopy(totalSize: Int, byteArrayList: MutableList<ByteArray>): ByteArray {
//        val destArray = ByteArray(totalSize)
//        var index = 0
//        for (byteArray in byteArrayList) {
//            val realSize = byteArray.size
//            System.arraycopy(byteArray, 0, destArray, index, realSize)
//            index += realSize
//        }
//        return destArray
//    }

//    //    @SuppressLint("DefaultLocale")
//    fun getCharset(s: String): String? {
//        var s = s
//        s = s.replace(" ".toRegex(), "")
//        for (i in CHARSET_CHARACTER.indices) {
//            if (s.contains(CHARSET_CHARACTER[i])) {
//                val start = s.indexOf(CHARSET_CHARACTER[i]) + CHARSET_CHARACTER[i]!!.length
//                val end = if (i == CHARSET_CHARACTER_SPECIAL_INDEX) s.indexOf("'", start) else s.indexOf("\"", start)
//                return s.substring(start, end).uppercase(Locale.getDefault())
//            }
//        }
//
//        return null
//    }

    /**
     * Returns a String that is suitable for use as an `application/x-www-form-urlencoded`
     * list of parameters in an HTTP PUT or HTTP POST.
     *
     * @param parameters The parameters to include.
     * @param encoding   The encoding to use.
     */
    fun format(
        parameters: List<out NameValuePair>,
        encoding: String?
    ): String {
        val result = StringBuilder()
        for (parameter in parameters) {
            val encodedName = parameter.name.urlEncode()
            val value = parameter.value
            val encodedValue = if (value != null) value.urlEncode() else ""
            if (result.length > 0) result.append(PARAMETER_SEPARATOR)
            result.append(encodedName)
            result.append(NAME_VALUE_SEPARATOR)
            result.append(encodedValue)
        }
        return result.toString()
    }
}

fun String.urlEncode(): String? {
    return encodeURLParameter()
}

fun String.urlDecode(): String? {
    return this.decodeURLQueryComponent()
}
