package com.seazon.feedme.lib.network

import com.seazon.feedme.lib.utils.LogUtils
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.prepareGet
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.Json

class HttpManager {

    companion object {

        const val IMAGE_MAX_SIZE = 1024 * 1024 * 15
        const val EXT_SVG = "svg"

        private var host: String? = null
        private var port: Int = 0
        private var ua: String? = null

        val client = HttpClient {
            engine {
                // TODO set proxy
//                if (!host.isNullOrEmpty() && port != 0) {
//                    proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
//                }
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
                connectTimeoutMillis = HttpUtils.CONNECT_TIMEOUT
                requestTimeoutMillis = HttpUtils.REQUEST_TIMEOUT
                socketTimeoutMillis = HttpUtils.SOCKET_TIMEOUT
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
            xFormParams: List<NameValuePair>? = null,
            body: String? = null,
        ): HttpResponse {
            return runBlocking {
                request(httpMethod, url, params, headers, xFormParams, body, true)
            }
        }

        fun requestBlock(
            httpMethod: HttpMethod,
            url: String,
            params: List<NameValuePair>? = null,
            headers: Map<String, String>? = null,
            xFormParams: List<NameValuePair>? = null,
            body: String? = null,
            json: Boolean = true,
        ): HttpResponse {
            return runBlocking {
                request(httpMethod, url, params, headers, xFormParams, body, json)
            }
        }

        suspend fun request(
            httpMethod: HttpMethod,
            url: String,
            params: List<NameValuePair>? = null,
            headers: Map<String, String>? = null,
            xFormParams: List<NameValuePair>? = null,
            body: String? = null,
            json: Boolean = true,
        ): HttpResponse {
            LogUtils.debug("[$httpMethod]${url}")
            if (!params.isNullOrEmpty()) {
                LogUtils.debug(
                    "params: ${
                        params.joinToString(
                            separator = "&",
                            transform = { "${it.name}=${it.value}" })
                    }"
                )
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
            val headersNew = headers.orEmpty().toMutableMap()
            if (!headersNew.contains(HttpUtils.HTTP_HEADERS_ACCEPT)) {
                headersNew[HttpUtils.HTTP_HEADERS_ACCEPT] = HttpUtils.HTTP_HEADERS_ACCEPT_DEFAULT_VALUE
            }
            if (!xFormParams.isNullOrEmpty()) {
                LogUtils.debug(
                    "xFormParams: ${
                        xFormParams.joinToString(
                            separator = "&",
                            transform = { "${it.name}=${it.value}" })
                    }"
                )
            }
            if (!body.isNullOrEmpty()) {
                LogUtils.debug("body: $body")
            }

            val response = client.request(url) {
                method = when (httpMethod) {
                    HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
                    HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
                    HttpMethod.POST -> io.ktor.http.HttpMethod.Post
                    HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
                    else -> io.ktor.http.HttpMethod.Get
                }
                url {
                    params?.forEach {
                        parameters.append(it.name, it.value)
                    }
                }
                if (!body.isNullOrEmpty()) {
                    setBody(body)
                } else if (!xFormParams.isNullOrEmpty()) {
                    val formParameters = Parameters.build {
                        xFormParams.forEach {
                            append(it.name, it.value)
                        }
                    }
                    setBody(FormDataContent(formParameters))
                }
                headers {
                    headersNew.forEach {
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
            xFormParams: List<NameValuePair>? = null,
            body: String? = null,
            json: Boolean = true,
        ): SimpleResponse {
            val a = request(httpMethod, url, params, headers, xFormParams, body, json)
            val b = SimpleResponse(a.status.value, a.bodyAsText())
            LogUtils.debug("response code: ${b.code}")
            LogUtils.debug("response body: ${b.body}")
            return b
        }

//        suspend fun stream(
//            httpMethod: HttpMethod,
//            url: String,
//            params: List<NameValuePair>? = null,
//            headers: Map<String, String>? = null,
//            body: String? = null,
//            json: Boolean = true,
//        ): InputStream {
//            return request(httpMethod, url, params, headers, body, json).bodyAsChannel().toInputStream()
//        }

        fun HttpResponse.contentLength(): Long {
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
            return headers[HttpUtils.HTTP_HEADERS_CONTENT_TYPE]
        }

        fun HttpResponse.imageType(): String? {
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

        suspend fun saveFile(url: String, savePath: String) = withContext(Dispatchers.IO) {
            try {
                val path = Path(savePath)
                client.prepareGet(url).execute { response ->
                    when (response.status.value) {
                        200 -> writeChannelToFile(response.bodyAsChannel(), path)
                        403 -> downloadWithReferer(url, path)
                        else -> throw SyncIgnoreException("Request failed, code: ${response.status.value}")
                    }
                }
            } catch (e: Exception) {
                throw SyncIgnoreException("Request failed, error:${e.message}")
            }
        }

        private suspend fun writeChannelToFile(channel: ByteReadChannel, path: Path) {
            SystemFileSystem.sink(path).buffered().use { sink ->
                val buffer = ByteArray(8192)
                while (!channel.isClosedForRead) {
                    val size = channel.readAvailable(buffer)
                    if (size > 0) {
                        sink.write(buffer, 0, size)
                    }
                }
                sink.flush()
                sink.close()
            }
        }

        private suspend fun downloadWithReferer(url: String, path: Path) {
            client.prepareGet(url) {
                header("Referer", "no-referer")
            }.execute { response ->
                if (response.status.value == 200) {
                    writeChannelToFile(response.bodyAsChannel(), path)
                } else {
                    throw IllegalStateException("Request failed for 403 case, code: ${response.status.value}")
                }
            }
        }
    }
}
