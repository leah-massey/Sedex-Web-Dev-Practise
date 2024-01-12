package com.example

import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.lens.Header

class HelloClient(private val baseURL: String) {
    private val client: HttpHandler = JavaHttpClient() // establish a httpHandler for the client variable so that it can take a request and return a response
    fun sayHello(name: String? = null, language: String = "en-US"): String =

        client(Request(GET, "${baseURL}/hello")
            .query("name", name)
            .header("Accept-language", language)
            ).bodyString()
    fun echoHeadersForJson(data: Map<String, String> ): Map<String, String> {

        val dataAsList: List<Pair<String, String?>> = data.toList()
        // construct req with accept header set to application json
        // add all the pairs from data as request headers
        val request = Request(GET, "${baseURL}/echo_headers").header("Accept", "application/json").headers(dataAsList)

        // make request
        val response: Response = client(request)

        // convert response back to map and return it
        val regex: Regex = Regex("\"(.*?)\":\"(.*?)\"")
        val matches = regex.findAll(response.bodyString())
        val responseBodyAsMap: Map<String, String> = matches.associate { it.groupValues[1] to it.groupValues[2] }
        return responseBodyAsMap
    }
    fun echoHeadersForNoJson(data: Map<String, String>): Map<String, String> {

        val dataAsList: List<Pair<String, String>> = data.toList()

        // construct req with header not set to application json
        // add all the pairs from data as request headers
        var request: Request = Request(GET, "${baseURL}/echo_headers").header("Accept", "text/html").headers(dataAsList)

        // make request
        val responseBody: String = client(request).bodyString()

        //convert response back to map and return it
        val responseBodyAsMap: Map<String, String> = responseBody.split("\n")
            .associate { line ->
                val parts = line.split(": ")
                parts[0] to parts[1]
            }

        return responseBodyAsMap
    }
    fun echoHeadersWithPrefix(data: Map<String, String>): Map<String, String> {

        // convert data to list format
        val dataAsList = data.toList()

        // set up the request with prefix query
        // add data list as headers
        val request: Request = Request(GET, "${baseURL}/echo_headers?as_response_headers_with_prefix=X-Echo-").headers(dataAsList)

        // make request and get headers
        val responseHeaders: String = client(request).headers.toString()

        //convert response back to map and return it
        val regex = Regex("\\(([^,]+),\\s([^)]+)\\)")
        val matches = regex.findAll(responseHeaders)

        val responseHeaderAsMap: Map<String, String> = matches.associate {match ->
            val key = match.groupValues[1]
            val value = match.groupValues[2]
            key to value
        }
        return responseHeaderAsMap
    }

}

fun main() {
    val baseUrl = "http://localhost:9000"
    val helloClient = HelloClient(baseUrl)

    helloClient.sayHello()

    println("Response for json supporting client")
    println(helloClient.echoHeadersForJson(mapOf("testkey" to "testvalue")))

    println("Response for non json supporting client")
    println(helloClient.echoHeadersForNoJson(mapOf("testkey" to "testvalue")))

    println("Response for request with prefix query")
    println(helloClient.echoHeadersWithPrefix(mapOf("testkey" to "testvalue")))

}
