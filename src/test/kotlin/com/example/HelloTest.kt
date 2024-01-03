package com.example

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloTest {

    @Test
    fun `hello route with no optional parameters returns generic greeting`() {
        assertEquals(Response(OK).body("Hello"), app(Request(GET, "/hello")))
    }

    @Test
    fun `hello route with name parameter returns personalised greeting `() {
        assertEquals(Response(OK).body("Hello Margot"), app(Request(GET, "/hello?name=Margot")))
    }
    @Test
    fun `hello route optional name parameter throws BAD REQUEST if not string`() {
        assertEquals(Response(Status.BAD_REQUEST).body("Invalid name"), app(Request(GET, "/hello?name=123")))
    }

}
