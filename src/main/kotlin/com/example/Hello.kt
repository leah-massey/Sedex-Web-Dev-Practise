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
        val name: String = req.query("name") ?: ""

        val greeting = when (lang) {
            "en-US" -> "Hello"
            "fr-FR" -> "Bonjour"
            "en-AU" -> "G'day"
            "it-IT" -> "Ciao"
            "en-GB" -> "Good day"
            else -> "Hello"
        }

        if (name == "") {
            Response(OK).body("$greeting")
        } else if (name.matches(Regex("[a-zA-Z]+"))) {
            // Name is a string of characters (alphabetic) or not declared
            Response(OK).body("$greeting $name")
        } else {
            Response(BAD_REQUEST).body("Invalid name")
        }
    }
    ,

    "/echo_headers" bind GET to {req: Request ->

        val headers = req.headers

        val headersAsList = headers.map{"${it.first}: ${it.second}"}.joinToString("\n")
        val headersAsJson: JsonNode = headers.toMap().asJsonObject()

        val anyMediaType  = "*/*"
        val jsonType = "json"

        val value = headers.find { it.first == "Accept" }?.second

        if (value != null && jsonType in value) {
            println("Value supports json responses")
            Response(OK).body("this is JSON format: $headersAsJson")
        } else if (value != null && anyMediaType in value) {
            println("Value supports all media type responses")
            Response(OK).body("this is JSON format: $headersAsJson")
        } else {
            println("Value does not support json responses")
            Response(OK).body(" this is list format: $headersAsList")
        }
    }
)
fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(3000)).start()

    println("Server started on " + server.port())
}
