package com.seazon.feedme.lib.network

import com.seazon.feedme.lib.utils.LogUtils
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Proxy

class HttpManager {

    companion object {

        public const val IMAGE_MAX_SIZE = 1024 * 1024 * 15
        private const val EXT_SVG = "svg"

        private var host: String? = null
        private var port: Int = 0
        private var ua: String? = null

        val client = HttpClient {
            engine {
                if (!host.isNullOrEmpty() && port != 0) {
                    proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
                }
            }
            expectSuccess = false
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
            }
            install(UserAgent) {
                if (!ua.isNullOrEmpty()) {
                    agent = ua.orEmpty()
                }
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 15000
                requestTimeoutMillis = 15000
                socketTimeoutMillis = 30000
            }
        }

        fun setProxy(host: String?, port: Int) {
            this.host = host
            this.port = port
        }

        fun setUserAgent(ua: String) {
            this.ua = ua
        }

        fun requestBlock(
            httpMethod: HttpMethod,
            url: String,
            params: List<NameValuePair>? = null,
            headers: Map<String, String>? = null,
            body: String? = null,
        ): HttpResponse {
            return runBlocking {
                request(httpMethod, url, params, headers, body, true)
            }
        }

        fun requestBlock(
            httpMethod: HttpMethod,
            url: String,
            params: List<NameValuePair>? = null,
            headers: Map<String, String>? = null,
            body: String? = null,
            json: Boolean = true,
        ): HttpResponse {
            return runBlocking {
                request(httpMethod, url, params, headers, body, json)
            }
        }

        suspend fun request(
            httpMethod: HttpMethod,
            url: String,
            params: List<NameValuePair>? = null,
            headers: Map<String, String>? = null,
            body: String? = null,
            json: Boolean = true,
        ): HttpResponse {
            LogUtils.debug("[$httpMethod]${url}")
            if (!params.isNullOrEmpty()) {
                LogUtils.debug("params: ${params.joinToString(separator = "&", transform = { "${it.name}=${it.value}" })}")
            }
            if (!headers.isNullOrEmpty()) {
                LogUtils.debug(
                    "headers: ${
                        headers.entries.joinToString(
                            separator = "&",
                            transform = { "${it.key}=${it.value}" })
                    }"
                )
            }
            if (!body.isNullOrEmpty()) {
                LogUtils.debug("body: $body")
            }

            val response = client.request(url) {
                method = when (httpMethod) {
                    HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
                    HttpMethod.POST -> io.ktor.http.HttpMethod.Post
                    HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
                    else -> io.ktor.http.HttpMethod.Get
                }
                url {
                    params?.forEach {
                        parameters.append(it.name, it.value)
                    }
                }
                if (body.isNullOrEmpty()) {
//                    val formParameters = Parameters.build {
//                        params?.forEach {
//                            append(it.name, it.value)
//                        }
//                    }
//                    setBody(FormDataContent(formParameters))
                } else {
                    setBody(body)
                }
                headers {
                    headers?.forEach {
                        append(it.key, it.value)
                    }
                }
            }
            return response
        }

        suspend fun requestWrap(
            httpMethod: HttpMethod,
            url: String,
            params: List<NameValuePair>? = null,
            headers: Map<String, String>? = null,
            body: String? = null,
            json: Boolean = true,
        ): SimpleResponse {
            val a = request(httpMethod, url, params, headers, body, json)
            val b = SimpleResponse(a.status.value, a.bodyAsText())
            LogUtils.debug("response code: ${b.code}")
            LogUtils.debug("response body: ${b.body}")
            return b
        }

        suspend fun stream(
            httpMethod: HttpMethod,
            url: String,
            params: List<NameValuePair>? = null,
            headers: Map<String, String>? = null,
            body: String? = null,
            json: Boolean = true,
        ): InputStream {
            return request(httpMethod, url, params, headers, body, json).bodyAsChannel().toInputStream()
        }

        private fun HttpResponse.contentLength(): Long {
            val contentLengthHeader = headers["Content-Length"]
            if (contentLengthHeader != null) {
                try {
                    return contentLengthHeader.toLong()
                } catch (e: NumberFormatException) {
                    LogUtils.error("Failed to parse Content-Length", e)
                    return -1
                }
            } else {
                LogUtils.error("Content-Length header is not present in the response.")
                return -1
            }
        }

        private fun HttpResponse.contentType(): String? {
            return headers["Content-Type"]
        }

        private fun HttpResponse.imageType(): String? {
            return contentType()?.let {
                it.split("/").lastOrNull()?.let {
                    if (it.contains(EXT_SVG) == true) {
                        EXT_SVG
                    } else {
                        it
                    }
                }
            }
        }

        suspend fun saveImage(
            url: String,
            path: String,
            filename: String,
            useContentType: Boolean,
            ignoreSmallIcon: Boolean,
            onSaveImage: (uriString: String, inputStream: InputStream) -> Unit,
        ): String {
            val response = client.get(url)
            if (response.status.value != 200) {
                throw SyncIgnoreException("Request failed, code:" + response.status.value)
            }

            val inputStream = response.bodyAsChannel().toInputStream()
            if (ignoreSmallIcon) {
                val size = response.contentLength()
                if (size != -1L && size < 128) {
                    throw SyncIgnoreException("Image is too small, lenght:$size")
                }
                if (size > IMAGE_MAX_SIZE) {
                    throw SyncIgnoreException("Image is too large, lenght:$size")
                }
            }

            var filename2 = filename
            if (useContentType) {
                response.imageType()?.let {
                    filename2 += ".$EXT_SVG"
                }
            }

            onSaveImage(path + filename, inputStream)
            LogUtils.debug("response, code:" + response.status.value)
            return filename
        }
    }
}
