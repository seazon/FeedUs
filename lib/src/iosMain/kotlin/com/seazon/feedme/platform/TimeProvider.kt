package com.seazon.feedme.platform

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual object TimeProvider {
    actual fun currentTimeMillis(): Long =
        (NSDate().timeIntervalSince1970 * 1000).toLong()
}