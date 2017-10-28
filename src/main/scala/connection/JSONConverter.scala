package io.zophie.connection

import io.zophie.api.event._

import org.json4s._

// Implementation of JSON, to be passed to JSON converter 
trait EventDataJSONConverter[T <: EventData] {
    def toJSON   (data : T) : JObject
    def fromJSON (data : JObject) : T
}

trait EventDataJSONConverterRegistry {
    def getEvent (name : String) : Event
    def getEventDataJSONConverter (event : Event) : EventDataJSONConverter[event.eventData]
}

// A converter that creates JSON-data from an Event-Event data pair
package object JSONConverter {

    implicit object edjsonNoEventData extends EventDataJSONConverter[NoEventData.type] {
        override def toJSON   (data : NoEventData.type) = JObject()
        override def fromJSON (data : JObject) = NoEventData
    }
    
    implicit object EventDataJSONConverterRegistry {
        override def getEvent (name : String) = null
        override def getEventDataJSONConverter (event : Event) = null
    }
    
    class JSONEventConverter(implicit edconvreg : EventDataJSONConverterRegistry) extends EventConverter[String] {

        // Stringifies the JSON for the pair
        def toData (event : EventDataComposite) : String = {

            // For now just returns placeholder
            return """{"event":"placeholder","data":{}}"""
        }

        // Parses JSON string into Event-EventData pair
        def fromData (data  : String) : EventDataComposite = {

            // Just return placeholder
            return new EventDataComposite(Placeholder)(NoEventData)
        }
    }
    
    // Implicit version, so it doesn't need to be passed
    implicit case object jsonc = JSONEventConverter
}
