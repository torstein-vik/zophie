package io.zophie.connection

import io.zophie.api.event._

import org.json4s._
import org.json4s.native.JsonMethods._

// Implementation of JSON, to be passed to JSON converter 
trait EventDataJSONConverter[T <: EventData] {
    def toJSON   (data : T) : JObject
    def fromJSON (data : JObject) : T
}

trait EventDataJSONConverterRegistry {
    def getEvent (name : String) : Event
    def getEventDataJSONConverter (event : Event) : EventDataJSONConverter[event.eventData]
}

class JSONEventConverter(implicit edconvreg : EventDataJSONConverterRegistry) extends EventConverter[String] {

    def toData (event : EventDataComposite) : String = {
        val eventDataJSONConverter = edconvreg.getEventDataJSONConverter(event.event)
        
        val eventstring = event.event.name
        val data = eventDataJSONConverter.toJSON(event.data)
        
        val json = {
            import org.json4s.JsonDSL._
            ("event" -> eventstring) ~
            ("data" -> data)
        }
        
        return compact(render(json))
    }

    // Parses JSON string into Event-EventData pair
    def fromData (data  : String) : EventDataComposite = {

        // Just return placeholder
        return new EventDataComposite(Placeholder)(NoEventData)
    }
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
    
    // Implicit version, so it doesn't need to be passed
    implicit case object jsonc = JSONEventConverter
}
