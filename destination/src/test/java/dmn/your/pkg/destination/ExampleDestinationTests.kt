package dmn.your.pkg.destination

import com.segment.analytics.kotlin.core.*
import com.segment.analytics.kotlin.core.platform.Plugin
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class ExampleDestinationTests {

    private val exampleDestination: ExampleDestination = ExampleDestination() /* init */

    @Test
    fun `settings are updated correctly`() {
        // An example settings blob
        val settingsBlob: Settings = Json.decodeFromString("""
            {
              "integrations": {
                "FooDestination": {
                 "configA": true,
                 "configB": 10,
                 "configC": "value3"
              }
            }
        """.trimIndent())
        exampleDestination.update(settingsBlob, Plugin.UpdateType.Initial)

        /* assertions about config */
        assertNotNull(exampleDestination.settings)
        with(exampleDestination.settings!!) {
            assertTrue(configA)
            assertEquals(10, configB)
            assertEquals("value3", configC)
        }

        TODO("verify any side-effects via mocks")
    }

    @Test
    fun `track is handled correctly`() {
        val sampleEvent = TrackEvent(
            event = "Product Clicked",
            properties = buildJsonObject { put("Item Name", "Biscuits") }
        ).apply {
            messageId = "qwerty-1234"
            anonymousId = "anonId"
            integrations = emptyJsonObject
            context = emptyJsonObject
            timestamp = "2021-07-13T00:59:09"
        }
        val trackEvent = exampleDestination.track(sampleEvent)

        /* assertions about new event */
        assertNotNull(trackEvent)
        with(trackEvent!! as TrackEvent) {
            assertEquals("PRODUCT_CLICKED", event)
        }

        TODO("verify any side-effects via mocks")
    }

    @Test
    fun `identify is handled correctly`() {
        val sampleEvent = IdentifyEvent(
            userId = "abc-123",
            traits = buildJsonObject { put("email", "123@abc.com") }
        ).apply {
            messageId = "qwerty-1234"
            anonymousId = "anonId"
            integrations = emptyJsonObject
            context = emptyJsonObject
            timestamp = "2021-07-13T00:59:09"
        }
        val identifyEvent = exampleDestination.identify(sampleEvent)

        /* assertions about new event */
        assertNotNull(identifyEvent)
        with(identifyEvent!! as IdentifyEvent) {
            assertEquals("abc-123", userId)
        }

        TODO("verify any side-effects via mocks")
    }

    @Test
    fun `group is handled correctly`() {
        val sampleEvent = GroupEvent(
            groupId = "grp-123",
            traits = buildJsonObject { put("company", "abc") }
        ).apply {
            messageId = "qwerty-1234"
            anonymousId = "anonId"
            integrations = emptyJsonObject
            context = emptyJsonObject
            timestamp = "2021-07-13T00:59:09"
        }
        val groupEvent = exampleDestination.group(sampleEvent)

        /* assertions about new event */
        assertNotNull(groupEvent)
        with(groupEvent!! as GroupEvent) {
            assertEquals("grp-123", groupId)
        }

        TODO("verify any side-effects via mocks")
    }

    @Test
    fun `screen is handled correctly`() {
        val sampleEvent = ScreenEvent(
            name = "LoginFragment",
            properties = buildJsonObject {
                put("startup", false)
                put("parent", "MainActivity")
            },
            category = "signup_flow"
        ).apply {
            messageId = "qwerty-1234"
            anonymousId = "anonId"
            integrations = emptyJsonObject
            context = emptyJsonObject
            timestamp = "2021-07-13T00:59:09"
        }
        val screenEvent = exampleDestination.screen(sampleEvent)

        /* assertions about new event */
        assertNotNull(screenEvent)
        with(screenEvent!! as ScreenEvent) {
            assertEquals("LoginFragment", name)
        }

        TODO("verify any side-effects via mocks")
    }
}