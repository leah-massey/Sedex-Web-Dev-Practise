package com.example

import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import org.http4k.hamkrest.hasBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HelloClientTest {
    val testClient = HelloClient("http://localhost:9000")

    @Nested
    inner class helloMethodTest {
        @Test
        fun `hello method with no parameters, returns 'Hello'`() {
            val expected: String = "Hello"
            val actual: String = testClient.sayHello()
            assertEquals(expected, actual)
        }

        @Test
        fun `hello method with name parameter, returns 'Hello $name'`() {
            val expected: String = "Hello Kimmy"
            val actual: String = testClient.sayHello(name = "Kimmy")
            assertEquals(expected, actual)
        }

        @Test
        fun `hello method with recognised language parameter, returns greeting in recognised language`() {
            val expected: String = "Bonjour Kimmy"
            val actual: String = testClient.sayHello(name = "Kimmy", language = "fr-FR")
            assertEquals(expected, actual)
        }

        @Test
        fun `hello method with unrecognised language parameter, returns greeting in en-US`() {
            val expected: String = "Hello Kimmy"
            val actual: String = testClient.sayHello(name = "Kimmy", language = "fr-F")
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class echo_headersPathTest {
        // not sure how to write tests here
    }
}