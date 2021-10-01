package dmn.your.pkg.destination

import com.segment.analytics.kotlin.core.*
import com.segment.analytics.kotlin.core.platform.DestinationPlugin
import com.segment.analytics.kotlin.core.platform.Plugin
import com.segment.analytics.kotlin.core.utilities.mapTransform
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class FooSettings(
    var configA: Boolean,
    var configB: Int,
    var configC: String
)

class ExampleDestination : DestinationPlugin() {

    internal var settings: FooSettings? = null

    override val key: String = TODO()

    override fun setup(analytics: Analytics) {
        super.setup(analytics)
    }

    override fun update(settings: Settings, type: Plugin.UpdateType) {
        super.update(settings, type)
        if (settings.isDestinationEnabled(key)) {
            this.settings = settings.destinationSettings(key)
            if (type == Plugin.UpdateType.Initial) {
                TODO("initialize singleton partner SDK here")
            }
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
        payload.event = EVENT_MAPPER[payload.event] ?: payload.event
        payload.properties = payload.properties.mapTransform(PROPERTY_MAPPER)
        TODO("something with ${payload.event} & ${payload.properties} in partner SDK")
        return payload
    }

    override fun reset() {
        TODO("partnerSDK reset")
    }

    companion object {
        // Rules for transforming a track event name
        private val PROPERTY_MAPPER: Map<String, String> = mapOf(
            "id" to "item_id",
            "name" to "item_name",
            "price" to "item_price",
            "quantity" to "item_quantity"
        )

        // Rules for transforming property keys
        private val EVENT_MAPPER: Map<String, String> = mapOf(
            "Product Added" to "added",
            "Products Searched" to "search"
        )
    }
}