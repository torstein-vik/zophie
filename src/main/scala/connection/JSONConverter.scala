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
    def getEvent[T <: EventData] (name : String) : Event[T]
    def getEventDataJSONConverter[T <: EventData] (event : Event[T]) : EventDataJSONConverter[T]
}

class JSONEventConverter(implicit edconvreg : EventDataJSONConverterRegistry) extends EventConverter[String] {

    def toData[T <: EventData] (event : EventDataComposite[T]) : String = {
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
    def fromData (data  : String) : EventDataComposite[EventData] = {

        // Just return placeholder
        case object Placeholder extends EventNoData("placeholder")
        return new EventDataComposite(Placeholder)(NoEventData)
    }
}

// A converter that creates JSON-data from an Event-Event data pair
package object JSONConverter {

    implicit object edjsonNoEventData extends EventDataJSONConverter[NoEventData.type] {
        override def toJSON   (data : NoEventData.type) = JObject()
        override def fromJSON (data : JObject) = NoEventData
    }
    
    implicit object mainEventJSONConverterRegistry extends EventDataJSONConverterRegistry {
        override def getEvent[T <: EventData] (name : String) = null
        override def getEventDataJSONConverter[T <: EventData] (event : Event[T]) = null
    }
        
    // Implicit version, so it doesn't need to be passed
    implicit case object jsonc extends JSONEventConverter {
        
    }
}
