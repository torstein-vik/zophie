package io.zophie.connection

import io.zophie.api.event._

import org.json4s._

// Implementation of JSON, to be passed to JSON converter 
trait EventDataJSONConverter[T <: EventData] {
    def toJSON   (data : T) : JObject
    def fromJSON (data : JObject) : T
}

// A converter that creates JSON-data from an Event-Event data pair
package object JSONConverter {

    implicit object edjsonNoEventData extends EventDataJSONConverter[NoEventData.type] {
        override def toJSON   (data : NoEventData.type) = JObject()
        override def fromJSON (data : JObject) = NoEventData
    }
    
    // Implicit version, so it doesn't need to be passed
    implicit object jsonc extends EventConverter[String] {

        // For now, just a placeholder event
        case object Placeholder extends EventNoData("placeholder")

        // Stringifies the JSON for the pair
        def toData (event : EventDataComposite) : String = {

            // For now just returns placeholder
            return "placeholder"
        }

        // Parses JSON string into Event-EventData pair
        def fromData (data  : String) : EventDataComposite = {

            // Just return placeholder
            return new EventDataComposite(Placeholder)(NoEventData)
        }
    }
}
