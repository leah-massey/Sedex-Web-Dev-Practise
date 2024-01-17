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
    fun echoHeadersWhenJsonSupported(data: Map<String, String> ): Map<String, String> {
        val dataAsList: List<Pair<String, String?>> = data.toList()

        // construct req with "Accept" header set to "application/json". Add all pairs from dataAsList as request headers
        val request = Request(GET, "${baseURL}/echo_headers").header("Accept", "application/json").headers(dataAsList)

        // make request (returned in json format)
        val response: Response = client(request)

        // convert json response to Map<String, String> and return it.
        val regex = Regex("\"(.*?)\":\"(.*?)\"")
        val matches = regex.findAll(response.bodyString())
        val responseBodyAsMap: Map<String, String> = matches.associate { it.groupValues[1] to it.groupValues[2] }
        return responseBodyAsMap
    }
    fun echoHeadersWhenJsonNotSupported(data: Map<String, String>): Map<String, String> {
        val dataAsList: List<Pair<String, String>> = data.toList()

        // construct req with header not set to application json. Add all pairs from dataAsList as request headers
        var request: Request = Request(GET, "${baseURL}/echo_headers").header("Accept", "text/html").headers(dataAsList)

        // make request (returned as a string in list format)
        val responseBody: String = client(request).bodyString()

        //convert response to Map<String, String> and return it
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

        // construct req with prefix query. Add dataAsList as headers
        val request: Request = Request(GET, "${baseURL}/echo_headers?as_response_headers_with_prefix=X-Echo-").headers(dataAsList)

        // make request and get headers
        val responseHeaders: String = client(request).headers.toString()

        //convert response to Map<String, String> and return it
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

    // below is just some testing to see if the code is performing as expected 👇🏻.
    helloClient.sayHello()

    println("Response for json supporting client")
    println(helloClient.echoHeadersWhenJsonSupported(mapOf("testkey" to "testvalue")))

    println("Response for non json supporting client")
    println(helloClient.echoHeadersWhenJsonNotSupported(mapOf("testkey" to "testvalue")))

    println("Response for request with prefix query")
    println(helloClient.echoHeadersWithPrefix(mapOf("testkey" to "testvalue")))

}
