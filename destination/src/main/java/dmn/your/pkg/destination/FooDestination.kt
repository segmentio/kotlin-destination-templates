package dmn.your.pkg.destination

import com.segment.analytics.kotlin.core.*
import com.segment.analytics.kotlin.core.platform.DestinationPlugin
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import java.util.*

/** NEED TO MOVE OUT **/
inline fun <reified T : Any> Settings.destinationSettings(
    name: String,
    strategy: DeserializationStrategy<T> = Json.serializersModule.serializer()
): T? {
    val integrationData = integrations[name]?.jsonObject ?: return null
    val typedSettings = Json.decodeFromJsonElement(strategy, integrationData)
    return typedSettings
}

fun Settings.isDestinationEnabled(name: String): Boolean {
    return integrations.containsKey(name)
}

fun JsonObject.transformKeys(transform: (String) -> String): JsonObject {
    return JsonObject(this.mapKeys { transform(it.key) })
}

fun JsonObject.transformValues(transform: (JsonElement) -> JsonElement): JsonObject {
    return JsonObject(this.mapValues { transform(it.value) })
}
/** END MOVE OUT **/

@Serializable
data class FooSettings(
    var configA: Boolean,
    var configB: Int,
    var configC: String
)


// Rules for transforming a track event name
private fun eventNameMapper(event: String): String {
    return when (event) {
        "Product Added" -> "ADD_TO_CART" /* transformation 1 */
        "Product Clicked" -> "PRODUCT_CLICKED" /* transformation 2 */
        else -> event /* default value */
    }
}

// Rules for transforming property keys
private fun propertyKeyMapper(key: String): String {
    return key.toLowerCase(Locale.getDefault())
        .replace(' ', '_')
        .replace('-', '_')
}

class FooDestination : DestinationPlugin() {

    internal var settings: FooSettings? = null

    override val name: String = TODO()

    override fun setup(analytics: Analytics) {
        super.setup(analytics)
        TODO("initialize partner SDK here")
    }

    override fun update(settings: Settings) {
        super.update(settings)
        if (settings.isDestinationEnabled(name)) {
            this.settings = settings.destinationSettings(name)
            TODO("setup your destination using ${this.settings}")
        } else {
            TODO("something appropriate")
        }
    }

    override fun group(payload: GroupEvent): BaseEvent? {
        val groupId = payload.groupId
        val traits = payload.traits
        TODO("something with $groupId & $traits in partner SDK")
        return payload
    }

    override fun identify(payload: IdentifyEvent): BaseEvent? {
        val userId: String = payload.userId
        val traits: JsonObject = payload.traits
        TODO("something with $userId & $traits in partner SDK")
        return payload
    }

    override fun screen(payload: ScreenEvent): BaseEvent? {
        val screenName = payload.name
        val properties = payload.properties
        TODO("something with $screenName & $properties in partner SDK")
        return payload
    }

    override fun track(payload: TrackEvent): BaseEvent? {
        // Example of transforming event property keys
        payload.event = eventNameMapper(payload.event)
        payload.properties = payload.properties.transformKeys(::propertyKeyMapper)
        TODO("something with ${payload.event} & ${payload.properties} in partner SDK")
        return payload
    }

    override fun reset() {
        TODO("partnerSDK reset")
    }
}