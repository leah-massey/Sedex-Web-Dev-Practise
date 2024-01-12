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
        val response: Response = client(request)
        println("this is response body")
        println(response.bodyString())

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

    // 3 functions each

}

fun main() {
    val baseUrl = "http://localhost:9000"
    val helloClient = HelloClient(baseUrl)

//    println("First is res.body, second is actual")
//    println(helloClient.echoHeadersForJson(mapOf("testkey" to "testvalue")))

    println("First is res.body, second is actual")
    println(helloClient.echoHeadersForNoJson(mapOf("testkey" to "testvalue")))

}
