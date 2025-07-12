package com.seazon.feedme.platform

import io.ktor.utils.io.core.toByteArray
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.stringByAppendingString
import platform.Security.*
import kotlin.experimental.and

actual object Crypto {
    actual fun md5(input: String): String {
        return input
//        val data = input.toByteArray().toNSData()
//        val digest = ByteArray(CC_MD5_DIGEST_LENGTH.toInt())
//
//        data.usePinned {
//            CC_MD5(it.addressOf(0), data.length.toUInt(), digest.refTo(0))
//        }
//
//        return bytesToHex(digest)
    }

//    private fun bytesToHex(bytes: ByteArray): String {
//        val result = StringBuilder()
//        bytes.forEach {
//            val i = it.toInt() and 0xff
//            if (i < 0x10) {
//                result.append("0")
//            }
//            result.append(Integer.toHexString(i))
//        }
//        return result.toString()
//    }
//
//    // 扩展函数：将 Kotlin ByteArray 转换为 NSData
//    private fun ByteArray.toNSData(): NSData {
//        return NSData.create(bytes = this, length = this.size.toULong())
//    }
//
//    // 安全释放内存的扩展函数
//    private inline fun <T> NSData.usePinned(block: (ByteArray) -> T): T {
//        return withUnsafeMutablePointerToElements { ptr ->
//            block(ptr.pointed)
//        }
//    }
}