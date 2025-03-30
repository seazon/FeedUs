package com.seazon.feedus

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val language: String = "en" // TODO this language should get from iOS
}

actual fun getPlatform(): Platform = IOSPlatform()