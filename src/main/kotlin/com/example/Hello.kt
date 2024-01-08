package com.example

import com.fasterxml.jackson.databind.JsonNode
import netscape.javascript.JSObject
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.format.Jackson.asJsonArray
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.format.Jackson.auto


val app: HttpHandler = routes(
    "{lang:[a-zA-Z-]+}/hello" bind GET to { req: Request ->

        val lang: String = req.path("lang") ?: "en-US"
        val name: String? = req.query("name") //  query will look something like this: ?name=Jane

        val greeting = when (lang) {
            "en-US" -> "Hello"
            "fr-FR" -> "Bonjour"
            "en-AU" -> "G'day"
            "it-IT" -> "Ciao"
            "en-GB" -> "Good day"
            else -> "Hello"
        }

        if (name == null) {
            // No name is declared
            Response(OK).body("$greeting")
        } else if (name.matches(Regex("[a-zA-Z]+"))) {
            // Name is a string of alphabetic characters
            Response(OK).body("$greeting $name")
        } else {
            // Name is incorrectly formatted eg - a number or symbol
            Response(BAD_REQUEST).body("Invalid name")
        }
    }
    ,
    "/echo_headers" bind GET to {req: Request ->

        val headers: Headers = req.headers
        val headersAsStringList: String = headers.map{"${it.first}: ${it.second}"}.joinToString("\n")
        val headersAsJson: JsonNode = headers.toMap().asJsonObject()
        val prefix: String? = req.query("as_response_headers_with_prefix")

        val prefixedResponseHeaders: Headers = headers.toMap().mapKeys { "${prefix}${it.key}" }.entries.map{it.key to it.value} // get headers to map form and update with prefix then turn back to header

        if (prefix !== null ) {
            // respond with the prefixed response headers
            Response(OK).headers(prefixedResponseHeaders)
        } else {
            // work out if client supports json responses and return body accordingly
            val acceptHeaderValue: String? = headers.find { it.first == "Accept" }?.second // this returns the value part of the accept key or null
            if ( acceptHeaderValue != null && ("json" in acceptHeaderValue || "*/*" in acceptHeaderValue)) {
                // json responses supported
                Response(OK).body("$headersAsJson")
            } else {
                // json responses not supported
                Response(OK).body(headersAsStringList)
            }
        }
    }
)
fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(3000)).start()

    println("Server started on " + server.port())
}
