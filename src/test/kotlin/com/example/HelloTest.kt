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
    fun `hello test no optional parameters`() {
        assertEquals(Response(OK).body("Hello"), app(Request(GET, "/hello")))
    }

    @Test
    fun `hello test with name parameter`() {
        assertEquals(Response(OK).body("Hello Margot"), app(Request(GET, "/hello?name=Margot")))
    }
    @Test
    fun `optional parameter throws BAD REQUEST if not string`() {
        assertEquals(Response(Status.BAD_REQUEST).body("Invalid name"), app(Request(GET, "/hello?name=123")))
    }

}
