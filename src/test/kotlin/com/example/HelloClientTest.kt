package com.example

import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.hamkrest.hasBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HelloClientTest {
    val testClient = HelloClient("http://localhost:9000")

        @Test
        fun `hello method with no parameters, returns 'Hello'`() {
            val expected: String = "Hello"
            val actual: String = testClient.sayHello()
            assertEquals(expected, actual)
        }

        @Test
        fun `hello method with name parameter, returns 'Hello $name'`() {
            val expected: String = "Hello Kimmy"
            val actual: String = testClient.sayHello(name="Kimmy")
            assertEquals(expected, actual)
        }
//
//        @Test
//        fun `hello method with recognised language parameter, returns greeting in recognised language`() {
//            val client = HelloClient(client)
//            val response = client.sayHello(name = "Kimmy", language = "fr-FR")
//            assertThat(response, hasBody("Bonjour Kimmy"))
//        }
//
//        @Test
//        fun `hello method with unrecognised language parameter, returns greeting in en-US`() {
//            val client = HelloClient(client)
//            val response = client.sayHello(name = "Kimmy", language = "fr-F")
//            assertThat(response, hasBody("Hello Kimmy"))
//        }







//        @Test
//        fun `echo_headers method returns request headers in json format when client supports json responses`() {
//            val client = Client(client)
//            val response = client.echo_headers()
//            assertThat(response, )
//        }

        @Test
        fun `echo_headers method with prefix argument returns an empty body`() {
            val client = HelloClient("http://localhost:9000")
//            val response = client.echo_headers(prefix = "X-Echo-")
//            assertThat(response, hasBody(""))
        }
//        @Test
//        fun `echo_headers method with prefix argument returns a non empty body in json format`() {
//            val client = Client(app)
//            val response = client.echo_headers(prefix = "X-Echo-")
//
//            assertThat(response, hasBody("X-Echo-test: yesy"))
//        }

}