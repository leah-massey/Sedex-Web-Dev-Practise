package com.example

import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.filter.DebuggingFilters.PrintResponse

class HelloClient(private val baseURL: String) {
    private val client: HttpHandler = JavaHttpClient() // establish a httpHandler for the client side
        fun sayHello(name: String? = null, language: String = "en-US"): String  = client(Request(GET, "${baseURL}/hello")
            .query("name", name)
            .header("Accept-language", language)
        ).bodyString()

//        fun echo_headers(prefix: String? = null, headers: List<Pair<String, String?>>?): Response = client(Request(GET, "${baseURL}/echo_headers")
//        .query("as_response_headers_with_prefix", prefix))
}

fun main() {
    val baseUrl = "http://localhost:9000"
    val helloClient = HelloClient(baseUrl)

    // Test - Call sayHello and print the response
    val helloResponse = helloClient.sayHello(name = "Cyril", language = "fr-FR")
    println("sayHello Response:")

}
