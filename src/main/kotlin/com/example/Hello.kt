package com.example

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val app: HttpHandler = routes(
    "/hello" bind GET to { req: Request ->
        val name = req.query("name") ?: ""

        if (name.matches(Regex("[a-zA-Z]+")) || name == "") {
            // Name is a string of characters (alphabetic)
            Response(OK).body("Hello $name")
        } else {
            Response(BAD_REQUEST).body("Invalid name")
        }
    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(3000)).start()

    println("Server started on " + server.port())
}
