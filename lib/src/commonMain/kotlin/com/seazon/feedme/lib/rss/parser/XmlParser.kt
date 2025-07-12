package com.seazon.feedme.lib.rss.parser

//import java.io.InputStream
//import java.util.*

class XmlParser {

//    private val stack: Stack<String> = Stack()

//    fun parse(xml: InputStream, listener: ParserListener) {
//        // TODO how to implement in kmp?
////        stack.clear()
////        val pullParser = Xml.newPullParser()
////        pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
////        pullParser.setInput(xml, "UTF-8")
////        var event = pullParser.eventType
////        while (event != XmlPullParser.END_DOCUMENT) {
////            if (listener.stop(getPath(), event)) {
////                break
////            }
////            when (event) {
////                XmlPullParser.START_TAG -> {
////                    stack.push(pullParser.name)
////                    listener.onTag(getPath())
////                    if (pullParser.attributeCount > 0) {
////                        val attrs = (0 until pullParser.attributeCount).map { i ->
////                            pullParser.getAttributeName(i) to pullParser.getAttributeValue(i)
////                        }.toMap()
////                        listener.onAttr(getPath(), attrs)
////                    }
////                }
////                XmlPullParser.TEXT -> if (pullParser.text.isNullOrBlank().not()) listener.onText(getPath(), pullParser.text)
////                XmlPullParser.END_TAG -> stack.pop()
////            }
////            event = pullParser.next()
////        }
//    }
//
//    private fun getPath() = stack.joinToString(separator = "/")

    interface ParserListener {
        fun onTag(path: String)
        fun onAttr(path: String, attrs: Map<String, String>)
        fun onText(path: String, text: String)
        fun stop(path: String, eventType: Int): Boolean
    }
}