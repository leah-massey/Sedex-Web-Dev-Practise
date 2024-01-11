package com.example

import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET

class HelloClient(private val baseURL: String) {
    private val client: HttpHandler = JavaHttpClient() // establish a httpHandler for the client side
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
        val response: Response = client(request) // make the request with accept header set to application json
        println(response.bodyString())

        // convert response back to map and return it
        val regex: Regex = Regex("\"(.*?)\":\"(.*?)\"")
        val matches = regex.findAll(response.bodyString())
        val responseBodyAsMap: Map<String, String> = matches.associate { it.groupValues[1] to it.groupValues[2] }
        return responseBodyAsMap
    }

    // 3 functions each

}

fun main() {
    val baseUrl = "http://localhost:9000"
    val helloClient = HelloClient(baseUrl)

    println(helloClient.echoHeadersForJson(mapOf("testkey" to "testvalue")))

}
