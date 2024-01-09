package com.example

import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.hamkrest.hasHeader
import com.natpryce.hamkrest.equalTo
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus

class HelloTest {

    @Nested
    inner class HelloPathTests {
        @Test
        fun `hello route with no optional parameters returns generic greeting`() {
            assertEquals(Response(OK).body("Hello"), app(Request(GET, "/hello")))
        }

        @Test
        fun `hello route with name parameter returns personalised greeting `() {
            assertEquals(Response(OK).body("Hello Margot"), app(Request(GET, "/hello?name=Margot")))
        }

//        👇🏻 test no longer applicable to project requirements
//        @Test
//        fun `hello route optional name parameter throws BAD REQUEST if not string of alphabetical characters`() {
//            assertEquals(Response(Status.BAD_REQUEST).body("Invalid name"), app(Request(GET, "en-US/hello?name=123")))
//        }

//        👇🏻 test no longer applicable to project requirements
//        @Test
//        fun `path variable for language allows greeting to be in multiple languages`() {
//            assertEquals(Response(OK).body("Bonjour Amelie"), app(Request(GET, "fr-FR/hello?name=Amelie")))
//            assertEquals(Response(OK).body("Ciao Marco"), app(Request(GET, "it-IT/hello?name=Marco")))
//            assertEquals(Response(OK).body("G'day Skippy"), app(Request(GET, "en-AU/hello?name=Skippy")))
//            assertEquals(Response(OK).body("Good day Jeeves"), app(Request(GET, "en-GB/hello?name=Jeeves")))
//        }

//        👇🏻 test no longer applicable to project requirements
//        @Test
//        fun `an unsupported language receives a greeting in English`() {
//            assertEquals(Response(OK).body("Hello Amelie"), app(Request(GET, "ru-RU/hello?name=Amelie")))
//        }

    }

    @Nested
    inner class Echo_HeadersPathTest {
        @Test
        fun `returns 200-OK on the root URI on a valid request`() {
            assertEquals(Response(OK), app(Request(GET, "/echo_headers")))
        }

        @Test
        fun `when client supports json responses, header is returned in the body as json`() {
            val expected: String = listOf( // could be written in json
                "Accept-encoding" to "gzip".asJsonValue(),
                "Accept" to "*/*".asJsonValue(),
                "Connection" to "keep-alive".asJsonValue(),
                "Host" to "localhost:3000".asJsonValue()
            ).asJsonObject().toString()

            val actual = app(
                Request(Method.GET, "/echo_headers")
                    .header("Accept-encoding", "gzip")
                    .header("Accept", "*/*")
                    .header("Connection", "keep-alive")
                    .header("Host", "localhost:3000")
            ).body.toString()

            assertEquals(expected, actual)
        }

        @Test
        fun `when client does not support json responses, header is returned in the body a list in string format`() {
            val expected: String = listOf(
                "Accept-encoding: gzip",
                "Accept: text",
                "Connection: keep-alive",
                "Host: localhost:3000"
            ).joinToString("\n")

            val actual = app(Request(Method.GET, "/echo_headers")
                .header("Accept-encoding", "gzip")
                .header("Accept", "text")
                .header("Connection","keep-alive")
                .header("Host","localhost:3000")
            ).body.toString()

            assertEquals(expected, actual)

        }

        // no body in response
        // response headers all start with prefix
        // response headers are the same as request headers

        @Test
        fun `when query parameter 'as_response_headers_with_prefix' is used, no body in Response`() {
            val expected: String = ""
            val response = app(Request(Method.GET,"/echo_headers?as_response_headers_with_prefix=X-Echo-" ))
            val actual: String = response.body.toString()

            assertEquals(expected, actual)
        }

        @Test
        fun `when query parameter 'as_response_headers_with_prefix' is 'X-Echo-', 'X-Echo-' is added as a prefix to each header key`() {

            val response = app(Request(Method.GET,"/echo_headers?as_response_headers_with_prefix=X-Echo-" )
                .header("Host","localhost:3000")
                .header("Accept-encoding", "gzip")
                .header("Accept", "text")
                .header("Connection","keep-alive"))

            assertThat(Response(OK), hasStatus(OK))
            assertThat(response, hasHeader("X-Echo-Host","localhost:3000"))
            assertThat(response, hasHeader("X-Echo-Accept-encoding","gzip"))
            assertThat(response, hasHeader("X-Echo-Accept","text"))
            assertThat(response, hasHeader("X-Echo-Connection","keep-alive"))
        }



    }










}
